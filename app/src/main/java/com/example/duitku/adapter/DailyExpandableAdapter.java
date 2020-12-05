package com.example.duitku.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.duitku.R;
import com.example.duitku.model.DailyTransaction;
import com.example.duitku.model.Transaction;

import java.util.HashMap;
import java.util.List;

// adapter ini harus subclass dari BaseExpandableListAdapter
public class DailyExpandableAdapter extends BaseExpandableListAdapter {

    private List<DailyTransaction> mDailyTransactionList;
    private HashMap<DailyTransaction, List<Transaction>> mDailyTransactionListHashMap;
    private Context mContext;

    // constructor kosongan
    public DailyExpandableAdapter(List<DailyTransaction> dailyTransactionList,
                                  HashMap<DailyTransaction, List<Transaction>> dailyTransactionListHashMap,
                                  Context context){
        mDailyTransactionList = dailyTransactionList;
        mDailyTransactionListHashMap = dailyTransactionListHashMap;
        mContext = context;
    }

    // ada berapa group
    @Override
    public int getGroupCount() {
        return mDailyTransactionList.size();
    }

    // ada berapa children di group yang ke i
    @Override
    public int getChildrenCount(int i) {
        return mDailyTransactionListHashMap.get(mDailyTransactionList.get(i)).size();
    }

    // minta grup ke i dong
    @Override
    public Object getGroup(int i) {
        return mDailyTransactionList.get(i);
    }

    // minta child ke i1 dari grup ke i dong
    @Override
    public Object getChild(int i, int i1) {
        return mDailyTransactionListHashMap.get(mDailyTransactionList.get(i)).get(i1);
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_list_transaction_daily, viewGroup, false);
        }

        // Tinggal ngeset2 view nya aja
        TextView date = view.findViewById(R.id.item_list_transaction_daily_date_textview);
        date.setText(Integer.toString(dailyTransaction.getDate()));

        TextView day = view.findViewById(R.id.item_list_transaction_daily_day_textview);
        day.setText(dailyTransaction.getDay());

        TextView income = view.findViewById(R.id.item_list_transaction_daily_income_textview);
        income.setText(Double.toString(dailyTransaction.getIncome()));

        TextView expense = view.findViewById(R.id.item_list_transaction_daily_expense_textview);
        expense.setText(Double.toString(dailyTransaction.getExpense()));

        return view;
    }

    // buat ngatur view dari Transaction nya
    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        // ambil Transaction object nya
        Transaction transaction = (Transaction) getChild(i, i1);

        // penjelasan nya sama kyk di atas
        if (view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_list_transaction, viewGroup, false);
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