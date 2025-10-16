package com.gummybear.desktop;

import javafx.geometry.Point2D;
import javafx.scene.layout.VBox;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ContextMenu {

    Point2D position;
    VBox menuUI;

}
