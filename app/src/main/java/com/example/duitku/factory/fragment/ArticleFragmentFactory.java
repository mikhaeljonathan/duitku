package com.example.duitku.factory.fragment;

import androidx.fragment.app.Fragment;

import com.example.duitku.flows.ArticleFragment;
import com.example.duitku.interfaces.FragmentFactory;

public class ArticleFragmentFactory implements FragmentFactory {

    private static Fragment instance;

    @Override
    public Fragment createProduct() {
        if (instance == null){
            instance = new ArticleFragment();
            return instance;
        }
        return instance;
    }

}
