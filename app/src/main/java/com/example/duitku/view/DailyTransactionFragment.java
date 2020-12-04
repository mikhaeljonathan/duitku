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

    // MainActivity --> TransactionFragment --> DailyTransactionFragment
    // Aga ribet tapi ya mau gmn lagi

    // Ini kita pake ExpandableListView buat listView yang bisa di-expand
    ExpandableListView dailyExpandableListView;
    DailyExpandableAdapter dailyExpandableAdapter; // ExpandableListView juga perlu adapter

    // DailyTransaction ini buat gabungan dari beberapa Transaction dalam sehari
    // Istilahnya group kalo di ExpandableListView
    List<DailyTransaction> dailyTransactionList;

    // Setiap DailyTransaction, ada beberapa Transaction
    // Istilahnya child kalo di ExpandableListView
    HashMap<DailyTransaction, List<Transaction>> dailyTransactionListHashMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // rootView ini buat nampilin view fragment nya
        View rootView = inflater.inflate(R.layout.fragment_transaction_daily, container, false);
        // header ini buat elemen pertama dari ExpandableListView yang berupa summary nya
        View header = inflater.inflate(R.layout.fragment_transaction_header_month, null);

        // initiate ExpandableListViewnya
        dailyExpandableListView = rootView.findViewById(R.id.transaction_daily_expandablelistview);
        dailyExpandableListView.addHeaderView(header); // ini buat masukin header nya

        // Ini buat dummy data, data sebenarnya nanti diretrieve dari database
        setContent();

        // Bikin adapternya
        dailyExpandableAdapter = new DailyExpandableAdapter();
        // masukin adapter ke ExpandableListView
        dailyExpandableListView.setAdapter(dailyExpandableAdapter);

        return rootView;
    }

    private void setContent(){

        dailyTransactionList = new ArrayList<>();
        dailyTransactionListHashMap = new HashMap<>();

        dailyTransactionList.add(new DailyTransaction(16, "Fri", "Rp 5.000.000", "Rp 500.000"));
        dailyTransactionList.add(new DailyTransaction(17, "Sat", "Rp 5.000.000", "Rp 500.000"));
        dailyTransactionList.add(new DailyTransaction(18, "Sun", "Rp 5.000.000", "Rp 500.000"));

        List<Transaction> transaction1 = new ArrayList<>();
        List<Transaction> transaction2 = new ArrayList<>();
        List<Transaction> transaction3 = new ArrayList<>();
//        for (int i = 0; i < 1;i++){
//            transaction1.add(new Transaction("13/10/2020", "Bank account", "income", 5000000, "gajian"));
//            transaction2.add(new Transaction("14/10/2020", "Cash", "income", 50000, "nemu di jalan"));
//            transaction3.add(new Transaction("15/10/2020", "Bank account", "expense", 100000, "dipalak"));
//        }

        dailyTransactionListHashMap.put(dailyTransactionList.get(0), transaction1);
        dailyTransactionListHashMap.put(dailyTransactionList.get(1), transaction2);
        dailyTransactionListHashMap.put(dailyTransactionList.get(2), transaction3);

    }

    // adapter ini harus subclass dari BaseExpandableListAdapter
    class DailyExpandableAdapter extends BaseExpandableListAdapter{

        // constructor kosongan
        public DailyExpandableAdapter(){

        }

        // ada berapa group
        @Override
        public int getGroupCount() {
            return dailyTransactionList.size();
        }

        // ada berapa children di group yang ke i
        @Override
        public int getChildrenCount(int i) {
            return dailyTransactionListHashMap.get(dailyTransactionList.get(i)).size();
        }

        // minta grup ke i dong
        @Override
        public Object getGroup(int i) {
            return dailyTransactionList.get(i);
        }

        // minta child ke i1 dari grup ke i dong
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

        // gatau buat apa
        @Override
        public boolean hasStableIds() {
            return false;
        }

        // buat ngatur view dari DailyTransaction nya
        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            // ambil DailyTransaction object nya
            DailyTransaction dailyTransaction = (DailyTransaction) getGroup(i);

            // kalo view nya masih blm dibuat, dibuat dari awal
            // caranya pake LayoutInflater trs inflate gitu
            // itu ada R.layout.item_list_transaction_daily XML yang dicustom sendiri
            if (view == null){
                view = LayoutInflater.from(getContext()).inflate(R.layout.item_list_transaction_daily, viewGroup, false);
            }

            // Tinggal ngeset2 view nya aja
            TextView date = view.findViewById(R.id.item_list_transaction_daily_date_textview);
            date.setText(Integer.toString(dailyTransaction.getDate()));

            TextView day = view.findViewById(R.id.item_list_transaction_daily_day_textview);
            day.setText(dailyTransaction.getDay());

            TextView income = view.findViewById(R.id.item_list_transaction_daily_income_textview);
            income.setText(dailyTransaction.getIncome());

            TextView expense = view.findViewById(R.id.item_list_transaction_daily_expense_textview);
            expense.setText(dailyTransaction.getExpense());

            return view;
        }

        // buat ngatur view dari Transaction nya
        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            // ambil Transaction object nya
            Transaction transaction = (Transaction) getChild(i, i1);

            // penjelasan nya sama kyk di atas
            if (view == null){
                view = LayoutInflater.from(getContext()).inflate(R.layout.item_list_transaction, viewGroup, false);
            }

            TextView category = view.findViewById(R.id.item_list_transaction_category_textview);
            category.setText(Long.toString(transaction.getCategoryId()));

            TextView desc = view.findViewById(R.id.item_list_transaction_desc_textview);
            desc.setText(transaction.getDesc());

            TextView wallet = view.findViewById(R.id.item_list_transaction_wallet_textview);
            wallet.setText(Long.toString(transaction.getWalletId()));

            TextView amount = view.findViewById(R.id.item_list_transaction_amount_textview);
            amount.setText(Double.toString(transaction.getAmount()));

            return view;
        }

        // Transaction nya bisa dipencet
        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }

    }

}
