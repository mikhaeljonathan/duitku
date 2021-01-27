package com.example.duitku.budget.fragment;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.duitku.R;
import com.example.duitku.budget.Budget;
import com.example.duitku.budget.BudgetController;
import com.example.duitku.category.Category;
import com.example.duitku.category.CategoryController;
import com.example.duitku.database.DuitkuContract.BudgetEntry;
import com.example.duitku.main.Utility;

import java.text.DecimalFormat;
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

        ConstraintLayout cl = view.findViewById(R.id.item_list_budget_constraintlayout);
        budget = new BudgetController(context).convertCursorToBudget(cursor);
        category = new CategoryController(context).getCategoryById(budget.getCategory_id());

        setUpName(view);
        setUpUntilDate(view);
        setUpProgressBar(view);

        cl.setBackgroundResource(R.drawable.custom_shape);
    }

    private void setUpName(View view){
        TextView nameTextView = view.findViewById(R.id.item_list_budget_name_textview);
        nameTextView.setText(category.getCategory_name());
    }

    private void setUpUntilDate(View view){
        TextView untilTextView = view.findViewById(R.id.item_list_budget_until_textview);
        untilTextView.setText("Until\n" + getUntilDate(budget));
    }

    private void setUpProgressBar(View view){
        double used = budget.getBudget_used();
        double amount = budget.getBudget_amount();

        ProgressBar progressBar = view.findViewById(R.id.item_list_budget_progressbar);
        progressBar.setMax((int)amount);
        progressBar.setProgress((int)used);

        TextView usedTextView = view.findViewById(R.id.item_list_budget_used_textview);
        TextView amountTextView = view.findViewById(R.id.item_list_budget_amount_textview);

        usedTextView.setText(new DecimalFormat("###,###").format(used));
        amountTextView.setText(new DecimalFormat("###,###").format(amount));
    }

    private String getUntilDate(Budget budget){
        // custom date
        Date endDate = budget.getBudget_enddate();
        if (endDate != null){
            return Utility.convertDateToString(budget.getBudget_enddate());
        }

        // periodically
        Calendar calendar = Calendar.getInstance();

        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        if (budget.getBudget_type().equals(BudgetEntry.TYPE_3MONTH)){
            month = 3 * Utility.getQuarter(month);
        } else if (budget.getBudget_type().equals(BudgetEntry.TYPE_YEAR)){
            month = 12;
        }

        return Utility.getMaxDayOfMonth(month, year) + "/" + month + "/" + year;
    }

}