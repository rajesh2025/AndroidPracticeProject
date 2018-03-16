package com.android.practice.UI;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.practice.R;
import com.android.practice.Receivers.LocalNotificationReceiver;

import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //Create an offset from the current time in which the alarm will go off.
        Calendar cal = Calendar.getInstance();
       // cal.add(Calendar.HOUR_OF_DAY, 16);
        cal.add(Calendar.MINUTE, 1);

        Calendar repeatTime = Calendar.getInstance();
        repeatTime.add(Calendar.HOUR_OF_DAY, 24);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent myIntent = new Intent(AlarmActivity.this, LocalNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        }
    }
}
