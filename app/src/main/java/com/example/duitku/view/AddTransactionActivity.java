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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddTransactionActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AddTransactionAdapter adapter;
    private Button saveBtn;

    private EditText dateField;
    private EditText walletField;
    private EditText categoryField;
    private EditText amountField;
    private EditText descField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        // Initialize the views
        tabLayout = findViewById(R.id.add_transaction_tablayout);
        viewPager = findViewById(R.id.add_transaction_viewpager);
        saveBtn = findViewById(R.id.add_transaction_income_save_btn);

        dateField = findViewById(R.id.add_transaction_income_date_edittext);
        walletField = findViewById(R.id.add_transaction_income_wallet_edittext);
        categoryField = findViewById(R.id.add_transaction_income_category_edittext);
        amountField = findViewById(R.id.add_transaction_income_amount_edittext);
        descField = findViewById(R.id.add_transaction_income_desc_edittext);

        // buat adapter, viewpager, sama tablayout nya
        adapter = new AddTransactionAdapter(getSupportFragmentManager());
        adapter.addFrag(new AddTransactionIncomeFragment(), "Income");
        adapter.addFrag(new AddTransactionExpenseFragment(), "Expense");
        adapter.addFrag(new AddTransactionTransferFragment(), "Transfer");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        // ini buat save button nya
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                addTransaction();
            }
        });

    }

    private void addTransaction(){

        // ambil data dari view
        String date = dateField.getText().toString().trim();
        String wallet = walletField.getText().toString().trim();
        String category = categoryField.getText().toString().trim();
        double amount = Double.parseDouble(amountField.getText().toString().trim());
        String desc = descField.getText().toString().trim();

        // taruh di contentvalues
        ContentValues values = new ContentValues();
        values.put(TransactionEntry.COLUMN_DATE, date);
        values.put(TransactionEntry.COLUMN_WALLET_ID, wallet);
        values.put(TransactionEntry.COLUMN_CATEGORY_ID, category);
        values.put(TransactionEntry.COLUMN_AMOUNT, amount);
        values.put(TransactionEntry.COLUMN_DESC, desc);

        // panggil controller nya
        Uri result = new TransactionController().addTransaction(values);

        // hasil insert nya gimana
        if (result == null){
            Toast.makeText(this, "Error inserting new transaction", Toast.LENGTH_SHORT);
        } else {
            Toast.makeText(this, "Transaction Added", Toast.LENGTH_SHORT);
        }

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