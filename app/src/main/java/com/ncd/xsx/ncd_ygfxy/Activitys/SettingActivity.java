package com.ncd.xsx.ncd_ygfxy.Activitys;

import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ncd.xsx.ncd_ygfxy.Activitys.Dialogs.DialogDefine;
import com.ncd.xsx.ncd_ygfxy.Activitys.Dialogs.SelectDialog;
import com.ncd.xsx.ncd_ygfxy.Activitys.Listeners.DialogSubmittListener;
import com.ncd.xsx.ncd_ygfxy.R;

public class SettingActivity extends MyActivity {

    Button user_set_button;
    Button data_query_button;
    Button device_info_button;
    Button net_set_button;
    Button more_set_button;
    Button about_us_button;

    private String[] netSetMenuItems = null;

    private SelectDialog selectDialog = null;
    private static final String selectDialogTag = "net_set_select_dialog";
    private FragmentManager fragmentManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        super.MyActivityCommonInit();

        user_set_button = (Button) findViewById(R.id.user_set_button);
        data_query_button = (Button) findViewById(R.id.data_query_button);
        device_info_button = (Button) findViewById(R.id.device_info_button);
        net_set_button = (Button) findViewById(R.id.net_set_button);
        more_set_button = (Button) findViewById(R.id.more_set_button);
        about_us_button = (Button) findViewById(R.id.about_us_button);

        netSetMenuItems = getResources().getStringArray(R.array.net_set_menu_items);

        selectDialog = SelectDialog.newInstance(this);
        selectDialog.setDialogSubmittListener(new DialogSubmittListener<Integer>() {
            @Override
            public void onValued(Integer value, int userValue) {

                if(value == DialogDefine.DIALOG_SUBMMIT_COM_VALUE_1)
                {
                    Intent wifiSettingsIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    wifiSettingsIntent.putExtra("extra_prefs_show_button_bar", true);
                    wifiSettingsIntent.putExtra("extra_prefs_set_back_text", "返回");
                    wifiSettingsIntent.putExtra("extra_prefs_set_next_text", (String) null);
                    startActivity(wifiSettingsIntent);
                }
                   //startActivity(new Intent(SettingActivity.this, WifiSetActivity.class));
                else if(value == DialogDefine.DIALOG_SUBMMIT_COM_VALUE_2)
                {
                    Intent wifiSettingsIntent = new Intent("android.settings.ETHERNET_SETTINGS");
                    wifiSettingsIntent.putExtra("extra_prefs_show_button_bar", true);
                    wifiSettingsIntent.putExtra("extra_prefs_set_back_text", "返回");
                    wifiSettingsIntent.putExtra("extra_prefs_set_next_text", (String) null);
                    startActivity(wifiSettingsIntent);
                    //startActivity(new Intent(SettingActivity.this, EthernetSettingActivity.class));
                }
                else if(value == DialogDefine.DIALOG_SUBMMIT_COM_VALUE_3)
                    startActivity(new Intent(SettingActivity.this, ServerSetActivity.class));
            }
        });
        fragmentManager = getFragmentManager();

        device_info_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, DeviceInfoActivity.class));
            }
        });

        user_set_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, UserManagementActivity.class));
            }
        });

        net_set_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDialog.showDialog(fragmentManager, SettingActivity.selectDialogTag,
                        getResources().getString(R.string.setting_menu_net_select_dialog_title), netSetMenuItems[0], netSetMenuItems[1], netSetMenuItems[2]);
            }
        });

        data_query_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, DataQueryActivity.class));
            }
        });

        about_us_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, AboutUsActivity.class));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        netSetMenuItems = null;

        selectDialog = null;
    }
}
