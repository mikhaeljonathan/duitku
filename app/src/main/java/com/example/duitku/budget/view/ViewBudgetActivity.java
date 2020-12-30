package com.example.duitku.budget.view;

import androidx.appcompat.app.AppCompatActivity;

import com.example.duitku.budget.Budget;
import com.example.duitku.budget.BudgetController;
import com.example.duitku.transaction.Transaction;
import com.example.duitku.transaction.TransactionController;

import android.os.Bundle;

import java.util.List;

public class ViewBudgetActivity extends AppCompatActivity {

    private ViewBudgetActivityView viewBudgetActivityView;
    private TransactionController transactionController = new TransactionController(this);
    private BudgetController budgetController = new BudgetController(this);

    private Budget budget;
    private long budgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        budgetId = getIntent().getLongExtra("ID", -1);

        viewBudgetActivityView = new ViewBudgetActivityView(budgetId, this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        budget = budgetController.getBudgetById(budgetId);
        if (budget == null){
            finish();
            return;
        }
        List<Transaction> transactionList = transactionController.getTransactionsByBudget(budget);
        viewBudgetActivityView.setTransactionList(transactionList);
        viewBudgetActivityView.setUpUI();
    }

}