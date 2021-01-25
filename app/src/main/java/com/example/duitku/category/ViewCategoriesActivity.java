package com.example.duitku.category;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.duitku.R;
import com.example.duitku.category.pick.ViewCategoriesFragment;
import com.example.duitku.database.DuitkuContract.CategoryEntry;
import com.google.android.material.tabs.TabLayout;

public class ViewCategoriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_categories);

        setUpViewPager();
    }

    private void setUpViewPager(){
        ViewPager viewPager = findViewById(R.id.view_categories_viewpager);

        ViewCategoriesAdapter adapter = new ViewCategoriesAdapter(getSupportFragmentManager());

        adapter.addFrag(new ViewCategoriesFragment(CategoryEntry.TYPE_INCOME), "Income");
        adapter.addFrag(new ViewCategoriesFragment(CategoryEntry.TYPE_EXPENSE), "Expense");

        viewPager.setAdapter(adapter);
        setUpTabLayout(viewPager);
    }

    private void setUpTabLayout(ViewPager viewPager) {
        TabLayout tabLayout = findViewById(R.id.view_categories_viewpager);
        tabLayout.setupWithViewPager(viewPager);
    }

}