package com.ncd.xsx.ncd_ygfxy.Services;

public abstract class MyThread extends Thread{
    private boolean running = true;

    public MyThread() {
        this.running = true;
    }

    public void stopThread(){
        this.running = false;
    }

    public boolean threadRunning()
    {
        return this.running;
    }
}
