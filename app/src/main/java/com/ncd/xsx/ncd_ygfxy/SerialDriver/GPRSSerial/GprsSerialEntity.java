package com.ncd.xsx.ncd_ygfxy.SerialDriver.GPRSSerial;

import com.ncd.xsx.ncd_ygfxy.Tools.CheckSum;

public class GprsSerialEntity {

    private int deviceaddr;                                  // 1 byte

    private int functioncode;                          // 1 byte

    private int cmd;                                    // 2 byte

    private int datalen;                                // 2 byte

    private byte[] data;                         //change enable

    private int checksum;                               // deviceaddr - data, 1 byte

    public GprsSerialEntity(){

    }

    public GprsSerialEntity(int deviceaddr, int functioncode, int cmd, byte ... data) {
        this.deviceaddr = deviceaddr;
        this.functioncode = functioncode;
        this.cmd = cmd;
        this.datalen = data.length;
        this.data = data;
    }


    public int getDeviceaddr() {
        return deviceaddr;
    }

    public void setDeviceaddr(int deviceaddr) {
        this.deviceaddr = deviceaddr;
    }

    public int getFunctioncode() {
        return functioncode;
    }

    public void setFunctioncode(int functioncode) {
        this.functioncode = functioncode;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public int getDatalen() {
        return datalen;
    }

    public void setDatalen(int datalen) {
        this.datalen = datalen;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getChecksum() {
        return checksum;
    }

    public void setChecksum(int checksum) {
        this.checksum = checksum;
    }

    public byte[] changeToByteForSend(){
        byte[] sendByte = new byte[datalen + 7];

        sendByte[0] = (byte)deviceaddr;
        sendByte[1] = (byte)functioncode;
        sendByte[2] = (byte)(cmd>>8);
        sendByte[3] = (byte)(cmd);

        sendByte[4] = (byte)(datalen>>8);
        sendByte[5] = (byte)(datalen);

        System.arraycopy(data, 0, sendByte, 6, datalen);

        sendByte[sendByte.length - 1] = CheckSum.checkSum(sendByte, sendByte.length - 1);

        return sendByte;
    }

}
