package com.example.duitku.flows;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.duitku.view.UIView;
import com.example.duitku.view.ViewWalletActivityView;

public class ViewWalletActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long id = getIntent().getLongExtra("ID", -1);
        UIView viewWalletActivityView = new ViewWalletActivityView(id, this);
        viewWalletActivityView.setUpUI();
    }

}
