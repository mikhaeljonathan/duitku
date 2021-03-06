package duitku.project.se.report;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import duitku.project.se.R;
import duitku.project.se.category.Category;
import duitku.project.se.category.CategoryController;
import duitku.project.se.database.DuitkuContract.CategoryEntry;
import duitku.project.se.transaction.Transaction;
import duitku.project.se.wallet.Wallet;
import duitku.project.se.wallet.WalletController;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

public class ReportExpandableAdapter extends BaseExpandableListAdapter {

    private final List<Report> reportList;
    private final HashMap<Report, List<Transaction>> incomeReportListHashMap;
    private final Context context;
    private final String type;

    public ReportExpandableAdapter(List<Report> reportList,
                                   HashMap<Report, List<Transaction>> incomeReportListHashMap,
                                   Context context,
                                   String type) {
        this.reportList = reportList;
        this.incomeReportListHashMap = incomeReportListHashMap;
        this.context = context;
        this.type = type;
    }

    @Override
    public int getGroupCount() {
        return reportList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return incomeReportListHashMap.get(reportList.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return reportList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return incomeReportListHashMap.get(reportList.get(i)).get(i1);
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
        Report report = (Report) getGroup(i);

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_list_report, viewGroup, false);
        }

        View hidden = view.findViewById(R.id.hidden_view_report);
        ConstraintLayout cl = view.findViewById(R.id.item_list_report_constraintlayout);
        ImageView image = view.findViewById(R.id.arrow_expandable_report);

        TextView categoryName = view.findViewById(R.id.item_list_report_name);
        Category category = new CategoryController(context).getCategoryById(report.getCategoryId());
        categoryName.setText(category.getCategory_name());

        TextView amount = view.findViewById(R.id.item_list_report_total);
        amount.setText(new DecimalFormat("###,###").format(report.getAmount()));

        TextView percentage = view.findViewById(R.id.item_list_report_percentage);
        percentage.setText(String.format("%.2f", report.getPercentage()) + "%");

        if (!b) {
            image.setImageResource(R.drawable.icon_arrow_up);
            cl.setBackgroundResource(R.drawable.custom_shape);
            hidden.setVisibility(View.VISIBLE);
        } else {
            image.setImageResource(R.drawable.icon_arrow_down);
            cl.setBackgroundResource(R.drawable.custom_shape_top_rounded);
            hidden.setVisibility(View.GONE);
        }

        // handle grup terakhir supaya gakeluar hidden view nya
        if (i == (reportList.size() - 1)) {
            hidden.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        Transaction transaction = (Transaction) getChild(i, i1);

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_list_transaction, viewGroup, false);
        }

        ConstraintLayout cl = view.findViewById(R.id.item_list_transaction_constraintlayout);
        View hidden = view.findViewById(R.id.hidden_view_transaction);

        setUpIcon(view);
        setUpHeader(view, transaction);

        TextView descTextView = view.findViewById(R.id.item_list_transaction_desc_textview);
        descTextView.setText(transaction.getTransaction_desc());

        TextView amountTextView = view.findViewById(R.id.item_list_transaction_amount_textview);
        amountTextView.setText(new DecimalFormat("###,###").format(transaction.getTransaction_amount()));

        if (b) {
            cl.setBackgroundResource(R.drawable.custom_shape_bottom_rounded);
            hidden.setBackgroundResource(R.color.colorPrimary);
        } else {
            cl.setBackgroundResource(R.color.colorPrimaryDark);
            hidden.setBackgroundResource(R.color.colorPrimaryDark);
        }
        hidden.setVisibility(View.VISIBLE);

        return view;
    }

    private void setUpIcon(View view) {
        ImageView categoryImageView = view.findViewById(R.id.item_list_transaction_categorytype_icon);
        if (type.equals(CategoryEntry.TYPE_EXPENSE)) {
            categoryImageView.setImageResource(R.drawable.icon_expense);
        } else {
            categoryImageView.setImageResource(R.drawable.icon_income);
        }
    }

    private void setUpHeader(View view, Transaction transaction) {
        TextView headerTextView = view.findViewById(R.id.item_list_transaction_header_textview);
        ImageView transferIcon = view.findViewById(R.id.item_list_transaction_transfer_imageview);
        TextView walletDestTextView = view.findViewById(R.id.item_list_transaction_walletdest_textview);

        Wallet wallet = new WalletController(context).getWalletById(transaction.getWallet_id());
        headerTextView.setText(wallet.getWallet_name());
        transferIcon.setVisibility(View.GONE);
        walletDestTextView.setVisibility(View.GONE);
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

}
