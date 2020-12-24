package com.example.duitku.transaction.daily;

import androidx.fragment.app.Fragment;

import com.example.duitku.interfaces.FragmentFactory;
import com.example.duitku.transaction.daily.DailyTransactionFragment;

public class DailyTransactionFragmentFactory implements FragmentFactory {

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
