package com.gummybear;

import com.gummybear.desktop.window.TerminalWindow;
import javafx.fxml.FXML;
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
    public void enterCommand(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String path = windowInstance.getCurrentPath().getText();
            String command = windowInstance.getInputLine().getText();
            windowInstance.getInputLine().setText("");

            Label commandLabel = new Label(path + command);
            int nextIndex = terminalContents.getChildren().size()-1;

            if (!command.isEmpty()) {
                String result = windowInstance.parseCommand(command);
                Label commandResult = new Label(result);
                terminalContents.getChildren().add(nextIndex, commandResult);
            }
            terminalContents.getChildren().add(nextIndex, commandLabel);
        }
    }

}
