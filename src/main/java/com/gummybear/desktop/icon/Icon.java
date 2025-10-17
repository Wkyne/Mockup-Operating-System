package com.gummybear.desktop.icon;

import com.gummybear.desktop.Desktop;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.swing.*;

@AllArgsConstructor
@Data
public class Icon {

    int id;
    String name;
    Label nameLabel;
    Point2D position;
    ImageView iconImage;
    VBox iconVBox;

    public Icon () {
        Desktop d = Desktop.getInstance();
        int size = d.getDesktopWidth() / d.getIconSize().getSize();

        id = d.getNexID();
        d.setNexID(id+1);

        position = new Point2D(0,0);
        iconImage = new ImageView();
        nameLabel = new Label();
        iconVBox = new VBox();

        nameLabel.setTextFill(Color.WHITE);
        nameLabel.setAlignment(Pos.CENTER);

        final double[] dragDelta = new double[2];
        iconVBox.setOnMousePressed(event -> {
            dragDelta[0] = event.getSceneX() - iconVBox.getLayoutX();
            dragDelta[1] = event.getSceneY() - iconVBox.getLayoutY();
        });
        iconVBox.setOnMouseDragged(event -> {
            iconVBox.setLayoutX(event.getSceneX() - dragDelta[0]);
            iconVBox.setLayoutY(event.getSceneY() - dragDelta[1]);
        });
        iconVBox.setOnMouseReleased(event -> {
            int offsetY = 20;
            double snappedX = d.getDesktopPadding() + Math.round((iconVBox.getLayoutX() - d.getDesktopPadding()) / size) * size;
            double snappedY = d.getDesktopPadding() + Math.round((iconVBox.getLayoutY() - d.getDesktopPadding()) / (size+offsetY)) * (size+offsetY);
            iconVBox.setLayoutX(snappedX);
            iconVBox.setLayoutY(snappedY);
            System.out.println("[INFO] Icon" + id + " Snapped To: X=" + snappedX + " Y=" + snappedY);
        });
    }

}

// <a target="_blank" href="https://icons8.com/icon/14077/document">Document</a> icon by <a target="_blank" href="https://icons8.com">Icons8</a>
