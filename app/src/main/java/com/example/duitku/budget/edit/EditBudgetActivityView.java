package com.example.duitku.budget.edit;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.example.duitku.R;
import com.example.duitku.budget.Budget;
import com.example.duitku.budget.BudgetController;
import com.example.duitku.category.Category;
import com.example.duitku.category.CategoryController;
import com.example.duitku.category.PickCategoryDialog;
import com.example.duitku.database.DuitkuContract;
import com.example.duitku.date.DatePickerFragment;
import com.example.duitku.interfaces.UIView;
import com.example.duitku.main.Utility;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;

public class EditBudgetActivityView implements UIView {

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
    private Button saveBudgetBtn;

    private CategoryController categoryController;
    private BudgetController budgetController;

    private long id;
    private double amount;
    private double used;
    private Date startDate;
    private Date endDate;
    private String type;
    private long categoryId;
    private int budgetTypePos;

    private Budget budget;
    private AppCompatActivity activity;

    public EditBudgetActivityView(long id, AppCompatActivity activity){
        Log.v("HAHA", Long.toString(id));
        this.id = id;
        this.activity = activity;

        budgetController = new BudgetController(activity);
        categoryController = new CategoryController(activity);

        budget = budgetController.getBudgetById(id);

        amount = budget.getAmount();
        used = budget.getUsed();
        startDate = budget.getStartDate();
        endDate = budget.getEndDate();
        type = budget.getType();
        categoryId = budget.getCategoryId();
        if (type == null){
            budgetTypePos = 0;
        } else {
            budgetTypePos = BudgetController.budgetPeriodMap.get(type);
        }

    }

    @Override
    public void setUpUI() {
        activity.setContentView(R.layout.activity_edit_budget);

        setUpField();
        setUpButtons();
    }

    private void setUpField(){
        setUpCategory();
        setUpAmount();
        setUpStartDate();
        setUpEndDate();
        setUpCheckBox();
        setUpType();
    }

