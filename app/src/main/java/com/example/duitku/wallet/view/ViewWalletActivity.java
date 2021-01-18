package com.example.duitku.wallet.view;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.duitku.transaction.TransactionController;
import com.example.duitku.database.DuitkuContract.TransactionEntry;
import com.example.duitku.wallet.Wallet;
import com.example.duitku.wallet.WalletController;

import java.util.Calendar;

public class ViewWalletActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private int month = Calendar.getInstance().get(Calendar.MONTH);
    private int year = Calendar.getInstance().get(Calendar.YEAR);

    private ViewWalletActivityView viewWalletActivityView;
    private final TransactionController transactionController = new TransactionController(this);

    private long walletId;

    private final int WALLET_TRANSACTION_LOADER = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        walletId = getIntent().getLongExtra("ID", -1);
        viewWalletActivityView = new ViewWalletActivityView(walletId, this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Wallet wallet = new WalletController(this).getWalletById(walletId);
        if (wallet == null){
            finish();
            return;
        }

        LoaderManager.getInstance(this).restartLoader(WALLET_TRANSACTION_LOADER, null, this);
        LoaderManager.getInstance(this).initLoader(WALLET_TRANSACTION_LOADER, null, this);
        viewWalletActivityView.setUpUI();
        viewWalletActivityView.updatePeriodButton(month, year);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == WALLET_TRANSACTION_LOADER) {
            String[] projection = transactionController.getFullProjection();
            String selection = TransactionEntry.COLUMN_DATE + " LIKE ? AND (" + TransactionEntry.COLUMN_WALLET_ID + " = ? OR " + TransactionEntry.COLUMN_WALLET_DEST_ID + " = ?)";
            String[] selectionArgs = new String[]{"%/" + String.format("%02d", month + 1) + "/" + year, Long.toString(walletId), Long.toString(walletId)};
            return new CursorLoader(this, TransactionEntry.CONTENT_URI, projection, selection, selectionArgs, null);
        }
        throw new IllegalStateException("Unknown Loader");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        viewWalletActivityView.setUpAdapter(transactionController.convertCursorToListOfTransaction(data));
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    public void pickMonthYear(int month, int year) {
        this.month = month;
        this.year = year;
        viewWalletActivityView.updatePeriodButton(month, year);
        LoaderManager.getInstance(this).restartLoader(WALLET_TRANSACTION_LOADER, null, this);
    }

}
