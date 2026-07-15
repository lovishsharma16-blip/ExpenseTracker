package com.example.expensetrackergui;
import java.util.ArrayList;
public class ExpenseManager {
    private ArrayList<Expense> expenses;

    public ExpenseManager() {
        expenses = new ArrayList<>();
    }

    public Expense searchExpense(int ExpenseID) {
        for (Expense expense : expenses) {
            if (expense.getExpenseID() == ExpenseID) {
                return expense;
            }
        }
        return null;
    }

    public boolean addExpense(Expense expense) {

        if (searchExpense(expense.getExpenseID()) != null) {
            return false;
        }

        expenses.add(expense);
        return true;
    }

    public boolean deleteExpense(int expenseID) {

        Expense expense = searchExpense(expenseID);

        if (expense == null) {

            System.out.println("EXPENSE NOT FOUND");
            return false;

        } else {

            expenses.remove(expense);

            System.out.println("EXPENSE DELETED SUCCESSFULLY");
            return true;

        }
    }

    public double getTotalExpense() {
        double totalExpense = 0;
        for (Expense expense : expenses) {
            totalExpense += expense.getAmount();

        }
        return totalExpense;
    }
    public ArrayList<Expense> getExpense(){
        return expenses;
    }
}

