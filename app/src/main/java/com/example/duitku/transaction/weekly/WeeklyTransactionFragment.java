package com.example.duitku.transaction.weekly;

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

import com.example.duitku.category.CategoryController;
import com.example.duitku.transaction.TransactionController;
import com.example.duitku.database.DuitkuContract.CategoryEntry;
import com.example.duitku.database.DuitkuContract.TransactionEntry;
import com.example.duitku.date.MonthYearPickerDialog;
import com.example.duitku.category.Category;
import com.example.duitku.transaction.category.CategoryTransaction;
import com.example.duitku.transaction.Transaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class WeeklyTransactionFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, MonthYearPickerDialog.PickMonthYearListener {

    private int month;
    private int year;

    private List<WeeklyTransaction> weeklyTransactionList = new ArrayList<>();
    private HashMap<WeeklyTransaction, List<CategoryTransaction>> categoryTransactionListHashMap = new HashMap<>();
    private HashMap<Long, CategoryTransaction> categoryTransactionHashMap = new HashMap<>();
    private double totalIncome;
    private double totalExpense;
    private double totalGlobalIncome;
    private double totalGlobalExpense;

    private TransactionController transactionController = new TransactionController(getActivity());

    private WeeklyTransactionFragmentView weeklyTransactionFragmentView;

    private final int TRANSACTION_LOADER_WEEKLY = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // get current month and year
        Calendar calendar = Calendar.getInstance();
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        // setup the UI
        weeklyTransactionFragmentView = new WeeklyTransactionFragmentView(inflater, container, this);
        weeklyTransactionFragmentView.setUpUI();
        weeklyTransactionFragmentView.updatePeriodButton(month, year);

        // ini direstart biar kalau misal ganti period terus kembali ke fragment ini dan onCreateView kepanggil,
        // isi dari expandablelistview nya ga lengket
        LoaderManager.getInstance(this).restartLoader(TRANSACTION_LOADER_WEEKLY, null, this);
        LoaderManager.getInstance(this).initLoader(TRANSACTION_LOADER_WEEKLY, null, this);

        return weeklyTransactionFragmentView.getView();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id){
            case TRANSACTION_LOADER_WEEKLY:
                String[] projection = transactionController.getFullProjection();
                // yang transfer ga termasuk
                String selection = TransactionEntry.COLUMN_DATE + " LIKE ? AND " + TransactionEntry.COLUMN_CATEGORY_ID + " > 0";
                String[] selectionArgs = new String[]{"%/" + String.format("%02d", month + 1) + "/" + year};
                return new CursorLoader(getContext(), TransactionEntry.CONTENT_URI, projection, selection, selectionArgs, null);
            default:
                throw new IllegalStateException("Unknown Loader");
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        List<Transaction> allTransactions = transactionController.convertCursorToListOfTransaction(data);
        // sort transaction brdsrkan tanggal
        Collections.sort(allTransactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction t1, Transaction t2) {
                return t2.getDate().compareTo(t1.getDate()); // dari tanggal yang paling recent
            }
        });
        setUpListAndHashMap(allTransactions);
        weeklyTransactionFragmentView.updateSummary(totalGlobalIncome, totalGlobalExpense);
        weeklyTransactionFragmentView.fillListView(weeklyTransactionList, categoryTransactionListHashMap, getActivity());
    }

    private void setUpListAndHashMap(List<Transaction> allTransactions){
        // initialize variabel2 penting
        weeklyTransactionList.clear();
        categoryTransactionListHashMap.clear();
        categoryTransactionHashMap.clear();
        totalIncome = 0;
        totalExpense = 0;
        totalGlobalIncome = 0;
        totalGlobalExpense = 0;

        int lastWeek = -1;

        if (allTransactions.isEmpty()) return;

        // traverse through list
        for (Transaction curTransaction: allTransactions){
            // kalo dah ganti pekan
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(curTransaction.getDate());
            if (lastWeek != -1 && calendar.get(Calendar.WEEK_OF_MONTH) != lastWeek) {
                addToListAndHashMap(lastWeek);
                // reset variabel2 agregasinya
                totalIncome = 0;
                totalExpense = 0;
                categoryTransactionHashMap.clear();
            }
            updateIncomeAndExpense(curTransaction);
            addToCategoryTransactionHashMap(curTransaction); //ini buat bikin transaksi yang digrup by category
            // setiap iterasi pasti jalanin ini
            calendar.setTime(curTransaction.getDate());
            lastWeek = calendar.get(Calendar.WEEK_OF_MONTH);
        }
        //sisanya
        addToListAndHashMap(lastWeek);
    }

    private void addToListAndHashMap(int lastWeek){
        WeeklyTransaction weeklyTransaction = new WeeklyTransaction(lastWeek, "Intervals", totalIncome, totalExpense);
        weeklyTransactionList.add(weeklyTransaction);
        categoryTransactionListHashMap.put(weeklyTransaction, transactionController.convertHashMapToListOfCategoryTransaction(categoryTransactionHashMap));
    }

    private void updateIncomeAndExpense(Transaction curTransaction){
        CategoryController categoryController = new CategoryController(getActivity());
        long categoryId = curTransaction.getCategoryId();
        Category category = categoryController.getCategoryById(categoryId);

        if (category == null) return;

        String type = category.getType();
        if (type.equals(CategoryEntry.TYPE_EXPENSE)){
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
    public void pickMonthYear(int month, int year) {
        this.month = month;
        this.year = year;
        weeklyTransactionFragmentView.updatePeriodButton(month, year);
        LoaderManager.getInstance(this).restartLoader(TRANSACTION_LOADER_WEEKLY, null, this);
    }

}
