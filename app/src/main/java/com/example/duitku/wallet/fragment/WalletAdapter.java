package com.example.duitku.wallet.fragment;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.duitku.R;
import com.example.duitku.wallet.Wallet;
import com.example.duitku.wallet.WalletController;

public class WalletAdapter extends CursorAdapter {

    private final WalletController walletController;

    public WalletAdapter(Context context, Cursor c) {
        super(context, c, 0);
        walletController = new WalletController(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.item_list_wallet, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Wallet wallet = walletController.convertCursorToWallet(cursor);

        String name = wallet.getName();
        double amount = wallet.getAmount();

        TextView walletNameTextView = view.findViewById(R.id.item_list_wallet_name_textview);
        TextView walletAmountTextView = view.findViewById(R.id.item_list_wallet_amount_textview);

        walletNameTextView.setText(name);
        walletAmountTextView.setText(Double.toString(amount));
    }

}
