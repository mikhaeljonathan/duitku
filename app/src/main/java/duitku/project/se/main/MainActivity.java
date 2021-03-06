package duitku.project.se.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import duitku.project.se.R;
import duitku.project.se.database.DuitkuContract.UserEntry;
import duitku.project.se.interfaces.UIView;
import duitku.project.se.passcode.PasscodeActivity;
//import com.example.duitku.user.AddProfileActivity;
import duitku.project.se.user.User;
import duitku.project.se.user.UserController;
import com.github.florent37.tutoshowcase.TutoShowcase;
import duitku.project.se.welcome.WelcomeActivity;

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

        if (user != null && user.getUser_passcode() != null) {
            launchPasscode();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        user = userController.getUser();

        if (user == null) {
            showWelcome();  //showWelcome() --> showGetStarted()
            return;
        }

        if (user.getUser_first_time().equals(UserEntry.TYPE_FIRST_TIME)) {
            setUpShowCase();
        }

    }

    private void showWelcome() {
        Intent welcomeIntent = new Intent(this, WelcomeActivity.class);
        startActivity(welcomeIntent);
    }

    private void launchPasscode() {
        Intent insertPasscodeIntent = new Intent(this, PasscodeActivity.class);
        insertPasscodeIntent.putExtra("Flag", "INPUT");
        startActivity(insertPasscodeIntent);
    }

    private void setUpShowCase() {
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
                if (position == 4) showSwipeable();
            }
        });
    }

    private void showSwipeable() {
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

    private void updateUserNotFirstTime() {
        user.setUser_first_time(UserEntry.TYPE_NOT_FIRST_TIME);
        userController.updateUser(user);
    }

}