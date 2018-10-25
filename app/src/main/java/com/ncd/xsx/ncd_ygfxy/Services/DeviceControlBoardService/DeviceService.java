package com.ncd.xsx.ncd_ygfxy.Services.DeviceControlBoardService;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;

import com.ncd.xsx.ncd_ygfxy.R;
import com.ncd.xsx.ncd_ygfxy.RxBus.MessageEvent;
import com.ncd.xsx.ncd_ygfxy.RxBus.MsgDefine;
import com.ncd.xsx.ncd_ygfxy.RxBus.RxBus;
import com.ncd.xsx.ncd_ygfxy.RxBus.ServiceStatuMsg;

import java.util.Date;

public class DeviceService extends Service{

    private DeviceThread deviceThread;

    @Override
    public void onCreate() {
        super.onCreate();

        Notification.Builder localBuilder = new Notification.Builder(this);
        localBuilder.setAutoCancel(false);
        localBuilder.setOngoing(true); //禁止滑动删除
        localBuilder.setShowWhen(true);//右上角的时间显示
        localBuilder.setSmallIcon(R.mipmap.notification_l);
        localBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.notification_l));
        localBuilder.setContentTitle("DEVICE SERVICE");
        localBuilder.setContentText("running...");
        startForeground(2, localBuilder.build());

        deviceThread = new DeviceThread(this);
        deviceThread.start();

        RxBus.getInstance().post(new ServiceStatuMsg(ServiceStatuMsg.DEVICE_SERVICE_LIVE));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        RxBus.getInstance().post(new ServiceStatuMsg(ServiceStatuMsg.DEVICE_SERVICE_DEAD));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
