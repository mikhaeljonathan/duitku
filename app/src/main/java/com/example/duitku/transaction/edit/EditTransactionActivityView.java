package com.example.duitku.transaction.edit;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.example.duitku.R;
import com.example.duitku.category.Category;
import com.example.duitku.category.CategoryController;
import com.example.duitku.category.PickCategoryDialog;
import com.example.duitku.date.DatePickerFragment;
import com.example.duitku.interfaces.UIView;
import com.example.duitku.main.Utility;
import com.example.duitku.transaction.Transaction;
import com.example.duitku.transaction.TransactionController;
import com.example.duitku.wallet.Wallet;
import com.example.duitku.wallet.WalletController;
import com.example.duitku.wallet.pick.PickWalletDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;

public class EditTransactionActivityView implements UIView {

    private TextInputLayout amountLayout;
    private TextInputLayout descLayout;
    private TextInputEditText amountField;
    private TextInputEditText descField;
    private ConstraintLayout dateConstraintLayout;
    private TextView dateTextView;
    private ConstraintLayout categoryConstraintLayout;
    private TextView categoryTextView;
    private TextView categoryErrorTextView;
    private ConstraintLayout walletConstraintLayout;
    private TextView walletTextView;
    private TextView walletErrorTextView;
    private ConstraintLayout walletDestConstraintLayout;
    private TextView walletDestTextView;
    private TextView walletDestErrorTextView;
    private Button saveBtn;
    private Button deleteBtn;

    private TransactionController transactionController;

    private long id;
    private double amount;
    private String desc;
    private Date date;
    private long categoryId;
    private long walletId;
    private long walletDestId;

    private Transaction transaction;
    private AppCompatActivity activity;

    public EditTransactionActivityView(long id, AppCompatActivity activity){
        this.id = id;
        this.activity = activity;

        transactionController = new TransactionController(activity);

        transaction = transactionController.getTransactionById(id);
        amount = transaction.getAmount();
        desc = transaction.getDesc();
        date = transaction.getDate();
        categoryId = transaction.getCategoryId();
        walletId = transaction.getWalletId();
        walletDestId = transaction.getWalletDestId();
    }

    @Override
    public void setUpUI() {
        activity.setContentView(R.layout.activity_edit_transaction);

        setUpField();
        setUpButtons();
    }

    private void setUpField(){
        setUpAmount();
        setUpDescription();
        setUpDatePicker();
        setUpCategoryPicker();
        setUpWalletPicker();
        setUpWalletDestPicker();
    }

    private void setUpAmount(){
        amountLayout = activity.findViewById(R.id.transaction_amount_textinputlayout);
        amountField = activity.findViewById(R.id.transaction_amount_edittext);
        amountField.setText(Double.toString(amount));
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
    }

    private void setUpDescription(){
        descLayout = activity.findViewById(R.id.transaction_desc_textinputlayout);
        descField = activity.findViewById(R.id.transaction_desc_edittext);
        descField.setText(desc);
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
    }

