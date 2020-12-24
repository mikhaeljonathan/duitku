package com.example.duitku.date;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.duitku.R;

import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment {

    private Date date;
    private DatePickerDialog.OnDateSetListener listener;

    public DatePickerFragment(Date date, DatePickerDialog.OnDateSetListener listener){
        this.date = date;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance(); //default
        if (date != null) {
            calendar.setTime(date); //custom date (bukan default)
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.datepicker, listener, year, month, day);
        return datePickerDialog;
    }
}
