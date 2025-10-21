package com.gummybear.filemanagement;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class FileManager {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_PATH = "DirectoryData/fileTree.json";
    private static FileItem root;


    public static void loadFileStructure() throws IOException {
        try (Reader reader = new FileReader(FILE_PATH)) {
            root = gson.fromJson(reader, FileItem.class);
        }
    }

    public static void saveFileStructure(FileItem root) throws IOException {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(root, writer);
        }
    }

    public static void createFile(FileItem currentFolder, String fileName, String contentPath) {
        FileItem newFile = new FileItem(fileName, "file");
        newFile.setContentPath(contentPath);
        currentFolder.getContents().add(newFile);
    }

    public static void createFolder(FileItem currentFolder, String folderName) {
        FileItem newFolder = new FileItem(folderName, "folder");
        currentFolder.getContents().add(newFolder);
    }

    public static void deleteItem(FileItem currentFolder, String itemName) {
        currentFolder.getContents().removeIf(item -> item.getName().equals(itemName));
    }

    public static FileItem getRoot() {
        return root;
    }


    //Testing functions
    public static void printTree(FileItem folder) {
        helperprintTree(folder, 0); 
    }

    private static void helperprintTree(FileItem folder, int level) {
        String indent = "  ".repeat(level); 

        for (FileItem item : folder.getContents()) {
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


    // public static void saveFileStructure(String filePath, List<FileItem> items) throws IOException {
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
