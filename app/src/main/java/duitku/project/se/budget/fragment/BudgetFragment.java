package duitku.project.se.budget.fragment;

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

import duitku.project.se.budget.BudgetController;
import duitku.project.se.database.DuitkuContract.BudgetEntry;

public class BudgetFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private BudgetFragmentView budgetFragmentView;

    private final int BUDGET_LOADER = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        budgetFragmentView = new BudgetFragmentView(inflater, container, this);
        budgetFragmentView.setUpUI();

        LoaderManager.getInstance(this).initLoader(BUDGET_LOADER, null, this);

        return budgetFragmentView.getView();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == BUDGET_LOADER) {
            String[] projection = new BudgetController(getActivity()).getFullProjection();
            return new CursorLoader(getActivity(), BudgetEntry.CONTENT_URI, projection, null, null, null);
        }
        throw new IllegalStateException("Unknown Loader");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        budgetFragmentView.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        budgetFragmentView.swapCursor(null);
    }

}
