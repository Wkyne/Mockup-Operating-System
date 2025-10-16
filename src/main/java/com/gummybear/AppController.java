package com.gummybear;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class AppController {

    @FXML
    Pane desktop;

    public void initialize() {

        Image backgroundImage = new Image(
                getClass().getResource("/com/gummybear/images/desktop-background1.jpg").toExternalForm()
        );

        // Create a BackgroundImage object
        BackgroundImage bgImage = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,  // No repeat horizontally
                BackgroundRepeat.NO_REPEAT,  // No repeat vertically
                BackgroundPosition.CENTER,   // Center the image
                new BackgroundSize(
                        BackgroundSize.AUTO,   // Width automatic
                        BackgroundSize.AUTO,   // Height automatic
                        false,                 // Width not relative to pane
                        false,                 // Height not relative to pane
                        true,                  // Contain within pane
                        true                   // Preserve aspect ratio
                )
        );

        // Set the background on the Pane
        desktop.setBackground(new Background(bgImage));

    }

}
