package com.example.duitku.transaction;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.duitku.R;
import com.example.duitku.category.Category;
import com.example.duitku.category.CategoryController;
import com.example.duitku.database.DuitkuContract.CategoryEntry;
import com.example.duitku.main.Utility;
import com.example.duitku.wallet.Wallet;
import com.example.duitku.wallet.WalletController;

public class ViewTransactionDialog extends AppCompatDialogFragment {

    private long id;
    private Transaction transaction;
    private Category category;
    private String categoryType;

    public ViewTransactionDialog(long id){
        this.id = id;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        initialize();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_view_transaction, null);

        setUpUI(view);

        builder.setView(view);

        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary); //biar bg gelap
        return dialog;
    }

    private void initialize(){
        transaction = new TransactionController(getActivity()).getTransactionById(id);

        CategoryController categoryController = new CategoryController(getActivity());
        category = categoryController.getCategoryById(transaction.getCategoryId());
        if (category != null){
            categoryType = category.getType();
        } else{
            categoryType = CategoryEntry.TYPE_TRANSFER;
        }
    }

    private void setUpUI(View view){
        setUpDateTitle(view);
        setUpAmount(view);
        setUpCategory(view);
        setUpWallet(view);
        setUpDesc(view);
        setUpButton(view);
    }

    private void setUpDateTitle(View view){
        TextView dateTextView = view.findViewById(R.id.view_transaction_date_textview);
        dateTextView.setText(Utility.convertDateToFullString(transaction.getDate()));
    }

    private void setUpAmount(View view){
        ImageView categoryTypeIcon = view.findViewById(R.id.view_transaction_categorytype_icon);
        if (categoryType.equals(CategoryEntry.TYPE_EXPENSE)){
            categoryTypeIcon.setImageResource(R.drawable.icon_expense);
        } else if (categoryType.equals(CategoryEntry.TYPE_INCOME)){
            categoryTypeIcon.setImageResource(R.drawable.icon_income);
        } else {
            categoryTypeIcon.setImageResource(R.drawable.icon_transfer);
        }

        TextView amountTextView = view.findViewById(R.id.view_transaction_amount_textview);
        amountTextView.setText(Double.toString(transaction.getAmount()));
    }

    private void setUpCategory(View view){
        ImageView categoryImageView = view.findViewById(R.id.view_transaction_category_imageview);
        TextView categoryTextView = view.findViewById(R.id.view_transaction_category_textview);
        if (categoryType.equals(CategoryEntry.TYPE_TRANSFER)){
            categoryImageView.setVisibility(View.GONE);
            categoryTextView.setVisibility(View.GONE);
            return;
        }
        categoryTextView.setText(category.getName());
    }

    private void setUpWallet(View view){
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

    private void setUpDesc(View view){
        TextView descTextView = view.findViewById(R.id.view_transaction_desc_textview);
        descTextView.setText(transaction.getDesc());
        if (transaction.getDesc().isEmpty()){
            descTextView.setVisibility(View.GONE);
        }
    }

    private void setUpButton(View view){
        ImageView editBtn = view.findViewById(R.id.view_transaction_edit_btn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editTransactionIntent = new Intent(getActivity(), EditTransactionActivity.class);
                editTransactionIntent.putExtra("ID", id);
                getActivity().startActivity(editTransactionIntent);
            }
        });
    }

}
