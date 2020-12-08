package com.example.duitku.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.duitku.DateValue;
import com.example.duitku.R;

import java.util.Calendar;

public class MonthYearPickerDialog extends AppCompatDialogFragment {

    private Spinner monthSpinner;
    private Spinner yearSpinner;
    private Button pickButton;

    private int mMonth;
    private int mYear;

    private PickMonthYearListener listener;

    public MonthYearPickerDialog(PickMonthYearListener mListener, int month, int year){
        super();
        listener = mListener;
        mMonth = month;
        mYear = year;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_monthyear_picker, null);

        monthSpinner = view.findViewById(R.id.dialog_month_spinner);
        yearSpinner = view.findViewById(R.id.dialog_year_spinner);
        pickButton = view.findViewById(R.id.dialog_monthyear_pick_btn);

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, DateValue.monthsName);
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, DateValue.generateYear());

        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        monthSpinner.setAdapter(monthAdapter);
        yearSpinner.setAdapter(yearAdapter);

        final Calendar calendar = Calendar.getInstance();
        monthSpinner.setSelection(mMonth);
        yearSpinner.setSelection(mYear - calendar.get(Calendar.YEAR) + 5);

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mMonth = DateValue.monthPosition().get(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mMonth = calendar.get(Calendar.MONTH);
            }
        });

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mYear = Integer.parseInt(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mYear = calendar.get(Calendar.YEAR);
            }
        });

        pickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.pickMonthYear(mMonth, mYear);
                dismiss();
            }
        });


        builder.setView(view);

        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary);
        return dialog;

    }

    public interface PickMonthYearListener{
        void pickMonthYear(int month, int year);
    }

}
