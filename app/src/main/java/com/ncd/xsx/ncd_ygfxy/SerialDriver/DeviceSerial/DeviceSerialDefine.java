package com.ncd.xsx.ncd_ygfxy.SerialDriver.DeviceSerial;

public class DeviceSerialDefine {

    public static final String SERIAL_FILE_NAME = "/dev/ttySAC4";
    public static final long SERIAL_BAUD = 115200;
    public static final int SERIAL_READ_WAIT_TIME = 500000;                      //ms

    public static final int SERIAL_READBUF_LEN = 4096;

    //设备地址
    public static final int CONTROL_BORD_ADDR = 0x01;

    //命令类型定义
    public static final int FUNCTION_READ = 0x03;
    public static final int FUNCTION_WRITE = 0x05;

    //命令定义
    public static final int CHECK_CONTROL_BORD_IS_READY = 0x8001;
}
