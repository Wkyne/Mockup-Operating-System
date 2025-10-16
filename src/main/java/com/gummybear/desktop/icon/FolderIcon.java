package com.gummybear.desktop.icon;

public class FolderIcon extends Icon {

    public FolderIcon() {
        super();
        name = "MyFolder" + id;
        System.out.println("[INFO] Created New Folder: " + this.toString());
    }

}
