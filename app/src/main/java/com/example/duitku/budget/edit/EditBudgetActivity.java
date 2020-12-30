package com.example.duitku.budget.edit;

import androidx.appcompat.app.AppCompatActivity;

import com.example.duitku.R;
import com.example.duitku.interfaces.UIView;
import com.example.duitku.transaction.edit.EditTransactionActivityView;

import android.os.Bundle;

public class EditBudgetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long budgetId = getIntent().getLongExtra("ID", -1);
        UIView editBudgetActivityView = new EditBudgetActivityView(budgetId, this);
        editBudgetActivityView.setUpUI();
    }
}