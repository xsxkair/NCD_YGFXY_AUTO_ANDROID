package com.ncd.xsx.ncd_ygfxy.Tools;

public class Feng {
    int startx;

    int endx;

    int length;

    int lianxu_zero_num;

    int sum;

    boolean haszheng;

    public Feng() {
        reset();
    }

    public void reset()
    {
        startx = 0;
        endx = 0;
        this.length = 0;
        lianxu_zero_num = 0;
        haszheng = false;
    }

    @Override
    public String toString() {
        return "Feng{" +
                "startx=" + startx +
                ", endx=" + endx +
                ", length=" + length +
                ", sum=" + sum +
                ", haszheng=" + haszheng +
                '}';
    }
}
