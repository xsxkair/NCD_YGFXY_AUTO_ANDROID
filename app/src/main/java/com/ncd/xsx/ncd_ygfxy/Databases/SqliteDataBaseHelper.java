package com.ncd.xsx.ncd_ygfxy.Databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.ncd.xsx.ncd_ygfxy.Databases.Entity.Patient;
import com.ncd.xsx.ncd_ygfxy.Defines.PublicStringDefine;
import com.ncd.xsx.ncd_ygfxy.Databases.Entity.Card;
import com.ncd.xsx.ncd_ygfxy.Databases.Entity.TestData;
import com.ncd.xsx.ncd_ygfxy.Databases.Entity.User;

import java.sql.SQLException;

public class SqliteDataBaseHelper extends OrmLiteSqliteOpenHelper {

    // 本类的单例实例
    private static SqliteDataBaseHelper instance;

    private static final int databasesVersion = 3;

    // 私有的构造方法
    private SqliteDataBaseHelper(Context context) {
        super(context, PublicStringDefine.dataBaseFileName, null, databasesVersion);
    }

    // 获取本类单例对象的方法
    public static synchronized SqliteDataBaseHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (SqliteDataBaseHelper.class) {
                if (instance == null) {
                    instance = new SqliteDataBaseHelper(context);
                }
            }
        }
        return instance;
    }


    public <T> Dao<T, Integer> getDaos(Class<T> clazz) throws SQLException {
        Dao<T, Integer> dao = instance.getDao(clazz);
        Log.d("xsx", dao.getTableName());
        return dao;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

        try {
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, Card.class);
            TableUtils.createTable(connectionSource, TestData.class);
            TableUtils.createTable(connectionSource, Patient.class);
            Log.d("xsx","create xsx.db success");
        } catch (SQLException e) {
            Log.d("xsx","create xsx.db error: "+e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.d("xsx","dropTable");
            TableUtils.dropTable(connectionSource, User.class, true);
            TableUtils.dropTable(connectionSource, Card.class, true);
            TableUtils.dropTable(connectionSource, Patient.class, true);
            TableUtils.dropTable(connectionSource, TestData.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        super.close();
    }
}
