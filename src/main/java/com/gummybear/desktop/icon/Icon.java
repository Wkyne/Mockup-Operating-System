package com.gummybear.desktop.icon;

import com.gummybear.data.FileData;
import com.gummybear.desktop.Desktop;
import com.gummybear.desktop.window.Window;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Icon {

    String name;
    Point2D position;
    Label nameLabel;
    ImageView iconImage;
    VBox iconVBox;
    FileData data;

    Window window;

    int size;
    final double[] dragDelta = new double[2];

    public Icon(String name, Boolean isFile) {
        data = new FileData()
            .setName(name)
            .setPath("root/desktop/" + name)
            .setText("")
            .setContents(null);

        position = new Point2D(0,0);

        iconImage = new ImageView();
        iconImage.getStyleClass().add("icon");

        nameLabel = new Label(name);
        nameLabel.setTextFill(Color.WHITE);
        nameLabel.setAlignment(Pos.CENTER);

        iconVBox = new VBox();
        Desktop desktop = Desktop.getInstance();

        size = desktop.getDesktopWidth() / desktop.getIconSize().getSize();
        // id = desktop.getNextIconID();
        // desktop.setNextIconID(id+1); //dk abt this one

        if(isFile) {
            data.setType("file");
            iconImage.setImage(new Image(getClass().getResource("/com/gummybear/images/file-icon.png").toExternalForm()));
        }else{
            data.setType("folder");
            iconImage.setImage(new Image(getClass().getResource("/com/gummybear/images/folder-icon.png").toExternalForm()));
        }

        iconVBox.getChildren().addAll(iconImage, nameLabel);
        desktop.getIconArrayList().add(this);
        desktop.refresh();

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
