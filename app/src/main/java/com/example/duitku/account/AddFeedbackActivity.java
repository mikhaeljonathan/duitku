package com.example.duitku.account;

import androidx.appcompat.app.AppCompatActivity;
import com.example.duitku.R;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class AddFeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feedback);

        setUpUI();
        setUpBackBtn();
    }

    private void setUpUI(){
        EditText editText = findViewById(R.id.editTextTextMultiLine);
        editText.requestFocus();
    }


    private void setUpBackBtn(){
        ImageView backBtn = findViewById(R.id.add_feedback_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}