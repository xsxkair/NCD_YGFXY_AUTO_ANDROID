package com.ncd.xsx.ncd_ygfxy.Activitys;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ncd.xsx.ncd_ygfxy.Activitys.Adapter.TestDataAdapter;
import com.ncd.xsx.ncd_ygfxy.Activitys.Adapter.UserDecoration;
import com.ncd.xsx.ncd_ygfxy.Activitys.Dialogs.ComfirmDialog;
import com.ncd.xsx.ncd_ygfxy.Activitys.Dialogs.DialogDefine;
import com.ncd.xsx.ncd_ygfxy.Activitys.Dialogs.SelectDialog;
import com.ncd.xsx.ncd_ygfxy.Activitys.Listeners.DialogSubmittListener;
import com.ncd.xsx.ncd_ygfxy.Activitys.Listeners.OnViewItemClickListener;
import com.ncd.xsx.ncd_ygfxy.Activitys.Listeners.OnViewItemLongClickListener;
import com.ncd.xsx.ncd_ygfxy.Databases.Entity.User;
import com.ncd.xsx.ncd_ygfxy.Databases.Page;
import com.ncd.xsx.ncd_ygfxy.Databases.Services.TestDataService;
import com.ncd.xsx.ncd_ygfxy.R;
import com.ncd.xsx.ncd_ygfxy.Databases.Entity.Card;
import com.ncd.xsx.ncd_ygfxy.Databases.Entity.TestData;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class DataQueryActivity extends MyActivity {

    private static final String activity_tag = "DataQueryActivity";

    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

    TextView testDateQueryContentEditText;
    Spinner testItemQuerySpinner;
    EditText testSampleIdQueryEditText;
    Button createTestDataButton;
    Button queryDataButton;
    RecyclerView dataRecycerView;
    TextView currentPageIndexTextView;
    ImageView clearTestDateQueryImageView;
    ImageView clearTestItemQueryImageView;
    ImageView clearTestSampleQueryImageView;
    ImageView pre_page_imagebutton;
    ImageView next_page_imagebutton;
    ImageView reportCheckQueryImageView;

    private String[] itemArray;

    private DatePickerDialog test_date_datepicker_dialog;
    private SelectDialog selectDialog;
    private String[] action_menu = null;

    private TestDataAdapter testDataAdapter;
    private Observer<Page<TestData>> testDataPageObserver;

    private long currentPageIndex = 0;
    private long totalPageSize = 0;
    private String queryTime = null;
    private String queryItem = null;
    private String querySampleId = null;
    private Boolean queryChecked = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_query);

        super.MyActivityCommonInit();

        testDateQueryContentEditText = (TextView) findViewById(R.id.testDateQueryContentEditText);
        testItemQuerySpinner = (Spinner) findViewById(R.id.testItemQuerySpinner);
        testSampleIdQueryEditText = (EditText) findViewById(R.id.testSampleIdQueryEditText);
        createTestDataButton = (Button) findViewById(R.id.createTestDataButton);
        queryDataButton = (Button) findViewById(R.id.queryDataButton);
        dataRecycerView = (RecyclerView) findViewById(R.id.dataRecycerView);
        currentPageIndexTextView = (TextView) findViewById(R.id.currentPageIndexTextView);
        clearTestDateQueryImageView = (ImageView) findViewById(R.id.clearTestDateQueryImageView);
        clearTestItemQueryImageView = (ImageView) findViewById(R.id.clearTestItemQueryImageView);
        clearTestSampleQueryImageView = (ImageView) findViewById(R.id.clearTestSampleQueryImageView);
        reportCheckQueryImageView = (ImageView) findViewById(R.id.reportCheckQueryImageView);
        pre_page_imagebutton = (ImageView) findViewById(R.id.pre_page_imagebutton);
        next_page_imagebutton = (ImageView) findViewById(R.id.next_page_imagebutton);

        Calendar t = Calendar.getInstance();
        test_date_datepicker_dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                testDateQueryContentEditText.setText(String.format("%d-%0d-%0d", year, monthOfYear, dayOfMonth));
                clearTestDateQueryImageView.setVisibility(View.VISIBLE);
            }
        }, t.get(Calendar.YEAR), t.get(Calendar.MONTH), t.get(Calendar.DAY_OF_MONTH));

        itemArray = getResources().getStringArray(R.array.NCD_Test_Item_Array);

        action_menu = getResources().getStringArray(R.array.data_item_long_press_action_menu);
        selectDialog = SelectDialog.newInstance(this);
        selectDialog.setDialogSubmittListener(new DialogSubmittListener<Integer>() {
            @Override
            public void onValued(Integer value, int userValue) {

                if(value == DialogDefine.DIALOG_SUBMMIT_COM_VALUE_1)
                    Log.i(activity_tag, "delete: "+userValue);
                else if(value == DialogDefine.DIALOG_SUBMMIT_COM_VALUE_2)
                    Log.i(activity_tag, "up: "+userValue);
                else if(value == DialogDefine.DIALOG_SUBMMIT_COM_VALUE_3)
                    Log.i(activity_tag, "print: "+userValue);
            }
        });

        //测试日期
        testDateQueryContentEditText.setText(null);
        testDateQueryContentEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test_date_datepicker_dialog.show();
            }
        });

        clearTestDateQueryImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testDateQueryContentEditText.setText(null);
                clearTestDateQueryImageView.setVisibility(View.INVISIBLE);
            }
        });

        //测试项目
        testItemQuerySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    queryItem = null;
                    clearTestItemQueryImageView.setVisibility(View.INVISIBLE);
                } else {
                    queryItem = itemArray[position];
                    clearTestItemQueryImageView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        clearTestItemQueryImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testItemQuerySpinner.setSelection(0);
            }
        });

        //样本编号
        testSampleIdQueryEditText.setText(null);
        testSampleIdQueryEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString();
                if (string.length() > 0) {
                    querySampleId = String.format("%%%s%%", string);
                    clearTestSampleQueryImageView.setVisibility(View.VISIBLE);
                } else {
                    querySampleId = null;
                    clearTestSampleQueryImageView.setVisibility(View.INVISIBLE);
                }
            }
        });
        clearTestSampleQueryImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testSampleIdQueryEditText.setText(null);
            }
        });

        //报告审核
        reportCheckQueryImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (queryChecked == null) {
                    queryChecked = true;
                    reportCheckQueryImageView.setImageResource(R.drawable.recordpass);
                } else if (queryChecked) {
                    queryChecked = false;
                    reportCheckQueryImageView.setImageResource(R.drawable.recordnopass);
                } else {
                    queryChecked = null;
                    reportCheckQueryImageView.setImageResource(R.drawable.record);
                }

                startQueryTestDataPages();
            }
        });


        //测试数据
        testDataAdapter = new TestDataAdapter(null, this, R.layout.layout_test_data_item_view);

        testDataAdapter.setOnItemClickListener(new OnViewItemClickListener<TestData>() {
            @Override
            public void onItemClick(View view, int position, TestData testData) {

                Log.d("xsx", "click: " + position);
                Intent intent = new Intent(DataQueryActivity.this, TestDetailActivity.class);
                intent.putExtra(getResources().getResourceName(R.string.TestDataIntentNameText), testData);
                startActivity(intent);
            }
        });
        testDataAdapter.setOnItemLongClickListener(new OnViewItemLongClickListener<TestData>() {
            @Override
            public void onItemLongClick(View view, int position, TestData testData) {
                selectDialog.showDialog(getFragmentManager(), activity_tag, getResources().getString(R.string.setting_menu_net_select_dialog_title),
                        action_menu, position);
            }
        });

        dataRecycerView.setAdapter(testDataAdapter);
        dataRecycerView.setLayoutManager(new LinearLayoutManager(this));
        dataRecycerView.addItemDecoration(new UserDecoration(2));

        //页码切换
        pre_page_imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPageIndex--;
                if (currentPageIndex < 0)
                    currentPageIndex = totalPageSize - 1;

                if (currentPageIndex < 0)
                    currentPageIndex = 0;

                startQueryTestDataPages();
            }
        });

        next_page_imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPageIndex++;
                if (currentPageIndex >= totalPageSize)
                    currentPageIndex = 0;

                startQueryTestDataPages();
            }
        });

        testDataPageObserver = new Observer<Page<TestData>>() {
            private Disposable disposable = null;

            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(Page<TestData> testDataPage) {
                showQueiedTestData(testDataPage);
            }

            @Override
            public void onError(Throwable e) {
                Log.d("xsx", e.getMessage());
                disposable.dispose();
            }

            @Override
            public void onComplete() {
                disposable.dispose();
            }
        };

        createTestDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewTestData();
            }
        });

        queryDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQueryTestDataPages();
            }
        });

        currentPageIndex = 0;
        startQueryTestDataPages();
    }

    private void createNewTestData() {
        Card card = new Card();
        TestData testData = new TestData();




        testData.setCard(card);
        testData.setResultok(false);
        testData.setTestv((float) (Math.random()));
        testData.setTester(new User("zx"));
        testData.setBline(213);
        testData.setCline(267);
        testData.setTline(187);
        testData.setSampleid(String.valueOf(System.currentTimeMillis()));
        testData.setTesttime(new Timestamp(System.currentTimeMillis()));
        testData.setSeries("[279,294,302,307,310,312,313,314,314,314,315,315,315,316,316,317,317,318,318,319,319,319,320,320,320,320,320,319,319,318,318,317,316,315,315,314,313,313,312,311,311,310,310,309,309,309,308,308,308,307,307,307,307,307,307,306,306,306,306,306,306,306,306,306,306,306,306,306,306,306,306,306,306,306,306,306,306,306,306,306,306,306,306,306,306,306,306,307,307,307,308,308,309,309,309,310,310,311,311,311,312,312,312,312,312,312,312,311,311,311,310,310,309,309,309,308,308,307,307,307,306,306,306,305,305,305,305,305,305,305,305,305,305,305,305,305,305,305,305,305,305,305,305,305,305,305,305,305,305,304,304,304,304,304,304,305,305,305,305,305,305,306,306,307,308,309,311,313,317,321,327,334,345,358,375,397,422,453,487,525,565,606,646,683,715,740,757,765,763,751,730,702,667,628,586,543,501,460,423,390,361,336,316,299,285,274,266,260,255,252,249,248,247,246,246,246,246,246,246,246,246,246,247,247,247,248,248,248,249,249,249,250,250,250,251,251,252,252,253,253,254,255,256,257,259,261,263,267,272,278,286,297,311,330,353,382,417,456,501,549,599,650,698,742,779,807,825,832,829,815,791,760,721,678,631,582,533,486,441,400,363,331,304,281,262,247,235,225,218,213,209,207,205,204,203,202,202,202,202,202]");

        try {
            TestDataService.getInstance().getTestDataDao().add(testData);
            startQueryTestDataPages();
        } catch (SQLException e) {
            Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show();
        }

    }

    private void showQueiedTestData(Page<TestData> testDataPage) {

        totalPageSize = testDataPage.getTotalPages();
        currentPageIndexTextView.setText(String.format(" page: %d/%d ", testDataPage.getCurrentPageIndex(), testDataPage.getTotalPages()));

        testDataAdapter.updateTestData(testDataPage.getContent());
        dataRecycerView.callOnClick();
    }

    private void startQueryTestDataPages() {

        TestDataService.getInstance().queryTestDataByPageService(testDataPageObserver, currentPageIndex, 30l,
                queryTime, queryItem, querySampleId, queryChecked);
    }

}
