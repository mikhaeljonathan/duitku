package duitku.project.se.transaction.add;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import duitku.project.se.R;
import duitku.project.se.database.DuitkuContract.CategoryEntry;
import duitku.project.se.interfaces.UIView;
import com.google.android.material.tabs.TabLayout;

public class AddTransactionActivityView implements UIView {

    private ViewPager viewPager;
    private AddTransactionViewPagerAdapter adapter;

    private final AppCompatActivity activity;

    public AddTransactionActivityView(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public void setUpUI() {
        activity.setContentView(R.layout.activity_add_transaction);
        setUpAdapter();
        setUpViewPager();
        setUpTabLayout();
        setUpButtons();
    }

    private void setUpAdapter() {
        adapter = new AddTransactionViewPagerAdapter(activity.getSupportFragmentManager());
        adapter.addFrag(new AddTransactionFragment(CategoryEntry.TYPE_INCOME), "Income");
        adapter.addFrag(new AddTransactionFragment(CategoryEntry.TYPE_EXPENSE), "Expense");
        adapter.addFrag(new AddTransactionFragment(CategoryEntry.TYPE_TRANSFER), "Transfer");
    }

    private void setUpViewPager() {
        viewPager = activity.findViewById(R.id.add_transaction_viewpager);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1); // biar langsung di tengah2 (expense)
    }

    private void setUpTabLayout() {
        TabLayout tabLayout = activity.findViewById(R.id.add_transaction_tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setUpButtons() {
        ImageButton backBtn = activity.findViewById(R.id.add_transaction_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
    }

    @Override
    public View getView() {
        return activity.getLayoutInflater().inflate(R.layout.activity_add_transaction,
                (ViewGroup) activity.findViewById(R.id.add_transaction_constraintlayout));
    }
}
