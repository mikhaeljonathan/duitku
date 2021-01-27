package com.example.duitku.transaction.weekly;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
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

import com.example.duitku.R;
import com.example.duitku.database.DuitkuContract;
import com.example.duitku.main.Utility;
import com.example.duitku.transaction.category.CategoryTransaction;
import com.example.duitku.interfaces.UIView;
import com.example.duitku.transaction.category.ViewCategoryTransactionActivity;
import com.example.duitku.user.User;
import com.example.duitku.user.UserController;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

public class WeeklyTransactionFragmentView implements UIView {

    private ExpandableListView expandableListView;
    private WeeklyExpandableAdapter adapter;
    private Button periodButton;
    private View header;
    private View emptyView;
    private ImageView imageEmptyView;

    private ConstraintLayout summaryContainer;
    private TextView totalAmountTextView;
    private TextView totalGlobalIncomeTextView;
    private TextView totalGlobalExpenseTextView;

    private final LayoutInflater inflater;
    private final ViewGroup container;
    private View view;
    private final WeeklyTransactionFragment fragment;

    public WeeklyTransactionFragmentView(LayoutInflater inflater, ViewGroup container, WeeklyTransactionFragment fragment){
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

    private void setUpHeader(){
        header = inflater.inflate(R.layout.fragment_transaction_header, container, false);

        summaryContainer = header.findViewById(R.id.transaction_header_summary_container);
        TextView titleTextView = header.findViewById(R.id.transaction_header_title_textview);
        TextView totalWalletTextView = header.findViewById(R.id.transaction_header_totalwallet_textview);
        ImageView addButton = header.findViewById(R.id.transaction_header_add_btn);

        titleTextView.setText("Weekly Transaction");
        totalWalletTextView.setVisibility(View.GONE);
        addButton.setVisibility(View.GONE);

        AdView adView = header.findViewById(R.id.transaction_header_adview);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        User user = new UserController(fragment.getActivity()).getUser();
        if (user != null){
            if (user.getUser_status().equals(DuitkuContract.UserEntry.TYPE_PREMIUM)) {
                adView.setVisibility(View.GONE);
            }
        }

        totalAmountTextView = header.findViewById(R.id.transaction_header_amount_textview);
        totalGlobalIncomeTextView = header.findViewById(R.id.transaction_header_income_amount_textview);
        totalGlobalExpenseTextView = header.findViewById(R.id.transaction_header_expense_amount_textview);

        periodButton = header.findViewById(R.id.transaction_header_period_btn);
    }

    private void setUpExpandableListView(){
        ListView listView = view.findViewById(R.id.fragment_transaction_viewpager_listview);
        listView.setVisibility(View.GONE);

        expandableListView = view.findViewById(R.id.fragment_transaction_viewpager_expandablelistview);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                CategoryTransaction categoryTransaction = (CategoryTransaction) adapter.getChild(i, i1);
                viewCategoryTransaction(categoryTransaction);
                return true;
            }
        });

        expandableListView.addHeaderView(header, null, false);
    }

    private void setUpEmptyView(){
        emptyView = inflater.inflate(R.layout.empty_view, null, false);

        imageEmptyView = emptyView.findViewById(R.id.empty_view_imageview);
        imageEmptyView.setImageResource(R.drawable.empty_transaction);

        TextView textView = emptyView.findViewById(R.id.empty_view_textview);
        textView.setText("There is no transaction\nTry adding a new one");

        expandableListView.addFooterView(emptyView, null, false);
    }

    private void viewCategoryTransaction(CategoryTransaction categoryTransaction){
        Intent viewCategoryTransactionIntent = new Intent(fragment.getActivity(), ViewCategoryTransactionActivity.class);
        viewCategoryTransactionIntent.putExtra("CategoryTransaction", categoryTransaction);
        fragment.getActivity().startActivity(viewCategoryTransactionIntent);
    }

    public void updatePeriodButton(final int month, final int year){
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

    public void updateSummary(double totalGlobalIncome, double totalGlobalExpense){
        totalAmountTextView.setText(new DecimalFormat("###,###").format(totalGlobalIncome-totalGlobalExpense));
        totalGlobalIncomeTextView.setText(new DecimalFormat("###,###").format(totalGlobalIncome));
        totalGlobalExpenseTextView.setText(new DecimalFormat("###,###").format(totalGlobalExpense));
    }

    public void fillListView(List<WeeklyTransaction> weeklyTransactionList, HashMap<WeeklyTransaction, List<CategoryTransaction>> categoryTransactionListHashMap, Context context){
        adapter = new WeeklyExpandableAdapter(weeklyTransactionList, categoryTransactionListHashMap, context);
        expandableListView.setAdapter(adapter);

        if (adapter.getGroupCount() == 0){
            summaryContainer.setVisibility(View.GONE);
            imageEmptyView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            summaryContainer.setVisibility(View.VISIBLE);
            imageEmptyView.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public View getView() {
        return view;
    }
}
