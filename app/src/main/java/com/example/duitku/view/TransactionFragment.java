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

    // ini dijelasin di bawah, jgn takut
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TransactionAdapter adapter;

    // onCreateView dan onViewCreated itu sama kayak onCreate di Activity

    // Perbedaan onCreateView dan onViewCreated
    // onCreateView: inflate layout dari XML aja, jgn findViewById di sini
    // onViewCreated: ini dipanggil saat view bener2 dibuat,
    // jadi aman untuk panggil findViewById

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // tampilin fragment_transaction.xml di activity sekarang yang manggil TransactionFragment,
        // which is MainActivity
        return inflater.inflate(R.layout.fragment_transaction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // tabLayout sama viewPager itu saling berhubungan gaes
        // tablayout itu picker yang di atas, viewpager itu placeholder buat fragmentnya
        tabLayout = view.findViewById(R.id.transaction_tabLayout);
        viewPager = view.findViewById(R.id.transaction_viewPager);

        // nah, viewPager itu butuh adapter buat ganti2 fragmentnya
        // di bawah ini buat nambahin fragment ke adapter
        adapter = new TransactionAdapter(getChildFragmentManager());
        adapter.addFrag(new DailyTransactionFragment(), "Daily");
        adapter.addFrag(new WeeklyTransactionFragment(), "Weekly");
        adapter.addFrag(new MonthlyTransactionFragment(), "Monthly");
        adapter.addFrag(new OthersTransactionFragment(), "Others");

        // set adapter ke viewPager, dan hubungin viewPager sama tabLayout
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    // ini inner class buat adapternya, class ini harus subclassnya FragmentPagerAdapter
    // dibuat inner class karena ga digunain class lain selain TransactionFragment ini
    private class TransactionAdapter extends FragmentPagerAdapter {

        // kumpulan fragment dan judul fragmentnya
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        // constructornya, ga perlu tau fragmentManager ini gpp
        public TransactionAdapter(FragmentManager fm){
            super(fm, (BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));
        }

        // di sini return fragment yang akan ditampilin
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        // jumlah fragment nya berapa sih (ada 4 yaitu daily, weekly, monthly, others)
        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        // ini buat judul yg akan ditampilin di tabLayout
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        // method buatan kita sendiri buat nambahin ke list
        public void addFrag(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

    }

}
