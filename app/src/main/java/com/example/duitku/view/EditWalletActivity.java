package com.example.duitku.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

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

public class EditWalletActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int WALLET_LOADER = 0;
    private Uri currentWalletUri;

    private EditText walletNameEditText;
    private EditText walletAmountEditText;
    private EditText walletDescEditText;
    private ImageButton editWalletBackBtn;
    private Button editWalletSaveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_wallet);

        // custom toolbar
        setSupportActionBar((Toolbar) findViewById(R.id.edit_wallet_toolbar));
        getSupportActionBar().setDisplayShowTitleEnabled(false); // Ga usah judul, diganti sama custom toolbar

        // ambil data uri buat 1 wallet ini
        Intent intent = getIntent();
        currentWalletUri = intent.getData();

        // initialize the views
        walletNameEditText = findViewById(R.id.edit_wallet_name_edittext);
        walletAmountEditText = findViewById(R.id.edit_wallet_amount_edittext);
        walletDescEditText = findViewById(R.id.edit_wallet_desc_edittext);
        editWalletBackBtn = findViewById(R.id.edit_wallet_back_btn);
        editWalletSaveBtn = findViewById(R.id.edit_wallet_save_btn);

        // set the buttons
        editWalletBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        editWalletSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ambil value nya
                String walletName = walletNameEditText.getText().toString().trim();
                double walletAmount = Double.parseDouble(walletAmountEditText.getText().toString().trim());
                String walletDesc = walletDescEditText.getText().toString().trim();

                // panggil contentresolver yg akan return jumlah row yang diupdate
                Wallet wallet = new Wallet(walletName, walletAmount, walletDesc);
                int rowsUpdated = new WalletController(EditWalletActivity.this).updateWallet(wallet, currentWalletUri);

                // kasih pesan error atau berhasil
                if (rowsUpdated == 0){
                    Toast.makeText(EditWalletActivity.this, "Error editing wallet", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditWalletActivity.this, "Wallet edited", Toast.LENGTH_SHORT).show();
                }

                // activity sdh selesai
                finish();
            }
        });

        LoaderManager.getInstance(this).initLoader(WALLET_LOADER, null, this);

    }

    // buat nampilin menu delete di atas kanan
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_wallet_menu, menu);
        return true;
    }

    // kalau menu delete diteken
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_delete_wallet:
                // delete dari database
                int rowsDeleted = new WalletController(EditWalletActivity.this).deleteWallet(currentWalletUri);
                // kasih pesan sukses atau gagal
                if (rowsDeleted == 0){
                    Toast.makeText(this, "Error deleting wallet", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Wallet is deleted", Toast.LENGTH_SHORT).show();
                }
                // activity ny dah selesai
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        String[] projection = {
                WalletEntry._ID,
                WalletEntry.COLUMN_NAME,
                WalletEntry.COLUMN_AMOUNT,
                WalletEntry.COLUMN_DESC
        };

        return new CursorLoader(this,
                currentWalletUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        // cursor cuma 1 dan yang pertama
        if (data.moveToFirst()){
            // posisi kolomnya
            int walletNameColumnIndex = data.getColumnIndex(WalletEntry.COLUMN_NAME);
            int walletAmountColumnIndex = data.getColumnIndex(WalletEntry.COLUMN_AMOUNT);
            int walletDescColumnIndex = data.getColumnIndex(WalletEntry.COLUMN_DESC);

            // ambil data
            String walletName = data.getString(walletNameColumnIndex);
            double walletAmount = data.getDouble(walletAmountColumnIndex);
            String walletDesc = data.getString(walletDescColumnIndex);

            // atur view nya
            walletNameEditText.setText(walletName);
            walletAmountEditText.setText(Double.toString(walletAmount));
            walletDescEditText.setText(walletDesc);

        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}