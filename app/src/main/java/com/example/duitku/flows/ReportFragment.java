package com.example.duitku.flows;

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

public class ReportFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ReportAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // tabLayout sama viewPager itu saling berhubungan gaes
        // tablayout itu picker yang di atas, viewpager itu placeholder buat fragmentnya
        tabLayout = view.findViewById(R.id.report_tabLayout);
        viewPager = view.findViewById(R.id.report_viewPager);

        // nah, viewPager itu butuh adapter buat ganti2 fragmentnya
        // di bawah ini buat nambahin fragment ke adapter
        adapter = new ReportFragment.ReportAdapter(getChildFragmentManager());
        adapter.addFrag(new ReportIncomeFragment(), "Income");
        adapter.addFrag(new ReportExpenseFragment(), "Expense");

        // set adapter ke viewPager, dan hubungin viewPager sama tabLayout
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }


    private class ReportAdapter extends FragmentPagerAdapter{

        // kumpulan fragment dan judul fragmentnya
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ReportAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
    }
}
