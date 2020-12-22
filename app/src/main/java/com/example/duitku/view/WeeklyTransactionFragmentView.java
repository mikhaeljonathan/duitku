package com.example.duitku.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.duitku.R;
import com.example.duitku.Utility;
import com.example.duitku.adapter.WeeklyExpandableAdapter;
import com.example.duitku.dialog.MonthYearPickerDialog;
import com.example.duitku.flows.WeeklyTransactionFragment;
import com.example.duitku.model.CategoryTransaction;
import com.example.duitku.model.WeeklyTransaction;

import java.util.HashMap;
import java.util.List;

public class WeeklyTransactionFragmentView implements UIView{

    private ExpandableListView expandableListView;
    private WeeklyExpandableAdapter adapter;
    private Button periodButton;
    private View header;

    private TextView totalAmountTextView;
    private TextView totalGlobalIncomeTextView;
    private TextView totalGlobalExpenseTextView;

    private LayoutInflater inflater;
    private ViewGroup container;
    private View view;
    private WeeklyTransactionFragment fragment;

    public WeeklyTransactionFragmentView(LayoutInflater inflater, ViewGroup container, WeeklyTransactionFragment fragment){
        this.inflater = inflater;
        this.container = container;
        this.fragment = fragment;
    }

    @Override
    public void setUpUI() {
        this.view = inflater.inflate(R.layout.fragment_transaction_viewpager, container, false);
        setUpExpandableListView();
        setUpHeader();
        setUpPeriodButton();
    }

    public void updatePeriodButton(final int month, final int year){
        periodButton.setText(Utility.monthsName[month] + " " + year);
        periodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatDialogFragment monthYearPickerDialog = new MonthYearPickerDialog(fragment, month, year);
                monthYearPickerDialog.show(fragment.getFragmentManager(), "Month Year Picker Dialog");
            }
        });
    }

    public void updateSummary(double totalGlobalIncome, double totalGlobalExpense){
        totalAmountTextView.setText((totalGlobalIncome - totalGlobalExpense) + "");
        totalGlobalIncomeTextView.setText(totalGlobalIncome + "");
        totalGlobalExpenseTextView.setText(totalGlobalExpense + "");
    }

    public void fillListView(List<WeeklyTransaction> weeklyTransactionList, HashMap<WeeklyTransaction, List<CategoryTransaction>> categoryTransactionListHashMap, Context context){
        adapter = new WeeklyExpandableAdapter(weeklyTransactionList, categoryTransactionListHashMap, context);
        expandableListView.setAdapter(adapter);
    }

    private void setUpExpandableListView(){
        ListView listView = view.findViewById(R.id.fragment_transaction_viewpager_listview);
        listView.setVisibility(View.GONE);
        expandableListView = view.findViewById(R.id.fragment_transaction_viewpager_expandablelistview);
    }

    private void setUpHeader(){
        header = inflater.inflate(R.layout.fragment_transaction_header, container, false);

        TextView titleTextView = header.findViewById(R.id.transaction_header_title_textview);
        TextView totalWalletTextView = header.findViewById(R.id.transaction_header_totalwallet_textview);
        ImageView addButton = header.findViewById(R.id.transaction_header_add_btn);

        titleTextView.setText("Weekly Transaction");
        totalWalletTextView.setVisibility(View.GONE);
        addButton.setVisibility(View.GONE);

        totalAmountTextView = header.findViewById(R.id.transaction_header_amount_textview);
        totalGlobalIncomeTextView = header.findViewById(R.id.transaction_header_income_amount_textview);
        totalGlobalExpenseTextView = header.findViewById(R.id.transaction_header_expense_amount_textview);

        expandableListView.addHeaderView(header, null, false);
    }

    private void setUpPeriodButton(){
        periodButton = header.findViewById(R.id.transaction_header_period_btn);
    }

    @Override
    public View getView() {
        return view;
    }
}
