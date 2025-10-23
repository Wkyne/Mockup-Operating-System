package com.gummybear.desktop.window;

import com.gummybear.ExplorerController;
import com.gummybear.data.FileData;
import com.gummybear.data.FileDataTree;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import lombok.Getter;

@Getter
public class ExplorerWindow extends Window {

    ExplorerController explorerController;

    FileData currentDirectory = FileDataTree.getRootDirectory();

    public ExplorerWindow() {
        super();
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

        controller.getWindowRoot().setCenter(explorerRoot);
    }

}
