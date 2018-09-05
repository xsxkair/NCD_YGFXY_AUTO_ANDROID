package com.ncd.xsx.ncd_ygfxy.Services.GprsService;


import android.content.Context;
import android.util.Log;

import com.ncd.xsx.ncd_ygfxy.Databases.Services.TestDataService;
import com.ncd.xsx.ncd_ygfxy.RxBus.RxBus;
import com.ncd.xsx.ncd_ygfxy.RxBus.ServiceStatuMsg;
import com.ncd.xsx.ncd_ygfxy.SerialDriver.GPRSSerial.GprsSerialDriver;
import com.ncd.xsx.ncd_ygfxy.Tools.MySdcardSharedPreferences;
import com.ncd.xsx.ncd_ygfxy.Databases.Entity.DeviceInfo;
import com.ncd.xsx.ncd_ygfxy.Databases.Entity.TestData;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

public class GprsThread extends Thread {

    private boolean threadControlFlag = true;

    private Context mContext;

    private DeviceInfo deviceInfo;

    private List<TestData> testDataList = null;
    private Iterator<TestData> testDataIterator = null;

    private int sendCnt = 0;

    public GprsThread(Context context){
        threadControlFlag = true;

        this.mContext = context;

        deviceInfo = new DeviceInfo();
    }

    public void stopSerialThread(){
        threadControlFlag = false;
    }

    @Override
    public void run()
    {

        while(threadControlFlag)
        {

            //检查设备信息是否有更新
            MySdcardSharedPreferences.getInstance().readLatestDeviceInfo(deviceInfo);
            if(deviceInfo.isState())
            {
                GprsSerialDriver.getInstance().uploadDeviceInfo(mContext, deviceInfo);

            }

            GprsSerialDriver.getInstance().uploadYgfxyData(mContext);

            //检查是否有数据需要上传纽康度服务器
            /*if(testDataIterator == null)
            {
                try {
                    testDataList = TestDataService.getInstance().getTestDataDao().queryTestDataWhereIsNotUpLimit(true, 10);
                    testDataIterator = testDataList.iterator();
                } catch (SQLException e) {
                    testDataList = null;
                    testDataIterator = null;
                }
            }
            else
            {
                if(testDataIterator.hasNext())
                {
                    TestData testData = testDataIterator.next();
                    Log.d("xsx", "id "+testData.getId());
                }
                else
                    testDataIterator = null;
            }
*/

            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
