package com.example.duitku.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.duitku.R;
import com.example.duitku.adapter.TransactionAdapter;
import com.example.duitku.factory.fragment.BudgetTransactionFragmentFactory;
import com.example.duitku.factory.fragment.DailyTransactionFragmentFactory;
import com.example.duitku.factory.fragment.FragmentFactory;
import com.example.duitku.factory.fragment.MonthlyTransactionFragmentFactory;
import com.example.duitku.factory.fragment.WalletTransactionFragmentFactory;
import com.example.duitku.factory.fragment.WeeklyTransactionFragmentFactory;

import java.util.ArrayList;
import java.util.List;

public class TransactionFragmentView implements UIView{

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

        ArrayList<FragmentFactory> fragmentFactories = new ArrayList<>();
        fragmentFactories.add(new MonthlyTransactionFragmentFactory());
        fragmentFactories.add(new WeeklyTransactionFragmentFactory());
        fragmentFactories.add(new DailyTransactionFragmentFactory());
        fragmentFactories.add(new WalletTransactionFragmentFactory());
        fragmentFactories.add(new BudgetTransactionFragmentFactory());

        TransactionAdapter adapter = new TransactionAdapter(fragment.getChildFragmentManager());

        for (FragmentFactory fragmentFactory: fragmentFactories){
            adapter.addFrag(fragmentFactory.createProduct());
        }

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(2); // mulai dari daily transaction
    }

    public View getView(){
        return view;
    }

}