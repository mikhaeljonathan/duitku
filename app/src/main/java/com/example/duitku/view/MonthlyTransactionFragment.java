package com.example.duitku.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.duitku.R;
import com.example.duitku.model.CategoryTransaction;
import com.example.duitku.model.MonthlyTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MonthlyTransactionFragment extends Fragment {

    // Kurang lebih sama kayak DailyTransactionFragment penjelasannya

    ExpandableListView monthlyExpandableListView;
    MonthlyExpandableAdapter monthlyExpandableAdapter;
    List<MonthlyTransaction> monthlyTransactionList;
    HashMap<MonthlyTransaction, List<CategoryTransaction>> categoryTransactionListHashMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transaction_monthly, container, false);
        View header = inflater.inflate(R.layout.fragment_transaction_header_year, null);

        monthlyExpandableListView = rootView.findViewById(R.id.transaction_monthly_expandablelistview);
        monthlyExpandableListView.addHeaderView(header);
        setContent();

        monthlyExpandableAdapter = new MonthlyExpandableAdapter();
        monthlyExpandableListView.setAdapter(monthlyExpandableAdapter);

        return rootView;
    }

    private void setContent(){

        monthlyTransactionList = new ArrayList<>();
        categoryTransactionListHashMap = new HashMap<>();

        monthlyTransactionList.add(new MonthlyTransaction("Jan", "Rp 10.000.000", "Rp 50.000.000"));
        monthlyTransactionList.add(new MonthlyTransaction("Feb", "Rp 15.000.000", "Rp 75.000.000"));
        monthlyTransactionList.add(new MonthlyTransaction("Mar", "Rp 20.000.000", "Rp 100.000.000"));

        ArrayList<String> temp = new ArrayList<String>();
        temp.add("Salary");
        temp.add("Transfer");
        temp.add("Food");

        List<CategoryTransaction> categoryTransaction1 = new ArrayList<>();
        List<CategoryTransaction> categoryTransaction2 = new ArrayList<>();
        List<CategoryTransaction> categoryTransaction3 = new ArrayList<>();

        for (int i = 0; i < temp.size();i++){
            categoryTransaction1.add(new CategoryTransaction(temp.get(i), "Rp 100.000"));
            categoryTransaction2.add(new CategoryTransaction(temp.get(i), "Rp 200.000"));
            categoryTransaction3.add(new CategoryTransaction(temp.get(i), "Rp 300.000"));
        }

        categoryTransactionListHashMap.put(monthlyTransactionList.get(0), categoryTransaction1);
        categoryTransactionListHashMap.put(monthlyTransactionList.get(1), categoryTransaction2);
        categoryTransactionListHashMap.put(monthlyTransactionList.get(2), categoryTransaction3);

    }

    class MonthlyExpandableAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return monthlyTransactionList.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return categoryTransactionListHashMap.get(monthlyTransactionList.get(i)).size();
        }

        @Override
        public Object getGroup(int i) {
            return monthlyTransactionList.get(i);
        }

        @Override
        public Object getChild(int i, int i1) {
            return categoryTransactionListHashMap.get(monthlyTransactionList.get(i)).get(i1);
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
                view = getLayoutInflater().from(getContext()).inflate(R.layout.item_list_transaction_monthly, viewGroup, false);
            }

            TextView monthName = view.findViewById(R.id.item_list_transaction_monthly_name_textview);
            monthName.setText(monthlyTransaction.getMonth());

            TextView income = view.findViewById(R.id.item_list_transaction_monthly_income_textview);
            income.setText(monthlyTransaction.getIncome());

            TextView expense = view.findViewById(R.id.item_list_transaction_monthly_expense_textview);
            expense.setText(monthlyTransaction.getExpense());

            return view;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            CategoryTransaction categoryTransaction = (CategoryTransaction) getChild(i, i1);

            if (view == null){
                view = LayoutInflater.from(getContext()).inflate(R.layout.item_list_transaction_category, viewGroup, false);
            }

            TextView categoryName = view.findViewById(R.id.item_list_transaction_category_name_textview);
            categoryName.setText(categoryTransaction.getCategory());

            TextView amount = view.findViewById(R.id.item_list_transaction_category_amount_textview);
            amount.setText(categoryTransaction.getAmount());

            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }
}
