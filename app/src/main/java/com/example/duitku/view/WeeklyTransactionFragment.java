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

import com.example.duitku.Utility;
import com.example.duitku.R;
import com.example.duitku.adapter.WeeklyExpandableAdapter;
import com.example.duitku.database.DuitkuContract.CategoryEntry;
import com.example.duitku.database.DuitkuContract.TransactionEntry;
import com.example.duitku.dialog.MonthYearPickerDialog;
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
    private TextView totalAmountTextView;
    private TextView totalGlobalIncomeTextView;
    private TextView totalGlobalExpenseTextView;

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
        periodTextView.setText(Utility.monthsName[mMonth] + " " + mYear);

        totalAmountTextView = header.findViewById(R.id.transaction_header_weekly_amount_textview);
        totalGlobalIncomeTextView = header.findViewById(R.id.transaction_header_weekly_income_amount_textview);
        totalGlobalExpenseTextView = header.findViewById(R.id.transaction_header_weekly_expense_amount_textview);

        weeklyExpandableListView = rootView.findViewById(R.id.transaction_weekly_expandablelistview);
        weeklyExpandableListView.addHeaderView(header, null, false); // ini buat masukin header nya

        weeklyTransactionList = new ArrayList<>();
        categoryTransactionListHashMap = new HashMap<>();

        periodTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MonthYearPickerDialog monthYearPickerDialog = new MonthYearPickerDialog(WeeklyTransactionFragment.this, mMonth, mYear);
                monthYearPickerDialog.show(getFragmentManager(), "Month Year Picker Dialog");
            }
        });

        LoaderManager.getInstance(this).restartLoader(TRANSACTION_LOADER_WEEKLY, null, this);
        LoaderManager.getInstance(this).initLoader(TRANSACTION_LOADER_WEEKLY, null, this);

        return rootView;
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

        HashMap<Long, CategoryTransaction> categoryTransactionHashMap = new HashMap<>();
        double totalIncome = 0;
        double totalExpense = 0;
        int lastWeek = -1;
        Calendar c = Calendar.getInstance();
        double totalGlobalIncome = 0;
        double totalGlobalExpense = 0;

        List<Transaction> allTransactions = Utility.convertCursorToListOfTransaction(data);

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
                categoryTransactionListHashMap.put(weeklyTransaction, Utility.convertHashMapToList(categoryTransactionHashMap)); // masukin hashmap juga dr parent ke anak2nya

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
                totalGlobalExpense += curTransaction.getAmount();
                totalExpense += curTransaction.getAmount();
            } else if (type.equals(CategoryEntry.TYPE_INCOME)){
                totalGlobalIncome += curTransaction.getAmount();
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
            categoryTransactionListHashMap.put(weeklyTransaction, Utility.convertHashMapToList(categoryTransactionHashMap)); // masukin hashmap juga dr parent ke anak2nya
        }

        totalAmountTextView.setText((totalGlobalIncome - totalGlobalExpense) + "");
        totalGlobalIncomeTextView.setText(totalGlobalIncome + "");
        totalGlobalExpenseTextView.setText(totalGlobalExpense + "");

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
        periodTextView.setText(Utility.monthsName[mMonth] + " " + mYear);
        LoaderManager.getInstance(this).restartLoader(TRANSACTION_LOADER_WEEKLY, null, this);
    }

}
