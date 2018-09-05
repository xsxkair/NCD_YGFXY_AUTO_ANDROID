package com.ncd.xsx.ncd_ygfxy.Http;

import android.util.Log;

import io.reactivex.functions.Function;

public class HttpResultFunction<T, T1> implements Function<T, T1> {
    @Override
    public T1 apply(T httpResult) throws Exception {
        if(httpResult == null)
        {
            Log.d("xsx", "null data");
            throw new HttpApiException("登录失败");
        }

        return (T1)httpResult.toString();
    }
}
