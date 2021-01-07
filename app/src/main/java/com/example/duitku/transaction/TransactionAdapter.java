package com.example.duitku.transaction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.duitku.R;
import com.example.duitku.category.Category;
import com.example.duitku.category.CategoryController;
import com.example.duitku.database.DuitkuContract;
import com.example.duitku.wallet.Wallet;
import com.example.duitku.wallet.WalletController;

import java.util.List;

public class TransactionAdapter extends ArrayAdapter<Transaction> {

    private final List<Transaction> transactions;

    private Transaction transaction;
    private Category category;
    private Wallet wallet;

    private final Long walletId;

    private final WalletController walletController;
    private final CategoryController categoryController;

    public TransactionAdapter(Context context, List<Transaction> transactions, Long walletId) {
        super(context, 0, transactions);
        this.transactions = transactions;
        this.walletId = walletId;
        walletController = new WalletController(context);
        categoryController = new CategoryController(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_list_transaction, parent, false);
        }

        transaction = getItem(position);

        category = categoryController.getCategoryById(transaction.getCategoryId());
        wallet = walletController.getWalletById(transaction.getWalletId());

        setUpIcon(view);
        setUpHeader(view);

        TextView descTextView = view.findViewById(R.id.item_list_transaction_desc_textview);
        descTextView.setText(transaction.getDesc());

        TextView amountTextView = view.findViewById(R.id.item_list_transaction_amount_textview);
        amountTextView.setText(Double.toString(transaction.getAmount()));

        return view;
    }

    private void setUpIcon(View view){
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

    private void setUpHeader(View view){
        TextView headerTextView = view.findViewById(R.id.item_list_transaction_header_textview);
        ImageView transferIcon = view.findViewById(R.id.item_list_transaction_transfer_imageview);
        TextView walletDestTextView = view.findViewById(R.id.item_list_transaction_walletdest_textview);

        transferIcon.setVisibility(View.GONE);
        walletDestTextView.setVisibility(View.GONE);

        if (walletId != null){ // View Wallet
            if (category == null) { // transfer
                if (wallet.getId() == walletId){ // the wallet source is same with current wallet
                    Wallet wallet = walletController.getWalletById(transaction.getWalletDestId());
                    headerTextView.setText("Transferred to Wallet " + wallet.getName());
                    return;
                }
                // the wallet dest is same with current wallet
                Wallet wallet = walletController.getWalletById(transaction.getWalletId());
                headerTextView.setText("Transferred from Wallet " + wallet.getName());
                return;
            }
            // expense or transfer
            headerTextView.setText(category.getName());
            return;
        }
        // View category transaction
        headerTextView.setText(wallet.getName());
    }

    public Transaction getTransaction(int position) {
        return transactions.get(position);
    }

}
