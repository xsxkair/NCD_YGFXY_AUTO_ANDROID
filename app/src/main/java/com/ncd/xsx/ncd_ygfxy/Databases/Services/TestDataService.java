package com.ncd.xsx.ncd_ygfxy.Databases.Services;

import android.content.Context;

import com.ncd.xsx.ncd_ygfxy.Databases.Daos.CardDao;
import com.ncd.xsx.ncd_ygfxy.Databases.Page;
import com.ncd.xsx.ncd_ygfxy.Databases.Daos.TestDataDao;
import com.ncd.xsx.ncd_ygfxy.Databases.Entity.TestData;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TestDataService {

    private TestDataDao testDataDao = null;

    private TestDataService(){

    }

    private static class SingletonHolder{
        private static final TestDataService INSTANCE = new TestDataService();
    }

    public static TestDataService getInstance(){
        return TestDataService.SingletonHolder.INSTANCE;
    }

    public void TestDataServiceInit(Context context)
    {
        testDataDao = new TestDataDao(context);
    }

    public TestDataDao getTestDataDao() {
        return testDataDao;
    }

    public void saveNewTestDataService(Observer<Boolean> resultObserver, TestData testData){

        Observable<Boolean> observable = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {

                testDataDao.add(testData);

                emitter.onNext(true);
                emitter.onComplete();
            }
        });

        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultObserver);
    }

    public Observable<List<TestData>> queryAllTestDataService(){

        Observable<List<TestData>> observable = Observable.create(new ObservableOnSubscribe<List<TestData>>() {
            @Override
            public void subscribe(ObservableEmitter<List<TestData>> emitter) throws Exception {

                List<TestData> testDataList = testDataDao.all();

                emitter.onNext(testDataList);
                emitter.onComplete();
            }
        });

        return observable;
    }


    public void queryTestDataByPageService(Observer<Page<TestData>> resultObserver, long startPage, long pageSize, String testTime,
                                                                 String testItem, String sampleId, Boolean isChecked){

        Observable<Page<TestData>> observable = Observable.create(new ObservableOnSubscribe<Page<TestData>>() {
            @Override
            public void subscribe(ObservableEmitter<Page<TestData>> emitter) throws Exception {

                Page<TestData> testDataPage = testDataDao.queryTestDataByPage(startPage, pageSize, testTime, testItem, sampleId, isChecked);

                emitter.onNext(testDataPage);
                emitter.onComplete();
            }
        });

        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultObserver);
    }
}
