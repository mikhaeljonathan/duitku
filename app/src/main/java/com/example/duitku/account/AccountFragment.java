package com.example.duitku.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.duitku.R;

public class AccountFragment extends Fragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);
        setUpButtons();
        return view;
    }

    private void setUpButtons(){
        setUpEditProfileButton();
        setUpSetPasscodeButton();
        setUpAddFeedbackButton();
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

}
