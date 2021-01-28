package com.example.duitku.budget.edit;

import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.duitku.R;
import com.example.duitku.budget.Budget;
import com.example.duitku.budget.BudgetController;
import com.example.duitku.budget.form.BudgetForm;
import com.example.duitku.interfaces.UIView;

import java.util.Date;

public class EditBudgetActivityView implements UIView {

    private BudgetForm budgetForm;
    private Button deleteBtn;

    private final AppCompatActivity activity;

    private final BudgetController budgetController;

    private final Budget budget;

    public EditBudgetActivityView(long id, AppCompatActivity activity) {
        this.activity = activity;

        budgetController = new BudgetController(activity);
        budget = budgetController.getBudgetById(id);
    }

    @Override
    public void setUpUI() {
        setUpViews();
        setUpForm();
        setUpButtons();
    }

    private void setUpViews() {
        activity.setContentView(R.layout.activity_edit);

        TextView titleTV = activity.findViewById(R.id.activity_edit_title);
        titleTV.setText("Edit Budget");

        LinearLayout budgetFormContainer = activity.findViewById(R.id.activity_edit_form);
        budgetFormContainer.addView(activity.getLayoutInflater().inflate(R.layout.form_budget,
                (ViewGroup) activity.findViewById(R.id.form_budget_constraintlayout)));

        deleteBtn = activity.findViewById(R.id.activity_edit_delete);
        deleteBtn.setText("Delete Budget");
    }

    private void setUpForm() {
        budgetForm = new BudgetForm(activity, null, activity);
        budgetForm.setUpUI();
        budgetForm.setAmount(budget.getBudget_amount());
        budgetForm.setStartDate(budget.getBudget_startdate());
        budgetForm.setEndDate(budget.getBudget_enddate());
        budgetForm.setBudgetType(budget.getBudget_type());
        budgetForm.setCategoryId(budget.getCategory_id());
    }

    private void setUpButtons() {
        setUpSaveButton();
        setUpDeleteButton();
        setUpBackButton();
    }

    private void setUpSaveButton() {
        Button saveBudgetBtn = activity.findViewById(R.id.budget_save_btn);
        saveBudgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!budgetForm.validateInput()) return;
                int rowsUpdated = updateBudget();
                if (rowsUpdated == 0) {
                    Toast.makeText(activity, "Error updating budget", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "Budget updated", Toast.LENGTH_SHORT).show();
                }
                activity.finish();
            }
        });
    }

    private int updateBudget() {
        double amount = budgetForm.getAmount();
        Date startDate = budgetForm.getStartDate();
        Date endDate = budgetForm.getEndDate();
        int budgetTypePos = budgetForm.getBudgetTypePos();
        long categoryId = budgetForm.getCategoryId();

        budget.setBudget_amount(amount);
        budget.setBudget_used(0);
        if (budgetForm.getCustomDateCheckBox().isChecked()) {
            budget.setBudget_startdate(startDate);
            budget.setBudget_enddate(endDate);
            budget.setBudget_type(null);
        } else {
            budget.setBudget_startdate(null);
            budget.setBudget_enddate(null);
            budget.setBudget_type(BudgetController.budgetType[budgetTypePos]);
        }
        budget.setCategory_id(categoryId);

        return budgetController.updateAndRestartBudget(budget);
    }

    private void setUpDeleteButton() {
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteConfirmationDialog();
            }
        });
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity, R.style.AlertDialogCustom);
        alertDialogBuilder.setTitle("Delete Confirmation");
        alertDialogBuilder.setMessage("Are you sure to delete this budget?\nYou can't undo this")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteBudget();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary); //biar bg gelap
        alertDialog.show();
    }

    private void deleteBudget() {
        int rowsDeleted = budgetController.deleteBudget(budget);
        if (rowsDeleted == 0) {
            Toast.makeText(activity, "Error deleting budget", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "Budget is deleted", Toast.LENGTH_SHORT).show();
        }
        activity.finish();
    }

    private void setUpBackButton() {
        ImageView backBtn = activity.findViewById(R.id.activity_edit_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
    }

    @Override
    public View getView() {
        return activity.getLayoutInflater().inflate(R.layout.activity_edit,
                (ViewGroup) activity.findViewById(R.id.activity_edit_constraintlayout));
    }
}
