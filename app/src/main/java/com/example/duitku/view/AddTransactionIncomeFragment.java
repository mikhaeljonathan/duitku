package com.example.duitku.view;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.duitku.R;
import com.example.duitku.controller.TransactionController;
import com.example.duitku.database.DuitkuContract;

public class AddTransactionIncomeFragment extends Fragment {

    private Button saveBtn;

    private EditText dateField;
    private EditText walletField;
    private EditText categoryField;
    private EditText amountField;
    private EditText descField;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_transaction_income, container, false);

        // Initialize the views
        saveBtn = rootView.findViewById(R.id.add_transaction_income_save_btn);

        dateField = rootView.findViewById(R.id.add_transaction_income_date_edittext);
        walletField = rootView.findViewById(R.id.add_transaction_income_wallet_edittext);
        categoryField = rootView.findViewById(R.id.add_transaction_income_category_edittext);
        amountField = rootView.findViewById(R.id.add_transaction_income_amount_edittext);
        descField = rootView.findViewById(R.id.add_transaction_income_desc_edittext);

        // setting save button nya
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                addTransaction();
            }
        });

        return rootView;
    }

    private void addTransaction(){

        // ambil data dari view
        String date = dateField.getText().toString().trim();
        String wallet = walletField.getText().toString().trim();
        String category = categoryField.getText().toString().trim();
        double amount = Double.parseDouble(amountField.getText().toString().trim());
        String desc = descField.getText().toString().trim();

        // taruh di contentvalues
        ContentValues values = new ContentValues();
        values.put(DuitkuContract.TransactionEntry.COLUMN_DATE, date);
        values.put(DuitkuContract.TransactionEntry.COLUMN_WALLET_ID, wallet);
        values.put(DuitkuContract.TransactionEntry.COLUMN_CATEGORY_ID, category);
        values.put(DuitkuContract.TransactionEntry.COLUMN_AMOUNT, amount);
        values.put(DuitkuContract.TransactionEntry.COLUMN_DESC, desc);

        // panggil controller nya
        Uri result = new TransactionController().addTransaction(values);

        // hasil insert nya gimana
        if (result == null){
            Toast.makeText(getContext(), "Error inserting new transaction", Toast.LENGTH_SHORT);
        } else {
            Toast.makeText(getContext(), "Transaction Added", Toast.LENGTH_SHORT);
        }

    }

}
