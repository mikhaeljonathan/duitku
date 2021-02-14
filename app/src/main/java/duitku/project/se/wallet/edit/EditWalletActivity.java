package duitku.project.se.wallet.edit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import duitku.project.se.interfaces.UIView;

public class EditWalletActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long walletId = getIntent().getLongExtra("ID", -1);
        UIView editWalletActivityView = new EditWalletActivityView(walletId, this);
        editWalletActivityView.setUpUI();
    }

}