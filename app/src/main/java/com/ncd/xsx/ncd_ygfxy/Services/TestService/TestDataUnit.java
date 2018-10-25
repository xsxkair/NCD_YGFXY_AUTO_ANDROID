package com.ncd.xsx.ncd_ygfxy.Services.TestService;

import com.ncd.xsx.ncd_ygfxy.Databases.Entity.TestData;

public class TestDataUnit {

    private TestData testData;

    //0 -- 说明还没开始倒计时
    private long startTime;

    private static final long MAX_REMAINING_TIME = 0xffffffffl;

    public TestDataUnit(){
        this.startTime = TestState.STATE_NO_CONF;
        this.testData = new TestData();
    };

    public TestDataUnit(String sample){
        this.startTime = TestState.STATE_NO_CONF;
        this.testData = new TestData(sample);
    };

    public TestData getTestData() {
        return testData;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    //获取倒计时剩余时间
    public long getRemainingTimeInSec(){
        if(this.startTime <= 0 || this.testData.getCard() == null)
            return TestDataUnit.MAX_REMAINING_TIME;

        long totalTime = this.testData.getCard().getWaitt();
        long usedTime = System.currentTimeMillis();

        usedTime -= startTime;
        usedTime /= 1000;

        totalTime *= 60;

        if(totalTime < usedTime)
            return 0;
        else
            return (totalTime - usedTime);
    }
}
