package com.example.duitku.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.duitku.R;
import com.example.duitku.controller.TransactionController;
import com.example.duitku.database.DuitkuContract;
import com.example.duitku.model.Transaction;

import org.w3c.dom.Text;

public class AddTransactionIncomeFragment extends Fragment {

    private ConstraintLayout dateConstraintLayout;
    private ConstraintLayout categoryConstraintLayout;
    private ConstraintLayout walletConstraintLayout;

    private TextView dateTextView;
    private TextView categoryTextView;
    private TextView walletTextView;
    private EditText amountField;
    private EditText descField;

    private Button saveBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_transaction_income, container, false);

        // Initialize the views
        dateConstraintLayout = rootView.findViewById(R.id.add_transaction_income_date_constraintlayout);
        categoryConstraintLayout = rootView.findViewById(R.id.add_transaction_income_category_constraintlayout);
        walletConstraintLayout = rootView.findViewById(R.id.add_transaction_income_wallet_constraintlayout);
        dateTextView = rootView.findViewById(R.id.add_transaction_income_date_textview);
        categoryTextView = rootView.findViewById(R.id.add_transaction_income_category_textview);
        walletTextView = rootView.findViewById(R.id.add_transaction_income_wallet_textview);
        amountField = rootView.findViewById(R.id.add_transaction_income_amount_edittext);
        descField = rootView.findViewById(R.id.add_transaction_income_desc_edittext);
        saveBtn = rootView.findViewById(R.id.add_transaction_income_save_btn);

        categoryConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewCategoryDialog viewCategoryDialog = new ViewCategoryDialog();
                viewCategoryDialog.show(getFragmentManager(), "View Category Dialog");
            }
        });

        // setting save button nya
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                addTransaction();
            }
        });

        return rootView;
    }

    public static class ViewCategoryDialog extends AppCompatDialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view = inflater.inflate(R.layout.dialog_view_category, null);

            builder.setView(view);
            Dialog dialog = builder.create();
            return dialog;

        }
    }

//    private void addTransaction(){
//
//        // ambil data dari view
//        String date = dateField.getText().toString().trim();
//        String wallet = walletField.getText().toString().trim();
//        String category = categoryField.getText().toString().trim();
//        double amount = Double.parseDouble(amountField.getText().toString().trim());
//        String desc = descField.getText().toString().trim();
//
//        Transaction transactionAdded = new Transaction(date, wallet, category, amount, desc);
//
//        // panggil controller nya
//        Uri uri = new TransactionController().addTransaction(transactionAdded);
//
//        // hasil insert nya gimana
//        if (uri == null){
//            Toast.makeText(getContext(), "Error inserting new transaction", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(getContext(), "Transaction Added", Toast.LENGTH_SHORT).show();
//        }
//
//    }

}
