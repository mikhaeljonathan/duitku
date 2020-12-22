package com.example.duitku.flows;

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
import com.example.duitku.model.CategoryTransaction;
import com.example.duitku.model.IncomeReport;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReportIncomeFragment extends Fragment {

    ExpandableListView incomeExpandableListView;
    IncomeExpandableAdapter incomeExpandableAdapter;
    List<IncomeReport> incomeReportList;
    HashMap<IncomeReport, List<CategoryTransaction>> incomeReportListHashMap;
    PieChart pieChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_report_income, container, false);
        View header = inflater.inflate(R.layout.fragment_report_income_header, null);

        pieChart = header.findViewById(R.id.piechart_income);
        ArrayList<PieEntry> income = new ArrayList<>();
        income.add(new PieEntry(50, "gaji"));
        income.add(new PieEntry(20, "hadiah"));
        income.add(new PieEntry(10, "jajan"));
        income.add(new PieEntry(20, "nemu"));

        PieDataSet pieDataSet = new PieDataSet(income, "Income");
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

        incomeExpandableListView = rootView.findViewById(R.id.report_income_expandablelistview);
        incomeExpandableListView.addHeaderView(header);
        setContent();

        incomeExpandableAdapter = new IncomeExpandableAdapter();
        incomeExpandableListView.setAdapter(incomeExpandableAdapter);

        return rootView;
    }

    private void setContent(){

        incomeReportList = new ArrayList<>();
        incomeReportListHashMap = new HashMap<>();

        incomeReportList.add(new IncomeReport("Nemu", "Rp 2.000.000", "20%"));
        incomeReportList.add(new IncomeReport("Gaji", "Rp 5.000.000", "50%"));
        incomeReportList.add(new IncomeReport("Hadiah", "Rp 2.000.000", "20%"));
        incomeReportList.add(new IncomeReport("Jajan", "Rp 1.000.000", "10%"));

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

        incomeReportListHashMap.put(incomeReportList.get(0), categoryTransaction1);
        incomeReportListHashMap.put(incomeReportList.get(1), categoryTransaction2);
        incomeReportListHashMap.put(incomeReportList.get(2), categoryTransaction3);
        incomeReportListHashMap.put(incomeReportList.get(3), categoryTransaction4);
    }

    class IncomeExpandableAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return incomeReportList.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return incomeReportListHashMap.get(incomeReportList.get(i)).size();
        }

        @Override
        public Object getGroup(int i) {
            return incomeReportList.get(i);
        }

        @Override
        public Object getChild(int i, int i1) {
            return incomeReportListHashMap.get(incomeReportList.get(i)).get(i1);
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
            IncomeReport incomeReport = (IncomeReport) getGroup(i);

            if (view == null){
                view = getLayoutInflater().from(getContext()).inflate(R.layout.item_list_report_income, viewGroup, false);
            }

            TextView categoryName = view.findViewById(R.id.item_list_category_income_name);
            categoryName.setText(incomeReport.getmCategoryId());

            TextView amount = view.findViewById(R.id.item_list_category_income_total);
            amount.setText(incomeReport.getmAmount());

            TextView percentage = view.findViewById(R.id.item_list_category_income_percentage);
            percentage.setText(incomeReport.getmPercentage());

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