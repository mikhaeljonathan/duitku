package com.example.duitku.flows;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.duitku.MainActivityView;
import com.example.duitku.interfaces.UIView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIView mainActivityView = new MainActivityView(this);
        mainActivityView.setUpUI();
    }

}