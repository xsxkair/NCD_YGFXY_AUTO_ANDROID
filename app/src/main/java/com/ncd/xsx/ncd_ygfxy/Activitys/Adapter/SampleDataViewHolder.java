package com.ncd.xsx.ncd_ygfxy.Activitys.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ncd.xsx.ncd_ygfxy.Databases.Entity.TestData;
import com.ncd.xsx.ncd_ygfxy.R;
import com.ncd.xsx.ncd_ygfxy.Services.TestService.TestDataUnit;
import com.ncd.xsx.ncd_ygfxy.Services.TestService.TestState;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

public class SampleDataViewHolder extends RecyclerView.ViewHolder {

    View itemView;
    TextView sample_index_textview;
    TextView sample_id_textview;
    TextView patient_id_textview;
    TextView patient_bed_num_textview;
    TextView patient_name_textview;
    TextView patient_item_textview;
    TextView test_state_textview;


    public SampleDataViewHolder(View itemView) {
        super(itemView);

        this.itemView = itemView;
        sample_index_textview = (TextView) itemView.findViewById(R.id.sample_index_textview);
        sample_id_textview = (TextView) itemView.findViewById(R.id.sample_id_textview);
        patient_id_textview = (TextView) itemView.findViewById(R.id.patient_id_textview);
        patient_bed_num_textview = (TextView) itemView.findViewById(R.id.patient_bed_num_textview);
        patient_name_textview = (TextView) itemView.findViewById(R.id.patient_name_textview);
        patient_item_textview = (TextView) itemView.findViewById(R.id.patient_item_textview);
        test_state_textview = (TextView) itemView.findViewById(R.id.test_state_textview);
    }

    public void fillViewHolderContent(TestDataUnit testDataUnit, int position){

        sample_index_textview.setText(String.format("%03d", position));

        if(testDataUnit.getStartTime() == TestState.STATE_NO_CONF)
            test_state_textview.setText(R.string.sample_state_no_conf);
        else if(testDataUnit.getStartTime() == TestState.STATE_WAIT)
            test_state_textview.setText(R.string.sample_state_wait);
        else
            test_state_textview.setText(String.format("%d", testDataUnit.getRemainingTimeInSec()));

        sample_id_textview.setText(testDataUnit.getTestData().getSampleid());

        try {
            patient_item_textview.setText(testDataUnit.getTestData().getCard().getItemConstData().getName_en());
        }catch (Exception e){
            patient_item_textview.setText(null);
        }

        try {
            patient_id_textview.setText(testDataUnit.getTestData().getPatient().getSickid());
        }catch (Exception e){
            patient_id_textview.setText(null);
        }

        try {
            patient_bed_num_textview.setText(testDataUnit.getTestData().getBednum());
        }catch (Exception e){
            patient_bed_num_textview.setText(null);
        }

        try {
            patient_name_textview.setText(testDataUnit.getTestData().getPatient().getName());
        }catch (Exception e){
            patient_name_textview.setText(null);
        }


        userHolderUnSelectedEvent();
    }

    public void userHolderSelectedEvent(){
        this.itemView.setBackgroundResource(R.color.button_pressed);
    }

    public void userHolderUnSelectedEvent(){
        this.itemView.setBackgroundResource(R.color.white);
    }

    public View getItemView() {
        return itemView;
    }

}
