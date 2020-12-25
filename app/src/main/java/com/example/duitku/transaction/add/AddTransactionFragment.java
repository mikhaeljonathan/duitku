package com.example.duitku.transaction.add;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.duitku.interfaces.UIView;
import com.example.duitku.wallet.PickWalletDialog;
import com.example.duitku.category.PickCategoryDialog;
import com.example.duitku.database.DuitkuContract.CategoryEntry;
import com.example.duitku.database.DuitkuContract.WalletEntry;

public class AddTransactionFragment extends Fragment {

    private String type;

    public AddTransactionFragment(String type){
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        UIView addTransactionFragmentView = new AddTransactionFragmentView(inflater, container, this, type);
        addTransactionFragmentView.setUpUI();
        return addTransactionFragmentView.getView();
    }

}
