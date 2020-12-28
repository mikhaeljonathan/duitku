package com.example.duitku.transaction.add;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.duitku.interfaces.UIView;

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
