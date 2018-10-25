package com.ncd.xsx.ncd_ygfxy.Tools;

import android.util.Log;

import com.ncd.xsx.ncd_ygfxy.Logger.LoggerUnits;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ComTools {

    public static byte[] intToByteArray(int value) {
        byte[] byteArray = new byte[4];

        byteArray[0] = (byte)(value>>24);
        byteArray[1] = (byte)(value>>16);
        byteArray[2] = (byte)(value>>8);
        byteArray[3] = (byte)(value);

        return byteArray;
    }
}
