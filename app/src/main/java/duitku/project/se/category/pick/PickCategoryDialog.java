package duitku.project.se.category.pick;

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

import duitku.project.se.category.CategoryController;
import duitku.project.se.category.add.AddCategoryDialog;
import duitku.project.se.category.fragment.CategoryAdapter;

import duitku.project.se.R;

import duitku.project.se.database.DuitkuContract;

public class PickCategoryDialog extends AppCompatDialogFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private GridView gridView;
    private CategoryAdapter categoryAdapter;

    private final PickCategoryListener listener;
    private View view;

    private final String categoryType;
    private final int CATEGORY_LOADER = 0;

    public PickCategoryDialog(PickCategoryListener listener, String categoryType) {
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

    private void setUpUI() {
        setUpViews();
        setUpGridView();
        setUpEmptyView();
        setUpButtons();
    }

    private void setUpViews() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_pick,
                (ViewGroup) getActivity().findViewById(R.id.dialog_pick_constraintlayout));

        TextView titleTV = view.findViewById(R.id.dialog_pick_title);
        titleTV.setText("Pick Category");
    }

    private void setUpGridView() {
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

    private void setUpEmptyView() {
        View emptyView = view.findViewById(R.id.dialog_pick_emptyview);

        ImageView imageEmptyView = emptyView.findViewById(R.id.dialog_pick_empty_imageview);
        imageEmptyView.setImageResource(R.drawable.empty_category);

        TextView textView = emptyView.findViewById(R.id.dialog_pick_empty_textview);
        textView.setText("There is no category\nTry adding a new one");

        gridView.setEmptyView(emptyView);
    }

    private void setUpAdapter() {
        categoryAdapter = new CategoryAdapter(getActivity(), null);
        gridView.setAdapter(categoryAdapter);
    }

    private void setUpButtons() {
        setUpAddBtn();
    }

    private void setUpAddBtn() {
        ImageView addCategoryBtn = view.findViewById(R.id.dialog_pick_add_btn);
        addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCategory();
            }
        });
    }

    private void addCategory() {
        AddCategoryDialog addCategoryDialog = new AddCategoryDialog(categoryType);
        addCategoryDialog.show(getFragmentManager(), "Add Category Dialog");
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == CATEGORY_LOADER) {// category yang diquery tergantung dialog ini dibuka di income atau expense
            String[] projection = new CategoryController(getActivity()).getFullProjection();
            String selection = DuitkuContract.CategoryEntry.COLUMN_TYPE + " = ?";
            String[] selectionArgs = new String[]{categoryType};
            return new CursorLoader(getActivity(), DuitkuContract.CategoryEntry.CONTENT_URI, projection, selection, selectionArgs, null);
        }
        throw new IllegalStateException("Unknown Loader");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        categoryAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        categoryAdapter.swapCursor(null);
    }

    public interface PickCategoryListener {
        void pickCategory(long id);
    }

}
