package com.example.duitku.main;

import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.duitku.R;
import com.example.duitku.database.DuitkuContract;
import com.example.duitku.database.DuitkuContract.UserEntry;
import com.example.duitku.firebase.FirebaseJobService;
import com.example.duitku.interfaces.UIView;
import com.example.duitku.passcode.PasscodeActivity;
//import com.example.duitku.user.AddProfileActivity;
import com.example.duitku.user.User;
import com.example.duitku.user.UserController;
import com.github.florent37.tutoshowcase.TutoShowcase;
import com.example.duitku.welcome.WelcomeActivity;

import java.util.UUID;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class MainActivity extends AppCompatActivity {

    private User user;
    private UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UIView mainActivityView = new MainActivityView(this);
        mainActivityView.setUpUI();

        userController = new UserController(this);
        user = userController.getUser();

        if (user != null && user.getPasscode() != null){
            launchPasscode();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        user = userController.getUser();

        if (user == null){
            showWelcome();  //showWelcome() --> showGetStarted()
            return;
        }

        if (user.getFirstTime().equals(UserEntry.TYPE_FIRST_TIME)){
            setUpShowCase();
        }

    }

    private void showWelcome(){
        Intent welcomeIntent = new Intent(this, WelcomeActivity.class);
        startActivity(welcomeIntent);
    }

    private void launchPasscode(){
        Intent insertPasscodeIntent = new Intent(this, PasscodeActivity.class);
        insertPasscodeIntent.putExtra("Flag", "INPUT");
        startActivity(insertPasscodeIntent);
    }

    private void setUpShowCase(){
        ShowcaseConfig config = new ShowcaseConfig();

        config.setDelay(250);

        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, randomUUIDString);
        sequence.setConfig(config);

        sequence.addSequenceItem(findViewById(R.id.nav_transaction),
                "This is the menu to see your daily, weekly, monthly transaction, wallet, and budget", "GOT IT");

        sequence.addSequenceItem(findViewById(R.id.nav_report),
                "This is the menu to see your transaction report", "GOT IT");

        sequence.addSequenceItem(findViewById(R.id.main_add_fab),
                "This is the button to add a daily transaction", "GOT IT");

        sequence.addSequenceItem(findViewById(R.id.nav_article),
                "This is the menu to see some article that you may need", "GOT IT");

        sequence.addSequenceItem(findViewById(R.id.nav_account),
                "This is the menu to see and edit your profile", "GOT IT");

        sequence.start();
        sequence.setOnItemDismissedListener(new MaterialShowcaseSequence.OnSequenceItemDismissedListener() {
            @Override
            public void onDismiss(MaterialShowcaseView itemView, int position) {
                if(position==4) showSwipeable();
            }
        });
    }

    private void showSwipeable(){
        TutoShowcase.from(this)
                .setContentView(R.layout.showcase_swipeable)
                .setFitsSystemWindows(true)
                .on(R.id.account_name_textview)
                .displaySwipableLeft()
                .delayed(1000)
                .animated(true)
                .show();
        updateUserNotFirstTime();
    }

    private void updateUserNotFirstTime(){
        user.setFirstTime(UserEntry.TYPE_NOT_FIRST_TIME);
        userController.updateUser(user);
    }

}