package com.example.duitku.transaction.form;

import android.content.Context;
import android.view.View;

import com.example.duitku.transaction.form.components.TransactionAmountComponent;
import com.example.duitku.transaction.form.components.TransactionCategoryComponent;
import com.example.duitku.transaction.form.components.TransactionDateComponent;
import com.example.duitku.transaction.form.components.TransactionDescriptionComponent;
import com.example.duitku.transaction.form.components.TransactionWalletComponent;

import java.util.Date;

public class TransactionForm extends View {

    private TransactionAmountComponent amountComponent;
    private TransactionDescriptionComponent descriptionComponent;
    private TransactionDateComponent dateComponent;
    private TransactionCategoryComponent categoryComponent;
    private TransactionWalletComponent walletComponent;

    private final View rootView;
    private final Object activity;
    private final String type;

    public TransactionForm(Context context, View rootView, Object activity, String type) {
        super(context);
        this.rootView = rootView;
        this.activity = activity;
        this.type = type;
    }

    public void setUpUI() {
        setUpFields();
    }

    private void setUpFields() {
        amountComponent = new TransactionAmountComponent(getContext(), rootView, activity);
        descriptionComponent = new TransactionDescriptionComponent(getContext(), rootView, activity);
        dateComponent = new TransactionDateComponent(getContext(), rootView, activity);
        categoryComponent = new TransactionCategoryComponent(getContext(), rootView, activity, type);
        walletComponent = new TransactionWalletComponent(getContext(), rootView, activity, type);
    }

    public boolean validateInput() {
        if (!amountComponent.validateInput()) return false;
        if (!descriptionComponent.validateInput()) return false;
        if (!categoryComponent.validateInput()) return false;
        return walletComponent.validateInput();
    }

    public double getAmount() {
        return amountComponent.getAmount();
    }

    public String getDesc() {
        return descriptionComponent.getDesc();
    }

    public Date getDate() {
        return dateComponent.getDate();
    }

    public long getCategoryId() {
        return categoryComponent.getCategoryId();
    }

    public long getWalletId() {
        return walletComponent.getWalletId();
    }

    public long getWalletDestId() {
        return walletComponent.getWalletDestId();
    }

    public void setAmount(double amount) {
        amountComponent.setAmount(amount);
    }

    public void setDesc(String desc) {
        descriptionComponent.setDesc(desc);
    }

    public void setDate(Date date){
        dateComponent.setDate(date);
    }

    public void setCategoryId(long categoryId) {
        categoryComponent.setCategoryId(categoryId);
    }

    public void setWalletId(long walletId) {
        walletComponent.setWalletId(walletId);
    }

    public void setWalletDestId(long walletDestId) {
        walletComponent.setWalletDestId(walletDestId);
    }

}
