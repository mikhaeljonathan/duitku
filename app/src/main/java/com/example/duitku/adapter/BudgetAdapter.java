package com.example.duitku.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.duitku.R;
import com.example.duitku.database.DuitkuContract.CategoryEntry;
import com.example.duitku.database.DuitkuContract.BudgetEntry;


// penjelasan hampir sama kayak walletadapter
public class BudgetAdapter extends CursorAdapter {

    public BudgetAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.item_list_budget, viewGroup, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView budgetCategoryTextView = view.findViewById(R.id.item_list_budget_category_textview);
        TextView budgetAmountTextView = view.findViewById(R.id.item_list_budget_amount_textview);
        TextView budgetUsedTextView = view.findViewById(R.id.item_list_budget_used_textview);
        TextView budgetLeftTextView = view.findViewById(R.id.item_list_budget_left_textview);

        int budgetCategoryColumnIndex = cursor.getColumnIndex(BudgetEntry.COLUMN_CATEGORY_ID);
        int budgetAmountColumnIndex = cursor.getColumnIndex(BudgetEntry.COLUMN_AMOUNT);

        long budgetCategory = cursor.getLong(budgetCategoryColumnIndex);
        double budgetAmount = cursor.getDouble(budgetAmountColumnIndex);

        // tampilin nama category nya
        Uri currentCategoryUri = ContentUris.withAppendedId(CategoryEntry.CONTENT_URI, budgetCategory);
        String[] projection = new String[]{ CategoryEntry.COLUMN_ID, CategoryEntry.COLUMN_NAME};
        Cursor cursorCategory = context.getContentResolver().query(currentCategoryUri, projection, null, null);
        if (cursorCategory.moveToFirst()){
            String categoryName = cursorCategory.getString(cursorCategory.getColumnIndex(CategoryEntry.COLUMN_NAME));
            budgetCategoryTextView.setText(categoryName);
        }

        budgetAmountTextView.setText(Double.toString(budgetAmount));
        budgetUsedTextView.setText(Double.toString(0));
        budgetLeftTextView.setText(Double.toString(0));

    }

}