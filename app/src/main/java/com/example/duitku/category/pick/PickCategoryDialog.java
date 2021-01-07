package com.example.duitku.category.pick;

import android.app.AlertDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.duitku.category.CategoryController;
import com.example.duitku.category.add.AddCategoryDialog;
import com.example.duitku.database.DuitkuContract.CategoryEntry;

import com.example.duitku.R;

public class PickCategoryDialog extends AppCompatDialogFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private GridView gridView;
    private PickCategoryAdapter pickCategoryAdapter;

    private final PickCategoryListener listener;
    private View view;

    private final String categoryType;
    private final int CATEGORY_LOADER = 0;

    public PickCategoryDialog(PickCategoryListener listener, String categoryType){
        this.listener = listener;
        this.categoryType = categoryType;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        setUpUI();

        builder.setView(view);

        LoaderManager.getInstance(this).initLoader(CATEGORY_LOADER, null, this);

        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary); //biar background item
        return dialog;
    }

    private void setUpUI(){
        setUpViews();
        setUpGridView();
        setUpButtons();
    }

    private void setUpViews(){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_pick,
                (ViewGroup) getActivity().findViewById(R.id.dialog_pick_constraintlayout));

        TextView titleTV = view.findViewById(R.id.dialog_pick_title);
        titleTV.setText("Pick Category");
    }

    private void setUpGridView(){
        ListView listView = view.findViewById(R.id.dialog_pick_listview);
        listView.setVisibility(View.GONE);

        gridView = view.findViewById(R.id.dialog_pick_gridview);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                listener.pickCategory(id);
                dismiss();
            }
        });

        setUpAdapter();
    }

    private void setUpAdapter(){
        pickCategoryAdapter = new PickCategoryAdapter(getActivity(), null);
        gridView.setAdapter(pickCategoryAdapter);
    }

    private void setUpButtons(){
        setUpAddBtn();
    }

    private void setUpAddBtn(){
        ImageView addCategoryBtn = view.findViewById(R.id.dialog_pick_add_btn);
        addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCategory();
            }
        });
    }

    private void addCategory(){
        AddCategoryDialog addCategoryDialog = new AddCategoryDialog(categoryType);
        addCategoryDialog.show(getFragmentManager(), "Add Category Dialog");
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == CATEGORY_LOADER) {// category yang diquery tergantung dialog ini dibuka di income atau expense
            String[] projection = new CategoryController(getActivity()).getFullProjection();
            String selection = CategoryEntry.COLUMN_TYPE + " = ?";
            String[] selectionArgs = new String[]{categoryType};
            return new CursorLoader(getActivity(), CategoryEntry.CONTENT_URI, projection, selection, selectionArgs, null);
        }
        throw new IllegalStateException("Unknown Loader");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        pickCategoryAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        pickCategoryAdapter.swapCursor(null);
    }

    public interface PickCategoryListener {
        void pickCategory(long id);
    }

}
