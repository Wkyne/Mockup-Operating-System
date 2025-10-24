package com.gummybear;

import com.gummybear.data.FileData;
import com.gummybear.data.FileDataTree;
import com.gummybear.desktop.window.ExplorerWindow;
import com.gummybear.desktop.window.FileWindow;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Optional;

@Getter
public class ExplorerController {

    @Setter
    ExplorerWindow windowInstance;

    @FXML
    BorderPane explorerRoot;

    @FXML
    HBox explorerNavigationBar;

    @FXML
    Button explorerBackButton;

    @FXML
    Button explorerForwardButton;

    @FXML
    TextField inputField;

    @FXML
    TableView<FileData> explorerTable;
    @FXML
    TableColumn<FileData, String> typeColumn;
    @FXML
    TableColumn<FileData, String> nameColumn;
    @FXML
    TableColumn<FileData, String> pathColumn;

    @FXML
    public void clickOnItem(MouseEvent event) {
        if (event.getClickCount() == 2) {
            FileData selectedItem = explorerTable.getSelectionModel().getSelectedItem();
            if (selectedItem.isWindowOpen()) return;
            if (Objects.equals(selectedItem.getType(), "file")) {
                System.out.println("[INFO] Opened File: " + selectedItem.getName());
                new FileWindow(selectedItem);
                selectedItem.setWindowOpen(true);
            } else if (Objects.equals(selectedItem.getType(), "folder")) {
                System.out.println("[INFO] Moved To Folder: " + selectedItem.getName());
                windowInstance.getPreviousFileStack().add(selectedItem.getParent());
                windowInstance.setCurrentDirectory(selectedItem);
                windowInstance.reloadItems();
            }
        }
    }

    @FXML
    public void goToPreviousDirectory() {
        windowInstance.getNextFileStack().add(windowInstance.getCurrentDirectory());
        FileData previousDirectory = windowInstance.getPreviousFileStack().pop();
        windowInstance.setCurrentDirectory(previousDirectory);
        windowInstance.reloadItems();
        System.out.println("[INFO] Moved To Folder: " + previousDirectory.getName());
    }
    @FXML
    public void goToNextDirectory() {
        windowInstance.getPreviousFileStack().add(windowInstance.getCurrentDirectory());
        FileData nextDirectory = windowInstance.getNextFileStack().pop();
        windowInstance.setCurrentDirectory(nextDirectory);
        windowInstance.reloadItems();
        System.out.println("[INFO] Moved To Folder: " + nextDirectory.getName());
    }

    @FXML
    public void explorerSearch(KeyEvent event) {
        if (event.getCode() != KeyCode.ENTER) return;

        // Root
        if (inputField.getText().equals("root") || inputField.getText().isEmpty()) {
            windowInstance.setCurrentDirectory(FileDataTree.getRootDirectory());
            windowInstance.reloadItems();
            System.out.println("[INFO] Moved To Folder: " + FileDataTree.getRootDirectory().getName());
            return;
        }

        // Search for path
        FileData directory;
        Optional<FileData> searchedDirectory = FileDataTree
                .getRootDirectory()
                .getContents()
                .stream()
                .filter(a -> a.getPath().equals((inputField.getText())))
                .findFirst();
        if (searchedDirectory.isPresent()) {
            directory = searchedDirectory.get();
            if (directory.getType().equals("folder")) {
                windowInstance.setCurrentDirectory(directory);
                windowInstance.reloadItems();
                System.out.println("[INFO] Moved To Folder: " + directory.getName());
            } else if (directory.getType().equals("file")) {
                explorerTable.getItems().clear();
                explorerTable.getItems().add(directory);
                System.out.println("[INFO] Searched File: " + directory.getName());
            }
            return;
        }

        // Search for files
        System.out.println("[INFO] Searching for: \"" + inputField.getText() + "\"");
        explorerTable.getItems().clear();
        fileSearch(FileDataTree.getRootDirectory());
    }

    private void fileSearch(FileData currentDirectory) {
        currentDirectory
                .getContents()
                .forEach(a -> {
                    String name = a.getName().toUpperCase();
                    String searched = inputField.getText().toUpperCase();
                    if (name.contains(searched)) {
                        explorerTable.getItems().add(a);
                        System.out.println("[INFO] " + a.toString());
                    }
                    fileSearch(a);
                });
    }

}
