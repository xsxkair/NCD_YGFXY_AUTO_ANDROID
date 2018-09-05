package com.ncd.xsx.ncd_ygfxy.Tools;

import android.content.Context;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.ncd.xsx.ncd_ygfxy.R;
import com.ncd.xsx.ncd_ygfxy.Databases.Entity.MyWifiInfo;

import java.util.List;

public class WifiTool {

    public static final int SECURITY_NONE = 0;
    public static final int SECURITY_WEP = 1;
    public static final int SECURITY_PSK = 2;
    public static final int SECURITY_EAP = 3;

    public static int getSecurity(String wifiCapabilities) {

        if (wifiCapabilities.contains("WEP")) {
            return SECURITY_WEP;
        } else if (wifiCapabilities.contains("PSK")) {
            return SECURITY_PSK;
        } else if (wifiCapabilities.contains("EAP")) {
            return SECURITY_EAP;
        }
        return SECURITY_NONE;
    }

    public static void updateMyWifiInfoState(Context context, List<MyWifiInfo> myWifiInfoList, String ssid, NetworkInfo.DetailedState detailedState)
    {
        Log.d("xsx", ssid +" : "+detailedState);
        String ssid_f = ssid.substring(1, ssid.length()-1);
        for (MyWifiInfo myWifiInfo : myWifiInfoList)
        {
            if(myWifiInfo.getSsid().equals(ssid_f))
            {
                switch (detailedState)
                {
                    case CONNECTING: myWifiInfo.setState(context.getResources().getString(R.string.wifi_state_connecting)); break;
                    case AUTHENTICATING: myWifiInfo.setState(context.getResources().getString(R.string.wifi_state_AUTHENTICATING)); break;
                    case OBTAINING_IPADDR: myWifiInfo.setState(context.getResources().getString(R.string.wifi_state_OBTAINING_IPADDR)); break;
                    case FAILED: myWifiInfo.setState(context.getResources().getString(R.string.wifi_state_FAILED)); break;
                    case CONNECTED: myWifiInfo.setState(context.getResources().getString(R.string.wifi_status_connected)); break;
                    case DISCONNECTED: myWifiInfo.setState(context.getResources().getString(R.string.wifi_status_disconnected)); break;
                }
            }
            else
                myWifiInfo.setState(context.getResources().getString(R.string.space_string));
        }
    }

    public static boolean connectWifi(WifiManager wifiManager, WifiConfiguration wifiConfiguration)
    {

        List<WifiConfiguration> wifiConfigurationList = wifiManager.getConfiguredNetworks();
        for(WifiConfiguration wificonfig : wifiConfigurationList)
        {
            wifiManager.disableNetwork(wificonfig.networkId);
        }

        int idd = wifiManager.addNetwork(wifiConfiguration);

        if( !wifiManager.enableNetwork(idd, true))
        {
            wifiManager.reconnect();

            return false;
        }
        else
            return true;
    }

    public static List<MyWifiInfo> changeToMyWifiInfoList(List<MyWifiInfo> myWifiInfoList, WifiManager wifiManager, Context context)
    {
        String connectWifiSsid = null;
        List<ScanResult> scanResultList = wifiManager.getScanResults();
        WifiInfo connecttedWifiInfo = wifiManager.getConnectionInfo();

        myWifiInfoList.clear();

        if(scanResultList == null || scanResultList.size() == 0)
            return myWifiInfoList;

        if(connecttedWifiInfo != null)
        {
            connectWifiSsid = connecttedWifiInfo.getSSID();
            connectWifiSsid = connectWifiSsid.substring(1, connectWifiSsid.length()-1);
        }
        else
            connectWifiSsid = null;

        for (ScanResult result : scanResultList)
        {
            MyWifiInfo info = new MyWifiInfo();
            info.setSsid(result.SSID);
            info.setSecurity(getSecurity(result.capabilities));
            info.setLevel(result.level);

            if(connectWifiSsid != null && result.SSID.equals(connectWifiSsid))
            {
                info.setState(context.getResources().getString(R.string.wifi_status_connected));
            }
            else if(wifiIsExist(info.getSsid(), wifiManager) != null)
                info.setState(context.getResources().getString(R.string.wifi_status_saved));
            else
                info.setState(context.getResources().getString(R.string.space_string));

            myWifiInfoList.add(info);
        }

        return myWifiInfoList;
    }

    public static WifiConfiguration wifiIsExist(String ssid, WifiManager wifiManager)
    {
        List<WifiConfiguration> configs = wifiManager.getConfiguredNetworks();
        String ssid_a = String.format("\"%s\"", ssid);

        for (WifiConfiguration config : configs) {
            if (config.SSID.equals(ssid_a)) {
                return config;
            }
        }
        return null;
    }

    public static WifiConfiguration createWifiConfig(String ssid, String password, int type) {
        //初始化WifiConfiguration
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();

        //指定对应的SSID
        config.SSID = String.format("\"%s\"", ssid);
        config.hiddenSSID = true;

        switch (type)
        {
            case SECURITY_NONE:
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                break;

            case SECURITY_WEP:
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);

                if(password == null)
                    return null;
                else
                    config.wepKeys[0] = String.format("\"%s\"", password);

                break;

            case SECURITY_PSK:
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                if(password == null)
                    return null;
                else
                    config.preSharedKey = String.format("\"%s\"", password);
                break;

            case  SECURITY_EAP:
                return null;
        }

        return config;
    }

}
