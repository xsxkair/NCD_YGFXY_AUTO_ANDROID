package com.ncd.xsx.ncd_ygfxy.Activitys;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.ncd.xsx.ncd_ygfxy.Activitys.Dialogs.ComfirmDialog;
import com.ncd.xsx.ncd_ygfxy.Activitys.Dialogs.InputDialog;
import com.ncd.xsx.ncd_ygfxy.Defines.PublicStringDefine;
import com.ncd.xsx.ncd_ygfxy.Logger.LoggerUnits;
import com.ncd.xsx.ncd_ygfxy.R;
import com.ncd.xsx.ncd_ygfxy.Tools.MySdcardSharedPreferences;


public class DeviceInfoActivity extends MyActivity {

    ImageView deviceInfoSetImageView;
    TextView deviceIdValueTextView;
    EditText deviceAddrEditText;
    ImageView clear_addr_imageview;
    EditText deviceUserEditText;
    ImageView clear_user_imageview;
    EditText devicePhoneEditText;
    ImageView clear_phone_imageview;
    Switch modifyDeviceInfoSwitch;

    private String superPassword = null;
    private String comfirmDialogTag = null;
    private String InputDialogTag = null;
    private ComfirmDialog comfirmDialog = null;
    private InputDialog inputDialog = null;

