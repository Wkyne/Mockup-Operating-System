package com.gummybear.desktop.icon;

import javafx.scene.control.Label;

public class DocumentIcon extends Icon {

    public DocumentIcon() {
        super();
        name = "MyDocument" + id;
        nameLabel.setText(name);
        iconVBox.getChildren().addAll(iconImage, nameLabel);
        System.out.println("[INFO] Created New Folder: " + this.toString());
    }

}
