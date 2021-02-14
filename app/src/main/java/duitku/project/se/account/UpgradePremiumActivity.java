package duitku.project.se.account;

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

import duitku.project.se.R;
import duitku.project.se.database.DuitkuContract.UserEntry;
import duitku.project.se.firebase.FirebaseJobService;
import duitku.project.se.user.User;
import duitku.project.se.user.UserController;

public class UpgradePremiumActivity extends AppCompatActivity {

    public static final int BACKUP_FIRESTORE = 123;

    private TextView upgradedTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_premium);

        setUpUI();

        setUpBackBtn();
        setUpUpgradeBtn();
    }

    private void setUpUI() {
        TextView titleTV = findViewById(R.id.upgrade_premium_textView1);
        String text = "Go <font color='#00FFFF'>Premium</font> to enjoy more <font color='#00FFFF'>extraordinary</font> experiences with us!";
        titleTV.setText(Html.fromHtml(text));

        upgradedTV = findViewById(R.id.upgrade_premium_upgraded);

        if (new UserController(this).isPremium()) {
            upgradedTV.setVisibility(View.VISIBLE);
        } else {
            upgradedTV.setVisibility(View.GONE);
        }
    }

    private void setUpBackBtn() {
        ImageView backBtn = findViewById(R.id.upgrade_premium_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setUpUpgradeBtn() {
        final Button upgradeBtn = findViewById(R.id.upgrade_premium_btn);
        if (new UserController(this).isPremium()) {
            upgradeBtn.setVisibility(View.GONE);
        }

        upgradeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserController userController = new UserController(UpgradePremiumActivity.this);
                User user = userController.getUser();
                user.setUser_status(UserEntry.TYPE_PREMIUM);

                userController.updateUser(user);
                upgradedTV.setVisibility(View.VISIBLE);
                upgradeBtn.setVisibility(View.GONE);

                startBackup();
            }
        });
    }

    private void startBackup() {
        //ini buat scheduling backup
        ComponentName componentName = new ComponentName(this, FirebaseJobService.class);
        JobInfo info = new JobInfo.Builder(BACKUP_FIRESTORE, componentName)
                .setPersisted(true)
                .setPeriodic(6 * 3600 * 1000) // 6 hours
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.schedule(info);
    }

}