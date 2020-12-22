package com.example.duitku.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.duitku.R;
import com.example.duitku.controller.CategoryController;
import com.example.duitku.controller.WalletController;
import com.example.duitku.database.DuitkuContract.WalletEntry;
import com.example.duitku.database.DuitkuContract.CategoryEntry;
import com.example.duitku.model.Category;
import com.example.duitku.model.DailyTransaction;
import com.example.duitku.model.Transaction;
import com.example.duitku.model.Wallet;

import java.util.HashMap;
import java.util.List;

public class DailyExpandableAdapter extends BaseExpandableListAdapter {

    private List<DailyTransaction> dailyTransactionList;
    private HashMap<DailyTransaction, List<Transaction>> dailyTransactionListHashMap;
    private Context context;

    public DailyExpandableAdapter(List<DailyTransaction> dailyTransactionList,
                                  HashMap<DailyTransaction, List<Transaction>> dailyTransactionListHashMap,
                                  Context context){
        this.dailyTransactionList = dailyTransactionList;
        this.dailyTransactionListHashMap = dailyTransactionListHashMap;
        this.context = context;
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
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_list_transaction_daily, viewGroup, false);
        }

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

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {

        Transaction transaction = (Transaction) getChild(i, i1);

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_list_transaction, viewGroup, false);
        }

        // get the category
        CategoryController categoryController = new CategoryController(context);
        Category category = categoryController.getCategoryById(transaction.getCategoryId());

        setUpIcon(view, category);
        setupHeader(view, category, transaction);

        TextView descTextView = view.findViewById(R.id.item_list_transaction_desc_textview);
        descTextView.setText(transaction.getDesc());

        TextView amountTextView = view.findViewById(R.id.item_list_transaction_amount_textview);
        amountTextView.setText(Double.toString(transaction.getAmount()));

        return view;
    }

    private void setupHeader(View view, Category category, Transaction transaction){

        TextView headerTextView = view.findViewById(R.id.item_list_transaction_header_textview);
        ImageView transferImageView = view.findViewById(R.id.item_list_transaction_transfer_imageview);
        TextView walletDestTextView = view.findViewById(R.id.item_list_transaction_walletdest_textview);

        // the default is gone
        transferImageView.setVisibility(View.GONE);
        walletDestTextView.setVisibility(View.GONE);

        if (category == null){

            transferImageView.setVisibility(View.VISIBLE);
            walletDestTextView.setVisibility(View.VISIBLE);

            WalletController walletController = new WalletController(context);
            long walletId = transaction.getWalletId();
            long walletDestId = transaction.getWalletDestId();

            Wallet walletSource = walletController.getWalletById(walletId);
            headerTextView.setText(walletSource.getName());

            Wallet walletDest = walletController.getWalletById(walletDestId);
            walletDestTextView.setText(walletDest.getName());

        } else {
            headerTextView.setText(category.getName());
        }

    }

    private void setUpIcon(View view, Category category){
        ImageView categoryImageView = view.findViewById(R.id.item_list_transaction_categorytype_icon);

        if (category == null){
            categoryImageView.setImageResource(R.drawable.icon_transfer);
            return;
        }

        String type = category.getType();
        if (type.equals(CategoryEntry.TYPE_INCOME)){
            categoryImageView.setImageResource(R.drawable.icon_income);
        } else {
            categoryImageView.setImageResource(R.drawable.icon_expense);
        }
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

}