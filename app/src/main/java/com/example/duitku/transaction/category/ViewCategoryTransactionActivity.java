package com.example.duitku.transaction.category;

import androidx.appcompat.app.AppCompatActivity;

import com.example.duitku.interfaces.UIView;

import android.os.Bundle;

public class ViewCategoryTransactionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CategoryTransaction categoryTransaction = (CategoryTransaction) getIntent().getSerializableExtra("CategoryTransaction");

        assert categoryTransaction != null;
        UIView viewCategoryTransactionActivityView = new ViewCategoryTransactionActivityView(categoryTransaction, this);
        viewCategoryTransactionActivityView.setUpUI();
    }

}