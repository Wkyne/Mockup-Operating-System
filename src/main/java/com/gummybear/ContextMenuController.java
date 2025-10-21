package com.gummybear;

import com.gummybear.desktop.Desktop;
import com.gummybear.desktop.icon.FileIcon;
import com.gummybear.desktop.icon.FolderIcon;
import com.gummybear.desktop.icon.Icon;
import com.gummybear.desktop.icon.IconSize;
import com.gummybear.desktop.window.PersonalizeWindow;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.Getter;

import java.util.Objects;

@Getter
public class ContextMenuController {

    @FXML
    private VBox contextMenuRoot;

    @FXML
    public void createNewFile() {
        Icon icon = new FileIcon();
        Image image = new Image(Objects.requireNonNull(getClass().getResource("/com/gummybear/images/document-icon.png")).toExternalForm());
        icon.getIconImage().setImage(image);
        icon.getIconVBox().getStyleClass().add("icon");

        Desktop desktop = Desktop.getInstance();
        desktop.getIconArrayList().add(icon);
        desktop.render();
    }

    @FXML
    public void createNewFolder() {
        Icon icon = new FolderIcon();
        Image image = new Image(Objects.requireNonNull(getClass().getResource("/com/gummybear/images/folder-icon.png")).toExternalForm());
        icon.getIconImage().setImage(image);
        icon.getIconImage().getStyleClass().add("icon");

        Desktop desktop = Desktop.getInstance();
        desktop.getIconArrayList().add(icon);
        desktop.render();
    }

    @FXML
    public void personalizeBtn(){
        PersonalizeWindow.getInstance();

        System.out.println("[INFO] Changed Background Image");
    }

    @FXML
    public void changeToSmallIcons() {
        Desktop desktop = Desktop.getInstance();
        desktop.setIconSize(IconSize.SMALL);
        desktop.render();

        System.out.println("[INFO] Changed Icon Size to SMALL");
    }

    @FXML
    public void changeToMediumIcons() {
        Desktop desktop = Desktop.getInstance();
        desktop.setIconSize(IconSize.MEDIUM);
        desktop.render();

        System.out.println("[INFO] Changed Icon Size to MEDIUM");
    }

    @FXML
    public void changeToLargeIcons() {
        Desktop desktop = Desktop.getInstance();
        desktop.setIconSize(IconSize.LARGE);
        desktop.render();

        System.out.println("[INFO] Changed Icon Size to LARGE");
    }

    @FXML
    public void deleteIcon() {
        Desktop desktop = Desktop.getInstance();
        if (!desktop.getSelectedIconsArrayList().isEmpty()) {
            System.out.println("[INFO] Deleted Icons:");
            for (Icon icon : desktop.getSelectedIconsArrayList()) {
                System.out.println("[INFO] " + icon.toString());
            }

            Pane desktopPane = (Pane) contextMenuRoot.getParent();
            desktopPane.getChildren().removeAll(desktop.getSelectedIconsArrayList().stream().map(Icon::getIconVBox).toList());
            desktop.getIconArrayList().removeAll(desktop.getSelectedIconsArrayList());
        }
    }
}
