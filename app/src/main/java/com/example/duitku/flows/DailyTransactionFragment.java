package com.example.duitku.flows;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.duitku.Utility;
import com.example.duitku.R;
import com.example.duitku.controller.CategoryController;
import com.example.duitku.controller.TransactionController;
import com.example.duitku.database.DuitkuContract.CategoryEntry;
import com.example.duitku.database.DuitkuContract.TransactionEntry;
import com.example.duitku.dialog.MonthYearPickerDialog;
import com.example.duitku.model.Category;
import com.example.duitku.model.DailyTransaction;
import com.example.duitku.model.Transaction;
import com.example.duitku.view.DailyTransactionFragmentView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DailyTransactionFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, MonthYearPickerDialog.PickMonthYearListener {

    private int month;
    private int year;

    private List<DailyTransaction> dailyTransactionList = new ArrayList<>();
    private HashMap<DailyTransaction, List<Transaction>> dailyTransactionListHashMap = new HashMap<>();
    private double totalIncome = 0;
    private double totalExpense = 0;

    private TransactionController transactionController = new TransactionController(getActivity());

    private DailyTransactionFragmentView dailyTransactionFragmentView;

    private final int TRANSACTION_LOADER_DAILY = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // get current date
        Calendar calendar = Calendar.getInstance();
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        // setup the UI
        dailyTransactionFragmentView = new DailyTransactionFragmentView(inflater, container, this);
        dailyTransactionFragmentView.setUpUI();
        dailyTransactionFragmentView.updatePeriodButton(month, year);

//        LoaderManager.getInstance(this).restartLoader(TRANSACTION_LOADER_DAILY, null, this); //kenapa hrs restart?
        LoaderManager.getInstance(this).initLoader(TRANSACTION_LOADER_DAILY, null, this);

        return dailyTransactionFragmentView.getView();
    }

    private List<Transaction> convertCursorToList(Cursor data){
        List<Transaction> ret = new ArrayList<>();
        if (!data.moveToFirst()) return ret;
        do {
            ret.add(transactionController.convertCursorToTransaction(data));
        } while (data.moveToNext());
        return ret;
    }

    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        switch (id){
            case TRANSACTION_LOADER_DAILY:
                String[] projection = transactionController.getProjection();
                String selection = TransactionEntry.COLUMN_DATE + " LIKE ?";
                String[] selectionArgs = new String[]{"%/" + String.format("%02d", month + 1) + "/" + year};
                return new CursorLoader(getActivity(), TransactionEntry.CONTENT_URI, projection, selection, selectionArgs, null);
            default:
                throw new IllegalStateException("Unknown Loader");
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        List<Transaction> allTransactions = convertCursorToList(data);

        // sort transaction brdsrkan tanggal
        Collections.sort(allTransactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction t1, Transaction t2) {
                return t2.getDate().compareTo(t1.getDate()); // dari tanggal yang paling recent
            }
        });

        setUpListAndHashMap(allTransactions);
        dailyTransactionFragmentView.fillListView(dailyTransactionList, dailyTransactionListHashMap, getActivity());
    }

    private void setUpListAndHashMap(List<Transaction> allTransactions){
        // initialize variabel2 penting
        dailyTransactionList.clear();
        dailyTransactionListHashMap.clear();
        List<Transaction> transactions = new ArrayList<>();

        Date lastDate = null;

        if (allTransactions.isEmpty()) return;

        // traverse through list
        for (Transaction curTransaction: allTransactions){
            // kalo dah ganti hari
            if (lastDate != null && !curTransaction.getDate().equals(lastDate)) {
                addToListAndHashMap(lastDate, transactions);
                // reset variabel2 agregasinya
                totalIncome = 0;
                totalExpense = 0;
                transactions = new ArrayList<>();
            }
            updateIncomeAndExpense(curTransaction);
            // setiap iterasi pasti jalanin ini
            transactions.add(curTransaction);
            lastDate = curTransaction.getDate();
        }
        //sisanya
        addToListAndHashMap(lastDate, transactions);
    }

    private void addToListAndHashMap(Date lastDate, List<Transaction> transactions){
        Calendar calendar = Calendar.getInstance();
        // dapetin transaksi yang barusan (posisi skrg - 1) hari apa
        calendar.setTime(lastDate);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // buat object dailytransaction (judul / parentnya)
        DailyTransaction dailyTransaction = new DailyTransaction(dayOfMonth, Utility.daysName[dayOfWeek], totalIncome, totalExpense);
        dailyTransactionList.add(dailyTransaction); // masukin list
        dailyTransactionListHashMap.put(dailyTransaction, transactions); // masukin hashmap juga dr parent ke anak2nya
    }

    private void updateIncomeAndExpense(Transaction curTransaction){
        CategoryController categoryController = new CategoryController(getActivity());
        long categoryId = curTransaction.getCategoryId();
        Category category = categoryController.getCategoryById(categoryId);

        if (category == null) return;

        String type = category.getType();

        if (type.equals(CategoryEntry.TYPE_EXPENSE)){
            totalExpense += curTransaction.getAmount();
        } else {
            totalIncome += curTransaction.getAmount();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {

    }

    @Override
    public void pickMonthYear(int month, int year) {
        this.month = month;
        this.year = year;
        dailyTransactionFragmentView.updatePeriodButton(month, year);
        LoaderManager.getInstance(this).restartLoader(TRANSACTION_LOADER_DAILY, null, this);
    }

}
