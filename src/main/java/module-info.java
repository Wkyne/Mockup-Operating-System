module com.gummybear {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires com.google.gson;

    requires java.desktop;
    requires javafx.graphics;
    requires javafx.base;
    requires com.google.errorprone.annotations;

    exports com.gummybear;
    exports com.gummybear.desktop;
    exports com.gummybear.desktop.icon;
    exports com.gummybear.desktop.terminal;

    opens com.gummybear to javafx.fxml;
    opens com.gummybear.data to javafx.base, com.google.gson;
}
