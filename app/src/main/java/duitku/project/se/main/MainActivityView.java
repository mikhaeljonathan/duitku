package duitku.project.se.main;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import duitku.project.se.R;
import duitku.project.se.account.AccountFragment;
import duitku.project.se.article.ArticleFragment;
import duitku.project.se.report.fragment.ReportFragment;
import duitku.project.se.transaction.add.AddTransactionActivity;
import duitku.project.se.interfaces.UIView;
import duitku.project.se.transaction.fragment.TransactionFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivityView implements UIView {

    private final AppCompatActivity activity;

    public MainActivityView(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public void setUpUI() {
        activity.setContentView(R.layout.activity_main);
        setUpBottomNavBar();
        setUpFloatingActionButton();
    }

    private void setUpBottomNavBar() {
        final BottomNavigationView bottomNavBar = activity.findViewById(R.id.main_bottomnavbar);
        bottomNavBar.getMenu().getItem(2).setEnabled(false); // yang di tengah2 gabisa diklik

        bottomNavBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // biar kalau diklik ga kerefresh
                if (bottomNavBar.getSelectedItemId() == menuItem.getItemId()) return true;
                Fragment fragment = null;
                switch (menuItem.getItemId()) {
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

    private void setUpFloatingActionButton() {
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
