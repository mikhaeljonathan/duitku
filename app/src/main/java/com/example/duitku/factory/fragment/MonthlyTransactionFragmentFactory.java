package com.example.duitku.factory.fragment;

import androidx.fragment.app.Fragment;

import com.example.duitku.flows.MonthlyTransactionFragment;

public class MonthlyTransactionFragmentFactory implements FragmentFactory {

    private static Fragment instance;

    @Override
    public Fragment createProduct() {
        if (instance == null){
            instance = new MonthlyTransactionFragment();
            return instance;
        }
        return instance;
    }

}
