package com.example.duitku.wallet;

import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.duitku.R;
import com.example.duitku.date.MonthYearPickerDialog;
import com.example.duitku.main.Utility;
import com.example.duitku.transaction.TransactionAdapter;
import com.example.duitku.interfaces.UIView;

public class ViewWalletActivityView implements UIView {

    private ListView listView;
    private TransactionAdapter adapter;

    private Button periodButton;

    private WalletController walletController;

    private long id;
    private Wallet wallet;
    private ViewWalletActivity activity;
    private View header;

    public ViewWalletActivityView(long id, ViewWalletActivity activity){
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
        setUpPeriodButton();
    }

    public TransactionAdapter getAdapter(){
        return adapter;
    }

    public void updatePeriodButton(final int month, final int year){
        periodButton.setText(Utility.monthsName[month] + " " + year);
        periodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatDialogFragment monthYearPickerDialog = new MonthYearPickerDialog(activity, month, year);
                monthYearPickerDialog.show(activity.getSupportFragmentManager(), "Month Year Picker Dialog");
            }
        });
    }

    private void setUpListView(){
        listView = activity.findViewById(R.id.view_wallet_listview);
    }

    private void setUpHeader(){
        header = activity.getLayoutInflater().inflate(R.layout.activity_summary_header, null);

        TextView nameTextView = header.findViewById(R.id.summary_header_title);
        TextView amountTextView = header.findViewById(R.id.summary_header_amount);
        TextView descTextView = header.findViewById(R.id.summary_header_desc);
        TextView transactionTextView = header.findViewById(R.id.summary_header_transaction_textview);

        nameTextView.setText(wallet.getName());
        amountTextView.setText(wallet.getAmount() + "");
        String desc = wallet.getDescription();
        if (desc.equals("")){
            descTextView.setVisibility(View.GONE);
        } else {
            descTextView.setText(wallet.getDescription());
        }
        transactionTextView.setPaintFlags(transactionTextView.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG); //underline

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

    private void setUpPeriodButton(){
        periodButton = header.findViewById(R.id.summary_header_period_btn);
    }

    @Override
    public View getView() {
        return null;
    }
}
