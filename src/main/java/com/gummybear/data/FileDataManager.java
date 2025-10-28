package com.gummybear.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gummybear.desktop.icon.Icon;
import javafx.scene.control.Label;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

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
        try (Reader reader = new FileReader(FILE_PATH)) {
            rootDirectory = gson.fromJson(reader, FileData.class);
            assignParent(rootDirectory);
        } catch (Exception ignored) {}
        return rootDirectory;
    }

    public void saveRootDirectory() {
        FileData rootDirectory = FileDataTree.getRootDirectory();
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(rootDirectory, writer);
        } catch (IOException ignored) {}
    }

    //TODO turn nameExists into just newFile.setName(newFile.getName + "(1)")
    public String createFile(FileData currentDirectory, FileData newFile) {
        boolean nameExists = currentDirectory.getContents().stream().anyMatch(a -> Objects.equals(a.getName(), newFile.getName()));
        if (nameExists) return "[Failed] " + newFile.getName() + " Is Already Taken";
        currentDirectory.getContents().add(newFile);
        if (currentDirectory.getName().equals("desktop")) {
            new Icon(newFile);
        }
        saveRootDirectory();
        return "Created File: " + newFile.getName();
    }

    public String createFolder(FileData currentDirectory, FileData newFolder) {
        boolean nameExists = currentDirectory.getContents().stream().anyMatch(a -> Objects.equals(a.getName(), newFolder.getName()));
        if (nameExists) return "[Failed] " + newFolder.getName() + " Is Already Taken";
        currentDirectory.getContents().add(newFolder);
        if (currentDirectory.getName().equals("desktop")) {
            new Icon(newFolder);
        }
        saveRootDirectory();
        return "Created Folder: " + newFolder.getName();
    }

    public String deleteItem(FileData currentDirectory, String fileName) {
        Optional<FileData> optionalFile = currentDirectory.getContents().stream().filter(a -> Objects.equals(a.getName(), fileName)).findFirst();
        if (optionalFile.isPresent()) {
            FileData file = optionalFile.get();
            String type = file.getType().replace("f", "F");
            String name = file.getName();

            file.getParent().getContents().remove(file);

            saveRootDirectory();
            return "Deleted " + type + ": " + name;
        } else {
            return fileName + " Not Found";
        }
    }

    public String renameItem(FileData currentDirectory, String oldName, String newName) {
        boolean nameExists = currentDirectory.getContents().stream().anyMatch(a -> Objects.equals(a.getName(), newName));
        if (nameExists) return newName + " Is Already Taken";
        Optional<FileData> optionalFile = currentDirectory.getContents().stream().filter(a -> Objects.equals(a.getName(), oldName)).findFirst();
        if (optionalFile.isPresent()) {
            FileData file = optionalFile.get();
            file.setName(newName);
            file.setPath(currentDirectory.getPath()+"/"+newName);
            updatePath(file);
            return "Renamed File: " + oldName + " To " + newName;
        }
        return oldName + " Not Found";
    }

    public FileData findDirectory(FileData currentDirectory, String path) {

        System.out.println("[INFO] Searching for: " + (currentDirectory.getPath()+"/"+path));

        // Search for path
        if (path.contains("/")) {
            if (path.contains("root")) {
                return findFile(FileDataTree.getRootDirectory(), path);
            }
            while (path.startsWith("../")) {
                if (currentDirectory == FileDataTree.getRootDirectory()) return null;
                currentDirectory = currentDirectory.getParent();
                path = path.replaceFirst("../", "");
            }
            return findFile(currentDirectory, currentDirectory.getPath()+"/"+path);
        }

        // Search for children
        String finalPath = path;
        Optional<FileData> childFile = currentDirectory
                .getContents()
                .stream()
                .filter(a -> Objects.equals(a.getName(), finalPath))
                .findFirst();
        return childFile.orElse(null);

    }

    private void assignParent(FileData fileData) {
        fileData.getContents().stream().forEach(a -> {
            a.setParent(fileData);
            assignParent(a);
        });
    }

    private void updatePath(FileData currentDirectory) {
        currentDirectory
                .getContents()
                .forEach(a -> {
                    a.setPath(currentDirectory.getPath()+"/"+a.getName());
                    updatePath(a);
                });
    }

    private FileData findFile(FileData currentDirectory, String path) {
        for (FileData fileData : currentDirectory.getContents()) {
            if (Objects.equals(fileData.getPath(), path)) {
                return fileData;
            }
            FileData found = findFile(fileData, path);
            if (found != null) {
                return found;
            }
        }
        return null;
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
