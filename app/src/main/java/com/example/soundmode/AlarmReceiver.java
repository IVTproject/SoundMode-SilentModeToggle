package com.example.soundmode;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    private static final int silentHour = 22;
    private static final int silentMinute = 30;

    private static final int normalHour = 9;
    private static final int normalMinute = 30;

    public void onReceive(Context context, Intent intent) {
        setMode(context);
    }

    private void setMode(Context context) {
        if (silentModeTime()) {
            setAlarmManager(context, normalHour, normalMinute);
            setSoundMode(context, true);
        } else {
            setAlarmManager(context, silentHour, silentMinute);
            setSoundMode(context, false);
        }
    }

    private void setAlarmManager(Context context, int hour, int minute) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        // Set the alarm to start
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
    }

    private boolean silentModeTime() {
        Calendar c = Calendar.getInstance();

        int currentHour = c.get(Calendar.HOUR_OF_DAY);
        int currentMinute = c.get(Calendar.MINUTE);

        if (currentHour >= silentHour) {
            return true;
        } else {
            return false;
        }
    }

    public void setSoundMode(Context context, boolean silentMode) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Service.AUDIO_SERVICE);

        if (silentMode)
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        else
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    }
}
