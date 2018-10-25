package com.ncd.xsx.ncd_ygfxy.Services.TestService;


import android.content.Context;
import android.util.Log;

import java.util.List;

public class TestThread extends Thread {

    private static final String TAG = "TestThread";

    private Context mContext;
    private List<TestDataUnit> testDataUnits;

    public TestThread(Context context){

        this.mContext = context;
        //testDataUnits = TestFunction.getInstance().getCurrentTestDataUnitList();
    }

    @Override
    public void run()
    {

        while(true)
        {

            //Log.i(TAG, "testDataUnits size: "+testDataUnits.size());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
