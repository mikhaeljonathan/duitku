package com.example.duitku.view;

import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.duitku.R;
import com.example.duitku.controller.TransactionController;
import com.example.duitku.database.DuitkuContract.WalletEntry;
import com.example.duitku.dialog.DatePickerFragment;
import com.example.duitku.dialog.PickWalletDialog;
import com.example.duitku.model.Transaction;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddTransactionTransferFragment extends Fragment implements PickWalletDialog.PickWalletListener, PickWalletDialog.PickWalletDestListener {

    private ConstraintLayout dateConstraintLayout;
    private ConstraintLayout walletConstraintLayout;
    private ConstraintLayout walletDestConstraintLayout;
    private TextView dateTextView;
    private TextView walletTextView;
    private TextView walletDestTextView;

    private TextInputLayout amountLayout;
    private TextInputLayout descLayout;
    private TextInputEditText amountField;
    private TextInputEditText descField;

    private TextView walletErrorTextView;
    private TextView walletDestErrorTextView;

    private Button saveBtn;

    private Date mDate;
    private long walletId;
    private long walletDestId;

    private DatePickerDialog.OnDateSetListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_transaction_transfer, container, false);

        // Initialize the views
        dateConstraintLayout = rootView.findViewById(R.id.add_transaction_transfer_date_constraintlayout);
        walletConstraintLayout = rootView.findViewById(R.id.add_transaction_transfer_wallet_constraintlayout);
        walletDestConstraintLayout = rootView.findViewById(R.id.add_transaction_transfer_walletdest_constraintlayout);
        dateTextView = rootView.findViewById(R.id.add_transaction_transfer_date_textview);
        walletTextView = rootView.findViewById(R.id.add_transaction_transfer_wallet_textview);
        walletDestTextView = rootView.findViewById(R.id.add_transaction_transfer_walletdest_textview);
        amountLayout = rootView.findViewById(R.id.add_transaction_transfer_amount_layout);
        descLayout = rootView.findViewById(R.id.add_transaction_transfer_desc_layout);
        amountField = rootView.findViewById(R.id.add_transaction_transfer_amount_edittext);
        descField = rootView.findViewById(R.id.add_transaction_transfer_desc_edittext);
        walletErrorTextView = rootView.findViewById(R.id.add_transasction_transfer_wallet_error_textview);
        walletDestErrorTextView = rootView.findViewById(R.id.add_transasction_transfer_walletdest_error_textview);
        saveBtn = rootView.findViewById(R.id.add_transaction_transfer_save_btn);

        amountField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 9){
                    amountLayout.setError("Amount too much");
                } else {
                    amountLayout.setErrorEnabled(false);
                }
            }
        });
        descField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 50){
                    descLayout.setError("Description max 50 characters");
                } else {
                    descLayout.setErrorEnabled(false);
                }
            }
        });

        walletId = -1;
        walletDestId = -1;
        Calendar c = Calendar.getInstance();
        mDate = c.getTime();

        listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                // ambil value date dari datepicker
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mDate = calendar.getTime();

                // ubah jadi string
                String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(mDate);

                calendar = Calendar.getInstance();
                if (calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.MONTH) == month && calendar.get(Calendar.DAY_OF_MONTH) == dayOfMonth){
                    currentDateString = "Today";
                }

                // update di startdate textview
                dateTextView.setText(currentDateString);
            }
        };

        dateConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment(mDate, listener);
                datePicker.show(getFragmentManager(), "Date Picker Dialog");
            }
        });
        walletConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickWalletDialog pickWalletDialog = new PickWalletDialog(AddTransactionTransferFragment.this, false);
                pickWalletDialog.show(getFragmentManager(), "Pick Wallet Dialog");
            }
        });
        walletDestConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickWalletDialog pickWalletDialog = new PickWalletDialog(AddTransactionTransferFragment.this, true);
                pickWalletDialog.show(getFragmentManager(), "Pick Wallet Destination Dialog");
            }
        });

        // setting save button nya
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addTransaction() != null){
                    getActivity().finish(); // activity add transaction ny udh selesai
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }
            }
        });
        
        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void pickWallet(long id) {
        // tampilin nama wallet nya
        walletId = id;
        Uri currentWalletUri = ContentUris.withAppendedId(WalletEntry.CONTENT_URI, walletId);
        String[] projection = new String[]{ WalletEntry.COLUMN_ID, WalletEntry.COLUMN_NAME};
        Cursor cursor = getContext().getContentResolver().query(currentWalletUri, projection, null, null);
        if (cursor.moveToFirst()){
            String walletName = cursor.getString(cursor.getColumnIndex(WalletEntry.COLUMN_NAME));
            walletTextView.setText(walletName);
            walletTextView.setTextColor(getResources().getColor(android.R.color.white));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void pickWalletDest(long id) {
        // tampilin nama wallet nya
        walletDestId = id;
        Uri currentWalletUri = ContentUris.withAppendedId(WalletEntry.CONTENT_URI, walletDestId);
        String[] projection = new String[]{ WalletEntry.COLUMN_ID, WalletEntry.COLUMN_NAME};
        Cursor cursor = getContext().getContentResolver().query(currentWalletUri, projection, null, null);
        if (cursor.moveToFirst()){
            String walletName = cursor.getString(cursor.getColumnIndex(WalletEntry.COLUMN_NAME));
            walletDestTextView.setText(walletName);
            walletDestTextView.setTextColor(getResources().getColor(android.R.color.white));
        }
    }

    private Uri addTransaction(){

        // ambil data dari view
        String amountString = amountField.getText().toString().trim();
        if (amountString.equals("")){
            amountLayout.setError("Amount can't be empty");
            return null;
        }

        double amount = Double.parseDouble(amountString);
        String desc = descField.getText().toString().trim();

        if (amount <= 0){
            amountLayout.setError("Amount not allowed");
            return null;
        }

        if (amount > 999999999){
            return null;
        }

        if (desc.length() > 50){
            return null;
        }

        if (walletId == -1){
            walletErrorTextView.setVisibility(View.VISIBLE);
            return null;
        } else {
            walletErrorTextView.setVisibility(View.GONE);
        }

        if (walletDestId == -1){
            walletDestErrorTextView.setVisibility(View.VISIBLE);
            return null;
        } else {
            walletDestErrorTextView.setVisibility(View.GONE);
        }

        // panggil controller nya
        Transaction transactionAdded = new Transaction(-1, mDate, walletId, walletDestId, -1, amount, desc);
        Uri uri = new TransactionController(getContext()).addTransferTransaction(transactionAdded);

        // hasil insert nya gimana
        if (uri != null){
            Toast.makeText(getContext(), "Transaction Added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Error inserting new transaction", Toast.LENGTH_SHORT).show();
        }
        return uri;

    }

}