    private void setUpCategory(){
        categoryConstraintLayout = activity.findViewById(R.id.budget_category_constraintlayout);
        categoryTextView = activity.findViewById(R.id.budget_category_textview);
        errorCategoryTextView = activity.findViewById(R.id.budget_category_error_textview);
        errorCategoryTextView.setVisibility(View.GONE);

        Category category = categoryController.getCategoryById(categoryId);
        categoryTextView.setText(category.getName());
        categoryId = id;

        final PickCategoryDialog.PickCategoryListener categoryPickerListener = new PickCategoryDialog.PickCategoryListener() {
            @Override
            public void pickCategory(long id) {
                Category category = categoryController.getCategoryById(id);
                categoryTextView.setText(category.getName());
                categoryId = id;
            }
        };
        categoryConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // yang dibudget pasti expense
                PickCategoryDialog pickCategoryDialog = new PickCategoryDialog(categoryPickerListener, DuitkuContract.CategoryEntry.TYPE_EXPENSE);
                pickCategoryDialog.show(activity.getSupportFragmentManager(), "View Categories Dialog");
            }
        });
    }

    private void setUpAmount(){
        amountLayout = activity.findViewById(R.id.budget_amount_textinputlayout);
        amountField = activity.findViewById(R.id.budget_amount_field);
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
        amountField.setText(Double.toString(amount));
    }

    private void setUpCheckBox(){
        customDateCheckBox = activity.findViewById(R.id.budget_customdate_checkbox);

        if (startDate == null){
            customDateCheckBox.setChecked(false);
        } else {
            customDateCheckBox.setChecked(true);
        }

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
    }

    private void setUpStartDate(){
        startDateConstraintLayout = activity.findViewById(R.id.budget_startdate_constraintlayout);
        startDateTextView = activity.findViewById(R.id.budget_startdate_textview);
        errorStartDateTextView = activity.findViewById(R.id.budget_startdate_error_textview);
        errorStartDateTextView.setVisibility(View.GONE);

        final DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                startDate = Utility.convertElementsToDate(year, month, dayOfMonth);
                startDateTextView.setText(Utility.convertDateToFullString(startDate));
            }
        };

        startDateConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment(startDate, listener);
                datePicker.show(activity.getSupportFragmentManager(), "Start Date Picker Dialog");
            }
        });

        if (startDate == null){
            startDateConstraintLayout.setVisibility(View.GONE);
        } else {
            startDateTextView.setText(Utility.convertDateToFullString(startDate));
        }

    }

    private void setUpEndDate(){
        endDateConstraintLayout = activity.findViewById(R.id.budget_enddate_constraintlayout);
        endDateTextView = activity.findViewById(R.id.budget_enddate_textview);
        errorEndDateTextView = activity.findViewById(R.id.budget_enddate_error_textview);
        errorEndDateTextView.setVisibility(View.GONE);

        final DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                endDate = Utility.convertElementsToDate(year, month, dayOfMonth);
                endDateTextView.setText(Utility.convertDateToFullString(endDate));
            }
        };

        endDateConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment(endDate, listener);
                datePicker.show(activity.getSupportFragmentManager(), "End Date Picker Dialog");
            }
        });

        if (endDate == null){
            endDateConstraintLayout.setVisibility(View.GONE);
        } else {
            endDateTextView.setText(Utility.convertDateToFullString(endDate));
        }
    }

    private void setUpType(){
        periodConstraintLayout = activity.findViewById(R.id.budget_period_constraintlayout);
        if (startDate != null){
            periodConstraintLayout.setVisibility(View.GONE);
        }

        periodSpinner = activity.findViewById(R.id.budget_period_spinner);
        ArrayAdapter<String> periodAdapter = new ArrayAdapter<>(activity, R.layout.spinner_item, BudgetController.budgetPeriod);
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
        periodSpinner.setSelection(budgetTypePos);
    }

    private void setUpButtons(){
        setUpSaveButton();
        setUpDeleteButton();
        setUpBackButton();
    }

    private void setUpSaveButton(){
        saveBudgetBtn = activity.findViewById(R.id.budget_save_btn);
        saveBudgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateInput()) return;
                int rowsUpdated = updateBudget();
                if (rowsUpdated == 0){
                    Toast.makeText(activity, "Error updating budget", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "Budget updated", Toast.LENGTH_SHORT).show();
                }
                activity.finish();
            }
        });
    }

    private boolean validateInput(){
        // category
        Budget budgetTemp = budgetController.getBudgetByCategoryId(categoryId);
        if (budgetTemp != null && id != budgetTemp.getId()){
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

    private int updateBudget(){
        budget.setAmount(amount);
        budget.setUsed(0);
        if (customDateCheckBox.isChecked()){
            budget.setStartDate(startDate);
            budget.setEndDate(endDate);
            budget.setType(null);
        } else {
            budget.setStartDate(null);
            budget.setEndDate(null);
            budget.setType(BudgetController.budgetType[budgetTypePos]);
        }
        budget.setCategoryId(categoryId);

        int rowsUpdated = budgetController.updateBudget(budget);
        return rowsUpdated;
    }

    private void setUpDeleteButton(){
        Button deleteBtn = activity.findViewById(R.id.edit_budget_delete_btn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteConfirmationDialog();
            }
        });
    }

    private void showDeleteConfirmationDialog(){
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

    private void deleteBudget(){
        int rowsDeleted = budgetController.deleteBudget(id);
        if (rowsDeleted == 0){
            Toast.makeText(activity, "Error deleting budget", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "Budget is deleted", Toast.LENGTH_SHORT).show();
        }
        activity.finish();
    }

    private void setUpBackButton(){
        ImageView backBtn = activity.findViewById(R.id.edit_budget_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
    }

    @Override
    public View getView() {
        return null;
    }
}
