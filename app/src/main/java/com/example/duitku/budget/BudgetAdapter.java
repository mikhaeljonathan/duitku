package com.example.duitku.budget;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
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
import com.example.duitku.database.DuitkuContract.BudgetEntry;
import com.example.duitku.database.DuitkuContract.CategoryEntry;
import com.example.duitku.main.Utility;

import java.util.Calendar;
import java.util.Date;

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
        String untilDate = getUntilDate(budget);
        double used = budget.getUsed();
        double amount = budget.getAmount();

        TextView nameTextView = view.findViewById(R.id.item_list_budget_name_textview);
        TextView untilTextView = view.findViewById(R.id.item_list_budget_until_textview);
        TextView usedTextView = view.findViewById(R.id.item_list_budget_used_textview);
        TextView amountTextView = view.findViewById(R.id.item_list_budget_amount_textview);
        ProgressBar progressBar = view.findViewById(R.id.item_list_budget_progressbar);

        nameTextView.setText(categoryName);
        untilTextView.setText("Until\n" + untilDate);
        usedTextView.setText(Double.toString(used));
        amountTextView.setText(Double.toString(amount));

        progressBar.setMax((int)amount);
        progressBar.setProgress((int)used);
    }

    private String getUntilDate(Budget budget){
        // custom date
        Date endDate = budget.getEndDate();
        if (endDate != null){
            return Utility.convertDateToString(budget.getEndDate());
        }

        // budget type
        Calendar calendar = Calendar.getInstance();

        // monthly
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        if (budget.getType().equals(BudgetEntry.TYPE_3MONTH)){
            int quarter = month / 4 + 2; // value dari 1 sampe 4
            month = 3 * quarter;
        } else if (budget.getType().equals(BudgetEntry.TYPE_YEAR)){
            month = 12;
        }

        String ret = Utility.getMaxDayOfMonth(month, year) + "/" + month + "/" + year;
        return ret;
    }

}