package com.example.duitku.view;

import android.content.ContentUris;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.duitku.DateValue;
import com.example.duitku.R;
import com.example.duitku.adapter.WeeklyExpandableAdapter;
import com.example.duitku.database.DuitkuContract.CategoryEntry;
import com.example.duitku.database.DuitkuContract.TransactionEntry;
import com.example.duitku.dialog.MonthYearPickerDialog;
import com.example.duitku.model.Category;
import com.example.duitku.model.CategoryTransaction;
import com.example.duitku.model.Transaction;
import com.example.duitku.model.WeeklyTransaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeeklyTransactionFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, MonthYearPickerDialog.PickMonthYearListener {

    // Kurang lebih sama kayak DailyTransactionFragment penjelasannya

    private ExpandableListView weeklyExpandableListView;
    private WeeklyExpandableAdapter weeklyExpandableAdapter;
    private TextView periodTextView;

    private List<WeeklyTransaction> weeklyTransactionList;
    private HashMap<WeeklyTransaction, List<CategoryTransaction>> categoryTransactionListHashMap;

    private Calendar calendar;
    private int mMonth;
    private int mYear;

    private static final int TRANSACTION_LOADER_WEEKLY = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_transaction_weekly, container, false);
        // header ini buat elemen pertama dari ExpandableListView yang berupa summary nya
        View header = inflater.inflate(R.layout.fragment_transaction_header_weekly, null);

        calendar = Calendar.getInstance();
        mMonth = calendar.get(Calendar.MONTH);
        mYear = calendar.get(Calendar.YEAR);

        periodTextView = header.findViewById(R.id.transaction_header_weekly_period);
        periodTextView.setText(DateValue.monthsName[mMonth] + " " + mYear);

        weeklyExpandableListView = rootView.findViewById(R.id.transaction_weekly_expandablelistview);
        weeklyExpandableListView.addHeaderView(header); // ini buat masukin header nya

        weeklyTransactionList = new ArrayList<>();
        categoryTransactionListHashMap = new HashMap<>();

        periodTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MonthYearPickerDialog monthYearPickerDialog = new MonthYearPickerDialog(WeeklyTransactionFragment.this, mMonth, mYear);
                monthYearPickerDialog.show(getFragmentManager(), "Month Year Picker Dialog");
            }
        });
//        setContent();

        LoaderManager.getInstance(this).restartLoader(TRANSACTION_LOADER_WEEKLY, null, this);
        LoaderManager.getInstance(this).initLoader(TRANSACTION_LOADER_WEEKLY, null, this);

        return rootView;
    }


