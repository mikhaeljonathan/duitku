package com.example.duitku.transaction.category;

import android.graphics.Paint;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.duitku.R;
import com.example.duitku.category.Category;
import com.example.duitku.category.CategoryController;
import com.example.duitku.interfaces.UIView;
import com.example.duitku.transaction.Transaction;
import com.example.duitku.transaction.TransactionAdapter;
import com.example.duitku.transaction.TransactionController;
import com.example.duitku.transaction.view.ViewTransactionDialog;

public class ViewCategoryTransactionActivityView implements UIView {

    private ListView listView;
    private TransactionAdapter adapter;

    private View header;

    private CategoryTransaction categoryTransaction;
    private Category category;

    private AppCompatActivity activity;

    public ViewCategoryTransactionActivityView(CategoryTransaction categoryTransaction, AppCompatActivity activity){
        this.categoryTransaction = categoryTransaction;
        this.activity = activity;
        this.category = new CategoryController(activity).getCategoryById(categoryTransaction.getCategoryId());
    }

    @Override
    public void setUpUI() {
        activity.setContentView(R.layout.activity_view);
        TextView textView = activity.findViewById(R.id.view_title_textview);
        textView.setText("View Category Transaction");
        ImageView editBtn = activity.findViewById(R.id.view_edit_btn);
        editBtn.setVisibility(View.GONE);

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

    private void viewTransaction(long id){
        ViewTransactionDialog viewTransactionDialog = new ViewTransactionDialog(id);
        viewTransactionDialog.show(activity.getSupportFragmentManager(), "View Transaction Dialog");
    }

    private void setUpHeader(){
        header = activity.getLayoutInflater().inflate(R.layout.activity_view_header, null);

        TextView nameTextView = header.findViewById(R.id.view_header_title);
        TextView amountTextView = header.findViewById(R.id.view_header_subtitle);
        TextView periodTextView = header.findViewById(R.id.view_header_subsubtitle);
        TextView transactionTextView = header.findViewById(R.id.view_header_transaction_textview);

        nameTextView.setText(category.getName());
        amountTextView.setText(Double.toString(categoryTransaction.getAmount()));
        periodTextView.setText("dari sini sampe sini");
        transactionTextView.setPaintFlags(transactionTextView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG); //underline

        hideView();

        listView.addHeaderView(header, null, false);
    }

    private void hideView(){
        ProgressBar progressBar = header.findViewById(R.id.view_header_progressbar);
        TextView usedTextView = header.findViewById(R.id.view_header_used_textview);
        TextView amountTextView = header.findViewById(R.id.view_header_max_textview);
        Button periodBtn = header.findViewById(R.id.view_header_period_btn);

        progressBar.setVisibility(View.GONE);
        usedTextView.setVisibility(View.GONE);
        amountTextView.setVisibility(View.GONE);
        periodBtn.setVisibility(View.GONE);
    }

    private void setUpButtons(){
        ImageButton backBtn = activity.findViewById(R.id.view_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
    }

    private void setUpAdapter(){
        adapter = new TransactionAdapter(activity, categoryTransaction.getTransactions(), null);
        listView.setAdapter(adapter);
    }

    @Override
    public View getView() {
        return null;
    }
}
