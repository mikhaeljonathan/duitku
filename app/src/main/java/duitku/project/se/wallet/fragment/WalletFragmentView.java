package duitku.project.se.wallet.fragment;

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

import duitku.project.se.R;
import duitku.project.se.interfaces.UIView;
import duitku.project.se.wallet.add.AddWalletDialog;
import duitku.project.se.wallet.view.ViewWalletActivity;

import java.text.DecimalFormat;

public class WalletFragmentView implements UIView {

    private ListView listView;
    private WalletAdapter adapter;
    private TextView totalWalletTextView;
    private View header;
    private View emptyView;
    private ImageView imageEmptyView;

    private LayoutInflater inflater;
    private ViewGroup container;
    private View view;
    private Fragment fragment;

    public WalletFragmentView(LayoutInflater inflater, ViewGroup container, Fragment fragment) {
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

    public void updateTotalWalletTextView(double amount) {
        totalWalletTextView.setText("Total amount:   " + new DecimalFormat("###,###").format(amount));
    }

    public WalletAdapter getAdapter() {
        return adapter;
    }

    private void setUpHeader() {
        header = inflater.inflate(R.layout.fragment_transaction_header, container, false);
        totalWalletTextView = header.findViewById(R.id.transaction_header_totalwallet_textview);

        setUpTitle();
        setUpAddBtn();

        hideView();
    }

    private void setUpTitle() {
        TextView titleTextView = header.findViewById(R.id.transaction_header_title_textview);
        titleTextView.setText("Wallet");
    }

    private void setUpAddBtn() {
        ImageView addButton = header.findViewById(R.id.transaction_header_add_btn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWallet();
            }
        });
    }

    private void addWallet() {
        AddWalletDialog addWalletDialog = new AddWalletDialog();
        addWalletDialog.show(fragment.getFragmentManager(), "Add Wallet Dialog");
    }

    private void hideView() {
        Button periodButton = header.findViewById(R.id.transaction_header_period_btn);
        ConstraintLayout summaryContainer = header.findViewById(R.id.transaction_header_summary_container);

        periodButton.setVisibility(View.GONE);
        summaryContainer.setVisibility(View.GONE);
    }

    private void setUpListView() {
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
        listView.addHeaderView(header, null, false);

        setUpAdapter();
    }

    private void viewWallet(long id) {
        Intent viewWalletIntent = new Intent(fragment.getActivity(), ViewWalletActivity.class);
        viewWalletIntent.putExtra("ID", id);
        fragment.getActivity().startActivity(viewWalletIntent);
    }

    private void setUpAdapter() {
        adapter = new WalletAdapter(fragment.getActivity(), null);
        listView.setAdapter(adapter);
    }

    private void setUpEmptyView() {
        emptyView = inflater.inflate(R.layout.empty_view, null, false);

        imageEmptyView = emptyView.findViewById(R.id.empty_view_imageview);
        imageEmptyView.setImageResource(R.drawable.empty_wallet);

        TextView textView = emptyView.findViewById(R.id.empty_view_textview);
        textView.setText("There is no wallet\nTry adding a new one");

        listView.addFooterView(emptyView, null, false);
    }

    public void swapCursor(Cursor data) {
        if (data != null) {
            if (!data.moveToFirst()) {
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
