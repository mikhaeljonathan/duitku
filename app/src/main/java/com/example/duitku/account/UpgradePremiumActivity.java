package com.example.duitku.account;

import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.duitku.R;
import com.example.duitku.database.DuitkuContract.UserEntry;
import com.example.duitku.firebase.FirebaseJobService;
import com.example.duitku.user.User;
import com.example.duitku.user.UserController;

public class UpgradePremiumActivity extends AppCompatActivity {

    private static final int BACKUP_FIRESTORE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_premium);

        setUpUI();

        setUpBackBtn();
        setUpUpgradeBtn();
    }

    private void setUpUI(){
        TextView titleTV = findViewById(R.id.upgrade_premium_textView1);
        String text = "Go <font color='#00FFFF'>Premium</font> to enjoy more <font color='#00FFFF'>extraordinary</font> experiences with us!";
        titleTV.setText(Html.fromHtml(text));
    }

    private void setUpBackBtn(){
        ImageView backBtn = findViewById(R.id.upgrade_premium_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setUpUpgradeBtn(){
        Button upgradeBtn = findViewById(R.id.upgrade_premium_btn);
        upgradeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserController userController = new UserController(UpgradePremiumActivity.this);
                User user = userController.getUser();
                user.setUser_status(UserEntry.TYPE_PREMIUM);

                userController.updateUser(user);

                startBackup();
            }
        });
    }

    public void startBackup() {
        //ini buat scheduling backup
        ComponentName componentName = new ComponentName(this, FirebaseJobService.class);
        JobInfo info = new JobInfo  .Builder(BACKUP_FIRESTORE, componentName)
                .setPersisted(true)
                .setPeriodic(6 * 3600 * 1000) // 6 hours
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.schedule(info);
    }

    public void cancelBackup() {
        // ini buat cancel backup
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(BACKUP_FIRESTORE);
    }
}