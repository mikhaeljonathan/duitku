package duitku.project.se.transaction.category;

import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import duitku.project.se.R;
import duitku.project.se.category.Category;
import duitku.project.se.category.CategoryController;
import duitku.project.se.interfaces.UIView;
import duitku.project.se.transaction.Transaction;
import duitku.project.se.transaction.TransactionAdapter;
import duitku.project.se.transaction.TransactionController;
import duitku.project.se.transaction.view.ViewTransactionDialog;

import java.text.DecimalFormat;

public class ViewCategoryTransactionActivityView implements UIView {

    private ListView listView;
    private TransactionAdapter adapter;

    private View header;

    private CategoryTransaction categoryTransaction;
    private final Category category;

    private final ViewCategoryTransactionActivity activity;

    public ViewCategoryTransactionActivityView(CategoryTransaction categoryTransaction, ViewCategoryTransactionActivity activity) {
        this.categoryTransaction = categoryTransaction;
        this.activity = activity;
        this.category = new CategoryController(activity).getCategoryById(categoryTransaction.getCategoryId());
    }

    @Override
    public void setUpUI() {
        this.categoryTransaction = new TransactionController(activity).recalculateCategoryTransaction(categoryTransaction);
        setUpViews();
        setUpComponents();
        setUpButtons();
    }

    private void setUpViews() {
        activity.setContentView(R.layout.activity_view);

        TextView titleTV = activity.findViewById(R.id.view_title_textview);
        titleTV.setText("View Category Transaction");

        ImageView editBtn = activity.findViewById(R.id.view_edit_btn);
        editBtn.setVisibility(View.GONE);
    }

    private void setUpComponents() {
        setUpHeader();
        setUpListView();
    }

    private void setUpHeader() {
        header = activity.getLayoutInflater().inflate(R.layout.activity_view_header,
                (ViewGroup) activity.findViewById(R.id.activity_view_constraintlayout));

        setUpName();
        setUpAmount();
        setUpPeriod();
        setUpTransactionTextView();

        hideView();
    }

    private void setUpName() {
        TextView nameTextView = header.findViewById(R.id.view_header_title);
        nameTextView.setText(category.getCategory_name());
    }

    private void setUpAmount() {
        TextView amountTextView = header.findViewById(R.id.view_header_subtitle);
        amountTextView.setText(new DecimalFormat("###,###").format(categoryTransaction.getAmount()));
    }

    private void setUpPeriod() {
        TextView periodTextView = header.findViewById(R.id.view_header_subsubtitle);
        periodTextView.setVisibility(View.GONE);
    }

    private void setUpTransactionTextView() {
        TextView transactionTextView = header.findViewById(R.id.view_header_transaction_textview);
        transactionTextView.setPaintFlags(transactionTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); //underline
    }

    private void hideView() {
        ProgressBar progressBar = header.findViewById(R.id.view_header_progressbar);
        progressBar.setVisibility(View.GONE);

        TextView usedTextView = header.findViewById(R.id.view_header_used_textview);
        usedTextView.setVisibility(View.GONE);

        TextView amountTextView = header.findViewById(R.id.view_header_max_textview);
        amountTextView.setVisibility(View.GONE);

        Button periodBtn = header.findViewById(R.id.view_header_period_btn);
        periodBtn.setVisibility(View.GONE);

        ImageView warnImageView = header.findViewById(R.id.view_header_warn_imageview);
        warnImageView.setVisibility(View.GONE);

        TextView warnTextView = header.findViewById(R.id.view_header_warn_textview);
        warnTextView.setVisibility(View.GONE);
    }

    private void setUpListView() {
        listView = activity.findViewById(R.id.view_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                Transaction transaction = adapter.getTransaction(i - 1);
                viewTransaction(transaction.get_id());
            }
        });
        listView.addHeaderView(header, null, false);
        setUpAdapter();
    }

    private void setUpAdapter() {
        adapter = new TransactionAdapter(activity, categoryTransaction.getTransactions(), null);
        listView.setAdapter(adapter);
    }

    private void viewTransaction(long id) {
        ViewTransactionDialog viewTransactionDialog = new ViewTransactionDialog(id);
        viewTransactionDialog.show(activity.getSupportFragmentManager(), "View Transaction Dialog");
    }


    private void setUpButtons() {
        setUpBackBtn();
    }

    private void setUpBackBtn() {
        ImageButton backBtn = activity.findViewById(R.id.view_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
    }

    @Override
    public View getView() {
        return activity.findViewById(R.id.view_constraintlayout);
    }
}
