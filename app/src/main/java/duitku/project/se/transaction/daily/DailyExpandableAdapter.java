package duitku.project.se.transaction.daily;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import duitku.project.se.R;
import duitku.project.se.category.CategoryController;
import duitku.project.se.wallet.WalletController;
import duitku.project.se.database.DuitkuContract.CategoryEntry;
import duitku.project.se.category.Category;
import duitku.project.se.transaction.Transaction;
import duitku.project.se.wallet.Wallet;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

public class DailyExpandableAdapter extends BaseExpandableListAdapter {

    private final List<DailyTransaction> dailyTransactionList;
    private final HashMap<DailyTransaction, List<Transaction>> dailyTransactionListHashMap;
    private final Context context;

    public DailyExpandableAdapter(List<DailyTransaction> dailyTransactionList,
                                  HashMap<DailyTransaction, List<Transaction>> dailyTransactionListHashMap,
                                  Context context) {
        this.dailyTransactionList = dailyTransactionList;
        this.dailyTransactionListHashMap = dailyTransactionListHashMap;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return dailyTransactionList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return dailyTransactionListHashMap.get(dailyTransactionList.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return dailyTransactionList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return dailyTransactionListHashMap.get(dailyTransactionList.get(i)).get(i1);
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
        DailyTransaction dailyTransaction = (DailyTransaction) getGroup(i);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_list_transaction_daily, viewGroup, false);
        }

        View hidden = view.findViewById(R.id.view_hidden2);
        ConstraintLayout cl = view.findViewById(R.id.item_list_transaction_daily_constraintlayout);
        ImageView image = view.findViewById(R.id.arrow_expandable);

        TextView dateTextView = view.findViewById(R.id.item_list_transaction_daily_date_textview);
        dateTextView.setText(Integer.toString(dailyTransaction.getDate()));

        TextView dayTextView = view.findViewById(R.id.item_list_transaction_daily_day_textview);
        dayTextView.setText(dailyTransaction.getDay());

        TextView incomeTextView = view.findViewById(R.id.item_list_transaction_daily_income_textview);
        incomeTextView.setText(new DecimalFormat("###,###").format(dailyTransaction.getIncome()));

        TextView expenseTextView = view.findViewById(R.id.item_list_transaction_daily_expense_textview);
        expenseTextView.setText(new DecimalFormat("###,###").format(dailyTransaction.getExpense()));
        new DecimalFormat("#0,000.00");

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
        if (i == (dailyTransactionList.size() - 1)) {
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

        // get the category
        CategoryController categoryController = new CategoryController(context);
        Category category = categoryController.getCategoryById(transaction.getCategory_id());

        setUpIcon(view, category);
        setupHeader(view, category, transaction);

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

    private void setUpIcon(View view, Category category) {
        ImageView categoryImageView = view.findViewById(R.id.item_list_transaction_categorytype_icon);

        if (category == null) {
            categoryImageView.setImageResource(R.drawable.icon_transfer);
            return;
        }

        String type = category.getCategory_type();
        if (type.equals(CategoryEntry.TYPE_INCOME)) {
            categoryImageView.setImageResource(R.drawable.icon_income);
        } else {
            categoryImageView.setImageResource(R.drawable.icon_expense);
        }
    }

    private void setupHeader(View view, Category category, Transaction transaction) {
        TextView headerTextView = view.findViewById(R.id.item_list_transaction_header_textview);
        ImageView transferImageView = view.findViewById(R.id.item_list_transaction_transfer_imageview);
        TextView walletDestTextView = view.findViewById(R.id.item_list_transaction_walletdest_textview);

        // the default is gone
        transferImageView.setVisibility(View.GONE);
        walletDestTextView.setVisibility(View.GONE);

        if (category == null) {

            transferImageView.setVisibility(View.VISIBLE);
            walletDestTextView.setVisibility(View.VISIBLE);

            WalletController walletController = new WalletController(context);
            long walletId = transaction.getWallet_id();
            long walletDestId = transaction.getWalletdest_id();

            Wallet walletSource = walletController.getWalletById(walletId);
            headerTextView.setText(walletSource.getWallet_name());

            Wallet walletDest = walletController.getWalletById(walletDestId);
            walletDestTextView.setText(walletDest.getWallet_name());

        } else {
            headerTextView.setText(category.getCategory_name());
        }

    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

}