package com.ncd.xsx.ncd_ygfxy.Activitys;

import android.annotation.Nullable;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ncd.xsx.ncd_ygfxy.Activitys.Dialogs.MyEditTextPreference;
import com.ncd.xsx.ncd_ygfxy.Defines.PublicStringDefine;
import com.ncd.xsx.ncd_ygfxy.R;
import com.ncd.xsx.ncd_ygfxy.Tools.NetUtils;

import java.net.Inet4Address;

public class SystemPrefFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    //private SharedPreferences prefs;
    private MyEditTextPreference device_id_EditTextPreference;
    private EditTextPreference device_user_EditTextPreference;
    private EditTextPreference device_addr_EditTextPreference;
    private SwitchPreference auto_print_SwitchPreference;
    private EditTextPreference server_ip_EditTextPreference;
    private EditTextPreference server_port_EditTextPreference;
    private Preference wlan_set_Preference;
    private Preference ethernet_set_Preference;
    private Preference datetime_set_Preference;

    private Intent wifiSettingsIntent;
    private Intent ethernetSettingsIntent;
    private Intent datetimeSettingsIntent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.system_set_preference);

        device_id_EditTextPreference = (MyEditTextPreference) findPreference(getResources().getString(R.string.device_id_key));
        device_user_EditTextPreference = (EditTextPreference) findPreference(getResources().getString(R.string.device_user_key));
        device_addr_EditTextPreference = (EditTextPreference) findPreference(getResources().getString(R.string.device_addr_key));
        auto_print_SwitchPreference = (SwitchPreference) findPreference(getResources().getString(R.string.auto_print_key));
        server_ip_EditTextPreference = (EditTextPreference) findPreference(getResources().getString(R.string.server_ip_key));
        server_port_EditTextPreference = (EditTextPreference) findPreference(getResources().getString(R.string.server_port_key));

        wlan_set_Preference = (Preference) findPreference(getResources().getString(R.string.wlan_set_key));
        ethernet_set_Preference = (Preference) findPreference(getResources().getString(R.string.ethernet_set_key));
        datetime_set_Preference = (Preference) findPreference(getResources().getString(R.string.datetime_set_key));

        device_id_EditTextPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String[] value = (String[]) newValue;

                if(PublicStringDefine.ADMIN_PASSWD.equals(value[0]))
                    return true;
                else
                    return false;
            }
        });

        Preference.OnPreferenceChangeListener onPreferenceChangeListener = (preference, newValue)->{
            return true;
        };

        device_user_EditTextPreference.setOnPreferenceChangeListener(onPreferenceChangeListener);
        device_addr_EditTextPreference.setOnPreferenceChangeListener(onPreferenceChangeListener);
        auto_print_SwitchPreference.setOnPreferenceChangeListener(onPreferenceChangeListener);
        server_port_EditTextPreference.setOnPreferenceChangeListener(onPreferenceChangeListener);

        server_ip_EditTextPreference.setOnPreferenceChangeListener((preference, newValue)->{
            Inet4Address inet4Address = NetUtils.myGetIPv4Address((String) newValue);
            if(inet4Address == null)
                return false;
            else
                return true;
        });

        wifiSettingsIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        wifiSettingsIntent.putExtra("extra_prefs_show_button_bar", true);
        wifiSettingsIntent.putExtra("extra_prefs_set_next_text", "返回");
        wifiSettingsIntent.putExtra("extra_prefs_set_back_text", (String) null);
        wlan_set_Preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                startActivity(wifiSettingsIntent);

                return false;
            }
        });

        ethernetSettingsIntent = new Intent("android.settings.ETHERNET_SETTINGS");
        ethernetSettingsIntent.putExtra("extra_prefs_show_button_bar", true);
        ethernetSettingsIntent.putExtra("extra_prefs_set_next_text", "返回");
        ethernetSettingsIntent.putExtra("extra_prefs_set_back_text", (String) null);
        ethernet_set_Preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                startActivity(ethernetSettingsIntent);

                return false;
            }
        });

        datetimeSettingsIntent = new Intent(Settings.ACTION_DATE_SETTINGS);
        datetimeSettingsIntent.putExtra("extra_prefs_show_button_bar", true);
        datetimeSettingsIntent.putExtra("extra_prefs_set_next_text", "返回");
        datetimeSettingsIntent.putExtra("extra_prefs_set_back_text", (String) null);
        datetime_set_Preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                startActivity(datetimeSettingsIntent);

                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        initSummary();

        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        initSummary();
    }

    private void initSummary()
    {
        device_id_EditTextPreference.setSummary(device_id_EditTextPreference.getText());
        device_user_EditTextPreference.setSummary(device_user_EditTextPreference.getText());
        device_addr_EditTextPreference.setSummary(device_addr_EditTextPreference.getText());
        server_ip_EditTextPreference.setSummary(server_ip_EditTextPreference.getText());
        server_port_EditTextPreference.setSummary(server_port_EditTextPreference.getText());
    }
}
