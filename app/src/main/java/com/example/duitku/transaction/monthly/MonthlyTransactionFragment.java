package com.example.duitku.transaction.monthly;

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

import com.example.duitku.main.Utility;
import com.example.duitku.category.CategoryController;
import com.example.duitku.transaction.TransactionController;
import com.example.duitku.database.DuitkuContract;
import com.example.duitku.date.YearPickerDialog;
import com.example.duitku.category.Category;
import com.example.duitku.category.CategoryTransaction;
import com.example.duitku.transaction.Transaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class MonthlyTransactionFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, YearPickerDialog.PickYearListener {

    private int year;

    private List<MonthlyTransaction> monthlyTransactionList = new ArrayList<>();
    private HashMap<MonthlyTransaction, List<CategoryTransaction>> categoryTransactionListHashMap = new HashMap<>();
    private HashMap<Long, CategoryTransaction> categoryTransactionHashMap = new HashMap<>();
    private double totalIncome;
    private double totalExpense;
    private double totalGlobalIncome;
    private double totalGlobalExpense;

    private TransactionController transactionController = new TransactionController(getActivity());

    private MonthlyTransactionFragmentView monthlyTransactionFragmentView;

    private final int TRANSACTION_LOADER_MONTHLY = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // get current year
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        // setup the UI
        monthlyTransactionFragmentView = new MonthlyTransactionFragmentView(inflater, container, this);
        monthlyTransactionFragmentView.setUpUI();
        monthlyTransactionFragmentView.updatePeriodButton(year);

        // ini direstart biar kalau misal ganti period terus kembali ke fragment ini dan onCreateView kepanggil,
        // isi dari expandablelistview nya ga lengket
        LoaderManager.getInstance(this).restartLoader(TRANSACTION_LOADER_MONTHLY, null, this);
        LoaderManager.getInstance(this).initLoader(TRANSACTION_LOADER_MONTHLY, null, this);

        return monthlyTransactionFragmentView.getView();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id) {
            case TRANSACTION_LOADER_MONTHLY:
                String[] projection = transactionController.getFullProjection();
                // yang transfer ga termasuk
                String selection = DuitkuContract.TransactionEntry.COLUMN_DATE + " LIKE ? AND " + DuitkuContract.TransactionEntry.COLUMN_CATEGORY_ID + " > 0";
                String[] selectionArgs = new String[]{"%/%/" + year};
                return new CursorLoader(getContext(), DuitkuContract.TransactionEntry.CONTENT_URI, projection, selection, selectionArgs, null);
            default:
                throw new IllegalStateException("Unknown Loader");
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        List<Transaction> allTransactions = transactionController.convertCursorToList(data);
        // sort transaction brdsrkan tanggal
        Collections.sort(allTransactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction t1, Transaction t2) {
                return t2.getDate().compareTo(t1.getDate()); // dari tanggal yang paling recent
            }
        });
        setUpListAndHashMap(allTransactions);
        monthlyTransactionFragmentView.updateSummary(totalGlobalIncome, totalGlobalExpense);
        monthlyTransactionFragmentView.fillListView(monthlyTransactionList, categoryTransactionListHashMap, getActivity());
    }

    private void setUpListAndHashMap(List<Transaction> allTransactions){
        monthlyTransactionList.clear();
        categoryTransactionListHashMap.clear();
        categoryTransactionHashMap.clear();
        totalIncome = 0;
        totalExpense = 0;
        totalGlobalIncome = 0;
        totalGlobalExpense = 0;

        int lastMonth = -1;

        if (allTransactions.isEmpty()) return;

        // traverse through list
        for (Transaction curTransaction: allTransactions){
            // kalo dah ganti bulan
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(curTransaction.getDate());
            if (lastMonth != -1 && calendar.get(Calendar.MONTH) != lastMonth) {
                addToListAndHashMap(lastMonth);
                // reset variabel2 agregasinya
                totalIncome = 0;
                totalExpense = 0;
                categoryTransactionHashMap.clear();
            }
            updateIncomeAndExpense(curTransaction);
            addToCategoryTransactionHashMap(curTransaction); //ini buat bikin transaksi yang digrup by category
            // setiap iterasi pasti jalanin ini
            calendar.setTime(curTransaction.getDate());
            lastMonth = calendar.get(Calendar.MONTH);
        }
        //sisanya
        addToListAndHashMap(lastMonth);
    }

    private void addToListAndHashMap(int lastMonth){
        MonthlyTransaction monthlyTransaction = new MonthlyTransaction(lastMonth, totalIncome, totalExpense);
        monthlyTransactionList.add(monthlyTransaction);
        categoryTransactionListHashMap.put(monthlyTransaction, transactionController.convertHashMapToList(categoryTransactionHashMap));
    }

    private void updateIncomeAndExpense(Transaction curTransaction){
        CategoryController categoryController = new CategoryController(getActivity());
        long categoryId = curTransaction.getCategoryId();
        Category category = categoryController.getCategoryById(categoryId);

        if (category == null) return;

        String type = category.getType();
        if (type.equals(DuitkuContract.CategoryEntry.TYPE_EXPENSE)){
            totalGlobalExpense += curTransaction.getAmount();
            totalExpense += curTransaction.getAmount();
        } else {
            totalGlobalIncome += curTransaction.getAmount();
            totalIncome += curTransaction.getAmount();
        }
    }

    private void addToCategoryTransactionHashMap(Transaction curTransaction){
        long categoryId = curTransaction.getCategoryId();
        CategoryTransaction categoryTransaction = categoryTransactionHashMap.get(categoryId);
        if (categoryTransaction == null){ //belum ada category yang setipe dengan transaksi ini
            categoryTransaction = new CategoryTransaction(categoryId, 0);
            categoryTransactionHashMap.put(categoryId, categoryTransaction);
        }
        categoryTransaction.addTransaction(curTransaction);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public void pickYear(int year) {
        this.year = year;
        monthlyTransactionFragmentView.updatePeriodButton(year);
        LoaderManager.getInstance(this).restartLoader(TRANSACTION_LOADER_MONTHLY, null, this);
    }

}

