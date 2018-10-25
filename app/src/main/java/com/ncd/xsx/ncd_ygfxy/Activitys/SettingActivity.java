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
    Button more_set_button;
    Button about_us_button;

    private static final int hide_test_select_def = 2;

    private String[] hideTestMenuItems = null;

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
        more_set_button = (Button) findViewById(R.id.more_set_button);
        about_us_button = (Button) findViewById(R.id.about_us_button);

        hideTestMenuItems = getResources().getStringArray(R.array.hide_test_menu_items);

        selectDialog = SelectDialog.newInstance(this);
        selectDialog.setDialogSubmittListener(new DialogSubmittListener<Integer>() {
            @Override
            public void onValued(Integer value, int userValue) {

               if(userValue == hide_test_select_def)
               {
                   switch (value)
                   {
                       case DialogDefine.DIALOG_SUBMMIT_COM_VALUE_1 : startActivity(new Intent(SettingActivity.this, MotorTestActivity.class)); break;
                       case DialogDefine.DIALOG_SUBMMIT_COM_VALUE_2 : break;
                       case DialogDefine.DIALOG_SUBMMIT_COM_VALUE_3 : break;
                       default:break;
                   }
               }
            }
        });
        fragmentManager = getFragmentManager();

        user_set_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, UserManagementActivity.class));
            }
        });

        data_query_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, DataQueryActivity.class));
            }
        });

        more_set_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, SystemCfgActivity.class));
            }
        });

        about_us_button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                selectDialog.showDialog(fragmentManager, SettingActivity.selectDialogTag,
                        getResources().getString(R.string.setting_menu_net_select_dialog_title), hideTestMenuItems, hide_test_select_def);
                return false;
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

        selectDialog = null;
    }
}
