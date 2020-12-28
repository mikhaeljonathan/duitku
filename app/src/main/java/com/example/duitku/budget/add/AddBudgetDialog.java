package com.example.duitku.budget.add;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.example.duitku.R;
import com.example.duitku.budget.Budget;
import com.example.duitku.budget.BudgetController;
import com.example.duitku.category.Category;
import com.example.duitku.category.CategoryController;
import com.example.duitku.database.DuitkuContract.CategoryEntry;
import com.example.duitku.date.DatePickerFragment;
import com.example.duitku.category.PickCategoryDialog;
import com.example.duitku.main.Utility;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;

public class AddBudgetDialog extends AppCompatDialogFragment {

    private ConstraintLayout categoryConstraintLayout;
    private TextView categoryTextView;
    private TextView errorCategoryTextView;
    private TextInputLayout amountLayout;
    private TextInputEditText amountField;
    private ConstraintLayout periodConstraintLayout;
    private Spinner periodSpinner;
    private CheckBox customDateCheckBox;
    private ConstraintLayout startDateConstraintLayout;
    private TextView startDateTextView;
    private TextView errorStartDateTextView;
    private ConstraintLayout endDateConstraintLayout;
    private TextView endDateTextView;
    private TextView errorEndDateTextView;
    private Button addBudgetBtn;

    private DatePickerDialog.OnDateSetListener startDatePickerListener;
    private DatePickerDialog.OnDateSetListener endDatePickerListener;
    private PickCategoryDialog.PickCategoryListener categoryPickerListener;

    private CategoryController categoryController;
    private BudgetController budgetController;

    private long categoryId;
    private double amount;
    private int budgetTypePos;
    private Date startDate;
    private Date endDate;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        categoryId = -1;
        budgetTypePos = 0;
        startDate = null;
        endDate = null;

