package com.example.duitku.account;

import androidx.appcompat.app.AppCompatActivity;

import com.example.duitku.R;
import com.example.duitku.firebase.FirebaseHelper;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AddFeedbackActivity extends AppCompatActivity {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feedback);

        setUpUI();
        setUpBackBtn();
        setUpSendBtn();
    }

    private void setUpUI() {
        editText = findViewById(R.id.editTextTextMultiLine);
        editText.requestFocus();
    }

    private void setUpBackBtn() {
        ImageView backBtn = findViewById(R.id.add_feedback_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setUpSendBtn() {
        Button sendBtn = findViewById(R.id.add_feedback_save_btn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String feedback = editText.getText().toString();
                new FirebaseHelper().addFeedback(feedback);
                editText.getText().clear();

                Toast.makeText(AddFeedbackActivity.this, "Your feedback has been submitted. Thank you!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}