package com.example.duitku.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.duitku.R;
import com.example.duitku.database.DuitkuContract.UserEntry;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AddProfileActivity extends AppCompatActivity {

    private TextInputLayout nameLayout;
    private TextInputEditText nameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setUpTitle();
        setUpNameEditText();
        setUpSaveButton();
    }

    private void setUpTitle(){
        TextView textView = findViewById(R.id.profile_title);
        textView.setText("Add Profile");
    }

    private void setUpNameEditText(){
        nameLayout = findViewById(R.id.profile_name_textinputlayout);

        nameField = findViewById(R.id.profile_name_edittext);
        nameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 20){
                    nameLayout.setError("Profile name max 20 characters");
                } else {
                    nameLayout.setErrorEnabled(false);
                }
            }
        });
    }

    private void setUpSaveButton(){
        Button saveBtn = findViewById(R.id.profile_save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInput()){
                    saveProfile();
                    finish();
                }
            }
        });
    }

    private boolean validateInput(){
        if (nameField.getText().length() < 1){
            nameLayout.setError("Name couldn't be empty");
            return false;
        }

        return true;
    }

    private void saveProfile(){
        String name = nameField.getText().toString();

        User user = new User("uid", name, "email", UserEntry.TYPE_REGULAR, UserEntry.TYPE_FIRST_TIME, null);

        new UserController(this).addUser(user);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}