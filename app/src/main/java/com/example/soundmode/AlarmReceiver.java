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

    private static final int SILENT_TYPE = 1;
    private static final int NORMAL_TYPE = 2;

    private static final int silentHour = 22;
    private static final int silentMinute = 0;

    private static final int normalHour = 9;
    private static final int normalMinute = 0;

    public void onReceive(Context context, Intent intent) {
        setMode(context);
    }

    private void setMode(Context context) {
        boolean isSilent = silentModeTime();
        setAlarmManager(context, isSilent);
        setSoundMode(context, isSilent);
    }

    private void setAlarmManager(Context context, boolean isSilent) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        long nextTime = System.currentTimeMillis() + getDifTime(isSilent ? SILENT_TYPE : NORMAL_TYPE);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, nextTime, alarmIntent);
    }

    private long getDifTime(int type) {
        switch (type) {
            case SILENT_TYPE:
                return difHours(normalHour, silentHour) * 60 * 60 * 1000;
            case NORMAL_TYPE:
                return difHours(silentHour, normalHour) * 60 * 60 * 1000;
            default: return 0;
        }
    }

    private int difHours(int hour1, int hour2) {
        if (hour1 < hour2)
            return hour2 - hour1;
        else if (hour1 > hour2)
            return 24 - hour1 + hour2;
        else
            return 24;
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
