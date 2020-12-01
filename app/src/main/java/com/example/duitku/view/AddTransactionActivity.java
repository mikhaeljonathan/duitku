package com.example.duitku.view;

import com.example.duitku.controller.TransactionController;
import com.example.duitku.database.DuitkuContract.TransactionEntry;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.duitku.R;
import com.google.android.material.tabs.TabLayout;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddTransactionActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AddTransactionAdapter adapter;
    private ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        // Initialize the views
        tabLayout = findViewById(R.id.add_transaction_tablayout);
        viewPager = findViewById(R.id.add_transaction_viewpager);
        backBtn = findViewById(R.id.add_transaction_back_btn);

        // buat adapter, viewpager, sama tablayout nya
        adapter = new AddTransactionAdapter(getSupportFragmentManager());
        adapter.addFrag(new AddTransactionIncomeFragment(), "Income");
        adapter.addFrag(new AddTransactionExpenseFragment(), "Expense");
        adapter.addFrag(new AddTransactionTransferFragment(), "Transfer");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        // ini buat back button nya
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private class AddTransactionAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public AddTransactionAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        public void addFrag(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

    }

}