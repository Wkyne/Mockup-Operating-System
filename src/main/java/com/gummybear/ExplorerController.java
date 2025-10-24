package com.gummybear;

import com.gummybear.data.FileData;
import com.gummybear.desktop.window.ExplorerWindow;
import com.gummybear.desktop.window.FileWindow;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

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
    }
    @FXML
    public void goToNextDirectory() {
        windowInstance.getPreviousFileStack().add(windowInstance.getCurrentDirectory());
        FileData nextDirectory = windowInstance.getNextFileStack().pop();
        windowInstance.setCurrentDirectory(nextDirectory);
        windowInstance.reloadItems();
    }

}
