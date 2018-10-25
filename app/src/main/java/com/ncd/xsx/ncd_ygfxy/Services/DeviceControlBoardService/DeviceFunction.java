package com.ncd.xsx.ncd_ygfxy.Services.DeviceControlBoardService;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.util.Log;

import com.ncd.xsx.ncd_ygfxy.Defines.PublicStringDefine;
import com.ncd.xsx.ncd_ygfxy.Logger.LoggerUnits;
import com.ncd.xsx.ncd_ygfxy.RxBus.RxBus;
import com.ncd.xsx.ncd_ygfxy.SerialDriver.DeviceSerial.DeviceSerialDefine;
import com.ncd.xsx.ncd_ygfxy.SerialDriver.DeviceSerial.DeviceSerialDriver;
import com.ncd.xsx.ncd_ygfxy.SerialDriver.DeviceSerial.DeviceSerialEntity;
import com.ncd.xsx.ncd_ygfxy.Services.TestService.TestFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class DeviceFunction {

    private static LinkedBlockingQueue<DeviceSerialEntity> DeviceSerialTxBlockingQueue;
    private static LinkedBlockingQueue<DeviceSerialEntity> DeviceSerialRxBlockingQueue;
    private static Intent new_sample_intent;

    static {
        DeviceSerialTxBlockingQueue = new LinkedBlockingQueue<>();
        DeviceSerialRxBlockingQueue = new LinkedBlockingQueue<>();

        new_sample_intent = new Intent(PublicStringDefine.NEW_SAMPLE_INPUT_BROADCASR);
    }


    /*
    底层数据通信接口，仅供DeviceThread调用
     */
    public static void DeviceSerislSendFunction()
    {
        DeviceSerialEntity deviceSerialEntity = null;

        try {
            deviceSerialEntity = DeviceSerialTxBlockingQueue.poll(10, TimeUnit.MILLISECONDS);

        } catch (InterruptedException e) {
            e.printStackTrace();
            LoggerUnits.error("pull tx deviceSerialEntity InterruptedException", e);
            deviceSerialEntity = null;
        }

        if(deviceSerialEntity != null)
        {
            DeviceSerialDriver.getInstance().writeDataToDevice(deviceSerialEntity);
        }
    }

    /*
    底层数据通信接口，仅供DeviceThread调用
     */
    public static void DeviceSerislRecvFunction(Context context)
    {
        DeviceSerialEntity deviceSerialEntity = DeviceSerialDriver.getInstance().readDataFromDevice();

        if(deviceSerialEntity != null)
        {
            //如果数据是stm32主动发的数据，则处理
            if(deviceSerialEntity.SessionFrom == DeviceSerialDefine.SESSION_STM32)
            {
                if(deviceSerialEntity.cmd == DeviceSerialDefine.NEW_SAMPLE_INPUT_CMD)
                {
                    String sample_id = new String(deviceSerialEntity.data);

                    int testDataUnitIndex = TestFunction.getInstance().addNewTestDataUnitService(sample_id);

                    if(testDataUnitIndex >= 0)
                    {
                        sendDataToDevice(deviceSerialEntity.SessionFrom, deviceSerialEntity.SessionId, deviceSerialEntity.cmd, new byte[]{DeviceSerialDefine.ACK_OK});

                        //如果添加成功，发出广播，通知监听者有新样本，不附带样本内容
                        new_sample_intent.putExtra(PublicStringDefine.NEW_SAMPLE_INTENT_KEY, testDataUnitIndex);
                        context.sendBroadcast(new_sample_intent);
                    }
                    else
                        sendDataToDevice(deviceSerialEntity.SessionFrom, deviceSerialEntity.SessionId, deviceSerialEntity.cmd, new byte[]{DeviceSerialDefine.ACK_FAIL});
                }
                else
                    LoggerUnits.error(String.format("recv data cmd: 0x%x  error", deviceSerialEntity.cmd));
            }
            //如果会话是android发起，则交由RxBlockingQueue处理
            else if(deviceSerialEntity.SessionFrom == DeviceSerialDefine.SESSION_ANDROID)
            {
                try {
                    DeviceSerialRxBlockingQueue.offer(deviceSerialEntity, 500, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    LoggerUnits.error("recv data offer blockqueue fail", e);
                }
            }
            else
                LoggerUnits.error("recv data SessionFrom error");
        }
    }

    /*
    上层数据发送接口，如需发送数据需调用此接口
     */
    public static boolean sendDataToDevice(int sessionFrom, int serrionId, int cmd, byte[] data)
    {
        DeviceSerialEntity deviceSerialEntity = new DeviceSerialEntity(sessionFrom, serrionId, cmd, data);

        return sendDataToDevice(deviceSerialEntity);
    }

    public static boolean sendDataToDevice(int sessionFrom, int serrionId, int cmd, String data)
    {
        DeviceSerialEntity deviceSerialEntity = new DeviceSerialEntity(sessionFrom, serrionId, cmd, data);

        return sendDataToDevice(deviceSerialEntity);
    }

    public static boolean sendDataToDevice(DeviceSerialEntity deviceSerialEntity)
    {
        try {
            return DeviceSerialTxBlockingQueue.offer(deviceSerialEntity, 500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            LoggerUnits.error("recv data offer blockqueue fail", e);

            return false;
        }
    }

    /*
    上层数据接收接口，如需接收数据需调用此接口
     */
    public static DeviceSerialEntity recvDataFromDevice()
    {
        DeviceSerialEntity deviceSerialEntity = null;

        try {
            deviceSerialEntity = DeviceSerialRxBlockingQueue.poll(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            LoggerUnits.error("recvDataFromDevice", e);
            deviceSerialEntity = null;
        }

        return deviceSerialEntity;
    }
}
