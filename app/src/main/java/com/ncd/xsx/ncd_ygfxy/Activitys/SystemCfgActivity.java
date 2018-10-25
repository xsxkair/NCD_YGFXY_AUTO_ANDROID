package com.ncd.xsx.ncd_ygfxy.Activitys;

import android.os.Bundle;

import com.ncd.xsx.ncd_ygfxy.R;

public class SystemCfgActivity extends MyActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_set);
        super.MyActivityCommonInit();

       /* //加载PrefFragment
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        SystemPrefFragment prefFragment = new SystemPrefFragment();
        transaction.add(R.id.prefFragment, prefFragment);
        transaction.commit();*/
    }

}
