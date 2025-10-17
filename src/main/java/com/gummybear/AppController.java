package com.gummybear;

import com.gummybear.desktop.Desktop;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.Objects;

public class AppController {

    @FXML
    Pane desktopPane;

    private final Desktop desktop = Desktop.getInstance();

    public void initialize() {

        desktop.setDesktopPane(desktopPane);
        desktop.setBackground();

        try {
            FXMLLoader contextMenuLoader = new FXMLLoader(getClass().getResource("context-menu.fxml"));
            Parent contextMenuUI = contextMenuLoader.load();

            desktop.getContextMenu().setMenuUI((VBox) contextMenuUI);
            desktopPane.getChildren().add(contextMenuUI);
            contextMenuUI.setVisible(false);

            desktopPane.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    contextMenuUI.setLayoutX(event.getX());
                    contextMenuUI.setLayoutY(event.getY());
                    contextMenuUI.setVisible(true);
                    contextMenuUI.toFront();
                } else { // hide on any other click
                    contextMenuUI.setVisible(false);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