    private static final int Input_Dialog_Password_User_Value = 0x9001;
    private static final int Input_Dialog_DeviceId_User_Value = 0x9002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);

        super.MyActivityCommonInit();

        deviceInfoSetImageView = (ImageView) findViewById(R.id.deviceInfoSetImageView);
        deviceIdValueTextView = (TextView) findViewById(R.id.deviceIdValueTextView);
        deviceAddrEditText = (EditText) findViewById(R.id.deviceAddrEditText);
        deviceUserEditText = (EditText) findViewById(R.id.deviceUserEditText);
        devicePhoneEditText = (EditText) findViewById(R.id.devicePhoneEditText);
        modifyDeviceInfoSwitch = (Switch) findViewById(R.id.modifyDeviceInfoSwitch);
        clear_addr_imageview = (ImageView) findViewById(R.id.clear_addr_imageview);
        clear_user_imageview = (ImageView) findViewById(R.id.clear_user_imageview);
        clear_phone_imageview = (ImageView) findViewById(R.id.clear_phone_imageview);

        superPassword = getResources().getString(R.string.SuperAdminPassword);
        comfirmDialogTag = getResources().getString(R.string.ConfirmDialogTag);
        InputDialogTag = getResources().getString(R.string.InputDialogTag);

        comfirmDialog = ComfirmDialog.newInstance();

        inputDialog = InputDialog.newInstance();
        inputDialog.setOnInputDialogSubmit(new InputDialog.InputDialogSubmitListener() {
            @Override
            public void onSubmit(String msg, int userValue) {

                if(userValue == Input_Dialog_Password_User_Value){
                    if(superPassword.equals(msg))
                        inputDialog.showInputDialog(getFragmentManager(), InputDialogTag, "信息录入", "请输入设备编号", false,
                                Input_Dialog_DeviceId_User_Value);
                    else
                        comfirmDialog.showComfirmDialog(getFragmentManager(), comfirmDialogTag,
                                getResources().getString(R.string.dialog_content_password_error_text),
                                getResources().getString(R.string.button_confirm_text));
                }
                else{
                    saveAndUpdateDeviceId(msg);
                }
            }
        });

        deviceAddrEditText.setEnabled(false);
        deviceUserEditText.setEnabled(false);
        devicePhoneEditText.setEnabled(false);

        modifyDeviceInfoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    deviceAddrEditText.setEnabled(true);
                    clear_addr_imageview.setVisibility(View.VISIBLE);
                    deviceUserEditText.setEnabled(true);
                    clear_user_imageview.setVisibility(View.VISIBLE);
                    devicePhoneEditText.setEnabled(true);
                    clear_phone_imageview.setVisibility(View.VISIBLE);
                }
                else
                {
                    deviceAddrEditText.setEnabled(false);
                    deviceUserEditText.setEnabled(false);
                    devicePhoneEditText.setEnabled(false);

                    clear_addr_imageview.setVisibility(View.GONE);
                    clear_user_imageview.setVisibility(View.GONE);
                    clear_phone_imageview.setVisibility(View.GONE);

                    saveAndUpdateDeviceInfo();
                }
            }
        });

        deviceInfoSetImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialog.showInputDialog(getFragmentManager(), InputDialogTag, getResources().getString(R.string.CheckPermissionText),
                        getResources().getString(R.string.PleaseInputPassword), true, Input_Dialog_Password_User_Value);

                return true;
            }
        });

        clear_addr_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deviceAddrEditText.setText(PublicStringDefine.EMPTY_STRING);
            }
        });
        clear_user_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deviceUserEditText.setText(PublicStringDefine.EMPTY_STRING);
            }
        });
        clear_phone_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                devicePhoneEditText.setText(PublicStringDefine.EMPTY_STRING);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        deviceIdValueTextView.setText(MySdcardSharedPreferences.getInstance().getValue(MySdcardSharedPreferences.Keys.DEVICE_ID_KEY, PublicStringDefine.EMPTY_STRING));
        deviceAddrEditText.setText(MySdcardSharedPreferences.getInstance().getValue(MySdcardSharedPreferences.Keys.DEVICE_ADDR_KEY, PublicStringDefine.EMPTY_STRING));
        deviceUserEditText.setText(MySdcardSharedPreferences.getInstance().getValue(MySdcardSharedPreferences.Keys.DEVICE_USER_KEY, PublicStringDefine.EMPTY_STRING));
        devicePhoneEditText.setText(MySdcardSharedPreferences.getInstance().getValue(MySdcardSharedPreferences.Keys.DEVICE_PHONE_KEY, PublicStringDefine.EMPTY_STRING));
    }

    private void saveAndUpdateDeviceId(String deviceid){

        if(deviceid == null || deviceid.length() <= 0)
            comfirmDialog.showComfirmDialog(getFragmentManager(), comfirmDialogTag,
                    getResources().getString(R.string.dialog_content_parm_error_text),
                    getResources().getString(R.string.button_confirm_text));
        else {
            try {

                MySdcardSharedPreferences.getInstance().getEditor().putString(MySdcardSharedPreferences.Keys.DEVICE_ID_KEY, deviceid)
                        .putBoolean(MySdcardSharedPreferences.Keys.DEVICE_STATE_KEY, true)
                        .commit();

                deviceIdValueTextView.setText(MySdcardSharedPreferences.getInstance().getValue(MySdcardSharedPreferences.Keys.DEVICE_ID_KEY, PublicStringDefine.EMPTY_STRING));

            }catch (Exception e){
                comfirmDialog.showComfirmDialog(getFragmentManager(), comfirmDialogTag,
                        getResources().getString(R.string.dialog_content_save_fail_text),
                        getResources().getString(R.string.button_confirm_text));
                LoggerUnits.error("保存设备编号失败", e);
            }
        }

    }

    private void saveAndUpdateDeviceInfo(){
        try {

            MySdcardSharedPreferences.getInstance().getEditor().putString(MySdcardSharedPreferences.Keys.DEVICE_ADDR_KEY, deviceAddrEditText.getText().toString())
                    .putString(MySdcardSharedPreferences.Keys.DEVICE_USER_KEY, deviceUserEditText.getText().toString())
                    .putString(MySdcardSharedPreferences.Keys.DEVICE_PHONE_KEY, devicePhoneEditText.getText().toString())
                    .putBoolean(MySdcardSharedPreferences.Keys.DEVICE_STATE_KEY, true).commit();

            deviceAddrEditText.setText(MySdcardSharedPreferences.getInstance().getValue(MySdcardSharedPreferences.Keys.DEVICE_ADDR_KEY, PublicStringDefine.EMPTY_STRING));
            deviceUserEditText.setText(MySdcardSharedPreferences.getInstance().getValue(MySdcardSharedPreferences.Keys.DEVICE_USER_KEY, PublicStringDefine.EMPTY_STRING));
            devicePhoneEditText.setText(MySdcardSharedPreferences.getInstance().getValue(MySdcardSharedPreferences.Keys.DEVICE_PHONE_KEY, PublicStringDefine.EMPTY_STRING));
            comfirmDialog.showComfirmDialog(getFragmentManager(), comfirmDialogTag,
                    getResources().getString(R.string.dialog_content_save_fail_text),
                    getResources().getString(R.string.button_confirm_text));
        }catch (Exception e){
            comfirmDialog.showComfirmDialog(getFragmentManager(), comfirmDialogTag,
                    getResources().getString(R.string.dialog_content_save_fail_text),
                    getResources().getString(R.string.button_confirm_text));
            LoggerUnits.error("保存设备信息失败", e);
        }

    }

}
