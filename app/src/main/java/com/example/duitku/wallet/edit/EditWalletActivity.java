package com.example.duitku.wallet.edit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.duitku.interfaces.UIView;

public class EditWalletActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long walletId = getIntent().getLongExtra("ID", -1);
        UIView editWalletActivityView = new EditWalletActivityView(walletId, this);
        editWalletActivityView.setUpUI();
    }

}