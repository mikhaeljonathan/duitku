package com.example.duitku.factory.fragment;

import androidx.fragment.app.Fragment;

import com.example.duitku.flows.ReportFragment;

public class ReportFragmentFactory implements FragmentFactory{

    private static Fragment instance;

    @Override
    public Fragment createProduct() {
        if (instance == null){
            instance = new ReportFragment();
            return instance;
        }
        return instance;
    }

}
