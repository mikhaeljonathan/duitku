package com.example.duitku.wallet.form;

import android.content.Context;
import android.view.View;

import com.example.duitku.wallet.form.components.WalletAmountComponent;
import com.example.duitku.wallet.form.components.WalletDescComponent;
import com.example.duitku.wallet.form.components.WalletNameComponent;

public class WalletForm extends View {

    private WalletNameComponent walletNameComponent;
    private WalletAmountComponent walletAmountComponent;
    private WalletDescComponent walletDescComponent;

    private final View rootView;
    private final Object activity;

    public WalletForm(Context context, View rootView, Object activity) {
        super(context);
        this.rootView = rootView;
        this.activity = activity;
    }

    public void setUpUI() {
        setUpFields();
    }

    private void setUpFields() {
        walletNameComponent = new WalletNameComponent(getContext(), rootView, activity);
        walletAmountComponent = new WalletAmountComponent(getContext(), rootView, activity);
        walletDescComponent = new WalletDescComponent(getContext(), rootView, activity);
    }

    public boolean validateInput() {
        if (!walletNameComponent.validateInput()) return false;
        if (!walletAmountComponent.validateInput()) return false;
        return walletDescComponent.validateInput();
    }

    public String getName() {
        return walletNameComponent.getName();
    }

    public double getAmount() {
        return walletAmountComponent.getAmount();
    }

    public String getDesc() {
        return walletDescComponent.getDesc();
    }

    public void setName(String name) {
        walletNameComponent.setName(name);
    }

    public void setAmount(double amount) {
        walletAmountComponent.setAmount(amount);
    }

    public void setDesc(String desc) {
        walletDescComponent.setDesc(desc);
    }

}
