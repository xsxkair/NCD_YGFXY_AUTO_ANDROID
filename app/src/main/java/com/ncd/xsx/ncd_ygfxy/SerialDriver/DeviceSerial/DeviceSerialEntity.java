package com.ncd.xsx.ncd_ygfxy.SerialDriver.DeviceSerial;

import android.util.Log;

import com.ncd.xsx.ncd_ygfxy.Logger.LoggerUnits;
import com.ncd.xsx.ncd_ygfxy.Tools.CheckSum;
import com.ncd.xsx.ncd_ygfxy.Tools.ComTools;

public class DeviceSerialEntity {

    public int SynCode;                                  // 1 byte -- 同步码

    public int SessionFrom;                                // 1 byte -- 会话发起者

    public int SessionId;                               //4 byte -- 会话编号

    public int cmd;                                    // 2 byte

    public int datalen;                                // 2 byte

    public byte[] data;                         //change enable

    public int checksum;                               // deviceaddr - data, 1 byte

    public DeviceSerialEntity(){

    }

    public DeviceSerialEntity(int sessionFrom, int serrionId, int cmd, byte[] data) {
        this.SynCode = DeviceSerialDefine.SYN_CODE;
        this.SessionFrom = sessionFrom;
        this.SessionId = serrionId;
        this.cmd = cmd;
        this.data = data;
        this.datalen = this.data.length;
    }

    public DeviceSerialEntity(int sessionFrom, int serrionId, int cmd, String data) {
        this.SynCode = DeviceSerialDefine.SYN_CODE;
        this.SessionFrom = sessionFrom;
        this.SessionId = serrionId;
        this.cmd = cmd;
        this.data = data.getBytes();
        this.datalen = this.data.length;
    }
    /*
        public DeviceSerialEntity(int sessionId, int cmd, int maxcnt, byte ... data) {
            this.SynCode = DeviceSerialDefine.SYN_CODE;
            this.SessionId = sessionId;
            this.cmd = cmd;
            this.data = data;
            this.datalen = this.data.length;
            this.SendTime = 0;
            this.ReSendCnt = 0;
            this.MaxSendCnt = maxcnt;
        }

        public DeviceSerialEntity(int sessionId, int cmd, int maxcnt, int data) {
            this.SynCode = DeviceSerialDefine.SYN_CODE;
            this.SessionId = sessionId;
            this.cmd = cmd;
            this.data = ComTools.intToByteArray(data);
            this.datalen = this.data.length;
            this.SendTime = 0;
            this.ReSendCnt = 0;
            this.MaxSendCnt = maxcnt;
        }
    */
    /*
    * 由于传入的数组为接受缓冲区，故需传入真是数据长度
     */
    public static DeviceSerialEntity ParseBytesToDeviceSerialEntity(byte[] datas, int datalen){

        int sum = 0;

        if(datas[0] != DeviceSerialDefine.SYN_CODE)
        {
            LoggerUnits.error("recv data from device SynCode error");
            return null;
        }

        if(datalen < 11 || datalen > 128)
        {
            LoggerUnits.error("recv data from device length error");
            return null;
        }

        DeviceSerialEntity serialEntity = new DeviceSerialEntity();

        serialEntity.SynCode = datas[0];

        serialEntity.SessionFrom = datas[1];

        serialEntity.SessionId = (datas[2]&0xff)<<24 | (datas[3]&0xff)<<16 | (datas[4]&0xff)<<8 | (datas[5]&0xff);

        serialEntity.cmd = (datas[6]&0xff)<<8 | (datas[7]&0xff);

        serialEntity.datalen = (datas[8]&0xff)<<8 | (datas[9]&0xff);

        //如果有数据
        if(serialEntity.datalen > 0)
        {
            serialEntity.data = new byte[serialEntity.datalen];
            System.arraycopy(datas, 10, serialEntity.data, 0, serialEntity.datalen);
        }

        serialEntity.checksum = datas[serialEntity.datalen+10];

        //check sum
        sum = CheckSum.checkSum(datas, serialEntity.datalen + 10);
        if(serialEntity.checksum == sum)
            return serialEntity;
        else
        {
            LoggerUnits.error("recv data from device checksum error");
            return null;
        }
    }

    public byte[] ParseDeviceSerialEntityToBytes(){
        byte[] sendByte = new byte[datalen + 11];

        sendByte[0] = (byte)SynCode;
        sendByte[1] = (byte)(SessionFrom);
        sendByte[2] = (byte)(SessionId>>24);
        sendByte[3] = (byte)(SessionId>>16);
        sendByte[4] = (byte)(SessionId>>8);
        sendByte[5] = (byte)(SessionId);
        sendByte[6] = (byte)(cmd>>8);
        sendByte[7] = (byte)(cmd);

        sendByte[8] = (byte)(datalen>>8);
        sendByte[9] = (byte)(datalen);

        if(datalen > 0)
            System.arraycopy(data, 0, sendByte, 10, datalen);

        sendByte[sendByte.length - 1] = CheckSum.checkSum(sendByte, sendByte.length - 1);

        return sendByte;
    }

    @Override
    public String toString() {
        byte[] txbyte = this.ParseDeviceSerialEntityToBytes();
        StringBuffer s = new StringBuffer();

        for(int i=0; i<txbyte.length; i++)
        {
            s.append(String.format(" 0x%02x", txbyte[i]));
        }
        return s.toString();

    }
}
