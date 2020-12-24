package com.example.duitku.report;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.duitku.R;
import com.example.duitku.category.CategoryTransaction;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReportExpenseFragment extends Fragment {

    ExpandableListView expenseExpandableListView;
    ExpenseExpandableAdapter expenseExpandableAdapter;
    List<ExpenseReport> expenseReportList;
    HashMap<ExpenseReport, List<CategoryTransaction>> expenseReportListHashMap;
    PieChart pieChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_report_expense, container, false);
        View header = inflater.inflate(R.layout.fragment_report_expense_header, null);

        pieChart = header.findViewById(R.id.piechart_expense);
        ArrayList<PieEntry> expense = new ArrayList<>();
        expense.add(new PieEntry(40, "makan"));
        expense.add(new PieEntry(17, "bensin"));
        expense.add(new PieEntry(20, "jajan"));
        expense.add(new PieEntry(23, "sedekah"));

        PieDataSet pieDataSet = new PieDataSet(expense, "Expense");
        pieDataSet.setColors(ColorTemplate.PASTEL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(12);

        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(4);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(5);
        pieChart.animate();

        expenseExpandableListView = rootView.findViewById(R.id.report_expense_expandablelistview);
        expenseExpandableListView.addHeaderView(header);
        setContent();

        expenseExpandableAdapter = new ExpenseExpandableAdapter();
        expenseExpandableListView.setAdapter(expenseExpandableAdapter);

        return rootView;
    }

    private void setContent(){

        expenseReportList = new ArrayList<>();
        expenseReportListHashMap = new HashMap<>();

        expenseReportList.add(new ExpenseReport("Bensin", "Rp 1.700.000", "17%"));
        expenseReportList.add(new ExpenseReport("Makan", "Rp 4.000.000", "40%"));
        expenseReportList.add(new ExpenseReport("Sedekah", "Rp 2.300.000", "23%"));
        expenseReportList.add(new ExpenseReport("Jajan", "Rp 2.000.000", "20%"));

        ArrayList<String> temp = new ArrayList<String>();
        temp.add("day1");
        temp.add("day2");
        temp.add("day3");
        temp.add("day4");

        List<CategoryTransaction> categoryTransaction1 = new ArrayList<>();
        List<CategoryTransaction> categoryTransaction2 = new ArrayList<>();
        List<CategoryTransaction> categoryTransaction3 = new ArrayList<>();
        List<CategoryTransaction> categoryTransaction4 = new ArrayList<>();

        for (int i = 0; i < temp.size();i++){
            categoryTransaction1.add(new CategoryTransaction(1, 1000));
            categoryTransaction2.add(new CategoryTransaction(2, 2000));
            categoryTransaction3.add(new CategoryTransaction(3, 3000));
            categoryTransaction4.add(new CategoryTransaction(4, 4000));
        }

        expenseReportListHashMap.put(expenseReportList.get(0), categoryTransaction1);
        expenseReportListHashMap.put(expenseReportList.get(1), categoryTransaction2);
        expenseReportListHashMap.put(expenseReportList.get(2), categoryTransaction3);
        expenseReportListHashMap.put(expenseReportList.get(3), categoryTransaction4);
    }

    class ExpenseExpandableAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return expenseReportList.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return expenseReportListHashMap.get(expenseReportList.get(i)).size();
        }

        @Override
        public Object getGroup(int i) {
            return expenseReportList.get(i);
        }

        @Override
        public Object getChild(int i, int i1) {
            return expenseReportListHashMap.get(expenseReportList.get(i)).get(i1);
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            ExpenseReport expenseReport = (ExpenseReport) getGroup(i);

            if (view == null){
                view = getLayoutInflater().from(getContext()).inflate(R.layout.item_list_report_expense, viewGroup, false);
            }

            TextView categoryName = view.findViewById(R.id.item_list_category_expense_name);
            categoryName.setText(expenseReport.getmCategoryId());

            TextView amount = view.findViewById(R.id.item_list_category_expense_total);
            amount.setText(expenseReport.getmAmount());

            TextView percentage = view.findViewById(R.id.item_list_category_expense_percentage);
            percentage.setText(expenseReport.getmPercentage());

            return view;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            CategoryTransaction categoryTransaction = (CategoryTransaction) getChild(i, i1);

            if (view == null){
                view = LayoutInflater.from(getContext()).inflate(R.layout.item_list_transaction_category, viewGroup, false);
            }

            TextView categoryName = view.findViewById(R.id.item_list_transaction_category_name_textview);
            categoryName.setText(Long.toString(categoryTransaction.getCategoryId()));

            TextView amount = view.findViewById(R.id.item_list_transaction_category_amount_textview);
            amount.setText(Double.toString(categoryTransaction.getAmount()));

            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }
}