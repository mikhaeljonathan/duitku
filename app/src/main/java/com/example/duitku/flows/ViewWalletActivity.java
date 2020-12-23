package com.example.duitku.flows;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.duitku.controller.TransactionController;
import com.example.duitku.database.DuitkuContract.TransactionEntry;
import com.example.duitku.view.ViewWalletActivityView;

import java.util.Calendar;

public class ViewWalletActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private int month;
    private int year;

    private long walletId;

    private ViewWalletActivityView viewWalletActivityView;
    private TransactionController transactionController = new TransactionController(this);

    private final int WALLET_TRANSACTION_LOADER = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get current date
        Calendar calendar = Calendar.getInstance();
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        walletId = getIntent().getLongExtra("ID", -1);
        viewWalletActivityView = new ViewWalletActivityView(walletId, this);
        viewWalletActivityView.setUpUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoaderManager.getInstance(this).restartLoader(WALLET_TRANSACTION_LOADER, null, this);
        LoaderManager.getInstance(this).initLoader(WALLET_TRANSACTION_LOADER, null, this);
        viewWalletActivityView.setUpUI();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id){
            case WALLET_TRANSACTION_LOADER:
                String[] projection = transactionController.getProjection();
                String selection = TransactionEntry.COLUMN_DATE + " LIKE ? AND " + TransactionEntry.COLUMN_WALLET_ID + " = ?";
                String[] selectionArgs = new String[]{"%/" + String.format("%02d", month + 1) + "/" + year, Long.toString(walletId)};
                return new CursorLoader(this, TransactionEntry.CONTENT_URI, projection, selection, selectionArgs, null);
            default:
                throw new IllegalStateException("Unknown Loader");
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        viewWalletActivityView.getAdapter().swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        viewWalletActivityView.getAdapter().swapCursor(null);
    }

}
