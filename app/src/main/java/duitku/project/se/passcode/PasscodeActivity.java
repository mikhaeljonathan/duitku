package duitku.project.se.passcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import duitku.project.se.R;
import duitku.project.se.user.User;
import duitku.project.se.user.UserController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PasscodeActivity extends AppCompatActivity {

    HashMap<Integer, ImageView> hm = new HashMap<>();
    List<Button> buttons = new ArrayList<>();
    TextView titleTV;

    StringBuilder passcode = new StringBuilder();
    String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode);

        flag = getIntent().getStringExtra("Flag");

        setTitle();
        setImageViewHashMap();
        setButtonsList();
        setButtonsBehavior();

    }

    private void setTitle() {
        titleTV = findViewById(R.id.passcode_title);
        if (flag.equals("SET")) {
            titleTV.setText("Set Passcode");
        } else if (flag.equals("CONFIRM")) {
            titleTV.setText("Confirm Passcode");
        } else {
            titleTV.setText("Input Passcode");
        }
    }

    private void setImageViewHashMap() {
        hm.put(1, (ImageView) findViewById(R.id.imageView));
        hm.put(2, (ImageView) findViewById(R.id.imageView2));
        hm.put(3, (ImageView) findViewById(R.id.imageView3));
        hm.put(4, (ImageView) findViewById(R.id.imageView4));
    }

    private void setButtonsList() {
        buttons.add((Button) findViewById(R.id.button0));
        buttons.add((Button) findViewById(R.id.button1));
        buttons.add((Button) findViewById(R.id.button2));
        buttons.add((Button) findViewById(R.id.button3));
        buttons.add((Button) findViewById(R.id.button4));
        buttons.add((Button) findViewById(R.id.button5));
        buttons.add((Button) findViewById(R.id.button6));
        buttons.add((Button) findViewById(R.id.button7));
        buttons.add((Button) findViewById(R.id.button8));
        buttons.add((Button) findViewById(R.id.button9));
    }

    private void setButtonsBehavior() {
        setNumberButtons();
        setCancelButton();
        setBackButton();
    }

    private void setNumberButtons() {
        for (int i = 0; i < 10; i++) {
            Button button = buttons.get(i);
            final int temp = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    passcode.append(temp);
                    changeToWhite();
                }
            });
        }
    }

    private void setCancelButton() {
        Button cancelBtn = findViewById(R.id.button_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setBackButton() {
        Button backBtn = findViewById(R.id.button_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passcode.length() > 0) {
                    passcode.deleteCharAt(passcode.length() - 1);
                    changeToDark();
                }
            }
        });
    }

    private void changeToDark() {
        int length = passcode.length();
        hm.get(length + 1).setImageResource(R.drawable.icon_circle_dark);
    }

    private void changeToWhite() {
        int length = passcode.length();
        hm.get(length).setImageResource(R.drawable.icon_circle_white);
        if (length == 4) {
            takeAction();
        }
    }

    private void takeAction() {
        if (flag.equals("SET")) {
            finish();
            openConfirmActivity();
        } else if (flag.equals("CONFIRM")) {
            String passcodeConfirm = getIntent().getStringExtra("Passcode");
            if (passcodeConfirm.equals(passcode.toString())) {
                finish();

                UserController userController = new UserController(this);
                User user = userController.getUser();
                user.setUser_passcode(passcode.toString());
                userController.updateUser(user);

            } else {
                errorState("Passcode doesn't match");
            }
        } else {
            String userPasscode = new UserController(this).getUser().getUser_passcode();
            if (userPasscode.equals(passcode.toString())) {
                finish();
            } else {
                errorState("Invalid Passcode");
            }
        }
    }

    private void openConfirmActivity() {
        Intent confirmPasscodeIntent = new Intent(this, PasscodeActivity.class);
        confirmPasscodeIntent.putExtra("Flag", "CONFIRM");
        confirmPasscodeIntent.putExtra("Passcode", passcode.toString());
        startActivity(confirmPasscodeIntent);
    }

    private void errorState(String message) {
        titleTV.setText(message);
        passcode.delete(0, passcode.length());
        for (int i = 1; i <= 4; i++) {
            hm.get(i).setImageResource(R.drawable.icon_circle_dark);
        }
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        assert v != null;
        v.vibrate(300);
    }

    @Override
    public void onBackPressed() {
        if (flag.equals("INPUT")) {
            finishAffinity();
        }
        super.onBackPressed();
    }

}