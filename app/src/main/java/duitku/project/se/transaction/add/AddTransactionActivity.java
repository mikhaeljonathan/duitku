package duitku.project.se.transaction.add;

import androidx.appcompat.app.AppCompatActivity;

import duitku.project.se.interfaces.UIView;

import android.os.Bundle;

public class AddTransactionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIView transactionFragmentView = new AddTransactionActivityView(this);
        transactionFragmentView.setUpUI();
    }

}