package duitku.project.se.budget.fragment;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import duitku.project.se.R;
import duitku.project.se.budget.Budget;
import duitku.project.se.budget.BudgetController;
import duitku.project.se.category.Category;
import duitku.project.se.category.CategoryController;
import duitku.project.se.main.Utility;

import java.text.DecimalFormat;

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

    private void setUpName(View view) {
        TextView nameTextView = view.findViewById(R.id.item_list_budget_name_textview);
        nameTextView.setText(category.getCategory_name());
    }

    private void setUpUntilDate(View view) {
        TextView untilTextView = view.findViewById(R.id.item_list_budget_until_textview);
        untilTextView.setText("Until\n" + Utility.getUntilDate(budget));
    }

    private void setUpProgressBar(View view) {
        double used = budget.getBudget_used();
        double amount = budget.getBudget_amount();

        ProgressBar progressBar = view.findViewById(R.id.item_list_budget_progressbar);
        progressBar.setMax((int) amount);
        progressBar.setProgress((int) used);

        TextView usedTextView = view.findViewById(R.id.item_list_budget_used_textview);
        TextView amountTextView = view.findViewById(R.id.item_list_budget_amount_textview);

        usedTextView.setText(new DecimalFormat("###,###").format(used));
        amountTextView.setText(new DecimalFormat("###,###").format(amount));
    }

}