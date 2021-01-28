package com.example.duitku.wallet.edit;

import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.duitku.R;
import com.example.duitku.transaction.TransactionController;
import com.example.duitku.interfaces.UIView;
import com.example.duitku.wallet.Wallet;
import com.example.duitku.wallet.WalletController;
import com.example.duitku.wallet.form.WalletForm;

public class EditWalletActivityView implements UIView {

    private WalletForm walletForm;
    private Button deleteBtn;

    private final AppCompatActivity activity;

    private final WalletController walletController;

    private final Wallet wallet;

    public EditWalletActivityView(long id, AppCompatActivity activity) {
        this.activity = activity;

        walletController = new WalletController(activity);
        this.wallet = walletController.getWalletById(id);
    }

    @Override
    public void setUpUI() {
        setUpViews();
        setUpForm();
        setUpButtons();
    }

    private void setUpViews() {
        activity.setContentView(R.layout.activity_edit);

        TextView titleTV = activity.findViewById(R.id.activity_edit_title);
        titleTV.setText("Edit Wallet");

        LinearLayout walletFormContainer = activity.findViewById(R.id.activity_edit_form);
        walletFormContainer.addView(activity.getLayoutInflater().inflate(R.layout.form_wallet,
                (ViewGroup) activity.findViewById(R.id.form_wallet_constraintlayout)));

        deleteBtn = activity.findViewById(R.id.activity_edit_delete);
        deleteBtn.setText("Delete Wallet");
    }

    private void setUpForm() {
        walletForm = new WalletForm(activity, null, activity);
        walletForm.setUpUI();
        walletForm.setName(wallet.getWallet_name());
        walletForm.setAmount(wallet.getWallet_amount());
        walletForm.setDesc(wallet.getWallet_desc());
    }

    private void setUpButtons() {
        setUpSaveButton();
        setUpDeleteButton();
        setUpBackButton();
    }

    private void setUpSaveButton() {
        Button saveBtn = activity.findViewById(R.id.wallet_save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!walletForm.validateInput()) return;
                int rowsUpdated = updateWallet();
                if (rowsUpdated == 0) {
                    Toast.makeText(activity, "Error editing wallet", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "Wallet edited", Toast.LENGTH_SHORT).show();
                }
                activity.finish();
            }
        });
    }

    private int updateWallet() {
        double amountBefore = wallet.getWallet_amount();

        String name = walletForm.getName();
        double amount = walletForm.getAmount();
        String desc = walletForm.getDesc();

        wallet.setWallet_name(name);
        wallet.setWallet_amount(amount);
        wallet.setWallet_desc(desc);

        if (amountBefore != wallet.getWallet_amount()) {
            new TransactionController(activity).addTransactionFromUpdatedWallet(amountBefore, wallet);
        }

        return walletController.updateWallet(wallet);
    }

    private void setUpDeleteButton() {
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteConfirmationDialog();
            }
        });
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity, R.style.AlertDialogCustom);
        alertDialogBuilder.setTitle("Delete Confirmation");
        alertDialogBuilder.setMessage("Are you sure to delete this wallet?\nAll transactions with this wallet are deleted too.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteWallet();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary); //biar bg gelap
        alertDialog.show();
    }

    private void deleteWallet() {
        int rowsDeleted = walletController.deleteWallet(wallet);
        if (rowsDeleted == 0) {
            Toast.makeText(activity, "Error deleting wallet", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "Wallet is deleted", Toast.LENGTH_SHORT).show();
        }
        activity.finish();
    }

    private void setUpBackButton() {
        ImageButton backBtn = activity.findViewById(R.id.activity_edit_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
    }

    @Override
    public View getView() {
        return activity.findViewById(R.id.activity_edit_constraintlayout);
    }

}
