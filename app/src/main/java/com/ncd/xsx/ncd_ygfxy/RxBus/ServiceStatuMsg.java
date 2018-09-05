package com.ncd.xsx.ncd_ygfxy.RxBus;

public class ServiceStatuMsg {

    public static final int DEVICE_SERVICE_LIVE = 1;
    public static final int DEVICE_SERVICE_DEAD = 2;

    public static final int GPRS_SERVICE_LIVE = 3;
    public static final int GPRS_SERVICE_DEAD = 4;

    private int statu;

    public ServiceStatuMsg(int statu) {
        this.statu = statu;
    }

    public int getStatu() {
        return statu;
    }

    public void setStatu(int statu) {
        this.statu = statu;
    }
}
