package com.ncd.xsx.ncd_ygfxy.Activitys;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ncd.xsx.ncd_ygfxy.Defines.PublicStringDefine;
import com.ncd.xsx.ncd_ygfxy.R;
import com.ncd.xsx.ncd_ygfxy.RxBus.RxBus;
import com.ncd.xsx.ncd_ygfxy.RxBus.ServiceStatuMsg;
import com.ncd.xsx.ncd_ygfxy.Tools.DateTimeTool;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public abstract class MyActivity extends Activity {

    private RelativeLayout includeTopLayout;
    private TextView system_time_textview;
    private ImageView system_net_statu_imageview;
    private ImageView device_service_statu_imageview;
    private ImageView gprs_service_statu_imageview;
    private ImageButton back_button;
    private Disposable serviceDisposable;

    private BroadcastReceiver mReceiver;
    private IntentFilter mFilter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFilter = new IntentFilter();
        mFilter.addAction(Intent.ACTION_TIME_TICK);
        mFilter.addAction(Intent.ACTION_TIME_CHANGED);
        mFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();

                update_system_datetime();

                if(ConnectivityManager.CONNECTIVITY_ACTION.equals(action))
                    update_connect_status();
            }
        };
        Log.d(PublicStringDefine.OWNER_TAG, getClass().getSimpleName()+ " -- onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();


        Log.d(PublicStringDefine.OWNER_TAG, getClass().getSimpleName()+ " -- onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.d(PublicStringDefine.OWNER_TAG, getClass().getSimpleName()+ " -- onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();

        update_system_datetime();
        update_connect_status();
        registerReceiver(mReceiver, mFilter);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Log.d(PublicStringDefine.OWNER_TAG, getClass().getSimpleName()+ " -- onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(mReceiver);

        Log.d(PublicStringDefine.OWNER_TAG, getClass().getSimpleName()+ " -- onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(PublicStringDefine.OWNER_TAG, getClass().getSimpleName()+ " -- onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        RxBus.getInstance().unregister(this.serviceDisposable);
        Log.d(PublicStringDefine.OWNER_TAG, getClass().getSimpleName()+ " -- onDestroy");
    }

    private String getActivityName()
    {
        return getClass().getSimpleName();
    }

    public void activityFresh(){

    }

    public void MyActivityCommonInit(){
        includeTopLayout = (RelativeLayout) findViewById(R.id.include);
        system_time_textview = (TextView) includeTopLayout.findViewById(R.id.system_time_textview);
        system_net_statu_imageview = (ImageView) includeTopLayout.findViewById(R.id.system_net_statu_imageview);
        device_service_statu_imageview = (ImageView) includeTopLayout.findViewById(R.id.device_service_statu_imageview);
        gprs_service_statu_imageview = (ImageView) includeTopLayout.findViewById(R.id.gprs_service_statu_imageview);

        try
        {
            back_button = (ImageButton) includeTopLayout.findViewById(R.id.back_button);

            back_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }catch (Exception e){
            back_button = null;
        }

        serviceDisposable = RxBus.getInstance().toObservable(ServiceStatuMsg.class, new Consumer<ServiceStatuMsg>() {
            @Override
            public void accept(ServiceStatuMsg msg) throws Exception {

                switch (msg.getStatu())
                {
                    case ServiceStatuMsg.DEVICE_SERVICE_LIVE:
                        device_service_statu_imageview.setImageResource(R.drawable.device_service_live);
                        break;
                    case ServiceStatuMsg.DEVICE_SERVICE_DEAD:
                        device_service_statu_imageview.setImageResource(R.drawable.device_service_dead);
                        break;
                    case ServiceStatuMsg.GPRS_SERVICE_DEAD:
                        gprs_service_statu_imageview.setImageResource(R.drawable.gprs_service_dead);
                        break;
                    case ServiceStatuMsg.GPRS_SERVICE_LIVE:
                        gprs_service_statu_imageview.setImageResource(R.drawable.gprs_service_live);
                        break;
                }
            }
        });
    }

    private void update_system_datetime()
    {
        system_time_textview.setText(DateTimeTool.getSystemDateTime_min());
    }

    private void update_connect_status()
    {
        ConnectivityManager connectivityManager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if(activeNetworkInfo != null && activeNetworkInfo.isConnected())
        {
            switch (activeNetworkInfo.getType())
            {
                case ConnectivityManager.TYPE_WIFI:
                    system_net_statu_imageview.setVisibility(View.VISIBLE);
                    if(back_button == null)
                        system_net_statu_imageview.setImageResource(R.drawable.net_wifi);
                    else
                        system_net_statu_imageview.setImageResource(R.drawable.net_wifi_white);
                    break;
                case ConnectivityManager.TYPE_ETHERNET:
                    system_net_statu_imageview.setVisibility(View.VISIBLE);
                    if(back_button == null)
                        system_net_statu_imageview.setImageResource(R.drawable.net_eth);
                    else
                        system_net_statu_imageview.setImageResource(R.drawable.net_eth_white);
                    break;
                default:
                    system_net_statu_imageview.setVisibility(View.GONE);
                    break;
            }

        }
        else
            system_net_statu_imageview.setVisibility(View.GONE);
    }

}
