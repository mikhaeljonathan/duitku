package com.example.duitku.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.duitku.R;
import com.example.duitku.budget.Budget;
import com.example.duitku.budget.view.ViewBudgetActivity;
import com.example.duitku.category.Category;
import com.example.duitku.category.CategoryController;

import static com.example.duitku.main.App.CHANNEL_ID_BUDGET;
import static com.example.duitku.main.App.CHANNEL_ID_FIRESTORE;

public class NotificationController {

    private NotificationManagerCompat notificationManager;
    private final Context context;

    public NotificationController(Context context){
        this.context = context;
        notificationManager = NotificationManagerCompat.from(context);
    }

    public void sendOnChannelBudgetOverflow(Budget budget){
        if (!notificationManager.areNotificationsEnabled()){
            openNotificationSettings();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
                isChannelBlocked(CHANNEL_ID_BUDGET)){
            openChannelSettings(CHANNEL_ID_BUDGET);
            return;
        }

        sendBudgetNotification(budget);
    }

    public void sendOnChannelBackupDatabase(){
        if (!notificationManager.areNotificationsEnabled()){
            openNotificationSettings();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
                isChannelBlocked(CHANNEL_ID_FIRESTORE)){
            openChannelSettings(CHANNEL_ID_FIRESTORE);
            return;
        }

        sendBackupNotification();
    }

    private void openNotificationSettings(){
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
        } else {
            intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
        }
        context.startActivity(intent);
    }

    @RequiresApi(26)
    private boolean isChannelBlocked(String channelId){
        NotificationManager manager = context.getSystemService(NotificationManager.class);
        NotificationChannel channel = manager.getNotificationChannel(channelId);

        return channel != null &&
                channel.getImportance() == NotificationManager.IMPORTANCE_NONE;
    }

    @RequiresApi(26)
    private void openChannelSettings(String channelId){
        Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, channelId);
        context.startActivity(intent);
    }

    private void sendBudgetNotification(Budget budget){
        Intent activityIntent = new Intent(context, ViewBudgetActivity.class);
        activityIntent.putExtra("ID", budget.get_id());
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Category category = new CategoryController(context).getCategoryById(budget.getCategory_id());
        double delta = budget.getBudget_used() - budget.getBudget_amount();

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID_BUDGET)
                .setSmallIcon(R.drawable.icon_logo)
                .setContentTitle("Budget Overflow")
                .setContentText("Budget " + category.getCategory_name() + "\nOverspent " + delta)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(1, notification);
    }

    private void sendBackupNotification(){
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID_FIRESTORE)
                .setSmallIcon(R.drawable.icon_logo)
                .setContentTitle("Cloud Backup")
                .setContentText("Your data have been backed up in cloud")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(2, notification);
    }



}
