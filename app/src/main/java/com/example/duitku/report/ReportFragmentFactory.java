package com.example.duitku.report;

import androidx.fragment.app.Fragment;

import com.example.duitku.interfaces.FragmentFactory;

public class ReportFragmentFactory implements FragmentFactory {

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
