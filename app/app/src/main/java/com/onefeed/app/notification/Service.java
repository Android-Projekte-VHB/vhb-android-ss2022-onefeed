package com.onefeed.app.notification;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.onefeed.app.R;
import com.onefeed.app.data.addSource.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Service extends android.app.Service {

    SharedPreferences preferences;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        preferences = getSharedPreferences(getResources()
                .getString(R.string.init_process_boolean), 0);
        boolean notification = preferences.getBoolean(Constants.initial.Notification.name(), false);
        if(notification){
            initProcess();
        }
        stopSelf();
        return START_STICKY;
    }

    /*
    This Method will overhand the System with an Alarm Manager the Time when the Application wants
    to be called
     */
    @SuppressLint("UnspecifiedImmutableFlag")
    private void initProcess() {
        Intent intent = new Intent(this,ReminderBroadcast.class);

        PendingIntent pendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getBroadcast
                    (this, 0, intent, PendingIntent.FLAG_MUTABLE);
        }
        else
        {
            pendingIntent = PendingIntent.getBroadcast
                    (this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }
        GetMilliSeconds getMilliSeconds = new GetMilliSeconds(this);
        long time = getMilliSeconds.getMilliSeconds();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date resultDate = new Date(time);
        Log.d("Service",sdf.format(resultDate));
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,time,pendingIntent);
    }
}
