package com.example.lab_4;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.lab_4.database.Alarm;
import com.example.lab_4.database.AlarmDao;
import com.example.lab_4.database.AppDatabase;
import com.example.lab_4.database.DBProvider;

import java.util.List;

public class MainActivity extends AppWidgetProvider {

    public static String ACTION_WIDGET_RECEIVER = "ActionReceiverWidget";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int id: appWidgetIds) {

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

            AppDatabase db = DBProvider.getInstance().getDatabase();
            AlarmDao dao = db.alarmDao();

            List<Alarm> l = dao.getAll();

            Alarm alarm = dao.getByIdWidget(id);

            if (alarm != null) {
                long date = alarm.time;


                String days = "";
                long cur_date = System.currentTimeMillis();
                System.out.println(cur_date + " " + date);

                int days1 = (int) Math.floor((date - cur_date) / (1000 * 60 * 60 * 24));

                days = String.valueOf(days1) + " полных суток осталось до оповещения";

                remoteViews.setTextViewText(R.id.tv, days);
            } else {

                System.out.println("SDfgkjsdngkjnfkjn");
            }

            //Подготавливаем Intent для Broadcast
            Intent active = new Intent(context, MainActivity.class);
            active.setAction(ACTION_WIDGET_RECEIVER);

            PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);

            remoteViews.setOnClickPendingIntent(R.id.tv, actionPendingIntent);

            appWidgetManager.updateAppWidget(id, remoteViews);

        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {


        final String action = intent.getAction();
        if (ACTION_WIDGET_RECEIVER.equals(action)) {
            Intent iintent = new Intent(context, ChoosingDate.class);
            iintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(iintent);
        }
        super.onReceive(context, intent);
    }
}
