package com.gummybear;

import com.gummybear.desktop.window.TerminalWindow;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

@Getter
public class TerminalController {

    @Setter
    TerminalWindow windowInstance;

    @FXML
    ScrollPane terminaRoot;
    @FXML
    VBox terminalContents;

    @FXML
    public void inputLineType(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String command = windowInstance.getInputLine().getText();
            windowInstance.getInputLine().setText("");
            readInputLine(command);
            return;
        }
    }

    public void readInputLine(String command) {
        String path = windowInstance.getCurrentPath().getText();
        Label commandLabel = new Label(path + command);
        commandLabel.setWrapText(false);
        int nextIndex = terminalContents.getChildren().size() - 1;
        if (!command.isEmpty()) {
            String result = windowInstance.parseCommand(command);
            Label commandResult = new Label(result);
            commandResult.setWrapText(false);
            terminalContents.getChildren().add(nextIndex, commandResult);
            Platform.runLater(()->{
                terminaRoot.setVvalue(1.0);
            });
        }
        terminalContents.getChildren().add(nextIndex, commandLabel);
        Platform.runLater(()->{
            terminaRoot.setVvalue(1.0);
        });
    }




}
