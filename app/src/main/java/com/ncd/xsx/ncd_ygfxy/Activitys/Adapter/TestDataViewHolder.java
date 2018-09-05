package com.ncd.xsx.ncd_ygfxy.Activitys.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ncd.xsx.ncd_ygfxy.Databases.Entity.TestData;
import com.ncd.xsx.ncd_ygfxy.R;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

public class TestDataViewHolder extends RecyclerView.ViewHolder {

    View itemView;
    TextView testDataIndexTextView;
    TextView testDataItemTextView;
    TextView testDataSampleTextView;
    TextView testDataResultTextView;
    TextView testDataDateTextView;
    TextView testDataTesterTextView;
    ImageView testDataReportImageView;
    private DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private NumberFormat numberFormat = NumberFormat.getNumberInstance();
    private Boolean ischecked;

    public TestDataViewHolder(View itemView) {
        super(itemView);

        this.itemView = itemView;
        testDataIndexTextView = (TextView) itemView.findViewById(R.id.testDataIndexTextView);
        testDataItemTextView = (TextView) itemView.findViewById(R.id.testDataItemTextView);
        testDataSampleTextView = (TextView) itemView.findViewById(R.id.testDataSampleTextView);
        testDataResultTextView = (TextView) itemView.findViewById(R.id.testDataResultTextView);
        testDataDateTextView = (TextView) itemView.findViewById(R.id.testDataDateTextView);
        testDataTesterTextView = (TextView) itemView.findViewById(R.id.testDataTesterTextView);
        testDataReportImageView = (ImageView) itemView.findViewById(R.id.testDataReportImageView);
    }

    public void fillViewHolderContent(TestData testData){
        testDataIndexTextView.setText(testData.getIndex().toString());
        testDataItemTextView.setText(testData.getCard().getItem());
        testDataSampleTextView.setText(testData.getSampleid());

        numberFormat.setMaximumFractionDigits(testData.getCard().getPointnum());
        if(!testData.getResultok())
            testDataResultTextView.setText(R.string.TestResultErrorText);
        else if(testData.getTestv() < testData.getCard().getLowestresult())
            testDataResultTextView.setText(String.format("<%s %s", numberFormat.format(testData.getCard().getLowestresult()), testData.getCard().getMeasure()));
        else
            testDataResultTextView.setText(String.format("%s %s", numberFormat.format(testData.getTestv()), testData.getCard().getMeasure()));

        testDataDateTextView.setText(sdf.format(testData.getTesttime()));
        testDataTesterTextView.setText(testData.getTester());

        ischecked = testData.getCheck();
        if(ischecked == null)
            testDataReportImageView.setImageResource(R.drawable.record_b);
        else if (ischecked)
            testDataReportImageView.setImageResource(R.drawable.recordpass_b);
        else
            testDataReportImageView.setImageResource(R.drawable.recordnopass_b);

        userHolderUnSelectedEvent();
    }

    public void userHolderSelectedEvent(){
        this.itemView.setBackgroundResource(R.color.button_pressed);
    }

    public void userHolderUnSelectedEvent(){
        if(ischecked == null)
            this.itemView.setBackgroundResource(R.color.white);
        else if(ischecked)
            this.itemView.setBackgroundResource(R.color.light_green);
        else
            this.itemView.setBackgroundResource(R.color.light_Red);
    }

    public View getItemView() {
        return itemView;
    }

}
