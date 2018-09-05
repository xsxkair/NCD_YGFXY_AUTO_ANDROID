package com.ncd.xsx.ncd_ygfxy.Logger;


public class LogException extends Exception{
    public LogException() {
        super();
    }

    public LogException(String detailMessage) {
        super(detailMessage);
    }

    public LogException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public LogException(Throwable throwable) {
        super(throwable);
    }
}
