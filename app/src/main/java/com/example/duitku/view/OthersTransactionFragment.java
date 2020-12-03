package com.example.duitku.view;

import com.example.duitku.adapter.BudgetAdapter;
import com.example.duitku.adapter.WalletAdapter;
import com.example.duitku.controller.WalletController;
import com.example.duitku.database.DuitkuContract.WalletEntry;
import com.example.duitku.database.DuitkuContract.BudgetEntry;
import com.example.duitku.database.DuitkuContract.TransactionEntry;
import com.example.duitku.database.DuitkuContract.CategoryEntry;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.duitku.R;
import com.example.duitku.database.DuitkuContract;
import com.example.duitku.dialog.AddBudgetDialog;
import com.example.duitku.dialog.AddWalletDialog;
import com.example.duitku.dialog.ViewCategoriesDialog;
import com.example.duitku.dialog.ViewWalletDialog;
import com.example.duitku.model.Budget;
import com.example.duitku.model.Wallet;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OthersTransactionFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    // ini constant buat loadernya, jadi 1 activity itu bisa pake bbrp loader
    // cara bedain nya ya pake variabel2 ini
    // dan di fragment ini kita bakal query wallet sm budget doang
    private static final int WALLET_LOADER = 0;
    private static final int BUDGET_LOADER = 1;

    // di sini ga pake expandable list view, tapi pake 2 listview
    // yaitu listview buat wallet sama budget
    // masing2 listview perlu adapter
    ListView walletListView;
    ListView budgetListView;
    ImageButton addWalletBtn;
    ImageButton addBudgetBtn;

    WalletAdapter walletAdapter;
    BudgetAdapter budgetAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transaction_others, container, false);

        // Initialize view nya
        walletListView = rootView.findViewById(R.id.transaction_others_wallet_listview);
        budgetListView = rootView.findViewById(R.id.transaction_others_budget_listview);
        addWalletBtn = rootView.findViewById(R.id.transaction_others_wallet_add_btn);
        addBudgetBtn = rootView.findViewById(R.id.transaction_others_budget_add_btn);

        walletAdapter = new WalletAdapter(getContext(), null);
        budgetAdapter = new BudgetAdapter(getContext(), null);

        walletListView.setAdapter(walletAdapter);
        budgetListView.setAdapter(budgetAdapter);

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
        addBudgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBudget();
            }
        });

        // ini buat LoaderCallbacks nya ngab, biar ngeretrieve hasil querynya efisien gitu
        LoaderManager.getInstance(this).initLoader(WALLET_LOADER, null, this);
        LoaderManager.getInstance(this).initLoader(BUDGET_LOADER, null, this);

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

    private void addBudget(){
        AddBudgetDialog addBudgetDialog = new AddBudgetDialog();
        addBudgetDialog.show(getFragmentManager(), "Add Budget Dialog");
    }

    // Ini buat nampilin hasil query nya (dibelakang UI thread) biar efisien
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        // kalo beda id loader, dia akan buat CursorLoader yg berbeda jg
        String[] projection;
        switch(id){
            case WALLET_LOADER:
                projection = new String[]{ WalletEntry.COLUMN_ID, WalletEntry.COLUMN_NAME, WalletEntry.COLUMN_AMOUNT, WalletEntry.COLUMN_DESC};
                return new CursorLoader(getContext(), WalletEntry.CONTENT_URI, projection,null,null,null);
            case BUDGET_LOADER:
                projection = new String[]{ BudgetEntry.COLUMN_ID, BudgetEntry.COLUMN_STARTDATE, BudgetEntry.COLUMN_ENDDATE, BudgetEntry.COLUMN_CATEGORY_ID, BudgetEntry.COLUMN_AMOUNT};
                return new CursorLoader(getContext(), BudgetEntry.CONTENT_URI, projection,null,null,null);
            default:
                throw new IllegalStateException("Unknown Loader");
        }
    }

    // Kalau loader udh selesai ngeload execute ini
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        // ini buat naruh data2 nya di adapter yg nanti ditampilin di listview
        int id = loader.getId();
        switch (id){
            case WALLET_LOADER:
                walletAdapter.swapCursor(data);
                break;
            case BUDGET_LOADER:
                budgetAdapter.swapCursor(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        // ini kalo loadernya di reset
        int id = loader.getId();
        switch (id){
            case WALLET_LOADER:
                walletAdapter.swapCursor(null);
                break;
            case BUDGET_LOADER:
                budgetAdapter.swapCursor(null);
                break;
        }
    }

}
