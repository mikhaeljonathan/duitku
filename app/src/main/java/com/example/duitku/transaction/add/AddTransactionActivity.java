package com.example.duitku.transaction.add;

import androidx.appcompat.app.AppCompatActivity;

import com.example.duitku.interfaces.UIView;

import android.os.Bundle;

public class AddTransactionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIView transactionFragmentView = new AddTransactionActivityView(this);
        transactionFragmentView.setUpUI();
    }

}