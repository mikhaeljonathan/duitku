package com.example.duitku.transaction.parent;

import androidx.fragment.app.Fragment;

import com.example.duitku.interfaces.FragmentFactory;

public class TransactionFragmentFactory implements FragmentFactory {

    private static Fragment instance;

    @Override
    public Fragment createProduct() {
        if (instance == null){
            instance = new TransactionFragment();
            return instance;
        }
        return instance;
    }

}
