package com.example.duitku.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.duitku.R;
import com.example.duitku.model.Budget;
import com.example.duitku.model.Wallet;

import java.util.ArrayList;
import java.util.List;

public class OthersTransactionFragment extends Fragment {

    // ini agak beda sama ke 3 fragment yang lain

    // di sini ga pake expandable list view, tapi pake 2 listview
    // yaitu listview buat wallet sama budget
    // masing2 listview perlu adapter
    ListView walletListView;
    ListView budgetListView;
    WalletAdapter walletAdapter;
    BudgetAdapter budgetAdapter;

    List<Wallet> wallets;
    List<Budget> budgets;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transaction_others, container, false);

        walletListView = rootView.findViewById(R.id.transaction_others_wallet_listview);
        budgetListView = rootView.findViewById(R.id.transaction_others_budget_listview);
        setContent();

        walletAdapter = new WalletAdapter(getContext(), wallets);
        budgetAdapter = new BudgetAdapter(getContext(), budgets);

        walletListView.setAdapter(walletAdapter);
        budgetListView.setAdapter(budgetAdapter);

        return rootView;
    }

    private void setContent(){
        wallets = new ArrayList<>();
        budgets = new ArrayList<>();

        wallets.add(new Wallet("Bank Account", "Rp 10.000.000"));
        wallets.add(new Wallet("OVO", "Rp 1.000.000"));
        wallets.add(new Wallet("OVO", "Rp 1.000.000"));
        wallets.add(new Wallet("OVO", "Rp 1.000.000"));
        wallets.add(new Wallet("OVO", "Rp 1.000.000"));
        wallets.add(new Wallet("Cash", "Rp 1.000.000"));

        budgets.add(new Budget("Food", "Rp 1.000.000", "Rp 700.000", "Rp 300.000"));
        budgets.add(new Budget("Shop", "Rp 2.000.000", "Rp 1.800.000", "Rp 200.000"));
        budgets.add(new Budget("Shop", "Rp 2.000.000", "Rp 1.800.000", "Rp 200.000"));
        budgets.add(new Budget("Shop", "Rp 2.000.000", "Rp 1.800.000", "Rp 200.000"));
        budgets.add(new Budget("Shop", "Rp 2.000.000", "Rp 1.800.000", "Rp 200.000"));
        budgets.add(new Budget("Shop", "Rp 2.000.000", "Rp 1.800.000", "Rp 200.000"));
        budgets.add(new Budget("Transport", "Rp 1.000.000", "Rp 300.000", "Rp 700.000"));

    }

    // adapter nya beda sama ExpandableListView
    // adapternya ini subclass dari ArrayAdapter<Object dari array nya>
    class WalletAdapter extends ArrayAdapter<Wallet>{

        // construct nya pake List of object
        public WalletAdapter(@NonNull Context context, @NonNull List<Wallet> wallets) {
            super(context, 0, wallets);
        }

        // ini buat custom view nya
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            // convertView itu view secara keseluruhan
            View walletListItem = convertView;
            if (walletListItem == null){
                walletListItem = LayoutInflater.from(getContext()).inflate(R.layout.item_list_wallet, parent, false);
            }

            // ambil object Walletnya
            Wallet wallet = getItem(position);

            TextView walletName = walletListItem.findViewById(R.id.item_list_wallet_name_textview);
            walletName.setText(wallet.getWalletName());

            TextView walletAmount = walletListItem.findViewById(R.id.item_list_wallet_amount_textview);
            walletAmount.setText(wallet.getAmount());

            return walletListItem;

        }

    }

    // penjelasan nya kurang lebih sama kayak di atas
    class BudgetAdapter extends ArrayAdapter<Budget> {

        public BudgetAdapter(@NonNull Context context, @NonNull List<Budget> budgets) {
            super(context, 0, budgets);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View budgetListItem = convertView;
            if (budgetListItem == null){
                budgetListItem = LayoutInflater.from(getContext()).inflate(R.layout.item_list_budget, parent, false);
            }

            Budget budget = getItem(position);

            TextView budgetCategory = budgetListItem.findViewById(R.id.item_list_budget_category_textview);
            budgetCategory.setText(budget.getCategory());

            TextView budgetAmount = budgetListItem.findViewById(R.id.item_list_budget_amount_textview);
            budgetAmount.setText(budget.getAmount());

            TextView budgetUsed = budgetListItem.findViewById(R.id.item_list_budget_used_textview);
            budgetUsed.setText(budget.getUsed());

            TextView budgetLeft = budgetListItem.findViewById(R.id.item_list_budget_left_textview);
            budgetLeft.setText(budget.getLeft());

            return budgetListItem;
        }

    }

}
