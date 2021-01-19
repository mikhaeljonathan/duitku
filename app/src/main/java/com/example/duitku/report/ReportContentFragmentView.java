package com.example.duitku.report;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.duitku.R;
import com.example.duitku.category.Category;
import com.example.duitku.category.CategoryController;
import com.example.duitku.database.DuitkuContract.CategoryEntry;
import com.example.duitku.interfaces.UIView;
import com.example.duitku.main.Utility;
import com.example.duitku.transaction.Transaction;
import com.example.duitku.transaction.view.ViewTransactionDialog;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReportContentFragmentView implements UIView {

    private View header;
    private Button periodButton;
    private View emptyView;
    private ImageView imageEmptyView;

    private final LayoutInflater inflater;
    private final ViewGroup container;
    private View view;

    private ArrayList<PieEntry> pieEntries = new ArrayList<>();
    private ReportExpandableAdapter adapter;
    private ExpandableListView expandableListView;
    private PieChart pieChart;

    private final ReportContentFragment fragment;
    private final String type;

    public ReportContentFragmentView(LayoutInflater inflater, ViewGroup container, ReportContentFragment fragment, String type){
        this.inflater = inflater;
        this.container = container;
        this.fragment = fragment;
        this.type = type;
    }

    @Override
    public void setUpUI() {
        this.view = inflater.inflate(R.layout.fragment_content_report, container, false);
        setUpHeader();
        setUpListView();
        setUpEmptyView();
    }

    private void setUpHeader(){
        header = inflater.inflate(R.layout.fragment_report_header, null);

        setUpTitle();
        setUpPieChart();

        periodButton = header.findViewById(R.id.report_period_btn);
    }

    private void setUpTitle(){
        TextView title = header.findViewById(R.id.report_title);
        if (type.equals(CategoryEntry.TYPE_EXPENSE)){
            title.setText("Expense Report");
        } else {
            title.setText("Income Report");
        }
    }

    private void setUpPieChart(){
        pieChart = header.findViewById(R.id.report_piechart);

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Income");
        pieDataSet.setColors(ColorTemplate.PASTEL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(12);

        pieChart.setData(new PieData(pieDataSet));
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(4);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(5);
        pieChart.animate();
    }

    private void setUpListView(){
        expandableListView = view.findViewById(R.id.content_report_expandablelistview);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                Transaction transaction = (Transaction) adapter.getChild(i, i1);
                viewTransaction(transaction.getId());
                return true;
            }
        });

        expandableListView.addHeaderView(header, null, false);
    }

    private void setUpEmptyView(){
        emptyView = inflater.inflate(R.layout.empty_view, null, false);

        imageEmptyView = emptyView.findViewById(R.id.empty_view_imageview);
        imageEmptyView.setImageResource(R.drawable.empty_transaction);

        TextView textView = emptyView.findViewById(R.id.empty_view_textview);
        textView.setText("There is no transaction\nTry adding a new one");

        expandableListView.addFooterView(emptyView, null, false);
    }

    private void viewTransaction(long id){
        ViewTransactionDialog viewTransactionDialog = new ViewTransactionDialog(id);
        viewTransactionDialog.show(fragment.getFragmentManager(), "View Transaction Dialog");
    }

    public void updatePeriodButton(final int month, final int year){
        periodButton.setText(Utility.monthsName[month] + " " + year);
        periodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(fragment.getActivity(), AlertDialog.THEME_HOLO_DARK,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                fragment.pickMonthYear(month, year);
                            }
                        }, year, month, 1);
                datePickerDialog.getDatePicker().findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
                datePickerDialog.show();
            }
        });
    }

    public void fillListView(List<Report> reportList, HashMap<Report, List<Transaction>> reportHashMap, Context context){
        adapter = new ReportExpandableAdapter(reportList, reportHashMap, context, type);
        expandableListView.setAdapter(adapter);
        pieEntries.clear();
        for (Report report: reportList){
            Category category = new CategoryController(context).getCategoryById(report.getCategoryId());
            float percentage = (float) report.getPercentage();
            pieEntries.add(new PieEntry(percentage, category.getName()));
        }

        if (adapter.getGroupCount() == 0){
            pieChart.setVisibility(View.GONE);
            imageEmptyView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            pieChart.setVisibility(View.VISIBLE);
            imageEmptyView.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
        }

    }

    @Override
    public View getView() {
        return view;
    }
}
