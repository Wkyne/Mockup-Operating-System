module com.gummybear {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires java.desktop;

    opens com.gummybear to javafx.fxml;
    exports com.gummybear;
    exports com.gummybear.desktop;
    exports com.gummybear.desktop.icon;
}
