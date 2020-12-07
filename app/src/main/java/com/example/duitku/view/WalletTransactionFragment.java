package com.example.duitku.view;

import com.example.duitku.adapter.WalletAdapter;
import com.example.duitku.database.DuitkuContract.WalletEntry;
import com.example.duitku.database.DuitkuContract.BudgetEntry;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.duitku.R;
import com.example.duitku.dialog.AddBudgetDialog;
import com.example.duitku.dialog.AddWalletDialog;
import com.example.duitku.dialog.ViewWalletDialog;

public class WalletTransactionFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    // ini constant buat loadernya, jadi 1 activity itu bisa pake bbrp loader
    // cara bedain nya ya pake variabel2 ini
    // dan di fragment ini kita bakal query wallet sm budget doang
    private static final int WALLET_LOADER = 0;

    // di sini ga pake expandable list view, tapi pake 2 listview
    // yaitu listview buat wallet sama budget
    // masing2 listview perlu adapter
    private ListView walletListView;
    private ImageButton addWalletBtn;

    private WalletAdapter walletAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transaction_wallet, container, false);

        // Initialize view nya
        walletListView = rootView.findViewById(R.id.transaction_wallet_listview);
        addWalletBtn = rootView.findViewById(R.id.transaction_wallet_add_btn);

        walletAdapter = new WalletAdapter(getContext(), null);
        walletListView.setAdapter(walletAdapter);

        // per item kalau diclick
        walletListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                viewWallet(position, id);
            }
        });

        // Set buat add button nya
        addWalletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWallet();
            }
        });

        // ini buat LoaderCallbacks nya ngab, biar ngeretrieve hasil querynya efisien gitu
        LoaderManager.getInstance(this).initLoader(WALLET_LOADER, null, this);

        return rootView;
    }

    private void viewWallet(int position, long id){
        // ambil cursor nya dlu baru pass ke dialog
        Cursor cursor = (Cursor) walletAdapter.getItem(position);

        ViewWalletDialog viewWalletDialog = new ViewWalletDialog(cursor, id);
        viewWalletDialog.show(getFragmentManager(), "View Wallet Dialog");
    }

    private void addWallet(){
        // bikin dialog dlu buat input data2 dari walletnya
        AddWalletDialog addWalletDialog = new AddWalletDialog();
        addWalletDialog.show(getFragmentManager(), "Add Wallet Dialog");
    }

    // Ini buat nampilin hasil query nya (dibelakang UI thread) biar efisien
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        // kalo beda id loader, dia akan buat CursorLoader yg berbeda jg
        switch(id){
            case WALLET_LOADER:
                String[] projection = new String[]{ WalletEntry.COLUMN_ID, WalletEntry.COLUMN_NAME, WalletEntry.COLUMN_AMOUNT, WalletEntry.COLUMN_DESC};
                return new CursorLoader(getContext(), WalletEntry.CONTENT_URI, projection,null,null,null);
            default:
                throw new IllegalStateException("Unknown Loader");
        }
    }

    // Kalau loader udh selesai ngeload execute ini
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        // ini buat naruh data2 nya di adapter yg nanti ditampilin di listview
        walletAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        // ini kalo loadernya di reset
        walletAdapter.swapCursor(null);

    }

}
