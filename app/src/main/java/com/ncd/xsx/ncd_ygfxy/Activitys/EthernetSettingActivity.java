package com.ncd.xsx.ncd_ygfxy.Activitys;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.EthernetManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ncd.xsx.ncd_ygfxy.Activitys.Dialogs.EthernetSetDialog;
import com.ncd.xsx.ncd_ygfxy.R;

public class EthernetSettingActivity extends MyActivity {

    private static final String activity_tag = "EthernetSettingActivity";

    Switch eth_enable_switch;
    TextView eth_conf_click_textview;

    private EthernetManager mEthManager = null;
    private ConnectivityManager connectivityManager;

    private EthernetSetDialog ethernetSetDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ethernet_setting);

        super.MyActivityCommonInit();

        eth_enable_switch = (Switch) findViewById(R.id.eth_enable_switch);
        eth_conf_click_textview = (TextView) findViewById(R.id.eth_conf_click_textview);

        eth_enable_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("xsx", "enable: "+isChecked);
                if (isChecked) {
                    //if (mEthDialog != null)
                    //mEthDialog.show();
                } else {
                    if (mEthManager != null)
                        mEthManager.stop();
                }

                Settings.Global.putInt(EthernetSettingActivity.this.getContentResolver(), Settings.ETHERNET_ON,
                        isChecked ? EthernetManager.ETH_STATE_ENABLED : EthernetManager.ETH_STATE_DISABLED);

                if (isChecked)
                {
                    if (mEthManager != null)
                        mEthManager.start();
                }
            }
        });

        eth_conf_click_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eth_enable_switch.isChecked())
                    ethernetSetDialog.showMyDialog(getFragmentManager(), activity_tag);
                else
                    Toast.makeText(EthernetSettingActivity.this, "please turn on ethernet", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //
        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        mEthManager = (EthernetManager)getSystemService(Context.ETHERNET_SERVICE);

        ethernetSetDialog = EthernetSetDialog.newInstance(this, mEthManager, connectivityManager);

        setupEthernetEnable();
    }


    private void setupEthernetEnable()
    {
        int enable = Settings.Global.getInt(getContentResolver(),Settings.ETHERNET_ON,0);//add by FriendlyARM
        if (enable == EthernetManager.ETH_STATE_ENABLED) {
            eth_enable_switch.setChecked(true);
        } else {
            eth_enable_switch.setChecked(false);
        }
    }

}
