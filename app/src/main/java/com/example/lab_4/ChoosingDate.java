package com.example.lab_4;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;
import android.widget.RemoteViews;

import com.example.lab_4.database.Alarm;
import com.example.lab_4.database.AlarmDao;
import com.example.lab_4.database.AppDatabase;
import com.example.lab_4.database.DBProvider;

import java.util.Calendar;
import java.util.TimeZone;

public class ChoosingDate extends AppCompatActivity {
    int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing_date);

        Intent iintent = getIntent();
        Bundle extras = iintent.getExtras();
        if (extras != null) {
            widgetID = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID
            );
        }

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

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent myIntent = new Intent(ctx, AlarmNotificationService.class);
                PendingIntent pendingIntent = PendingIntent.getService(
                        ctx, 1, myIntent, 0);

                alarmManager.cancel(pendingIntent);

                Intent intent = new Intent(ctx, AlarmNotificationService.class);
                intent.putExtra("date", true_date);
                PendingIntent pintent = PendingIntent.getService(ctx, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pintent);
                alarmManager.set(AlarmManager.RTC_WAKEUP, true_date, pintent);


                String days = "";


                AppWidgetManager a = AppWidgetManager.getInstance(ctx);

                RemoteViews remoteViews = new RemoteViews(ctx.getPackageName(), R.layout.widget);

                long cur_date = System.currentTimeMillis();

                int days1 = (int) Math.floor((true_date - cur_date) / (1000 * 60 * 60 * 24));

                if(days1 >= 0) {
                    AppDatabase db = DBProvider.getInstance().getDatabase();
                    AlarmDao dao = db.alarmDao();

                    Alarm alarm = dao.getByIdWidget(widgetID);
                    if (alarm != null) {
                        alarm.time = true_date;
                        dao.update(alarm);
                    } else {
                        alarm = new Alarm();
                        alarm.time = true_date;
                        alarm.id_widget = widgetID;
                        dao.insert(alarm);
                    }
                    
                    days = String.valueOf(days1) + " полных суток осталось";


                }else{
                    days = "Дата уже прошла";
                }

                remoteViews.setTextViewText(R.id.tv, days);
                a.updateAppWidget(widgetID, remoteViews);
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);

                setResult(RESULT_OK, resultValue);

                finish();
            }
        });
    }
}
