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

import com.example.duitku.Utility;
import com.example.duitku.R;

import java.util.Calendar;

public class YearPickerDialog extends AppCompatDialogFragment {

    private Spinner yearSpinner;
    private Button pickButton;

    private int mYear;

    private PickYearListener mListener;

    public YearPickerDialog(PickYearListener listener, int year){
        super();
        mYear = year;
        mListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_year_picker, null);

        yearSpinner = view.findViewById(R.id.year_picker_year_spinner);
        pickButton = view.findViewById(R.id.year_picker_pick_btn);

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, Utility.generateYear());
        yearSpinner.setAdapter(yearAdapter);

        final Calendar calendar = Calendar.getInstance();
        yearSpinner.setSelection(mYear - calendar.get(Calendar.YEAR) + 5);

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
                mListener.pickYear(mYear);
                dismiss();
            }
        });

        builder.setView(view);

        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary);
        return dialog;

    }

    public interface PickYearListener{
        void pickYear(int year);
    }
}
