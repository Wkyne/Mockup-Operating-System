package com.gummybear.desktop.window;

import com.gummybear.TerminalController;
import com.gummybear.data.FileData;
import com.gummybear.data.FileDataTree;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;

@Getter
public class TerminalWindow extends Window {

    TerminalController terminalController;

    FileData currentDirectory = FileDataTree.getRootDirectory();
    TextField inputLine = new TextField();
    Label currentPath = new Label(currentDirectory.getPath() + "> ");

    public TerminalWindow() {
        super();
        name = "Terminal";
        controller.getWindowTitleLabel().setText(name);

        ScrollPane terminalRoot = null;
        try {
            FXMLLoader terminalLoader = new FXMLLoader(getClass().getResource("/com/gummybear/command-line.fxml"));
            terminalRoot = terminalLoader.load();
            terminalRoot.prefWidthProperty().bind(windowUI.widthProperty());
            terminalRoot.prefHeightProperty().bind(windowUI.heightProperty().subtract(controller.getWindowTitleBarHBox().heightProperty()));
            terminalController = terminalLoader.getController();
            terminalController.setWindowInstance(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        inputLine.getStylesheets().add(getClass().getResource("/com/gummybear/style/command-line-window.css").toExternalForm());
        inputLine.getStyleClass().add("command-line-input");
        currentPath.getStylesheets().add(getClass().getResource("/com/gummybear/style/command-line-window.css").toExternalForm());
        currentPath.getStyleClass().add("command-line-path");

        HBox.setHgrow(inputLine, Priority.ALWAYS);

        VBox terminalContent = terminalController.getTerminalContents();
        HBox inputLineContainer = new HBox();
        inputLineContainer.getChildren().addAll(currentPath, inputLine);
        terminalContent.getChildren().add(inputLineContainer);

        controller.getWindowRoot().setCenter(terminalRoot);
    }

    final private ArrayList<String> terminalCommandsArrayList = new ArrayList<>(Arrays.asList(
            "hello",
            "show",
            "create"
    ));

    public String parseCommand(String commandString) {
        String tokenArray[] = commandString.split(" ");
        String commandWord = tokenArray[0];
        if (terminalCommandsArrayList.contains(commandWord)) {
            return switch (commandWord) {
                case "hello" -> helloCommand(tokenArray);
                case "show" -> showCommand(tokenArray);
                case "create" -> createCommand(tokenArray);
                default -> "Unknown Error Encountered";
            };
        } else {
            return "Unknown Command \"" + commandWord + "\"";
        }
    }

    private String helloCommand(String[] tokenArray) {
        int tokenAmount = tokenArray.length;
        if (tokenAmount-1 == 0) {
            return "World";
        } else {
            return "Parameter Mismatch: Expecting 0, Found " + tokenAmount;
        }
    }

    private String showCommand(String[] tokenArray) {
        int tokenAmount = tokenArray.length;
        if (tokenAmount-1 == 0) {
            StringBuilder directory = new StringBuilder();
            for (FileData fd : currentDirectory.getContents()) {
                directory.append(fd.getName()).append("\n");
            }
            return (directory.toString().isEmpty())? "Empty Directory" : directory.toString();
        } else {
            return "Parameter Mismatch: Expecting 0, Found " + tokenAmount;
        }
    }

    private String createCommand(String[] tokenArray) {
        int tokenAmount = tokenArray.length;
        if (tokenAmount-1 == 1) {
            boolean invalidArgument = tokenArray[1] == "file" || tokenArray[1] == "folder";
            if (invalidArgument) {
                return "Invalid Argument \"" + tokenArray[1] + "\"";
            }
            FileData fd = new FileData();
            fd.setName("New File");
            fd.setType(tokenArray[1]);
            fd.setPath("root/"+fd.getName());
            currentDirectory.getContents().add(fd);
            return "Created new File";
        } else {
            return "Parameter Mismatch: Expecting 1, Found " + tokenAmount;
        }
    }

}
