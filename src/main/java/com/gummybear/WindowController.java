package com.gummybear;

import com.gummybear.desktop.Desktop;
import com.gummybear.desktop.window.Window;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
// import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import lombok.Getter;
import lombok.Setter;

@Getter
public class WindowController {
    private boolean minimized = false;
    @Setter
    private Window windowInstance;

    @FXML
    BorderPane windowRoot;

    @FXML
    HBox windowTitleBarHBox;

    @FXML
    Label windowTitleLabel;

    @FXML
    Button windowMinimizeButton;

    @FXML
    Button windowMaximizeButton;

    @FXML
    Button windowExitButton;

    @FXML
    public void minimizeWindow() {
        System.out.println("[INFO] Minimized Window");
        windowInstance.minimize();
    }
    
    @FXML
    public void restoreWindow() {
        System.out.println("[INFO] Restored Window");
        windowInstance.restore();
    }
    
    @FXML
    public void maximizeWindow() {
        Desktop desktop = Desktop.getInstance();
        System.out.println("[INFO] " + (windowInstance.isMaximized() ? "Unmaximized" : "Maximized") + " Window: " + windowInstance.getName());
        windowInstance.maximize(desktop.getDesktopPane());
        windowMaximizeButton.setText(windowInstance.isMaximized() ? "🗗" : "🗖");
    }

    @FXML
    public void exitWindow() {
        System.out.println("[INFO] Exited Window");
        Desktop desktop = Desktop.getInstance();
        desktop.getWindowArrayList().remove(windowInstance);
        desktop.getDesktopPane().getChildren().remove(windowInstance.getWrapper());
    }

}
