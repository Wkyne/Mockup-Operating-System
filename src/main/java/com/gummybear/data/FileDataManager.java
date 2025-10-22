package com.gummybear.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class FileDataManager {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final String FILE_PATH = "DirectoryData/fileTree.json";
    private int nextFileDataID = 0;

    // Singleton Manager
    private static FileDataManager fileDataManagerInstance = null;
    public static FileDataManager getInstance() {
        if (fileDataManagerInstance == null) {
            fileDataManagerInstance = new FileDataManager();
        }
        return fileDataManagerInstance;
    }
    private FileDataManager() {}

    public FileData loadRootDirectory() {
        FileData rootDirectory = null;
        try {
            Reader reader = new FileReader(FILE_PATH);
            rootDirectory = gson.fromJson(reader, FileData.class);
            return rootDirectory;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveRootDirectory() {
        FileData rootDirectory = null;
        try {
            Writer writer = new FileWriter(FILE_PATH);
            gson.toJson(rootDirectory, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FileData createFile(FileData currentDirectory) {
        FileData newFile = new FileData();
        newFile.setId(nextFileDataID++);
        newFile.setName("New File");
        newFile.setType("file");
        currentDirectory.getContents().add(newFile);

        return newFile;
    }
    public FileData createFile() {
        return createFile(FileDataTree.getRootDirectory());
    }

    public FileData createFolder(FileData currentDirectory) {
        FileData newFile = new FileData();
        newFile.setId(nextFileDataID++);
        newFile.setName("New Folder");
        newFile.setType("folder");
        currentDirectory.getContents().add(newFile);

        return newFile;
    }
    public FileData createFolder() {
        return createFolder(FileDataTree.getRootDirectory());
    }

    public void deleteItem(FileData currentDirectory, FileData deleteFile) {
        currentDirectory.getContents().removeIf(file -> file == deleteFile);
    }
    public void deleteItem(FileData deleteFile) {
        deleteItem(FileDataTree.getRootDirectory(), deleteFile);
    }


    //Testing functions
    public void printTree(FileData folder) {
        helperprintTree(folder, 0); 
    }

    private void helperprintTree(FileData folder, int level) {
        String indent = "  ".repeat(level); 

        for (FileData item : folder.getContents()) {
            System.out.println(indent + "- " + item.getName() + " (" + item.getType() + ")");

            if ("folder".equals(item.getType())) {
                helperprintTree(item, level + 1); 
            }
        }
    }


}



// Old Hard Coded JSON Parsing
    // private static final Gson gson = new Gson();

    // public static List<JsonObject> loadFileStructure(String filePath) throws IOException {
    //     try (Reader reader = new FileReader(filePath)) {
    //         JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
    //         return getContents(root);
    //     }
    // }

    // public static List<JsonObject> getContents(JsonObject folder) {
    //     List<JsonObject> items = new ArrayList<>();
    //     if (folder.has("contents")) {
    //         JsonArray contents = folder.getAsJsonArray("contents");
    //         for (JsonElement element : contents) {
    //             JsonObject item = element.getAsJsonObject();
    //             items.add(item);
    //         }
    //     }
    //     return items;
    // }


    // public static void saveFileStructure(String filePath, List<FileData> items) throws IOException {
    //     try (Writer writer = new FileWriter(filePath)) {
    //         gson.toJson(items, writer);
    //     }
    // } 



    // public static void createFile(JsonObject currentFolder, String fileName, String fileContentPath) throws IOException {
    //     JsonObject newFile = new JsonObject();
    //     newFile.addProperty("name", fileName);
    //     newFile.addProperty("type", "file");
    //     newFile.addProperty("contentPath", fileContentPath);


    //     JsonArray contents = currentFolder.getAsJsonArray("contents");
    //     if (contents == null) {
    //         contents = new JsonArray();
    //         currentFolder.add("contents", contents);
    //     }
    //     contents.add(tempFile);
    // }

    // public static void createFolder(JsonObject currentFolder, String folderName) throws IOException {
        
    //     JsonObject newFolder = new JsonObject();
    //     newFolder.addProperty("name", folderName);
    //     newFolder.addProperty("type", "folder");
    //     newFolder.add("contents", new JsonArray());

    //     JsonArray contents = currentFolder.getAsJsonArray("contents");
    //     if (contents == null) {
    //         contents = new JsonArray();
    //         currentFolder.add("contents", contents);
    //     }
    //     contents.add(newFolder);
    // }
