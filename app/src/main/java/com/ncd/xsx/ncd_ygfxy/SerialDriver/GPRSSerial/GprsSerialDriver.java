package com.ncd.xsx.ncd_ygfxy.SerialDriver.GPRSSerial;

import android.content.Context;
import android.util.Log;

import com.friendlyarm.AndroidSDK.HardwareControler;
import com.ncd.xsx.ncd_ygfxy.Defines.PublicStringDefine;
import com.ncd.xsx.ncd_ygfxy.Defines.PublicValueDefine;
import com.ncd.xsx.ncd_ygfxy.Tools.DateTimeTool;
import com.ncd.xsx.ncd_ygfxy.Tools.MySdcardSharedPreferences;
import com.ncd.xsx.ncd_ygfxy.Databases.Entity.DeviceInfo;

public class GprsSerialDriver {

    private int serialDeviceFile = -1;
    private byte[] recvBuf = new byte[GprsSerialDefine.SERIAL_READBUF_LEN];
    StringBuffer stringBuffer = new StringBuffer();
    //在访问HttpMethods时创建单例
    private static class SingletonHolder{
        private static final GprsSerialDriver INSTANCE = new GprsSerialDriver();
    }

    //获取单例
    public static GprsSerialDriver getInstance(){
        return GprsSerialDriver.SingletonHolder.INSTANCE;
    }


