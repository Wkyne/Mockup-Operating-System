package com.gummybear.desktop;

import com.gummybear.desktop.icon.Icon;
import com.gummybear.desktop.icon.IconSize;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.*;

import java.util.ArrayList;

@Data
public class Desktop {

    private int nexID = 0;

    private static Desktop currentDesktopInstance;
    private Desktop () {}
    public static Desktop getInstance() {
        if (currentDesktopInstance == null) {
            currentDesktopInstance = new Desktop();
        }
        return currentDesktopInstance;
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
            VBox iconGroup = icon.getIconVBox();

            desktopPane.getChildren().remove(iconGroup);

            Point2D position = new Point2D(initialX, initialY);
            icon.setPosition(position);
            icon.getIconVBox().setLayoutX(position.getX());
            icon.getIconVBox().setLayoutY(position.getY());

            ImageView imageView = icon.getIconImage();
            Label nameLabel = icon.getNameLabel();
            int size = desktopWidth / iconSize.getSize();
            imageView.setFitHeight(size);
            imageView.setFitWidth(size);
            nameLabel.setPrefWidth(size);

            desktopPane.getChildren().add(icon.getIconVBox());
            initialX += size;

            int offsetY = 20;
            if (initialX+size >= desktopWidth) {
                initialX = desktopPadding;
                initialY += size + offsetY;
            }
        }
    }

}
