package com.example.duitku.factory.fragment;

import androidx.fragment.app.Fragment;

import com.example.duitku.flows.DailyTransactionFragment;

public class DailyTransactionFragmentFactory implements FragmentFactory{

    private static Fragment instance;

    @Override
    public Fragment createProduct() {
        if (instance == null){
            instance = new DailyTransactionFragment();
            return instance;
        }
        return instance;
    }

}
