package com.example.duitku.model;

public class Wallet {

    String mWalletName;
    String mAmount;

    public Wallet(String walletName, String amount){
        mWalletName = walletName;
        mAmount = amount;
    }

    public String getWalletName() {
        return mWalletName;
    }

    public String getAmount() {
        return mAmount;
    }
}
