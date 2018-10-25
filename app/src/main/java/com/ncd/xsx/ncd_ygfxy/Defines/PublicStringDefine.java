package com.ncd.xsx.ncd_ygfxy.Defines;

public class PublicStringDefine {

    public static final String ADMIN_PASSWD = "201300";
    //设备类型
    public static final String DEVICE_TYPE = "YGFXY_3";

    public static final String Device_Language = "CH";

    public static final String OWNER_TAG = "xsx";

    //数据顶级文件夹
    public static final String APP_DATA_DIR = "/mnt/sdcard/whnewcando";

    //二级文件夹 -- log
    public static final String APP_DATA_LOG_DIR = "/mnt/sdcard/whnewcando/Log";

    //数据库路径
    public static final String dataBaseFileName = "/mnt/sdcard/whnewcando/xsx.db";

    public static final String EMPTY_STRING = "";

    public static final String ACTION_FAIL_STRING = "操作失败！";

    /*
    **自定义广播
     */
    //每秒一次广播，有的界面需要实时刷新，此广播作为刷新动力
    public static final String MY_SEC_BROADCAST = "com.xsx.ygfxy.my_sec_broadcast";

    //从串口读取到新的试管条码
    public static final String NEW_SAMPLE_INPUT_BROADCASR = "com.xsx.ygfxy.my_new_sample_input_broadcast";
    public static final String NEW_SAMPLE_INTENT_KEY = "NEW_SAMPLE_FOUND";
}
