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
import javafx.scene.layout.Pane;
import lombok.Data;

import java.io.IOException;

@Data
public class Window {

    int id;
    String name;
    Point2D position;
    double width, height;
    
    BorderPane windowUI;
    BorderPane wrapper;
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
            // windowUI.setLayoutX(position.getX());
            // windowUI.setLayoutY(position.getY());
            // windowUI.setPrefWidth(width);
            // windowUI.setPrefHeight(height);
            // windowUI.setPickOnBounds(true);
            controller = windowLoader.getController();
            controller.setWindowInstance(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // desktop.getWindowArrayList().add(this);
        // desktop.getDesktopPane().getChildren().add(windowUI);









         // Window resizing part



        //make another pane thingamajig behind the actual window to handle resizing
        final int RESIZE_MARGIN = 10;
        wrapper = new BorderPane();
        
        wrapper.setPrefSize(width + RESIZE_MARGIN * 2, height + RESIZE_MARGIN * 2);
        wrapper.setStyle("-fx-background-color: transparent;");
        wrapper.setPickOnBounds(true);

        
        wrapper.setCenter(windowUI);
        BorderPane.setMargin(windowUI, new javafx.geometry.Insets(RESIZE_MARGIN));

        
        wrapper.setLayoutX((desktop.getDesktopWidth() - width - RESIZE_MARGIN * 2) / 2);
        wrapper.setLayoutY((desktop.getDesktopHeight() - height - RESIZE_MARGIN * 2) / 2);

        // add to le desktoppy
        desktop.getWindowArrayList().add(this);
        desktop.getDesktopPane().getChildren().add(wrapper);



        
        HBox titleBar = controller.getWindowTitleBarHBox();
        titleBar.setOnMousePressed(event -> {
            dragDelta[0] = event.getSceneX() - wrapper.getLayoutX();
            dragDelta[1] = event.getSceneY() - wrapper.getLayoutY();
        });
        titleBar.setOnMouseDragged(event -> {
            wrapper.setLayoutX(event.getSceneX() - dragDelta[0]);
            wrapper.setLayoutY(event.getSceneY() - dragDelta[1]);
        });

        final double[] pressScene = new double[2];
       
        final double[] origBounds = new double[4];  
        final boolean[] resizing = {false};
        final String[] resizeDir = {""};

        wrapper.setOnMouseMoved(event -> {
            double x = event.getX();
            double y = event.getY();
            double w = wrapper.getWidth();
            double h = wrapper.getHeight();

            // the logic GPT gave me 
            if (x < RESIZE_MARGIN && y < RESIZE_MARGIN) {
                wrapper.setCursor(javafx.scene.Cursor.NW_RESIZE);
                resizeDir[0] = "NW";
            } else if (x > w - RESIZE_MARGIN && y < RESIZE_MARGIN) {
                wrapper.setCursor(javafx.scene.Cursor.NE_RESIZE);
                resizeDir[0] = "NE";
            } else if (x < RESIZE_MARGIN && y > h - RESIZE_MARGIN) {
                wrapper.setCursor(javafx.scene.Cursor.SW_RESIZE);
                resizeDir[0] = "SW";
            } else if (x > w - RESIZE_MARGIN && y > h - RESIZE_MARGIN) {
                wrapper.setCursor(javafx.scene.Cursor.SE_RESIZE);
                resizeDir[0] = "SE";
            } else if (x < RESIZE_MARGIN) {
                wrapper.setCursor(javafx.scene.Cursor.W_RESIZE);
                resizeDir[0] = "W";
            } else if (x > w - RESIZE_MARGIN) {
                wrapper.setCursor(javafx.scene.Cursor.E_RESIZE);
                resizeDir[0] = "E";
            } else if (y < RESIZE_MARGIN) {
                wrapper.setCursor(javafx.scene.Cursor.N_RESIZE);
                resizeDir[0] = "N";
            } else if (y > h - RESIZE_MARGIN) {
                wrapper.setCursor(javafx.scene.Cursor.S_RESIZE);
                resizeDir[0] = "S";
            } else {
                wrapper.setCursor(javafx.scene.Cursor.DEFAULT);
                resizeDir[0] = "";
            }

            System.out.println("Cursor at (" + x + ", " + y + "), resizeDir: " + resizeDir[0]);
        });

        wrapper.setOnMousePressed(event -> {
            System.out.println("Clicking to resize, direction: " + resizeDir[0]);
            resizing[0] = !resizeDir[0].isEmpty();
            if (resizing[0]) {
                System.out.println("Start resizing in direction: " + resizeDir[0]);
                pressScene[0] = event.getSceneX();
                pressScene[1] = event.getSceneY();
                origBounds[0] = wrapper.getLayoutX();
                origBounds[1] = wrapper.getLayoutY();
                origBounds[2] = windowUI.getWidth() > 0 ? windowUI.getWidth() : windowUI.getPrefWidth();
                origBounds[3] = windowUI.getHeight() > 0 ? windowUI.getHeight() : windowUI.getPrefHeight();
            }
            event.consume();
        });

        wrapper.setOnMouseDragged(event -> {
            if (!resizing[0]) return;
            System.out.println("Resizing...");
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

            System.out.println("New dimensions: (" + newWidth + ", " + newHeight + ") at (" + newX + ", " + newY + ")");
            System.out.println("wrapper bounds: " + wrapper.getBoundsInParent().toString());
            System.out.println("windowUI bounds: " + windowUI.getBoundsInParent().toString());
            final double MIN_W = 300;
            final double MIN_H = 200;
            if (newWidth < MIN_W) {
                if (resizeDir[0].contains("W")) {
                    
                    newX += newWidth - MIN_W;
                }
                newWidth = MIN_W;
                System.out.println("max width reached");
            }

            if (newHeight < MIN_H) {
                if (resizeDir[0].contains("N")) {
                    newY += newHeight - MIN_H;
                }
                newHeight = MIN_H;
                System.out.println("max height reached");
            }

            wrapper.setLayoutX(newX);
            wrapper.setLayoutY(newY);
            wrapper.setPrefWidth(newWidth + RESIZE_MARGIN * 2);
            wrapper.setPrefHeight(newHeight + RESIZE_MARGIN * 2);
            windowUI.setPrefWidth(newWidth - RESIZE_MARGIN * 2);
            windowUI.setPrefHeight(newHeight - RESIZE_MARGIN * 2);
            event.consume();
        });
        
         wrapper.setOnMouseReleased(e -> resizing[0] = false);
        
    }


