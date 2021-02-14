package duitku.project.se.wallet.add;

import android.app.AlertDialog;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import duitku.project.se.R;
import duitku.project.se.wallet.Wallet;
import duitku.project.se.wallet.WalletController;
import duitku.project.se.wallet.form.WalletForm;

public class AddWalletDialog extends AppCompatDialogFragment {

    private WalletForm walletForm;
    private View view;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        setUpUI();

        builder.setView(view);

        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary); //biar bg gelap
        return dialog;
    }

    private void setUpUI() {
        setUpViews();
        setUpForm();
        setUpButtons();
    }

    private void setUpViews() {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        view = inflater.inflate(R.layout.dialog_add,
                (ViewGroup) getActivity().findViewById(R.id.dialog_add_constraintlayout));

        setUpTitle();

        LinearLayout walletFormContainer = view.findViewById(R.id.dialog_add_form);
        walletFormContainer.addView(inflater.inflate(R.layout.form_wallet,
                (ViewGroup) getActivity().findViewById(R.id.form_wallet_constraintlayout)));
    }

    private void setUpTitle() {
        TextView titleTV = view.findViewById(R.id.dialog_add_title);
        titleTV.setText("Add Wallet");
    }

    private void setUpForm() {
        walletForm = new WalletForm(getActivity(), view, this);
        walletForm.setUpUI();
    }

    private void setUpButtons() {
        setUpAddBtn();
    }

    private void setUpAddBtn() {
        Button addButton = view.findViewById(R.id.wallet_save_btn);
        addButton.setText("Add");
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!walletForm.validateInput()) return;
                Uri uri = addWallet();
                if (uri == null) {
                    Toast.makeText(getContext(), "Error adding new wallet", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Wallet added", Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        });
    }

    private Uri addWallet() {
        String name = walletForm.getName();
        double amount = walletForm.getAmount();
        String desc = walletForm.getDesc();

        Wallet wallet = new Wallet(-1, name, amount, desc);
        return new WalletController(getActivity()).addWallet(wallet);
    }

}