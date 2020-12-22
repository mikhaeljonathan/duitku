package com.example.duitku.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.duitku.R;
import com.example.duitku.Utility;
import com.example.duitku.adapter.DailyExpandableAdapter;
import com.example.duitku.dialog.MonthYearPickerDialog;
import com.example.duitku.flows.DailyTransactionFragment;
import com.example.duitku.model.DailyTransaction;
import com.example.duitku.model.Transaction;

import java.util.HashMap;
import java.util.List;

public class DailyTransactionFragmentView implements UIView{

    private ExpandableListView dailyExpandableListView;
    private DailyExpandableAdapter dailyExpandableAdapter;
    private Button periodButton;
    private View header;

    private LayoutInflater inflater;
    private ViewGroup container;
    private View view;
    private DailyTransactionFragment fragment;

    public DailyTransactionFragmentView(LayoutInflater inflater, ViewGroup container, DailyTransactionFragment fragment){
        this.inflater = inflater;
        this.container = container;
        this.fragment = fragment;
    }

    @Override
    public void setUpUI() {
        this.view = inflater.inflate(R.layout.fragment_transaction_daily, container, false);
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

    public void fillListView(List<DailyTransaction> dailyTransactionList, HashMap<DailyTransaction, List<Transaction>> dailyTransactionListHashMap, Context context){
        dailyExpandableAdapter = new DailyExpandableAdapter(dailyTransactionList, dailyTransactionListHashMap, context);
        dailyExpandableListView.setAdapter(dailyExpandableAdapter);
    }

    private void setUpPeriodButton(){
        periodButton = header.findViewById(R.id.transaction_header_period_btn);
    }

    private void setUpExpandableListView(){
        dailyExpandableListView = view.findViewById(R.id.transaction_daily_expandablelistview);
    }

    private void setUpHeader(){
        header = inflater.inflate(R.layout.fragment_transaction_header, container, false);

        TextView titleTextView = header.findViewById(R.id.transaction_header_title_textview);
        ImageView addButton = header.findViewById(R.id.transaction_header_add_btn);
        ConstraintLayout summaryContainer = header.findViewById(R.id.transaction_header_summary_container);

        titleTextView.setText("Daily Transaction");
        addButton.setVisibility(View.GONE);
        summaryContainer.setVisibility(View.GONE);

        dailyExpandableListView.addHeaderView(header, null, false);
    }

    @Override
    public View getView() {
        return view;
    }

}
