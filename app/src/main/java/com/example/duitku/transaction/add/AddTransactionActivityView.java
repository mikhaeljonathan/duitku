package com.example.duitku.transaction.add;

import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.duitku.R;
import com.example.duitku.database.DuitkuContract.CategoryEntry;
import com.example.duitku.interfaces.UIView;
import com.google.android.material.tabs.TabLayout;

public class AddTransactionActivityView implements UIView {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageButton backBtn;
    private AddTransactionViewPagerAdapter adapter;

    private AppCompatActivity activity;

    public AddTransactionActivityView(AppCompatActivity activity){
        this.activity = activity;
    }

    @Override
    public void setUpUI() {
        activity.setContentView(R.layout.activity_add_transaction);
        setUpAdapter();
        setUpViewPager();
        setUpTabLayout();
        setUpButtons();
    }

    private void setUpAdapter(){
        adapter = new AddTransactionViewPagerAdapter(activity.getSupportFragmentManager());
        adapter.addFrag(new AddTransactionFragment(CategoryEntry.TYPE_INCOME), "Income");
        adapter.addFrag(new AddTransactionFragment(CategoryEntry.TYPE_EXPENSE), "Expense");
        adapter.addFrag(new AddTransactionFragment(CategoryEntry.TYPE_TRANSFER), "Transfer");
    }

    private void setUpViewPager(){
        viewPager = activity.findViewById(R.id.add_transaction_viewpager);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1); // biar langsung di tengah2 (expense)
    }

    private void setUpTabLayout(){
        tabLayout = activity.findViewById(R.id.add_transaction_tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setUpButtons(){
        backBtn = activity.findViewById(R.id.add_transaction_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
    }

    @Override
    public View getView() {
        return null;
    }
}
