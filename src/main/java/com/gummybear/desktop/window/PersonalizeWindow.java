package com.gummybear.desktop.window;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import com.gummybear.desktop.Desktop;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Data;


@Data
public class PersonalizeWindow extends Window{
    public PersonalizeWindow (){
        super();
        GridPane grid = new GridPane();
        grid.setBackground(
            new Background(
                new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)
            )
        );

        grid.setHgap(10);
        grid.setVgap(10);
        int maxCol = 3;
        int col = 0;
        int row = 0;
        for(Image img : Desktop.getInstance().getBackgrounds()){
            ImageView imageView = new ImageView(img);
            imageView.setFitHeight(150);
            imageView.setPreserveRatio(true);
            imageView.setOnMouseClicked(e->{
                Desktop.getInstance().setBackground(img);
            });


            grid.add(imageView, col, row);
            GridPane.setMargin(imageView, new Insets(10));
            col++;
            if(col == maxCol){
                row++;
                col = 0;
            }
        }
        controller.getWindowTitleLabel().setText("Personalize Background");
        controller.getWindowTitleBarHBox().minWidthProperty().bind(grid.widthProperty());
        controller.getWindowRoot().setCenter(grid);
    }
}