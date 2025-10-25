package com.gummybear.desktop.window;

import com.gummybear.TerminalController;
import com.gummybear.data.FileData;
import com.gummybear.data.FileDataManager;
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
import java.util.Objects;
import java.util.Optional;

@Getter
public class TerminalWindow extends Window {

    TerminalController terminalController;

    FileData currentDirectory = FileDataTree.getRootDirectory();
    TextField inputLine = new TextField();
    Label currentPath = new Label(currentDirectory.getPath() + "> ");
    HBox inputLineContainer = new HBox();
    private void moveDirectory(FileData newDirectory) {
        currentDirectory = newDirectory;
        inputLineContainer.getChildren().stream().filter(a -> a.getClass() == Label.class).findFirst().map(a -> (Label)a).get().setText(currentDirectory.getPath() + "> ");
        currentPath = new Label(currentDirectory.getPath() + "> ");
    }

    public TerminalWindow() {
        super();
        name = "Terminal";
        controller.getWindowTitleLabel().setText(name);

        ScrollPane terminalRoot = null;
        try {
            FXMLLoader terminalLoader = new FXMLLoader(getClass().getResource("/com/gummybear/terminal.fxml"));
            terminalRoot = terminalLoader.load();
            terminalRoot.prefWidthProperty().bind(windowUI.widthProperty());
            terminalRoot.prefHeightProperty().bind(windowUI.heightProperty().subtract(controller.getWindowTitleBarHBox().heightProperty()));
            terminalController = terminalLoader.getController();
            terminalController.setWindowInstance(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        inputLine.getStylesheets().add(getClass().getResource("/com/gummybear/style/terminal.css").toExternalForm());
        inputLine.getStyleClass().add("command-line-input");
        currentPath.getStylesheets().add(getClass().getResource("/com/gummybear/style/terminal.css").toExternalForm());
        currentPath.getStyleClass().add("command-line-path");
        inputLineContainer.getChildren().addAll(currentPath, inputLine);

        HBox.setHgrow(inputLine, Priority.ALWAYS);

        VBox terminalContent = terminalController.getTerminalContents();
        terminalContent.getChildren().add(inputLineContainer);

        controller.getWindowRoot().setCenter(terminalRoot);
    }

    final private ArrayList<String> terminalCommandsArrayList = new ArrayList<>(Arrays.asList(
            "hello",
            "help",
            "list",
            "create",
            "remove",
            "move",
            "open",
            "run",
            "rename"
    ));

    public String parseCommand(String commandString) {
        String tokenArray[] = commandString.split(" ");
        String commandWord = tokenArray[0];
        if (terminalCommandsArrayList.contains(commandWord)) {
            return switch (commandWord) {
                case "hello" -> helloCommand(tokenArray);
                case "help" -> helpCommand(tokenArray);
                case "list" -> listCommand(tokenArray);
                case "create" -> createCommand(tokenArray);
                case "remove" -> removeCommand(tokenArray);
                case "move" -> moveCommand(tokenArray);
                case "open" -> openCommand(tokenArray);
                case "run" -> runCommand(tokenArray);
                case "rename" -> renameCommand(tokenArray);
                default -> "Unknown Error Encountered";
            };
        } else {
            return "Unknown Command \"" + commandWord + "\"";
        }
    }

    private String helloCommand(String[] tokenArray) {
        int tokenAmount = tokenArray.length;
        if (tokenAmount-1 == 0) {
            return "world";
        } else {
            return "Parameter Mismatch: Expecting 0, Found " + tokenAmount;
        }
    }

    private String helpCommand(String[] tokenArray) {
        int tokenAmount = tokenArray.length;
        if (tokenAmount-1 != 0) {
            return "Parameter Mismatch: Expecting 0, Found " + tokenAmount;
        }

        return """
                create [file|folder] [<name>] - Creates a file or folder in the current directory.
                hello - Replies with "world"
                help - Displays a list of commands
                list - Prints all files and folders in the current directory
                move [<foldername>] - Moves to an existing directory
                open [<filename>] - Opens an existing file in the current directory
                remove [<name>] - Deletes an existing file or folder
                rename [<name1>] [<name2>] - Replaces the name of an existing file or forder with a new one
                run [<scriptname>] - Runs an existing script file in the current directory
                """;
    }

    private String listCommand(String[] tokenArray) {
        int tokenAmount = tokenArray.length;
        if (tokenAmount-1 == 0) {
            StringBuilder directory = new StringBuilder();
            for (FileData fd : currentDirectory.getContents()) {
                directory.append(fd.getName());
                if (fd.getType().equals("folder")) directory.append("/");
                directory.append("\n");
            }
            return (directory.toString().isEmpty())? "Empty Directory" : directory.toString();
        } else {
            return "Parameter Mismatch: Expecting 0, Found " + tokenAmount;
        }
    }

    private String createCommand(String[] tokenArray) {
        int tokenAmount = tokenArray.length;
        if (tokenAmount-1 != 2) {
            return "Parameter Mismatch: Expecting 2, Found " + tokenAmount;
        }

        boolean invalidArgument = !(Objects.equals(tokenArray[1], "file") || Objects.equals(tokenArray[1], "folder"));
        if (invalidArgument) {
            return "Invalid Argument: " + tokenArray[1];
        }

        FileData fileData = new FileData();
        fileData.setName(tokenArray[2]);
        fileData.setType(tokenArray[1]);
        fileData.setText("");
        fileData.setPath(currentDirectory.getPath()+"/"+fileData.getName());
        fileData.setParent(currentDirectory);
        fileData.setContents(new ArrayList<>());

        FileDataManager manager = FileDataManager.getInstance();
        return (Objects.equals(tokenArray[1], "file"))?
                manager.createFile(currentDirectory, fileData)
                :
                manager.createFolder(currentDirectory, fileData);
    }

    private String removeCommand(String[] tokenArray) {
        int tokenAmount = tokenArray.length;
        if (tokenAmount-1 != 1) {
            return "Parameter Mismatch: Expecting 1, Found " + tokenAmount;
        }

        FileDataManager manager = FileDataManager.getInstance();
        return manager.deleteItem(currentDirectory, tokenArray[1]);
    }

    private String moveCommand(String[] tokenArray) {
        int tokenAmount = tokenArray.length;
        if (tokenAmount-1 != 1) {
            return "Parameter Mismatch: Expecting 1, Found " + tokenAmount;
        }

        if (Objects.equals(tokenArray[1], "..")) {
            moveDirectory(currentDirectory.getParent());
            return "Moved to Folder: " + currentDirectory.getName();
        }

        Optional<FileData> optionalFile = currentDirectory.getContents().stream().filter(a -> Objects.equals(a.getName(), tokenArray[1])).findFirst();
        FileData file = null;
        if (optionalFile.isPresent()) {
            file = optionalFile.get();
            moveDirectory(file);
            return "Moved to Folder: " + file.getName();
        } else {
            return tokenArray[1] + " Not Found";
        }
    }

    private String openCommand(String[] tokenArray) {
        int tokenAmount = tokenArray.length;
        if (tokenAmount-1 != 1) {
            return "Parameter Mismatch: Expecting 1, Found " + tokenAmount;
        }

        Optional<FileData> optionalFile = currentDirectory.getContents().stream().filter(a -> Objects.equals(a.getName(), tokenArray[1])).findFirst();
        FileData file = null;
        if (optionalFile.isPresent()) {
            file = optionalFile.get();
            if (file.isWindowOpen()) {
                return "File Already Opened In A Window";
            }
            if (Objects.equals(file.getType(), "file")) {
                file.setWindowOpen(true);
                new FileWindow(file);
                return "Opened File: " + file.getName();
            } else if (Objects.equals(file.getType(), "folder")) {
                file.setWindowOpen(true);
                new ExplorerWindow(file);
                return "Opened Folder: " + file.getName();
            } else {
                return "Unknown Type Encountered";
            }
        } else {
            return tokenArray[1] + " Not Found";
        }
    }

    private String runCommand(String[] tokenArray) {
        int tokenAmount = tokenArray.length;
        if (tokenAmount-1 != 1) {
            return "Parameter Mismatch: Expecting 1, Found " + tokenAmount;
        }

        Optional<FileData> optionalFile = currentDirectory.getContents().stream().filter(a -> Objects.equals(a.getName(), tokenArray[1])).findFirst();
        FileData file = null;
        if (optionalFile.isPresent()) {
            file = optionalFile.get();
            if (Objects.equals(file.getType(), "file") && file.getName().contains(".script")) {
                String[] scriptCode = file.getText().split("\n");
                for (String command : scriptCode) {
                    terminalController.readInputLine(command);
                }
                return "Finished Running Script";
            }
            return "Invalid Argument: Expected A Script File";
        }

        return tokenArray[1] + " Not Found";
    }

    private String renameCommand(String[] tokenArray) {
        int tokenAmount = tokenArray.length;
        if (tokenAmount-1 != 2) {
            return "Parameter Mismatch: Expecting 2, Found " + tokenAmount;
        }

        FileDataManager manager = FileDataManager.getInstance();
        return manager.renameItem(currentDirectory, tokenArray[1], tokenArray[2]);
    }

}
