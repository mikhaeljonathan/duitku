package duitku.project.se.transaction.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import duitku.project.se.R;
import duitku.project.se.budget.fragment.BudgetFragment;
import duitku.project.se.interfaces.UIView;
import duitku.project.se.search.SearchActivity;
import duitku.project.se.transaction.daily.DailyTransactionFragment;
import duitku.project.se.transaction.monthly.MonthlyTransactionFragment;
import duitku.project.se.transaction.weekly.WeeklyTransactionFragment;
import duitku.project.se.wallet.fragment.WalletFragment;

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
