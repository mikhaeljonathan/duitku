package com.example.duitku.factory.fragment;

import androidx.fragment.app.Fragment;

import com.example.duitku.flows.WalletTransactionFragment;

public class WalletTransactionFragmentFactory implements FragmentFactory{

    private static Fragment instance;

    @Override
    public Fragment createProduct() {
        if (instance == null){
            instance = new WalletTransactionFragment();
            return instance;
        }
        return instance;
    }

}
