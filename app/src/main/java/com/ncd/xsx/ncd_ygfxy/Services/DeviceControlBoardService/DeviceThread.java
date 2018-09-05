package com.ncd.xsx.ncd_ygfxy.Services.DeviceControlBoardService;


import com.ncd.xsx.ncd_ygfxy.RxBus.RxBus;
import com.ncd.xsx.ncd_ygfxy.RxBus.ServiceStatuMsg;

public class DeviceThread extends Thread {

    private boolean threadControlFlag = true;

    private int sendCnt = 0;

    public DeviceThread(){
        threadControlFlag = true;
    }

    public void stopSerialThread(){
        threadControlFlag = false;
    }

    @Override
    public void run()
    {

        while(threadControlFlag)
        {
            RxBus.getInstance().post(new ServiceStatuMsg(ServiceStatuMsg.DEVICE_SERVICE_LIVE));

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
