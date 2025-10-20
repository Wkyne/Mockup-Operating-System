package com.gummybear;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import com.gummybear.filemanagement.FileItem;
import com.gummybear.filemanagement.FileManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {

        try {
            List<JsonObject> rootItems = FileManager.loadFileStructure("DirectoryData/fileTree.json");
            
            for (JsonObject item : rootItems) {
                System.out.println(item.get("name").getAsString() + " (" + item.get("type").getAsString() + ")");
                if (item.get("type").getAsString().equals("folder")) {
                    List<JsonObject> contents = FileManager.getContents(item);
                    for (JsonObject contentItem : contents) {
                        System.out.println("  - " + contentItem.get("name").getAsString() + " (" + contentItem.get("type").getAsString() + ")");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        scene = new Scene(loadFXML("app"));
        stage.setScene(scene);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                try {
                    stop();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}