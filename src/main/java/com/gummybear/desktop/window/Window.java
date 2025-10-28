package com.gummybear.desktop.window;

import com.gummybear.WindowController;
import com.gummybear.data.FileData;
import com.gummybear.desktop.Desktop;
import com.gummybear.desktop.icon.Icon;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import lombok.Data;

import java.io.IOException;

@Data
public class Window {

    int id;
    String name;
    Point2D position;
    double width, height;

    BorderPane windowUI;
    WindowController controller;

    final double[] dragDelta = new double[2];

    public Window() {
        Desktop desktop = Desktop.getInstance();

        id = desktop.getNextWindowID();
        desktop.setNextWindowID(id+1);

        width = 600;
        height = 400;
        position = new Point2D((desktop.getDesktopWidth() - width)/2, (desktop.getDesktopHeight() - height)/2);

        try {
            FXMLLoader windowLoader = new FXMLLoader(getClass().getResource("/com/gummybear/window.fxml"));
            windowUI = windowLoader.load();
            windowUI.setLayoutX(position.getX());
            windowUI.setLayoutY(position.getY());
            windowUI.setPrefWidth(width);
            windowUI.setPrefHeight(height);
            controller = windowLoader.getController();
            controller.setWindowInstance(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        desktop.getWindowArrayList().add(this);
        desktop.getDesktopPane().getChildren().add(windowUI);

        HBox titleBar = controller.getWindowTitleBarHBox();
        titleBar.setOnMousePressed(event -> {
            dragDelta[0] = event.getSceneX() - windowUI.getLayoutX();
            dragDelta[1] = event.getSceneY() - windowUI.getLayoutY();
        });
        titleBar.setOnMouseDragged(event -> {
            windowUI.setLayoutX(event.getSceneX() - dragDelta[0]);
            windowUI.setLayoutY(event.getSceneY() - dragDelta[1]);
        });

         // Window resizing part
        final int RESIZE_MARGIN = 20;

        final double[] pressScene = new double[2];
       
        final double[] origBounds = new double[4];  
        final boolean[] resizing = {false};
        final String[] resizeDir = {""};

        windowUI.setOnMouseMoved(event -> {
            double x = event.getX();
            double y = event.getY();
            double w = windowUI.getWidth();
            double h = windowUI.getHeight();

            // Detect which edge/corner the cursor is near
            if (x < RESIZE_MARGIN && y < RESIZE_MARGIN) {
                windowUI.setCursor(javafx.scene.Cursor.NW_RESIZE);
                resizeDir[0] = "NW";
            } else if (x > w - RESIZE_MARGIN && y < RESIZE_MARGIN) {
                windowUI.setCursor(javafx.scene.Cursor.NE_RESIZE);
                resizeDir[0] = "NE";
            } else if (x < RESIZE_MARGIN && y > h - RESIZE_MARGIN) {
                windowUI.setCursor(javafx.scene.Cursor.SW_RESIZE);
                resizeDir[0] = "SW";
            } else if (x > w - RESIZE_MARGIN && y > h - RESIZE_MARGIN) {
                windowUI.setCursor(javafx.scene.Cursor.SE_RESIZE);
                resizeDir[0] = "SE";
            } else if (x < RESIZE_MARGIN) {
                windowUI.setCursor(javafx.scene.Cursor.W_RESIZE);
                resizeDir[0] = "W";
            } else if (x > w - RESIZE_MARGIN) {
                windowUI.setCursor(javafx.scene.Cursor.E_RESIZE);
                resizeDir[0] = "E";
            } else if (y < RESIZE_MARGIN) {
                windowUI.setCursor(javafx.scene.Cursor.N_RESIZE);
                resizeDir[0] = "N";
            } else if (y > h - RESIZE_MARGIN) {
                windowUI.setCursor(javafx.scene.Cursor.S_RESIZE);
                resizeDir[0] = "S";
            } else {
                windowUI.setCursor(javafx.scene.Cursor.DEFAULT);
                resizeDir[0] = "";
            }

            System.out.println("Cursor at (" + x + ", " + y + "), resizeDir: " + resizeDir[0]);
        });

        windowUI.setOnMousePressed(event -> {
            resizing[0] = !resizeDir[0].isEmpty();
            if (resizing[0]) {
                // capture initial scene coords and original bounds (use pref if actual width not set)
                pressScene[0] = event.getSceneX();
                pressScene[1] = event.getSceneY();
                origBounds[0] = windowUI.getLayoutX();
                origBounds[1] = windowUI.getLayoutY();
                origBounds[2] = windowUI.getWidth() > 0 ? windowUI.getWidth() : windowUI.getPrefWidth();
                origBounds[3] = windowUI.getHeight() > 0 ? windowUI.getHeight() : windowUI.getPrefHeight();
            }
        });

        windowUI.setOnMouseDragged(event -> {
            if (!resizing[0]) return;

            double dx = event.getSceneX() - pressScene[0];
            double dy = event.getSceneY() - pressScene[1];

            double newX = origBounds[0];
            double newY = origBounds[1];
            double newWidth = origBounds[2];
            double newHeight = origBounds[3];

            switch (resizeDir[0]) {
                case "E" -> newWidth = origBounds[2] + dx;
                case "S" -> newHeight = origBounds[3] + dy;
                case "SE" -> { newWidth = origBounds[2] + dx; newHeight = origBounds[3] + dy; }
                case "W" -> {
                    newWidth = origBounds[2] - dx;
                    newX = origBounds[0] + dx;
                }
                case "N" -> {
                    newHeight = origBounds[3] - dy;
                    newY = origBounds[1] + dy;
                }
                case "NW" -> {
                    newWidth = origBounds[2] - dx;
                    newX = origBounds[0] + dx;
                    newHeight = origBounds[3] - dy;
                    newY = origBounds[1] + dy;
                }
                case "NE" -> {
                    newWidth = origBounds[2] + dx;
                    newHeight = origBounds[3] - dy;
                    newY = origBounds[1] + dy;
                }
                case "SW" -> {
                    newWidth = origBounds[2] - dx;
                    newX = origBounds[0] + dx;
                    newHeight = origBounds[3] + dy;
                }
            }


            final double MIN_W = 300;
            final double MIN_H = 200;
            if (newWidth < MIN_W) {
                if (resizeDir[0].contains("W")) {
                    
                    newX += newWidth - MIN_W;
                }
                newWidth = MIN_W;
            }
            else{
                System.out.println("max width reached");
            }
            if (newHeight < MIN_H) {
                if (resizeDir[0].contains("N")) {
                    newY += newHeight - MIN_H;
                }
                newHeight = MIN_H;
            }
            else{
                System.out.println("max height reached");
            }

            windowUI.setLayoutX(newX);
            windowUI.setLayoutY(newY);
            windowUI.setPrefWidth(newWidth);
            windowUI.setPrefHeight(newHeight);
        });

         windowUI.setOnMouseReleased(e -> resizing[0] = false);

    }

}