package com.ncd.xsx.ncd_ygfxy.Services.CommonService;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;

import com.ncd.xsx.ncd_ygfxy.R;
import com.ncd.xsx.ncd_ygfxy.Tools.MySdcardSharedPreferences;

import java.util.Calendar;

public class CommonService extends Service{

    private CommonThread commonThread;

    private BroadcastReceiver timeTickReceiver;

    @Override
    public void onCreate() {
        super.onCreate();

        Notification.Builder localBuilder = new Notification.Builder(this);
        localBuilder.setAutoCancel(false);
        localBuilder.setOngoing(true); //禁止滑动删除
        localBuilder.setShowWhen(true);//右上角的时间显示
        localBuilder.setSmallIcon(R.mipmap.notification_l);
        localBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.notification_l));
        localBuilder.setContentTitle("COMMON SERVICE");
        localBuilder.setContentText("running...");
        startForeground(1, localBuilder.build());

        timeTickReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                int minute = Calendar.getInstance().get(Calendar.MINUTE);
                //整点
                if(minute == 0)
                    MySdcardSharedPreferences.getInstance().putBoolean(MySdcardSharedPreferences.Keys.DEVICE_STATE_KEY, true)
                        .commit();
            }
        };

        commonThread = new CommonThread(this);
        commonThread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        registerReceiver(timeTickReceiver,new IntentFilter(Intent.ACTION_TIME_TICK));

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        commonThread.stopThread();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
