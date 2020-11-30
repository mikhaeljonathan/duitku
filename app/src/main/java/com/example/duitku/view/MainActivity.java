package com.example.duitku.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.duitku.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    // onCreate itu fungsi bawaan dari AppCompatActivity,
    // dijalankan kalo MainActivity ini ditampilkan
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Custom toolbar sendiri
        setSupportActionBar((Toolbar) findViewById(R.id.myToolbar));
        getSupportActionBar().setDisplayShowTitleEnabled(false); // Ga usah judul, diganti sama custom toolbar

        // Setting BottomNavBar
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_bar);
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

    }

    // Buat nampilin tombol search di toolbar atas
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}