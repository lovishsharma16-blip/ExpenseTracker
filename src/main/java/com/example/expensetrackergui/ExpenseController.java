package com.example.expensetrackergui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.Optional;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.CategoryAxis;

import javafx.scene.chart.NumberAxis;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.*;
import java.util.HashMap;
import java.util.Map;

public class ExpenseController {

    private ExpenseManager expenseManager = new ExpenseManager();

    @FXML
    private TextField idField;

    @FXML
    private TextField titleField;

    @FXML
    private TextField amountField;

    @FXML
    private TextField categoryField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TableView<Expense> expenseTable;

    @FXML
    private TableColumn<Expense, Integer> idColumn;

    @FXML
    private TableColumn<Expense, String> titleColumn;

    @FXML
    private TableColumn<Expense, Double> amountColumn;

    @FXML
    private TableColumn<Expense, String> categoryColumn;

    @FXML
    private TableColumn<Expense, LocalDate> dateColumn;
    @FXML
    private PieChart pieChart;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private LineChart<String, Number> lineChart;

    @FXML
    private Label totalLabel;

    @FXML
    private TextField searchField;

    @FXML
    public void initialize() {

        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("expenseID"));

        titleColumn.setCellValueFactory(
                new PropertyValueFactory<>("title"));

        amountColumn.setCellValueFactory(
                new PropertyValueFactory<>("amount"));

        categoryColumn.setCellValueFactory(
                new PropertyValueFactory<>("category"));

        dateColumn.setCellValueFactory(
                new PropertyValueFactory<>("date"));

        expenseTable.setPlaceholder(
                new Label("No Expenses Available"));
    }

    @FXML
    public void addExpense() {

        try {

            if (idField.getText().isEmpty() ||
                    titleField.getText().isEmpty() ||
                    amountField.getText().isEmpty() ||
                    categoryField.getText().isEmpty() ||
                    datePicker.getValue() == null) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Please fill all fields.");
                alert.showAndWait();
                return;
            }

            int id = Integer.parseInt(idField.getText());
            String title = titleField.getText();
            double amount = Double.parseDouble(amountField.getText());
            String category = categoryField.getText();
            LocalDate date = datePicker.getValue();

            Expense expense =
                    new Expense(id, title, amount, category, date);

            Alert confirm = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Do you want to add this expense?",
                    ButtonType.YES,
                    ButtonType.NO
            );

            confirm.setHeaderText(null);

            Optional<ButtonType> result =
                    confirm.showAndWait();

            if (result.isEmpty() ||
                    result.get() != ButtonType.YES) {
                return;
            }

            if (expenseManager.addExpense(expense)) {

                displayExpenses();

                Alert alert =
                        new Alert(Alert.AlertType.INFORMATION);

                alert.setHeaderText(null);
                alert.setContentText(
                        "Expense Added Successfully!");

                alert.showAndWait();

                clearFields();

            } else {

                Alert alert =
                        new Alert(Alert.AlertType.ERROR);

                alert.setHeaderText(null);
                alert.setContentText(
                        "Expense ID Already Exists!");

                alert.showAndWait();
            }

        } catch (NumberFormatException e) {

            Alert alert =
                    new Alert(Alert.AlertType.ERROR);

            alert.setHeaderText(null);
            alert.setContentText(
                    "Expense ID and Amount must be numeric.");

            alert.showAndWait();
        }
    }

    @FXML
    private void deleteExpense() {

        try {

            int id = Integer.parseInt(idField.getText());

            Alert confirm = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to delete this expense?",
                    ButtonType.YES,
                    ButtonType.NO
            );

            confirm.setHeaderText(null);

            Optional<ButtonType> result =
                    confirm.showAndWait();

            if (result.isEmpty() ||
                    result.get() != ButtonType.YES) {
                return;
            }

            if (expenseManager.deleteExpense(id)) {

                displayExpenses();

                Alert alert =
                        new Alert(Alert.AlertType.INFORMATION);

                alert.setHeaderText(null);
                alert.setContentText(
                        "Expense Deleted Successfully!");

                alert.showAndWait();

                clearFields();

            } else {

                Alert alert =
                        new Alert(Alert.AlertType.ERROR);

                alert.setHeaderText(null);
                alert.setContentText(
                        "Expense Not Found!");

                alert.showAndWait();
            }

        } catch (NumberFormatException e) {

            Alert alert =
                    new Alert(Alert.AlertType.ERROR);

            alert.setHeaderText(null);
            alert.setContentText(
                    "Please enter a valid Expense ID.");

            alert.showAndWait();
        }
    }
    @FXML
    private void searchExpense() {

        try {

            int id = Integer.parseInt(searchField.getText());

            Expense expense = expenseManager.searchExpense(id);

            if(expense != null) {

                idField.setText(String.valueOf(expense.getExpenseID()));
                titleField.setText(expense.getTitle());
                amountField.setText(String.valueOf(expense.getAmount()));
                categoryField.setText(expense.getCategory());
                datePicker.setValue(expense.getDate());
                expenseTable.getSelectionModel().select(expense);
                expenseTable.scrollTo(expense);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Expense Found Successfully!");
                alert.showAndWait();

            } else {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Expense Not Found!");
                alert.showAndWait();
            }

        } catch(NumberFormatException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid Expense ID.");
            alert.showAndWait();
        }
    }

    @FXML
    public void displayExpenses() {

        ObservableList<Expense> list =
                FXCollections.observableArrayList(
                        expenseManager.getExpense());

        expenseTable.setItems(list);

        totalLabel.setText(
                "Total Expense : ₹" +
                        expenseManager.getTotalExpense());
        updatePieChart();
        updateBarChart();
        updateLineChart();
    }
    private void updatePieChart() {

        pieChart.getData().clear();

        Map<String, Double> categoryTotals = new HashMap<>();

        for (Expense expense : expenseManager.getExpense()) {

            categoryTotals.put(
                    expense.getCategory(),
                    categoryTotals.getOrDefault(
                            expense.getCategory(), 0.0
                    ) + expense.getAmount()
            );
        }

        for (String category : categoryTotals.keySet()) {

            pieChart.getData().add(
                    new PieChart.Data(
                            category,
                            categoryTotals.get(category)
                    )
            );
        }
    }
    private void updateBarChart() {

        barChart.getData().clear();

        XYChart.Series<String, Number> series =
                new XYChart.Series<>();

        Map<String, Double> categoryTotals = new HashMap<>();

        for (Expense expense : expenseManager.getExpense()) {

            categoryTotals.put(
                    expense.getCategory(),
                    categoryTotals.getOrDefault(
                            expense.getCategory(), 0.0
                    ) + expense.getAmount()
            );
        }

        for (String category : categoryTotals.keySet()) {

            series.getData().add(
                    new XYChart.Data<>(
                            category,
                            categoryTotals.get(category)
                    )
            );
        }

        barChart.getData().add(series);
    }
    private void updateLineChart() {

        lineChart.getData().clear();

        XYChart.Series<String, Number> series =
                new XYChart.Series<>();

        for (Expense expense : expenseManager.getExpense()) {

            series.getData().add(
                    new XYChart.Data<>(
                            expense.getDate().toString(),
                            expense.getAmount()
                    )
            );
        }

        lineChart.getData().add(series);
    }

    private void clearFields() {

        idField.clear();
        titleField.clear();
        amountField.clear();
        categoryField.clear();
        datePicker.setValue(null);
    }
}
