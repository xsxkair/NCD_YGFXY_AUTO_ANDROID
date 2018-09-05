package com.ncd.xsx.ncd_ygfxy.SerialDriver.GPRSSerial;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import io.reactivex.functions.Function;

public class GprsSerialResultFunction<T extends GprsSerialEntity, R> implements Function<T, R> {

    @Override
    public R apply(T result) throws Exception {

        String dataStr = null;

        if(result == null)
            throw new Exception("recv data null");

        Type type = new TypeToken<R>(){}.getType();
        Gson gson = new Gson();

        try{
            dataStr = new String(result.getData());
        }catch (Exception e){
            throw new Exception("recv data can not be changed to string");
        }

        try{
            return gson.fromJson(dataStr, type);
        }catch (Exception e){

            throw new Exception("recv data can not be changed to string");
        }

    }
}
