module com.gummybear {
    requires javafx.controls;
    requires javafx.fxml;
    
    opens com.gummybear to javafx.fxml;
    exports com.gummybear;
}
