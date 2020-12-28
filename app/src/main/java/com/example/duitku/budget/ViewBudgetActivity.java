package com.example.duitku.budget;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.duitku.R;
import com.example.duitku.database.DuitkuContract.TransactionEntry;
import com.example.duitku.transaction.Transaction;
import com.example.duitku.transaction.TransactionController;

import android.database.Cursor;
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
        List<Transaction> transactionList = transactionController.getTransactionsByBudget(budget);
        viewBudgetActivityView.setTransactionList(transactionList);
        viewBudgetActivityView.setUpUI();
    }

}