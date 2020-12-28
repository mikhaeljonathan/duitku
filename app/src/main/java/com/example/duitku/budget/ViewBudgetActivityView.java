package com.example.duitku.budget;

import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.duitku.R;
import com.example.duitku.category.CategoryController;
import com.example.duitku.interfaces.UIView;
import com.example.duitku.transaction.Transaction;
import com.example.duitku.transaction.TransactionAdapter;
import com.example.duitku.transaction.TransactionController;
import com.example.duitku.transaction.ViewTransactionDialog;
import com.example.duitku.wallet.EditWalletActivity;

import java.util.List;

public class ViewBudgetActivityView implements UIView {

    private List<Transaction> transactionList;

    private ListView listView;
    private TransactionAdapter adapter;

    private BudgetController budgetController;

    private long id;
    private Budget budget;
    private AppCompatActivity activity;
    private View header;

    public ViewBudgetActivityView(long id, AppCompatActivity activity){
        this.id = id;
        this.activity = activity;
        budgetController = new BudgetController(activity);
    }

    public void setTransactionList(List<Transaction> transactionList){
        this.transactionList = transactionList;
    }

    @Override
    public void setUpUI() {
        activity.setContentView(R.layout.activity_view);
        TextView textView = activity.findViewById(R.id.view_title_textview);
        textView.setText("View Budget");

        budget = budgetController.getBudgetById(id);
        if (budget == null) { // kalau di delete lgsg finish activity
            activity.finish();
            return;
        }

        setUpListView();
        setUpHeader();
        setUpButtons();
        setUpAdapter();
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
        TextView periodTextView = header.findViewById(R.id.view_header_subsubtitle);

        ProgressBar progressBar = header.findViewById(R.id.view_header_progressbar);
        TextView usedTextView = header.findViewById(R.id.view_header_used_textview);
        TextView maxTextView = header.findViewById(R.id.view_header_max_textview);

        TextView transactionTextView = header.findViewById(R.id.view_header_transaction_textview);

        nameTextView.setText(new CategoryController(activity).getCategoryById(id).getName());
        amountTextView.setText(Double.toString(budget.getAmount()));
        String period = "dari sini sampe sini";
        periodTextView.setText(period);

        progressBar.setProgress((int)budget.getUsed());
        progressBar.setMax((int)budget.getAmount());
        usedTextView.setText(Double.toString(budget.getUsed()));
        maxTextView.setText(Double.toString(budget.getAmount()));

        transactionTextView.setPaintFlags(transactionTextView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG); //underline

        hideView();

        listView.addHeaderView(header, null, false);
    }

    private void hideView(){
        Button periodBtn = header.findViewById(R.id.view_header_period_btn);
        periodBtn.setVisibility(View.GONE);
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
                Intent editBudgetIntent = new Intent(activity, EditBudgetActivity.class);
                editBudgetIntent.putExtra("ID", id);
                activity.startActivity(editBudgetIntent);
            }
        });
    }

    private void setUpAdapter(){
        adapter = new TransactionAdapter(activity, transactionList, null);
        listView.setAdapter(adapter);
    }

    private void viewTransaction(long id){
        Transaction transaction = new TransactionController(activity).getTransactionById(id);
        ViewTransactionDialog viewTransactionDialog = new ViewTransactionDialog(transaction);
        viewTransactionDialog.show(activity.getSupportFragmentManager(), "View Transaction Dialog");
    }

    @Override
    public View getView() {
        return null;
    }
}
