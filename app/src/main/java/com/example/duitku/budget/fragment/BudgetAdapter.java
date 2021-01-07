package com.example.duitku.budget.fragment;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.duitku.R;
import com.example.duitku.budget.Budget;
import com.example.duitku.budget.BudgetController;
import com.example.duitku.category.Category;
import com.example.duitku.category.CategoryController;
import com.example.duitku.database.DuitkuContract.BudgetEntry;
import com.example.duitku.main.Utility;

import java.util.Calendar;
import java.util.Date;

public class BudgetAdapter extends CursorAdapter {

    private Budget budget;
    private Category category;

    public BudgetAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.item_list_budget, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        budget = new BudgetController(context).convertCursorToBudget(cursor);
        category = new CategoryController(context).getCategoryById(budget.getCategoryId());

        setUpName(view);
        setUpUntilDate(view);
        setUpProgressBar(view);
    }

    private void setUpName(View view){
        TextView nameTextView = view.findViewById(R.id.item_list_budget_name_textview);
        nameTextView.setText(category.getName());
    }

    private void setUpUntilDate(View view){
        TextView untilTextView = view.findViewById(R.id.item_list_budget_until_textview);
        untilTextView.setText("Until\n" + getUntilDate(budget));
    }

    private void setUpProgressBar(View view){
        double used = budget.getUsed();
        double amount = budget.getAmount();

        ProgressBar progressBar = view.findViewById(R.id.item_list_budget_progressbar);
        progressBar.setMax((int)amount);
        progressBar.setProgress((int)used);

        TextView usedTextView = view.findViewById(R.id.item_list_budget_used_textview);
        TextView amountTextView = view.findViewById(R.id.item_list_budget_amount_textview);

        usedTextView.setText(Double.toString(used));
        amountTextView.setText(Double.toString(amount));
    }

    private String getUntilDate(Budget budget){
        // custom date
        Date endDate = budget.getEndDate();
        if (endDate != null){
            return Utility.convertDateToString(budget.getEndDate());
        }

        // periodically
        Calendar calendar = Calendar.getInstance();

        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        if (budget.getType().equals(BudgetEntry.TYPE_3MONTH)){
            month = 3 * Utility.getQuarter(month);
        } else if (budget.getType().equals(BudgetEntry.TYPE_YEAR)){
            month = 12;
        }

        return Utility.getMaxDayOfMonth(month, year) + "/" + month + "/" + year;
    }

}