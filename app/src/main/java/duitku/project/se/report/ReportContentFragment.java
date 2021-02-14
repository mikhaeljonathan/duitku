package duitku.project.se.report;

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

import duitku.project.se.category.Category;
import duitku.project.se.category.CategoryController;
import duitku.project.se.database.DuitkuContract.TransactionEntry;
import duitku.project.se.transaction.Transaction;
import duitku.project.se.transaction.TransactionController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ReportContentFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final List<Report> reportList = new ArrayList<>();
    private final HashMap<Report, List<Transaction>> reportHashMap = new HashMap<>();
    private final HashMap<Long, List<Transaction>> categoryHashMap = new HashMap<>();

    private int month = Calendar.getInstance().get(Calendar.MONTH);
    private int year = Calendar.getInstance().get(Calendar.YEAR);

    private final TransactionController transactionController = new TransactionController(getActivity());

    private ReportContentFragmentView reportContentFragmentView;
    private final String type;

    private final int REPORT_LOADER = 0;

    public ReportContentFragment(String type) {
        this.type = type;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        reportContentFragmentView = new ReportContentFragmentView(inflater, container, this, type);
        reportContentFragmentView.setUpUI();
        reportContentFragmentView.updatePeriodButton(month, year);

        LoaderManager.getInstance(this).initLoader(REPORT_LOADER, null, this);

        return reportContentFragmentView.getView();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == REPORT_LOADER) {
            String[] projection = transactionController.getFullProjection();
            // yang transfer ga termasuk
            String selection = TransactionEntry.COLUMN_DATE + " LIKE ? AND " + TransactionEntry.COLUMN_CATEGORY_ID + " > 0";
            String[] selectionArgs = new String[]{"%/" + String.format("%02d", month + 1) + "/" + year};
            return new CursorLoader(getContext(), TransactionEntry.CONTENT_URI, projection, selection, selectionArgs, null);
        }
        throw new IllegalStateException("Unknown Loader");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        List<Transaction> allTransactions = transactionController.convertCursorToListOfTransaction(data);
        setUpListAndHashMap(allTransactions);
        Collections.sort(reportList, new Comparator<Report>() {
            @Override
            public int compare(Report report, Report report2) {
                return report.getPercentage() > report2.getPercentage() ? -1 : 1;
            }
        });
        reportContentFragmentView.fillListView(reportList, reportHashMap, getActivity());
    }

    private void setUpListAndHashMap(List<Transaction> allTransactions) {
        // initialize variabel2 penting
        reportList.clear();
        reportHashMap.clear();
        categoryHashMap.clear();

        if (allTransactions.isEmpty()) return;
        double total = 0;

        // traverse through list
        for (Transaction curTransaction : allTransactions) {
            long categoryId = curTransaction.getCategory_id();

            Category category = new CategoryController(getActivity()).getCategoryById(categoryId);
            if (category == null) continue;
            if (!category.getCategory_type().equals(type)) continue;

            if (categoryHashMap.get(categoryId) == null) {
                List<Transaction> transactions = new ArrayList<>();
                transactions.add(curTransaction);
                categoryHashMap.put(categoryId, transactions);
            } else {
                categoryHashMap.get(categoryId).add(curTransaction);
            }
            total += curTransaction.getTransaction_amount();
        }

        addToReportHashMap(total);
    }

    private void addToReportHashMap(double totalGlobal) {
        Iterator hm = categoryHashMap.entrySet().iterator();

        while (hm.hasNext()) {
            Map.Entry element = (Map.Entry) hm.next();
            List<Transaction> transactions = (List<Transaction>) element.getValue();
            double totalTransaction = 0;
            for (Transaction transaction : transactions) {
                totalTransaction += transaction.getTransaction_amount();
            }

            double percentage = totalTransaction / totalGlobal * 100.0;

            Report report = new Report(transactions.get(0).getCategory_id(), totalTransaction, percentage);
            reportHashMap.put(report, transactions);
            reportList.add(report);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    public void pickMonthYear(int month, int year) {
        this.month = month;
        this.year = year;
        reportContentFragmentView.updatePeriodButton(month, year);
        LoaderManager.getInstance(this).restartLoader(REPORT_LOADER, null, this);
    }

}
