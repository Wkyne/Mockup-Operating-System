package com.gummybear.desktop;

import com.gummybear.desktop.icon.Icon;
import com.gummybear.desktop.icon.IconSize;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.*;

import java.util.ArrayList;
import java.util.Objects;

@Data
public class Desktop {

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
            desktopPane.getChildren().remove(icon.getIconImage());

            Point2D position = new Point2D(initialX, initialY);
            icon.setPosition(position);
            icon.getIconImage().setLayoutX(position.getX());
            icon.getIconImage().setLayoutY(position.getY());

            ImageView imageView = icon.getIconImage();
            int size = desktopWidth / iconSize.getSize();
            imageView.setFitHeight(size);
            imageView.setFitWidth(size);

            final double[] dragDelta = new double[2];
            imageView.setOnMousePressed(event -> {
                dragDelta[0] = event.getSceneX() - imageView.getLayoutX();
                dragDelta[1] = event.getSceneY() - imageView.getLayoutY();
            });
            imageView.setOnMouseDragged(event -> {
                imageView.setLayoutX(event.getSceneX() - dragDelta[0]);
                imageView.setLayoutY(event.getSceneY() - dragDelta[1]);
            });
            imageView.setOnMouseReleased(event -> {
                double snappedX = desktopPadding + Math.round((imageView.getLayoutX() - desktopPadding) / size) * size;
                double snappedY = desktopPadding + Math.round((imageView.getLayoutY() - desktopPadding) / size) * size;
                imageView.setLayoutX(snappedX);
                imageView.setLayoutY(snappedY);
                //System.out.println("Snapped to: " + snappedX + " " + snappedY);
            });

            System.out.println(initialX);
            desktopPane.getChildren().add(icon.getIconImage());
            initialX += size;

            if (initialX+size >= desktopWidth) {
                initialX = desktopPadding;
                initialY += size;
            }
        }
    }

}
