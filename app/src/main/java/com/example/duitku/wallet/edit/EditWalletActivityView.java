package com.example.duitku.wallet.edit;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.duitku.R;
import com.example.duitku.category.CategoryController;
import com.example.duitku.transaction.TransactionController;
import com.example.duitku.database.DuitkuContract;
import com.example.duitku.category.Category;
import com.example.duitku.transaction.Transaction;
import com.example.duitku.interfaces.UIView;
import com.example.duitku.wallet.Wallet;
import com.example.duitku.wallet.WalletController;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Date;

public class EditWalletActivityView implements UIView {

    private TextInputLayout nameLayout;
    private TextInputLayout amountLayout;
    private TextInputLayout descLayout;
    private TextInputEditText nameField;
    private TextInputEditText amountField;
    private TextInputEditText descField;

    private String name;
    private double amount;
    private String desc;

    private WalletController walletController;
    private CategoryController categoryController;
    private TransactionController transactionController;

    private long id;
    private Wallet wallet;
    private AppCompatActivity activity;

    public EditWalletActivityView(long id, AppCompatActivity activity){
        this.id = id;
        this.activity = activity;
        walletController = new WalletController(activity);
        categoryController = new CategoryController(activity);
        transactionController = new TransactionController(activity);
    }

    @Override
    public void setUpUI() {
        activity.setContentView(R.layout.activity_edit_wallet);

        wallet = walletController.getWalletById(id);
        setUpField();
        setUpButton();
    }

    private void setUpField(){
        // initialize the views
        nameLayout = activity.findViewById(R.id.wallet_name_textinputlayout);
        amountLayout = activity.findViewById(R.id.wallet_amount_textinputlayout);
        descLayout = activity.findViewById(R.id.wallet_desc_textinputlayout);
        nameField = activity.findViewById(R.id.wallet_name_field);
        amountField = activity.findViewById(R.id.wallet_amount_field);
        descField = activity.findViewById(R.id.wallet_desc_field);

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

        nameField.setText(wallet.getName());
        amountField.setText(wallet.getAmount() + "");
        descField.setText(wallet.getDescription());
    }

    private void setUpButton(){
        ImageButton backBtn = activity.findViewById(R.id.edit_wallet_back_btn);
        Button saveBtn = activity.findViewById(R.id.wallet_save_btn);
        Button deleteBtn = activity.findViewById(R.id.edit_wallet_delete_btn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateInput()) return;
                int rowsUpdated = updateWallet();
                if (rowsUpdated == 0){
                    Toast.makeText(activity, "Error editing wallet", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "Wallet edited", Toast.LENGTH_SHORT).show();
                }
                activity.finish();
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteConfirmationDialog();
            }
        });
    }

    private void showDeleteConfirmationDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity, R.style.AlertDialogCustom);
        alertDialogBuilder.setTitle("Delete Confirmation");
        alertDialogBuilder.setMessage("Are you sure to delete this wallet?\nAll transactions with this wallet are deleted too.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteWallet();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary); //biar bg gelap
        alertDialog.show();
    }

    private void deleteWallet(){
        int rowsDeleted = walletController.deleteWallet(id);
        if (rowsDeleted == 0){
            Toast.makeText(activity, "Error deleting wallet", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "Wallet is deleted", Toast.LENGTH_SHORT).show();
        }
        activity.finish();
    }

    private boolean validateInput(){
        // Wallet name
        name = nameField.getText().toString().trim();
        if (name.equals("")){
            nameLayout.setError("Wallet name can't be empty");
            return false;
        }
        if (!wallet.getName().equalsIgnoreCase(name)){
            Wallet wallet = walletController.getWalletByName(name);
            if (wallet != null){
                nameLayout.setError("There is a wallet with this name");
                return false;
            }
        }

        // Wallet amount
        String amountString = amountField.getText().toString().trim();
        if (amountString.equals("")){
            amount = 0;
        } else {
            amount = Double.parseDouble(amountString);
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

    private int updateWallet(){
        double amountBefore = wallet.getAmount();

        wallet.setName(name);
        wallet.setAmount(amount);
        wallet.setDescription(desc);

        if (amountBefore != wallet.getAmount()) {
            transactionController.addTransactionFromUpdatedWallet(amountBefore, wallet);
        }

        int rowsUpdated = walletController.updateWallet(wallet);
        return rowsUpdated;
    }

    @Override
    public View getView() {
        return null;
    }

}
