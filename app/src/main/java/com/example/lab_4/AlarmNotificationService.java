package com.example.lab_4;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;


public class AlarmNotificationService extends Service {
    public AlarmNotificationService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Context ctx = this;

        Intent notificationIntent = new Intent(ctx, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(ctx,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx);
        builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Будильник")
                .setContentText("Нажми на меня, чтобы выключить") // Текст уведомления
                .setTicker("Последнее предупреждение!")
                .setAutoCancel(true); // автоматически закрыть уведомление после нажатия

        NotificationManager notificationManager =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
