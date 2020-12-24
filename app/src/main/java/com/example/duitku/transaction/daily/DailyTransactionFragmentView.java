package com.example.duitku.transaction.daily;

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
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.duitku.R;
import com.example.duitku.main.Utility;
import com.example.duitku.date.MonthYearPickerDialog;
import com.example.duitku.transaction.Transaction;
import com.example.duitku.interfaces.UIView;

import java.util.HashMap;
import java.util.List;

public class DailyTransactionFragmentView implements UIView {

    private ExpandableListView expandableListView;
    private DailyExpandableAdapter adapter;
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

    public void fillListView(List<DailyTransaction> dailyTransactionList, HashMap<DailyTransaction, List<Transaction>> dailyTransactionListHashMap, Context context){
        adapter = new DailyExpandableAdapter(dailyTransactionList, dailyTransactionListHashMap, context);
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
        ConstraintLayout summaryContainer = header.findViewById(R.id.transaction_header_summary_container);

        titleTextView.setText("Daily Transaction");
        totalWalletTextView.setVisibility(View.GONE);
        addButton.setVisibility(View.GONE);
        summaryContainer.setVisibility(View.GONE);

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
