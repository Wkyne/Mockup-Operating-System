package com.gummybear.desktop.window;

import com.gummybear.ExplorerController;
import com.gummybear.data.FileData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import lombok.Getter;
import lombok.Setter;

import java.util.Stack;

@Getter
public class ExplorerWindow extends Window {


    ExplorerController explorerController;
    Stack<FileData> previousFileStack = new Stack<>(), nextFileStack = new Stack<>();

    @Setter
    FileData currentDirectory;

    public ExplorerWindow(FileData currentDirectory) {
        super();
        this.currentDirectory = currentDirectory;
        name = "File Explorer";
        controller.getWindowTitleLabel().setText(name);

        BorderPane explorerRoot;
        try {
            FXMLLoader explorerLoader = new FXMLLoader(getClass().getResource("/com/gummybear/explorer.fxml"));
            explorerRoot = explorerLoader.load();
            explorerRoot.prefWidthProperty().bind(windowUI.widthProperty());
            explorerRoot.prefHeightProperty().bind(windowUI.heightProperty().subtract(controller.getWindowTitleBarHBox().heightProperty()));
            explorerController = explorerLoader.getController();
            explorerController.setWindowInstance(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        explorerController.getInputField().setText(currentDirectory.getPath());

        explorerController.getNameColumn().setCellValueFactory(new PropertyValueFactory<FileData, String>("name"));
        explorerController.getTypeColumn().setCellValueFactory(new PropertyValueFactory<FileData, String>("type"));
        explorerController.getPathColumn().setCellValueFactory(new PropertyValueFactory<FileData, String>("path"));
        reloadItems();

        controller.getWindowRoot().setCenter(explorerRoot);
    }

    public void reloadItems() {
        explorerController.getExplorerTable().getItems().clear();
        ObservableList<FileData> items = FXCollections.observableArrayList(currentDirectory.getContents());
        explorerController.getExplorerTable().setItems(items);
        explorerController.getInputField().setText(currentDirectory.getPath());

        explorerController.getExplorerBackButton().setDisable(previousFileStack.isEmpty());
        explorerController.getExplorerForwardButton().setDisable(nextFileStack.isEmpty());
    }

}
