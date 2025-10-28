package com.gummybear;

import com.gummybear.data.FileData;
import com.gummybear.data.FileDataManager;
import com.gummybear.data.FileDataTree;
import com.gummybear.desktop.ContextMenu;
import com.gummybear.desktop.Desktop;
import com.gummybear.desktop.explorer.Explorer;
import com.gummybear.desktop.terminal.Terminal;
import com.gummybear.desktop.icon.Icon;
import com.gummybear.desktop.icon.IconSize;
import com.gummybear.desktop.window.PersonalizeWindow;

import javafx.fxml.FXML;
// import javafx.scene.Group;
// import javafx.scene.Parent;
import javafx.scene.layout.Pane;
// import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
public class ContextMenuController {

    @Setter
    ContextMenu contextMenuInstance;

    @FXML
    private VBox contextMenuRoot;

    @FXML
    public void createNewFile() {
        FileData parentDirectory = contextMenuInstance.getFileData();

        FileData fileData = new FileData();
        fileData.setName("NewFile");
        fileData.setType("file");
        fileData.setText("");
        fileData.setPath(parentDirectory.getPath()+"/"+fileData.getName());
        fileData.setParent(parentDirectory);
        fileData.setContents(new ArrayList<>());
        FileDataManager manager = FileDataManager.getInstance();
        while(manager.createFile(parentDirectory, fileData).contains("Failed")){
            fileData.setName(fileData.getName()+"I")
                    .setPath("root/desktop/" + fileData.getName());
        }
        //new Icon(fileData);
    }

    @FXML
    public void createNewFolder() {
        FileData parentDirectory = contextMenuInstance.getFileData();

        FileData fileData = new FileData();
        fileData.setName("NewFolder");
        fileData.setType("folder");
        fileData.setText("");
        fileData.setPath(parentDirectory.getPath()+"/"+fileData.getName());
        fileData.setParent(parentDirectory);
        fileData.setContents(new ArrayList<>());
        FileDataManager manager = FileDataManager.getInstance();
        while(manager.createFile(parentDirectory, fileData).contains("Failed")){
            fileData.setName(fileData.getName()+"I")
                    .setPath("root/desktop/" + fileData.getName());
        }
        //new Icon(fileData);
    }

    @FXML
    public void personalizeBtn(){
        new PersonalizeWindow();
        System.out.println("[INFO] Changed Background Image");
    }

    @FXML
    public void changeToSmallIcons() {
        Desktop desktop = Desktop.getInstance();
        desktop.setIconSize(IconSize.SMALL);
        desktop.refresh();

        System.out.println("[INFO] Changed Icon Size to SMALL");
    }

    @FXML
    public void changeToMediumIcons() {
        Desktop desktop = Desktop.getInstance();
        desktop.setIconSize(IconSize.MEDIUM);
        desktop.refresh();

        System.out.println("[INFO] Changed Icon Size to MEDIUM");
    }

    @FXML
    public void changeToLargeIcons() {
        Desktop desktop = Desktop.getInstance();
        desktop.setIconSize(IconSize.LARGE);
        desktop.refresh();

        System.out.println("[INFO] Changed Icon Size to LARGE");
    }
    @FXML
    public void refresh() {
        Desktop desktop = Desktop.getInstance();
        desktop.refresh();
    }

    @FXML
    public void deleteIcon() {
        FileData parentDirectory = contextMenuInstance.getFileData();
        Desktop desktop = Desktop.getInstance();
        if (!desktop.getSelectedIconsArrayList().isEmpty()) {
            System.out.println("[INFO] Deleted Icons:");
            for (Icon icon : desktop.getSelectedIconsArrayList()) {
                System.out.println("[INFO] " + icon.toString());
                FileDataManager manager = FileDataManager.getInstance();
                manager.deleteItem(parentDirectory, icon.getData().getName());
            }

            Pane desktopPane = (Pane) contextMenuRoot.getParent();
            desktopPane.getChildren().removeAll(desktop.getSelectedIconsArrayList().stream().map(Icon::getIconVBox).toList());
            desktop.getIconArrayList().removeAll(desktop.getSelectedIconsArrayList());
        }
    }

    @FXML
    public void openFileExplorer() {
        Explorer explorer = new Explorer();
    }

    @FXML
    public void openTerminal() {
        Terminal terminal = new Terminal();
    }
}
