package com.example.duitku.transaction.add;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.duitku.R;
import com.example.duitku.category.CategoryController;
import com.example.duitku.database.DuitkuContract.CategoryEntry;
import com.example.duitku.interfaces.UIView;
import com.example.duitku.transaction.Transaction;
import com.example.duitku.transaction.TransactionController;
import com.example.duitku.transaction.form.TransactionForm;
import com.example.duitku.wallet.pick.PickWalletDialog;
import com.example.duitku.wallet.Wallet;
import com.example.duitku.wallet.WalletController;

import java.util.Calendar;
import java.util.Date;

public class AddTransactionFragmentView implements UIView {

    private TransactionForm transactionForm;

    private final LayoutInflater inflater;
    private final ViewGroup container;
    private View view;
    private final Fragment fragment;

    private String type;

    public AddTransactionFragmentView(LayoutInflater inflater, ViewGroup container, Fragment fragment, String type){
        this.inflater = inflater;
        this.container = container;
        this.fragment = fragment;
        this.type = type;
    }

    @Override
    public void setUpUI() {
        setUpViews();
        setUpForm();
        setUpButtons();
    }

    private void setUpViews(){
        this.view = inflater.inflate(R.layout.fragment_add_transaction, container, false);
    }

    private void setUpForm(){
        transactionForm = new TransactionForm(fragment.getActivity(), view, fragment, type);
        transactionForm.setUpUI();
    }

    private void setUpButtons(){
        setUpAddBtn();
    }

    private void setUpAddBtn(){
        Button addBtn = view.findViewById(R.id.transaction_save_btn);
        addBtn.setText("Add");
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!transactionForm.validateInput()) return;
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

    private Uri addTransaction(){
        long walletId = transactionForm.getWalletId();
        long walletDestId = transactionForm.getWalletDestId();
        long categoryId = transactionForm.getCategoryId();
        String desc = transactionForm.getDesc();
        Date date = transactionForm.getDate();
        double amount = transactionForm.getAmount();

        Transaction transaction = new Transaction(-1, walletId, walletDestId, categoryId, desc, date, amount);
        Uri uri = new TransactionController(fragment.getActivity()).addTransaction(transaction);

        new WalletController(fragment.getActivity()).updateWalletFromInitialTransaction(transaction);

        return uri;
    }

    @Override
    public View getView() {
        return view;
    }
}
