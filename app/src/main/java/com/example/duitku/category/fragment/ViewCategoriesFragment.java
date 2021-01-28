package com.example.duitku.category.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.duitku.R;
import com.example.duitku.category.CategoryController;
import com.example.duitku.category.edit.EditCategoryActivity;
import com.example.duitku.database.DuitkuContract.CategoryEntry;
import com.example.duitku.wallet.view.ViewWalletActivity;

public class ViewCategoriesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private GridView gridView;
    private CategoryAdapter adapter;
    private View view;

    private LayoutInflater inflater;
    private ViewGroup container;

    private final String type;

    private final int CATEGORIES_LOADER = 0;

    private CategoryController categoryController = new CategoryController(getActivity());

    public ViewCategoriesFragment(String type) {
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;

        setUpView();
        setUpEmptyView();
        LoaderManager.getInstance(this).initLoader(CATEGORIES_LOADER, null, this);

        return view;
    }

    private void setUpView() {
        view = inflater.inflate(R.layout.fragment_view_categories, container, false);

        gridView = view.findViewById(R.id.fragment_categories_gridview);
        adapter = new CategoryAdapter(getActivity(), null);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                viewCategory(id);
            }
        });
    }

    private void setUpEmptyView() {
        View emptyView = view.findViewById(R.id.view_categories_emptyview);
        gridView.setEmptyView(emptyView);
    }

    private void viewCategory(long id) {
        Intent viewCategoryIntent = new Intent(getActivity(), EditCategoryActivity.class);
        viewCategoryIntent.putExtra("ID", id);
        getActivity().startActivity(viewCategoryIntent);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == CATEGORIES_LOADER) {
            String[] projection = categoryController.getFullProjection();
            String selection = CategoryEntry.COLUMN_TYPE + " = ?";
            String[] selectionArgs = new String[]{type};
            return new CursorLoader(getActivity(), CategoryEntry.CONTENT_URI, projection, selection, selectionArgs, null);
        }
        throw new IllegalStateException("Unknown Loader");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
