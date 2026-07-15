module com.example.expensetrackergui {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.expensetrackergui to javafx.fxml;
    exports com.example.expensetrackergui;
}