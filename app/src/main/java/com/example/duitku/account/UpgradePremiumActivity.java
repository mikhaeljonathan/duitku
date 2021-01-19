package com.example.duitku.account;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.duitku.R;

public class UpgradePremiumActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_premium);

        setUpUI();

        setUpBackBtn();
    }

    private void setUpUI(){
        TextView titleTV = findViewById(R.id.upgrade_premium_textView1);
        String text = "Go <font color='#00FFFF'>Premium</font> to enjoy more <font color='#00FFFF'>extraordinary</font> experiences with us!";
        titleTV.setText(Html.fromHtml(text));
    }

    private void setUpBackBtn(){
        ImageView backBtn = findViewById(R.id.upgrade_premium_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}