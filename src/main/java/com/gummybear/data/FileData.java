package com.gummybear.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

}

