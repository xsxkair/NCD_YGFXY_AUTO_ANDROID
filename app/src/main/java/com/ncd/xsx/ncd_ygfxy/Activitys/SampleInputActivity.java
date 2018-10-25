package com.ncd.xsx.ncd_ygfxy.Activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.FileUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.ncd.xsx.ncd_ygfxy.Activitys.Adapter.SampleDataAdapter;
import com.ncd.xsx.ncd_ygfxy.Activitys.Adapter.TestDataAdapter;
import com.ncd.xsx.ncd_ygfxy.Activitys.Adapter.UserAdapter;
import com.ncd.xsx.ncd_ygfxy.Activitys.Adapter.UserDecoration;
import com.ncd.xsx.ncd_ygfxy.Activitys.Dialogs.SampleInputDialog;
import com.ncd.xsx.ncd_ygfxy.Activitys.Listeners.DialogSubmittListener;
import com.ncd.xsx.ncd_ygfxy.Activitys.Listeners.OnViewItemClickListener;
import com.ncd.xsx.ncd_ygfxy.Activitys.Listeners.OnViewItemLongClickListener;
import com.ncd.xsx.ncd_ygfxy.Databases.Entity.Card;
import com.ncd.xsx.ncd_ygfxy.Databases.Entity.TestData;
import com.ncd.xsx.ncd_ygfxy.Databases.Entity.User;
import com.ncd.xsx.ncd_ygfxy.Databases.Services.PatientService;
import com.ncd.xsx.ncd_ygfxy.Databases.Services.UserService;
import com.ncd.xsx.ncd_ygfxy.Defines.PublicStringDefine;
import com.ncd.xsx.ncd_ygfxy.R;
import com.ncd.xsx.ncd_ygfxy.Services.TestService.TestDataUnit;
import com.ncd.xsx.ncd_ygfxy.Services.TestService.TestFunction;
import com.ncd.xsx.ncd_ygfxy.Services.TestService.TestState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class SampleInputActivity extends MyActivity {

    private static final String TAG = "SampleInputActivity";

    ImageButton add_sample_imagebutton;
    RecyclerView sample_data_recyclerview;

    private SampleDataAdapter sampleDataAdapter;
    private List<TestDataUnit> testDataUnits;

    private SampleInputDialog sampleInputDialog;
    private Semaphore sampleInputDialogShowSemaphore;

    private BroadcastReceiver mReceiver;
    private IntentFilter mFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_input);

        super.MyActivityCommonInit();

        mFilter = new IntentFilter();
        mFilter.addAction(PublicStringDefine.MY_SEC_BROADCAST);
        mFilter.addAction(PublicStringDefine.NEW_SAMPLE_INPUT_BROADCASR);
        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                //每秒刷新测试数据列表
                if(PublicStringDefine.MY_SEC_BROADCAST.equals(action))
                {
                    TestDataUnit[] testDataUnitArray = TestFunction.getInstance().getCurrentTestDataUnitList();
                    testDataUnits = Arrays.asList(testDataUnitArray);
                    sampleDataAdapter.updateTestData(testDataUnits);
                }
                //有新样本编号，则弹出对话框新增测试
                else if(PublicStringDefine.NEW_SAMPLE_INPUT_BROADCASR.equals(action))
                {
                    int testDataIndex = intent.getIntExtra(PublicStringDefine.NEW_SAMPLE_INTENT_KEY, -1);

                    //数据有效
                    if(testDataIndex >= 0)
                    {
                        TestDataUnit[] testDataUnitArray = TestFunction.getInstance().getCurrentTestDataUnitList();
                        testDataUnits = Arrays.asList(testDataUnitArray);
                        sampleDataAdapter.updateTestData(testDataUnits);

                        //且测试状态为未配置
                        if(testDataUnitArray[testDataIndex].getStartTime() == TestState.STATE_NO_CONF && sampleInputDialogShowSemaphore.tryAcquire())
                            sampleInputDialog.showMyDialog(getFragmentManager(), TAG, testDataUnitArray[testDataIndex].getTestData().getSampleid(), testDataIndex);
                    }
                }
            }
        };

        add_sample_imagebutton = (ImageButton) findViewById(R.id.add_sample_imagebutton);
        sample_data_recyclerview = (RecyclerView) findViewById(R.id.sample_data_recyclerview);

        sampleInputDialogShowSemaphore = new Semaphore(1);
        sampleInputDialog = SampleInputDialog.newInstance();
        sampleInputDialog.setOnDialogValuedSubmit(new DialogSubmittListener<List<TestDataUnit>>() {
            @Override
            public void onValued(List<TestDataUnit> testDataUnits, int userValue) {
                if(testDataUnits != null)
                {
                    //在数据库中添加病人信息，sick_id相同的覆盖
                    PatientService.getInstance().saveOrUpdatePatient(testDataUnits.get(0).getTestData().getPatient());

                    TestFunction.getInstance().addConfiguredTestDataUnit(testDataUnits, userValue);
                }

                sampleInputDialogShowSemaphore.release();
            }
        });

        sampleDataAdapter = new SampleDataAdapter(testDataUnits, this, R.layout.layout_sample_item_view);
        sampleDataAdapter.setOnItemClickListener(new OnViewItemClickListener<TestDataUnit>() {
            @Override
            public void onItemClick(View view, int position, TestDataUnit testDataUnit) {

                Log.d(TAG, "click: " + position);
            }
        });
        sampleDataAdapter.setOnItemLongClickListener(new OnViewItemLongClickListener<TestDataUnit>() {
            @Override
            public void onItemLongClick(View view, int position, TestDataUnit testDataUnit) {

            }
        });

        sample_data_recyclerview.setAdapter(sampleDataAdapter);
        sample_data_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        sample_data_recyclerview.addItemDecoration(new UserDecoration(2));

        add_sample_imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestDataUnit testDataUnit = new TestDataUnit();
                testDataUnit.getTestData().setCard(new Card());
                testDataUnit.getTestData().getCard().setWaitt(20);
                testDataUnit.setStartTime(System.currentTimeMillis());
                testDataUnits.add(testDataUnit);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(mReceiver);
    }
}
