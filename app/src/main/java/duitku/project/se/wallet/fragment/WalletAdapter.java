package duitku.project.se.wallet.fragment;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import duitku.project.se.R;
import duitku.project.se.wallet.Wallet;
import duitku.project.se.wallet.WalletController;

import java.text.DecimalFormat;

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

        String name = wallet.getWallet_name();
        double amount = wallet.getWallet_amount();

        ConstraintLayout cl = view.findViewById(R.id.item_list_wallet_constraintlayout);
        TextView walletNameTextView = view.findViewById(R.id.item_list_wallet_name_textview);
        TextView walletAmountTextView = view.findViewById(R.id.item_list_wallet_amount_textview);

        walletNameTextView.setText(name);
        walletAmountTextView.setText(new DecimalFormat("###,###").format(amount));

        cl.setBackgroundResource(R.drawable.custom_shape);
    }

}
