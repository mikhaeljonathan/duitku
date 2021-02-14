package duitku.project.se.budget.view;

import androidx.appcompat.app.AppCompatActivity;

import duitku.project.se.budget.Budget;
import duitku.project.se.budget.BudgetController;
import duitku.project.se.transaction.Transaction;
import duitku.project.se.transaction.TransactionController;

import android.os.Bundle;

import java.util.List;

public class ViewBudgetActivity extends AppCompatActivity {

    private ViewBudgetActivityView viewBudgetActivityView;

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

        Budget budget = new BudgetController(this).getBudgetById(budgetId);
        if (budget == null) {
            finish();
            return;
        }

        List<Transaction> transactionList = new TransactionController(this).getTransactionsByBudget(budget);
        viewBudgetActivityView.setTransactionList(transactionList);
        viewBudgetActivityView.setUpUI();
    }

}