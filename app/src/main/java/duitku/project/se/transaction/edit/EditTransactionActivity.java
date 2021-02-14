package duitku.project.se.transaction.edit;

import androidx.appcompat.app.AppCompatActivity;

import duitku.project.se.interfaces.UIView;

import android.os.Bundle;

public class EditTransactionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long transactionId = getIntent().getLongExtra("ID", -1);
        UIView editTransactionActivityView = new EditTransactionActivityView(transactionId, this);
        editTransactionActivityView.setUpUI();
    }
}