package com.example.duitku.category;

import android.app.AlertDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.duitku.database.DuitkuContract.CategoryEntry;

import com.example.duitku.R;

public class PickCategoryDialog extends AppCompatDialogFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private String categoryType;

    private ImageView addCategoryBtn;
    private GridView gridView;

    private ViewCategoryAdapter viewCategoryAdapter;
    private ViewCategoryListener listener;

    private CategoryController categoryController = new CategoryController(getActivity());

    private final int CATEGORY_LOADER = 0;

    public PickCategoryDialog(ViewCategoryListener listener, String type){
        this.listener = listener;
        this.categoryType = type;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_view_category, null);

        setUpUI(view);
        setUpAdapter();

        builder.setView(view);

        LoaderManager.getInstance(this).initLoader(CATEGORY_LOADER, null, this);

        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary); //biar background item
        return dialog;
    }

    private void setUpUI(View view){
        addCategoryBtn = view.findViewById(R.id.view_category_add_btn);
        gridView = view.findViewById(R.id.view_category_gridview);

        // set add button
        addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCategoryDialog addCategoryDialog = new AddCategoryDialog();
                addCategoryDialog.show(getFragmentManager(), "Add Category Dialog");
            }
        });

        // kalau salah 1 elemen dipilih
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                listener.pickCategory(id);
                dismiss();
            }
        });
    }

    private void setUpAdapter(){
        viewCategoryAdapter = new ViewCategoryAdapter(getActivity(), null);
        gridView.setAdapter(viewCategoryAdapter);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch(id){
            case CATEGORY_LOADER:
                // category yang diquery tergantung dialog ini dibuka di income atau expense
                String[] projection = categoryController.getFullProjection();
                String selection = CategoryEntry.COLUMN_TYPE + " = ?";
                String[] selectionArgs = new String[]{categoryType};
                return new CursorLoader(getActivity(), CategoryEntry.CONTENT_URI, projection, selection, selectionArgs, null);
            default:
                throw new IllegalStateException("Unknown Loader");
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        viewCategoryAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        viewCategoryAdapter.swapCursor(null);
    }

    public interface ViewCategoryListener {
        void pickCategory(long id);
    }

}
