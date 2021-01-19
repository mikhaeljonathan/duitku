package com.example.duitku.transaction.form.components;

import android.content.Context;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.duitku.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class TransactionAdComponent extends View {

    private final View rootView;
    private AppCompatActivity activity;

    public TransactionAdComponent(Context context, View rootView, Object activity) {
        super(context);

        this.rootView = rootView;
        if (activity instanceof AppCompatActivity){
            this.activity = (AppCompatActivity) activity;
        }

        setUpUI();
    }

    private void setUpUI(){
        AdView adView;

        if (rootView == null){
            adView = activity.findViewById(R.id.transaction_adview);
        } else {
            adView = rootView.findViewById(R.id.transaction_adview);
        }

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
}
