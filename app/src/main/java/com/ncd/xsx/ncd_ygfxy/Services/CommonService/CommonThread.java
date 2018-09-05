package com.ncd.xsx.ncd_ygfxy.Services.CommonService;


import android.util.Log;

import com.ncd.xsx.ncd_ygfxy.RxBus.MessageEvent;
import com.ncd.xsx.ncd_ygfxy.RxBus.RxBus;
import com.ncd.xsx.ncd_ygfxy.RxBus.TimeMsg;
import com.ncd.xsx.ncd_ygfxy.Services.MyThread;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonThread extends MyThread {

    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public CommonThread() {
        super();
    }

    @Override
    public void run()
    {

        while(threadRunning())
        {
            RxBus.getInstance().post(new TimeMsg(sf.format(new Date())));

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
