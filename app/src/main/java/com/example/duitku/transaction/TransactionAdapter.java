package com.example.duitku.transaction;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.duitku.R;
import com.example.duitku.category.CategoryController;
import com.example.duitku.transaction.TransactionController;
import com.example.duitku.wallet.WalletController;
import com.example.duitku.database.DuitkuContract;
import com.example.duitku.category.Category;
import com.example.duitku.transaction.Transaction;
import com.example.duitku.wallet.Wallet;

public class TransactionAdapter extends CursorAdapter {

    private TransactionController transactionController;
    private Transaction transaction;
    private Context context;
    private long walletId;

    public TransactionAdapter(Context context, long walletId, Cursor c) {
        super(context, c, 0);
        this.context = context;
        this.walletId = walletId;
        transactionController = new TransactionController(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.item_list_transaction, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        transaction = transactionController.convertCursorToTransaction(cursor);

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
        TextView headerTextView = view.findViewById(R.id.item_list_transaction_header_textview);
        ImageView transferIcon = view.findViewById(R.id.item_list_transaction_transfer_imageview);
        TextView walletDestTextView = view.findViewById(R.id.item_list_transaction_walletdest_textview);

        if (category == null) {
            WalletController walletController = new WalletController(context);
            if (transaction.getWalletId() == walletId){
                Wallet wallet = walletController.getWalletById(transaction.getWalletDestId());
                headerTextView.setText("Transferred to Wallet " + wallet.getName());
            } else {
                Wallet wallet = walletController.getWalletById(transaction.getWalletId());
                headerTextView.setText("Transferred from Wallet " + wallet.getName());
            }
        } else {
            headerTextView.setText(category.getName());
        }
        transferIcon.setVisibility(View.GONE);
        walletDestTextView.setVisibility(View.GONE);
    }

}
