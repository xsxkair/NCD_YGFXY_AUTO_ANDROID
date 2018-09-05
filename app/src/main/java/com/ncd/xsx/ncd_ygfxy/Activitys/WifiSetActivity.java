package com.ncd.xsx.ncd_ygfxy.Activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.ncd.xsx.ncd_ygfxy.Activitys.Adapter.UserDecoration;
import com.ncd.xsx.ncd_ygfxy.Activitys.Adapter.WifiListAdapter;
import com.ncd.xsx.ncd_ygfxy.Activitys.Dialogs.ComfirmDialog;
import com.ncd.xsx.ncd_ygfxy.Activitys.Dialogs.InputDialog;
import com.ncd.xsx.ncd_ygfxy.Activitys.Listeners.OnViewItemClickListener;
import com.ncd.xsx.ncd_ygfxy.R;
import com.ncd.xsx.ncd_ygfxy.Tools.WifiTool;
import com.ncd.xsx.ncd_ygfxy.Databases.Entity.MyWifiInfo;

import java.util.ArrayList;
import java.util.List;

public class WifiSetActivity extends MyActivity {

    Switch wifiSwitchSwitch;
    RecyclerView wifiListview;

    private InputDialog wifiPasswordDialog;
    private static final String wifiPasswordDialogTag = "wifi";
    private String wifiPasswordDialogTitle = null;
    private String wifiPasswordDialogLabel = null;

    private ComfirmDialog comfirmDialog = null;
    private static final String comfirmDialogTag = "comfirm";
    private String comfirmDialogConnectButtonText = null;
    private String comfirmDialogCancelButtonText = null;
    private String comfirmDialogUnsaveButtonText = null;

    private MyWifiInfo selectWifiInfo = null;                                                  //当前选中的wifi
    private WifiConfiguration selectWifiConfiguration = null;
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener;
    private boolean wifiSwitchChangeEventEnable = true;
    private WifiListAdapter wifiListAdapter;
    private List<MyWifiInfo> myWifiInfoList = null;

    private WifiManager wifiManager;
    private Scanner wifiScanner;
    private BroadcastReceiver mReceiver;
    private IntentFilter mFilter;

