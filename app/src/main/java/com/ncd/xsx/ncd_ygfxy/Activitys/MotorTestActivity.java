package com.ncd.xsx.ncd_ygfxy.Activitys;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ncd.xsx.ncd_ygfxy.Activitys.Dialogs.DialogDefine;
import com.ncd.xsx.ncd_ygfxy.Activitys.Dialogs.SelectDialog;
import com.ncd.xsx.ncd_ygfxy.Activitys.Dialogs.WaitDialog;
import com.ncd.xsx.ncd_ygfxy.Activitys.Listeners.DialogSubmittListener;
import com.ncd.xsx.ncd_ygfxy.R;
import com.ncd.xsx.ncd_ygfxy.RxBus.RxBus;
import com.ncd.xsx.ncd_ygfxy.RxBus.ServiceStatuMsg;
import com.ncd.xsx.ncd_ygfxy.SerialDriver.DeviceSerial.DeviceSerialDefine;
import com.ncd.xsx.ncd_ygfxy.SerialDriver.DeviceSerial.DeviceSerialEntity;
import com.ncd.xsx.ncd_ygfxy.Services.DeviceControlBoardService.DeviceFunction;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MotorTestActivity extends MyActivity {

    private static final String MotorTestActivity_tag = "MotorTestActivity_tag";

    Spinner motor_spinner;
    EditText motor_value_edittext;
    Button start_test_motor_button;

    private Disposable motorDisposable;
    private WaitDialog waitDialog = null;

    private int sessionId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motor_test);

        super.MyActivityCommonInit();

        motor_spinner = (Spinner) findViewById(R.id.motor_spinner);
        motor_value_edittext = (EditText) findViewById(R.id.motor_value_edittext);
        start_test_motor_button = (Button) findViewById(R.id.start_test_motor_button);

        waitDialog = WaitDialog.newInstance(this);

        start_test_motor_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sessionId = new Long(System.currentTimeMillis()).intValue();
                //DeviceFunction.getInstance().startMotorMove(sessionId, (String) motor_spinner.getSelectedItem(), Integer.valueOf(motor_value_edittext.getText().toString()).intValue());

               /* if()
                    Toast.makeText(MotorTestActivity.this, "启动成功", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MotorTestActivity.this, "启动失败", Toast.LENGTH_SHORT).show();*/
            }
        });

        motorDisposable = RxBus.getInstance().toObservable(DeviceSerialEntity.class, new Consumer<DeviceSerialEntity>() {
            @Override
            public void accept(DeviceSerialEntity msg) throws Exception {

                if(msg.SessionId == sessionId)
                {
                    if(msg.cmd == DeviceSerialDefine.MOTOR_1_MOVE_CMD)
                        Toast.makeText(MotorTestActivity.this, new String(msg.data), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        RxBus.getInstance().unregister(motorDisposable);
    }
}
