package com.ncd.xsx.ncd_ygfxy.Tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

//import com.github.yoojia.qrcode.qrcode.QRCodeDecoder;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;

public class MyQRCodeDecodeTool {

    private static final QRCodeDecoder mDecoder;

    static {
        mDecoder = new QRCodeDecoder.Builder().build();
    }

    public static String decode(String filePath){
        Bitmap mBitmap = BitmapFactory.decodeFile(filePath);
        return decode(mBitmap);
    }

    public static String decode(Bitmap mBitmap){
        return mDecoder.decode(mBitmap);
    }

    public static ResultPoint[] getResultPoint(){
        return  mDecoder.getResultPoints();
    }
}
