package com.gummybear.desktop.window;

import com.gummybear.WindowController;
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
    Icon icon;
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

    }

}