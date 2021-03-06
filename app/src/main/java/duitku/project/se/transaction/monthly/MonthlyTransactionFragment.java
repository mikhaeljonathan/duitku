package duitku.project.se.transaction.monthly;

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

import duitku.project.se.category.CategoryController;
import duitku.project.se.transaction.TransactionController;
import duitku.project.se.database.DuitkuContract;
import duitku.project.se.category.Category;
import duitku.project.se.transaction.category.CategoryTransaction;
import duitku.project.se.transaction.Transaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class MonthlyTransactionFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private int year = Calendar.getInstance().get(Calendar.YEAR);

    private final List<MonthlyTransaction> monthlyTransactionList = new ArrayList<>();
    private final HashMap<MonthlyTransaction, List<CategoryTransaction>> categoryTransactionListHashMap = new HashMap<>();
    private final HashMap<Long, CategoryTransaction> categoryTransactionHashMap = new HashMap<>();
    private double totalIncome;
    private double totalExpense;
    private double totalGlobalIncome;
    private double totalGlobalExpense;

    private final TransactionController transactionController = new TransactionController(getActivity());

    private MonthlyTransactionFragmentView monthlyTransactionFragmentView;

    private final int TRANSACTION_LOADER_MONTHLY = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        if (id == TRANSACTION_LOADER_MONTHLY) {
            String[] projection = transactionController.getFullProjection();
            // yang transfer ga termasuk
            String selection = DuitkuContract.TransactionEntry.COLUMN_DATE + " LIKE ? AND " + DuitkuContract.TransactionEntry.COLUMN_CATEGORY_ID + " > 0";
            String[] selectionArgs = new String[]{"%/%/" + year};
            return new CursorLoader(getContext(), DuitkuContract.TransactionEntry.CONTENT_URI, projection, selection, selectionArgs, null);
        }
        throw new IllegalStateException("Unknown Loader");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        List<Transaction> allTransactions = transactionController.convertCursorToListOfTransaction(data);
        // sort transaction brdsrkan tanggal
        Collections.sort(allTransactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction t1, Transaction t2) {
                return t2.getTransaction_date().compareTo(t1.getTransaction_date()); // dari tanggal yang paling recent
            }
        });
        setUpListAndHashMap(allTransactions);
        monthlyTransactionFragmentView.updateSummary(totalGlobalIncome, totalGlobalExpense);
        monthlyTransactionFragmentView.fillListView(monthlyTransactionList, categoryTransactionListHashMap, getActivity());
    }

    private void setUpListAndHashMap(List<Transaction> allTransactions) {
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
        for (Transaction curTransaction : allTransactions) {
            // kalo dah ganti bulan
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(curTransaction.getTransaction_date());
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
            calendar.setTime(curTransaction.getTransaction_date());
            lastMonth = calendar.get(Calendar.MONTH);
        }
        //sisanya
        addToListAndHashMap(lastMonth);
    }

    private void addToListAndHashMap(int lastMonth) {
        MonthlyTransaction monthlyTransaction = new MonthlyTransaction(lastMonth, totalIncome, totalExpense);
        monthlyTransactionList.add(monthlyTransaction);
        categoryTransactionListHashMap.put(monthlyTransaction, transactionController.convertHashMapToListOfCategoryTransaction(categoryTransactionHashMap));
    }

    private void updateIncomeAndExpense(Transaction curTransaction) {
        CategoryController categoryController = new CategoryController(getActivity());
        long categoryId = curTransaction.getCategory_id();
        Category category = categoryController.getCategoryById(categoryId);

        if (category == null) return;

        String type = category.getCategory_type();
        if (type.equals(DuitkuContract.CategoryEntry.TYPE_EXPENSE)) {
            totalGlobalExpense += curTransaction.getTransaction_amount();
            totalExpense += curTransaction.getTransaction_amount();
        } else {
            totalGlobalIncome += curTransaction.getTransaction_amount();
            totalIncome += curTransaction.getTransaction_amount();
        }
    }

    private void addToCategoryTransactionHashMap(Transaction curTransaction) {
        long categoryId = curTransaction.getCategory_id();
        CategoryTransaction categoryTransaction = categoryTransactionHashMap.get(categoryId);
        if (categoryTransaction == null) { //belum ada category yang setipe dengan transaksi ini
            categoryTransaction = new CategoryTransaction(categoryId, 0);
            categoryTransactionHashMap.put(categoryId, categoryTransaction);
        }
        categoryTransaction.addTransaction(curTransaction);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    public void pickYear(int year) {
        this.year = year;
        monthlyTransactionFragmentView.updatePeriodButton(year);
        LoaderManager.getInstance(this).restartLoader(TRANSACTION_LOADER_MONTHLY, null, this);
    }

}

