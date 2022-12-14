package com.onefeed.app.notification;

import android.annotation.SuppressLint;
import android.content.Context;

import com.onefeed.app.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GetMilliSeconds {


    private final long milliSeconds;
    @SuppressLint("SimpleDateFormat")
    // Time
    private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    private final long time = System.currentTimeMillis();
    private final Date resultDate = new Date(time);
    // Array with all Time Slots
    private final String[] notificationTimes;


    /*
    Constructor
     */
    public GetMilliSeconds(Context context){
        notificationTimes = context.getResources().getStringArray(R.array.notification_time_list);
        milliSeconds = getTimeInMillis(sdf.format(resultDate),notificationTimes);
    }


    /*
    Method calculate the Time between now and the Time Slot
     */
    private long getTimeInMillis(String timeNow, String[] PushName){
        // We know String timeNow is HH:mm
        // That's the reason why we must separate the Hours and the minutes
        String[] timeNowArray = timeNow.split(":");
        int hoursNow = Integer.parseInt(timeNowArray[0]);
        int minutesNow = Integer.parseInt(timeNowArray[1]);
        long hoursToMinutes = 60L;
        long secondsToMilliSeconds = 1000;

        /*
        We must do the same with the Push Time
        We have different Time Slots to Push a Notification that's the reason why the
        Method get overhand a PushName Array
         */
        String[][] pushTimeArray = new String[PushName.length][];
        for(int i = 0; i< PushName.length;i++){
            pushTimeArray[i] = PushName[i].split(":");
        }

        /*
        We know that Notification push times are always on the hour e.g. "13:00" that
        is the reason why we can ignore the minutes
         */
        int[] hoursArray = new int[PushName.length];
        for(int i = 0; i< PushName.length;i++){
            hoursArray[i] = Integer.parseInt(pushTimeArray[i][0]);
        }

        /*
        Know we can analyse the Time in millis when the Start Time equals PushTime
         */
        long result =0;
        long[] resultArray = new long[pushTimeArray.length];
        int lastNotification = 20;
        if(hoursNow <= lastNotification){
            for(int i = 0; i<resultArray.length;i++){
                long minutes = hoursToMinutes-minutesNow;
                long hoursInMinutes = (hoursArray[i]-(hoursNow+1))* hoursToMinutes;
                long b = minutes+hoursInMinutes;
                resultArray[i]= b*hoursToMinutes*secondsToMilliSeconds;
            }
        }
        else{
            /*
             If the time is over 20:00 we must calculate the time
              difference between now and 8:00
             */

            // the First Notification will be pushed at 8 o`clock in the morning
            long firstNotification = 8;
            long hoursPerDay = 24;
            long minutes = hoursToMinutes-minutesNow;
            long hoursInMinutes = ((hoursPerDay-(hoursNow+1))+firstNotification)*hoursToMinutes;
            long b = minutes+hoursInMinutes;
            result= b*hoursToMinutes*secondsToMilliSeconds;
        }


        // now we look which timestamp is the shortest
        for (long l : resultArray) {
            if (result == 0 && l > 0) {
                result = l;
            }
            if (result > l && l > 0) {
                result = l;
            }
        }
        return result;
    }

    /*
    Method overhand MilliSeconds until TimeSlot to another Class
     */
    public long getMilliSeconds() {
        return milliSeconds +time;
    }
}
