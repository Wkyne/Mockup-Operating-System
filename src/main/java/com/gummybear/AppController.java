package com.gummybear;

import com.gummybear.desktop.Desktop;
import com.gummybear.desktop.icon.Icon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class AppController {

    @FXML
    Pane desktopPane;

    private final Desktop desktop = Desktop.getInstance();

    public void initialize() {

        desktop.setDesktopPane(desktopPane);

        Image backgroundImage = new Image(
                Objects.requireNonNull(getClass().getResource("/com/gummybear/images/desktop-background1.jpg")).toExternalForm()
        );
        // Create a BackgroundImage object
        BackgroundImage bgImage = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,  // No repeat horizontally
                BackgroundRepeat.NO_REPEAT,  // No repeat vertically
                BackgroundPosition.CENTER,   // Center the image
                new BackgroundSize(
                        BackgroundSize.AUTO,   // Width automatic
                        BackgroundSize.AUTO,   // Height automatic
                        false,                 // Width not relative to pane
                        false,                 // Height not relative to pane
                        true,                  // Contain within pane
                        true                   // Preserve aspect ratio
                )
        );
        // Set the background on the Pane
        desktopPane.setBackground(new Background(bgImage));

        final Parent contextMenuUI;
        try {
            FXMLLoader contextMenuLoader = new FXMLLoader(getClass().getResource("context-menu.fxml"));
            contextMenuUI = contextMenuLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        desktop.getContextMenu().setMenuUI((VBox) contextMenuUI);
        desktopPane.getChildren().add(contextMenuUI);
        contextMenuUI.setVisible(false);

        desktopPane.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                contextMenuUI.setLayoutX(event.getX());
                contextMenuUI.setLayoutY(event.getY());
                contextMenuUI.setVisible(true);
                contextMenuUI.toFront();
            } else { // hide on any other click
                contextMenuUI.setVisible(false);
            }
        });



        desktopPane.setOnMousePressed(event -> {
            Bounds contextMenuBounds = contextMenuUI.localToScene(contextMenuUI.getBoundsInLocal());
            boolean mouseInsideContextMenu = contextMenuBounds.contains(event.getSceneX(), event.getSceneY());

            boolean hoveringOnSelectedIcon = false;
            for (Icon icon : desktop.getSelectedIconsArrayList()) {
                Bounds vboxBounds = icon.getIconVBox().localToScene(icon.getIconVBox().getBoundsInLocal());
                if (vboxBounds.contains(event.getSceneX(), event.getSceneY())) {
                    hoveringOnSelectedIcon = true;
                }
                icon.getDragDelta()[0] = event.getSceneX() - icon.getIconVBox().getLayoutX();
                icon.getDragDelta()[1] = event.getSceneY() - icon.getIconVBox().getLayoutY();
            }

            boolean hoveringOnIcon = false;
            if (!hoveringOnSelectedIcon) {
                for (Icon icon : desktop.getSelectedIconsArrayList()) {
                    icon.getIconVBox().getStyleClass().remove("icon-selected");
                }
                desktop.getSelectedIconsArrayList().clear();

                for (Icon icon : desktop.getIconArrayList()) {
                    Bounds vboxBounds = icon.getIconVBox().localToScene(icon.getIconVBox().getBoundsInLocal());
                    if (vboxBounds.contains(event.getSceneX(), event.getSceneY())) {
                        desktop.getSelectedIconsArrayList().add(icon);
                        icon.getIconVBox().getStyleClass().add("icon-selected");
                        System.out.println("[INFO] Icon" + icon.getId() + " Added to Selected Icons");
                        hoveringOnIcon = true;
                        icon.getDragDelta()[0] = event.getSceneX() - icon.getIconVBox().getLayoutX();
                        icon.getDragDelta()[1] = event.getSceneY() - icon.getIconVBox().getLayoutY();
                        break;
                    }
                }
            }

            if (!mouseInsideContextMenu) {
                contextMenuUI.setVisible(false);

                if (event.getButton() == MouseButton.PRIMARY && !(hoveringOnIcon || hoveringOnSelectedIcon)) {
                    desktop.setSelectionBoxOrigin(new Point2D(event.getSceneX(), event.getSceneY()));
                    desktop.setSelectionBox(new Rectangle(
                            desktop.getSelectionBoxOrigin().getX(),
                            desktop.getSelectionBoxOrigin().getY(),
                            0,
                            0
                    ));
                    desktop.getSelectionBox().getStyleClass().add("selection-box");
                    desktopPane.getChildren().add(desktop.getSelectionBox());
//                    System.out.println("[INFO] Selection Box Origin=(" +
//                            desktop.getSelectionBoxOrigin().getX() + ", " +
//                            desktop.getSelectionBoxOrigin().getY() + ")" +
//                            " End=(" + desktop.getSelectionBoxOrigin().getX() + ", " +
//                            desktop.getSelectionBoxOrigin().getY() + ")"
//                    );
                }
            }
        });



        desktopPane.setOnMouseDragged(event -> {
            for (Icon icon : desktop.getSelectedIconsArrayList()) {
                icon.getIconVBox().setLayoutX(event.getSceneX() - icon.getDragDelta()[0]);
                icon.getIconVBox().setLayoutY(event.getSceneY() - icon.getDragDelta()[1]);
            }


            if (event.getButton() == MouseButton.PRIMARY) {
                boolean selectionBoxExists = desktopPane.getChildren().contains(desktop.getSelectionBox());
                if (selectionBoxExists) {
                    desktop.setSelectionBoxEnd(new Point2D(event.getSceneX(), event.getSceneY()));
                    Point2D origin = desktop.getSelectionBoxOrigin();
                    Point2D end = desktop.getSelectionBoxEnd();
                    double relativeX = end.getX() - origin.getX();
                    double relativeY = end.getY() - origin.getY();
                    if (relativeX >= 0) {
                        desktop.getSelectionBox().setX(origin.getX());
                        desktop.getSelectionBox().setWidth(relativeX);
                    } else {
                        desktop.getSelectionBox().setX(end.getX());
                        desktop.getSelectionBox().setWidth(Math.abs(relativeX));
                    }
                    if (relativeY >= 0) {
                        desktop.getSelectionBox().setY(origin.getY());
                        desktop.getSelectionBox().setHeight(relativeY);
                    } else {
                        desktop.getSelectionBox().setY(end.getY());
                        desktop.getSelectionBox().setHeight(Math.abs(relativeY));
                    }

//                    System.out.println("[INFO] Selection Box Origin=(" +
//                            desktop.getSelectionBoxOrigin().getX() + ", " +
//                            desktop.getSelectionBoxOrigin().getY() + ")" +
//                            " End=(" + desktop.getSelectionBoxEnd().getX() + ", " +
//                            desktop.getSelectionBoxEnd().getY() + ")"
//                    );
                    Bounds selectionBoxBounds = desktop.getSelectionBox().localToScene(desktop.getSelectionBox().getBoundsInLocal());
                    for (Icon icon : desktop.getIconArrayList()) {
                        Bounds iconBounds = icon.getIconVBox().localToScene(icon.getIconVBox().getBoundsInLocal());
                        if (selectionBoxBounds.intersects(iconBounds)) {
                            if (!desktop.getSelectedIconBuffer().contains(icon)) {
                                desktop.getSelectedIconBuffer().add(icon);
                                icon.getIconVBox().getStyleClass().add("icon-selected");
                                System.out.println("[INFO] Icon" + icon.getId() + " Added to Selected Icons");
                            }
                        } else {
                            if (desktop.getSelectedIconBuffer().contains(icon)) {
                                desktop.getSelectedIconBuffer().remove(icon);
                                icon.getIconVBox().getStyleClass().remove("icon-selected");
                            }
                        }
                    }
                }
            }

        });



        desktopPane.setOnMouseReleased(event -> {
            desktop.getSelectedIconsArrayList().addAll(desktop.getSelectedIconBuffer());
            desktop.getSelectedIconBuffer().clear();

            for (Icon icon : desktop.getSelectedIconsArrayList()) {
                int offsetY = 20;
                double snappedX = desktop.getDesktopPadding() + Math.round((icon.getIconVBox().getLayoutX() - desktop.getDesktopPadding()) / icon.getSize()) * icon.getSize();
                double snappedY = desktop.getDesktopPadding() + Math.round((icon.getIconVBox().getLayoutY() - desktop.getDesktopPadding()) / (icon.getSize()+offsetY)) * (icon.getSize()+offsetY);
                icon.getIconVBox().setLayoutX(snappedX);
                icon.getIconVBox().setLayoutY(snappedY);
                System.out.println("[INFO] Icon" + icon.getId() + " Snapped To: X=" + snappedX + " Y=" + snappedY);
            }


            boolean selectionBoxExists = desktopPane.getChildren().contains(desktop.getSelectionBox());
            if (selectionBoxExists) {
                desktopPane.getChildren().remove(desktop.getSelectionBox());
                desktop.setSelectionBox(null);
            }
        });

    }

}
