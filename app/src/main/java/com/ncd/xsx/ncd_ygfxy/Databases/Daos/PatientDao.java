package com.ncd.xsx.ncd_ygfxy.Databases.Daos;

import android.content.Context;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.ncd.xsx.ncd_ygfxy.Databases.Entity.Patient;
import com.ncd.xsx.ncd_ygfxy.Databases.Entity.TestData;
import com.ncd.xsx.ncd_ygfxy.Databases.Page;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class PatientDao extends BaseDao<Patient> {
    public PatientDao(Context context) {
        super(context);
    }

    public List<Patient> queryPatientBySickId(String sick_id) throws SQLException {
        QueryBuilder<Patient, Integer> queryBuilder = getDaoQueryBuilder();
        Where<Patient, Integer> where = queryBuilder.where();

        where.like("sickid", String.format("%%%s%%", sick_id));

        List<Patient> testDataList = daoQueryBuilderQuery(queryBuilder);

        return testDataList;
    }
}
