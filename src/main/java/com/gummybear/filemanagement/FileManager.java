package com.gummybear.filemanagement;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
// import java.net.URI;
import java.util.List;
// import java.util.concurrent.ExecutionException;
import java.util.ArrayList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.gummybear.filemanagement.FileItem;

public class FileManager {
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
    //     File newFile = new File(fileName, fileContentPath);
    //     JsonObject tempFile = new JsonObject();
    //     tempFile.addProperty("name", newFile.getName());
    //     tempFile.addProperty("type", "file");
    //     tempFile.addProperty("contentPath", newFile.fileContentPath);


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

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static final String FILE_PATH = "DirectoryData/fileTree.json";

    public static FileItem loadFileStructure() throws IOException {
        try (Reader reader = new FileReader(FILE_PATH)) {
            return gson.fromJson(reader, FileItem.class);
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

}
