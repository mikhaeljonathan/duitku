package com.example.duitku.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.duitku.R;
import com.example.duitku.database.DuitkuContract.WalletEntry;

public class PickWalletAdapter extends CursorAdapter {

    public PickWalletAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.item_list_pick_wallet, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView walletTextView = view.findViewById(R.id.item_list_pick_wallet_textview);

        int walletNameColumnIndex = cursor.getColumnIndex(WalletEntry.COLUMN_NAME);
        String walletName = cursor.getString(walletNameColumnIndex);

        walletTextView.setText(walletName);

    }
}
