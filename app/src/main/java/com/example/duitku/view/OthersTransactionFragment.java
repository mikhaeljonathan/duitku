package com.example.duitku.view;

import com.example.duitku.controller.WalletController;
import com.example.duitku.database.DuitkuContract.WalletEntry;
import com.example.duitku.database.DuitkuContract.BudgetEntry;
import com.example.duitku.database.DuitkuContract.TransactionEntry;
import com.example.duitku.database.DuitkuContract.CategoryEntry;

import android.app.AlertDialog;
import android.app.Dialog;
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
                projection = new String[]{ BudgetEntry.COLUMN_ID, BudgetEntry.COLUMN_CATEGORY_ID, BudgetEntry.COLUMN_AMOUNT, BudgetEntry.COLUMN_USED};
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

    // adapter nya beda sama ExpandableListView soalnya ini berupa ListView biasa
    // adapternya ini subclass dari CursorAdapter
    class WalletAdapter extends CursorAdapter {

        // construct nya pake List of object
        public WalletAdapter(Context context, Cursor c) {
            super(context, c, 0);
        }

        // ini buat custom view nya

        // layout secara keseluruhan, kalo dibuat dari scratch
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            return LayoutInflater.from(context).inflate(R.layout.item_list_wallet, viewGroup, false);
        }

        // dalemnya layout diisi, selalu direcycle
        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            // view itu view secara keseluruhan
            TextView walletNameTextView = view.findViewById(R.id.item_list_wallet_name_textview);
            TextView walletAmountTextView = view.findViewById(R.id.item_list_wallet_amount_textview);

            // columnnya jadiin integer dlu
            int walletNameColumnIndex = cursor.getColumnIndex(WalletEntry.COLUMN_NAME);
            int walletAmountColumnIndex = cursor.getColumnIndex(WalletEntry.COLUMN_AMOUNT);

            // dapetin value nya
            String walletName = cursor.getString(walletNameColumnIndex);
            double walletAmount = cursor.getDouble(walletAmountColumnIndex);

            // tampilin di view
            walletNameTextView.setText(walletName);
            walletAmountTextView.setText(Double.toString(walletAmount));

        }

    }

    // penjelasan nya kurang lebih sama kayak di atas
    class BudgetAdapter extends CursorAdapter {

        public BudgetAdapter(Context context, Cursor c) {
            super(context, c, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            return LayoutInflater.from(context).inflate(R.layout.item_list_budget, viewGroup, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            TextView budgetCategoryTextView = view.findViewById(R.id.item_list_budget_category_textview);
            TextView budgetAmountTextView = view.findViewById(R.id.item_list_budget_amount_textview);
            TextView budgetUsedTextView = view.findViewById(R.id.item_list_budget_used_textview);
            TextView budgetLeftTextView = view.findViewById(R.id.item_list_budget_left_textview);

            int budgetCategoryColumnIndex = cursor.getColumnIndex(BudgetEntry.COLUMN_CATEGORY_ID);
            int budgetAmountColumnIndex = cursor.getColumnIndex(BudgetEntry.COLUMN_AMOUNT);
            int budgetUsedColumnIndex = cursor.getColumnIndex(BudgetEntry.COLUMN_USED);

            int budgetCategory = cursor.getInt(budgetCategoryColumnIndex);
            double budgetAmount = cursor.getDouble(budgetAmountColumnIndex);
            double budgetUsed = cursor.getDouble(budgetUsedColumnIndex);
            double budgetLeft = budgetAmount - budgetUsed;

            budgetCategoryTextView.setText(budgetCategory);
            budgetAmountTextView.setText(Double.toString(budgetAmount));
            budgetUsedTextView.setText(Double.toString(budgetUsed));
            budgetLeftTextView.setText(Double.toString(budgetLeft));

        }

    }

    public static class ViewWalletDialog extends AppCompatDialogFragment {

        private Cursor mCursor;
        private long mId;

        private TextView walletNameTextView;
        private TextView walletAmountTextView;
        private TextView walletDescTextView;
        private ImageButton editBtn;
        private ImageButton closeBtn;

        public ViewWalletDialog(Cursor cursor, long id){
            super();
            mCursor = cursor;
            mId = id;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view = inflater.inflate(R.layout.dialog_view_wallet, null);

            // initialize the views
            walletNameTextView = view.findViewById(R.id.view_wallet_name_textview);
            walletAmountTextView = view.findViewById(R.id.view_wallet_amount_textview);
            walletDescTextView = view.findViewById(R.id.view_wallet_desc_textview);
            editBtn = view.findViewById(R.id.view_wallet_edit_btn);
            closeBtn = view.findViewById(R.id.view_wallet_close_btn);

            // ambil index col nya
            int walletNameColumnIndex = mCursor.getColumnIndex(WalletEntry.COLUMN_NAME);
            int walletAmountColumnIndex = mCursor.getColumnIndex(WalletEntry.COLUMN_AMOUNT);
            int walletDescColumnIndex = mCursor.getColumnIndex(WalletEntry.COLUMN_DESC);

            // retrieve data nya
            String walletName = mCursor.getString(walletNameColumnIndex);
            double walletAmount = mCursor.getDouble(walletAmountColumnIndex);
            String walletDesc = mCursor.getString(walletDescColumnIndex);

            // set the views
            walletNameTextView.setText(walletName);
            walletAmountTextView.setText(Double.toString(walletAmount));
            walletDescTextView.setText(walletDesc);

            // set 2 button di atas
            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), EditWalletActivity.class);
                }
            });
            closeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            // set dialognya
            builder.setView(view);

            Dialog dialog = builder.create();
            dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary); //biar background dialogny hitam ngab
            return dialog;

        }
    }

    // ini class buat bikin dialog pas mau add wallet
    public static class AddWalletDialog extends AppCompatDialogFragment {

        private EditText walletNameEditText;
        private EditText walletAmountEditText;
        private EditText walletDescriptionEditText;

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view = inflater.inflate(R.layout.dialog_add_wallet, null);

            // initialize view nya
            walletNameEditText = view.findViewById(R.id.add_wallet_walletname_edittext);
            walletAmountEditText = view.findViewById(R.id.add_wallet_amount_edittext);
            walletDescriptionEditText = view.findViewById(R.id.add_wallet_desc_edittext);

            // bikin dialognya
            builder.setView(view)
                    .setTitle("Add Wallet")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // ga ngapa2in
                        }
                    })
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            // ambil value nya
                            String walletName = walletNameEditText.getText().toString().trim();
                            double walletAmount = Double.parseDouble(walletAmountEditText.getText().toString().trim());
                            String walletDesc = walletDescriptionEditText.getText().toString().trim();

                            // panggil controller untuk ditambahin ke database
                            Wallet walletAdded = new Wallet(walletName, walletAmount, walletDesc);
                            Uri uri = new WalletController(getContext()).addWallet(walletAdded);

                            // cek apakah insert nya error
                            if (uri == null){
                                Toast.makeText(getContext(), "Error adding new wallet", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Wallet added", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            // atur title nya
            TextView tv = new TextView(getContext());
            tv.setTextColor(Color.WHITE);
            tv.setText("Add Wallet");
            tv.setPadding(50, 50, 50, 50);
            tv.setTextSize(20F);
            builder.setCustomTitle(tv);

            // return dialog nya
            Dialog dialog = builder.create();
            dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary);
            return dialog;

        }
    }
}