        categoryController = new CategoryController(getActivity());
        budgetController = new BudgetController(getActivity());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_budget, null);

        setUpUI(view);

        builder.setView(view);

        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary); //biar bg gelap
        return dialog;
    }

    private void setUpUI(View view){
        categoryConstraintLayout = view.findViewById(R.id.add_budget_category_constraintlayout);
        categoryTextView = view.findViewById(R.id.add_budget_category_textview);
        errorCategoryTextView = view.findViewById(R.id.add_budget_category_error_textview);
        amountLayout = view.findViewById(R.id.add_budget_amount_textinputlayout);
        amountField = view.findViewById(R.id.add_budget_amount_field);
        periodConstraintLayout = view.findViewById(R.id.add_budget_period_constraintlayout);
        periodSpinner = view.findViewById(R.id.add_budget_period_spinner);
        customDateCheckBox = view.findViewById(R.id.add_budget_customdate_checkbox);
        startDateConstraintLayout = view.findViewById(R.id.add_budget_startdate_constraintlayout);
        startDateTextView = view.findViewById(R.id.add_budget_startdate_textview);
        errorStartDateTextView = view.findViewById(R.id.add_budget_startdate_error_textview);
        endDateConstraintLayout = view.findViewById(R.id.add_budget_enddate_constraintlayout);
        endDateTextView = view.findViewById(R.id.add_budget_enddate_textview);
        errorEndDateTextView = view.findViewById(R.id.add_budget_enddate_error_textview);
        addBudgetBtn = view.findViewById(R.id.add_budget_add_btn);

        // error view's visibility are gone
        errorCategoryTextView.setVisibility(View.GONE);
        errorStartDateTextView.setVisibility(View.GONE);
        errorEndDateTextView.setVisibility(View.GONE);

        // text changed listener
        amountField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 9){
                    amountLayout.setError("Amount too much");
                } else {
                    amountLayout.setErrorEnabled(false);
                }
            }
        });

        // setup checkbox
        startDateConstraintLayout.setVisibility(View.GONE);
        endDateConstraintLayout.setVisibility(View.GONE);
        periodConstraintLayout.setVisibility(View.VISIBLE);
        customDateCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    startDateConstraintLayout.setVisibility(View.VISIBLE);
                    endDateConstraintLayout.setVisibility(View.VISIBLE);
                    periodConstraintLayout.setVisibility(View.GONE);
                } else {
                    startDateConstraintLayout.setVisibility(View.GONE);
                    endDateConstraintLayout.setVisibility(View.GONE);
                    periodConstraintLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        // setup button
        addBudgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateInput()) return;
                Uri uri = addBudget();
                if (uri == null){
                    Toast.makeText(getActivity(), "Error adding new budget", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Budget added", Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        });

        setUpSpinner();
        setUpPicker();
    }

    private void setUpSpinner(){
        ArrayAdapter<String> periodAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, BudgetController.budgetPeriod);
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

    private void setUpPicker(){
        setUpListener();
        startDateConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment(startDate, startDatePickerListener);
                datePicker.show(getFragmentManager(), "Start Date Picker Dialog");
            }
        });
        endDateConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment(endDate, endDatePickerListener);
                datePicker.show(getFragmentManager(), "End Date Picker Dialog");
            }
        });
        categoryConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // yang dibudget pasti expense
                PickCategoryDialog pickCategoryDialog = new PickCategoryDialog(categoryPickerListener, CategoryEntry.TYPE_EXPENSE);
                pickCategoryDialog.show(getFragmentManager(), "View Categories Dialog");
            }
        });
    }

    private void setUpListener(){
        startDatePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                startDate = Utility.convertElementsToDate(year, month, dayOfMonth);
                startDateTextView.setText(Utility.convertDateToFullString(startDate));
            }
        };
        endDatePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                endDate = Utility.convertElementsToDate(year, month, dayOfMonth);
                endDateTextView.setText(Utility.convertDateToFullString(endDate));
            }
        };
        categoryPickerListener = new PickCategoryDialog.PickCategoryListener() {
            @Override
            public void pickCategory(long id) {
                Category category = categoryController.getCategoryById(id);
                categoryTextView.setText(category.getName());
                categoryId = id;
            }
        };
    }

    private Uri addBudget(){
        String budgetType = BudgetController.budgetType[budgetTypePos];
        Budget budget = new Budget(-1, amount, 0, startDate, endDate, budgetType, categoryId);
        Uri uri = budgetController.addBudget(budget);
        return uri;
    }

    private boolean validateInput(){
        // category
        if (categoryId == -1){
            errorCategoryTextView.setText("Category has to be chosen");
            errorCategoryTextView.setVisibility(View.VISIBLE);
            return false;
        } else {
            errorCategoryTextView.setVisibility(View.GONE);
        }
        Budget budget = budgetController.getBudgetByCategoryId(categoryId);
        if (budget != null){
            errorCategoryTextView.setText("There is a budget with this category");
            errorCategoryTextView.setVisibility(View.VISIBLE);
            return false;
        } else {
            errorCategoryTextView.setVisibility(View.GONE);
        }

        // amount
        String amountString = amountField.getText().toString().trim();
        if (amountString.equals("")){
            amountLayout.setError("Amount can't be empty");
            return false;
        } else {
            amount = Double.parseDouble(amountString);
        }
        if (amount <= 0){
            amountLayout.setError("Amount not allowed");
            return false;
        }
        if (amount > 999999999){
            return false;
        }

        // startDate
        if (customDateCheckBox.isChecked() && startDate == null){
            errorStartDateTextView.setVisibility(View.VISIBLE);
            return false;
        } else {
            errorStartDateTextView.setVisibility(View.GONE);
        }

        // endDate
        if (customDateCheckBox.isChecked() && endDate == null){
            errorEndDateTextView.setText("End date has to be chosen");
            errorEndDateTextView.setVisibility(View.VISIBLE);
            return false;
        } else {
            errorEndDateTextView.setVisibility(View.GONE);
        }

        // startDate and endDate
        if (customDateCheckBox.isChecked() && startDate.compareTo(endDate) > 0){
            errorEndDateTextView.setText("End date has to be later than start date");
            errorEndDateTextView.setVisibility(View.VISIBLE);
            return false;
        } else {
            errorEndDateTextView.setVisibility(View.GONE);
        }

        return true;
    }

}
