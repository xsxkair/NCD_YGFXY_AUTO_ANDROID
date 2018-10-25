package com.ncd.xsx.ncd_ygfxy.Services.DeviceControlBoardService;


import android.content.Context;

import com.ncd.xsx.ncd_ygfxy.RxBus.RxBus;
import com.ncd.xsx.ncd_ygfxy.RxBus.ServiceStatuMsg;

public class DeviceThread extends Thread {

    private Context mContext;

    public DeviceThread(Context context){
        this.mContext = context;
    }

    @Override
    public void run()
    {

        while(true)
        {

            DeviceFunction.DeviceSerislSendFunction();

            DeviceFunction.DeviceSerislRecvFunction(this.mContext);

            RxBus.getInstance().post(new ServiceStatuMsg(ServiceStatuMsg.DEVICE_SERVICE_LIVE));

        }

    }
}
