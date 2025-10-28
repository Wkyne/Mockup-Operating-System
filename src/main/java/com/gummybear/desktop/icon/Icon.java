package com.gummybear.desktop.icon;

import com.gummybear.data.FileData;
import com.gummybear.data.FileDataManager;
import com.gummybear.data.FileDataTree;
import com.gummybear.desktop.Desktop;
import com.gummybear.desktop.window.ExplorerWindow;
import com.gummybear.desktop.window.FileWindow;
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

import java.util.Objects;

@AllArgsConstructor
@Data
public class Icon {

    FileData data;

    Point2D position;
    Label nameLabel;
    ImageView iconImage;
    VBox iconVBox;

    Window window;

    int size;

    final double[] dragDelta = new double[2];

//    public Icon(String name, Boolean isFile) {
//        data = new FileData()
//            .setName(name)
//            .setPath("root/desktop/" + name)
//            .setText("");
//
//        //TODO: Rework position -> grid
//
//        position = new Point2D(0,0);
//        iconImage = new ImageView();
//        iconImage.getStyleClass().add("icon");
//        iconVBox = new VBox();
//        Desktop desktop = Desktop.getInstance();
//
//        size = desktop.getDesktopWidth() / desktop.getIconSize().getSize();
//        FileDataManager fdm = FileDataManager.getInstance();
//        FileData desktopData = fdm.findDirectory(FileDataTree.getRootDirectory(), "root/desktop");
//
//        if(isFile) {
//            data.setType("file");
//            iconImage.setImage(new Image(getClass().getResource("/com/gummybear/images/file-icon.png").toExternalForm()));
//            while(fdm.createFile(desktopData, data).contains("Failed")){
//                data.setName(data.getName()+"I")
//                    .setPath("root/desktop/" + data.getName());
//            }
//        }else{
//            data.setType("folder");
//            iconImage.setImage(new Image(getClass().getResource("/com/gummybear/images/folder-icon.png").toExternalForm()));
//            while(fdm.createFolder(desktopData, data).contains("Failed")){
//                data.setName(data.getName()+"I")
//                    .setPath("root/desktop/" + data.getName());
//            }
//        }
//
//        nameLabel = new Label(data.getName());
//        nameLabel.setTextFill(Color.WHITE);
//        nameLabel.setAlignment(Pos.CENTER);
//
//        iconVBox.getChildren().addAll(iconImage, nameLabel);
//        desktop.getIconArrayList().add(this);
//        desktop.refresh();
//
////        iconVBox.setOnMousePressed(event -> {
////            dragDelta[0] = event.getSceneX() - iconVBox.getLayoutX();
////            dragDelta[1] = event.getSceneY() - iconVBox.getLayoutY();
////        });
////        iconVBox.setOnMouseDragged(event -> {
////            iconVBox.setLayoutX(event.getSceneX() - dragDelta[0]);
////            iconVBox.setLayoutY(event.getSceneY() - dragDelta[1]);
////        });
////        iconVBox.setOnMouseReleased(event -> {
////            int offsetY = 20;
////            double snappedX = d.getDesktopPadding() + Math.round((iconVBox.getLayoutX() - d.getDesktopPadding()) / size) * size;
////            double snappedY = d.getDesktopPadding() + Math.round((iconVBox.getLayoutY() - d.getDesktopPadding()) / (size+offsetY)) * (size+offsetY);
////            iconVBox.setLayoutX(snappedX);
////            iconVBox.setLayoutY(snappedY);
////        });
//    }

    public Icon(FileData fileData){
        data = fileData;
        position = new Point2D(0,0);
        iconImage = new ImageView();
        iconImage.getStyleClass().add("icon");
        iconVBox = new VBox();
        Desktop desktop = Desktop.getInstance();
        size = desktop.getDesktopWidth() / desktop.getIconSize().getSize();

        if(data.getType().equals("file")) {
            data.setType("file");
            iconImage.setImage(new Image(getClass().getResource("/com/gummybear/images/file-icon.png").toExternalForm()));
        }else{
            data.setType("folder");
            iconImage.setImage(new Image(getClass().getResource("/com/gummybear/images/folder-icon.png").toExternalForm()));
        }

        nameLabel = new Label(data.getName());
        nameLabel.setTextFill(Color.WHITE);
        nameLabel.setAlignment(Pos.CENTER);

        iconVBox.getChildren().addAll(iconImage, nameLabel);
        desktop.getIconArrayList().add(this);
        desktop.refresh();

        iconVBox.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                if (Objects.equals(fileData.getType(), "file")) {
                    fileData.setWindowOpen(true);
                    new FileWindow(fileData);
                    System.out.println("Opened File: " + fileData.getName()); ;
                } else if (Objects.equals(fileData.getType(), "folder")) {
                    fileData.setWindowOpen(true);
                    new ExplorerWindow(fileData);
                    System.out.println("Opened Folder: " + fileData.getName());
                } else {
                    System.out.println("Unknown Type Encountered");
                }
            }
        });
    }

}

// <a target="_blank" href="https://icons8.com/icon/14077/document">Document</a> icon by <a target="_blank" href="https://icons8.com">Icons8</a>
