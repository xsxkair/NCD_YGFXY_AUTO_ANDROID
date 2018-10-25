package com.ncd.xsx.ncd_ygfxy.SerialDriver.DeviceSerial;

import android.util.Log;

import com.android.internal.widget.PreferenceImageView;
import com.friendlyarm.AndroidSDK.HardwareControler;
import com.ncd.xsx.ncd_ygfxy.SerialDriver.GPRSSerial.GprsSerialDefine;
import com.ncd.xsx.ncd_ygfxy.SerialDriver.GPRSSerial.GprsSerialDriver;
import com.ncd.xsx.ncd_ygfxy.Tools.CheckSum;

public class DeviceSerialDriver {

    private int serialDeviceFile = -1;
    private boolean serialIsBusy = false;

    private int totalReadDataLen = 0;
    private byte[] recvBuf = new byte[DeviceSerialDefine.SERIAL_READBUF_LEN];

    private int tempReadDataLen = 0;
    private byte[] tempRecvBuf = new byte[DeviceSerialDefine.SERIAL_TEMP_READBUF_LEN];

    //在访问HttpMethods时创建单例
    private static class SingletonHolder{
        private static final DeviceSerialDriver INSTANCE = new DeviceSerialDriver();
    }

    //获取单例
    public static DeviceSerialDriver getInstance(){
        return DeviceSerialDriver.SingletonHolder.INSTANCE;
    }

    public void DeviceSerialInit() throws Exception {
        openSerial();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //hardware control mathods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /*
    打开与控制板的通信串口
     */
    public void openSerial() throws Exception {

        serialDeviceFile = HardwareControler.openSerialPort(DeviceSerialDefine.SERIAL_FILE_NAME, DeviceSerialDefine.SERIAL_BAUD, 8, 1);
        Log.d("xsx", " "+serialDeviceFile);
        if(serialDeviceFile < 0)
            throw new Exception("open device Serial Fail");
    }

    /*
    读取串口状态
     */
    public boolean isSerialOpened(){
        if(serialDeviceFile > 0)
            return  true;
        else
            return  false;
    }

    public boolean writeDataToDevice(DeviceSerialEntity serialRequest)
    {
        byte[] serialRequestBytes = serialRequest.ParseDeviceSerialEntityToBytes();

        StringBuffer s = new StringBuffer();

        for(int i=0; i<serialRequestBytes.length; i++)
        {
            s.append(String.format(" 0x%02x", serialRequestBytes[i]));
        }
        Log.i("send data to device", s.toString());

        if (HardwareControler.write(serialDeviceFile, serialRequestBytes) == serialRequestBytes.length)
            return true;
        else
            return false;
    }

    public DeviceSerialEntity readDataFromDevice()
    {
        totalReadDataLen = 0;

        while(HardwareControler.select(serialDeviceFile, 0, DeviceSerialDefine.SERIAL_READ_WAIT_TIME) > 0)
        {
            tempReadDataLen = HardwareControler.read(serialDeviceFile, tempRecvBuf, GprsSerialDefine.SERIAL_READBUF_LEN);

            if(tempReadDataLen > 100)
                tempReadDataLen = 100;
            System.arraycopy(tempRecvBuf, 0, recvBuf, totalReadDataLen, tempReadDataLen);

            totalReadDataLen += tempReadDataLen;
            if(totalReadDataLen > 512)
                totalReadDataLen = 0;
        }

        if(totalReadDataLen < 11)
            return null;

        StringBuffer s = new StringBuffer();

        for(int i=0; i<totalReadDataLen; i++)
        {
            s.append(String.format(" 0x%02x", recvBuf[i]));
        }
        Log.i("recv data from device", s.toString());

        return DeviceSerialEntity.ParseBytesToDeviceSerialEntity(recvBuf, totalReadDataLen);
    }

    /*
     *   serialRequest -- 请求数据
     *   return : 通信失败 返回null
     */
  /*  public DeviceSerialEntity serialCommunicationFunction(DeviceSerialEntity serialRequest, long waitTime) throws Exception {
        int errorCnt = 5;
        int checkSum = 0;
        byte[] serialRequestBytes;

        if(serialIsBusy){
            Thread.sleep(waitTime);
        }

        if(serialIsBusy)
            throw new Exception("device busy");
        else
            serialIsBusy = true;

        serialRequestBytes = serialRequest.changeToByteForSend();

        while (errorCnt > 0){

            if (HardwareControler.write(serialDeviceFile, serialRequestBytes) > 0){

                while(HardwareControler.select(serialDeviceFile, 0, DeviceSerialDefine.SERIAL_READ_WAIT_TIME) > 0)
                {
                    tempReadDataLen = HardwareControler.read(serialDeviceFile, tempRecvBuf, GprsSerialDefine.SERIAL_READBUF_LEN);

                    System.arraycopy(tempRecvBuf, 0, recvBuf, totalReadDataLen, tempReadDataLen);

                    totalReadDataLen += tempReadDataLen;
                }

                //接收数据必须大于7字节才是正确的
                if(totalReadDataLen > 7)
                {
                    checkSum = CheckSum.checkSum(recvBuf, totalReadDataLen-1);

                    if(checkSum == recvBuf[totalReadDataLen - 1])
                    {
                        serialIsBusy = false;
                        return DeviceSerialEntity.build(recvBuf, totalReadDataLen);
                    }
                }
            }

            Thread.sleep(waitTime);

            errorCnt--;
        }

        serialIsBusy = false;

        throw new Exception("communicate fail");
    }*/

}
