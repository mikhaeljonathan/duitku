package com.example.duitku.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.duitku.R;
import com.example.duitku.controller.CategoryController;
import com.example.duitku.model.CategoryTransaction;
import com.example.duitku.model.WeeklyTransaction;

import java.util.HashMap;
import java.util.List;

public class WeeklyExpandableAdapter extends BaseExpandableListAdapter {

    private List<WeeklyTransaction> mWeeklyTransactionList;
    private HashMap<WeeklyTransaction, List<CategoryTransaction>> mCategoryTransactionListHashMap;
    private Context mContext;

    public WeeklyExpandableAdapter(List<WeeklyTransaction> weeklyTransactionList,
                                   HashMap<WeeklyTransaction, List<CategoryTransaction>> categoryTransactionListHashMap,
                                   Context context){
        super();
        mContext = context;
        mWeeklyTransactionList = weeklyTransactionList;
        mCategoryTransactionListHashMap = categoryTransactionListHashMap;
    }

    @Override
    public int getGroupCount() {
        return mWeeklyTransactionList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return mCategoryTransactionListHashMap.get(mWeeklyTransactionList.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return mWeeklyTransactionList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return mCategoryTransactionListHashMap.get(mWeeklyTransactionList.get(i)).get(i1);
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_list_transaction_weekly, viewGroup, false);
        }

        TextView weekTextView = view.findViewById(R.id.item_list_transaction_weekly_week_textview);
        weekTextView.setText("Week " + weeklyTransaction.getWeek());

        TextView intervalsTextView = view.findViewById(R.id.item_list_transaction_weekly_intervals_textview);
        intervalsTextView.setText(weeklyTransaction.getIntervals());

        TextView incomeTextView = view.findViewById(R.id.item_list_transaction_weekly_income_textview);
        incomeTextView.setText(Double.toString(weeklyTransaction.getIncome()));

        TextView expenseTextView = view.findViewById(R.id.item_list_transaction_weekly_expense_textview);
        expenseTextView.setText(Double.toString(weeklyTransaction.getExpense()));

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        CategoryTransaction categoryTransaction = (CategoryTransaction) getChild(i, i1);

        if (view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_list_transaction_category, viewGroup, false);
        }

        TextView categoryNameTextView = view.findViewById(R.id.item_list_transaction_category_name_textview);
        categoryNameTextView.setText(new CategoryController(mContext).getCategoryNameById(categoryTransaction.getCategoryId()));

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