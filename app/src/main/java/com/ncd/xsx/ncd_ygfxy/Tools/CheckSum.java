package com.ncd.xsx.ncd_ygfxy.Tools;

public class CheckSum {

    public static byte checkSum(byte[] data, int len){
        int sum = 0;

        if(len > data.length)
            return 0;

        for(int i=0; i<len; i++){
            sum += data[i];
        }

        return (byte)sum;
    }
}
