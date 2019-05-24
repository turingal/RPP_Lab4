package com.example.lab_4;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;

import java.util.Calendar;
import java.util.TimeZone;

public class ChoosingDate extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing_date);

        final Context ctx = this;

        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                long date = calendar.getTimeInMillis();

                calendar.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
                int hours = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);
                int seconds = calendar.get(Calendar.SECOND);

                long true_date = date - hours*60*60*1000 - minutes*60*1000 - seconds*1000 + 9*3600*1000;

                // удаление оповещения
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent myIntent = new Intent(ctx, AlarmNotificationService.class);
                PendingIntent pendingIntent = PendingIntent.getService(
                        ctx, 1, myIntent, 0);

                alarmManager.cancel(pendingIntent);


                // установка нового оповещения
                Intent intent = new Intent(ctx, AlarmNotificationService.class);
                intent.putExtra("date", true_date);
                PendingIntent pintent = PendingIntent.getService(ctx, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pintent);
                alarmManager.set(AlarmManager.RTC_WAKEUP, true_date, pintent);


                Intent to_main = new Intent(ctx, MainActivity.class);
                to_main.putExtra("date", true_date);
                ctx.startActivity(to_main);

                finish();
            }
        });
    }
}
