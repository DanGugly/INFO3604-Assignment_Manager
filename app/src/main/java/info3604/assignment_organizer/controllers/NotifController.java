package info3604.assignment_organizer.controllers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Random;

import info3604.assignment_organizer.R;
import info3604.assignment_organizer.views.NotificationActivity;

import static android.app.NotificationManager.EXTRA_NOTIFICATION_CHANNEL_ID;
import static android.app.NotificationManager.IMPORTANCE_DEFAULT;

public class NotifController extends BroadcastReceiver{

    private static final String CHANNEL_ID = "info3604.assignment_organizer.channelId";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent notificationIntent = new Intent(context, NotificationActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(NotificationActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context);

        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        String ticker = intent.getStringExtra("ticker");

        Notification notification = builder.setContentTitle(title)
                .setContentText(content)
                .setTicker(ticker)
                .setSmallIcon(R.drawable.ic_notification)
                .setPriority(Notification.PRIORITY_HIGH)
                //.setLights(0xffff00ff,300,100)
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS)
                .setContentIntent(pendingIntent).build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "NotificationDemo",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLightColor(0xffff00ff);
        }

        //For indexing notifs Random notification_id = new Random(); notificationManager.notify(notification_id.nextInt(100), notification);
        Random notification_id = new Random();
        notificationManager.notify(notification_id.nextInt(100), notification);
    }

    //Cancel a previous notif
    public void cancelNotif(Context context,int id){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }
}