    // Combo scans can take 5-6s to complete - set to 10s.
    private static final int WIFI_RESCAN_INTERVAL_MS = 10 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_set);

        super.MyActivityCommonInit();

        wifiSwitchSwitch = (Switch) findViewById(R.id.wifi_switch_switch);
        wifiListview = (RecyclerView) findViewById(R.id.wifi_listview);

        mFilter = new IntentFilter();
        mFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        mFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        mFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                handleEvent(intent);
            }
        };

        //WIFI 开关
        onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(!wifiSwitchChangeEventEnable)
                    return;

                if(!wifiManager.setWifiEnabled(isChecked)){
                    wifiSwitchSwitch.setEnabled(true);
                    Toast.makeText(WifiSetActivity.this, R.string.wifi_msg_error, Toast.LENGTH_SHORT).show();
                }
            }
        };
        wifiSwitchSwitch.setOnCheckedChangeListener(onCheckedChangeListener);

        wifiPasswordDialog = InputDialog.newInstance();
        wifiPasswordDialogTitle = getResources().getString(R.string.wifi_dialog_title);
        wifiPasswordDialogLabel = getResources().getString(R.string.wifi_dialog_input_label);
        wifiPasswordDialog.setOnInputDialogSubmit(new InputDialog.InputDialogSubmitListener() {
            @Override
            public void onSubmit(String msg, int userValue) {
                selectWifiConfiguration = WifiTool.createWifiConfig(selectWifiInfo.getSsid(), msg, selectWifiInfo.getSecurity());
                if(selectWifiConfiguration == null)
                    Toast.makeText(WifiSetActivity.this, getResources().getString(R.string.wifi_unsuport_security), Toast.LENGTH_SHORT).show();
                else
                {
                    if(!WifiTool.connectWifi(wifiManager, selectWifiConfiguration))
                        Toast.makeText(WifiSetActivity.this, getResources().getString(R.string.wifi_state_FAILED), Toast.LENGTH_SHORT).show();

                }
            }
        });

        comfirmDialog = ComfirmDialog.newInstance();
        comfirmDialogConnectButtonText = getResources().getString(R.string.button_connect_text);
        comfirmDialogCancelButtonText = getResources().getString(R.string.button_cancel_text);
        comfirmDialogUnsaveButtonText = getResources().getString(R.string.button_unsave_text);
 /*       comfirmDialog.setDialogItemSelectListener(new DialogItemSelectListener<String>() {
            @Override
            public void onItemSelectted(String s, String userValue) {

                if(comfirmDialogConnectButtonText.equals(s))
                {
                    if(!WifiTool.connectWifi(wifiManager, selectWifiConfiguration))
                        Toast.makeText(WifiSetActivity.this, getResources().getString(R.string.wifi_state_FAILED), Toast.LENGTH_SHORT).show();
                }
                else if(comfirmDialogUnsaveButtonText.equals(s))
                {
                    wifiManager.removeNetwork(selectWifiConfiguration.networkId);
                }
            }
        });*/

        myWifiInfoList = new ArrayList<>();
        wifiListAdapter = new WifiListAdapter(myWifiInfoList, this, R.layout.layout_wifi_item_view);
        wifiListAdapter.setOnItemClickListener(new OnViewItemClickListener<MyWifiInfo>() {
            @Override
            public void onItemClick(View view, int position, MyWifiInfo data) {

                selectWifiInfo = data;
                wifiManager.disconnect();
                selectWifiConfiguration = WifiTool.wifiIsExist(selectWifiInfo.getSsid(), wifiManager);
                if(selectWifiConfiguration == null)
                {
                    if(selectWifiInfo.getSecurity() != WifiTool.SECURITY_NONE)
                    {
                        wifiPasswordDialog.showInputDialog(getFragmentManager(), wifiPasswordDialogTag, wifiPasswordDialogTitle, wifiPasswordDialogLabel, true);
                    }
                    else
                    {
                        selectWifiConfiguration = WifiTool.createWifiConfig(selectWifiInfo.getSsid(), null, selectWifiInfo.getSecurity());
                        if(selectWifiConfiguration == null)
                            Toast.makeText(WifiSetActivity.this, getResources().getString(R.string.wifi_unsuport_security), Toast.LENGTH_SHORT).show();
                        else
                        {
                            if(!WifiTool.connectWifi(wifiManager, selectWifiConfiguration))
                                Toast.makeText(WifiSetActivity.this, getResources().getString(R.string.wifi_state_FAILED), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    comfirmDialog.showComfirmDialog(getFragmentManager(), comfirmDialogTag,
                            selectWifiInfo.getSsid(), comfirmDialogConnectButtonText, comfirmDialogCancelButtonText, comfirmDialogUnsaveButtonText, null);
                }
            }
        });

        wifiListview.setAdapter(wifiListAdapter);
        wifiListview.setLayoutManager(new LinearLayoutManager(this));
        wifiListview.addItemDecoration(new UserDecoration(2));

        wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiScanner = new Scanner(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        wifiSwitchChangeEventEnable = false;

        registerReceiver(mReceiver, mFilter);

        handleWifiStateChanged(wifiManager.getWifiState());

        wifiSwitchChangeEventEnable = true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        wifiScanner.pause();

        unregisterReceiver(mReceiver);

        wifiSwitchChangeEventEnable = false;
    }

    private void handleEvent(Intent intent) {
        String action = intent.getAction();

        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {

            handleWifiStateChanged(intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN));

        } else if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {

            WifiTool.changeToMyWifiInfoList(myWifiInfoList, wifiManager, this);
            wifiListAdapter.updateData(myWifiInfoList);

        } else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
            NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);

            if(networkInfo != null)
            {
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ssid = wifiInfo.getSSID();
                NetworkInfo.DetailedState detailedState = networkInfo.getDetailedState();

                WifiTool.updateMyWifiInfoState(this, myWifiInfoList, ssid, detailedState);
                wifiListAdapter.updateData(myWifiInfoList);
            }
        }
    }

    private static class Scanner extends Handler {
        private int mRetry = 0;
        private WifiSetActivity wifiSetActivity = null;

        Scanner(WifiSetActivity wifiSettings) {
            wifiSetActivity = wifiSettings;
        }

        void resume() {
            if (!hasMessages(0)) {
                sendEmptyMessage(0);
            }
        }

        void forceScan() {
            removeMessages(0);
            sendEmptyMessage(0);
        }

        void pause() {
            mRetry = 0;
            removeMessages(0);
        }

        @Override
        public void handleMessage(Message message) {

            if (wifiSetActivity.wifiManager.startScan()) {
                mRetry = 0;
            } else if (++mRetry >= 3) {
                mRetry = 0;
                Toast.makeText(wifiSetActivity, R.string.wifi_msg_scan_fail, Toast.LENGTH_LONG).show();
                return;
            }
            sendEmptyMessageDelayed(0, WIFI_RESCAN_INTERVAL_MS);
        }
    }

    private void handleWifiStateChanged(int state) {
        switch (state) {
            case WifiManager.WIFI_STATE_ENABLING:
                wifiSwitchSwitch.setEnabled(false);
                Toast.makeText(WifiSetActivity.this, R.string.wifi_msg_openning, Toast.LENGTH_SHORT).show();
                break;
            case WifiManager.WIFI_STATE_ENABLED:
                wifiSwitchSwitch.setChecked(true);
                wifiSwitchSwitch.setEnabled(true);
                wifiScanner.resume();
                Toast.makeText(WifiSetActivity.this, R.string.wifi_msg_opened, Toast.LENGTH_SHORT).show();
                return;
            case WifiManager.WIFI_STATE_DISABLING:
                wifiSwitchSwitch.setEnabled(false);
                Toast.makeText(WifiSetActivity.this, R.string.wifi_msg_closing, Toast.LENGTH_SHORT).show();
                break;
            case WifiManager.WIFI_STATE_DISABLED:
                wifiSwitchSwitch.setChecked(false);
                wifiSwitchSwitch.setEnabled(true);
                myWifiInfoList.clear();
                wifiListAdapter.updateData(null);
                Toast.makeText(WifiSetActivity.this, R.string.wifi_msg_closed, Toast.LENGTH_SHORT).show();
                break;
            default:
                wifiSwitchSwitch.setChecked(false);
                wifiSwitchSwitch.setEnabled(true);
                break;
        }

        wifiScanner.pause();
    }

}
