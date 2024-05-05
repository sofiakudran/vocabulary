module com.example.lab3__ {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.lab3__ to javafx.fxml;
    exports com.example.lab3__;
}