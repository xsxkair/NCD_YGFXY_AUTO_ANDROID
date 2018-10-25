package com.ncd.xsx.ncd_ygfxy.Services.CommonService;


import android.content.Context;
import android.content.Intent;

import com.ncd.xsx.ncd_ygfxy.Defines.PublicStringDefine;
import com.ncd.xsx.ncd_ygfxy.Services.MyThread;

public class CommonThread extends MyThread {

    private Context mContext;
    Intent sec_intent;

    public CommonThread(Context context) {
        super();
        this.mContext = context;
        sec_intent = new Intent(PublicStringDefine.MY_SEC_BROADCAST);
    }

    @Override
    public void run()
    {

        while(threadRunning())
        {
            mContext.sendBroadcast(sec_intent);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
