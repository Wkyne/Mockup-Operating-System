package com.gummybear;

import com.gummybear.desktop.Desktop;
import com.gummybear.desktop.icon.DocumentIcon;
import com.gummybear.desktop.icon.FolderIcon;
import com.gummybear.desktop.icon.Icon;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.Getter;

@Getter
public class ContextMenuController {

    @FXML
    private VBox contextMenuRoot;

    @FXML
    public void createNewFile() {
        Icon icon = new DocumentIcon();
        Image image = new Image(getClass().getResource("/com/gummybear/images/document-icon.png").toExternalForm());
        icon.getIconImage().setImage(image);
        icon.getIconImage().getStyleClass().add("icon");

        Desktop desktop = Desktop.getInstance();
        desktop.getIconArrayList().add(icon);
        desktop.render();
    }

    @FXML
    public void createNewFolder() {
        Icon icon = new FolderIcon();
        Image image = new Image(getClass().getResource("/com/gummybear/images/folder-icon.png").toExternalForm());
        icon.getIconImage().setImage(image);
        icon.getIconImage().getStyleClass().add("icon");

        Desktop desktop = Desktop.getInstance();
        desktop.getIconArrayList().add(icon);
        desktop.render();
    }

}
