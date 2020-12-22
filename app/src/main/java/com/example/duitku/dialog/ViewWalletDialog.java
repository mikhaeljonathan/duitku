package com.example.duitku.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.duitku.R;
import com.example.duitku.database.DuitkuContract.WalletEntry;
import com.example.duitku.flows.EditWalletActivity;

public class ViewWalletDialog extends AppCompatDialogFragment {

    private Cursor mCursor;
    private long mId;

    private TextView walletNameTextView;
    private TextView walletAmountTextView;
    private TextView walletDescTextView;
    private ImageButton editBtn;
    private ImageButton closeBtn;

    public ViewWalletDialog(Cursor cursor, long id){
        super();
        mCursor = cursor;
        mId = id;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_view_wallet, null);

        // initialize the views
        walletNameTextView = view.findViewById(R.id.view_wallet_name_textview);
        walletAmountTextView = view.findViewById(R.id.view_wallet_amount_textview);
        walletDescTextView = view.findViewById(R.id.view_wallet_desc_textview);
        editBtn = view.findViewById(R.id.view_wallet_edit_btn);
        closeBtn = view.findViewById(R.id.view_wallet_close_btn);

        // ambil index col nya
        int walletNameColumnIndex = mCursor.getColumnIndex(WalletEntry.COLUMN_NAME);
        int walletAmountColumnIndex = mCursor.getColumnIndex(WalletEntry.COLUMN_AMOUNT);
        int walletDescColumnIndex = mCursor.getColumnIndex(WalletEntry.COLUMN_DESC);

        // retrieve data nya
        String walletName = mCursor.getString(walletNameColumnIndex);
        double walletAmount = mCursor.getDouble(walletAmountColumnIndex);
        String walletDesc = mCursor.getString(walletDescColumnIndex);

        // set the views
        walletNameTextView.setText(walletName);
        walletAmountTextView.setText(Double.toString(walletAmount));
        walletDescTextView.setText(walletDesc);

        // set 2 button di atas
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                // bikin intent object
                Intent intent = new Intent(getActivity(), EditWalletActivity.class);
                // bikin uri buat 1 item doang di table wallet
                Uri currentWalletUri = ContentUris.withAppendedId(WalletEntry.CONTENT_URI, mId);
                intent.setData(currentWalletUri);
                // munculin activity baru
                startActivity(intent);
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss(); // tutup dialognya
            }
        });

        // set dialognya
        builder.setView(view);

        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary); //biar background dialogny hitam ngab
        return dialog;

    }
}
