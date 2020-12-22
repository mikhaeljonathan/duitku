package com.example.duitku.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.duitku.R;
import com.example.duitku.Utility;
import com.example.duitku.adapter.MonthlyExpandableAdapter;
import com.example.duitku.adapter.WeeklyExpandableAdapter;
import com.example.duitku.dialog.MonthYearPickerDialog;
import com.example.duitku.dialog.YearPickerDialog;
import com.example.duitku.flows.MonthlyTransactionFragment;
import com.example.duitku.flows.WeeklyTransactionFragment;
import com.example.duitku.model.CategoryTransaction;
import com.example.duitku.model.MonthlyTransaction;
import com.example.duitku.model.WeeklyTransaction;

import java.util.HashMap;
import java.util.List;

public class MonthlyTransactionFragmentView implements UIView{

    private ExpandableListView listView;
    private MonthlyExpandableAdapter adapter;
    private Button periodButton;
    private View header;

    private TextView totalAmountTextView;
    private TextView totalGlobalIncomeTextView;
    private TextView totalGlobalExpenseTextView;

    private LayoutInflater inflater;
    private ViewGroup container;
    private View view;
    private MonthlyTransactionFragment fragment;

    public MonthlyTransactionFragmentView(LayoutInflater inflater, ViewGroup container, MonthlyTransactionFragment fragment){
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

    public void updatePeriodButton(final int year){
        periodButton.setText(year + "");
        periodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YearPickerDialog yearPickerDialog = new YearPickerDialog(fragment, year);
                yearPickerDialog.show(fragment.getFragmentManager(), "Year Picker Dialog");
            }
        });
    }

    public void updateSummary(double totalGlobalIncome, double totalGlobalExpense){
        totalAmountTextView.setText((totalGlobalIncome - totalGlobalExpense) + "");
        totalGlobalIncomeTextView.setText(totalGlobalIncome + "");
        totalGlobalExpenseTextView.setText(totalGlobalExpense + "");
    }

    public void fillListView(List<MonthlyTransaction> monthlyTransactionList, HashMap<MonthlyTransaction, List<CategoryTransaction>> categoryTransactionListHashMap, Context context){
        adapter = new MonthlyExpandableAdapter(monthlyTransactionList, categoryTransactionListHashMap, context);
        listView.setAdapter(adapter);
    }

    private void setUpExpandableListView(){
        listView = view.findViewById(R.id.fragment_transaction_viewpager_expandablelistview);
    }

    private void setUpHeader(){
        header = inflater.inflate(R.layout.fragment_transaction_header, container, false);

        TextView titleTextView = header.findViewById(R.id.transaction_header_title_textview);
        ImageView addButton = header.findViewById(R.id.transaction_header_add_btn);

        titleTextView.setText("Monthly Transaction");
        addButton.setVisibility(View.GONE);

        totalAmountTextView = header.findViewById(R.id.transaction_header_amount_textview);
        totalGlobalIncomeTextView = header.findViewById(R.id.transaction_header_income_amount_textview);
        totalGlobalExpenseTextView = header.findViewById(R.id.transaction_header_expense_amount_textview);

        listView.addHeaderView(header, null, false);
    }

    private void setUpPeriodButton(){
        periodButton = header.findViewById(R.id.transaction_header_period_btn);
    }

    @Override
    public View getView() {
        return view;
    }
}