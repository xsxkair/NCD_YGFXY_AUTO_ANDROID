package com.ncd.xsx.ncd_ygfxy.Activitys;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.yoojia.qrcode.qrcode.QRCodeEncoder;
import com.ncd.xsx.ncd_ygfxy.R;

public class AboutUsActivity extends MyActivity {

    ImageView ncdQrImageView;
    TextView softVersionValueTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        super.MyActivityCommonInit();

        ncdQrImageView = (ImageView) findViewById(R.id.ncdQrImageView);
        softVersionValueTextView = (TextView) findViewById(R.id.softVersionValueTextView);

        // 生成的二维码图案
        Bitmap qrCodeImage = new QRCodeEncoder.Builder()
                .width(99) // 二维码图案的宽度
                .height(99)
                .paddingPx(0) // 二维码的内边距
                .marginPt(0) // 二维码的外边距
                .build()
                .encode(getResources().getString(R.string.Ncd_Company_Website_text));

        ncdQrImageView.setImageBitmap(qrCodeImage);

        softVersionValueTextView.setText(getResources().getString(R.string.app_version));
    }
}
