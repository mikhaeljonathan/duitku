package com.example.duitku.main;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.duitku.R;
import com.example.duitku.interfaces.UIView;
import com.github.florent37.tutoshowcase.TutoShowcase;

import java.util.UUID;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIView mainActivityView = new MainActivityView(this);
        mainActivityView.setUpUI();
        setUpShowCase();
    }

    private void setUpShowCase(){
        ShowcaseConfig config = new ShowcaseConfig();

        config.setDelay(500);

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
                .on(R.id.textView)
                .displaySwipableLeft()
                .delayed(1000)
                .animated(true)
                .show();
    }

}