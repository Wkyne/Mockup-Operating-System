package com.gummybear.desktop;

import com.gummybear.ContextMenuController;
import com.gummybear.data.FileData;
import javafx.geometry.Point2D;
import javafx.scene.layout.VBox;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ContextMenu {

    Point2D position;
    VBox menuUI;
    FileData fileData;

    ContextMenuController controller;

    public ContextMenu(FileData fileData) {
        this.fileData = fileData;
    }

}
