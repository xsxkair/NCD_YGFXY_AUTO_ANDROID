package com.ncd.xsx.ncd_ygfxy.Tools;

public class Point {
    int x;
    int y;

    public Point(){
        x=0;
        y=0;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Point)
        {
            if(((Point)o).x == x)
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
