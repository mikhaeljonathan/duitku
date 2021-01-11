package com.example.duitku.wallet;

import android.content.Intent;
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
import com.example.duitku.wallet.add.AddWalletDialog;
import com.example.duitku.wallet.view.ViewWalletActivity;

public class WalletFragmentView implements UIView {

    private ListView listView;
    private WalletAdapter adapter;
    private TextView totalWalletTextView;
    private View header;

    private LayoutInflater inflater;
    private ViewGroup container;
    private View view;
    private Fragment fragment;

    public WalletFragmentView(LayoutInflater inflater, ViewGroup container, Fragment fragment){
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

    public void updateTotalWalletTextView(double amount){
        totalWalletTextView.setText("Total amount:   " + amount);
    }

    public WalletAdapter getAdapter(){
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
                viewWallet(id);
            }
        });
    }

    private void setUpHeader(){
        header = inflater.inflate(R.layout.fragment_transaction_header, container, false);

        TextView titleTextView = header.findViewById(R.id.transaction_header_title_textview);
        ImageView addButton = header.findViewById(R.id.transaction_header_add_btn);
        Button periodButton = header.findViewById(R.id.transaction_header_period_btn);
        totalWalletTextView = header.findViewById(R.id.transaction_header_totalwallet_textview);
        ConstraintLayout summaryContainer = header.findViewById(R.id.transaction_header_summary_container);

        titleTextView.setText("Wallet");
        periodButton.setVisibility(View.GONE);
        summaryContainer.setVisibility(View.GONE);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWallet();
            }
        });

        listView.addHeaderView(header, null, false);
    }

    private void setUpAdapter(){
        adapter = new WalletAdapter(fragment.getActivity(), null);
        listView.setAdapter(adapter);
    }

    private void viewWallet(long id){
        Intent viewWalletIntent = new Intent(fragment.getActivity(), ViewWalletActivity.class);
        viewWalletIntent.putExtra("ID", id);
        fragment.getActivity().startActivity(viewWalletIntent);
    }

    private void addWallet(){
        AddWalletDialog addWalletDialog = new AddWalletDialog();
        addWalletDialog.show(fragment.getFragmentManager(), "Add Wallet Dialog");
    }

    @Override
    public View getView() {
        return view;
    }

}
