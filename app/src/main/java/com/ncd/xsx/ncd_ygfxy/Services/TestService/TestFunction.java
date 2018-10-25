package com.ncd.xsx.ncd_ygfxy.Services.TestService;

import android.content.Context;

import com.ncd.xsx.ncd_ygfxy.Databases.Daos.PatientDao;
import com.ncd.xsx.ncd_ygfxy.Databases.Entity.User;
import com.ncd.xsx.ncd_ygfxy.Databases.Services.PatientService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TestFunction {

    private Semaphore testDataUnitListSemaphore = null;
    private List<TestDataUnit> testDataUnitList = null;                 //所有待测试数据

    private User currentTester = null;

    private TestFunction(){

    }

    private static class SingletonHolder{
        private static final TestFunction INSTANCE = new TestFunction();
    }

    public static TestFunction getInstance(){
        return TestFunction.SingletonHolder.INSTANCE;
    }

    public void TestFunctionInit(Context context)
    {
        testDataUnitList = new ArrayList<TestDataUnit>();
        testDataUnitListSemaphore = new Semaphore(1);
    }

    public User getCurrentTester() {
        return currentTester;
    }

    public void setCurrentTester(User currentTester) {
        this.currentTester = currentTester;
    }

    public TestDataUnit[] getCurrentTestDataUnitList() {
        if(testDataUnitListSemaphore.tryAcquire())
        {
            TestDataUnit[] testDataUnits = new TestDataUnit[testDataUnitList.size()];
            testDataUnitList.toArray(testDataUnits);
            testDataUnitListSemaphore.release();
            return testDataUnits;
        }
        else
            return null;
    }

    /*
     * desc: 从样本编号创建测试，并添加到测试列表中
     *
     * parms: sample_id 样本编号
     *
     * return: -1 创建失败
     *          >0 创建成功后测试所在索引
     *
     * info: 2018-10-23 14:20, Administrator
     *
     */
    public int addNewTestDataUnitService(String sample_id){

        TestDataUnit testDataUnit = new TestDataUnit(sample_id);
        return addNewTestDataUnitService(testDataUnit);
    }

    /*
     * desc: 添加新建的测试到测试列表中
     *
     * parms: testDataUnit
     *
     * return: -1 创建失败
     *          >0 创建成功后测试所在索引
     *
     * info: 2018-10-23 14:28, Administrator
     *
     */
    public int addNewTestDataUnitService(TestDataUnit testDataUnit){

        if(testDataUnitListSemaphore.tryAcquire())
        {
            int size = testDataUnitList.size();
            //检查sample id是否已存在，
            //存在，且未配置： 直接返回索引
            //不存在，添加
            for(int i=0; i<size; i++)
            {
                TestDataUnit tempUnit = testDataUnitList.get(i);

                if(testDataUnit.getTestData().getSampleid().equals(tempUnit.getTestData().getSampleid()))
                {
                    testDataUnitListSemaphore.release();
                    return i;
                }
            }

            testDataUnitList.add(testDataUnit);
            testDataUnitListSemaphore.release();
            return size;
        }
        else
            return -1;
    }

    public int addConfiguredTestDataUnit(List<TestDataUnit> testDataUnits, int startIndex){
        if(testDataUnitListSemaphore.tryAcquire())
        {
            int size = testDataUnitList.size();

            testDataUnitList.remove(startIndex);

            testDataUnitList.addAll(startIndex, testDataUnits);

            testDataUnitListSemaphore.release();
            return size;
        }
        else
            return -1;
    }

}
