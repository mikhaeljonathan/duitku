package com.example.duitku.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.duitku.R;
import com.example.duitku.controller.CategoryController;
import com.example.duitku.controller.TransactionController;
import com.example.duitku.controller.WalletController;
import com.example.duitku.database.DuitkuContract.CategoryEntry;
import com.example.duitku.model.Category;
import com.example.duitku.model.Transaction;
import com.example.duitku.model.Wallet;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Date;

public class AddWalletDialog extends AppCompatDialogFragment {

    private TextInputLayout nameLayout;
    private TextInputLayout amountLayout;
    private TextInputLayout descLayout;
    private TextInputEditText nameField;
    private TextInputEditText amountField;
    private TextInputEditText descField;
    private Button addButton;

    private String name;
    private double amount;
    private String desc;

    private WalletController walletController;
    private CategoryController categoryController;
    private TransactionController transactionController;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        walletController = new WalletController(getActivity());
        categoryController = new CategoryController(getActivity());
        transactionController = new TransactionController(getActivity());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_wallet, null);

        setUpUI(view);

        builder.setView(view);

        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary); //biar bg gelap
        return dialog;
    }

    private void setUpUI(View view){
        nameLayout = view.findViewById(R.id.add_wallet_name_textinputlayout);
        amountLayout = view.findViewById(R.id.add_wallet_amount_textinputlayout);
        descLayout = view.findViewById(R.id.add_wallet_desc_textinputlayout);
        nameField = view.findViewById(R.id.add_wallet_name_edittext);
        amountField = view.findViewById(R.id.add_wallet_amount_edittext);
        descField = view.findViewById(R.id.add_wallet_desc_edittext);
        addButton = view.findViewById(R.id.add_wallet_add_btn);

        // set textChangedListener
        nameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 20){
                    nameLayout.setError("Wallet name max 20 characters");
                } else {
                    nameLayout.setErrorEnabled(false);
                }
            }
        });
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

        // set button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateInput()) return;
                Uri uri = addWallet();
                if (amount > 0) addTransaction(uri);

                if (uri == null){
                    Toast.makeText(getContext(), "Error adding new wallet", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Wallet added", Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        });
    }

    private Uri addWallet(){
        Wallet wallet = new Wallet(-1, name, amount, desc);
        Uri uri = walletController.addWallet(wallet);
        return uri;
    }

    private void addTransaction(Uri uri){
        Calendar calendar = Calendar.getInstance();
        Category category = categoryController.getCategoryByNameAndType(CategoryEntry.DEFAULT_CATEGORY_NAME, CategoryEntry.TYPE_INCOME);

        Date date = calendar.getTime();
        long walletId = ContentUris.parseId(uri);
        long walletDestId = -1;
        long categoryId = category.getId();
        String desc = "Initial Balance for Wallet " + name;
        Transaction transaction = new Transaction(-1, walletId, walletDestId, categoryId, desc, date, amount);
        transactionController.addTransaction(transaction);
    }

    private boolean validateInput(){
        // Wallet name
        name = nameField.getText().toString().trim();
        if (name.equals("")){
            nameLayout.setError("Wallet name can't be empty");
            return false;
        }

        // Wallet amount
        String amountString = amountField.getText().toString().trim();
        if (amountString.equals("")){
            amount = 0;
        } else {
            amount = Double.parseDouble(amountString);
        }
        if (amount < 0){
            amountLayout.setError("Amount not allowed");
            return false;
        }
        if (amount > 999999999){
            return false;
        }

        // Wallet description
        desc = descField.getText().toString().trim();
        if (desc.length() > 50){
            return false;
        }

        return true;
    }
}