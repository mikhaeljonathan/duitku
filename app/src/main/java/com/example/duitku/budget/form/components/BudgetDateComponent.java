package com.example.duitku.budget.form.components;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.duitku.R;
import com.example.duitku.date.DatePickerFragment;
import com.example.duitku.main.Utility;

import java.util.Date;

public class BudgetDateComponent extends View {

    private ConstraintLayout startDateConstraintLayout;
    private TextView startDateTextView;
    private TextView errorStartDateTextView;
    private ConstraintLayout endDateConstraintLayout;
    private TextView endDateTextView;
    private TextView errorEndDateTextView;

    private final View rootView;
    private AppCompatActivity activity = null;
    private Fragment fragment = null;

    private Date startDate = null;
    private Date endDate = null;

    public BudgetDateComponent(Context context, View rootView, Object activity) {
        super(context);

        this.rootView = rootView;
        if (activity instanceof AppCompatActivity){
            this.activity = (AppCompatActivity) activity;
        } else {
            this.fragment = (Fragment) activity;
        }

        setUpUI();
    }

    private void setUpUI(){
        initialize();
        setUpComponents();
    }

    private void initialize(){
        if (rootView == null){
            startDateConstraintLayout = activity.findViewById(R.id.budget_startdate_constraintlayout);
            startDateTextView = activity.findViewById(R.id.budget_startdate_textview);
            errorStartDateTextView = activity.findViewById(R.id.budget_startdate_error_textview);
            endDateConstraintLayout = activity.findViewById(R.id.budget_enddate_constraintlayout);
            endDateTextView = activity.findViewById(R.id.budget_enddate_textview);
            errorEndDateTextView = activity.findViewById(R.id.budget_enddate_error_textview);
        } else {
            startDateConstraintLayout = rootView.findViewById(R.id.budget_startdate_constraintlayout);
            startDateTextView = rootView.findViewById(R.id.budget_startdate_textview);
            errorStartDateTextView = rootView.findViewById(R.id.budget_startdate_error_textview);
            endDateConstraintLayout = rootView.findViewById(R.id.budget_enddate_constraintlayout);
            endDateTextView = rootView.findViewById(R.id.budget_enddate_textview);
            errorEndDateTextView = rootView.findViewById(R.id.budget_enddate_error_textview);
        }
    }

    private void setUpComponents(){
        this.setVisibility(View.GONE);
        errorStartDateTextView.setVisibility(View.GONE);
        errorEndDateTextView.setVisibility(View.GONE);

        setUpStartDatePicker();
        setUpEndDatePicker();
    }

    private void setUpStartDatePicker(){
        final DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                Date date = Utility.convertElementsToDate(year, month, dayOfMonth);
                startDateTextView.setText(Utility.convertDateToFullString(date));
                changeStartDateToWhite();
                startDate = date;
            }
        };
        startDateConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment(startDate, listener);
                FragmentManager fm;
                if (activity == null){
                    fm = fragment.getFragmentManager();
                } else {
                    fm = activity.getSupportFragmentManager();
                }
                datePicker.show(fm, "Start Date Picker Dialog");
            }
        });
    }

    private void setUpEndDatePicker(){
        final DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                Date date = Utility.convertElementsToDate(year, month, dayOfMonth);
                endDateTextView.setText(Utility.convertDateToFullString(date));
                changeEndDateToWhite();
                endDate = date;
            }
        };
        endDateConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment(endDate, listener);
                FragmentManager fm;
                if (activity == null){
                    fm = fragment.getFragmentManager();
                } else {
                    fm = activity.getSupportFragmentManager();
                }
                datePicker.show(fm, "End Date Picker Dialog");
            }
        });
    }

    private void changeStartDateToWhite(){
        if (fragment == null){
            startDateTextView.setTextColor(activity.getResources().getColor(android.R.color.white));
        } else {
            startDateTextView.setTextColor(fragment.getResources().getColor(android.R.color.white));
        }
    }

    private void changeEndDateToWhite(){
        if (fragment == null){
            endDateTextView.setTextColor(activity.getResources().getColor(android.R.color.white));
        } else {
            endDateTextView.setTextColor(fragment.getResources().getColor(android.R.color.white));
        }
    }

    public void setVisibility(int view){
        startDateConstraintLayout.setVisibility(view);
        endDateConstraintLayout.setVisibility(view);
    }

    public boolean validateInput(CheckBox customDateCheckBox){
        if (customDateCheckBox.isChecked() && startDate == null){
            errorStartDateTextView.setVisibility(View.VISIBLE);
            return false;
        } else {
            errorStartDateTextView.setVisibility(View.GONE);
        }

        if (customDateCheckBox.isChecked() && endDate == null){
            errorEndDateTextView.setText("End date has to be chosen");
            errorEndDateTextView.setVisibility(View.VISIBLE);
            return false;
        } else {
            errorEndDateTextView.setVisibility(View.GONE);
        }

        if (customDateCheckBox.isChecked() && startDate.compareTo(endDate) > 0){
            errorEndDateTextView.setText("End date has to be later than start date");
            errorEndDateTextView.setVisibility(View.VISIBLE);
            return false;
        } else {
            errorEndDateTextView.setVisibility(View.GONE);
        }

        return true;
    }

    public Date getStartDate(){
        return startDate;
    }

    public Date getEndDate(){
        return endDate;
    }

    public void setStartDate(Date date){
        startDate = date;
        if (startDate != null){
            startDateTextView.setText(Utility.convertDateToFullString(date));
        }
        changeStartDateToWhite();
    }

    public void setEndDate(Date date){
        endDate = date;
        if (endDate != null){
            endDateTextView.setText(Utility.convertDateToFullString(date));
        }
        changeEndDateToWhite();
    }

}
