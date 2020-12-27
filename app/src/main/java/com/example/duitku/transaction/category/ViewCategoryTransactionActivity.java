package com.example.duitku.transaction.category;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.duitku.R;
import com.example.duitku.database.DuitkuContract;
import com.example.duitku.interfaces.UIView;
import com.example.duitku.transaction.TransactionController;

import android.database.Cursor;
import android.os.Bundle;

public class ViewCategoryTransactionActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CategoryTransaction categoryTransaction = (CategoryTransaction) getIntent().getSerializableExtra("CategoryTransaction");

        UIView viewCategoryTransactionActivityView = new ViewCategoryTransactionActivityView(categoryTransaction, this);
        viewCategoryTransactionActivityView.setUpUI();
    }

}