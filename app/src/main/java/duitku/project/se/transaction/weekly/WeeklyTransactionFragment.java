package duitku.project.se.transaction.weekly;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import duitku.project.se.category.CategoryController;
import duitku.project.se.main.Utility;
import duitku.project.se.transaction.TransactionController;
import duitku.project.se.database.DuitkuContract.CategoryEntry;
import duitku.project.se.database.DuitkuContract.TransactionEntry;
import duitku.project.se.category.Category;
import duitku.project.se.transaction.category.CategoryTransaction;
import duitku.project.se.transaction.Transaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class WeeklyTransactionFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private int month = Calendar.getInstance().get(Calendar.MONTH);
    private int year = Calendar.getInstance().get(Calendar.YEAR);

    private final List<WeeklyTransaction> weeklyTransactionList = new ArrayList<>();
    private final HashMap<WeeklyTransaction, List<CategoryTransaction>> categoryTransactionListHashMap = new HashMap<>();
    private final HashMap<Long, CategoryTransaction> categoryTransactionHashMap = new HashMap<>();
    private double totalIncome;
    private double totalExpense;
    private double totalGlobalIncome;
    private double totalGlobalExpense;

    private final TransactionController transactionController = new TransactionController(getActivity());

    private WeeklyTransactionFragmentView weeklyTransactionFragmentView;

    private final int TRANSACTION_LOADER_WEEKLY = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        if (id == TRANSACTION_LOADER_WEEKLY) {
            String[] projection = transactionController.getFullProjection();
            // yang transfer ga termasuk
            String selection = TransactionEntry.COLUMN_DATE + " LIKE ? AND " + TransactionEntry.COLUMN_CATEGORY_ID + " > 0";
            String[] selectionArgs = new String[]{"%/" + String.format("%02d", month + 1) + "/" + year};
            return new CursorLoader(getContext(), TransactionEntry.CONTENT_URI, projection, selection, selectionArgs, null);
        }
        throw new IllegalStateException("Unknown Loader");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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
        weeklyTransactionFragmentView.updateSummary(totalGlobalIncome, totalGlobalExpense);
        weeklyTransactionFragmentView.fillListView(weeklyTransactionList, categoryTransactionListHashMap, getActivity());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setUpListAndHashMap(List<Transaction> allTransactions) {
        // initialize variabel2 penting
        weeklyTransactionList.clear();
        categoryTransactionListHashMap.clear();
        categoryTransactionHashMap.clear();
        totalIncome = 0;
        totalExpense = 0;
        totalGlobalIncome = 0;
        totalGlobalExpense = 0;

        int lastWeek = -1;
        int day = -1;
        int month = -1;
        int year = -1;

        if (allTransactions.isEmpty()) return;

        // traverse through list
        for (Transaction curTransaction : allTransactions) {
            // kalo dah ganti pekan
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(curTransaction.getTransaction_date());
            month = calendar.get(Calendar.MONTH);
            year = calendar.get(Calendar.YEAR);
            if (lastWeek != -1 && calendar.get(Calendar.WEEK_OF_MONTH) != lastWeek) {
                addToListAndHashMap(lastWeek, month, year);
                // reset variabel2 agregasinya
                totalIncome = 0;
                totalExpense = 0;
                categoryTransactionHashMap.clear();
            }
            updateIncomeAndExpense(curTransaction);
            addToCategoryTransactionHashMap(curTransaction); //ini buat bikin transaksi yang digrup by category
            // setiap iterasi pasti jalanin ini
            calendar.setTime(curTransaction.getTransaction_date());

            lastWeek = calendar.get(Calendar.WEEK_OF_MONTH);
            month = calendar.get(Calendar.MONTH);
            year = calendar.get(Calendar.YEAR);
        }
        //sisanya
        addToListAndHashMap(lastWeek, month, year);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addToListAndHashMap(int lastWeek, int month, int year) {
        WeeklyTransaction weeklyTransaction = new WeeklyTransaction(lastWeek, Utility.getIntervalsFromWeek(lastWeek, month, year), totalIncome, totalExpense);
        weeklyTransactionList.add(weeklyTransaction);
        categoryTransactionListHashMap.put(weeklyTransaction, transactionController.convertHashMapToListOfCategoryTransaction(categoryTransactionHashMap));
    }

    private void updateIncomeAndExpense(Transaction curTransaction) {
        CategoryController categoryController = new CategoryController(getActivity());
        long categoryId = curTransaction.getCategory_id();
        Category category = categoryController.getCategoryById(categoryId);

        if (category == null) return;

        String type = category.getCategory_type();
        if (type.equals(CategoryEntry.TYPE_EXPENSE)) {
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

    public void pickMonthYear(int month, int year) {
        this.month = month;
        this.year = year;
        weeklyTransactionFragmentView.updatePeriodButton(month, year);
        LoaderManager.getInstance(this).restartLoader(TRANSACTION_LOADER_WEEKLY, null, this);
    }

}
