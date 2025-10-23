package com.gummybear.data;

import lombok.Data;
import lombok.Getter;

@Data
public class FileDataTree {

    @Getter
    private static FileData rootDirectory;

    // Singleton Manager
    private static FileDataTree fileDataTreeInstance = null;
    public static FileDataTree getInstance() {
        if (fileDataTreeInstance == null) {
            fileDataTreeInstance = new FileDataTree();
        }
        return fileDataTreeInstance;
    }
    private FileDataTree() {
        FileDataManager manager = FileDataManager.getInstance();
        rootDirectory = manager.loadRootDirectory();
    }

}
