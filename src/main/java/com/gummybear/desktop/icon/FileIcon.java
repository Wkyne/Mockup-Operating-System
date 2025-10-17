package com.gummybear.desktop.icon;

public class FileIcon extends Icon {

    public FileIcon() {
        super();
        name = "MyFile" + id;
        nameLabel.setText(name);
        iconVBox.getChildren().addAll(iconImage, nameLabel);
        System.out.println("[INFO] Created New File: " + this.toString());
    }

}
