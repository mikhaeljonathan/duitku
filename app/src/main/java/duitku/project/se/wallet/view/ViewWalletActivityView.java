package duitku.project.se.wallet.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import duitku.project.se.R;
import duitku.project.se.main.Utility;
import duitku.project.se.transaction.Transaction;
import duitku.project.se.transaction.TransactionAdapter;
import duitku.project.se.interfaces.UIView;
import duitku.project.se.transaction.view.ViewTransactionDialog;
import duitku.project.se.wallet.Wallet;
import duitku.project.se.wallet.WalletController;
import duitku.project.se.wallet.edit.EditWalletActivity;

import java.text.DecimalFormat;
import java.util.List;

public class ViewWalletActivityView implements UIView {

    private Button periodButton;
    private TextView transactionTextView;

    private ListView listView;
    private TransactionAdapter adapter;

    private Wallet wallet;
    private final ViewWalletActivity activity;
    private View header;

    public ViewWalletActivityView(long id, ViewWalletActivity activity) {
        this.wallet = new WalletController(activity).getWalletById(id);
        this.activity = activity;
    }

    public void setUpAdapter(List<Transaction> transactions) {
        adapter = new TransactionAdapter(activity, transactions, wallet.get_id());
        listView.setAdapter(adapter);
        if (adapter.isEmpty()) {
            transactionTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void setUpUI() {
        wallet = new WalletController(activity).getWalletById(wallet.get_id());
        activity.setContentView(R.layout.activity_view);
        TextView textView = activity.findViewById(R.id.view_title_textview);
        textView.setText("View Wallet");

        setUpHeader();
        setUpListView();
        setUpButtons();
    }

    private void setUpHeader() {
        header = activity.getLayoutInflater().inflate(R.layout.activity_view_header,
                (ViewGroup) activity.findViewById(R.id.activity_view_constraintlayout));

        setUpWalletName();
        setUpAmount();
        setUpDesc();
        setUpTransactionTextView();
        hideView();

        periodButton = header.findViewById(R.id.view_header_period_btn);
    }

    private void setUpWalletName() {
        TextView nameTextView = header.findViewById(R.id.view_header_title);
        nameTextView.setText(wallet.getWallet_name());
    }

    private void setUpAmount() {
        TextView amountTextView = header.findViewById(R.id.view_header_subtitle);
        amountTextView.setText(new DecimalFormat("###,###").format(wallet.getWallet_amount()));
    }

    private void setUpDesc() {
        TextView descTextView = header.findViewById(R.id.view_header_subsubtitle);
        String desc = wallet.getWallet_desc();
        if (desc.equals("")) {
            descTextView.setVisibility(View.GONE);
        } else {
            descTextView.setText(wallet.getWallet_desc());
        }
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
    }

    private void viewTransaction(long id) {
        ViewTransactionDialog viewTransactionDialog = new ViewTransactionDialog(id);
        viewTransactionDialog.show(activity.getSupportFragmentManager(), "View Transaction Dialog");
    }

    private void setUpTransactionTextView() {
        transactionTextView = header.findViewById(R.id.view_header_transaction_textview);
        transactionTextView.setPaintFlags(transactionTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); //underline
    }

    private void hideView() {
        ProgressBar progressBar = header.findViewById(R.id.view_header_progressbar);
        progressBar.setVisibility(View.GONE);

        TextView usedTextView = header.findViewById(R.id.view_header_used_textview);
        usedTextView.setVisibility(View.GONE);

        TextView amountTextView = header.findViewById(R.id.view_header_max_textview);
        amountTextView.setVisibility(View.GONE);

        ImageView warnImageView = header.findViewById(R.id.view_header_warn_imageview);
        warnImageView.setVisibility(View.GONE);

        TextView warnTextView = header.findViewById(R.id.view_header_warn_textview);
        warnTextView.setVisibility(View.GONE);
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
                Intent editWalletIntent = new Intent(activity, EditWalletActivity.class);
                editWalletIntent.putExtra("ID", wallet.get_id());
                activity.startActivity(editWalletIntent);
            }
        });
    }

    public void updatePeriodButton(final int month, final int year) {
        periodButton.setText(Utility.monthsName[month] + " " + year);
        periodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(activity, AlertDialog.THEME_HOLO_DARK,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                activity.pickMonthYear(month, year);
                            }
                        }, year, month, 1);
                datePickerDialog.getDatePicker().findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
                datePickerDialog.show();
            }
        });
    }

    @Override
    public View getView() {
        return activity.findViewById(R.id.activity_view_constraintlayout);
    }
}
