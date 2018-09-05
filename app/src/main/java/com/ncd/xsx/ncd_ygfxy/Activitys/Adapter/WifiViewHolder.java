package com.ncd.xsx.ncd_ygfxy.Activitys.Adapter;

import android.net.wifi.WifiManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ncd.xsx.ncd_ygfxy.R;
import com.ncd.xsx.ncd_ygfxy.Tools.WifiTool;
import com.ncd.xsx.ncd_ygfxy.Databases.Entity.MyWifiInfo;

public class WifiViewHolder extends RecyclerView.ViewHolder {

    private ImageView wifiSigalImageView;
    private TextView wifiSsidTextView;
    private TextView wifiStateTextView;
    private View view;

    public WifiViewHolder(View itemView) {
        super(itemView);

        wifiSigalImageView = (ImageView) itemView.findViewById(R.id.wifi_sigal_imageview);
        wifiSsidTextView = (TextView) itemView.findViewById(R.id.wifi_ssid_textview);
        wifiStateTextView = (TextView) itemView.findViewById(R.id.wifi_state_textview);

        view = itemView;
    }

    public void fillViewHolderContent(MyWifiInfo wifiInfo){

        int level = WifiManager.calculateSignalLevel(wifiInfo.getLevel(), 5);
        int security = wifiInfo.getSecurity();

        switch (level)
        {
            case 0:
                wifiSigalImageView.setImageResource((security == WifiTool.SECURITY_NONE) ? R.drawable.ic_wifi_signal_0_light : R.drawable.ic_wifi_lock_signal_0_light);
                break;
            case 1:
                wifiSigalImageView.setImageResource((security == WifiTool.SECURITY_NONE) ? R.drawable.ic_wifi_signal_1_light : R.drawable.ic_wifi_lock_signal_1_light);
                break;
            case 2:
                wifiSigalImageView.setImageResource((security == WifiTool.SECURITY_NONE) ? R.drawable.ic_wifi_signal_2_light : R.drawable.ic_wifi_lock_signal_2_light);
                break;
            case 3:
                wifiSigalImageView.setImageResource((security == WifiTool.SECURITY_NONE) ? R.drawable.ic_wifi_signal_3_light : R.drawable.ic_wifi_lock_signal_3_light);
                break;
            case 4:
                wifiSigalImageView.setImageResource((security == WifiTool.SECURITY_NONE) ? R.drawable.ic_wifi_signal_4_light : R.drawable.ic_wifi_lock_signal_4_light);
                break;
            default:
                wifiSigalImageView.setImageResource(R.drawable.ic_wifi_signal_0_light);
                break;
        }

        wifiSsidTextView.setText(wifiInfo.getSsid());

        wifiStateTextView.setText(wifiInfo.getState());
    }

    public View getView() {
        return view;
    }
}
