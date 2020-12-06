package com.example.duitku.view;

import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.Intent;
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
import com.example.duitku.dialog.DatePickerFragment;
import com.example.duitku.dialog.PickWalletDialog;
import com.example.duitku.dialog.ViewCategoriesDialog;
import com.example.duitku.database.DuitkuContract.CategoryEntry;
import com.example.duitku.database.DuitkuContract.WalletEntry;
import com.example.duitku.model.Transaction;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddTransactionIncomeFragment extends Fragment implements ViewCategoriesDialog.ViewCategoriesListener, PickWalletDialog.PickWalletListener {

    private ConstraintLayout dateConstraintLayout;
    private ConstraintLayout categoryConstraintLayout;
    private ConstraintLayout walletConstraintLayout;
    private TextView dateTextView;
    private TextView categoryTextView;
    private TextView walletTextView;
    private EditText amountField;
    private EditText descField;
    private Button saveBtn;

    private Date mDate;
    private long categoryId;
    private long walletId;

    private DatePickerDialog.OnDateSetListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_transaction_income, container, false);

        // Initialize the views
        dateConstraintLayout = rootView.findViewById(R.id.add_transaction_income_date_constraintlayout);
        categoryConstraintLayout = rootView.findViewById(R.id.add_transaction_income_category_constraintlayout);
        walletConstraintLayout = rootView.findViewById(R.id.add_transaction_income_wallet_constraintlayout);
        dateTextView = rootView.findViewById(R.id.add_transaction_income_date_textview);
        categoryTextView = rootView.findViewById(R.id.add_transaction_income_category_textview);
        walletTextView = rootView.findViewById(R.id.add_transaction_income_wallet_textview);
        amountField = rootView.findViewById(R.id.add_transaction_income_amount_edittext);
        descField = rootView.findViewById(R.id.add_transaction_income_desc_edittext);
        saveBtn = rootView.findViewById(R.id.add_transaction_income_save_btn);

        categoryId = -1;
        walletId = -1;
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
        categoryConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewCategoriesDialog viewCategoriesDialog = new ViewCategoriesDialog(AddTransactionIncomeFragment.this, CategoryEntry.TYPE_INCOME);
                viewCategoriesDialog.show(getFragmentManager(), "View Category Dialog");
            }
        });
        walletConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickWalletDialog pickWalletDialog = new PickWalletDialog(AddTransactionIncomeFragment.this, false);
                pickWalletDialog.show(getFragmentManager(), "Pick Wallet Dialog");
            }
        });

        // setting save button nya
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTransaction();
                getActivity().finish(); // activity add transaction ny udh selesai
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void pickCategory(long id) {
        // tampilin nama category nya
        categoryId = id;
        Uri currentCategoryUri = ContentUris.withAppendedId(CategoryEntry.CONTENT_URI, categoryId);
        String[] projection = new String[]{ CategoryEntry.COLUMN_ID, CategoryEntry.COLUMN_NAME};
        Cursor cursor = getContext().getContentResolver().query(currentCategoryUri, projection, null, null);
        if (cursor.moveToFirst()){
            String categoryName = cursor.getString(cursor.getColumnIndex(CategoryEntry.COLUMN_NAME));
            categoryTextView.setText(categoryName);
        }
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

    private void addTransaction(){

        // ambil data dari view
        double amount = Double.parseDouble(amountField.getText().toString().trim());
        String desc = descField.getText().toString().trim();

        // panggil controller nya
        Transaction transactionAdded = new Transaction(mDate, walletId, -1, categoryId, amount, desc);
        Uri uri = new TransactionController(getContext()).addTransaction(transactionAdded);

        // hasil insert nya gimana
        if (uri == null){
            Toast.makeText(getContext(), "Error inserting new transaction", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Transaction Added", Toast.LENGTH_SHORT).show();
        }

    }

}
