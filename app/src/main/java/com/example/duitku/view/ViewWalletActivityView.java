package com.example.duitku.view;

import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.duitku.R;
import com.example.duitku.adapter.TransactionAdapter;
import com.example.duitku.controller.WalletController;
import com.example.duitku.flows.EditWalletActivity;
import com.example.duitku.model.Wallet;

public class ViewWalletActivityView implements UIView {

    private ListView listView;
    private TransactionAdapter adapter;

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
        if (wallet == null) {
            activity.finish();
            return;
        }
        setUpListView();
        setUpHeader();
        setUpButtons();
        setUpAdapter();
    }

    public TransactionAdapter getAdapter(){
        return adapter;
    }

    private void setUpListView(){
        listView = activity.findViewById(R.id.view_wallet_listview);
    }

    private void setUpHeader(){
        View header = activity.getLayoutInflater().inflate(R.layout.activity_summary_header, null);

        TextView nameTextView = header.findViewById(R.id.summary_header_title);
        TextView amountTextView = header.findViewById(R.id.summary_header_amount);
        TextView periodTextView = header.findViewById(R.id.summary_header_period);
        TextView descTextView = header.findViewById(R.id.summary_header_desc);

        nameTextView.setText(wallet.getName());
        amountTextView.setText(wallet.getAmount() + "");
        periodTextView.setText("December 2020");
        String desc = wallet.getDescription();
        if (desc.equals("")){
            descTextView.setVisibility(View.GONE);
        } else {
            descTextView.setText(wallet.getDescription());
        }

        listView.addHeaderView(header, null, false);
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

    private void setUpAdapter(){
        adapter = new TransactionAdapter(activity, id, null);
        listView.setAdapter(adapter);
    }

    @Override
    public View getView() {
        return null;
    }
}
