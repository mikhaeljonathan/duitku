package com.example.duitku.factory.fragment;

import androidx.fragment.app.Fragment;

import com.example.duitku.flows.BudgetTransactionFragment;

public class BudgetFragmentFactory implements FragmentFactory{

    private static Fragment instance;

    @Override
    public Fragment createProduct() {
        if (instance == null){
            instance = new BudgetTransactionFragment();
            return instance;
        }
        return instance;
    }

}
