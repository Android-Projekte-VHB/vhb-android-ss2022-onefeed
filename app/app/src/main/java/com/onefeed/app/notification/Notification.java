package com.onefeed.app.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.onefeed.app.R;
import com.onefeed.app.activity.FeedActivity;


public class Notification {

    /*
    This Class creates one Notification depend on several values
     */


    /*
    Constants
     */
    private final Context context;
    private final String title;
    private final String text;
    private final String CHANNEL_ID = "1";

    // Constructor
    public Notification( Context context, String title, String text){
        this.context = context;
        this.title = title;
        this.text = text;
    }

    /*
    This Method creates an Notification Builder and sets all important values of a Notification
     */
    private NotificationCompat.Builder notificationBuilder(PendingIntent pendingIntent){
        return new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_notification)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
    }

    /*
    With this Method you are able to Start one Notification to inform the User about new News
     */

    public void startNotification(int id){
        /*
        The Parameters create a Notification Channel and a NotificationManger
         to Mange the Build Notification
         */
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel
                (CHANNEL_ID,"OneFEED", importance);

        NotificationManager notificationManager = context
                .getSystemService(NotificationManager.class);

        notificationManager.createNotificationChannel(channel);

        notificationManager.notify(id, notificationBuilder(createIntentToApp()).build());
    }

    /*
    This Method creates a Pending Intent to able the user to open
     the App during clicking on the Notification
     */
    private PendingIntent createIntentToApp() {
        Intent intent = new Intent(context, FeedActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        return PendingIntent
                .getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
    }
}
