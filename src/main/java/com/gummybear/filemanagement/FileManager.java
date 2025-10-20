package com.gummybear.filemanagement;

import com.google.gson.Gson;
import java.io.*;
import java.net.URI;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.ArrayList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class FileManager {
    private static final Gson gson = new Gson();


    public static List<JsonObject> loadFileStructure(String filePath) throws IOException {
        try (Reader reader = new FileReader(filePath)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            return getContents(root);
        }
    }

    public static List<JsonObject> getContents(JsonObject folder) {
        List<JsonObject> items = new ArrayList<>();
        if (folder.has("contents")) {
            JsonArray contents = folder.getAsJsonArray("contents");
            for (JsonElement element : contents) {
                JsonObject item = element.getAsJsonObject();
                items.add(item);
            }
        }
        return items;
    }


    public static void saveFileStructure(String filePath, List<FileItem> items) throws IOException {
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(items, writer);
        }
    } 


   


    public static void createNewFile(JsonObject currentFolder, String fileName, String fileContentPath) throws IOException {
        JsonObject newFile = new JsonObject();
        newFile.addProperty("name", fileName);
        newFile.addProperty("type", "file");
        newFile.addProperty("contentPath", fileContentPath);

        currentFolder.add(fileName, newFile);
    }

}
