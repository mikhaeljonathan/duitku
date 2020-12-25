package com.example.duitku.transaction.parent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.duitku.R;
import com.example.duitku.budget.BudgetFragment;
import com.example.duitku.interfaces.UIView;
import com.example.duitku.transaction.daily.DailyTransactionFragment;
import com.example.duitku.transaction.monthly.MonthlyTransactionFragment;
import com.example.duitku.transaction.weekly.WeeklyTransactionFragment;
import com.example.duitku.wallet.WalletFragment;

import java.util.ArrayList;

public class TransactionFragmentView implements UIView {

    private ViewPager viewPager;

    private LayoutInflater inflater;
    private ViewGroup container;
    private View view;
    private Fragment fragment;

    public TransactionFragmentView(LayoutInflater inflater, ViewGroup container, Fragment fragment){
        this.inflater = inflater;
        this.container = container;
        this.fragment = fragment;
    }

    @Override
    public void setUpUI() {
        this.view = inflater.inflate(R.layout.fragment_transaction, container, false);;
        setUpViewPager();
    }

    private void setUpViewPager(){
        viewPager = view.findViewById(R.id.fragment_transaction_viewpager);

        TransactionViewPagerAdapter adapter = new TransactionViewPagerAdapter(fragment.getChildFragmentManager());

        adapter.addFrag(new MonthlyTransactionFragment());
        adapter.addFrag(new WeeklyTransactionFragment());
        adapter.addFrag(new DailyTransactionFragment());
        adapter.addFrag(new WalletFragment());
        adapter.addFrag(new BudgetFragment());

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(2); // mulai dari daily transaction
    }

    public View getView(){
        return view;
    }

}