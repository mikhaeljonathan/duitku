package com.example.duitku.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.duitku.R;
import com.example.duitku.database.DuitkuContract;


// penjelasan hampir sama kayak walletadapter
public class BudgetAdapter extends CursorAdapter {

    public BudgetAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.item_list_budget, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView budgetCategoryTextView = view.findViewById(R.id.item_list_budget_category_textview);
        TextView budgetAmountTextView = view.findViewById(R.id.item_list_budget_amount_textview);
        TextView budgetUsedTextView = view.findViewById(R.id.item_list_budget_used_textview);
        TextView budgetLeftTextView = view.findViewById(R.id.item_list_budget_left_textview);

        int budgetCategoryColumnIndex = cursor.getColumnIndex(DuitkuContract.BudgetEntry.COLUMN_CATEGORY_ID);
        int budgetAmountColumnIndex = cursor.getColumnIndex(DuitkuContract.BudgetEntry.COLUMN_AMOUNT);
        int budgetUsedColumnIndex = cursor.getColumnIndex(DuitkuContract.BudgetEntry.COLUMN_USED);

        int budgetCategory = cursor.getInt(budgetCategoryColumnIndex);
        double budgetAmount = cursor.getDouble(budgetAmountColumnIndex);
        double budgetUsed = cursor.getDouble(budgetUsedColumnIndex);
        double budgetLeft = budgetAmount - budgetUsed;

        budgetCategoryTextView.setText(budgetCategory);
        budgetAmountTextView.setText(Double.toString(budgetAmount));
        budgetUsedTextView.setText(Double.toString(budgetUsed));
        budgetLeftTextView.setText(Double.toString(budgetLeft));

    }

}