//    private void setContent(){
//
//
//
//        weeklyTransactionList.add(new WeeklyTransaction("12.10.2020 -\n18.10.2020", "Rp 7.000.000", "Rp 5.000.000"));
//        weeklyTransactionList.add(new WeeklyTransaction("18.10.2020 -\n24.10.2020", "Rp 5.000.000", "Rp 7.000.000"));
//        weeklyTransactionList.add(new WeeklyTransaction("24.10.2020 -\n30.10.2020", "Rp 3.000.000", "Rp 10.000.000"));
//
//        ArrayList<String> temp = new ArrayList<String>();
//        temp.add("Salary");
//        temp.add("Transfer");
//        temp.add("Food");
//
//        List<CategoryTransaction> categoryTransaction1 = new ArrayList<>();
//        List<CategoryTransaction> categoryTransaction2 = new ArrayList<>();
//        List<CategoryTransaction> categoryTransaction3 = new ArrayList<>();
//
//        for (int i = 0; i < temp.size();i++){
//            categoryTransaction1.add(new CategoryTransaction(temp.get(i), "Rp 100.000"));
//            categoryTransaction2.add(new CategoryTransaction(temp.get(i), "Rp 200.000"));
//            categoryTransaction3.add(new CategoryTransaction(temp.get(i), "Rp 300.000"));
//        }
//
//        categoryTransactionListHashMap.put(weeklyTransactionList.get(0), categoryTransaction1);
//        categoryTransactionListHashMap.put(weeklyTransactionList.get(1), categoryTransaction2);
//        categoryTransactionListHashMap.put(weeklyTransactionList.get(2), categoryTransaction3);
//
//    }

    private List<Transaction> convertCursorToList(Cursor data){
        // litsnya kita init dlu
        List<Transaction> ret = new ArrayList<>();

        if (!data.moveToFirst()) return ret;
        do {
            // posisi kolom
            int transactionIdColumnIndex = data.getColumnIndex(TransactionEntry.COLUMN_ID);
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
            long transactionId = data.getLong(transactionIdColumnIndex);
            long walletId = data.getLong(walletIdColumnIndex);
            long walletDestId = data.getLong(walletDestIdColumnIndex);
            long categoryId = data.getLong(categoryIdColumnIndex);
            String desc = data.getString(descColumnIndex);
            double amount= data.getDouble(amountColumnIndex);

            // masukin ke list
            ret.add(new Transaction(transactionId, curDate, walletId, walletDestId, categoryId, amount, desc));

        } while (data.moveToNext());

        // return listnya
        return ret;
    }

    private List<CategoryTransaction> convertHashMapToList(HashMap<Long, CategoryTransaction> hashMap){
        List<CategoryTransaction> ret = new ArrayList<>();
        for (Map.Entry mapElement: hashMap.entrySet()){
            ret.add((CategoryTransaction) mapElement.getValue());
        }
        return ret;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id){
            case TRANSACTION_LOADER_WEEKLY:
                String[] projection = new String[]{TransactionEntry.COLUMN_ID,
                        TransactionEntry.COLUMN_DESC,
                        TransactionEntry.COLUMN_DATE,
                        TransactionEntry.COLUMN_AMOUNT,
                        TransactionEntry.COLUMN_WALLET_ID,
                        TransactionEntry.COLUMN_WALLETDEST_ID,
                        TransactionEntry.COLUMN_CATEGORY_ID};
                String selection = TransactionEntry.COLUMN_DATE + " LIKE ?";
                String[] selectionArgs = new String[]{"%/" + String.format("%02d", mMonth + 1) + "/" + mYear};
                return new CursorLoader(getContext(), TransactionEntry.CONTENT_URI, projection, selection, selectionArgs, null);
            default:
                throw new IllegalStateException("Unknown Loader");
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        weeklyTransactionList.clear();
        categoryTransactionListHashMap.clear();

        List<CategoryTransaction> categorytransactions = new ArrayList<>();
        HashMap<Long, CategoryTransaction> categoryTransactionHashMap = new HashMap<>();
        double totalIncome = 0;
        double totalExpense = 0;
        int lastWeek = -1;
        Calendar c = Calendar.getInstance();

        List<Transaction> allTransactions = convertCursorToList(data);

        Collections.sort(allTransactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction t1, Transaction t2) {
                return t2.getDate().compareTo(t1.getDate()); // dari tanggal yang paling recent
            }
        });

        for (Transaction curTransaction: allTransactions){

            // kalo dah ganti pekan
            c.setTime(curTransaction.getDate());
            if (lastWeek != -1 && c.get(Calendar.WEEK_OF_MONTH) != lastWeek) {

                // buat object dailytransaction (judul / parentnya)
                WeeklyTransaction weeklyTransaction = new WeeklyTransaction(lastWeek, "Intervals", totalIncome, totalExpense);
                weeklyTransactionList.add(weeklyTransaction); // masukin list
                categoryTransactionListHashMap.put(weeklyTransaction, convertHashMapToList(categoryTransactionHashMap)); // masukin hashmap juga dr parent ke anak2nya

                // reset variabel2 agregasinya
                totalIncome = 0;
                totalExpense = 0;
                categoryTransactionHashMap = new HashMap<>();

            }

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

            long categoryId = curTransaction.getCategoryId();
            CategoryTransaction categoryTransaction = categoryTransactionHashMap.get(categoryId);
            if (categoryTransaction == null){
                categoryTransaction = new CategoryTransaction(categoryId, 0);
                categoryTransactionHashMap.put(categoryId, categoryTransaction);
            }
            categoryTransaction.addTransaction(curTransaction.getId());
            categoryTransaction.addAmount(curTransaction.getAmount());

            c.setTime(curTransaction.getDate());
            lastWeek = c.get(Calendar.WEEK_OF_MONTH);

        }

        if (allTransactions.size() > 0){
            WeeklyTransaction weeklyTransaction = new WeeklyTransaction(lastWeek, "Intervals", totalIncome, totalExpense);
            weeklyTransactionList.add(weeklyTransaction); // masukin list
            categoryTransactionListHashMap.put(weeklyTransaction, convertHashMapToList(categoryTransactionHashMap)); // masukin hashmap juga dr parent ke anak2nya
        }

        weeklyExpandableAdapter = new WeeklyExpandableAdapter(weeklyTransactionList, categoryTransactionListHashMap, getContext());
        weeklyExpandableListView.setAdapter(weeklyExpandableAdapter);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public void pickMonthYear(int month, int year) {
        mMonth = month;
        mYear = year;
        periodTextView.setText(DateValue.monthsName[mMonth] + " " + mYear);
        LoaderManager.getInstance(this).restartLoader(TRANSACTION_LOADER_WEEKLY, null, this);
    }

}
