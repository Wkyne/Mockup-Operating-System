package com.gummybear.desktop.icon;

import com.gummybear.desktop.window.FileWindow;
import com.gummybear.desktop.window.Window;
import javafx.scene.input.MouseButton;

public class FileIcon extends Icon {

    public FileIcon() {
        super();
        name = "MyFile" + id;
        nameLabel.setText(name);
        iconVBox.getChildren().addAll(iconImage, nameLabel);
        System.out.println("[INFO] Created New File: " + this.toString());

        iconVBox.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                //System.out.println("YOU CLICKED ME");
                window = new FileWindow(this);
            }
        });
    }

    public FileIcon(String name) {
        super();
        nameLabel.setText(name);
        iconVBox.getChildren().addAll(iconImage, nameLabel);
        System.out.println("[INFO] Created New File: " + this.toString());

        iconVBox.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                //System.out.println("YOU CLICKED ME");
                window = new FileWindow(this);
            }
        });
    }

}
