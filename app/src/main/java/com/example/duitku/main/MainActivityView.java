package com.example.duitku.main;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.duitku.R;
import com.example.duitku.account.AccountFragment;
import com.example.duitku.article.ArticleFragment;
import com.example.duitku.report.fragment.ReportFragment;
import com.example.duitku.transaction.add.AddTransactionActivity;
import com.example.duitku.interfaces.UIView;
import com.example.duitku.transaction.fragment.TransactionFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivityView implements UIView {

    private final AppCompatActivity activity;

    public MainActivityView(AppCompatActivity activity){
        this.activity = activity;
    }

    @Override
    public void setUpUI() {
        activity.setContentView(R.layout.activity_main);
        setUpBottomNavBar();
        setUpFloatingActionButton();
    }

    private void setUpBottomNavBar(){
        final BottomNavigationView bottomNavBar = activity.findViewById(R.id.main_bottomnavbar);
        bottomNavBar.getMenu().getItem(2).setEnabled(false); // yang di tengah2 gabisa diklik

        bottomNavBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // biar kalau diklik ga kerefresh
                if (bottomNavBar.getSelectedItemId() == menuItem.getItemId()) return true;
                Fragment fragment = null;
                switch (menuItem.getItemId()){
                    case R.id.nav_transaction:
                        fragment = new TransactionFragment();
                        break;
                    case R.id.nav_report:
                        fragment = new ReportFragment();
                        break;
                    case R.id.nav_article:
                        fragment = new ArticleFragment();
                        break;
                    case R.id.nav_account:
                        fragment = new AccountFragment();
                        break;
                }
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();
                return true;
            }

        });
        // Default fragment, kalau kita blm teken menu apa2 di bottomnavbar
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new TransactionFragment()).commit();
    }

    private void setUpFloatingActionButton(){
        FloatingActionButton addTransactionFab = activity.findViewById(R.id.main_add_fab);
        addTransactionFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent addTransactionIntent = new Intent(activity, AddTransactionActivity.class);
                activity.startActivity(addTransactionIntent);
            }

        });
    }


    @Override
    public View getView() {
        return activity.findViewById(R.id.main_coordlayout);
    }

}
