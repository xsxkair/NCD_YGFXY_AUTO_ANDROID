package com.ncd.xsx.ncd_ygfxy.Databases.Services;

import android.content.Context;

import com.ncd.xsx.ncd_ygfxy.Databases.Daos.PatientDao;
import com.ncd.xsx.ncd_ygfxy.Databases.Daos.UserDao;
import com.ncd.xsx.ncd_ygfxy.Databases.Entity.Patient;
import com.ncd.xsx.ncd_ygfxy.Databases.Entity.User;
import com.ncd.xsx.ncd_ygfxy.Logger.LoggerUnits;

import java.sql.SQLException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PatientService {

    private PatientDao dao = null;

    private PatientService(){

    }

    private static class SingletonHolder{
        private static final PatientService INSTANCE = new PatientService();
    }

    public static PatientService getInstance(){
        return PatientService.SingletonHolder.INSTANCE;
    }

    public void serviceInit(Context context)
    {
        dao = new PatientDao(context);
    }

    public PatientDao getDao() {
        return dao;
    }

    public int saveOrUpdatePatient(Patient user)
    {
        try{

            if(user == null)
                return -1;

            Patient tempUser = dao.queryOneByColumn("sickid", user.getSickid());
            if(tempUser == null)
            {
                dao.add(user);
                return 1;           //add success;
            }
            else
            {
                user.setId(tempUser.getId());
                dao.update(user);
                return 2;           //update success;
            }
        }catch (SQLException e){
            LoggerUnits.error("saveOrUpdateUser error", e);
            return -1;
        }
    }

    public void queryAllPatientBySickIdService(Observer<List<Patient>> resultObserver, String sick_id){
        Observable<List<Patient>> observable = Observable.create(new ObservableOnSubscribe<List<Patient>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Patient>> emitter) throws Exception {

                List<Patient> userList = dao.queryPatientBySickId(sick_id);

                emitter.onNext(userList);
                emitter.onComplete();
            }
        });

        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultObserver);
    }
}
