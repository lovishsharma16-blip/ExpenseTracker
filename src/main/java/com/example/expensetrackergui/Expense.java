package com.example.expensetrackergui;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
public class Expense {
    private int expenseID;
    private String title;
    private double amount;
    private String category;
    private LocalDate date;

    public Expense(int expenseID, String title, double amount, String category, LocalDate date) {
        this.expenseID = expenseID;
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    public int getExpenseID() {
        return expenseID;
    }

    public String getTitle() {
        return title;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getDate() {
        return date;
    }
}
