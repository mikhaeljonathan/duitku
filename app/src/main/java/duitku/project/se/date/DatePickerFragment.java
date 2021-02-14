package duitku.project.se.date;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import duitku.project.se.R;

import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment {

    private final Date date;
    private final DatePickerDialog.OnDateSetListener listener;

    public DatePickerFragment(Date date, DatePickerDialog.OnDateSetListener listener) {
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

        return new DatePickerDialog(getActivity(), R.style.datepicker, listener, year, month, day);
    }
}
