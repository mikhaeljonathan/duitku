package com.example.duitku.transaction.edit;

import androidx.appcompat.app.AppCompatActivity;

import com.example.duitku.R;
import com.example.duitku.interfaces.UIView;

import android.os.Bundle;

public class EditTransactionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long transactionId = getIntent().getLongExtra("ID", -1);
        UIView editTransactionActivityView = new EditTransactionActivityView(transactionId, this);
        editTransactionActivityView.setUpUI();
    }
}