package com.example.duitku.picker;

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

import com.example.duitku.Utility;
import com.example.duitku.R;

import java.util.Calendar;

public class YearPickerDialog extends AppCompatDialogFragment {

    private Spinner yearSpinner;
    private Button pickButton;

    private int year;

    private PickYearListener mListener;

    public YearPickerDialog(PickYearListener listener, int year){
        this.year = year;
        mListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_year_picker, null);

        setUpSpinner(view);
        setUpPickButton(view);

        builder.setView(view);

        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary); //biar bg gelap
        return dialog;
    }

    private void setUpSpinner(View view){
        // initialize spinner
        yearSpinner = view.findViewById(R.id.year_picker_year_spinner);

        // setup isi spinnernya
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, Utility.generateYear());

        // setup style isi spinnernya
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        yearSpinner.setAdapter(yearAdapter);

        // set current spinner
        final Calendar calendar = Calendar.getInstance();
        yearSpinner.setSelection(year - calendar.get(Calendar.YEAR) + 5);

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
        pickButton = view.findViewById(R.id.year_picker_pick_btn);
        pickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.pickYear(year);
                dismiss();
            }
        });
    }

    public interface PickYearListener{
        void pickYear(int year);
    }
}
