package com.example.duitku.dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.duitku.adapter.ViewCategoriesAdapter;
import com.example.duitku.database.DuitkuContract.CategoryEntry;

import com.example.duitku.R;

public class ViewCategoriesDialog extends AppCompatDialogFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CATEGORIES_LOADER = 0;
    private String mType;

    private ImageButton addCategoriesBtn;
    private GridView viewCategoriesGridView;

    private ViewCategoriesAdapter viewCategoriesAdapter;

    private ViewCategoriesListener listener;

    public ViewCategoriesDialog(Object caller, String type){
        super();
        listener = (ViewCategoriesListener) caller;
        mType = type;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_view_categories, null);

        // initialize view nya
        addCategoriesBtn = view.findViewById(R.id.view_categories_add_btn);
        viewCategoriesGridView = view.findViewById(R.id.view_categories_gridview);

        // bikin cursoradapter
        viewCategoriesAdapter = new ViewCategoriesAdapter(getContext(), null);
        viewCategoriesGridView.setAdapter(viewCategoriesAdapter);

        // implement buttonnya
        addCategoriesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCategoryDialog addCategoryDialog = new AddCategoryDialog();
                addCategoryDialog.show(getFragmentManager(), "Add Category Dialog");
            }
        });

        // kalau salah 1 elemen dipilih
        viewCategoriesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                listener.pickCategory(id);
                dismiss();
            }
        });

        // set view ke dialognya
        builder.setView(view);

        // initialize loader
        LoaderManager.getInstance(this).initLoader(CATEGORIES_LOADER, null, this);

        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary); //biar background item
        return dialog;

    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        switch(id){
            case CATEGORIES_LOADER:
                // category yang diquery tergantung dialog ini dibuka di income atau expense atau dua2nya
                String[] projection = new String[]{CategoryEntry.COLUMN_ID, CategoryEntry.COLUMN_NAME, CategoryEntry.COLUMN_TYPE};
                String selection;
                String[] selectionArgs;
                // salah 1 doang
                if (mType != null){
                    selection = CategoryEntry.COLUMN_TYPE + "=?";
                    selectionArgs = new String[] {mType};
                } else {
                    // dua2nya
                    selection = CategoryEntry.COLUMN_TYPE + "=?" + " OR " + CategoryEntry.COLUMN_TYPE + "=?";
                    selectionArgs = new String[] {CategoryEntry.TYPE_INCOME, CategoryEntry.TYPE_EXPENSE};
                }
                return new CursorLoader(getContext(), CategoryEntry.CONTENT_URI, projection, selection, selectionArgs, null);
            default:
                throw new IllegalStateException("Unknown Loader");
        }

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        viewCategoriesAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        viewCategoriesAdapter.swapCursor(null);
    }

    public interface ViewCategoriesListener {
        void pickCategory(long id);
    }

}
