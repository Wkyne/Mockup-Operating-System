package com.gummybear.desktop.icon;

import javafx.scene.control.Label;

public class FolderIcon extends Icon {

    public FolderIcon(String name) {
        super();
        nameLabel.setText(name);
        iconVBox.getChildren().addAll(iconImage, nameLabel);
        System.out.println("[INFO] Created New Folder: " + this.toString());
    }

    public FolderIcon() {
        super();
        name = "MyFolder" + id;
        nameLabel.setText(name);
        iconVBox.getChildren().addAll(iconImage, nameLabel);
        System.out.println("[INFO] Created New Folder: " + this.toString());
    }

}
