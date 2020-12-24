package com.example.duitku;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.duitku.factory.fragment.AccountFragmentFactory;
import com.example.duitku.factory.fragment.ArticleFragmentFactory;
import com.example.duitku.interfaces.FragmentFactory;
import com.example.duitku.factory.fragment.ReportFragmentFactory;
import com.example.duitku.transaction.parent.TransactionFragmentFactory;
import com.example.duitku.transaction.add.AddTransactionActivity;
import com.example.duitku.interfaces.UIView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivityView implements UIView {

    private AppCompatActivity activity;

    private BottomNavigationView bottomNavBar;
    private FloatingActionButton addTransactionFab;

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
        bottomNavBar = activity.findViewById(R.id.main_bottomnavbar);
        bottomNavBar.getMenu().getItem(2).setEnabled(false); // yang di tengah2 gabisa diklik
        bottomNavBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // biar kalau diklik ga kerefresh
                if (bottomNavBar.getSelectedItemId() == menuItem.getItemId()) return true;
                FragmentFactory fragmentFactory = null;
                switch (menuItem.getItemId()){
                    case R.id.nav_transaction:
                        fragmentFactory = new TransactionFragmentFactory();
                        break;
                    case R.id.nav_report:
                        fragmentFactory = new ReportFragmentFactory();
                        break;
                    case R.id.nav_article:
                        fragmentFactory = new ArticleFragmentFactory();
                        break;
                    case R.id.nav_account:
                        fragmentFactory = new AccountFragmentFactory();
                        break;
                }
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragmentFactory.createProduct()).commit();
                return true;
            }

        });
        // Default fragment, kalau kita blm teken menu apa2 di bottomnavbar
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new TransactionFragmentFactory().createProduct()).commit();
    }

    private void setUpFloatingActionButton(){
        addTransactionFab = activity.findViewById(R.id.main_add_fab);
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
        return null;
    }

}
