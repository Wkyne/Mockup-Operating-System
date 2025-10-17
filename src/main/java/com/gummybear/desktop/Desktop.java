package com.gummybear.desktop;

import com.gummybear.desktop.icon.Icon;
import com.gummybear.desktop.icon.IconSize;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Objects;

@Data
public class Desktop {

    private int nexID = 0;

    private static Desktop currentDesktopInstance;
    private Desktop () {}
    public static Desktop getInstance() {
        if (currentDesktopInstance == null) {
            currentDesktopInstance = new Desktop();
            currentDesktopInstance.addBackground("desktop-background1.jpg");
            currentDesktopInstance.addBackground("desktop-background2.png");
            currentDesktopInstance.addBackground("desktop-background3.jpg");
        }
        return currentDesktopInstance;
    }

    private static ArrayList<Image> bgArr = new ArrayList<>();

    BackgroundSize bgSize = new BackgroundSize(BackgroundSize.AUTO,BackgroundSize.AUTO,false,false,true,true); // 1 2 WH Auto && 3 4 Not Relative to Pane, 5 Preserve Aspect Ratio
    private static int currIndex = 0;

    public void setBackground(){
        BackgroundImage bgImage = new BackgroundImage(
                bgArr.get(currIndex),
                BackgroundRepeat.NO_REPEAT,  // No repeat horizontally
                BackgroundRepeat.NO_REPEAT,  // No repeat vertically
                BackgroundPosition.CENTER,   // Center the image
                bgSize
        );
        currIndex = (currIndex+1)%bgArr.size();
        desktopPane.setBackground(new Background(bgImage));
    }

    public void addBackground(String filename){ //Assert String filename in bg folder
        bgArr.add(new Image(
            Objects.requireNonNull(Desktop.class.getResource("/com/gummybear/images/backgrounds/"+filename).toExternalForm()))
        );
    }

    private ContextMenu contextMenu = new ContextMenu();
    private ArrayList<Icon> iconArrayList = new ArrayList<>();
    private Pane desktopPane;
    private int desktopWidth, desktopHeight;

    public void setDesktopPane(Pane pane) {
        desktopPane = pane;
        desktopPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            desktopWidth = newVal.intValue();
        });
        desktopPane.heightProperty().addListener((obs, oldVal, newVal) -> {
            desktopHeight = newVal.intValue();
        });
    }

    private IconSize iconSize = IconSize.MEDIUM;
    private int desktopPadding = 10;

    public void render() {
        int initialX = desktopPadding;
        int initialY = desktopPadding;
        for (Icon icon : iconArrayList) {
            desktopPane.getChildren().remove(icon.getIconImage());

            Point2D position = new Point2D(initialX, initialY);
            icon.setPosition(position);
            icon.getIconImage().setLayoutX(position.getX());
            icon.getIconImage().setLayoutY(position.getY());

            ImageView imageView = icon.getIconImage();
            int size = desktopWidth / iconSize.getSize();
            imageView.setFitHeight(size);
            imageView.setFitWidth(size);

            //System.out.println(initialX);
            desktopPane.getChildren().add(icon.getIconImage());
            initialX += size;

            if (initialX+size >= desktopWidth) {
                initialX = desktopPadding;
                initialY += size;
            }
        }
    }

}
