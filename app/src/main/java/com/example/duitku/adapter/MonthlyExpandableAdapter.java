package com.example.duitku.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.duitku.R;
import com.example.duitku.Utility;
import com.example.duitku.controller.CategoryController;
import com.example.duitku.model.CategoryTransaction;
import com.example.duitku.model.MonthlyTransaction;
import com.example.duitku.model.WeeklyTransaction;

import java.util.HashMap;
import java.util.List;

public class MonthlyExpandableAdapter extends BaseExpandableListAdapter {

    private List<MonthlyTransaction> mMonthlyTransactionList;
    private HashMap<MonthlyTransaction, List<CategoryTransaction>> mCategoryTransactionListHashMap;
    private Context mContext;

    public MonthlyExpandableAdapter(List<MonthlyTransaction> monthlyTransactionList,
                                    HashMap<MonthlyTransaction, List<CategoryTransaction>> categoryTransactionListHashMap,
                                    Context context){
        mMonthlyTransactionList = monthlyTransactionList;
        mCategoryTransactionListHashMap = categoryTransactionListHashMap;
        mContext = context;
    }

    @Override
    public int getGroupCount() {
        return mMonthlyTransactionList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return mCategoryTransactionListHashMap.get(mMonthlyTransactionList.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return mMonthlyTransactionList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return mCategoryTransactionListHashMap.get(mMonthlyTransactionList.get(i)).get(i1);
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
        MonthlyTransaction monthlyTransaction = (MonthlyTransaction) getGroup(i);

        if (view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_list_transaction_monthly, viewGroup, false);
        }

        TextView monthName = view.findViewById(R.id.item_list_transaction_monthly_name_textview);
        monthName.setText(Utility.monthsNameShort[monthlyTransaction.getMonth()]);

        TextView income = view.findViewById(R.id.item_list_transaction_monthly_income_textview);
        income.setText(monthlyTransaction.getIncome() + "");

        TextView expense = view.findViewById(R.id.item_list_transaction_monthly_expense_textview);
        expense.setText(monthlyTransaction.getExpense() + "");

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
