package com.ncd.xsx.ncd_ygfxy.Activitys.Dialogs;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.EthernetManager;
import android.net.IpConfiguration;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.RouteInfo;
import android.net.StaticIpConfiguration;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.ncd.xsx.ncd_ygfxy.Activitys.Listeners.DialogCancelListener;
import com.ncd.xsx.ncd_ygfxy.Activitys.Listeners.DialogSubmittListener;
import com.ncd.xsx.ncd_ygfxy.Defines.PublicStringDefine;
import com.ncd.xsx.ncd_ygfxy.Logger.LoggerUnits;
import com.ncd.xsx.ncd_ygfxy.R;
import com.ncd.xsx.ncd_ygfxy.Databases.Entity.User;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Iterator;

import static com.ncd.xsx.ncd_ygfxy.Tools.NetUtils.myGetIPv4Address;

public class EthernetSetDialog extends DialogFragment {

    private Switch eth_connect_type_switch;
    private EditText ipaddr_edit;
    private EditText eth_dns_edit;
    private EditText eth_gw_edit;
    private EditText eth_mask_length_edit;
    private Button ethSaveConfigButton;
    private Button cancelButton;

    private Bundle args = null;

    private Context mContext;
    private EthernetManager mEthManager;
    private ConnectivityManager mCM;

    private DialogSubmittListener<User> dialogValuetListener = null;
    private DialogCancelListener dialogCancelListener = null;


    public void setOnDialogValuedSubmit(DialogSubmittListener<User> listener){
        dialogValuetListener = listener;
    }

    public void setOnDialogCancel(DialogCancelListener listener){
        dialogCancelListener = listener;
    }

    public static EthernetSetDialog newInstance(Context context, EthernetManager EthManager, ConnectivityManager cm) {
        EthernetSetDialog f = new EthernetSetDialog();

        f.mContext = context;
        f.mEthManager = EthManager;
        f.mCM = cm;

        // Supply num input as an argument.
        f.args = new Bundle();

        f.setArguments(f.args);

        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_ethernet_set, container);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        eth_connect_type_switch = (Switch) view.findViewById(R.id.eth_connect_type_switch);
        ipaddr_edit = (EditText) view.findViewById(R.id.ipaddr_edit);
        eth_dns_edit = (EditText) view.findViewById(R.id.eth_dns_edit);
        eth_gw_edit = (EditText) view.findViewById(R.id.eth_gw_edit);
        eth_mask_length_edit = (EditText) view.findViewById(R.id.eth_mask_length_edit);
        cancelButton = (Button) view.findViewById(R.id.cancelButton);
        ethSaveConfigButton = (Button) view.findViewById(R.id.ethSaveConfigButton);

