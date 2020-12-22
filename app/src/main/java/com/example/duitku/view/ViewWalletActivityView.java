package com.example.duitku.view;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.duitku.R;
import com.example.duitku.controller.WalletController;
import com.example.duitku.flows.EditWalletActivity;
import com.example.duitku.model.Wallet;

public class ViewWalletActivityView implements UIView{

    private WalletController walletController;

    private long id;
    private Wallet wallet;
    private AppCompatActivity activity;

    public ViewWalletActivityView(long id, AppCompatActivity activity){
        this.id = id;
        this.activity = activity;
        walletController = new WalletController(activity);
    }

    @Override
    public void setUpUI() {
        activity.setContentView(R.layout.activity_view_wallet);
        wallet = walletController.getWalletById(id);
        setUpSummary();
        setUpButtons();
    }

    private void setUpSummary(){
        TextView nameTextView = activity.findViewById(R.id.view_wallet_name_textview);
        TextView amountTextView = activity.findViewById(R.id.view_wallet_amount_textview);
        TextView descTextView = activity.findViewById(R.id.view_wallet_desc_textview);

        nameTextView.setText(wallet.getName());
        amountTextView.setText(wallet.getAmount() + "");
        descTextView.setText(wallet.getDescription());
    }

    private void setUpButtons(){
        ImageButton backBtn = activity.findViewById(R.id.view_wallet_back_btn);
        ImageButton editBtn = activity.findViewById(R.id.view_wallet_edit_btn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editWalletIntent = new Intent(activity, EditWalletActivity.class);
                editWalletIntent.putExtra("ID", id);
                activity.startActivity(editWalletIntent);
            }
        });
    }

    @Override
    public View getView() {
        return null;
    }
}
