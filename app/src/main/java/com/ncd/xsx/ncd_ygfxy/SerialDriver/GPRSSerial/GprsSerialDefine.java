package com.ncd.xsx.ncd_ygfxy.SerialDriver.GPRSSerial;

public class GprsSerialDefine {

    public static final String SERIAL_FILE_NAME = "/dev/ttySAC2";
    public static final long SERIAL_BAUD = 115200;

    public static final int SERIAL_READ_WAIT_TIME_20_sec = 20;                      //s
    public static final int SERIAL_READ_WAIT_TIME_30_sec = 30;                      //s
    public static final int SERIAL_READ_WAIT_TIME_2_sec = 2;                      //s
    public static final int SERIAL_READ_WAIT_TIME_0_usec = 0;                      //s

    public static final int SERIAL_READBUF_LEN = 5000;

    public static final String GPRS_AT_CMD = "AT\r\n";
    public static final String GPRS_OK = "OK";

    //url
    public static final String NcdServerUpDeviceUrlStr = "/NCD_Server/up_device";

    public static final String HTTP_RESPONE_SUCCESS = "success";
}
