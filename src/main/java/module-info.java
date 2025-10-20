module com.gummybear {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires com.google.gson;
    
    requires java.desktop;
    requires javafx.graphics;
    requires javafx.base;
    
    exports com.gummybear;
    exports com.gummybear.desktop;
    exports com.gummybear.desktop.icon;
    
    opens com.gummybear.filemanagement to com.google.gson;
    opens com.gummybear to javafx.fxml;
}
