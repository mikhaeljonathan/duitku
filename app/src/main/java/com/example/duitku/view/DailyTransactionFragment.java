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
import com.example.duitku.model.Category;
import com.example.duitku.model.DailyTransaction;
import com.example.duitku.model.Transaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
    private static final String[] daysName = {"", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

    // kalau resume jangan panggin onLoaderFInished lagi biar listnya ga dobel2
    private boolean calledOnResume;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // rootView ini buat nampilin view fragment nya
        View rootView = inflater.inflate(R.layout.fragment_transaction_daily, container, false);

        // initiate ExpandableListViewnya
        dailyExpandableListView = rootView.findViewById(R.id.transaction_daily_expandablelistview);

        dailyTransactionList = new ArrayList<>();
        dailyTransactionListHashMap = new HashMap<>();

        // Ini buat dummy data, data sebenarnya nanti diretrieve dari database
        calledOnResume = false;

        // initialize loaderny
        LoaderManager.getInstance(this).initLoader(TRANSACTION_LOADER, null, this);

        return rootView;
    }

    // convert cursor ke list supaya bisa disort brdasarkan tanggal
    private List<Transaction> convertCursorToList(Cursor data){
        // litsnya kita init dlu
        List<Transaction> ret = new ArrayList<>();

        if (!data.moveToFirst()) return ret;
        do {
            // posisi kolom
            int dateColumnIndex = data.getColumnIndex(TransactionEntry.COLUMN_DATE);
            int walletIdColumnIndex = data.getColumnIndex(TransactionEntry.COLUMN_WALLET_ID);
            int walletDestIdColumnIndex = data.getColumnIndex(TransactionEntry.COLUMN_WALLETDEST_ID);
            int categoryIdColumnIndex = data.getColumnIndex(TransactionEntry.COLUMN_CATEGORY_ID);
            int descColumnIndex = data.getColumnIndex(TransactionEntry.COLUMN_DESC);
            int amountColumnIndex = data.getColumnIndex(TransactionEntry.COLUMN_AMOUNT);

            // ambil datanya
            Date curDate = null;
            try {
                curDate = new SimpleDateFormat("dd/MM/yyyy").parse(data.getString(dateColumnIndex));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long walletId = data.getLong(walletIdColumnIndex);
            long walletDestId = data.getLong(walletDestIdColumnIndex);
            long categoryId = data.getLong(categoryIdColumnIndex);
            String desc = data.getString(descColumnIndex);
            double amount= data.getDouble(amountColumnIndex);

            // masukin ke list
            ret.add(new Transaction(curDate, walletId, walletDestId, categoryId, amount, desc));

        } while (data.moveToNext());

        // return listnya
        return ret;
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

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        // kalau resume jgn panggil function ini
        if (calledOnResume) return;

        // initialize variabel2 penting
        int lastDayOfMonth = -1;
        List<Transaction> transactions = new ArrayList<>();
        double totalIncome = 0;
        double totalExpense = 0;
        Date lastDate = null;
        Calendar c = Calendar.getInstance();

        List<Transaction> allTransactions = convertCursorToList(data);
        if (allTransactions.size() == 0) return;

        Collections.sort(allTransactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction t1, Transaction t2) {
                return t2.getDate().compareTo(t1.getDate()); // dari tanggal yang paling recent
            }
        });

        for (Transaction curTransaction: allTransactions){

            // ambil tanggalnya doang buat ngecek apakah sdh ganti hari
            c.setTime(curTransaction.getDate());
            int curDateOfMonth = c.get(Calendar.DAY_OF_MONTH);

            // kalo dah ganti hari
            if (curDateOfMonth != lastDayOfMonth && lastDayOfMonth != -1) {

                // dapetin transaksi yang barusan (posisi skrg - 1) hari apa
                c.setTime(lastDate);
                int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

                // buat object dailytransaction (judul / parentnya)
                DailyTransaction dailyTransaction = new DailyTransaction(lastDayOfMonth, daysName[dayOfWeek], totalIncome, totalExpense);
                dailyTransactionList.add(dailyTransaction); // masukin list
                dailyTransactionListHashMap.put(dailyTransaction, transactions); // masukin hashmap juga dr parent ke anak2nya

                // reset variabel2 agregasinya
                totalIncome = 0;
                totalExpense = 0;
                transactions = new ArrayList<>();

            }

            // tipe nya expense atau income
            Cursor temp = getContext().getContentResolver().query(ContentUris.withAppendedId(CategoryEntry.CONTENT_URI, curTransaction.getCategoryId()), new String[]{CategoryEntry.COLUMN_ID, CategoryEntry.COLUMN_TYPE}, null,null, null);
            String type = "TRANS";
            if (temp.moveToFirst()){
                type = temp.getString(temp.getColumnIndex(CategoryEntry.COLUMN_TYPE));
            }

            // agregasi expense atau income
            if (type.equals(CategoryEntry.TYPE_EXPENSE)){
                totalExpense += curTransaction.getAmount();
            } else if (type.equals(CategoryEntry.TYPE_INCOME)){
                totalIncome += curTransaction.getAmount();
            }

            // setiap iterasi pasti jalanin ini
            transactions.add(curTransaction);
            lastDayOfMonth = curDateOfMonth;
            lastDate = curTransaction.getDate();
        }

        // sisanya
        c.setTime(lastDate);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        DailyTransaction dailyTransaction = new DailyTransaction(lastDayOfMonth, daysName[dayOfWeek], totalIncome, totalExpense);
        dailyTransactionList.add(dailyTransaction);
        dailyTransactionListHashMap.put(dailyTransaction, transactions);

        // Bikin adapternya
        dailyExpandableAdapter = new DailyExpandableAdapter(dailyTransactionList, dailyTransactionListHashMap, getContext());
        // masukin adapter ke ExpandableListView
        dailyExpandableListView.setAdapter(dailyExpandableAdapter);

        // kalau udh pernah jalanin onLoadFinished ini, jgn pernah jalanin onLoadFinished lagi
        calledOnResume = true;

    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {

    }

}
