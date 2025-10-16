package com.gummybear.desktop.icon;

import com.gummybear.desktop.Desktop;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
public class Icon {

    int id;
    String name;
    Point2D position;
    ImageView iconImage;

    public Icon () {
        Desktop d = Desktop.getInstance();
        int size = d.getDesktopWidth() / d.getIconSize().getSize();

        id = d.getNexID();
        d.setNexID(id+1);

        position = new Point2D(0,0);
        iconImage = new ImageView();

        final double[] dragDelta = new double[2];
        iconImage.setOnMousePressed(event -> {
            dragDelta[0] = event.getSceneX() - iconImage.getLayoutX();
            dragDelta[1] = event.getSceneY() - iconImage.getLayoutY();
        });
        iconImage.setOnMouseDragged(event -> {
            iconImage.setLayoutX(event.getSceneX() - dragDelta[0]);
            iconImage.setLayoutY(event.getSceneY() - dragDelta[1]);
        });
        iconImage.setOnMouseReleased(event -> {
            double snappedX = d.getDesktopPadding() + Math.round((iconImage.getLayoutX() - d.getDesktopPadding()) / size) * size;
            double snappedY = d.getDesktopPadding() + Math.round((iconImage.getLayoutY() - d.getDesktopPadding()) / size) * size;
            iconImage.setLayoutX(snappedX);
            iconImage.setLayoutY(snappedY);
            System.out.println("[INFO] Icon" + id + " snapped to: X=" + snappedX + " Y=" + snappedY);
        });
    }

}

// <a target="_blank" href="https://icons8.com/icon/14077/document">Document</a> icon by <a target="_blank" href="https://icons8.com">Icons8</a>
