package com.example.duitku.transaction.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.duitku.R;
import com.example.duitku.budget.view.ViewBudgetActivity;
import com.example.duitku.category.Category;
import com.example.duitku.category.CategoryController;
import com.example.duitku.database.DuitkuContract.CategoryEntry;
import com.example.duitku.main.Utility;
import com.example.duitku.transaction.Transaction;
import com.example.duitku.transaction.TransactionController;
import com.example.duitku.transaction.edit.EditTransactionActivity;
import com.example.duitku.wallet.Wallet;
import com.example.duitku.wallet.WalletController;

import java.text.DecimalFormat;

public class ViewTransactionDialog extends AppCompatDialogFragment {

    private final long transactionId;
    private Transaction transaction;
    private Category category;
    private String categoryType;

    private Dialog dialog;

    private View view;

    public ViewTransactionDialog(long transactionId){
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
        if (transaction == null){
            dismiss();
            return;
        }

        initializeCategory();
        setUpUI();
    }

    private void initializeCategory(){
        CategoryController categoryController = new CategoryController(getActivity());
        category = categoryController.getCategoryById(transaction.getCategoryId());
        if (category != null){
            categoryType = category.getType();
        } else{
            categoryType = CategoryEntry.TYPE_TRANSFER;
        }
    }

    private void setUpUI(){
        setUpDateTitle();
        setUpAmount();
        setUpCategory();
        setUpWallet();
        setUpDesc();
        setUpEditBtn();
    }

    private void setUpDateTitle(){
        TextView dateTextView = view.findViewById(R.id.view_transaction_date_textview);
        dateTextView.setText(Utility.convertDateToFullString(transaction.getDate()));
    }

    private void setUpAmount(){
        ImageView categoryTypeIcon = view.findViewById(R.id.view_transaction_categorytype_icon);
        if (categoryType.equals(CategoryEntry.TYPE_EXPENSE)){
            categoryTypeIcon.setImageResource(R.drawable.icon_expense);
        } else if (categoryType.equals(CategoryEntry.TYPE_INCOME)){
            categoryTypeIcon.setImageResource(R.drawable.icon_income);
        } else {
            categoryTypeIcon.setImageResource(R.drawable.icon_transfer);
        }

        TextView amountTextView = view.findViewById(R.id.view_transaction_amount_textview);
        amountTextView.setText(new DecimalFormat("###,###").format(transaction.getAmount()));
    }

    private void setUpCategory(){
        ImageView categoryImageView = view.findViewById(R.id.view_transaction_category_imageview);
        TextView categoryTextView = view.findViewById(R.id.view_transaction_category_textview);
        if (categoryType.equals(CategoryEntry.TYPE_TRANSFER)){
            categoryImageView.setVisibility(View.GONE);
            categoryTextView.setVisibility(View.GONE);
            return;
        }
        categoryTextView.setText(category.getName());
    }

    private void setUpWallet(){
        WalletController walletController = new WalletController(getActivity());

        TextView walletTextView = view.findViewById(R.id.view_transaction_wallet_textview);
        Wallet wallet = walletController.getWalletById(transaction.getWalletId());
        walletTextView.setText(wallet.getName());

        ImageView walletDestImageView = view.findViewById(R.id.view_transaction_walletdest_imageview);
        TextView walletDestTextView = view.findViewById(R.id.view_transaction_walletdest_textview);
        if (!categoryType.equals(CategoryEntry.TYPE_TRANSFER)){
            walletDestImageView.setVisibility(View.GONE);
            walletDestTextView.setVisibility(View.GONE);
            return;
        }
        Wallet walletDest = walletController.getWalletById(transaction.getWalletDestId());
        walletDestTextView.setText(walletDest.getName());
    }

    private void setUpDesc(){
        TextView descTextView = view.findViewById(R.id.view_transaction_desc_textview);
        descTextView.setText(transaction.getDesc());
        if (transaction.getDesc().isEmpty()){
            descTextView.setVisibility(View.GONE);
        } else {
            descTextView.setVisibility(View.VISIBLE);
        }
    }

    private void setUpEditBtn(){
        ImageView editBtn = view.findViewById(R.id.view_transaction_edit_btn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editTransactionIntent = new Intent(getActivity(), EditTransactionActivity.class);
                editTransactionIntent.putExtra("ID", transaction.getId());
                getActivity().startActivity(editTransactionIntent);
            }
        });
    }

}
