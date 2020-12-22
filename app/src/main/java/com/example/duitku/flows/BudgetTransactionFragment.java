package com.example.duitku.flows;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.duitku.R;
import com.example.duitku.adapter.BudgetAdapter;
import com.example.duitku.database.DuitkuContract.BudgetEntry;
import com.example.duitku.dialog.AddBudgetDialog;

public class BudgetTransactionFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int BUDGET_LOADER = 0;

    private ListView budgetListView;
    private ImageView addBudgetBtn;

    private BudgetAdapter budgetAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transaction_budget, container, false);

        budgetListView = rootView.findViewById(R.id.transaction_budget_listview);
        addBudgetBtn = rootView.findViewById(R.id.transaction_budget_add_btn);

        budgetAdapter = new BudgetAdapter(getContext(), null);
        budgetListView.setAdapter(budgetAdapter);

        budgetListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                viewBudget(position, id);
            }
        });

        addBudgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBudget();
            }
        });

        LoaderManager.getInstance(this).initLoader(BUDGET_LOADER, null, this);

        return rootView;
    }

    private void viewBudget(int position, long id){
        return;
    }

    private void addBudget(){
        AddBudgetDialog addBudgetDialog = new AddBudgetDialog();
        addBudgetDialog.show(getFragmentManager(), "Add Budget Dialog");
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        switch (id){
            case BUDGET_LOADER:
                String[] projection = new String[]{BudgetEntry.COLUMN_ID, BudgetEntry.COLUMN_CATEGORY_ID, BudgetEntry.COLUMN_AMOUNT, BudgetEntry.COLUMN_STARTDATE, BudgetEntry.COLUMN_ENDDATE};
                return new CursorLoader(getContext(), BudgetEntry.CONTENT_URI, projection,null,null,null);
            default:
                throw new IllegalStateException("Unknown Loader");
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        budgetAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        budgetAdapter.swapCursor(null);
    }
}
