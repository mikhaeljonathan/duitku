package com.example.duitku.article;

import androidx.fragment.app.Fragment;

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
