package com.example.duitku.account;

import androidx.fragment.app.Fragment;

import com.example.duitku.interfaces.FragmentFactory;

public class AccountFragmentFactory implements FragmentFactory {

    private static Fragment instance;

    @Override
    public Fragment createProduct() {
        if (instance == null){
            instance = new AccountFragment();
            return instance;
        }
        return instance;
    }

}
