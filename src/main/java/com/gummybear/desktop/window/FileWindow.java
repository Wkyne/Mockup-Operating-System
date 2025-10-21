package com.gummybear.desktop.window;

import com.gummybear.desktop.icon.Icon;
import javafx.scene.control.TextArea;

public class FileWindow extends Window {

    String text = "";

    public FileWindow(Icon icon) {
        super();
        this.icon = icon;
        name = icon.getName();
        controller.getWindowTitleLabel().setText(name);

        TextArea textArea = new TextArea();
        controller.getWindowRoot().setCenter(textArea);
    }

}
