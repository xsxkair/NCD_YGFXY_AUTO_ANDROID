package com.ncd.xsx.ncd_ygfxy.Databases.Services;

import android.content.Context;

import com.ncd.xsx.ncd_ygfxy.Databases.Daos.TestDataDao;
import com.ncd.xsx.ncd_ygfxy.Databases.Daos.UserDao;
import com.ncd.xsx.ncd_ygfxy.Databases.Entity.User;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserService {

    private UserDao userDao = null;

    private UserService(){

    }

    private static class SingletonHolder{
        private static final UserService INSTANCE = new UserService();
    }

    public static UserService getInstance(){
        return UserService.SingletonHolder.INSTANCE;
    }

    public void UserServiceInit(Context context)
    {
        userDao = new UserDao(context);
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public User queryUserByName(String name)
    {
        return userDao.queryOneByColumn("name", name);
    }

    public void addNewUserService(Observer<String> resultObserver, User user){

        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                userDao.add(user);

                emitter.onNext(String.format("用户：%s， 添加成功", user.getName()));
                emitter.onComplete();
            }
        });

        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultObserver);
    }

    public void deleteUserService(Observer<String> resultObserver, User user){
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                userDao.delete(user);

                emitter.onNext(String.format("用户：%s， 删除成功", user.getName()));
                emitter.onComplete();
            }
        });

        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultObserver);
    }

    public void updateUserService(Observer<String> resultObserver, User user){
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                userDao.update(user);

                emitter.onNext(String.format("用户：%s， 信息更新成功", user.getName()));

                emitter.onComplete();
            }
        });

        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultObserver);
    }

    public void queryAllUserService(Observer<List<User>> resultObserver){
        Observable<List<User>> observable = Observable.create(new ObservableOnSubscribe<List<User>>() {
            @Override
            public void subscribe(ObservableEmitter<List<User>> emitter) throws Exception {

                List<User> userList = userDao.all();

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
