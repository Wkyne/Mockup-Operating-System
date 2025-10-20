package com.gummybear.filemanagement;

import java.util.ArrayList;

public class FileItem {
    private String name;
    private String type; // folder or file
    private ArrayList<FileItem> contents;
    private String contentPath;

    public FileItem(String name, String type) {
        this.name = name;
        this.type = type;
    }
    public String getName() {
        return name;
    }
    public String getType() {
        return type;
    }
    public ArrayList<FileItem> getContents() {
        return contents;
    }
    public void setContents(ArrayList<FileItem> contents) {
        this.contents = contents;
    }
    public String getContentPath() {
        return contentPath;
    }
    public void setContentPath(String contentPath) {
        this.contentPath = contentPath;
    }
}

