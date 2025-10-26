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
                // === Window resizing ===
        final int RESIZE_MARGIN = 8;
        final double[] resizeDelta = new double[2];
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
        });

        windowUI.setOnMousePressed(event -> {
            resizing[0] = !resizeDir[0].isEmpty();
            resizeDelta[0] = event.getSceneX();
            resizeDelta[1] = event.getSceneY();
        });

        windowUI.setOnMouseDragged(event -> {
            if (!resizing[0]) return;

            double dx = event.getSceneX() - resizeDelta[0];
            double dy = event.getSceneY() - resizeDelta[1];

            double newX = windowUI.getLayoutX();
            double newY = windowUI.getLayoutY();
            double newWidth = windowUI.getWidth();
            double newHeight = windowUI.getHeight();

            switch (resizeDir[0]) {
                case "E" -> newWidth += dx;
                case "S" -> newHeight += dy;
                case "SE" -> { newWidth += dx; newHeight += dy; }
                case "W" -> {
                    newWidth -= dx;
                    newX += dx;
                }
                case "N" -> {
                    newHeight -= dy;
                    newY += dy;
                }
                case "NW" -> {
                    newWidth -= dx;
                    newX += dx;
                    newHeight -= dy;
                    newY += dy;
                }
                case "NE" -> {
                    newWidth += dx;
                    newHeight -= dy;
                    newY += dy;
                }
                case "SW" -> {
                    newWidth -= dx;
                    newX += dx;
                    newHeight += dy;
                }
            }

            // Prevent too small
            if (newWidth < 300) newWidth = 300;
            if (newHeight < 200) newHeight = 200;

            windowUI.setLayoutX(newX);
            windowUI.setLayoutY(newY);
            windowUI.setPrefWidth(newWidth);
            windowUI.setPrefHeight(newHeight);

            resizeDelta[0] = event.getSceneX();
            resizeDelta[1] = event.getSceneY();
        });

        windowUI.setOnMouseReleased(e -> resizing[0] = false);

    }

}