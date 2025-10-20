package com.gummybear.desktop.icon;

import com.gummybear.desktop.Desktop;
import com.gummybear.desktop.window.Window;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Icon {

    int id;
    String name;
    Point2D position;
    Label nameLabel;
    ImageView iconImage;
    VBox iconVBox;

    Window window;

    int size;
    final double[] dragDelta = new double[2];

    public Icon () {
        Desktop d = Desktop.getInstance();
        //size = d.getDesktopWidth() / d.getIconSize().getSize();

        id = d.getNextIconID();
        d.setNextIconID(id+1);

        position = new Point2D(0,0);
        iconImage = new ImageView();
        nameLabel = new Label();
        iconVBox = new VBox();

        nameLabel.setTextFill(Color.WHITE);
        nameLabel.setAlignment(Pos.CENTER);

//        iconVBox.setOnMousePressed(event -> {
//            dragDelta[0] = event.getSceneX() - iconVBox.getLayoutX();
//            dragDelta[1] = event.getSceneY() - iconVBox.getLayoutY();
//        });
//        iconVBox.setOnMouseDragged(event -> {
//            iconVBox.setLayoutX(event.getSceneX() - dragDelta[0]);
//            iconVBox.setLayoutY(event.getSceneY() - dragDelta[1]);
//        });
//        iconVBox.setOnMouseReleased(event -> {
//            int offsetY = 20;
//            double snappedX = d.getDesktopPadding() + Math.round((iconVBox.getLayoutX() - d.getDesktopPadding()) / size) * size;
//            double snappedY = d.getDesktopPadding() + Math.round((iconVBox.getLayoutY() - d.getDesktopPadding()) / (size+offsetY)) * (size+offsetY);
//            iconVBox.setLayoutX(snappedX);
//            iconVBox.setLayoutY(snappedY);
//            System.out.println("[INFO] Icon" + id + " Snapped To: X=" + snappedX + " Y=" + snappedY);
//        });
        
    }

}

// <a target="_blank" href="https://icons8.com/icon/14077/document">Document</a> icon by <a target="_blank" href="https://icons8.com">Icons8</a>
