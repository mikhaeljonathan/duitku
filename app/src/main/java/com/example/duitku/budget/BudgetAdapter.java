package com.example.duitku.budget;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.duitku.R;
import com.example.duitku.category.Category;
import com.example.duitku.category.CategoryController;
import com.example.duitku.database.DuitkuContract.CategoryEntry;
import com.example.duitku.main.Utility;

public class BudgetAdapter extends CursorAdapter {

    private BudgetController budgetController;
    private CategoryController categoryController;

    public BudgetAdapter(Context context, Cursor c) {
        super(context, c, 0);
        budgetController = new BudgetController(context);
        categoryController = new CategoryController(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.item_list_budget, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Budget budget = budgetController.convertCursorToBudget(cursor);
        Category category = categoryController.getCategoryById(budget.getCategoryId());

        String categoryName = category.getName();
        String untilDate = "Gatau";
        double used = budget.getUsed();
        double amount = budget.getAmount();

        TextView nameTextView = view.findViewById(R.id.item_list_budget_name_textview);
        TextView untilTextView = view.findViewById(R.id.item_list_budget_until_textview);
        TextView usedTextView = view.findViewById(R.id.item_list_budget_used_textview);
        TextView amountTextView = view.findViewById(R.id.item_list_budget_amount_textview);
        ProgressBar progressBar = view.findViewById(R.id.item_list_budget_progressbar);

        nameTextView.setText(categoryName);
        untilTextView.setText(untilDate);
        usedTextView.setText(Double.toString(used));
        amountTextView.setText(Double.toString(amount));

        progressBar.setMax((int)amount);
        progressBar.setProgress((int)used);
    }

}