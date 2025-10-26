package com.gummybear.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)

public class FileData {
    private int id;
    private String name;
    private String type; // folder or file
    private String text;
    private String path;
    @ToString.Exclude
    private transient FileData parent;
    @ToString.Exclude
    private transient boolean windowOpen;

    private ArrayList<FileData> contents;

    public void setTo(FileData fileData) {
        this.id = fileData.id;
        this.name = fileData.name;
        this.type = fileData.type;
        this.text = fileData.text;
        this.path = fileData.path;
        this.parent = fileData.parent;
        this.contents = fileData.contents;
    }
}

