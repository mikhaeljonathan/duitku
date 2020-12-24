package com.example.duitku.budget;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.duitku.R;
import com.example.duitku.interfaces.UIView;


public class BudgetFragmentView implements UIView {

    private ListView listView;
    private BudgetAdapter adapter;
    private View header;

    private LayoutInflater inflater;
    private ViewGroup container;
    private View view;
    private Fragment fragment;

    public BudgetFragmentView(LayoutInflater inflater, ViewGroup container, Fragment fragment){
        this.inflater = inflater;
        this.container = container;
        this.fragment = fragment;
    }

    @Override
    public void setUpUI() {
        this.view = inflater.inflate(R.layout.fragment_transaction_viewpager, container, false);
        setUpListView();
        setUpHeader();
        setUpAdapter();
    }

    public BudgetAdapter getAdapter(){
        return adapter;
    }

    private void setUpListView(){
        ExpandableListView expandableListView = view.findViewById(R.id.fragment_transaction_viewpager_expandablelistview);
        expandableListView.setVisibility(View.GONE);
        listView = view.findViewById(R.id.fragment_transaction_viewpager_listview);
        // per item kalau diclick
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                viewBudget(id);
            }
        });
    }

    private void setUpHeader(){
        header = inflater.inflate(R.layout.fragment_transaction_header, container, false);

        TextView titleTextView = header.findViewById(R.id.transaction_header_title_textview);
        ImageView addButton = header.findViewById(R.id.transaction_header_add_btn);
        Button periodButton = header.findViewById(R.id.transaction_header_period_btn);
        TextView totalWalletTextView = header.findViewById(R.id.transaction_header_totalwallet_textview);
        ConstraintLayout summaryContainer = header.findViewById(R.id.transaction_header_summary_container);

        titleTextView.setText("Budget");
        periodButton.setVisibility(View.GONE);
        totalWalletTextView.setVisibility(View.GONE);
        summaryContainer.setVisibility(View.GONE);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBudget();
            }
        });

        listView.addHeaderView(header, null, false);
    }

    private void setUpAdapter(){
        adapter = new BudgetAdapter(fragment.getActivity(), null);
        listView.setAdapter(adapter);
    }

    private void viewBudget(long id){

    }

    private void addBudget(){
        AddBudgetDialog addBudgetDialog = new AddBudgetDialog();
        addBudgetDialog.show(fragment.getFragmentManager(), "Add Budget Dialog");
    }

    @Override
    public View getView() {
        return view;
    }
}
