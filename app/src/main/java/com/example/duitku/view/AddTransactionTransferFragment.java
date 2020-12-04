package com.example.duitku.view;

import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.example.duitku.dialog.ViewCategoriesDialog;
import com.example.duitku.model.Transaction;

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
    private EditText amountField;
    private EditText descField;
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
        amountField = rootView.findViewById(R.id.add_transaction_transfer_amount_edittext);
        descField = rootView.findViewById(R.id.add_transaction_transfer_desc_edittext);
        saveBtn = rootView.findViewById(R.id.add_transaction_transfer_save_btn);

        walletId = -1;
        walletDestId = -1;
        mDate = null;

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
                addTransaction();
                getActivity().finish(); // activity add transaction ny udh selesai
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
        }
    }

    private void addTransaction(){

        // ambil data dari view
        String date = DateFormat.getDateInstance(DateFormat.SHORT).format(mDate);
        double amount = Double.parseDouble(amountField.getText().toString().trim());
        String desc = descField.getText().toString().trim();

        // panggil controller nya
        Transaction transactionAdded = new Transaction(date, walletId, walletDestId, -1, amount, desc);
        Uri uri = new TransactionController(getContext()).addTransaction(transactionAdded);

        // hasil insert nya gimana
        if (uri == null){
            Toast.makeText(getContext(), "Error inserting new transaction", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Transaction Added", Toast.LENGTH_SHORT).show();
        }

    }

}
