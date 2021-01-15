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
import androidx.core.content.ContextCompat;

import com.example.duitku.R;
import com.example.duitku.budget.Budget;
import com.example.duitku.budget.view.ViewBudgetActivity;
import com.example.duitku.budget.view.ViewBudgetActivityView;
import com.example.duitku.category.Category;
import com.example.duitku.category.CategoryController;

import static com.example.duitku.main.App.CHANNEL_ID;

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
                isChannelBlocked(CHANNEL_ID)){
            openChannelSettings(CHANNEL_ID);
            return;
        }

        sendNotification(budget);
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

    private void sendNotification(Budget budget){
        Intent activityIntent = new Intent(context, ViewBudgetActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, activityIntent, 0);

        Category category = new CategoryController(context).getCategoryById(budget.getCategoryId());
        double delta = budget.getUsed() - budget.getAmount();

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_down)
                .setContentTitle("Budget Overflow")
                .setContentText("Budget " + category.getName() + "\nOverspent " + delta)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(1, notification);
    }



}
