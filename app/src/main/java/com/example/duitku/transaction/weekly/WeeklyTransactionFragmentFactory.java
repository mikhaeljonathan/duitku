package com.example.duitku.transaction.weekly;

import androidx.fragment.app.Fragment;

import com.example.duitku.interfaces.FragmentFactory;
import com.example.duitku.transaction.weekly.WeeklyTransactionFragment;

public class WeeklyTransactionFragmentFactory implements FragmentFactory {

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
