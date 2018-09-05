package com.ncd.xsx.ncd_ygfxy.Http;

import com.ncd.xsx.ncd_ygfxy.Http.HttpServices.UserService;
import com.ncd.xsx.ncd_ygfxy.Databases.Entity.User;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpMethods {

    private static final int DEFAULT_TIMEOUT = 5000;

    private Retrofit retrofit;
    private UserService userService;

    private HttpMethods(){
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);

        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(HttpUrlDefine.BASE_URL)
                .build();

        //user service
        userService = retrofit.create(UserService.class);
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder{
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    //获取单例
    public static HttpMethods getInstance(){
        return SingletonHolder.INSTANCE;
    }

    //user service
   /* public void login(Observer<User> userObserver, String account, String password){
        userService.loginService2(account, password)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(userObserver);
    }*/
    public void login(Observer<String> userObserver, String account, String password){
        userService.loginService2(account, password)
                .map(new HttpResultFunction<User, String>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(userObserver);
    }
}
