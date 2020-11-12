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

        walletListView = rootView.findViewById(R.id.wallet_list_view);
        budgetListView = rootView.findViewById(R.id.budget_list_view);
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

    class WalletAdapter extends ArrayAdapter<Wallet>{

        public WalletAdapter(@NonNull Context context, @NonNull List<Wallet> wallets) {
            super(context, 0, wallets);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View walletListItem = convertView;
            if (walletListItem == null){
                walletListItem = LayoutInflater.from(getContext()).inflate(R.layout.item_list_wallet, parent, false);
            }

            Wallet wallet = getItem(position);

            TextView walletName = walletListItem.findViewById(R.id.wallet_name);
            walletName.setText(wallet.getWalletName());

            TextView walletAmount = walletListItem.findViewById(R.id.wallet_amount);
            walletAmount.setText(wallet.getAmount());

            return walletListItem;
        }

    }

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

            TextView budgetCategory = budgetListItem.findViewById(R.id.budget_category);
            budgetCategory.setText(budget.getCategory());

            TextView budgetAmount = budgetListItem.findViewById(R.id.budget_amount);
            budgetAmount.setText(budget.getAmount());

            TextView budgetUsed = budgetListItem.findViewById(R.id.budget_used);
            budgetUsed.setText(budget.getUsed());

            TextView budgetLeft = budgetListItem.findViewById(R.id.budget_left);
            budgetLeft.setText(budget.getLeft());

            return budgetListItem;
        }
    }
}
