package com.gummybear;

import com.gummybear.data.FileDataManager;
import com.gummybear.data.FileDataTree;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
// import java.lang.reflect.Array;
// import java.util.ArrayList;
// import java.util.List;

import com.gummybear.data.FileData;

// import com.oracle.graal.enterprise.hotspot.javacodegen.r;

public class App extends Application {

    private static Scene scene;

    public static void main(String[] args) {
        launch();
    }   


    @Override
    public void start(Stage stage) throws IOException {

        FileDataTree tree = FileDataTree.getInstance();

//        FileDataManager fileDataManager = FileDataManager.getInstance();
//
//        fileDataManager.loadFileStructure();
//        FileData root = fileDataManager.getRoot();
//        fileDataManager.printTree(root);


        // Old Hard Coded JSON Parsing
        // ContextMenuController contextMenuController = new ContextMenuController();
        // try {
        //     List<JsonObject> rootItems = FileDataManager.loadFileStructure("DirectoryData/fileTree.json");
            
        //     for (JsonObject item : rootItems) {
        //         System.out.println(item.get("name").getAsString() + " (" + item.get("type").getAsString() + ")");
                
        //         if (item.get("type").getAsString().equals("file")) {
        //             contextMenuController.createNewFile(item.get("name").getAsString());
        //         }

        //         if (item.get("type").getAsString().equals("folder")) {
                    
        //             contextMenuController.createNewFolder(item.get("name").getAsString());

        //             List<JsonObject> contents = FileDataManager.getContents(item);
        //             for (JsonObject contentItem : contents) {
        //                 System.out.println("  - " + contentItem.get("name").getAsString() + " (" + contentItem.get("type").getAsString() + ")");
        //             }
        //         }
        //     }

        // } catch (Exception e) {
        //     e.printStackTrace();
        // }




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



}