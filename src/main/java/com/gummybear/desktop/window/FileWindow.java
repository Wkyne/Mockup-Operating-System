package com.gummybear.desktop.window;

import com.gummybear.data.FileData;
import com.gummybear.data.FileDataManager;
import com.gummybear.desktop.icon.Icon;
import javafx.scene.control.TextArea;

public class FileWindow extends Window {

    String text = "";

    FileData owner;

    public FileWindow(FileData owner) {
        super();
        this.owner = owner;
        name = owner.getName();
        controller.getWindowTitleLabel().setText(name);

        TextArea textArea = new TextArea(owner.getText());
        controller.getWindowRoot().setCenter(textArea);

        textArea.setOnKeyTyped(event -> {
            FileDataManager manager = FileDataManager.getInstance();
            owner.setText(textArea.getText());
            manager.saveRootDirectory();
        });

        controller.getWindowExitButton().setOnMousePressed(event -> {
            owner.setWindowOpen(false);
        });
    }

}
