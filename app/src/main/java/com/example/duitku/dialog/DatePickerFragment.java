package com.example.duitku.dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.duitku.R;

import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment {

    private Date mStartDate;
    private DatePickerDialog.OnDateSetListener mListener;

    public DatePickerFragment(Date startDate, DatePickerDialog.OnDateSetListener listener){
        super();
        mStartDate = startDate;
        mListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // hari ini
        Calendar calendar = Calendar.getInstance();

        // sudah terpilih (bisa hari ini bisa aja bukan)
        if (mStartDate != null) {
            calendar.setTime(mStartDate);
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.datepicker, mListener, year, month, day);
        return datePickerDialog;

    }
}
