package com.example.expensetrackergui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class    ExpenseApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ExpenseApplication.class.getResource("expense-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1800, 1000);
        scene.getStylesheets().add(
                ExpenseApplication.class
                        .getResource("style.css")
                        .toExternalForm()
        );
        stage.setTitle("Expense Tracker");
        stage.setScene(scene);
        stage.show();

    }
}
