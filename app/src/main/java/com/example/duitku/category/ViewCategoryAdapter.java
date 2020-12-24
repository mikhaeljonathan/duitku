package com.example.duitku.category;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.duitku.R;
import com.example.duitku.database.DuitkuContract;

public class ViewCategoryAdapter extends CursorAdapter {

    private CategoryController categoryController;

    public ViewCategoryAdapter(Context context, Cursor c) {
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
        categoryTextView.setText(category.getName());
    }

}
