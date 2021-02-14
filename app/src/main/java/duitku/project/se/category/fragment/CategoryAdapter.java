package duitku.project.se.category.fragment;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import duitku.project.se.R;
import duitku.project.se.category.Category;
import duitku.project.se.category.CategoryController;

public class CategoryAdapter extends CursorAdapter {

    private final CategoryController categoryController;

    public CategoryAdapter(Context context, Cursor c) {
        super(context, c, 0);
        categoryController = new CategoryController(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.item_list_category, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView categoryTextView = view.findViewById(R.id.item_list_category_textview);
        Category category = categoryController.convertCursorToCategory(cursor);
        categoryTextView.setText(category.getCategory_name());
    }

}
