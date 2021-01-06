package com.example.duitku.budget.form.components;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.duitku.R;
import com.example.duitku.budget.BudgetController;

public class BudgetPeriodComponent extends View {

    private ConstraintLayout periodConstraintLayout;
    private Spinner periodSpinner;

    private final View rootView;
    private AppCompatActivity activity = null;

    private int budgetTypePos = 0;

    public BudgetPeriodComponent(Context context, View rootView, Object activity) {
        super(context);

        this.rootView = rootView;
        if (activity instanceof AppCompatActivity){
            this.activity = (AppCompatActivity) activity;
        }

        setUpUI();
    }

    private void setUpUI(){
        initialize();
        setUpSpinner();
    }

    private void initialize(){
        if (rootView == null){
            periodConstraintLayout = activity.findViewById(R.id.budget_period_constraintlayout);
            periodSpinner = activity.findViewById(R.id.budget_period_spinner);
        } else {
            periodConstraintLayout = rootView.findViewById(R.id.budget_period_constraintlayout);
            periodSpinner = rootView.findViewById(R.id.budget_period_spinner);
        }
    }

    private void setUpSpinner(){
        ArrayAdapter<String> periodAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, BudgetController.budgetPeriod);
        periodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        periodSpinner.setAdapter(periodAdapter);
        periodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                budgetTypePos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void setVisibility(int view){
        periodConstraintLayout.setVisibility(view);
    }

    public int getBudgetTypePos(){
        return budgetTypePos;
    }

    public void setBudgetTypePos(int budgetTypePos){
        this.budgetTypePos = budgetTypePos;
        periodSpinner.setSelection(budgetTypePos);
    }

}
