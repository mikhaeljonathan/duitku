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
import com.example.duitku.model.WeeklyTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WeeklyTransactionFragment extends Fragment {

    ExpandableListView weeklyExpandableListView;
    WeeklyExpandableAdapter weeklyExpandableAdapter;
    List<WeeklyTransaction> weeklyTransactionList;
    HashMap<WeeklyTransaction, List<CategoryTransaction>> categoryTransactionListHashMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transaction_weekly, container, false);
        View header = inflater.inflate(R.layout.header_list_view_month, null);

        weeklyExpandableListView = rootView.findViewById(R.id.weekly_expandable_list_view);
        weeklyExpandableListView.addHeaderView(header);
        setContent();

        weeklyExpandableAdapter = new WeeklyExpandableAdapter();
        weeklyExpandableListView.setAdapter(weeklyExpandableAdapter);

        return rootView;
    }

    private void setContent(){

        weeklyTransactionList = new ArrayList<>();
        categoryTransactionListHashMap = new HashMap<>();

        weeklyTransactionList.add(new WeeklyTransaction("12.10.2020 -\n18.10.2020", "Rp 7.000.000", "Rp 5.000.000"));
        weeklyTransactionList.add(new WeeklyTransaction("18.10.2020 -\n24.10.2020", "Rp 5.000.000", "Rp 7.000.000"));
        weeklyTransactionList.add(new WeeklyTransaction("24.10.2020 -\n30.10.2020", "Rp 3.000.000", "Rp 10.000.000"));

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

        categoryTransactionListHashMap.put(weeklyTransactionList.get(0), categoryTransaction1);
        categoryTransactionListHashMap.put(weeklyTransactionList.get(1), categoryTransaction2);
        categoryTransactionListHashMap.put(weeklyTransactionList.get(2), categoryTransaction3);

    }

    class WeeklyExpandableAdapter extends BaseExpandableListAdapter{

        public WeeklyExpandableAdapter(){

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
                view = LayoutInflater.from(getContext()).inflate(R.layout.item_list_weekly_transaction, viewGroup, false);
            }

            TextView intervals = view.findViewById(R.id.weekly_intervals);
            intervals.setText(weeklyTransaction.getIntervals());

            TextView income = view.findViewById(R.id.weekly_income);
            income.setText(weeklyTransaction.getIncome());

            TextView expense = view.findViewById(R.id.weekly_expense);
            expense.setText(weeklyTransaction.getExpense());

            return view;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            CategoryTransaction categoryTransaction = (CategoryTransaction) getChild(i, i1);

            if (view == null){
                view = LayoutInflater.from(getContext()).inflate(R.layout.item_list_category_transaction, viewGroup, false);
            }

            TextView categoryName = view.findViewById(R.id.category_name);
            categoryName.setText(categoryTransaction.getCategory());

            TextView amount = view.findViewById(R.id.category_amount);
            amount.setText(categoryTransaction.getAmount());

            return view;

        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }
}
