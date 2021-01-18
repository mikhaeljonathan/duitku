package com.example.duitku.account;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.example.duitku.R;

public class UpgradePremiumActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_premium);

        TextView titleTV = findViewById(R.id.upgrade_premium_textView1);
        String text = "Go <font color='#00FFFF'>Premium</font> to enjoy more <font color='#00FFFF'>extraordinary</font> experiences with us!";
        titleTV.setText(Html.fromHtml(text));
    }
}