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

import com.example.duitku.R;
import com.example.duitku.Utility;
import com.example.duitku.adapter.MonthlyExpandableAdapter;
import com.example.duitku.database.DuitkuContract;
import com.example.duitku.dialog.YearPickerDialog;
import com.example.duitku.model.CategoryTransaction;
import com.example.duitku.model.MonthlyTransaction;
import com.example.duitku.model.Transaction;
import com.example.duitku.model.WeeklyTransaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class MonthlyTransactionFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, YearPickerDialog.PickYearListener {

    // Kurang lebih sama kayak DailyTransactionFragment penjelasannya
    private ExpandableListView monthlyExpandableListView;
    private MonthlyExpandableAdapter monthlyExpandableAdapter;
    private TextView periodTextView;
    private TextView totalAmountTextView;
    private TextView totalGlobalIncomeTextView;
    private TextView totalGlobalExpenseTextView;

    private List<MonthlyTransaction> monthlyTransactionList;
    private HashMap<MonthlyTransaction, List<CategoryTransaction>> categoryTransactionListHashMap;

    private Calendar calendar;
    private int mYear;

    private static final int TRANSACTION_LOADER_MONTHLY = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_transaction_monthly, container, false);
        View header = inflater.inflate(R.layout.fragment_transaction_header_monthly, null);

        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);

        periodTextView = header.findViewById(R.id.transaction_header_monthly_period);
        periodTextView.setText(mYear + "");

        totalAmountTextView = header.findViewById(R.id.transaction_header_monthly_amount_textview);
        totalGlobalIncomeTextView = header.findViewById(R.id.transaction_header_monthly_income_amount_textview);
        totalGlobalExpenseTextView = header.findViewById(R.id.transaction_header_monthly_expense_amount_textview);

        monthlyExpandableListView = rootView.findViewById(R.id.transaction_monthly_expandablelistview);
        monthlyExpandableListView.addHeaderView(header);

        monthlyTransactionList = new ArrayList<>();
        categoryTransactionListHashMap = new HashMap<>();

        periodTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YearPickerDialog yearPickerDialog = new YearPickerDialog(MonthlyTransactionFragment.this, mYear);
                yearPickerDialog.show(getFragmentManager(), "Year Picker Dialog");
            }
        });

        LoaderManager.getInstance(this).restartLoader(TRANSACTION_LOADER_MONTHLY, null, this);
        LoaderManager.getInstance(this).initLoader(TRANSACTION_LOADER_MONTHLY, null, this);

        return rootView;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id) {
            case TRANSACTION_LOADER_MONTHLY:
                String[] projection = new String[]{DuitkuContract.TransactionEntry.COLUMN_ID,
                        DuitkuContract.TransactionEntry.COLUMN_DESC,
                        DuitkuContract.TransactionEntry.COLUMN_DATE,
                        DuitkuContract.TransactionEntry.COLUMN_AMOUNT,
                        DuitkuContract.TransactionEntry.COLUMN_WALLET_ID,
                        DuitkuContract.TransactionEntry.COLUMN_WALLETDEST_ID,
                        DuitkuContract.TransactionEntry.COLUMN_CATEGORY_ID};
                String selection = DuitkuContract.TransactionEntry.COLUMN_DATE + " LIKE ?";
                String[] selectionArgs = new String[]{"%/%/" + mYear};
                return new CursorLoader(getContext(), DuitkuContract.TransactionEntry.CONTENT_URI, projection, selection, selectionArgs, null);
            default:
                throw new IllegalStateException("Unknown Loader");
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        monthlyTransactionList.clear();
        categoryTransactionListHashMap.clear();

        HashMap<Long, CategoryTransaction> categoryTransactionHashMap = new HashMap<>();
        double totalIncome = 0;
        double totalExpense = 0;
        int lastMonth = -1;
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
            if (lastMonth != -1 && c.get(Calendar.MONTH) != lastMonth) {

                // buat object dailytransaction (judul / parentnya)
                MonthlyTransaction monthlyTransaction = new MonthlyTransaction(lastMonth, totalIncome, totalExpense);
                monthlyTransactionList.add(monthlyTransaction); // masukin list
                categoryTransactionListHashMap.put(monthlyTransaction, Utility.convertHashMapToList(categoryTransactionHashMap)); // masukin hashmap juga dr parent ke anak2nya

                // reset variabel2 agregasinya
                totalIncome = 0;
                totalExpense = 0;
                categoryTransactionHashMap = new HashMap<>();

            }

            Cursor temp = getContext().getContentResolver().query(ContentUris.withAppendedId(DuitkuContract.CategoryEntry.CONTENT_URI, curTransaction.getCategoryId()), new String[]{DuitkuContract.CategoryEntry.COLUMN_ID, DuitkuContract.CategoryEntry.COLUMN_TYPE}, null,null, null);
            String type = "TRANS";
            if (temp.moveToFirst()){
                type = temp.getString(temp.getColumnIndex(DuitkuContract.CategoryEntry.COLUMN_TYPE));
            }
            // agregasi expense atau income
            if (type.equals(DuitkuContract.CategoryEntry.TYPE_EXPENSE)){
                totalGlobalExpense += curTransaction.getAmount();
                totalExpense += curTransaction.getAmount();
            } else if (type.equals(DuitkuContract.CategoryEntry.TYPE_INCOME)){
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
            lastMonth = c.get(Calendar.MONTH);
        }

        if (allTransactions.size() > 0){
            MonthlyTransaction monthlyTransaction = new MonthlyTransaction(lastMonth, totalIncome, totalExpense);
            monthlyTransactionList.add(monthlyTransaction); // masukin list
            categoryTransactionListHashMap.put(monthlyTransaction, Utility.convertHashMapToList(categoryTransactionHashMap));
        }

        totalAmountTextView.setText((totalGlobalIncome - totalGlobalExpense) + "");
        totalGlobalIncomeTextView.setText(totalGlobalIncome + "");
        totalGlobalExpenseTextView.setText(totalGlobalExpense + "");

        monthlyExpandableAdapter = new MonthlyExpandableAdapter(monthlyTransactionList, categoryTransactionListHashMap, getContext());
        monthlyExpandableListView.setAdapter(monthlyExpandableAdapter);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }


    @Override
    public void pickYear(int year) {
        mYear = year;
        periodTextView.setText(mYear + "");
        LoaderManager.getInstance(this).restartLoader(TRANSACTION_LOADER_MONTHLY, null, this);
    }

}

