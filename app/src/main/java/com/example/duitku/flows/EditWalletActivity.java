package com.example.duitku.flows;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.duitku.controller.WalletController;
import com.example.duitku.database.DuitkuContract.WalletEntry;

import com.example.duitku.R;
import com.example.duitku.model.Wallet;
import com.example.duitku.view.EditWalletActivityView;
import com.example.duitku.view.UIView;

public class EditWalletActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long walletId = getIntent().getLongExtra("ID", -1);
        UIView editWalletActivityView = new EditWalletActivityView(walletId, this);
        editWalletActivityView.setUpUI();
    }

}