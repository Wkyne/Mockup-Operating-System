package com.gummybear.desktop;

import com.gummybear.desktop.icon.Icon;
import com.gummybear.desktop.icon.IconSize;
import com.gummybear.desktop.window.Window;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.scene.layout.*;
import lombok.*;


import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.scene.text.TextAlignment;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;



import com.gummybear.ContextMenuController;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

// import javax.naming.Context;

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
            currentDesktopInstance.addBackground("desktop-background4.jpg");
            currentDesktopInstance.addBackground("desktop-background5.jpg");
            currentDesktopInstance.addBackground("desktop-background6.jpg");
        }
        return currentDesktopInstance;
    }

    public void addBackground(String filename){ //Assert String filename in bg folder
        backgroundImageArrayList.add(new Image(
            Objects.requireNonNull(Desktop.class.getResource("/com/gummybear/images/backgrounds/"+filename).toExternalForm()))
        );
    }
    BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO,BackgroundSize.AUTO,false,false,true,true); // 1 2 WH Auto && 3 4 Not Relative to Pane, 5 Preserve Aspect Ratio

    private static ArrayList<Image> backgroundImageArrayList = new ArrayList<>();

    public void setBackground(Image img){
        BackgroundImage bgImage = new BackgroundImage(
                img,
                BackgroundRepeat.NO_REPEAT,  // No repeat horizontally
                BackgroundRepeat.NO_REPEAT,  // No repeat vertically
                BackgroundPosition.CENTER,   // Center the image
                backgroundSize
        );
        desktopPane.setBackground(new Background(bgImage));
    }

    public ArrayList<Image> getBackgrounds(){
        return backgroundImageArrayList;
    }

    public void setDefaultBackground(){
        setBackground(backgroundImageArrayList.get(0));
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
        createTaskbar();
    }

    private IconSize iconSize = IconSize.MEDIUM;
    private int desktopPadding = 10;

    public void refresh() {

        // Reload icons
        int initialX = desktopPadding;
        int initialY = desktopPadding;
        for (Icon icon : iconArrayList) {
            VBox iconGroup = icon.getIconVBox();

            if (selectedIconsArrayList.contains(icon)) {
                iconGroup.getStyleClass().add("icon-selected");
            } else {
                iconGroup.getStyleClass().remove("icon-selected");
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

        // Reload windows
        for (Window window : windowArrayList) {
            desktopPane.getChildren().remove(window.getWindowUI());
            desktopPane.getChildren().add(window.getWindowUI());
        }
    }

    private HBox taskbar;
    private Label dateLabel;
    private Button fileExplorerButton;
    private Button terminalButton;


    public void createTaskbar() {
    // Create taskbar layout
    taskbar = new HBox();
    taskbar.setStyle("-fx-background-color: rgba(30,30,30,0.9); -fx-padding: 5 15 5 15;");
    taskbar.setPrefHeight(35);
    taskbar.setAlignment(Pos.CENTER);
    taskbar.setSpacing(20);

    // Buttons
    fileExplorerButton = new Button("📁 File Explorer");
    terminalButton = new Button("💻 Terminal");

    fileExplorerButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
    terminalButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");

    // Date label (right side)
    dateLabel = new Label();
    dateLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");
    dateLabel.setTextAlignment(TextAlignment.RIGHT);

    // Live clock update
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, MMM d  HH:mm");
    Timeline clock = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
        dateLabel.setText(LocalDateTime.now().format(formatter));
    }));
    clock.setCycleCount(Animation.INDEFINITE);
    clock.play();

    // Create flexible spacing
    Region leftSpacer = new Region();
    Region rightSpacer = new Region();
    HBox.setHgrow(leftSpacer, Priority.ALWAYS);
    HBox.setHgrow(rightSpacer, Priority.ALWAYS);

    // Add everything (centered buttons + right date)
    taskbar.getChildren().addAll(leftSpacer, fileExplorerButton, terminalButton, rightSpacer, dateLabel);

    // Bind to bottom of desktop
    taskbar.layoutYProperty().bind(desktopPane.heightProperty().subtract(taskbar.heightProperty()));
    taskbar.prefWidthProperty().bind(desktopPane.widthProperty());
    ContextMenuController contextMenuController = new ContextMenuController();
    fileExplorerButton.setOnAction(e -> contextMenuController.openFileExplorer());
    terminalButton.setOnAction(e -> contextMenuController.openTerminal());

    desktopPane.getChildren().add(taskbar);
}


}
