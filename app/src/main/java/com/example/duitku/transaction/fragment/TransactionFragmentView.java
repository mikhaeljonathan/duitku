package com.example.duitku.transaction.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.duitku.R;
import com.example.duitku.budget.fragment.BudgetFragment;
import com.example.duitku.interfaces.UIView;
import com.example.duitku.search.SearchActivity;
import com.example.duitku.transaction.daily.DailyTransactionFragment;
import com.example.duitku.transaction.monthly.MonthlyTransactionFragment;
import com.example.duitku.transaction.weekly.WeeklyTransactionFragment;
import com.example.duitku.wallet.fragment.WalletFragment;

public class TransactionFragmentView implements UIView {

    private final LayoutInflater inflater;
    private final ViewGroup container;
    private View view;
    private final Fragment fragment;

    public TransactionFragmentView(LayoutInflater inflater, ViewGroup container, Fragment fragment) {
        this.inflater = inflater;
        this.container = container;
        this.fragment = fragment;
    }

    @Override
    public void setUpUI() {
        this.view = inflater.inflate(R.layout.fragment_transaction, container, false);
        setUpViewPager();
        setUpSearchEditText();
    }

    private void setUpViewPager() {
        ViewPager viewPager = view.findViewById(R.id.fragment_transaction_viewpager);

        TransactionViewPagerAdapter adapter = new TransactionViewPagerAdapter(fragment.getChildFragmentManager());

        adapter.addFrag(new MonthlyTransactionFragment());
        adapter.addFrag(new WeeklyTransactionFragment());
        adapter.addFrag(new DailyTransactionFragment());
        adapter.addFrag(new WalletFragment());
        adapter.addFrag(new BudgetFragment());

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(2); // mulai dari daily transaction
    }

    private void setUpSearchEditText() {
        Button searchBtn = view.findViewById(R.id.fragment_transaction_search_btn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchIntent = new Intent(fragment.getActivity(), SearchActivity.class);
                fragment.startActivity(searchIntent);
            }
        });
    }

    public View getView() {
        return view;
    }

}
