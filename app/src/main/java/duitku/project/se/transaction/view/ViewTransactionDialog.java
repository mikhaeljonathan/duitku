package duitku.project.se.transaction.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import duitku.project.se.R;
import duitku.project.se.category.Category;
import duitku.project.se.category.CategoryController;
import duitku.project.se.database.DuitkuContract.CategoryEntry;
import duitku.project.se.main.Utility;
import duitku.project.se.transaction.Transaction;
import duitku.project.se.transaction.TransactionController;
import duitku.project.se.transaction.edit.EditTransactionActivity;
import duitku.project.se.wallet.Wallet;
import duitku.project.se.wallet.WalletController;

import java.text.DecimalFormat;

public class ViewTransactionDialog extends AppCompatDialogFragment {

    private final long transactionId;
    private Transaction transaction;
    private Category category;
    private String categoryType;

    private Dialog dialog;

    private View view;

    public ViewTransactionDialog(long transactionId) {
        this.transactionId = transactionId;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.transaction = new TransactionController(getActivity()).getTransactionById(transactionId);
        initializeCategory();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_view_transaction,
                (ViewGroup) getActivity().findViewById(R.id.view_transaction_constraintlayout));

        setUpUI();

        builder.setView(view);

        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary); //biar bg gelap
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.transaction = new TransactionController(getActivity()).getTransactionById(transactionId);
        if (transaction == null) {
            dismiss();
            return;
        }

        initializeCategory();
        setUpUI();
    }

    private void initializeCategory() {
        CategoryController categoryController = new CategoryController(getActivity());
        category = categoryController.getCategoryById(transaction.getCategory_id());
        if (category != null) {
            categoryType = category.getCategory_type();
        } else {
            categoryType = CategoryEntry.TYPE_TRANSFER;
        }
    }

    private void setUpUI() {
        setUpDateTitle();
        setUpAmount();
        setUpCategory();
        setUpWallet();
        setUpDesc();
        setUpEditBtn();
    }

    private void setUpDateTitle() {
        TextView dateTextView = view.findViewById(R.id.view_transaction_date_textview);
        dateTextView.setText(Utility.convertDateToFullString(transaction.getTransaction_date()));
    }

    private void setUpAmount() {
        ImageView categoryTypeIcon = view.findViewById(R.id.view_transaction_categorytype_icon);
        if (categoryType.equals(CategoryEntry.TYPE_EXPENSE)) {
            categoryTypeIcon.setImageResource(R.drawable.icon_expense);
        } else if (categoryType.equals(CategoryEntry.TYPE_INCOME)) {
            categoryTypeIcon.setImageResource(R.drawable.icon_income);
        } else {
            categoryTypeIcon.setImageResource(R.drawable.icon_transfer);
        }

        TextView amountTextView = view.findViewById(R.id.view_transaction_amount_textview);
        amountTextView.setText(new DecimalFormat("###,###").format(transaction.getTransaction_amount()));
    }

    private void setUpCategory() {
        ImageView categoryImageView = view.findViewById(R.id.view_transaction_category_imageview);
        TextView categoryTextView = view.findViewById(R.id.view_transaction_category_textview);
        if (categoryType.equals(CategoryEntry.TYPE_TRANSFER)) {
            categoryImageView.setVisibility(View.GONE);
            categoryTextView.setVisibility(View.GONE);
            return;
        }
        categoryTextView.setText(category.getCategory_name());
    }

    private void setUpWallet() {
        WalletController walletController = new WalletController(getActivity());

        TextView walletTextView = view.findViewById(R.id.view_transaction_wallet_textview);
        Wallet wallet = walletController.getWalletById(transaction.getWallet_id());
        walletTextView.setText(wallet.getWallet_name());

        ImageView walletDestImageView = view.findViewById(R.id.view_transaction_walletdest_imageview);
        TextView walletDestTextView = view.findViewById(R.id.view_transaction_walletdest_textview);
        if (!categoryType.equals(CategoryEntry.TYPE_TRANSFER)) {
            walletDestImageView.setVisibility(View.GONE);
            walletDestTextView.setVisibility(View.GONE);
            return;
        }
        Wallet walletDest = walletController.getWalletById(transaction.getWalletdest_id());
        walletDestTextView.setText(walletDest.getWallet_name());
    }

    private void setUpDesc() {
        TextView descTextView = view.findViewById(R.id.view_transaction_desc_textview);
        descTextView.setText(transaction.getTransaction_desc());
        if (transaction.getTransaction_desc().isEmpty()) {
            descTextView.setVisibility(View.GONE);
        } else {
            descTextView.setVisibility(View.VISIBLE);
        }
    }

    private void setUpEditBtn() {
        ImageView editBtn = view.findViewById(R.id.view_transaction_edit_btn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editTransactionIntent = new Intent(getActivity(), EditTransactionActivity.class);
                editTransactionIntent.putExtra("ID", transaction.get_id());
                getActivity().startActivity(editTransactionIntent);
            }
        });
    }

}
