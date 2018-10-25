package com.ncd.xsx.ncd_ygfxy.Services.TestService;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;

import com.ncd.xsx.ncd_ygfxy.R;
import com.ncd.xsx.ncd_ygfxy.RxBus.RxBus;
import com.ncd.xsx.ncd_ygfxy.RxBus.ServiceStatuMsg;

public class TestService extends Service{

    private TestThread testThread;

    @Override
    public void onCreate() {
        super.onCreate();

        Notification.Builder localBuilder = new Notification.Builder(this);
        localBuilder.setAutoCancel(false);
        localBuilder.setOngoing(true); //禁止滑动删除
        localBuilder.setShowWhen(true);//右上角的时间显示
        localBuilder.setSmallIcon(R.mipmap.notification_l);
        localBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.notification_l));
        localBuilder.setContentTitle("TEST SERVICE");
        localBuilder.setContentText("running...");
        startForeground(4, localBuilder.build());

        TestFunction.getInstance().TestFunctionInit(this);

        testThread = new TestThread(this);
        testThread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
