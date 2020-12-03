package com.example.duitku.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.duitku.database.DuitkuContract.CategoryEntry;

import com.example.duitku.R;

public class ViewCategoriesDialog extends AppCompatDialogFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CATEGORIES_LOADER = 0;
    private String mType;

    private ImageButton addCategoriesBtn;
    private GridView viewCategoriesGridView;

    private ViewCategoriesAdapter viewCategoriesAdapter;

    private ViewCategoriesListener listener;

    public ViewCategoriesDialog(String type){
        super();
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

        // set view ke dialognya
        builder.setView(view);

        // initialize loader
        LoaderManager.getInstance(this).initLoader(CATEGORIES_LOADER, null, this);

        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary); //biar background item
        return dialog;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
//        try {
//            listener = (ViewCategoryListener) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString() + " must implement ViewCategoryListener");
//        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        switch(id){
            case CATEGORIES_LOADER:
                // category yang diquery tergantung dialog ini dibuka di income atau expense
                String[] projection = new String[]{CategoryEntry.COLUMN_ID, CategoryEntry.COLUMN_NAME, CategoryEntry.COLUMN_TYPE};
                String selection = CategoryEntry.COLUMN_TYPE + "=?";
                String[] selectionArgs = new String[] {mType};
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

    private class ViewCategoriesAdapter extends CursorAdapter {

        public ViewCategoriesAdapter(Context context, Cursor c) {
            super(context, c, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            return LayoutInflater.from(context).inflate(R.layout.item_list_category, viewGroup, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            TextView categoryTextView = view.findViewById(R.id.item_list_category_textview);

            int categoryNameColumnIndex = cursor.getColumnIndex(CategoryEntry.COLUMN_NAME);
            String categoryName = cursor.getString(categoryNameColumnIndex);

            categoryTextView.setText(categoryName);

        }

    }

    public interface ViewCategoriesListener {
        void pickCategory();
    }

}
