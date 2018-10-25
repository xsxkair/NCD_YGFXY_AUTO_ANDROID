package com.ncd.xsx.ncd_ygfxy.SerialDriver.DeviceSerial;

public class DeviceSerialDefine {

    public static final String SERIAL_FILE_NAME = "/dev/ttySAC4";
    public static final long SERIAL_BAUD = 115200;

    public static final int SERIAL_BUSY_WAIT_TIME = 100;                      //ms
    public static final int SERIAL_READ_WAIT_TIME = 100000;                      //us
    public static final int SERIAL_ACK_WAIT_TIME = 5000;                      //ms

    public static final int SERIAL_TEMP_READBUF_LEN = 128;
    public static final int SERIAL_READBUF_LEN = 4096;

    //同步码
    public static final int SYN_CODE = 0x27;

    //会话发起者
    public static final int SESSION_ANDROID = 0x11;
    public static final int SESSION_STM32 = 0x12;

    //命令定义
    public static final int ERROR_CMD = 0x8000;
    public static final int NEW_SAMPLE_INPUT_CMD = 0x8001;                  //读取到样本编号
    public static final int MOTOR_1_MOVE_CMD = 0x8002;
    public static final int MOTOR_2_MOVE_CMD = 0x8003;
    public static final int MOTOR_3_MOVE_CMD = 0x8004;
    public static final int MOTOR_4_MOVE_CMD = 0x8005;
    public static final int MOTOR_5_MOVE_CMD = 0x8006;
    public static final int MOTOR_6_MOVE_CMD = 0x8007;
    public static final int DEVICE_NO_ACK_CMD = 0x8008;

    //常用数据定义
    public static final String DEVICE_NO_ACK_STRING = "device no ack";
    public static final int ACK_OK = 0x01;
    public static final int ACK_FAIL = 0x02;
}
