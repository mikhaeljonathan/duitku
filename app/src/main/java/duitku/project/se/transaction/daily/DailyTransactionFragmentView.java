package duitku.project.se.transaction.daily;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import duitku.project.se.R;
import duitku.project.se.database.DuitkuContract;
import duitku.project.se.main.Utility;
import duitku.project.se.transaction.Transaction;
import duitku.project.se.interfaces.UIView;
import duitku.project.se.transaction.view.ViewTransactionDialog;
import duitku.project.se.user.User;
import duitku.project.se.user.UserController;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.HashMap;
import java.util.List;

public class DailyTransactionFragmentView implements UIView {

    private ExpandableListView expandableListView;
    private DailyExpandableAdapter adapter;
    private Button periodButton;
    private View header;
    private View emptyView;
    private ImageView imageEmptyView;

    private final LayoutInflater inflater;
    private final ViewGroup container;
    private View view;
    private final DailyTransactionFragment fragment;

    public DailyTransactionFragmentView(LayoutInflater inflater, ViewGroup container, DailyTransactionFragment fragment) {
        this.inflater = inflater;
        this.container = container;
        this.fragment = fragment;
    }

    @Override
    public void setUpUI() {
        this.view = inflater.inflate(R.layout.fragment_transaction_viewpager, container, false);
        setUpHeader();
        setUpExpandableListView();
        setUpEmptyView();
    }

    private void setUpHeader() {
        header = inflater.inflate(R.layout.fragment_transaction_header, container, false);

        TextView titleTextView = header.findViewById(R.id.transaction_header_title_textview);
        titleTextView.setText("Daily Transaction");

        TextView totalWalletTextView = header.findViewById(R.id.transaction_header_totalwallet_textview);
        totalWalletTextView.setVisibility(View.GONE);

        ImageView addButton = header.findViewById(R.id.transaction_header_add_btn);
        addButton.setVisibility(View.GONE);

        ConstraintLayout summaryContainer = header.findViewById(R.id.transaction_header_summary_container);
        summaryContainer.setVisibility(View.GONE);

        AdView adView = header.findViewById(R.id.transaction_header_adview);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        User user = new UserController(fragment.getActivity()).getUser();
        if (user != null) {
            if (user.getUser_status().equals(DuitkuContract.UserEntry.TYPE_PREMIUM)) {
                adView.setVisibility(View.GONE);
            }
        }

        periodButton = header.findViewById(R.id.transaction_header_period_btn);
    }

    private void setUpExpandableListView() {
        ListView listView = view.findViewById(R.id.fragment_transaction_viewpager_listview);
        listView.setVisibility(View.GONE);

        expandableListView = view.findViewById(R.id.fragment_transaction_viewpager_expandablelistview);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long id) {
                Transaction transaction = (Transaction) adapter.getChild(i, i1);
                viewTransaction(transaction.get_id());
                return true;
            }
        });

        expandableListView.addHeaderView(header, null, false);
    }

    private void setUpEmptyView() {
        emptyView = inflater.inflate(R.layout.empty_view, null, false);

        imageEmptyView = emptyView.findViewById(R.id.empty_view_imageview);
        imageEmptyView.setImageResource(R.drawable.empty_transaction);

        TextView textView = emptyView.findViewById(R.id.empty_view_textview);
        textView.setText("There is no transaction\nTry adding a new one");

        expandableListView.addFooterView(emptyView, null, false);
    }

    private void viewTransaction(long id) {
        ViewTransactionDialog viewTransactionDialog = new ViewTransactionDialog(id);
        viewTransactionDialog.show(fragment.getFragmentManager(), "View Transaction Dialog");
    }

    public void updatePeriodButton(final int month, final int year) {
        periodButton.setText(Utility.monthsName[month] + " " + year);
        periodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(fragment.getActivity(), AlertDialog.THEME_HOLO_DARK,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                fragment.pickMonthYear(month, year);
                            }
                        }, year, month, 1);
                datePickerDialog.getDatePicker().findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
                datePickerDialog.show();
            }
        });
    }

    public void fillListView(List<DailyTransaction> dailyTransactionList, HashMap<DailyTransaction, List<Transaction>> dailyTransactionListHashMap, Context context) {
        adapter = new DailyExpandableAdapter(dailyTransactionList, dailyTransactionListHashMap, context);
        expandableListView.setAdapter(adapter);

        if (adapter.getGroupCount() == 0) {
            imageEmptyView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            imageEmptyView.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public View getView() {
        return view;
    }

}
