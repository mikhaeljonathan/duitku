package com.example.duitku.transaction.add;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.duitku.R;
import com.example.duitku.budget.BudgetController;
import com.example.duitku.category.Category;
import com.example.duitku.category.CategoryController;
import com.example.duitku.category.PickCategoryDialog;
import com.example.duitku.database.DuitkuContract.CategoryEntry;
import com.example.duitku.date.DatePickerFragment;
import com.example.duitku.interfaces.UIView;
import com.example.duitku.main.Utility;
import com.example.duitku.transaction.Transaction;
import com.example.duitku.transaction.TransactionController;
import com.example.duitku.wallet.PickWalletDialog;
import com.example.duitku.wallet.Wallet;
import com.example.duitku.wallet.WalletController;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Date;

public class AddTransactionFragmentView implements UIView {

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

    private double amount;
    private String desc;
    private Date date;
    private long categoryId;
    private long walletId;
    private long walletDestId;

    private TransactionController transactionController;
    private CategoryController categoryController;
    private WalletController walletController;
    private BudgetController budgetController;

    private LayoutInflater inflater;
    private ViewGroup container;
    private View view;
    private Fragment fragment;

    private String type;

    public AddTransactionFragmentView(LayoutInflater inflater, ViewGroup container, Fragment fragment, String type){
        this.inflater = inflater;
        this.container = container;
        this.fragment = fragment;
        this.type = type;

        transactionController = new TransactionController(fragment.getActivity());
        categoryController = new CategoryController(fragment.getActivity());
        walletController = new WalletController(fragment.getActivity());
        budgetController = new BudgetController(fragment.getActivity());

        desc = "";
        categoryId = -1;
        walletId = -1;
        walletDestId = -1;
        Calendar calendar = Calendar.getInstance();
        date = calendar.getTime();
    }

    @Override
    public void setUpUI() {
        this.view = inflater.inflate(R.layout.fragment_add_transaction, container, false);
        setUpTextInput();
        setUpPicker();
        setUpButtons();
    }

    private void setUpTextInput(){
        amountLayout = view.findViewById(R.id.add_transaction_amount_textinputlayout);
        descLayout = view.findViewById(R.id.add_transaction_desc_textinputlayout);
        amountField = view.findViewById(R.id.add_transaction_amount_edittext);
        descField = view.findViewById(R.id.add_transaction_desc_edittext);

        // set up textChangedListener
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
    }

    private void setUpPicker(){
        setUpDatePicker();
        setUpCategoryPicker();
        setUpWalletPicker();
        setUpWalletDestPicker();
    }

