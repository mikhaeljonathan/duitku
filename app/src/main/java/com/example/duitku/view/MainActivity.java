package com.example.duitku.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.duitku.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    FloatingActionButton addTransactionFab;

    // onCreate itu fungsi bawaan dari AppCompatActivity,
    // dijalankan kalo MainActivity ini ditampilkan
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // saat activity dijalanin, dia bakal inflate layout activity_main
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Custom toolbar sendiri
//        setSupportActionBar((Toolbar) findViewById(R.id.main_toolbar));
//        getSupportActionBar().setDisplayShowTitleEnabled(false); // Ga usah judul, diganti sama custom toolbar

        // Setting BottomNavBar
        bottomNav = findViewById(R.id.main_bottomnavbar);
        bottomNav.getMenu().getItem(2).setEnabled(false); // supaya yang di tengah2 (placeholder) gabisa diklik
        // Disini object Fragment dibuat, Fragment itu kayak mini Activity
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment = null;

                switch (menuItem.getItemId()){
                    case R.id.nav_transaction:
                        selectedFragment = new TransactionFragment();
                        break;
                    case R.id.nav_report:
                        selectedFragment = new ReportFragment();
                        break;
                    case R.id.nav_article:
                        selectedFragment = new ArticleFragment();
                        break;
                    case R.id.nav_account:
                        selectedFragment = new AccountFragment();
                        break;
                }

                // Tampilin fragment dari setiap menu nya
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, selectedFragment).commit();
                return true;
            }

        });

        // Default fragment, kalau kita blm teken menu apa2 di bottomnavbar
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new TransactionFragment()).commit();

        // Setting floating action button
        addTransactionFab = findViewById(R.id.main_add_fab);
        addTransactionFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent addTransactionIntent = new Intent(MainActivity.this, AddTransactionActivity.class);
                startActivity(addTransactionIntent);
            }

        });

    }

}