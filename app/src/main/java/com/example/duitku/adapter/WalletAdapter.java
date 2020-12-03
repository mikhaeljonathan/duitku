package com.example.duitku.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.duitku.R;
import com.example.duitku.database.DuitkuContract;

// adapter nya beda sama ExpandableListView soalnya ini berupa ListView biasa
// adapternya ini subclass dari CursorAdapter
public class WalletAdapter extends CursorAdapter {

    // construct nya pake List of object
    public WalletAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    // ini buat custom view nya

    // layout secara keseluruhan, kalo dibuat dari scratch
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.item_list_wallet, viewGroup, false);
    }

    // dalemnya layout diisi, selalu direcycle
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // view itu view secara keseluruhan
        TextView walletNameTextView = view.findViewById(R.id.item_list_wallet_name_textview);
        TextView walletAmountTextView = view.findViewById(R.id.item_list_wallet_amount_textview);

        // columnnya jadiin integer dlu
        int walletNameColumnIndex = cursor.getColumnIndex(DuitkuContract.WalletEntry.COLUMN_NAME);
        int walletAmountColumnIndex = cursor.getColumnIndex(DuitkuContract.WalletEntry.COLUMN_AMOUNT);

        // dapetin value nya
        String walletName = cursor.getString(walletNameColumnIndex);
        double walletAmount = cursor.getDouble(walletAmountColumnIndex);

        // tampilin di view
        walletNameTextView.setText(walletName);
        walletAmountTextView.setText(Double.toString(walletAmount));

    }

}
