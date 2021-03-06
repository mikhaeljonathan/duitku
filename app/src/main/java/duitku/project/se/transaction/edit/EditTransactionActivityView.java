package duitku.project.se.transaction.edit;

import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import duitku.project.se.R;
import duitku.project.se.category.Category;
import duitku.project.se.category.CategoryController;
import duitku.project.se.database.DuitkuContract.CategoryEntry;
import duitku.project.se.interfaces.UIView;
import duitku.project.se.transaction.Transaction;
import duitku.project.se.transaction.TransactionController;
import duitku.project.se.transaction.form.TransactionForm;

import java.util.Date;

public class EditTransactionActivityView implements UIView {

    private TransactionForm transactionForm;
    private Button deleteBtn;

    private final AppCompatActivity activity;

    private final TransactionController transactionController;

    private final Transaction transaction;
    private final String type;

    public EditTransactionActivityView(long id, AppCompatActivity activity) {
        this.activity = activity;

        transactionController = new TransactionController(activity);

        transaction = transactionController.getTransactionById(id);

        Category category = new CategoryController(activity).getCategoryById(transaction.getCategory_id());
        if (category != null) {
            type = category.getCategory_type();
        } else {
            type = CategoryEntry.TYPE_TRANSFER;
        }
    }

    @Override
    public void setUpUI() {
        setUpViews();
        setUpForm();
        setUpButtons();
    }

    private void setUpViews() {
        activity.setContentView(R.layout.activity_edit);

        TextView titleTV = activity.findViewById(R.id.activity_edit_title);
        titleTV.setText("Edit Transaction");

        LinearLayout transactionFormContainer = activity.findViewById(R.id.activity_edit_form);
        transactionFormContainer.addView(activity.getLayoutInflater().inflate(R.layout.form_transaction,
                (ViewGroup) activity.findViewById(R.id.form_transaction_constraintlayout)));

        deleteBtn = activity.findViewById(R.id.activity_edit_delete);
        deleteBtn.setText("Delete Transaction");
    }

    private void setUpForm() {
        transactionForm = new TransactionForm(activity, null, activity, type);
        transactionForm.setUpUI();
        transactionForm.setAmount(transaction.getTransaction_amount());
        transactionForm.setDesc(transaction.getTransaction_desc());
        transactionForm.setDate(transaction.getTransaction_date());
        transactionForm.setCategoryId(transaction.getCategory_id());
        transactionForm.setWalletId(transaction.getWallet_id());
        transactionForm.setWalletDestId(transaction.getWalletdest_id());
    }

    private void setUpButtons() {
        setUpSaveButton();
        setUpDeleteButton();
        setUpBackButton();
    }

    private void setUpSaveButton() {
        Button saveBtn = activity.findViewById(R.id.transaction_save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!transactionForm.validateInput()) return;
                int rowsUpdated = updateTransaction();
                if (rowsUpdated == 0) {
                    Toast.makeText(activity, "Error updating the transaction", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "Transaction updated", Toast.LENGTH_SHORT).show();
                }
                activity.finish();
            }
        });
    }

    private int updateTransaction() {
        Transaction transactionBefore = Transaction.clone(transaction);

        double amount = transactionForm.getAmount();
        String desc = transactionForm.getDesc();
        Date date = transactionForm.getDate();
        long categoryId = transactionForm.getCategoryId();
        long walletId = transactionForm.getWalletId();
        long walletDestId = transactionForm.getWalletDestId();

        transaction.setTransaction_amount(amount);
        transaction.setTransaction_desc(desc);
        transaction.setTransaction_date(date);
        transaction.setCategory_id(categoryId);
        transaction.setWallet_id(walletId);
        transaction.setWalletdest_id(walletDestId);

        return transactionController.updateTransaction(transactionBefore, transaction);
    }

    private void deleteTransaction() {
        int rowsDeleted = transactionController.deleteTransaction(transaction);
        if (rowsDeleted == 0) {
            Toast.makeText(activity, "Error deleting transaction", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "Transaction is deleted", Toast.LENGTH_SHORT).show();
        }
        activity.finish();
    }

    private void setUpDeleteButton() {
        deleteBtn = activity.findViewById(R.id.activity_edit_delete);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteConfirmationDialog();
            }
        });
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity, R.style.AlertDialogCustom);
        alertDialogBuilder.setTitle("Delete Confirmation");
        alertDialogBuilder.setMessage("Are you sure to delete this transaction?\nYou can't undo this")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteTransaction();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary); //biar bg gelap
        alertDialog.show();
    }

    private void setUpBackButton() {
        ImageView backBtn = activity.findViewById(R.id.activity_edit_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
    }

    @Override
    public View getView() {
        return activity.findViewById(R.id.activity_edit_constraintlayout);
    }
}
