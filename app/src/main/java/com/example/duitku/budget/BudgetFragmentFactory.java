package com.example.duitku.budget;

import androidx.fragment.app.Fragment;

import com.example.duitku.interfaces.FragmentFactory;

public class BudgetFragmentFactory implements FragmentFactory {

    private static Fragment instance;

    @Override
    public Fragment createProduct() {
        if (instance == null){
            instance = new BudgetFragment();
            return instance;
        }
        return instance;
    }

}
