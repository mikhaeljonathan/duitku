package com.example.duitku.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.duitku.R;
import com.example.duitku.database.DuitkuContract.WalletEntry;
import com.example.duitku.database.DuitkuContract.CategoryEntry;
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
        // ambil header object nya
        DailyTransaction dailyTransaction = (DailyTransaction) getGroup(i);

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_list_transaction_daily, viewGroup, false);
        }


        // Tinggal ngeset2 view nya aja
        TextView dateTextView = view.findViewById(R.id.item_list_transaction_daily_date_textview);
        dateTextView.setText(Integer.toString(dailyTransaction.getDate()));

        TextView dayTextView = view.findViewById(R.id.item_list_transaction_daily_day_textview);
        dayTextView.setText(dailyTransaction.getDay());

        TextView incomeTextView = view.findViewById(R.id.item_list_transaction_daily_income_textview);
        incomeTextView.setText(Double.toString(dailyTransaction.getIncome()));

        TextView expenseTextView = view.findViewById(R.id.item_list_transaction_daily_expense_textview);
        expenseTextView.setText(Double.toString(dailyTransaction.getExpense()));

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

        // untuk icon jenis categorynya
        ImageView categoryImageView = view.findViewById(R.id.item_list_transaction_categorytype_icon);
        Cursor categoryCursor = mContext.getContentResolver().query(ContentUris.withAppendedId(CategoryEntry.CONTENT_URI, transaction.getCategoryId()), new String[]{CategoryEntry.COLUMN_ID, CategoryEntry.COLUMN_NAME, CategoryEntry.COLUMN_TYPE}, null,null, null);
        String category = "";
        String type = "TRANS";
        if (categoryCursor.moveToFirst()){
            type = categoryCursor.getString(categoryCursor.getColumnIndex(CategoryEntry.COLUMN_TYPE));
            category = categoryCursor.getString(categoryCursor.getColumnIndex(CategoryEntry.COLUMN_NAME));
        }
        Log.v("WOW", transaction.getCategoryId() + " " + type + " " + transaction.getDesc());
        if (type.equals(CategoryEntry.TYPE_INCOME)){
            Log.v("WOW", "OK");
            categoryImageView.setImageResource(R.drawable.icon_income);
        } else if (type.equals(CategoryEntry.TYPE_EXPENSE)){
            categoryImageView.setImageResource(R.drawable.icon_expense);
        } else {
            categoryImageView.setImageResource(R.drawable.icon_transfer);
        }

        // kalau income/expense, dia cuma nampilin category, kalau transfer dia nampilin wallet sumber sm tujuanny
        TextView headerTextView = view.findViewById(R.id.item_list_transaction_header_textview);
        ImageView transferImageView = view.findViewById(R.id.item_list_transaction_transfer_imageview);
        TextView walletDestTextView = view.findViewById(R.id.item_list_transaction_walletdest_textview);

        transferImageView.setVisibility(View.GONE);
        walletDestTextView.setVisibility(View.GONE);

        if (category.isEmpty()){

            transferImageView.setVisibility(View.VISIBLE);
            walletDestTextView.setVisibility(View.VISIBLE);

            Cursor walletCursor = mContext.getContentResolver().query(ContentUris.withAppendedId(WalletEntry.CONTENT_URI, transaction.getWalletId()), new String[]{WalletEntry.COLUMN_ID, WalletEntry.COLUMN_NAME}, null,null, null);
            if (walletCursor.moveToFirst()){
                headerTextView.setText(walletCursor.getString(walletCursor.getColumnIndex(WalletEntry.COLUMN_NAME)));
            }

            walletCursor = mContext.getContentResolver().query(ContentUris.withAppendedId(WalletEntry.CONTENT_URI, transaction.getWalletDestId()), new String[]{WalletEntry.COLUMN_ID, WalletEntry.COLUMN_NAME}, null,null, null);
            if (walletCursor.moveToFirst()){
                walletDestTextView.setText(walletCursor.getString(walletCursor.getColumnIndex(WalletEntry.COLUMN_NAME)));
            }
        } else {
            headerTextView.setText(category);
        }

        // description spt biasa
        TextView descTextView = view.findViewById(R.id.item_list_transaction_desc_textview);
        descTextView.setText(transaction.getDesc());

        // amount jg spt biasa
        TextView amountTextView = view.findViewById(R.id.item_list_transaction_amount_textview);
        amountTextView.setText(Double.toString(transaction.getAmount()));

        return view;
    }

    // Transaction nya bisa dipencet
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

}