    // Winow top right thingies functions

    private boolean minimized = false;
    private boolean maximized = false;
    private double prevX, prevY, prevWidth, prevHeight;


    public void minimize() {
        if (getWrapper() != null) {
            getWrapper().setVisible(false);
            minimized = true;
        }
    }

    public void restore() {
        if (getWrapper() != null) {
            getWrapper().setVisible(true);
            minimized = false;
            getWrapper().toFront();
        }
    }

    public void maximize(Pane desktopPane) {
        if (getWrapper() == null) return;

        if (!maximized) {
            // save old dimensions n shiz to go back to
            prevX = getWrapper().getLayoutX();
            prevY = getWrapper().getLayoutY();
            prevWidth = getWrapper().getPrefWidth();
            prevHeight = getWrapper().getPrefHeight();

            
            double taskbarHeight = 35; 
            getWrapper().setLayoutX(0);
            getWrapper().setLayoutY(0);
            getWrapper().setPrefWidth(desktopPane.getWidth());
            getWrapper().setPrefHeight(desktopPane.getHeight() - taskbarHeight);

            maximized = true;
            getWrapper().toFront();
        } else {
            getWrapper().setLayoutX(prevX);
            getWrapper().setLayoutY(prevY);
            getWrapper().setPrefWidth(prevWidth);
            getWrapper().setPrefHeight(prevHeight);
            maximized = false;
        }
    }

    public boolean isMaximized() {
        return maximized;
    }

    public boolean isMinimized() {
        return minimized;
    }
}