    private void setUpDatePicker(){
        dateConstraintLayout = activity.findViewById(R.id.transaction_date_constraintlayout);
        dateTextView = activity.findViewById(R.id.transaction_date_textview);
        dateTextView.setText(Utility.convertDateToFullString(date));

        final DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                date = Utility.convertElementsToDate(year, month, dayOfMonth);
                dateTextView.setText(Utility.convertDateToFullString(date));
            }
        };
        dateConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment(date, listener);
                datePicker.show(activity.getSupportFragmentManager(), "Date Picker Dialog");
            }
        });
    }

    private void setUpCategoryPicker(){
        categoryConstraintLayout = activity.findViewById(R.id.transaction_category_constraintlayout);
        categoryTextView = activity.findViewById(R.id.transaction_category_textview);
        categoryErrorTextView = activity.findViewById(R.id.transasction_category_error_textview);
        categoryErrorTextView.setVisibility(View.GONE);

        // if transfer transaction
        if (categoryId == -1){
            categoryConstraintLayout.setVisibility(View.GONE);
            return;
        }

        final CategoryController categoryController = new CategoryController(activity);

        // set current category
        Category category = categoryController.getCategoryById(categoryId);
        categoryTextView.setText(category.getName());
        categoryTextView.setTextColor(activity.getResources().getColor(android.R.color.white));

        // set listener
        final PickCategoryDialog.PickCategoryListener pickCategoryListener = new PickCategoryDialog.PickCategoryListener() {
            @Override
            public void pickCategory(long id) {
                Category category = categoryController.getCategoryById(id);
                categoryTextView.setText(category.getName());
                categoryTextView.setTextColor(activity.getResources().getColor(android.R.color.white));
                categoryId = id;
            }
        };
        categoryConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickCategoryDialog pickCategoryDialog = new PickCategoryDialog(pickCategoryListener, categoryController.getCategoryById(categoryId).getType());
                pickCategoryDialog.show(activity.getSupportFragmentManager(), "View Category Dialog");
            }
        });

    }

    private void setUpWalletPicker(){
        walletConstraintLayout = activity.findViewById(R.id.transaction_wallet_constraintlayout);
        walletTextView = activity.findViewById(R.id.transaction_wallet_textview);
        walletErrorTextView = activity.findViewById(R.id.transasction_wallet_error_textview);
        walletErrorTextView.setVisibility(View.GONE);

        final WalletController walletController = new WalletController(activity);

        // set current wallet
        Wallet wallet = walletController.getWalletById(walletId);
        walletTextView.setText(wallet.getName());
        walletTextView.setTextColor(activity.getResources().getColor(android.R.color.white));

        // set listener
        final PickWalletDialog.PickWalletListener pickWalletListener = new PickWalletDialog.PickWalletListener() {
            @Override
            public void pickWallet(long id) {
                Wallet wallet = walletController.getWalletById(id);
                walletTextView.setText(wallet.getName());
                walletTextView.setTextColor(activity.getResources().getColor(android.R.color.white));
                walletId = id;
            }
        };
        walletConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickWalletDialog pickWalletDialog = new PickWalletDialog(pickWalletListener);
                pickWalletDialog.show(activity.getSupportFragmentManager(), "Pick Wallet Dialog");
            }
        });
    }

    private void setUpWalletDestPicker(){
        walletDestConstraintLayout = activity.findViewById(R.id.transaction_walletdest_constraintlayout);
        walletDestTextView = activity.findViewById(R.id.transaction_walletdest_textview);
        walletDestErrorTextView = activity.findViewById(R.id.transaction_walletdest_error_textview);
        walletDestErrorTextView.setVisibility(View.GONE);

        final WalletController walletController = new WalletController(activity);

        // if expense or income transaction
        if (walletDestId == -1){
            walletDestConstraintLayout.setVisibility(View.GONE);
            return;
        }

        // set current wallet dest
        Wallet wallet = walletController.getWalletById(walletDestId);
        walletDestTextView.setText(wallet.getName());
        walletDestTextView.setTextColor(activity.getResources().getColor(android.R.color.white));

        final PickWalletDialog.PickWalletListener pickWalletListener = new PickWalletDialog.PickWalletListener() {
            @Override
            public void pickWallet(long id) {
                Wallet wallet = walletController.getWalletById(id);
                walletDestTextView.setText(wallet.getName());
                walletDestTextView.setTextColor(activity.getResources().getColor(android.R.color.white));
                walletDestId = id;
            }
        };
        walletDestConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickWalletDialog pickWalletDialog = new PickWalletDialog(pickWalletListener);
                pickWalletDialog.show(activity.getSupportFragmentManager(), "Pick Wallet Dialog");
            }
        });
    }

    private void setUpButtons(){
        setUpSaveButton();
        setUpDeleteButton();
    }

    private void setUpSaveButton(){
        saveBtn = activity.findViewById(R.id.transaction_save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateInput()) return;
                int rowsUpdated = updateTransaction();
                if (rowsUpdated == 0){
                    Toast.makeText(activity, "Error updating the transaction", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "Transaction updated", Toast.LENGTH_SHORT).show();
                }
                activity.finish();
            }
        });
    }

    private boolean validateInput(){
        // amount
        String amountString = amountField.getText().toString().trim();
        if (amountString.equals("")){
            amountLayout.setError("Amount can't be empty");
            return false;
        } else {
            amount = Double.parseDouble(amountString);
        }
        if (amount <= 0){
            amountLayout.setError("Amount not allowed");
            return false;
        }
        if (amount > 999999999){
            return false;
        }

        // description
        desc = descField.getText().toString().trim();
        if (desc.length() > 50){
            return false;
        }

        // wallet and walletdest
        if (walletId == walletDestId){
            walletDestErrorTextView.setText("Wallet source can't be same\nwith wallet destination");
            walletDestErrorTextView.setVisibility(View.VISIBLE);
            return false;
        } else {
            walletDestErrorTextView.setVisibility(View.GONE);
        }

        return true;
    }

    private int updateTransaction(){
        transaction.setAmount(amount);
        transaction.setDesc(desc);
        transaction.setDate(date);
        transaction.setCategoryId(categoryId);
        transaction.setWalletId(walletId);
        transaction.setWalletDestId(walletDestId);

        int rowsUpdated = transactionController.updateTransaction(transaction);
        return rowsUpdated;
    }

    private void setUpDeleteButton(){
        deleteBtn = activity.findViewById(R.id.edit_transaction_delete_btn);
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
        alertDialogBuilder.setMessage("Are you sure to delete this transaction?\nYou can't undo this")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteTransaction();
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

    private void deleteTransaction(){
        int rowsDeleted = transactionController.deleteTransaction(id);
        if (rowsDeleted == 0){
            Toast.makeText(activity, "Error deleting transaction", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "Transaction is deleted", Toast.LENGTH_SHORT).show();
        }
        activity.finish();
    }

    @Override
    public View getView() {
        return null;
    }
}
