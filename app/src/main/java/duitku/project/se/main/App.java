package duitku.project.se.main;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {

    public static final String CHANNEL_ID_BUDGET = "budget_overflow";
    public static final String CHANNEL_ID_FIRESTORE = "backup_database";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channelBudget = new NotificationChannel(
                    CHANNEL_ID_BUDGET,
                    "Budget Overflow",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channelBudget.setDescription("Budget Overflow Channel");

            NotificationChannel channelFirestore = new NotificationChannel(
                    CHANNEL_ID_FIRESTORE,
                    "Backup Database",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channelFirestore.setDescription("Backup Database Channel");

            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(channelBudget);
            manager.createNotificationChannel(channelFirestore);
        }
    }
}
