package com.example.duitku.transaction.weekly;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.duitku.R;
import com.example.duitku.category.CategoryController;
import com.example.duitku.category.Category;
import com.example.duitku.category.CategoryTransaction;
import com.example.duitku.transaction.weekly.WeeklyTransaction;

import java.util.HashMap;
import java.util.List;

public class WeeklyExpandableAdapter extends BaseExpandableListAdapter {

    private List<WeeklyTransaction> weeklyTransactionList;
    private HashMap<WeeklyTransaction, List<CategoryTransaction>> categoryTransactionListHashMap;
    private Context context;

    public WeeklyExpandableAdapter(List<WeeklyTransaction> weeklyTransactionList,
                                   HashMap<WeeklyTransaction, List<CategoryTransaction>> categoryTransactionListHashMap,
                                   Context context){
        this.weeklyTransactionList = weeklyTransactionList;
        this.categoryTransactionListHashMap = categoryTransactionListHashMap;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return weeklyTransactionList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return categoryTransactionListHashMap.get(weeklyTransactionList.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return weeklyTransactionList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return categoryTransactionListHashMap.get(weeklyTransactionList.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        WeeklyTransaction weeklyTransaction = (WeeklyTransaction) getGroup(i);
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_list_transaction_weekly, viewGroup, false);
        }

        TextView weekTextView = view.findViewById(R.id.item_list_transaction_weekly_week_textview);
        weekTextView.setText("Week " + weeklyTransaction.getWeek());

        TextView intervalsTextView = view.findViewById(R.id.item_list_transaction_weekly_intervals_textview);
        intervalsTextView.setText(weeklyTransaction.getIntervals());

        TextView incomeTextView = view.findViewById(R.id.item_list_transaction_weekly_income_textview);
        incomeTextView.setText(weeklyTransaction.getIncome() + "");

        TextView expenseTextView = view.findViewById(R.id.item_list_transaction_weekly_expense_textview);
        expenseTextView.setText(weeklyTransaction.getExpense() + "");

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        CategoryTransaction categoryTransaction = (CategoryTransaction) getChild(i, i1);
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_list_transaction_category, viewGroup, false);
        }

        TextView categoryNameTextView = view.findViewById(R.id.item_list_transaction_category_name_textview);
        CategoryController categoryController = new CategoryController(context);
        Category category = categoryController.getCategoryById(categoryTransaction.getCategoryId());
        categoryNameTextView.setText(category.getName());

        TextView transactionCountTextView = view.findViewById(R.id.item_list_transaction_category_transactioncount_textview);
        transactionCountTextView.setText("There are " + categoryTransaction.getTransactions().size() + " transaction(s)");

        TextView amountTextView = view.findViewById(R.id.item_list_transaction_category_amount_textview);
        amountTextView.setText(Double.toString(categoryTransaction.getAmount()));

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}