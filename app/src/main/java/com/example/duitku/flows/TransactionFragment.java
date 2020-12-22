package com.example.duitku.flows;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.duitku.R;
import com.example.duitku.view.TransactionFragmentView;
import com.example.duitku.view.UIView;

public class TransactionFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        UIView transactionFragmentView = new TransactionFragmentView(inflater, container, this);
        transactionFragmentView.setUpUI();
        return transactionFragmentView.getView();
    }

}
