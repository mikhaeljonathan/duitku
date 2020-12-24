package com.example.duitku.wallet;

import androidx.fragment.app.Fragment;

import com.example.duitku.interfaces.FragmentFactory;

public class WalletFragmentFactory implements FragmentFactory {

    private static Fragment instance;

    @Override
    public Fragment createProduct() {
        if (instance == null){
            instance = new WalletFragment();
            return instance;
        }
        return instance;
    }

}
