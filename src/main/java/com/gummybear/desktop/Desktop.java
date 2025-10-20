package com.gummybear.desktop;

import com.gummybear.desktop.icon.Icon;
import com.gummybear.desktop.icon.IconSize;
import com.gummybear.desktop.window.Window;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Objects;

@Data
public class Desktop {

    private int nextIconID = 0;
    private int nextWindowID = 0;

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
    private ArrayList<Window> windowArrayList = new ArrayList<>();
    private Pane desktopPane;
    private int desktopWidth, desktopHeight;

    private ArrayList<Icon> selectedIconsArrayList = new ArrayList<>();
    private ArrayList<Icon> selectedIconBuffer = new ArrayList<>();
    Point2D selectionBoxOrigin, selectionBoxEnd;
    private Rectangle selectionBox;

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
            VBox iconGroup = icon.getIconVBox();

            if (selectedIconsArrayList.contains(icon)) {
                iconGroup.getStyleClass().add("icon-selected");
            } else {
                try {
                    iconGroup.getStyleClass().remove("icon-selected");
                } catch (Exception ignored) {}
            }

            desktopPane.getChildren().remove(iconGroup);

            Point2D position = new Point2D(initialX, initialY);
            icon.setPosition(position);
            icon.getIconVBox().setLayoutX(position.getX());
            icon.getIconVBox().setLayoutY(position.getY());

            ImageView imageView = icon.getIconImage();
            Label nameLabel = icon.getNameLabel();
            int size = desktopWidth / iconSize.getSize();
            icon.setSize(size);
            imageView.setFitHeight(size);
            imageView.setFitWidth(size);
            nameLabel.setPrefWidth(size);

            desktopPane.getChildren().add(icon.getIconVBox());
            initialX += size;

            int offsetY = 20;
            if (initialX+size-desktopPadding > desktopWidth) {
                initialX = desktopPadding;
                initialY += size + offsetY;
            }
        }

        for (Window window : windowArrayList) {
            desktopPane.getChildren().remove(window.getWindowUI());
            desktopPane.getChildren().add(window.getWindowUI());
        }
    }

}
