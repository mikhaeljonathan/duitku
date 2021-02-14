package duitku.project.se.budget.edit;

import androidx.appcompat.app.AppCompatActivity;

import duitku.project.se.R;
import duitku.project.se.interfaces.UIView;

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