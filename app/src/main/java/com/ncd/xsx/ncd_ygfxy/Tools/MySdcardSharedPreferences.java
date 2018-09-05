package com.ncd.xsx.ncd_ygfxy.Tools;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.ncd.xsx.ncd_ygfxy.Defines.PublicStringDefine;
import com.ncd.xsx.ncd_ygfxy.Databases.Entity.DeviceInfo;

import java.io.File;
import java.lang.reflect.Field;

public class MySdcardSharedPreferences {

    private final static String settingFilePath = "/mnt/sdcard/whnewcando/";
    private final static String settingFileName = "setting";

    private SharedPreferences sharedPreferences = null;
    private Editor editor = null;

    //在访问HttpMethods时创建单例
    private static class SingletonHolder{
        private static final MySdcardSharedPreferences INSTANCE = new MySdcardSharedPreferences();
    }

    //获取单例
    public static MySdcardSharedPreferences getInstance(){
        return MySdcardSharedPreferences.SingletonHolder.INSTANCE;
    }

    public void mySdcardSharedPreferencesInit(Context context) throws NoSuchFieldException, IllegalAccessException {
        //利用java反射机制将XML文件自定义存储
        Field field;
        // 获取ContextWrapper对象中的mBase变量。该变量保存了ContextImpl对象
        field = ContextWrapper.class.getDeclaredField("mBase");
        field.setAccessible(true);
        // 获取mBase变量
        Object obj = field.get(context);
        // 获取ContextImpl。mPreferencesDir变量，该变量保存了数据文件的保存路径
        field = obj.getClass().getDeclaredField("mPreferencesDir");
        field.setAccessible(true);
        // 创建自定义路径
        File file = new File(settingFilePath);
        // 修改mPreferencesDir变量的值
        field.set(obj, file);

        sharedPreferences = context.getSharedPreferences(settingFileName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public Editor getEditor() {
        return editor;
    }

    public void putValue(String key, Object value){
        String type = value.getClass().getSimpleName();

        if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) value);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) value);
        } else if ("String".equals(type)) {
            editor.putString(key, (String) value);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) value);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) value);
        }

        editor.commit();
    }

    public Editor putInt(String key, int value){
        editor.putInt(key, value);
        return editor;
    }

    public Editor putBoolean(String key, boolean value){
        editor.putBoolean(key, value);
        return editor;
    }

    public Editor putFloat(String key, float value){
        editor.putFloat(key, value);
        return editor;
    }

    public Editor putLong(String key, long value){
        editor.putLong(key, value);
        return editor;
    }

    public Editor putString(String key, String value){
        editor.putString(key, value);
        return editor;
    }

    public int getValue(String key, int defValue) {
        int value = sharedPreferences.getInt(key, defValue);
        return value;
    }

    public boolean getValue(String key, boolean defValue) {
        boolean value = sharedPreferences.getBoolean(key, defValue);
        return value;
    }

    public String getValue(String key, String defValue) {
        String value = sharedPreferences.getString(key, defValue);
        return value;
    }

    public float getValue(String key, float defValue) {
        float value = sharedPreferences.getFloat(key, defValue);
        return value;
    }

    public long getValue(String key, long defValue) {
        long value = sharedPreferences.getLong(key, defValue);
        return value;
    }

    public void readLatestDeviceInfo(DeviceInfo deviceInfo)
    {
        deviceInfo.setDeviceid(sharedPreferences.getString(Keys.DEVICE_ID_KEY, PublicStringDefine.EMPTY_STRING));
        deviceInfo.setUser(sharedPreferences.getString(Keys.DEVICE_USER_KEY, PublicStringDefine.EMPTY_STRING));
        deviceInfo.setPhone(sharedPreferences.getString(Keys.DEVICE_PHONE_KEY, PublicStringDefine.EMPTY_STRING));
        deviceInfo.setAddr(sharedPreferences.getString(Keys.DEVICE_ADDR_KEY, PublicStringDefine.EMPTY_STRING));
        deviceInfo.setState(sharedPreferences.getBoolean(Keys.DEVICE_STATE_KEY, false));
    }

    public class Keys {

        //设备信息
        public static final String DEVICE_ID_KEY = "deviceid";                                          //设备id
        public static final String DEVICE_USER_KEY = "deviceuser";                                      //责任人
        public static final String DEVICE_ADDR_KEY = "deviceaddr";                                      //设备地址
        public static final String DEVICE_PHONE_KEY = "devicephone";                                    //联系方式
        public static final String DEVICE_STATE_KEY = "devicestate";                                    //设备信息是否更新

        //有线网
        public static final String ETHERNET_DHCP_KEY = "eth_isdhcp";
        public static final String ETHERNET_IPV4_KEY = "eth_ipv4";
        public static final String ETHERNET_MASK_KEY = "eth_mask";
        public static final String ETHERNET_DNS_KEY = "eth_dns";
        public static final String ETHERNET_GATEWAY_KEY = "eth_gw";

        //服务器
        public static final String SERVER_IP_KEY = "server_ip";
        public static final String SERVER_PORT_KEY = "server_port";
    }
}
