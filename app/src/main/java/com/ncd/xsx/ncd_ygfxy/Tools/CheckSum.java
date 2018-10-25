package com.ncd.xsx.ncd_ygfxy.Tools;

public class CheckSum {

    public static byte checkSum(byte[] data, int len){
        int sum = 0;

        for(int i=0; i<len; i++){
            sum += data[i];
        }

        return (byte)sum;
    }

    public static boolean checkSumOk(byte[] data, int len){
        int sum = 0;
        int checksum = data[len-1];

        for(int i=0; i<len; i++){
            sum += data[i];
        }

        return (sum == checksum);
    }
}
