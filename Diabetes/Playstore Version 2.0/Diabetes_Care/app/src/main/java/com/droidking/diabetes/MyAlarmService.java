package com.droidking.diabetes;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by nazmul on 9/18/16.
 */
public class MyAlarmService extends Service {
    private NotificationManager mManager;
    NotificationManager manager;
    Notification myNotication;
    @Override
    public IBinder onBind(Intent arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        super.onCreate();
    }
    @SuppressWarnings("static-access")
    @Override
    public void onStart(Intent intent, int startId)
    {
        super.onStart(intent, startId);
        //for daily notification
       /* mManager = (NotificationManager) this.getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(this.getApplicationContext(),MainActivity.class);
        Notification notification = new Notification(R.mipmap.ic_launcher,"This is a test message!", System.currentTimeMillis());
        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity( this.getApplicationContext(),0, intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
       // notification.setLatestEventInfo(this.getApplicationContext(), "Daily Notification Demo", "This is a test message!", pendingNotificationIntent);
        mManager.notify(0, notification);*/
        notific();
    }
    private void notific(){
        //API level 11
        Intent intent = new Intent("com.droidkings.localnotification.MyReceiver");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, 0);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setAutoCancel(false);
        builder.setTicker("this is ticker text");
        builder.setContentTitle("Daily Notification");
        builder.setContentText("You have a new message");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentIntent(pendingIntent);
        builder.setOngoing(true);
        builder.setSubText("This is subtext...");   //API level 16
        builder.setNumber(100);
        builder.build();
        myNotication = builder.getNotification();
        manager.notify(11, myNotication);
    }
    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}
