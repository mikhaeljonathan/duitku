package com.example.duitku.dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.example.duitku.R;
import com.example.duitku.controller.BudgetController;
import com.example.duitku.database.DuitkuContract;
import com.example.duitku.model.Budget;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddBudgetDialog extends AppCompatDialogFragment implements ViewCategoriesDialog.ViewCategoriesListener {

    // variable buat view nya
    private ConstraintLayout startDateConstraintLayout;
    private ConstraintLayout endDateConstraintLayout;
    private ConstraintLayout categoryConstraintLayout;
    private TextView startDateTextView;
    private TextView endDateTextView;
    private TextView categoryTextView;
    private EditText goalAmountEditText;
    private CheckBox recurringCheckBox;
    private Button addBudgetBtn;

    private long categoryId;
    private Date startDate;
    private Date endDate;

    // listener dipisah jadi 2 soalnya ada startdate sama enddate
    private DatePickerDialog.OnDateSetListener startDatePickerListener;
    private DatePickerDialog.OnDateSetListener endDatePickerListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // bikin builderny
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_budget, null);

        // Initialize the views
        startDateConstraintLayout = view.findViewById(R.id.add_budget_startdate_constraintlayout);
        endDateConstraintLayout = view.findViewById(R.id.add_budget_enddate_constraintlayout);
        categoryConstraintLayout = view.findViewById(R.id.add_budget_category_constraintlayout);
        startDateTextView = view.findViewById(R.id.add_budget_startdate_textview);
        endDateTextView = view.findViewById(R.id.add_budget_enddate_textview);
        categoryTextView = view.findViewById(R.id.add_budget_category_textview);
        goalAmountEditText = view.findViewById(R.id.add_budget_amount_edittext);
        recurringCheckBox = view.findViewById(R.id.add_budget_recurring_textbox);
        addBudgetBtn = view.findViewById(R.id.add_budget_add_btn);

        // initialize variables
        categoryId = -1;
        startDate = null;
        endDate = null;

        // set date pickers
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
        // set the ondatelistener
        startDatePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                // ambil value date dari datepicker
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                startDate = calendar.getTime();

                // ubah jadi string
                String currentDateString = DateFormat.getDateInstance(DateFormat.SHORT).format(startDate);

                // update di startdate textview
                startDateTextView.setText(currentDateString);

            }
        };
        endDatePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                // ambil value date dari datepicker
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                endDate = calendar.getTime();

                // ubah jadi string
                String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(endDate);

                // update di startdate textview
                endDateTextView.setText(currentDateString);
            }
        };
        categoryConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewCategoriesDialog viewCategoriesDialog = new ViewCategoriesDialog(AddBudgetDialog.this, null);
                viewCategoriesDialog.show(getFragmentManager(), "View Categories Dialog");
            }
        });
        addBudgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBudget();
            }
        });

        builder.setView(view);

        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary);
        return dialog;

    }

    private void addBudget(){

        // ambil data nya
        String sd = DateFormat.getDateInstance(DateFormat.SHORT).format(startDate);
        String ed = DateFormat.getDateInstance(DateFormat.SHORT).format(endDate);
        Double amount = Double.parseDouble(goalAmountEditText.getText().toString().trim());
        boolean recurring = recurringCheckBox.isChecked();

        // bikin object budget dan insert ke db
        Budget budget = new Budget(sd, ed, categoryId, amount, recurring);
        Uri uri = new BudgetController(getContext()).addBudget(budget);

        // kasih pesan sukses / error
        if (uri == null){
            Toast.makeText(getContext(), "Error adding new budget", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Budget added", Toast.LENGTH_SHORT).show();
        }

        dismiss();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void pickCategory(long id) {

        // tampilin nama category nya
        categoryId = id;
        Uri currentCategoryUri = ContentUris.withAppendedId(DuitkuContract.CategoryEntry.CONTENT_URI, categoryId);
        String[] projection = new String[]{ DuitkuContract.CategoryEntry.COLUMN_ID, DuitkuContract.CategoryEntry.COLUMN_NAME};
        Cursor cursor = getContext().getContentResolver().query(currentCategoryUri, projection, null, null);
        if (cursor.moveToFirst()){
            String categoryName = cursor.getString(cursor.getColumnIndex(DuitkuContract.CategoryEntry.COLUMN_NAME));
            categoryTextView.setText(categoryName);
        }

    }

}
