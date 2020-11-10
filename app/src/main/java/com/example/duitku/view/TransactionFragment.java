package com.example.duitku.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.duitku.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class TransactionFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TransactionAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transaction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // tablayout itu picker yang di atas, viewpager itu isinya
        tabLayout = view.findViewById(R.id.transaction_tabLayout);
        viewPager = view.findViewById(R.id.transaction_viewPager);

        // nambahin fragment ke adapter. nanti adapternya ditaruh di viewpager
        adapter = new TransactionAdapter(getChildFragmentManager());
        adapter.addFrag(new DailyTransactionFragment(), "Daily");
        adapter.addFrag(new WeeklyTransactionFragment(), "Weekly");
        adapter.addFrag(new MonthlyTransactionFragment(), "Monthly");
        adapter.addFrag(new OthersTransactionFragment(), "Others");

        // set semuanya
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    // inner class buat pindah2 fragment di bagian transaction
    private class TransactionAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public TransactionAdapter(FragmentManager fm){
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
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
