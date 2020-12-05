package com.example.duitku.view;

import android.content.ContentUris;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.duitku.R;
import com.example.duitku.adapter.DailyExpandableAdapter;
import com.example.duitku.database.DuitkuContract.CategoryEntry;
import com.example.duitku.database.DuitkuContract.TransactionEntry;
import com.example.duitku.model.DailyTransaction;
import com.example.duitku.model.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DailyTransactionFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    // MainActivity --> TransactionFragment --> DailyTransactionFragment
    // Aga ribet tapi ya mau gmn lagi

    // Ini kita pake ExpandableListView buat listView yang bisa di-expand
    private ExpandableListView dailyExpandableListView;
    private DailyExpandableAdapter dailyExpandableAdapter; // ExpandableListView juga perlu adapter

    // DailyTransaction ini buat gabungan dari beberapa Transaction dalam sehari
    // Istilahnya group kalo di ExpandableListView
    private List<DailyTransaction> dailyTransactionList;

    // Setiap DailyTransaction, ada beberapa Transaction
    // Istilahnya child kalo di ExpandableListView
    private HashMap<DailyTransaction, List<Transaction>> dailyTransactionListHashMap;

    // buat loader nya
    private static final int TRANSACTION_LOADER = 0;

    private boolean calledOnResume;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // rootView ini buat nampilin view fragment nya
        View rootView = inflater.inflate(R.layout.fragment_transaction_daily, container, false);
        // header ini buat elemen pertama dari ExpandableListView yang berupa summary nya
        View header = inflater.inflate(R.layout.fragment_transaction_header_month, null);

        // initiate ExpandableListViewnya
        dailyExpandableListView = rootView.findViewById(R.id.transaction_daily_expandablelistview);
        dailyExpandableListView.addHeaderView(header); // ini buat masukin header nya

        dailyTransactionList = new ArrayList<>();
        dailyTransactionListHashMap = new HashMap<>();

        // Ini buat dummy data, data sebenarnya nanti diretrieve dari database
//        setContent();
        calledOnResume = false;

        LoaderManager.getInstance(this).initLoader(TRANSACTION_LOADER, null, this);

        return rootView;
    }

    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        switch (id){
            case TRANSACTION_LOADER:
                String[] projection = new String[]{TransactionEntry.COLUMN_ID,
                        TransactionEntry.COLUMN_DESC,
                        TransactionEntry.COLUMN_DATE,
                        TransactionEntry.COLUMN_AMOUNT,
                        TransactionEntry.COLUMN_WALLET_ID,
                        TransactionEntry.COLUMN_WALLETDEST_ID,
                        TransactionEntry.COLUMN_CATEGORY_ID};
                return new CursorLoader(getContext(), TransactionEntry.CONTENT_URI, projection, null, null, null);
            default:
                throw new IllegalStateException("Unknown Loader");
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        dailyTransactionList.clear();
//        dailyTransactionListHashMap.clear();
//        LoaderManager.getInstance(this).restartLoader(TRANSACTION_LOADER, null, this);
//    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        if (calledOnResume) return;

        int lastDayOfMonth = -1;
        List<Transaction> transactions = new ArrayList<>();
        double totalIncome = 0;
        double totalExpense = 0;

        // dari akhir ke awal biar enak bikin headernya
        if (!data.moveToFirst()) return;
        do {

            // posisi kolom
            int dateColumnIndex = data.getColumnIndex(TransactionEntry.COLUMN_DATE);
            int walletIdColumnIndex = data.getColumnIndex(TransactionEntry.COLUMN_WALLET_ID);
            int walletDestIdColumnIndex = data.getColumnIndex(TransactionEntry.COLUMN_WALLETDEST_ID);
            int categoryIdColumnIndex = data.getColumnIndex(TransactionEntry.COLUMN_CATEGORY_ID);
            int descColumnIndex = data.getColumnIndex(TransactionEntry.COLUMN_DESC);
            int amountColumnIndex = data.getColumnIndex(TransactionEntry.COLUMN_AMOUNT);

            // ambil datanya
            String curDate = data.getString(dateColumnIndex);
            long walletId = data.getLong(walletIdColumnIndex);
            long walletDestId = data.getLong(walletDestIdColumnIndex);
            long categoryId = data.getLong(categoryIdColumnIndex);
            String desc = data.getString(descColumnIndex);
            double amount= data.getDouble(amountColumnIndex);

            // ambil tanggalnya doang buat ngecek apakah sdh ganti hari
            String[] components = curDate.split("/");
            int curDateOfMonth = Integer.parseInt(components[1]);

            Log.v("TESTT", desc + " " + curDateOfMonth);

            // kalo dah ganti hari
            if (curDateOfMonth != lastDayOfMonth && lastDayOfMonth != -1) {
                Log.v("TEST", desc + " " + curDateOfMonth + " " + lastDayOfMonth);
                DailyTransaction dailyTransaction = new DailyTransaction(lastDayOfMonth, "HAHA", totalIncome, totalExpense);
                dailyTransactionList.add(dailyTransaction);
                dailyTransactionListHashMap.put(dailyTransaction, transactions);
                Log.v("TEST-","" +transactions.size());
                totalIncome = 0;
                totalExpense = 0;
                transactions = new ArrayList<>();
            }

            // tipe nya expense atau income
            Cursor temp = getContext().getContentResolver().query(ContentUris.withAppendedId(CategoryEntry.CONTENT_URI, categoryId), new String[]{CategoryEntry.COLUMN_ID, CategoryEntry.COLUMN_TYPE}, null,null, null);
            String type = "TRANS";
            if (temp.moveToFirst()){
                type = temp.getString(temp.getColumnIndex(CategoryEntry.COLUMN_TYPE));
            }

            if (type.equals(CategoryEntry.TYPE_EXPENSE)){
                totalExpense += amount;
            } else {
                totalIncome += amount;
            }

            transactions.add(new Transaction(curDate, walletId, walletDestId, categoryId, amount, desc));
            lastDayOfMonth = curDateOfMonth;

        } while (data.moveToNext());

        // sisanya
        DailyTransaction dailyTransaction = new DailyTransaction(lastDayOfMonth, "HAHA", totalIncome, totalExpense);
        dailyTransactionList.add(dailyTransaction);
        dailyTransactionListHashMap.put(dailyTransaction, transactions);

        // Bikin adapternya
        dailyExpandableAdapter = new DailyExpandableAdapter(dailyTransactionList, dailyTransactionListHashMap, getContext());
        // masukin adapter ke ExpandableListView
        dailyExpandableListView.setAdapter(dailyExpandableAdapter);

        calledOnResume = true;

    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
        Log.v("HEIHEI", "HEHE");
    }

}
