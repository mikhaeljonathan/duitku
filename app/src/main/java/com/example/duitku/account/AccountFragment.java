package com.example.duitku.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.duitku.R;
import com.google.firebase.auth.FirebaseAuth;

public class AccountFragment extends Fragment {

    private String displayName = "default";
    private String email = "default@domain.com";

    private FirebaseAuth mAuth;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);
        getCurrentUser();
        setUpButtons();
        setUpDisplayNameEmail();
        return view;
    }

    private void setUpDisplayNameEmail(){
        TextView txt_displayName = view.findViewById(R.id.account_name_textview);
        TextView txt_email = view.findViewById(R.id.account_email_textview);
        txt_displayName.setText(displayName);
        txt_email.setText(email);
    }

    private void getCurrentUser(){
        mAuth = FirebaseAuth.getInstance();
        if (mAuth != null) {
            displayName = mAuth.getCurrentUser().getDisplayName();
            email = mAuth.getCurrentUser().getEmail();
        }
    }

    private void setUpButtons(){
        setUpEditProfileButton();
        setUpSetPasscodeButton();
        setUpUpgradePremiumButton();
        setUpAddFeedbackButton();
        setUpSignOutButton();
    }

    private void setUpEditProfileButton(){
        Button editProfileBtn = view.findViewById(R.id.account_edit_profile_btn);
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editProfileIntent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(editProfileIntent);
            }
        });
    }

    private void setUpSetPasscodeButton(){
        Button setPasscodeBtn = view.findViewById(R.id.account_set_passcode_btn);
        setPasscodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent setPasscodeIntent = new Intent(getActivity(), PasscodeActivity.class);
                setPasscodeIntent.putExtra("Flag", "SET");
                startActivity(setPasscodeIntent);
            }
        });
    }

    private void setUpUpgradePremiumButton(){
        Button upgradePremiumBtn = view.findViewById(R.id.account_upgrade_premium_btn);
        upgradePremiumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent upgradePremiumIntent = new Intent(getActivity(), UpgradePremiumActivity.class);
                startActivity(upgradePremiumIntent);
            }
        });
    }

    private void setUpAddFeedbackButton(){
        Button addFeedbackBtn = view.findViewById(R.id.account_add_feedback_btn);
        addFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addFeedbackIntent = new Intent(getActivity(), AddFeedbackActivity.class);
                startActivity(addFeedbackIntent);
            }
        });
    }

    private void setUpSignOutButton(){

    }

}
