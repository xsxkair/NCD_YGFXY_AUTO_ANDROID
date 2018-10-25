package com.ncd.xsx.ncd_ygfxy.Activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.EthernetManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.ncd.xsx.ncd_ygfxy.Databases.Services.CardService;
import com.ncd.xsx.ncd_ygfxy.Databases.Services.PatientService;
import com.ncd.xsx.ncd_ygfxy.Databases.Services.TestDataService;
import com.ncd.xsx.ncd_ygfxy.Databases.Services.UserService;
import com.ncd.xsx.ncd_ygfxy.Defines.PublicStringDefine;
import com.ncd.xsx.ncd_ygfxy.Logger.LogException;
import com.ncd.xsx.ncd_ygfxy.Logger.LoggerUnits;
import com.ncd.xsx.ncd_ygfxy.R;
import com.ncd.xsx.ncd_ygfxy.SerialDriver.DeviceSerial.DeviceSerialDriver;
import com.ncd.xsx.ncd_ygfxy.Services.CommonService.CommonService;
import com.ncd.xsx.ncd_ygfxy.Services.DeviceControlBoardService.DeviceService;
import com.ncd.xsx.ncd_ygfxy.Services.TestService.TestService;
import com.ncd.xsx.ncd_ygfxy.Tools.ImgTool;
import com.ncd.xsx.ncd_ygfxy.Tools.MyQRCodeDecodeTool;
import com.ncd.xsx.ncd_ygfxy.Tools.MySdcardSharedPreferences;
import com.ncd.xsx.ncd_ygfxy.Tools.MySharedPreferences;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class LunchActivity extends Activity {

    ViewFlipper viewFlipper;
    TextView systeminitTextView;

    private Disposable systemInitDisposable = null;
    private Observer<String> systemInitObserver = null;
    private Observable<String> systemInitObservable = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch);

        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        systeminitTextView = (TextView) findViewById(R.id.systeminitTextView);

        viewFlipper.startFlipping();
        viewFlipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(LunchActivity.this, MainActivity.class));
                //startActivity(new Intent(LunchActivity.this, AdbActivity.class));
            }
        });

        systemInitObserver = new Observer<String>() {

            @Override
            public void onSubscribe(Disposable disposable) {
                systemInitDisposable = disposable;
            }

            @Override
            public void onNext(String str) {
                systeminitTextView.setText(str);
                LoggerUnits.info(str);

                /*if(str.equals("read ethernet ok")){
                    try {
                        EthernetSet.setSystemEthernet(LunchActivity.this, ethernetConfig);
                    } catch (Exception e) {
                        Toast.makeText(LunchActivity.this, "设置系统ethernet失败", Toast.LENGTH_SHORT).show();
                        LoggerUnits.error("设置系统ethernet失败", e);
                    }
                }*/
            }

            @Override
            public void onError(Throwable throwable) {

                systeminitTextView.setText(throwable.getMessage());

                if(throwable instanceof LogException)
                    Log.e("xsx", "logutil init fail");
                else{
                    LoggerUnits.error("系统初始化错误", throwable);
                }

                if(systemInitDisposable != null && !systemInitDisposable.isDisposed())
                    systemInitDisposable.dispose();
            }

            @Override
            public void onComplete() {

                LoggerUnits.info("系统初始化完成");

                startActivity(new Intent(LunchActivity.this, MainActivity.class));

                if(systemInitDisposable != null && !systemInitDisposable.isDisposed())
                    systemInitDisposable.dispose();
            }
        };

        systemInitObservable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                //创建纽康度数据文件夹，并且在内部创建log文件夹
                LoggerUnits.LoggerUnitsInit();

                UserService.getInstance().UserServiceInit(LunchActivity.this);
                CardService.getInstance().CardServerInit(LunchActivity.this);
                TestDataService.getInstance().TestDataServiceInit(LunchActivity.this);
                PatientService.getInstance().serviceInit(LunchActivity.this);
                emitter.onNext(" 数据库初始化完成");
                Thread.sleep(100);

                MySdcardSharedPreferences.getInstance().mySdcardSharedPreferencesInit(LunchActivity.this);
                emitter.onNext(" 系统参数加载完成");
                Thread.sleep(100);

                //设置设备信息为新，实现开机上传一次
                MySdcardSharedPreferences.getInstance().putBoolean(MySdcardSharedPreferences.Keys.DEVICE_STATE_KEY, true)
                        .commit();

                DeviceSerialDriver.getInstance().DeviceSerialInit();
                emitter.onNext(" 控制端口初始化完成");
                Thread.sleep(100);

                Intent commonServiceIntent = new Intent(LunchActivity.this, CommonService.class);
                startService(commonServiceIntent);

                Intent deviceServiceIntent = new Intent(LunchActivity.this, DeviceService.class);
                startService(deviceServiceIntent);

                Intent testServiceIntent = new Intent(LunchActivity.this, TestService.class);
                startService(testServiceIntent);

             /*   Settings.System.putString(LunchActivity.this.getContentResolver(), Settings.System.NCD_DEVICE_ADDR, "张雄");
                Log.d("xsx", Settings.System.getString(LunchActivity.this.getContentResolver(), "ncd_device_id"));
                Log.d("xsx", Settings.System.getString(LunchActivity.this.getContentResolver(), "ncd_device_addr"));
                Log.d("xsx", Settings.System.getString(LunchActivity.this.getContentResolver(), "ncd_device_user"));
                Log.d("xsx", Settings.System.getString(LunchActivity.this.getContentResolver(), "ncd_device_id"));
*/
                MySharedPreferences.putValue(LunchActivity.this, "xsx", "aaa");

                //MyQRCodeDecodeTool.decode("/mnt/sdcard/whnewcando/20181016003.jpg");
                //ImgTool.myimage(LunchActivity.this, "/mnt/sdcard/whnewcando/20181016003.jpg");

                //ImgTool.imgline(LunchActivity.this, "/mnt/sdcard/whnewcando/aaa.jpg");
                //for(int i=1; i<10; i++)
                 //   ImgTool.imgline(LunchActivity.this, String.format("/mnt/sdcard/whnewcando/pic/%d.jpg", i));
                //ImgTool.imgline(LunchActivity.this, "/mnt/sdcard/whnewcando/pic/3.jpg");
                //ImgTool.imgline(LunchActivity.this, "/mnt/sdcard/whnewcando/pic/4.jpg");
                //ImgTool.imgline(LunchActivity.this, "/mnt/sdcard/whnewcando/pic/5.jpg");
                //ImgTool.imgline(LunchActivity.this, "/mnt/sdcard/whnewcando/pic/6.jpg");
                //ImgTool.imgline(LunchActivity.this, "/mnt/sdcard/whnewcando/20181016003.jpg");
                /*GprsSerialDriver.getInstance().GprsSerialInit();
                emitter.onNext(" GPRS初始化完成");
                Thread.sleep(100);

                Intent commonServiceIntent = new Intent(LunchActivity.this, CommonService.class);
                startService(commonServiceIntent);
                Intent deviceServiceIntent = new Intent(LunchActivity.this, DeviceService.class);
                startService(deviceServiceIntent);
                Intent gprsServiceIntent = new Intent(LunchActivity.this, GprsService.class);
                startService(gprsServiceIntent);
*/
                /*while(false == SerialMethods.getInstance().getGprsSerialService().checkGprsIsOn()){
                    emitter.onNext(" GPRS准备中");
                    Thread.sleep(1000);
                }*/

                /*while (DeviceSerialDriver.getInstance().checkControlBordIsReady() == false) {
                    emitter.onNext(" 运动系统准备中");
                    Thread.sleep(1000);
                }*/

                emitter.onNext(" 初始化完成");
                Thread.sleep(100);

                emitter.onComplete();
            }
        });

        systemInitObservable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(systemInitObserver);

        Log.i("xsx", "SHA1: "+sHA1(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        systemInitDisposable = null;
        systemInitObserver = null;
        systemInitObservable = null;
    }

    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length()-1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
