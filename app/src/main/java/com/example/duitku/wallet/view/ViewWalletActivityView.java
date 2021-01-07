package com.example.duitku.wallet.view;

import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.duitku.R;
import com.example.duitku.main.Utility;
import com.example.duitku.transaction.Transaction;
import com.example.duitku.transaction.TransactionAdapter;
import com.example.duitku.interfaces.UIView;
import com.example.duitku.transaction.view.ViewTransactionDialog;
import com.example.duitku.wallet.Wallet;
import com.example.duitku.wallet.WalletController;
import com.example.duitku.wallet.edit.EditWalletActivity;

import java.util.List;

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
        activity.setContentView(R.layout.activity_view);
        TextView textView = activity.findViewById(R.id.view_title_textview);
        textView.setText("View Wallet");

        wallet = walletController.getWalletById(id);
        if (wallet == null) { // kalau di delete lgsg finish activity
            activity.finish();
            return;
        }

        setUpListView();
        setUpHeader();
        setUpButtons();
        setUpPeriodButton();
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
        listView = activity.findViewById(R.id.view_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                Transaction transaction = adapter.getTransaction(i - 1);
                viewTransaction(transaction.getId());
            }
        });
    }

    private void setUpHeader(){
        header = activity.getLayoutInflater().inflate(R.layout.activity_view_header, null);

        TextView nameTextView = header.findViewById(R.id.view_header_title);
        TextView amountTextView = header.findViewById(R.id.view_header_subtitle);
        TextView descTextView = header.findViewById(R.id.view_header_subsubtitle);
        TextView transactionTextView = header.findViewById(R.id.view_header_transaction_textview);

        nameTextView.setText(wallet.getName());
        amountTextView.setText(wallet.getAmount() + "");
        String desc = wallet.getDescription();
        if (desc.equals("")){
            descTextView.setVisibility(View.GONE);
        } else {
            descTextView.setText(wallet.getDescription());
        }
        transactionTextView.setPaintFlags(transactionTextView.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG); //underline

        hideView();

        listView.addHeaderView(header, null, false);
    }

    private void hideView(){
        ProgressBar progressBar = header.findViewById(R.id.view_header_progressbar);
        TextView usedTextView = header.findViewById(R.id.view_header_used_textview);
        TextView amountTextView = header.findViewById(R.id.view_header_max_textview);

        progressBar.setVisibility(View.GONE);
        usedTextView.setVisibility(View.GONE);
        amountTextView.setVisibility(View.GONE);
    }

    private void setUpButtons(){
        ImageButton backBtn = activity.findViewById(R.id.view_back_btn);
        ImageButton editBtn = activity.findViewById(R.id.view_edit_btn);

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

    public void setUpAdapter(List<Transaction> transactions){
        adapter = new TransactionAdapter(activity, transactions, id);
        listView.setAdapter(adapter);
    }

    private void setUpPeriodButton(){
        periodButton = header.findViewById(R.id.view_header_period_btn);
    }

    private void viewTransaction(long id){
        ViewTransactionDialog viewTransactionDialog = new ViewTransactionDialog(id);
        viewTransactionDialog.show(activity.getSupportFragmentManager(), "View Transaction Dialog");
    }

    @Override
    public View getView() {
        return null;
    }
}
