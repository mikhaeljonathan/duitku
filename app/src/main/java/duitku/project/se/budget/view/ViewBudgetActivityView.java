package duitku.project.se.budget.view;

import android.content.Intent;
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
import duitku.project.se.budget.Budget;
import duitku.project.se.budget.BudgetController;
import duitku.project.se.budget.edit.EditBudgetActivity;
import duitku.project.se.category.CategoryController;
import duitku.project.se.database.DuitkuContract.BudgetEntry;
import duitku.project.se.interfaces.UIView;
import duitku.project.se.main.Utility;
import duitku.project.se.transaction.Transaction;
import duitku.project.se.transaction.TransactionAdapter;
import duitku.project.se.transaction.view.ViewTransactionDialog;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

public class ViewBudgetActivityView implements UIView {

    private List<Transaction> transactionList;

    private TextView transactionTextView;
    private ListView listView;
    private TransactionAdapter adapter;

    private Budget budget;
    private final ViewBudgetActivity activity;
    private View header;

    public ViewBudgetActivityView(long id, ViewBudgetActivity activity) {
        this.budget = new BudgetController(activity).getBudgetById(id);
        this.activity = activity;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @Override
    public void setUpUI() {
        budget = new BudgetController(activity).getBudgetById(budget.get_id());
        activity.setContentView(R.layout.activity_view);
        TextView textView = activity.findViewById(R.id.view_title_textview);
        textView.setText("View Budget");

        setUpHeader();
        setUpListView();
        setUpButtons();
    }

    private void setUpHeader() {
        header = activity.getLayoutInflater().inflate(R.layout.activity_view_header,
                (ViewGroup) activity.findViewById(R.id.activity_view_constraintlayout));

        setUpBudgetName();
        setUpAmount();
        setUpPeriod();
        setUpProgressBar();
        setUpTransactionTextView();

        hideView();
    }

    private void setUpBudgetName() {
        TextView nameTextView = header.findViewById(R.id.view_header_title);
        nameTextView.setText(new CategoryController(activity).getCategoryById(budget.getCategory_id()).getCategory_name());
    }

    private void setUpAmount() {
        TextView amountTextView = header.findViewById(R.id.view_header_subtitle);
        amountTextView.setText(new DecimalFormat("###,###").format(budget.getBudget_amount()));
    }

    private void setUpPeriod() {
        TextView periodTextView = header.findViewById(R.id.view_header_subsubtitle);
        StringBuilder periodBuilder = new StringBuilder();
        if (budget.getBudget_startdate() != null){
            periodBuilder.append(Utility.convertDateToString(budget.getBudget_startdate()));
            periodBuilder.append(" - ");
            periodBuilder.append(Utility.convertDateToString(budget.getBudget_enddate()));
        } else {
            Calendar calendar = Calendar.getInstance();
            int month = calendar.get(Calendar.MONTH) + 1;
            int year = calendar.get(Calendar.YEAR);

            if (budget.getBudget_type().equals(BudgetEntry.TYPE_MONTH)){
                periodBuilder.append("1/" + month + "/" + year);
            } else if (budget.getBudget_type().equals(BudgetEntry.TYPE_3MONTH)){
                int quarter = Utility.getQuarter(month);
                periodBuilder.append("1/" + (3 * quarter - 2) + "/" + year);
            } else {
                periodBuilder.append("1/1/" + year);
            }

            periodBuilder.append(" - ");
            periodBuilder.append(Utility.getUntilDate(budget));
            periodBuilder.append(" • ");
            periodBuilder.append(BudgetController.budgetPeriod[BudgetController.budgetPeriodMap.get(budget.getBudget_type())]);
        }
        periodTextView.setText(periodBuilder.toString());
    }

    private void setUpProgressBar() {
        ProgressBar progressBar = header.findViewById(R.id.view_header_progressbar);
        TextView usedTextView = header.findViewById(R.id.view_header_used_textview);
        TextView maxTextView = header.findViewById(R.id.view_header_max_textview);

        double amount = budget.getBudget_amount();
        double used = budget.getBudget_used();

        progressBar.setMax((int) amount);
        progressBar.setProgress((int) used);

        maxTextView.setText(new DecimalFormat("###,###").format(amount));
        usedTextView.setText(new DecimalFormat("###,###").format(used));

        ImageView warnImageView = header.findViewById(R.id.view_header_warn_imageview);
        TextView warnTextView = header.findViewById(R.id.view_header_warn_textview);

        if (used > amount) {
            warnImageView.setVisibility(View.VISIBLE);
            warnTextView.setVisibility(View.VISIBLE);
            warnTextView.setText("Overspent " + new DecimalFormat("###,###").format(used - amount));
        } else {
            warnImageView.setVisibility(View.GONE);
            warnTextView.setVisibility(View.GONE);
        }
    }

    private void setUpTransactionTextView() {
        transactionTextView = header.findViewById(R.id.view_header_transaction_textview);
        transactionTextView.setPaintFlags(transactionTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); //underline
    }

    private void hideView() {
        Button periodBtn = header.findViewById(R.id.view_header_period_btn);
        periodBtn.setVisibility(View.GONE);
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

    private void viewTransaction(long id) {
        ViewTransactionDialog viewTransactionDialog = new ViewTransactionDialog(id);
        viewTransactionDialog.show(activity.getSupportFragmentManager(), "View Transaction Dialog");
    }

    private void setUpAdapter() {
        adapter = new TransactionAdapter(activity, transactionList, null);
        listView.setAdapter(adapter);
        if (adapter.isEmpty()) {
            transactionTextView.setVisibility(View.GONE);
        }
    }

    private void setUpButtons() {
        setUpBackBtn();
        setUpEditBtn();
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

    private void setUpEditBtn() {
        ImageButton editBtn = activity.findViewById(R.id.view_edit_btn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editBudgetIntent = new Intent(activity, EditBudgetActivity.class);
                editBudgetIntent.putExtra("ID", budget.get_id());
                activity.startActivity(editBudgetIntent);
            }
        });
    }

    @Override
    public View getView() {
        return activity.findViewById(R.id.activity_view_constraintlayout);
    }
}
