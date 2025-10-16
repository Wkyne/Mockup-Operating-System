package com.gummybear.desktop.icon;

public class DocumentIcon extends Icon {

    public DocumentIcon() {
        super();
        name = "MyDocument" + id;
        System.out.println("[INFO] Created New Folder: " + this.toString());
    }

}
