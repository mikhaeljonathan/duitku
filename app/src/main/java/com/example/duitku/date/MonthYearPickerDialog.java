package com.example.duitku.date;

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

import com.example.duitku.main.Utility;
import com.example.duitku.R;

import java.util.Calendar;

public class MonthYearPickerDialog extends AppCompatDialogFragment {

    private Spinner monthSpinner;
    private Spinner yearSpinner;
    private Button pickButton;

    private int month;
    private int year;

    private PickMonthYearListener listener;

    public MonthYearPickerDialog(PickMonthYearListener listener, int month, int year){
        this.listener = listener;
        this.month = month;
        this.year = year;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_monthyear_picker, null);

        setUpSpinner(view);
        setUpPickButton(view);

        builder.setView(view);

        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary); //biar bg gelap
        return dialog;
    }

    private void setUpSpinner(View view){
        // initialize spinner
        monthSpinner = view.findViewById(R.id.monthyear_picker_month_spinner);
        yearSpinner = view.findViewById(R.id.monthyear_picker_year_spinner);

        // setup isi spinnernya
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, Utility.monthsName);
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, Utility.generateYear());

        // setup style isi spinnernya
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        monthSpinner.setAdapter(monthAdapter);
        yearSpinner.setAdapter(yearAdapter);

        // set current spinner
        final Calendar calendar = Calendar.getInstance();
        monthSpinner.setSelection(month);
        yearSpinner.setSelection(year - calendar.get(Calendar.YEAR) + 5);

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                month = Utility.monthPosition().get(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                year = Integer.parseInt(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setUpPickButton(View view){
        pickButton = view.findViewById(R.id.monthyear_picker_pick_btn);
        pickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.pickMonthYear(month, year);
                dismiss();
            }
        });
    }

    public interface PickMonthYearListener{
        void pickMonthYear(int month, int year);
    }

}
