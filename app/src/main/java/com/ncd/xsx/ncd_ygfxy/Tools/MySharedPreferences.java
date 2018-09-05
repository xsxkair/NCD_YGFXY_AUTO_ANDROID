package com.ncd.xsx.ncd_ygfxy.Tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MySharedPreferences {

    private final static String settingFileName = "xsx_set";

    public static void putValue(Context context, String key, int value){

        Editor sp = context.getSharedPreferences(settingFileName, Context.MODE_PRIVATE).edit();
        sp.putInt(key, value);
        sp.commit();
    }

    public static void putValue(Context context, String key, boolean value){
        Editor sp = context.getSharedPreferences(settingFileName, Context.MODE_PRIVATE).edit();
        sp.putBoolean(key, value);
        sp.commit();
    }

    public static void putValue(Context context, String key, float value){
        Editor sp = context.getSharedPreferences(settingFileName, Context.MODE_PRIVATE).edit();
        sp.putFloat(key, value);
        sp.commit();
    }

    public static void putValue(Context context, String key, long value){
        Editor sp = context.getSharedPreferences(settingFileName, Context.MODE_PRIVATE).edit();
        sp.putLong(key, value);
        sp.commit();
    }

    public static void putValue(Context context, String key, String value){
        Editor sp = context.getSharedPreferences(settingFileName, Context.MODE_PRIVATE).edit();
        sp.putString(key, value);
        sp.commit();
    }

    public static int getValue(Context context, String key, int defValue) {
        SharedPreferences sp = context.getSharedPreferences(settingFileName, Context.MODE_PRIVATE);
        int value = sp.getInt(key, defValue);
        return value;
    }

    public static boolean getValue(Context context, String key, boolean defValue) {
        SharedPreferences sp = context.getSharedPreferences(settingFileName, Context.MODE_PRIVATE);
        boolean value = sp.getBoolean(key, defValue);
        return value;
    }

    public static String getValue(Context context, String key, String defValue) {
        SharedPreferences sp = context.getSharedPreferences(settingFileName, Context.MODE_PRIVATE);
        String value = sp.getString(key, defValue);
        return value;
    }

    public static float getValue(Context context, String key, float defValue) {
        SharedPreferences sp = context.getSharedPreferences(settingFileName, Context.MODE_PRIVATE);
        float value = sp.getFloat(key, defValue);
        return value;
    }

    public static long getValue(Context context, String key, long defValue) {
        SharedPreferences sp = context.getSharedPreferences(settingFileName, Context.MODE_PRIVATE);
        long value = sp.getLong(key, defValue);
        return value;
    }

}
