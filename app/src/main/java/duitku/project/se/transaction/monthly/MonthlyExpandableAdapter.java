package duitku.project.se.transaction.monthly;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import duitku.project.se.R;
import duitku.project.se.main.Utility;
import duitku.project.se.category.CategoryController;
import duitku.project.se.category.Category;
import duitku.project.se.transaction.category.CategoryTransaction;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

public class MonthlyExpandableAdapter extends BaseExpandableListAdapter {

    private final List<MonthlyTransaction> monthlyTransactionList;
    private final HashMap<MonthlyTransaction, List<CategoryTransaction>> categoryTransactionListHashMap;
    private final Context context;

    public MonthlyExpandableAdapter(List<MonthlyTransaction> monthlyTransactionList,
                                    HashMap<MonthlyTransaction, List<CategoryTransaction>> categoryTransactionListHashMap,
                                    Context context) {
        this.monthlyTransactionList = monthlyTransactionList;
        this.categoryTransactionListHashMap = categoryTransactionListHashMap;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return monthlyTransactionList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return categoryTransactionListHashMap.get(monthlyTransactionList.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return monthlyTransactionList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return categoryTransactionListHashMap.get(monthlyTransactionList.get(i)).get(i1);
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
        MonthlyTransaction monthlyTransaction = (MonthlyTransaction) getGroup(i);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_list_transaction_monthly, viewGroup, false);
        }

        ConstraintLayout cl = view.findViewById(R.id.item_list_transaction_monthly_constraintlayout);
        View hidden = view.findViewById(R.id.hidden_view_monthly);
        ImageView image = view.findViewById(R.id.arrow_expandable_monthly);

        TextView monthName = view.findViewById(R.id.item_list_transaction_monthly_name_textview);
        monthName.setText(Utility.monthsNameShort[monthlyTransaction.getMonth()]);

        TextView income = view.findViewById(R.id.item_list_transaction_monthly_income_textview);
        income.setText(new DecimalFormat("###,###").format(monthlyTransaction.getIncome()));

        TextView expense = view.findViewById(R.id.item_list_transaction_monthly_expense_textview);
        expense.setText(new DecimalFormat("###,###").format(monthlyTransaction.getExpense()));

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
        if (i == (monthlyTransactionList.size() - 1)) {
            hidden.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        CategoryTransaction categoryTransaction = (CategoryTransaction) getChild(i, i1);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_list_transaction_category, viewGroup, false);
        }

        ConstraintLayout cl = view.findViewById(R.id.item_list_transaction_category_constraintlayout);
        View hidden = view.findViewById(R.id.hidden_view_category);

        TextView categoryNameTextView = view.findViewById(R.id.item_list_transaction_category_name_textview);
        CategoryController categoryController = new CategoryController(context);
        Category category = categoryController.getCategoryById(categoryTransaction.getCategoryId());
        categoryNameTextView.setText(category.getCategory_name());

        TextView transactionCountTextView = view.findViewById(R.id.item_list_transaction_category_transactioncount_textview);
        transactionCountTextView.setText("There are " + categoryTransaction.getTransactions().size() + " transaction(s)");

        TextView amountTextView = view.findViewById(R.id.item_list_transaction_category_amount_textview);
        amountTextView.setText(new DecimalFormat("###,###").format(categoryTransaction.getAmount()));

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

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