    //打开串口，并配置gprs模块
    public void GprsSerialInit() throws Exception {
        openSerial();

        if(!configGprsModule())
            throw new Exception("gprs init Fail");
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //hardware control mathods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /*
    打开与控制板的通信串口
     */
    public void openSerial() throws Exception {

        serialDeviceFile = HardwareControler.openSerialPort(GprsSerialDefine.SERIAL_FILE_NAME, GprsSerialDefine.SERIAL_BAUD, 8, 1);

        if(serialDeviceFile < 0)
            throw new Exception("open gprs Serial Fail");
    }

    private boolean configGprsModule()
    {
        //判断是否能连上服务器

        //进入命令模式
        if(!serialCommunicationCMDFunction("+++", "a", GprsSerialDefine.SERIAL_READ_WAIT_TIME_2_sec))
            return false;

        if(!serialCommunicationCMDFunction("a", "+ok", GprsSerialDefine.SERIAL_READ_WAIT_TIME_2_sec))
            return false;

        //设置服务器等信息
        if(!serialCommunicationCMDFunction("AT+WKMOD=\"NET\"\r", "OK", GprsSerialDefine.SERIAL_READ_WAIT_TIME_2_sec))
            return false;
        if(!serialCommunicationCMDFunction("AT+SOCKAEN=\"on\"\r", "OK", GprsSerialDefine.SERIAL_READ_WAIT_TIME_2_sec))
            return false;
        if(!serialCommunicationCMDFunction("AT+SOCKA=\"TCP\",\"116.62.108.201\",8080\r", "OK", GprsSerialDefine.SERIAL_READ_WAIT_TIME_2_sec))
            return false;
        if(!serialCommunicationCMDFunction("AT+S\r", "USR-GM3 V3.2.0", GprsSerialDefine.SERIAL_READ_WAIT_TIME_30_sec))
            return false;

        return true;
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

    /*   发送命令
     *   serialRequest -- 请求数据
     *   return : 通信失败 返回null
     */
    private boolean serialCommunicationCMDFunction(String serialRequest, String expectedRespone, int waitSec) {
        int errorCnt = 3;
        int readDataSize = 0;

        while (errorCnt > 0)
        {
            if (HardwareControler.write(serialDeviceFile, serialRequest.getBytes()) > 0)
            {
                Log.i("xsx", "gprs send: "+serialRequest);

                stringBuffer.setLength(0);

                while(HardwareControler.select(serialDeviceFile, waitSec, 0) > 0)
                {
                    readDataSize = HardwareControler.read(serialDeviceFile, recvBuf, GprsSerialDefine.SERIAL_READBUF_LEN);
                    for(int i=0; i<readDataSize; i++)
                    {
                        stringBuffer.append( (char)recvBuf[i]);
                    }
                }

                if(stringBuffer.length() > 0)
                {
                    String respone = stringBuffer.toString();

                    Log.i("xsx", "gprs recv: "+respone);
                    if(expectedRespone == null)
                        return true;
                    else if(respone.contains(expectedRespone))
                        return true;
                }
            }

            errorCnt--;
        }

        return false;
    }

    /*
    数据交换
     */
    private int serialCommunicationDataFunction(String serialRequest, int waitSec) {

        int readDataSize = 0;
        byte[] tempbuf = new byte[5000];

        readDataSize = HardwareControler.write(serialDeviceFile, serialRequest.getBytes());

        if (readDataSize > 0)
        {
            Log.i("xsx", "gprs send: "+serialRequest);

            readDataSize = 0;
            while(HardwareControler.select(serialDeviceFile, waitSec, 0) > 0)
            {
                int packagesize = HardwareControler.read(serialDeviceFile, tempbuf, GprsSerialDefine.SERIAL_READBUF_LEN);

                System.arraycopy(tempbuf, 0, recvBuf, readDataSize, packagesize);

                readDataSize += packagesize;
            }
        }

        return readDataSize;
    }

    public boolean connectServer()
    {
        if(!serialCommunicationCMDFunction("AT+E=off\r", GprsSerialDefine.GPRS_OK, GprsSerialDefine.SERIAL_READ_WAIT_TIME_2_sec))
			return false;

        //"¹Ø±ÕÒÆ¶¯³¡¾°\r");
        if(!serialCommunicationCMDFunction("AT+CIPSHUT\r", "SHUT OK", GprsSerialDefine.SERIAL_READ_WAIT_TIME_2_sec))
            return false;
        if(!serialCommunicationCMDFunction("AT+CIPCLOSE\r", null, GprsSerialDefine.SERIAL_READ_WAIT_TIME_2_sec))
            return false;

        //"ÉèÖÃGPRSÒÆ¶¯Ì¨ÀàÐÍÎªB\r");
        if(!serialCommunicationCMDFunction("AT+CGCLASS=\"B\"\r", GprsSerialDefine.GPRS_OK, GprsSerialDefine.SERIAL_READ_WAIT_TIME_2_sec))
            return false;

        //"¶¨ÒåPDPÉÏÏÂÎÄ£º1, IP, CMNET\r");
        if(!serialCommunicationCMDFunction("AT+CGDCONT=1,\"IP\",\"CMNET\"\r", GprsSerialDefine.GPRS_OK, GprsSerialDefine.SERIAL_READ_WAIT_TIME_2_sec))
            return false;

        //"¸½×ÅGPRSÒµÎñ\r");
        if(!serialCommunicationCMDFunction("AT+CGATT=1\r", GprsSerialDefine.GPRS_OK, GprsSerialDefine.SERIAL_READ_WAIT_TIME_2_sec))
            return false;

        //"ÉèÖÃGPRSÁ¬½ÓÄ£Ê½Îª: GPRS\r");
        if(!serialCommunicationCMDFunction("AT+CIPCSGP=1,\"CMNET\"\r", GprsSerialDefine.GPRS_OK, GprsSerialDefine.SERIAL_READ_WAIT_TIME_2_sec))
            return false;

        //"ÉèÖÃÎªµ¥IPÁ¬½ÓÄ£Ê½\r");
        if(!serialCommunicationCMDFunction("AT+CIPMUX=0\r", GprsSerialDefine.GPRS_OK, GprsSerialDefine.SERIAL_READ_WAIT_TIME_2_sec))
            return false;

        //"ÉèÖÃ½ÓÊÕÊý¾ÝÏÔÊ¾IPÍ·\r");
        if(!serialCommunicationCMDFunction("AT+CIPHEAD=0\r", GprsSerialDefine.GPRS_OK, GprsSerialDefine.SERIAL_READ_WAIT_TIME_2_sec))
            return false;

        //ÉèÖÃÎªÍ¸´«Ä£Ê½
        if(!serialCommunicationCMDFunction("AT+CIPMODE=1\r", GprsSerialDefine.GPRS_OK, GprsSerialDefine.SERIAL_READ_WAIT_TIME_2_sec))
            return false;

        //"·¢ÆðÁ¬½Ó\r");
        if(serialCommunicationCMDFunction("AT+CIPSTART=\"TCP\",\"116.62.108.201\",\"8080\"\r", "CONNECT", GprsSerialDefine.SERIAL_READ_WAIT_TIME_20_sec))
        {
            try {
                Thread.sleep(2000, 0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }
        else
            return false;
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////
    //services
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void uploadDeviceInfo(Context context, DeviceInfo deviceInfo)
    {
        String deviceinfobody = String.format("did=%s&dversion=%d&addr=%s&name=%s&type=%s&lang=%s", deviceInfo.getDeviceid(), PublicValueDefine.APP_VERSION,
                deviceInfo.getAddr(), deviceInfo.getUser(), PublicStringDefine.DEVICE_TYPE, PublicStringDefine.Device_Language);
        String sendBuf = String.format("POST %s HTTP/1.1\nHost: 116.62.108.201:8080\nConnection: keep-alive\nContent-Length: %d\nContent-Type: application/x-www-form-urlencoded;charset=GBK\nAccept-Language: zh-CN,zh;q=0.8\n\n%s",
                GprsSerialDefine.NcdServerUpDeviceUrlStr, deviceinfobody.length(), deviceinfobody);

        int readSize = serialCommunicationDataFunction(sendBuf, GprsSerialDefine.SERIAL_READ_WAIT_TIME_20_sec);
        String readData = new String(recvBuf, 0, readSize);
        Log.i("xsx", "recv data: "+readData);

        if(readData.contains(GprsSerialDefine.HTTP_RESPONE_SUCCESS))
        {
            Log.i("xsx", "上传成功");
            MySdcardSharedPreferences.getInstance().putBoolean(MySdcardSharedPreferences.Keys.DEVICE_STATE_KEY, false)
            .commit();

            String[] strs = readData.split("success");
            String dateTime = strs[1].substring(6, 20);

            DateTimeTool.setSystemDateTime(context, dateTime);
        }
    }

    public void uploadYgfxyData(Context context)
    {
        String sendBuf = "POST /NCD_Server/UpLoadYGFXY HTTP/1.1\n" +
                "Host: 116.62.108.201:8080\n" +
                "Connection: keep-alive\n" +
                "Content-Length:1557\n" +
                "Content-Type:application/x-www-form-urlencoded;charset=GBK\n" +
                "Accept-Language: zh-CN,zh;q=0.8\n" +
                "\n" +
                "cardnum=00191&qrdata.cid=IB1710-01&device.did=NCD13011701101&tester=xsxx&sampleid=14&testtime=2017-12-5 16:42:37&overtime=0&cline=246&tline=164&bline=200&t_c_v=2.5338&t_tc_v=214571299250103960000000000000.0000&testv=17929&serialnum=IB1710-01-00191&t_isok=true&cparm=38&t_cv=815520896.0000&c_cv=0.0000&testaddr=11112222&errcode=99&series=[230,233,235,237,238,240,241,241,242,243,244,244,245,245,246,246,247,247,247,248,248,248,248,249,249,248,248,248,248,248,248,248,248,248,248,248,248,248,248,248,248,248,249,249,249,249,249,249,249,249,250,250,250,250,250,250,250,250,250,250,250,250,250,250,250,250,250,251,251,251,251,251,251,251,252,252,252,253,253,253,254,254,254,255,255,256,256,256,257,257,258,259,259,260,260,261,261,261,261,262,262,262,262,262,262,261,261,261,261,260,260,260,260,260,260,260,260,260,260,260,260,260,260,261,261,261,261,261,261,261,261,261,261,261,262,262,262,263,263,264,265,267,270,274,281,291,307,330,362,407,466,542,636,746,870,1006,1148,1291,1432,1564,1682,1780,1855,1904,1924,1915,1879,1815,1727,1621,1499,1367,1232,1099,970,849,739,638,549,472,407,352,308,273,246,225,209,197,188,181,176,173,170,168,167,166,165,164,164,164,163,163,163,163,163,163,163,163,163,163,163,163,163,163,163,163,163,164,164,164,164,165,166,167,169,172,176,182,191,205,223,247,277,314,358,408,462,518,576,633,687,737,780,816,841,856,858,849,829,798,760,714,664,612,559,505,454,407,363,323,289,259,234,214,198,185,176,168,163,159,156,153,152,151,150,149,149,148,148,148,148,148,148,148,148,148,148,148,148,148,148,148,148,148,148,148,148,148,149,149]";

        int readSize = serialCommunicationDataFunction(sendBuf, GprsSerialDefine.SERIAL_READ_WAIT_TIME_30_sec);
        String readData = new String(recvBuf, 0, readSize);
        Log.i("xsx", "recv data: "+readData);

        if(readData.contains(GprsSerialDefine.HTTP_RESPONE_SUCCESS))
        {
            Log.i("xsx", "上传成功");
        }
    }

    public boolean checkConnectIsOK(Context context, DeviceInfo deviceInfo)
    {
        /*String deviceinfobody = String.format("did=%s&dversion=%d&addr=%s&name=%s&type=%s&lang=%s", deviceInfo.getDeviceid(), PublicValueDefine.APP_VERSION,
                deviceInfo.getAddr(), deviceInfo.getUser(), PublicStringDefine.DEVICE_TYPE, PublicStringDefine.Device_Language);
        String sendBuf = String.format("POST %s HTTP/1.1\nHost: 116.62.108.201:8080\nConnection: keep-alive\nContent-Length: %d\nContent-Type: application/x-www-form-urlencoded;charset=GBK\nAccept-Language: zh-CN,zh;q=0.8\n\n%s",
                GprsSerialDefine.NcdServerUpDeviceUrlStr, deviceinfobody.length(), deviceinfobody);

        int readSize = serialCommunicationDataFunction(sendBuf, GprsSerialDefine.SERIAL_READ_WAIT_TIME_20_sec);
        String readData = new String(recvBuf, 0, readSize);
        Log.i("xsx", "recv data: "+readData);

        if(readData.contains(GprsSerialDefine.HTTP_RESPONE_SUCCESS))
        {
            Log.i("xsx", "上传成功");
            MySdcardSharedPreferences.getInstance().putBoolean(MySdcardSharedPreferences.Keys.DEVICE_STATE_KEY, false)
                    .commit();

            String[] strs = readData.split("success");
            String dateTime = strs[1].substring(6, 20);

            DateTimeTool.setSystemDateTime(context, dateTime);
        }*/

        return true;
    }

}


