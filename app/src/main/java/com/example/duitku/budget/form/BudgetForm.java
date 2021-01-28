package com.example.duitku.budget.form;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.duitku.R;
import com.example.duitku.budget.BudgetController;
import com.example.duitku.budget.form.components.BudgetAmountComponent;
import com.example.duitku.budget.form.components.BudgetCategoryComponent;
import com.example.duitku.budget.form.components.BudgetDateComponent;
import com.example.duitku.budget.form.components.BudgetPeriodComponent;

import java.util.Date;

public class BudgetForm extends View {

    private BudgetCategoryComponent categoryComponent;
    private BudgetAmountComponent amountComponent;
    private CheckBox customDateCheckBox;
    private BudgetPeriodComponent periodComponent;
    private BudgetDateComponent dateComponent;

    private final View rootView;
    private final Object activity;

    public BudgetForm(Context context, View rootView, Object activity) {
        super(context);
        this.rootView = rootView;
        this.activity = activity;
    }

    public void setUpUI() {
        setUpFields();
    }

    private void setUpFields() {
        categoryComponent = new BudgetCategoryComponent(getContext(), rootView, activity);
        amountComponent = new BudgetAmountComponent(getContext(), rootView, activity);
        periodComponent = new BudgetPeriodComponent(getContext(), rootView, activity);
        dateComponent = new BudgetDateComponent(getContext(), rootView, activity);
        setUpCustomDateCheckBox();
    }

    private void setUpCustomDateCheckBox() {
        if (rootView == null) {
            AppCompatActivity temp = (AppCompatActivity) activity;
            customDateCheckBox = temp.findViewById(R.id.budget_customdate_checkbox);
        } else {
            customDateCheckBox = rootView.findViewById(R.id.budget_customdate_checkbox);
        }
        customDateCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    dateComponent.setVisibility(View.VISIBLE);
                    periodComponent.setVisibility(View.GONE);
                } else {
                    dateComponent.setVisibility(View.GONE);
                    periodComponent.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public boolean validateInput() {
        if (!categoryComponent.validateInput()) return false;
        if (!amountComponent.validateInput()) return false;
        return dateComponent.validateInput(customDateCheckBox);
    }

    public CheckBox getCustomDateCheckBox() {
        return customDateCheckBox;
    }

    public double getAmount() {
        return amountComponent.getAmount();
    }

    public Date getStartDate() {
        return dateComponent.getStartDate();
    }

    public Date getEndDate() {
        return dateComponent.getEndDate();
    }

    public int getBudgetTypePos() {
        return periodComponent.getBudgetTypePos();
    }

    public long getCategoryId() {
        return categoryComponent.getCategoryId();
    }

    public void setAmount(double amount) {
        amountComponent.setAmount(amount);
    }

    public void setStartDate(Date date) {
        dateComponent.setStartDate(date);
        if (date != null) {
            customDateCheckBox.setChecked(true);
        }
    }

    public void setEndDate(Date date) {
        dateComponent.setEndDate(date);
    }

    public void setBudgetType(String type) {
        int budgetTypePos = 0;
        if (type != null) {
            budgetTypePos = BudgetController.budgetPeriodMap.get(type);
        }
        periodComponent.setBudgetTypePos(budgetTypePos);
    }

    public void setCategoryId(long id) {
        categoryComponent.setCategoryId(id);
    }

}
