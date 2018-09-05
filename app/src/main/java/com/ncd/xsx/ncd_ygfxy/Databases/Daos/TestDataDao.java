package com.ncd.xsx.ncd_ygfxy.Databases.Daos;

import android.content.Context;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.ncd.xsx.ncd_ygfxy.Databases.Page;
import com.ncd.xsx.ncd_ygfxy.Databases.Entity.TestData;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;


public class TestDataDao extends BaseDao<TestData> {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00.000");

    public TestDataDao(Context context) {
        super(context);
    }

    public Page<TestData> queryTestDataByPage(long startPage, long pageSize, String testTime, String testItem, String sampleId, Boolean isChecked) throws SQLException {
        QueryBuilder<TestData, Integer> queryBuilder = getDaoQueryBuilder();
        Where<TestData, Integer> where = queryBuilder.where();
        Page<TestData> page = new Page<>();

        if (isChecked != null)
            where.eq("check", isChecked);
        else
            where.isNotNull("id");

        if(testItem != null)
        {
            where.and().eq("item", testItem);
        }

        if(sampleId != null)
        {
            where.and().like("sampleid", sampleId);
        }

        if(testTime != null)
        {
            Timestamp begin = Timestamp.valueOf(String.format("%s 00:00:00.000", testTime));

            Timestamp end = Timestamp.valueOf(String.format("%s 23:59:59.999", testTime));

            where.and().between("testtime", begin, end);
        }

        page.setCurrentPageIndex(startPage);
        page.setTotalElements(queryBuilder.countOf());
        if(page.getTotalElements() % pageSize != 0)
            page.setTotalPages(queryBuilder.countOf() / pageSize + 1l);
        else
            page.setTotalPages(queryBuilder.countOf() / pageSize);

        queryBuilder.offset(startPage*pageSize);
        queryBuilder.limit(pageSize);
        queryBuilder.orderBy("testtime", false);

        List<TestData> testDataList = daoQueryBuilderQuery(queryBuilder);
        long startIndex = startPage*pageSize;
        for (TestData testData : testDataList){
            testData.setIndex(startIndex);
            startIndex++;
        }

        page.setContent(testDataList);

        return page;
    }

    public List<TestData> queryTestDataWhereIsNotUpLimit(boolean ncd, int num) throws SQLException {
        QueryBuilder<TestData, Integer> queryBuilder = getDaoQueryBuilder();
        Where<TestData, Integer> where = queryBuilder.where();

        if(ncd)
            where.eq("ncdup", false);
        else
            where.eq("userup", false);

        queryBuilder.offset(0l);
        queryBuilder.limit(10l);

        List<TestData> testDataList = daoQueryBuilderQuery(queryBuilder);

        return testDataList;
    }
}
