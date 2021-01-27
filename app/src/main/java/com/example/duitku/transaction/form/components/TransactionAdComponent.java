package com.example.duitku.transaction.form.components;

import android.content.Context;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.duitku.R;
import com.example.duitku.database.DuitkuContract.UserEntry;
import com.example.duitku.user.UserController;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class TransactionAdComponent extends View {

    private Context context;
    private final View rootView;
    private AppCompatActivity activity;

    public TransactionAdComponent(Context context, View rootView, Object activity) {
        super(context);

        this.context = context;
        this.rootView = rootView;
        if (activity instanceof AppCompatActivity) {
            this.activity = (AppCompatActivity) activity;
        }

        setUpUI();
    }

    private void setUpUI() {
        AdView adView;

        if (rootView == null) {
            adView = activity.findViewById(R.id.transaction_adview);
        } else {
            adView = rootView.findViewById(R.id.transaction_adview);
        }

        if (new UserController(context).getUser().getUser_status().equals(UserEntry.TYPE_PREMIUM)) {
            adView.setVisibility(GONE);
        }

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
}
