package com.example.duitku.budget.add;

import android.app.AlertDialog;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.example.duitku.R;
import com.example.duitku.budget.Budget;
import com.example.duitku.budget.BudgetController;
import com.example.duitku.budget.form.BudgetForm;

import java.util.Date;

public class AddBudgetDialog extends AppCompatDialogFragment {

    private BudgetForm budgetForm;
    private View view;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        setUpUI();

        builder.setView(view);

        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary); //biar bg gelap
        return dialog;
    }

    private void setUpUI(){
        setUpViews();
        setUpForm();
        setUpButtons();
    }

    private void setUpViews(){
        LayoutInflater inflater = getActivity().getLayoutInflater();

        view = inflater.inflate(R.layout.dialog_add,
                (ViewGroup) getActivity().findViewById(R.id.dialog_add_constraintlayout));

        TextView titleTV = view.findViewById(R.id.dialog_add_title);
        titleTV.setText("Add Budget");

        LinearLayout budgetFormContainer = view.findViewById(R.id.dialog_add_form);
        budgetFormContainer.addView(inflater.inflate(R.layout.form_budget,
                (ViewGroup) getActivity().findViewById(R.id.form_budget_constraintlayout)));
    }

    private void setUpForm(){
        budgetForm = new BudgetForm(getActivity(), view, this);
    }

    private void setUpButtons(){
        setUpAddButton();
    }

    private void setUpAddButton(){
        Button addBudgetBtn = view.findViewById(R.id.budget_save_btn);

        addBudgetBtn.setText("Add");
        addBudgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!budgetForm.validateInput()) return;
                Uri uri = addBudget();
                if (uri == null){
                    Toast.makeText(getActivity(), "Error adding new budget", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Budget added", Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        });
    }

    private Uri addBudget(){
        BudgetController budgetController = new BudgetController(getActivity());

        double amount = budgetForm.getAmount();
        Date startDate = budgetForm.getStartDate();
        Date endDate = budgetForm.getEndDate();
        int budgetTypePos = budgetForm.getBudgetTypePos();
        long categoryId = budgetForm.getCategoryId();

        String budgetType = BudgetController.budgetType[budgetTypePos];
        if (!budgetForm.getCustomDateCheckBox().isChecked()){
            startDate = null;
            endDate = null;
        }
        Budget budget = new Budget(-1, amount, 0, startDate, endDate, budgetType, categoryId);

        return budgetController.addBudget(budget);
    }

}
