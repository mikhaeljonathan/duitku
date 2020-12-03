package com.example.duitku.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.duitku.R;
import com.example.duitku.controller.WalletController;
import com.example.duitku.model.Wallet;

// ini class buat bikin dialog pas mau add wallet
public class AddWalletDialog extends AppCompatDialogFragment {

    private EditText walletNameEditText;
    private EditText walletAmountEditText;
    private EditText walletDescriptionEditText;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_wallet, null);

        // initialize view nya
        walletNameEditText = view.findViewById(R.id.add_wallet_walletname_edittext);
        walletAmountEditText = view.findViewById(R.id.add_wallet_amount_edittext);
        walletDescriptionEditText = view.findViewById(R.id.add_wallet_desc_edittext);

        // bikin dialognya
        builder.setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // ga ngapa2in
                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // ambil value nya
                        String walletName = walletNameEditText.getText().toString().trim();
                        double walletAmount = Double.parseDouble(walletAmountEditText.getText().toString().trim());
                        String walletDesc = walletDescriptionEditText.getText().toString().trim();

                        // panggil controller untuk ditambahin ke database
                        Wallet walletAdded = new Wallet(walletName, walletAmount, walletDesc);
                        Uri uri = new WalletController(getContext()).addWallet(walletAdded);

                        // cek apakah insert nya error
                        if (uri == null){
                            Toast.makeText(getContext(), "Error adding new wallet", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Wallet added", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // atur title nya
        TextView tv = new TextView(getContext());
        tv.setTextColor(Color.WHITE);
        tv.setText("Add Wallet");
        tv.setPadding(50, 50, 50, 50);
        tv.setTextSize(20F);
        builder.setCustomTitle(tv);

        // return dialog nya
        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary);
        return dialog;

    }
}