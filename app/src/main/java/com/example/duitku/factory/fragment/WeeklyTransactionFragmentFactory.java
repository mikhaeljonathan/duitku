package com.example.duitku.factory.fragment;

import androidx.fragment.app.Fragment;

import com.example.duitku.flows.WeeklyTransactionFragment;

public class WeeklyTransactionFragmentFactory implements FragmentFactory{

    private static Fragment instance;

    @Override
    public Fragment createProduct() {
        if (instance == null){
            instance = new WeeklyTransactionFragment();
            return instance;
        }
        return instance;
    }

}
