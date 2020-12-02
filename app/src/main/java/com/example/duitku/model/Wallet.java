package com.example.duitku.model;

public class Wallet {

    private String mWalletName;
    private double mAmount;
    private String mDescription;

    public Wallet(String walletName, double amount, String description){
        mWalletName = walletName;
        mAmount = amount;
        mDescription = description;
    }

    public String getWalletName() {
        return mWalletName;
    }

    public double getAmount() {
        return mAmount;
    }

    public String getDescription() {
        return mDescription;
    }
}