    private void setUpDatePicker(){
        dateConstraintLayout = view.findViewById(R.id.add_transaction_date_constraintlayout);
        dateTextView = view.findViewById(R.id.add_transaction_date_textview);

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
                datePicker.show(fragment.getFragmentManager(), "Date Picker Dialog");
            }
        });
    }

    private void setUpCategoryPicker(){
        categoryConstraintLayout = view.findViewById(R.id.add_transaction_category_constraintlayout);
        categoryTextView = view.findViewById(R.id.add_transaction_category_textview);
        categoryErrorTextView = view.findViewById(R.id.add_transasction_category_error_textview);

        final PickCategoryDialog.PickCategoryListener pickCategoryListener = new PickCategoryDialog.PickCategoryListener() {
            @Override
            public void pickCategory(long id) {
                Category category = categoryController.getCategoryById(id);
                categoryTextView.setText(category.getName());
                categoryTextView.setTextColor(fragment.getResources().getColor(android.R.color.white));
                categoryId = id;
            }
        };
        categoryConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickCategoryDialog pickCategoryDialog = new PickCategoryDialog(pickCategoryListener, type);
                pickCategoryDialog.show(fragment.getFragmentManager(), "View Category Dialog");
            }
        });
        if (type.equals(CategoryEntry.TYPE_TRANSFER)) {
            categoryConstraintLayout.setVisibility(View.GONE);
        }
        categoryErrorTextView.setVisibility(View.GONE);
    }

    private void setUpWalletPicker(){
        walletConstraintLayout = view.findViewById(R.id.add_transaction_wallet_constraintlayout);
        walletTextView = view.findViewById(R.id.add_transaction_wallet_textview);
        walletErrorTextView = view.findViewById(R.id.add_transasction_wallet_error_textview);

        final PickWalletDialog.PickWalletListener pickWalletListener = new PickWalletDialog.PickWalletListener() {
            @Override
            public void pickWallet(long id) {
                Wallet wallet = walletController.getWalletById(id);
                walletTextView.setText(wallet.getName());
                walletTextView.setTextColor(fragment.getResources().getColor(android.R.color.white));
                walletId = id;
            }
        };
        walletConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickWalletDialog pickWalletDialog = new PickWalletDialog(pickWalletListener);
                pickWalletDialog.show(fragment.getFragmentManager(), "Pick Wallet Dialog");
            }
        });
        walletErrorTextView.setVisibility(View.GONE);
    }

    private void setUpWalletDestPicker(){
        walletDestConstraintLayout = view.findViewById(R.id.add_transaction_walletdest_constraintlayout);
        walletDestTextView = view.findViewById(R.id.add_transaction_walletdest_textview);
        walletDestErrorTextView = view.findViewById(R.id.add_transaction_walletdest_error_textview);

        final PickWalletDialog.PickWalletListener pickWalletListener = new PickWalletDialog.PickWalletListener() {
            @Override
            public void pickWallet(long id) {
                Wallet wallet = walletController.getWalletById(id);
                walletDestTextView.setText(wallet.getName());
                walletDestTextView.setTextColor(fragment.getResources().getColor(android.R.color.white));
                walletDestId = id;
            }
        };
        walletDestConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickWalletDialog pickWalletDialog = new PickWalletDialog(pickWalletListener);
                pickWalletDialog.show(fragment.getFragmentManager(), "Pick Wallet Dialog");
            }
        });
        if (!type.equals(CategoryEntry.TYPE_TRANSFER)) {
            walletDestConstraintLayout.setVisibility(View.GONE);
        }
        walletDestErrorTextView.setVisibility(View.GONE);
    }

    private void setUpButtons(){
        saveBtn = view.findViewById(R.id.add_transaction_save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateInput()) return;
                Uri uri = addTransaction();
                if (uri == null){
                    Toast.makeText(fragment.getActivity(), "Error adding new transaction", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(fragment.getActivity(), "Transaction added", Toast.LENGTH_SHORT).show();
                }
                fragment.getActivity().finish();
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
        String desc = descField.getText().toString().trim();
        if (desc.length() > 50){
            return false;
        }

        // category
        if (!type.equals(CategoryEntry.TYPE_TRANSFER) && categoryId == -1){
            categoryErrorTextView.setVisibility(View.VISIBLE);
            return false;
        } else {
            categoryErrorTextView.setVisibility(View.GONE);
        }

        // wallet
        if (walletId == -1){
            walletErrorTextView.setVisibility(View.VISIBLE);
            return false;
        } else {
            walletErrorTextView.setVisibility(View.GONE);
        }

        // wallet dest
        if (type.equals(CategoryEntry.TYPE_TRANSFER) && walletDestId == -1){
            walletDestErrorTextView.setText("Wallet destination has to be chosen");
            walletDestErrorTextView.setVisibility(View.VISIBLE);
            return false;
        } else {
            walletDestErrorTextView.setVisibility(View.GONE);
        }

        // wallet and walletdest
        if (type.equals(CategoryEntry.TYPE_TRANSFER) && walletId == walletDestId){
            walletDestErrorTextView.setText("Wallet source can't be same\nwith wallet destination");
            walletDestErrorTextView.setVisibility(View.VISIBLE);
            return false;
        } else {
            walletDestErrorTextView.setVisibility(View.GONE);
        }

        return true;
    }

    private Uri addTransaction(){
        Transaction transaction = new Transaction(-1, walletId, walletDestId, categoryId, desc, date, amount);
        Uri uri = transactionController.addTransaction(transaction);

        walletController.updateWalletFromTransaction(transaction);

        Category category = categoryController.getCategoryById(transaction.getCategoryId());
        if (category != null && category.getType().equals(CategoryEntry.TYPE_EXPENSE)){ //budget pasti expense
            budgetController.updateBudgetFromTransaction(transaction);
        }

        return uri;
    }

    @Override
    public View getView() {
        return view;
    }
}