        eth_connect_type_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                //dhcp
                if(isChecked)
                {
                    ipaddr_edit.setEnabled(false);
                    eth_dns_edit.setEnabled(false);
                    eth_gw_edit.setEnabled(false);
                    eth_mask_length_edit.setEnabled(false);
                    //mMask.setEnabled(true);
                    eth_dns_edit.setText(PublicStringDefine.EMPTY_STRING);
                    eth_gw_edit.setText(PublicStringDefine.EMPTY_STRING);
                    ipaddr_edit.setText(PublicStringDefine.EMPTY_STRING);
                    eth_mask_length_edit.setText(PublicStringDefine.EMPTY_STRING);
                }
                else
                {
                    ipaddr_edit.setEnabled(true);
                    eth_dns_edit.setEnabled(true);
                    eth_gw_edit.setEnabled(true);
                    eth_mask_length_edit.setEnabled(true);

                    IpConfiguration ipinfo = mEthManager.getConfiguration();
                    StaticIpConfiguration staticConfig = ipinfo.getStaticIpConfiguration();
                    if (staticConfig != null) {
                        if (staticConfig.ipAddress != null) {
                            ipaddr_edit.setText(staticConfig.ipAddress.getAddress().getHostAddress());
                            eth_mask_length_edit.setText(Integer.toString(staticConfig.ipAddress.getNetworkPrefixLength()));
                        }
                        if (staticConfig.gateway != null) {
                            eth_gw_edit.setText(staticConfig.gateway.getHostAddress());
                        }
                        Iterator<InetAddress> dnsIterator = staticConfig.dnsServers.iterator();
                        if (dnsIterator.hasNext()) {
                            eth_dns_edit.setText(dnsIterator.next().getHostAddress());
                        }
                    }
                }
            }
        });

        ethSaveConfigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(handle_saveconf())
                    dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (dialogCancelListener != null)
                    dialogCancelListener.onCancel();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        updateInfo();

        getDialog().getWindow().setLayout(700, 600);//设置高宽
    }

    public void showMyDialog(FragmentManager manager, String tag) {

        show(manager, tag);

    }

    private void updateInfo() {
        int enable = Settings.Global.getInt(mContext.getContentResolver(),Settings.ETHERNET_ON,0);//add by FriendlyARM
        if (enable == EthernetManager.ETH_STATE_ENABLED)
        {
            //if (mEthManager.isAvailable()) {
            IpConfiguration ipinfo = mEthManager.getConfiguration();
            if (ipinfo != null)
            {
                if (ipinfo.ipAssignment == IpConfiguration.IpAssignment.DHCP)
                {
                    eth_connect_type_switch.setChecked(true);
                    ipaddr_edit.setEnabled(false);
                    eth_dns_edit.setEnabled(false);
                    eth_gw_edit.setEnabled(false);
                    eth_mask_length_edit.setEnabled(false);
                    //mMask.setEnabled(true);
                    eth_dns_edit.setText(PublicStringDefine.EMPTY_STRING);
                    eth_gw_edit.setText(PublicStringDefine.EMPTY_STRING);
                    ipaddr_edit.setText(PublicStringDefine.EMPTY_STRING);
                    eth_mask_length_edit.setText(PublicStringDefine.EMPTY_STRING);

                    if (mCM != null) {
                        LinkProperties lp = mCM.getLinkProperties(ConnectivityManager.TYPE_ETHERNET);
                        if (lp != null) {
                            showCurrentIpAddresses(lp);
                        }
                    }
                }
                else
                {
                    eth_connect_type_switch.setChecked(false);
                    ipaddr_edit.setEnabled(true);
                    eth_dns_edit.setEnabled(true);
                    eth_gw_edit.setEnabled(true);
                    eth_mask_length_edit.setEnabled(true);
                    StaticIpConfiguration staticConfig = ipinfo.getStaticIpConfiguration();
                    if (staticConfig != null) {
                        if (staticConfig.ipAddress != null) {
                            ipaddr_edit.setText(staticConfig.ipAddress.getAddress().getHostAddress());
                            eth_mask_length_edit.setText(Integer.toString(staticConfig.ipAddress.getNetworkPrefixLength()));
                        }
                        if (staticConfig.gateway != null) {
                            eth_gw_edit.setText(staticConfig.gateway.getHostAddress());
                        }
                        Iterator<InetAddress> dnsIterator = staticConfig.dnsServers.iterator();
                        if (dnsIterator.hasNext()) {
                            eth_dns_edit.setText(dnsIterator.next().getHostAddress());
                        }
                    }
                }
            }
        }
        else
        {

        }
    }

    private boolean handle_saveconf()
    {
        if (eth_connect_type_switch.isChecked())
        {
            LoggerUnits.info(getClass().getSimpleName(), "set ethernet dhcp mode");
            mEthManager.setConfiguration(new IpConfiguration(IpConfiguration.IpAssignment.DHCP, IpConfiguration.ProxySettings.NONE,
                    null, null));

            return true;
        }
        else
        {
            LoggerUnits.info(getClass().getSimpleName(), "set ethernet static mode");

            if (isIpAddress(ipaddr_edit.getText().toString())
                    && isIpAddress(eth_gw_edit.getText().toString())
                    && isIpAddress(eth_dns_edit.getText().toString()))
            {

                if (TextUtils.isEmpty(ipaddr_edit.getText().toString())
                        || TextUtils.isEmpty(eth_mask_length_edit.getText().toString())
                        || TextUtils.isEmpty(eth_gw_edit.getText().toString())
                        || TextUtils.isEmpty(eth_dns_edit.getText().toString()))
                {
                    Toast.makeText(mContext, R.string.eth_settings_empty, Toast.LENGTH_LONG).show();
                }

                StaticIpConfiguration mStaticIpConfiguration = new StaticIpConfiguration();
                String result = validateIpConfigFields(mStaticIpConfiguration);
                if("ok".equals(result))
                {
                    mEthManager.setConfiguration(new IpConfiguration(IpConfiguration.IpAssignment.STATIC, IpConfiguration.ProxySettings.NONE,
                            mStaticIpConfiguration, null));

                    return true;
                }
                else
                    Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
            }
            else
                Toast.makeText(mContext, R.string.eth_settings_error, Toast.LENGTH_LONG).show();
        }

        return false;
    }

    private void showCurrentIpAddresses(LinkProperties prop) {

        StringBuffer addresses = new StringBuffer();

        //ip
        Iterator<LinkAddress> linkAddressList = prop.getLinkAddresses().iterator();

        while (linkAddressList.hasNext()) {
            LinkAddress linkAddress = linkAddressList.next();
            ipaddr_edit.setText(linkAddress.getAddress().getHostAddress());
            eth_mask_length_edit.setText(String.valueOf(linkAddress.getPrefixLength()));
        }

        //gw
        Iterator<RouteInfo> routeInfoIterator = prop.getRoutes().iterator();

        addresses.setLength(0);
        while (routeInfoIterator.hasNext()) {
            addresses.append(routeInfoIterator.next().getGateway().getHostAddress());
            if(routeInfoIterator.hasNext())
                addresses.append(" / ");
        }
        eth_gw_edit.setText(addresses.toString());


        //dns
        Iterator<InetAddress> dnsIterator = prop.getDnsServers().iterator();
        addresses.setLength(0);
        while (dnsIterator.hasNext()) {
            addresses.append(dnsIterator.next().getHostAddress());
            if(dnsIterator.hasNext())
                addresses.append(" / ");

        }
        eth_dns_edit.setText(addresses.toString());

    }

    private boolean isIpAddress(String value) {
        int start = 0;
        int end = value.indexOf('.');
        int numBlocks = 0;

        while (start < value.length()) {
            if (end == -1) {
                end = value.length();
            }

            try {
                int block = Integer.parseInt(value.substring(start, end));
                if ((block > 255) || (block < 0)) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }

            numBlocks++;

            start = end + 1;
            end = value.indexOf('.', start);
        }
        return numBlocks == 4;
    }

    private String validateIpConfigFields(StaticIpConfiguration staticIpConfiguration) {

        String ipAddr = ipaddr_edit.getText().toString();
        Log.i("xsx", ipAddr);
        Inet4Address inetAddr = myGetIPv4Address(ipAddr);
        if (inetAddr == null) {
            return "ip : error";
        }
/*
        String netmask = mMask.getText().toString();
        if (TextUtils.isEmpty(netmask))
            return 11;
        Inet4Address netmas = getIPv4Address(netmask);
        if (netmas == null) {
            return 12;
        }
        int nmask = NetworkUtils.inetAddressToInt(netmas);
        int prefixlength = NetworkUtils.netmaskIntToPrefixLength(nmask);
*/
        int networkPrefixLength = -1;
        try {
            networkPrefixLength = Integer.parseInt(eth_mask_length_edit.getText().toString());
            if (networkPrefixLength < 0 || networkPrefixLength > 32) {
                return "eth network prefix length : error";
            }
            staticIpConfiguration.ipAddress = new LinkAddress(inetAddr, networkPrefixLength);
        } catch (NumberFormatException e) {
            // Set the hint as default after user types in ip address
        }

        String gateway = eth_gw_edit.getText().toString();

        InetAddress gatewayAddr = myGetIPv4Address(gateway);
        if (gatewayAddr == null) {
            return "gate way : error";
        }
        staticIpConfiguration.gateway = gatewayAddr;

        String dns = eth_dns_edit.getText().toString();
        InetAddress dnsAddr = null;

        dnsAddr = myGetIPv4Address(dns);
        if (dnsAddr == null) {
            return "dns : error";
        }

        staticIpConfiguration.dnsServers.add(dnsAddr);

        return "ok";
    }
}
