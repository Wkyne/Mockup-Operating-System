package com.gummybear.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileData {

    private int id;
    private String name;
    private String type; // folder or file
    private String text;
    private ArrayList<FileData> contents;

    private String contentPath;

}

