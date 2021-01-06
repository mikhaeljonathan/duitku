package com.example.duitku.transaction.form.components;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
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

import java.util.Calendar;
import java.util.Date;

public class TransactionDateComponent extends View {

    private ConstraintLayout dateConstraintLayout;
    private TextView dateTextView;

    private final View rootView;
    private AppCompatActivity activity = null;
    private Fragment fragment = null;

    private Date date = Calendar.getInstance().getTime();

    public TransactionDateComponent(Context context, View rootView, Object activity) {
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
        dateConstraintLayout = rootView.findViewById(R.id.transaction_date_constraintlayout);
        dateTextView = rootView.findViewById(R.id.transaction_date_textview);
    }

    private void setUpComponents(){
        final DatePickerDialog.OnDateSetListener listener = new android.app.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                date = Utility.convertElementsToDate(year, month, dayOfMonth);
                dateTextView.setText(Utility.convertDateToFullString(date));
            }
        };
        dateConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment(date, listener);
                FragmentManager fm;
                if (activity == null){
                    fm = fragment.getFragmentManager();
                } else {
                    fm = activity.getSupportFragmentManager();
                }
                datePicker.show(fm, "Date Picker Dialog");
            }
        });
    }

    public Date getDate(){
        return date;
    }

}
