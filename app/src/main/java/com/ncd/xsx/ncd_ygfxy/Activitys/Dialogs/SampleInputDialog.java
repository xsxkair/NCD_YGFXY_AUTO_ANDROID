package com.ncd.xsx.ncd_ygfxy.Activitys.Dialogs;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.ncd.xsx.ncd_ygfxy.Activitys.Adapter.PatientSpinnerAdapter;
import com.ncd.xsx.ncd_ygfxy.Activitys.Listeners.DialogCancelListener;
import com.ncd.xsx.ncd_ygfxy.Activitys.Listeners.DialogSubmittListener;
import com.ncd.xsx.ncd_ygfxy.Databases.Entity.Card;
import com.ncd.xsx.ncd_ygfxy.Databases.Entity.Patient;
import com.ncd.xsx.ncd_ygfxy.Databases.Entity.User;
import com.ncd.xsx.ncd_ygfxy.Databases.Services.PatientService;
import com.ncd.xsx.ncd_ygfxy.Defines.ItemConstData;
import com.ncd.xsx.ncd_ygfxy.Defines.PublicStringDefine;
import com.ncd.xsx.ncd_ygfxy.R;
import com.ncd.xsx.ncd_ygfxy.Services.TestService.TestDataUnit;
import com.ncd.xsx.ncd_ygfxy.Services.TestService.TestFunction;
import com.ncd.xsx.ncd_ygfxy.Services.TestService.TestState;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class SampleInputDialog extends DialogFragment {

    private EditText sample_id_editview;
    private EditText patient_bed_num_edittext;
    private AutoCompleteTextView patient_sick_id_autocompletetextview;
    private EditText patient_name_edittext;
    private RadioGroup patient_sex_radiogroup;
    private RadioButton patient_man_radiobutton;
    private EditText patient_age_edittext;
    private FlexboxLayout item_select_flexboxLayout;
    private CheckBox[] item_checkbox;
    private Button end_button;
    private Button add_button;

    private List<String> select_item_list;
    private String sample_id;
    private int index;              //创建的测试在列表中位置
    private Patient patient;
    private long uniqueNum;
    private PatientSpinnerAdapter patientSpinnerAdapter;
    private Observer<List<Patient>> patientListObserver;

    private Bundle args = null;

    private DialogSubmittListener<List<TestDataUnit>> dialogSubmittListener = null;

    public void setOnDialogValuedSubmit(DialogSubmittListener<List<TestDataUnit>> listener){
        dialogSubmittListener = listener;
    }

    public static SampleInputDialog newInstance() {
        SampleInputDialog f = new SampleInputDialog();

        // Supply num input as an argument.
        f.args = new Bundle();

        f.setArguments(f.args);

        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_sample_input, container);

        Log.i(PublicStringDefine.OWNER_TAG, "onCreateView");

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);

        sample_id_editview = (EditText) view.findViewById(R.id.sample_id_editview);
        patient_sick_id_autocompletetextview = (AutoCompleteTextView) view.findViewById(R.id.patient_sick_id_autocompletetextview);
        patient_bed_num_edittext = (EditText) view.findViewById(R.id.patient_bed_num_edittext);
        patient_name_edittext = (EditText) view.findViewById(R.id.patient_name_edittext);
        patient_sex_radiogroup = (RadioGroup) view.findViewById(R.id.patient_sex_radiogroup);
        patient_man_radiobutton = (RadioButton) view.findViewById(R.id.patient_man_radiobutton);
        patient_age_edittext = (EditText) view.findViewById(R.id.patient_age_edittext);
        item_select_flexboxLayout = (FlexboxLayout) view.findViewById(R.id.item_select_flexboxLayout);
        item_checkbox = new CheckBox[ItemConstData.ItemConstDataSize];
        end_button = (Button) view.findViewById(R.id.end_button);
        add_button = (Button) view.findViewById(R.id.add_button);

        uniqueNum = System.currentTimeMillis();

        patientListObserver = new Observer<List<Patient>>() {
            Disposable d = null;
            @Override
            public void onSubscribe(Disposable d) {
                this.d = d;
            }

            @Override
            public void onNext(List<Patient> patients) {
                patientSpinnerAdapter.updateData(patients);

                Log.i(PublicStringDefine.OWNER_TAG, "patient size:"+patients.size());
            }

            @Override
            public void onError(Throwable e) {
                Log.e(PublicStringDefine.OWNER_TAG, "query error", e);
                this.d.dispose();
            }

            @Override
            public void onComplete() {
                this.d.dispose();
            }
        };

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(PublicStringDefine.OWNER_TAG, s.toString());
                if(s.length() > 0)
                    PatientService.getInstance().queryAllPatientBySickIdService(patientListObserver, s.toString());
            }
        };
        patientSpinnerAdapter = new PatientSpinnerAdapter(getActivity(), null);
        patient_sick_id_autocompletetextview.setAdapter(patientSpinnerAdapter);
        patient_sick_id_autocompletetextview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                patient = patientSpinnerAdapter.getItem(position);
                patient_sick_id_autocompletetextview.removeTextChangedListener(textWatcher);
                patient_sick_id_autocompletetextview.setText(patient.getSickid());
                Selection.setSelection(patient_sick_id_autocompletetextview.getText(), patient.getSickid().length());
                patient_sick_id_autocompletetextview.addTextChangedListener(textWatcher);

                //填充选择的病人信息
                patient_name_edittext.setText(patient.getName());
                patient_age_edittext.setText(String.format("%d", patient.getAge()));
                patient_sex_radiogroup.check(patient.getSex() ? R.id.patient_man_radiobutton : R.id.patient_woman_radiobutton);
            }
        });
        patient_sick_id_autocompletetextview.addTextChangedListener(textWatcher);


        patient_sex_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.patient_man_radiobutton:
                        patient.setSex(true);break;
                    case R.id.patient_woman_radiobutton:
                        patient.setSex(false);break;
                }
            }
        });

        select_item_list = new ArrayList<>();
        CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CheckBox checkBox = (CheckBox) buttonView;
                if(isChecked)
                    select_item_list.add(checkBox.getText().toString());
                else if(select_item_list.contains(checkBox.getText().toString()))
                    select_item_list.remove(checkBox.getText().toString());

                if(select_item_list.isEmpty() || sample_id_editview.getText().length() <= 0)
                    add_button.setEnabled(false);
                else
                    add_button.setEnabled(true);
            }
        };

        for(int i=0; i<item_checkbox.length; i++)
        {
            item_checkbox[i] = new CheckBox(getActivity());
            item_checkbox[i].setTextSize(24);
            item_checkbox[i].setTextColor(getResources().getColor(R.color.ncd_blue));
            item_checkbox[i].setPadding(0, 0, 10, 10);
            item_checkbox[i].setText(ItemConstData.valueOf(i).getName_en());
            item_checkbox[i].setOnCheckedChangeListener(checkedChangeListener);

            item_select_flexboxLayout.addView(item_checkbox[i]);
        }

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //组合病人信息
                //如果为null，说明没有从数据库中选择
                String sick_id = patient_sick_id_autocompletetextview.getText().toString();
                String patient_name = patient_name_edittext.getText().toString();
                if(sick_id.length() <= 0 && patient_name.length() <= 0)
                {
                    ;//则忽略病人信息
                }
                else if(sick_id.length() > 0 )
                {
                    if(patient_name.length() <= 0)
                    {
                        Toast.makeText(getActivity(), "请输入病人姓名！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else
                    {
                        if(patient == null)
                            patient = new Patient();
                        patient.setSickid(sick_id);
                        patient.setName(patient_name);

                        try{
                            patient.setAge(Integer.valueOf(patient_age_edittext.getText().toString()));
                        }catch (Exception e){
                            patient.setAge(0);
                        }

                        patient.setSex(patient_man_radiobutton.isChecked());
                    }
                }

                List<TestDataUnit> testDataUnitList = new ArrayList<>(select_item_list.size());

                for(String item : select_item_list)
                {
                    TestDataUnit testDataUnit = new TestDataUnit(sample_id);

                    ItemConstData itemConstData = ItemConstData.getItemConstDataByName(item);
                    Card card = new Card(itemConstData);

                    testDataUnit.setStartTime(TestState.STATE_WAIT);
                    testDataUnit.getTestData().setCard(card);
                    testDataUnit.getTestData().setUniquenum(uniqueNum);
                    testDataUnit.getTestData().setTester(TestFunction.getInstance().getCurrentTester());
                    testDataUnit.getTestData().setPatient(patient);

                    testDataUnitList.add(testDataUnit);
                }

                dismiss();

                if(dialogSubmittListener != null)
                    dialogSubmittListener.onValued(testDataUnitList, index);

            }
        });

        end_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();

                if(dialogSubmittListener != null)
                    dialogSubmittListener.onValued(null, index);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(PublicStringDefine.OWNER_TAG, "onStart");

        sample_id_editview.setText(sample_id);
        patient = null;

        select_item_list.clear();

        add_button.setEnabled(false);

        getDialog().getWindow().setLayout(850, 650);//设置高宽
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.i(PublicStringDefine.OWNER_TAG, "onStop");
    }

    public void showMyDialog(FragmentManager manager, String tag, String sampleid, int index) {

        this.sample_id = sampleid;
        this.index = index;

        if(this.sample_id == null || this.sample_id.length() <= 0 || this.index < 0)
            return;

        show(manager, tag);

    }

}
