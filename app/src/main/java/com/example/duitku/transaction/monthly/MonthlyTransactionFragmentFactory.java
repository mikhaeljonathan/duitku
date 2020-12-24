package com.example.duitku.transaction.monthly;

import androidx.fragment.app.Fragment;

import com.example.duitku.interfaces.FragmentFactory;
import com.example.duitku.transaction.monthly.MonthlyTransactionFragment;

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
