package duitku.project.se.report.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import duitku.project.se.R;
import duitku.project.se.database.DuitkuContract.CategoryEntry;
import duitku.project.se.interfaces.UIView;
import duitku.project.se.report.ReportContentFragment;
import com.google.android.material.tabs.TabLayout;

public class ReportFragmentView implements UIView {

    private final LayoutInflater inflater;
    private final ViewGroup container;
    private View view;
    private final Fragment fragment;

    public ReportFragmentView(LayoutInflater inflater, ViewGroup container, Fragment fragment) {
        this.inflater = inflater;
        this.container = container;
        this.fragment = fragment;
    }

    @Override
    public void setUpUI() {
        this.view = inflater.inflate(R.layout.fragment_report, container, false);
        setUpViewPager();
    }

    private void setUpViewPager() {
        ViewPager viewPager = view.findViewById(R.id.report_viewPager);

        ReportPagerAdapter adapter = new ReportPagerAdapter(fragment.getChildFragmentManager());

        adapter.addFrag(new ReportContentFragment(CategoryEntry.TYPE_INCOME), "Income");
        adapter.addFrag(new ReportContentFragment(CategoryEntry.TYPE_EXPENSE), "Expense");

        viewPager.setAdapter(adapter);
        setUpTabLayout(viewPager);
    }

    private void setUpTabLayout(ViewPager viewPager) {
        TabLayout tabLayout = view.findViewById(R.id.report_tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public View getView() {
        return view;
    }
}
