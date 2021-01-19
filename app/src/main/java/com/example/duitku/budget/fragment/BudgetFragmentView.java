package com.example.duitku.budget.fragment;

import android.content.Intent;
import android.database.Cursor;
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
import com.example.duitku.budget.add.AddBudgetDialog;
import com.example.duitku.budget.view.ViewBudgetActivity;
import com.example.duitku.interfaces.UIView;

public class BudgetFragmentView implements UIView {

    private ListView listView;
    private BudgetAdapter adapter;
    private View header;
    private View emptyView;
    private ImageView imageEmptyView;

    private final LayoutInflater inflater;
    private final ViewGroup container;
    private View view;
    private final Fragment fragment;

    public BudgetFragmentView(LayoutInflater inflater, ViewGroup container, Fragment fragment){
        this.inflater = inflater;
        this.container = container;
        this.fragment = fragment;
    }

    @Override
    public void setUpUI() {
        this.view = inflater.inflate(R.layout.fragment_transaction_viewpager, container, false);
        setUpHeader();
        setUpListView();
        setUpEmptyView();
    }

    public BudgetAdapter getAdapter(){
        return adapter;
    }

    private void setUpHeader(){
        header = inflater.inflate(R.layout.fragment_transaction_header, container, false);

        setUpTitle();
        setUpAddBtn();

        hideView();
    }

    private void setUpTitle(){
        TextView titleTextView = header.findViewById(R.id.transaction_header_title_textview);
        titleTextView.setText("Budget");
    }

    private void setUpAddBtn(){
        ImageView addButton = header.findViewById(R.id.transaction_header_add_btn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBudget();
            }
        });
    }

    private void addBudget(){
        AddBudgetDialog addBudgetDialog = new AddBudgetDialog();
        addBudgetDialog.show(fragment.getFragmentManager(), "Add Budget Dialog");
    }

    private void hideView(){
        Button periodButton = header.findViewById(R.id.transaction_header_period_btn);
        TextView totalWalletTextView = header.findViewById(R.id.transaction_header_totalwallet_textview);
        ConstraintLayout summaryContainer = header.findViewById(R.id.transaction_header_summary_container);

        periodButton.setVisibility(View.GONE);
        totalWalletTextView.setVisibility(View.GONE);
        summaryContainer.setVisibility(View.GONE);
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
        listView.addHeaderView(header, null, false);

        setUpAdapter();
    }

    private void viewBudget(long id){
        Intent viewBudgetIntent = new Intent(fragment.getActivity(), ViewBudgetActivity.class);
        viewBudgetIntent.putExtra("ID", id);
        fragment.getActivity().startActivity(viewBudgetIntent);
    }

    private void setUpAdapter(){
        adapter = new BudgetAdapter(fragment.getActivity(), null);
        listView.setAdapter(adapter);
    }

    private void setUpEmptyView(){
        emptyView = inflater.inflate(R.layout.empty_view, null, false);

        imageEmptyView = emptyView.findViewById(R.id.empty_view_imageview);
        imageEmptyView.setImageResource(R.drawable.empty_budget);

        TextView textView = emptyView.findViewById(R.id.empty_view_textview);
        textView.setText("There is no budget\nTry adding a new one");

        listView.addFooterView(emptyView, null, false);
    }

    public void swapCursor(Cursor data){
        if (data != null){
            if (!data.moveToFirst()){
                imageEmptyView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.VISIBLE);
            } else {
                imageEmptyView.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);
            }
        }
        adapter.swapCursor(data);
    }

    @Override
    public View getView() {
        return view;
    }
}
