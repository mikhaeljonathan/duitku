package com.example.duitku.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.duitku.R;
import com.example.duitku.controller.CategoryController;
import com.example.duitku.controller.TransactionController;
import com.example.duitku.database.DuitkuContract;
import com.example.duitku.model.Category;
import com.example.duitku.model.Transaction;

public class TransactionAdapter extends CursorAdapter {

    private TransactionController transactionController;

    public TransactionAdapter(Context context, Cursor c) {
        super(context, c, 0);
        transactionController = new TransactionController(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.item_list_transaction, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Transaction transaction = transactionController.convertCursorToTransaction(cursor);

        CategoryController categoryController = new CategoryController(context);
        Category category = categoryController.getCategoryById(transaction.getCategoryId());

        setUpIcon(view, category);
        setUpHeader(view, category);

        TextView descTextView = view.findViewById(R.id.item_list_transaction_desc_textview);
        descTextView.setText(transaction.getDesc());

        TextView amountTextView = view.findViewById(R.id.item_list_transaction_amount_textview);
        amountTextView.setText(Double.toString(transaction.getAmount()));
    }

    private void setUpIcon(View view, Category category){
        ImageView categoryImageView = view.findViewById(R.id.item_list_transaction_categorytype_icon);

        if (category == null){
            categoryImageView.setImageResource(R.drawable.icon_transfer);
            return;
        }

        String type = category.getType();
        if (type.equals(DuitkuContract.CategoryEntry.TYPE_INCOME)){
            categoryImageView.setImageResource(R.drawable.icon_income);
        } else {
            categoryImageView.setImageResource(R.drawable.icon_expense);
        }
    }

    private void setUpHeader(View view, Category category){
        TextView categoryTextView = view.findViewById(R.id.item_list_transaction_header_textview);
        ImageView transferIcon = view.findViewById(R.id.item_list_transaction_transfer_imageview);
        TextView walletDestTextView = view.findViewById(R.id.item_list_transaction_walletdest_textview);

        categoryTextView.setText(category.getName());
        transferIcon.setVisibility(View.GONE);
        walletDestTextView.setVisibility(View.GONE);
    }

}
