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
import com.example.duitku.model.DailyTransaction;
import com.example.duitku.model.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DailyTransactionFragment extends Fragment {

    ExpandableListView dailyExpandableListView;
    DailyExpandableAdapter dailyExpandableAdapter;
    List<DailyTransaction> dailyTransactionList;
    HashMap<DailyTransaction, List<Transaction>> dailyTransactionListHashMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transaction_daily, container, false);
        View header = inflater.inflate(R.layout.header_list_view_month, null);

        dailyExpandableListView = rootView.findViewById(R.id.daily_expandable_list_view);
        dailyExpandableListView.addHeaderView(header);
        setContent();

        dailyExpandableAdapter = new DailyExpandableAdapter();
        dailyExpandableListView.setAdapter(dailyExpandableAdapter);

        return rootView;
    }

    private void setContent(){

        dailyTransactionList = new ArrayList<>();
        dailyTransactionListHashMap = new HashMap<>();

        dailyTransactionList.add(new DailyTransaction(16, "Fri", "Rp 5.000.000", "Rp 500.000"));
        dailyTransactionList.add(new DailyTransaction(17, "Sat", "Rp 5.000.000", "Rp 500.000"));
        dailyTransactionList.add(new DailyTransaction(18, "Sun", "Rp 5.000.000", "Rp 500.000"));

        List<Transaction> transactions1 = new ArrayList<>();
        List<Transaction> transactions2 = new ArrayList<>();
        List<Transaction> transactions3 = new ArrayList<>();
        for (int i = 0; i < 1;i++){
            transactions1.add(new Transaction("Bank account", "income", "gajian", "Rp 5.000.000"));
            transactions2.add(new Transaction("Cash", "income", "nemu di jalan", "Rp 50.000"));
            transactions3.add(new Transaction("Bank account", "expense", "dipalak", "Rp 100.000"));
        }

        dailyTransactionListHashMap.put(dailyTransactionList.get(0), transactions1);
        dailyTransactionListHashMap.put(dailyTransactionList.get(1), transactions2);
        dailyTransactionListHashMap.put(dailyTransactionList.get(2), transactions3);

    }

    class DailyExpandableAdapter extends BaseExpandableListAdapter{

        public DailyExpandableAdapter(){

        }

        @Override
        public int getGroupCount() {
            return dailyTransactionList.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return dailyTransactionListHashMap.get(dailyTransactionList.get(i)).size();
        }

        @Override
        public Object getGroup(int i) {
            return dailyTransactionList.get(i);
        }

        @Override
        public Object getChild(int i, int i1) {
            return dailyTransactionListHashMap.get(dailyTransactionList.get(i)).get(i1);
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
            DailyTransaction dailyTransaction = (DailyTransaction) getGroup(i);

            if (view == null){
                view = LayoutInflater.from(getContext()).inflate(R.layout.item_list_daily_transaction, viewGroup, false);
            }

            TextView date = view.findViewById(R.id.daily_date);
            date.setText(Integer.toString(dailyTransaction.getDate()));

            TextView day = view.findViewById(R.id.day);
            day.setText(dailyTransaction.getDay());

            TextView income = view.findViewById(R.id.income);
            income.setText(dailyTransaction.getIncome());

            TextView expense = view.findViewById(R.id.expense);
            expense.setText(dailyTransaction.getExpense());

            return view;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            Transaction transaction = (Transaction) getChild(i, i1);

            if (view == null){
                view = LayoutInflater.from(getContext()).inflate(R.layout.item_list_transaction, viewGroup, false);
            }

            TextView category = view.findViewById(R.id.transaction_category);
            category.setText(transaction.getCategory());

            TextView desc = view.findViewById(R.id.transaction_description);
            desc.setText(transaction.getDesc());

            TextView wallet = view.findViewById(R.id.transaction_wallet_name);
            wallet.setText(transaction.getWallet());

            TextView amount = view.findViewById(R.id.amount);
            amount.setText(transaction.getAmount());

            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }

}
