package com.ncd.xsx.ncd_ygfxy.Tools;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.Log;

import com.google.zxing.ResultPoint;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ImgTool {

    /**
     * 图片灰度化处理
     *
     * @param picpath
     * */
    public static Bitmap bitmap2Gray(Context context, String picpath) {
        Bitmap bmSrc = BitmapFactory.decodeFile(picpath);
        // 得到图片的长和宽
        int width = bmSrc.getWidth();
        int height = bmSrc.getHeight();
        // 创建目标灰度图像
        Bitmap bmpGray = null;
        bmpGray = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        // 创建画布
        Canvas c = new Canvas(bmpGray);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmSrc, 0, 0, paint);
        saveBitmap(context, bmpGray, "grey");

        return bmpGray;
    }

    public static void gray2Binary(Context context, Bitmap graymap)
    {
        // 得到图形的宽度和长度
        int width = graymap.getWidth();
        int height = graymap.getHeight();
        // 创建二值化图像
        Bitmap binarymap = null;
        binarymap = graymap.copy(Bitmap.Config.ARGB_8888, true);
        // 依次循环，对图像的像素进行处理
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                // 得到当前像素的值
                int col = binarymap.getPixel(i, j);
                // 得到alpha通道的值
                int alpha = col & 0xFF000000;
                // 得到图像的像素RGB的值
                int red = (col & 0x00FF0000) >> 16;
                int green = (col & 0x0000FF00) >> 8;
                int blue = (col & 0x000000FF);
                // 用公式X = 0.3×R+0.59×G+0.11×B计算出X代替原来的RGB
                int gray = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
                // 对图像进行二值化处理
                if (gray <= 95) {
                    gray = 0;
                } else {
                    gray = 255;
                }
                // 新的ARGB
                int newColor = alpha | (gray << 16) | (gray << 8) | gray;
                // 设置新图像的当前像素值
                binarymap.setPixel(i, j, newColor);
            }
        }
        saveBitmap(context, binarymap, "gray2Binary");
    }

    public static String saveBitmap(Context context, Bitmap mBitmap, String funname) {
        String savePath;
        File filePic;

        try {
            filePic = new File("/mnt/sdcard/whnewcando/"+funname+".jpg");

            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) { // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return filePic.getAbsolutePath();
    }

    //相对于中心点，3*3领域中的点需要偏移的位置
    public static final int delta[][] = {{ -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 0 }, { 0, 1 }, { 1, -1 }, { 1, 0 }, {1, 1}
    };
    public static void myimage(Context context, String picPath)
    {
        Bitmap bitmapSrc = BitmapFactory.decodeFile(picPath);

        int width = bitmapSrc.getWidth();
        int height = bitmapSrc.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        int[] imgPixels = new int[width * height];
        bitmapSrc.getPixels(imgPixels, 0, width, 0, 0, width, height);
        int[] oldpixels = new int[9];

        //1. 中值滤波，没有考虑边缘
        for (int i = 0; i < height-2; i++)
        {
            for (int j = 0; j < width-2; j++)
            {
                //1.1 提取领域值
                for (int k = 0; k < 9; ++k)
                {
                    oldpixels[k] = imgPixels[((k/3)+i)*width+j+k%3];
                }
                //1.2 排序
                Arrays.sort(oldpixels);
                //1.3 获取该中心点的值
                imgPixels[(i+1)*width+j] = oldpixels[4];
            }
        }

        //用curArr合成新的图片curPic
        newBitmap.setPixels(imgPixels, 0, width, 0, 0, width, height);
        saveBitmap(context, newBitmap, "aaa");
    }
/*
    public static void imgline(Context context, String picPath)
    {
        Bitmap bitmapSrc = BitmapFactory.decodeFile(picPath);
        int width = bitmapSrc.getWidth();
        int height = bitmapSrc.getHeight();


        //SETP.1 解析二维码
        MyQRCodeDecodeTool.decode(picPath);
        ResultPoint[] resultPoints = MyQRCodeDecodeTool.getResultPoint();
        int startX = 0;
        int startY = 65535;
        int endY = 0;

        for(ResultPoint resultPoint : resultPoints)
        {
            if(startX < resultPoint.getX())
                startX = (int) resultPoint.getX();

            if(startY > resultPoint.getY())
                startY = (int) resultPoint.getY();

            if(endY < resultPoint.getY())
                endY = (int) resultPoint.getY();
        }

        //起始扫描X，向右偏移30像素
        startX += 30;
        startY = (startY+endY)/2-20;
        endY = startY+40;

        Log.d("xsx", "startX:"+startX+"   startY:"+startY+"   endY:"+endY);

        //SETP.2 获取纵向像素颜色值和的曲线数据
        StringBuffer serialPointBuffer = new StringBuffer();
        Map<Integer, Integer> points = new HashMap<>();
        List<Integer> originPoint = new ArrayList<>();

        for (int i = startY; i < endY; i++)
        {

            for (int j = startX; j < width; j++)
            {
                // 得到当前像素的值
                int col = bitmapSrc.getPixel(j, i);
                // 得到alpha通道的值
                //int alpha = col & 0xFF000000;
                // 得到图像的像素RGB的值
                //int red = (col & 0x00FF0000) >> 16;
                int green = (col & 0x0000FF00) >> 8;
                int blue = (col & 0x000000FF);
                // 用公式X = 0.3×R+0.59×G+0.11×B计算出X代替原来的RGB
                int gray = (int) ( green + blue)/2;

                Integer temp = points.get(j);

                if(temp == null)
                    temp = 0;

                gray += temp;
                points.put(j, gray);
            }
        }

        //图像纵向和平均值
        serialPointBuffer.setLength(0);
        int num = endY - startY;
        for (int j = startX; j < width; j++)
        {
            Integer temp = points.get(j);
            temp /= num;
            originPoint.add(temp);
            serialPointBuffer.append(temp);
            serialPointBuffer.append(',');
        }
        serialPointBuffer.append("\r\n");
        writeTxt(serialPointBuffer.toString());


        //setp.3 滤波
        num = originPoint.size();
        Integer sum = 0;
        List<Integer> lvboPointList = new ArrayList<>();
        for (int j = 2; j < num; j++)
        {
            Integer value1 = originPoint.get(j-2);
            Integer value2 = originPoint.get(j-1);
            Integer value3 = originPoint.get(j);
            sum = (value1 +value2 +value3)/3;
            lvboPointList.add(sum);
        }

       //STEP.4 斜率
        List<Integer> xielvList = new ArrayList<>();
        num = lvboPointList.size();
        for (int j = 1; j < num; j++)
        {
            xielvList.add(lvboPointList.get(j) - lvboPointList.get(j-1));
        }

        //STEP.5 找峰
        num = xielvList.size();
        List<Feng> fengList = new ArrayList<>();
        Feng tempFeng = null;
        for(int j=0; j<num; j++)
        {
            int value = xielvList.get(j);
            if(value != 0)
            {
                if(tempFeng == null)
                {
                    tempFeng = new Feng();
                    tempFeng.startx = j;
                }
                tempFeng.sum += value;
                tempFeng.lianxu_zero_num = 0;
                tempFeng.endx = j;
                tempFeng.length += 1;
                if(value > 0)
                    tempFeng.haszheng = true;
            }
            else if(tempFeng != null)
            {
                tempFeng.length += 1;
                tempFeng.lianxu_zero_num += 1;

                if(tempFeng.lianxu_zero_num >= 2)
                {
                    tempFeng.length -= 2;

                    if(tempFeng.length >= 3)
                    {
                        fengList.add(tempFeng);
                        Log.d("feng", " "+tempFeng.toString());
                    }
                    tempFeng = null;
                }
            }
        }

        Log.d("过滤", "-------");
        num = fengList.size();
        Feng tempFeng1 = null;
        Feng tempFeng2 = null;
        List<Feng> combineFengList = new ArrayList<>();
        for(int j=1; j<=num; )
        {
            try {
                tempFeng1 = fengList.get(j-1);
            }catch (Exception e){
                tempFeng1 = null;
            }
           // Log.d("feng1", " "+tempFeng1.toString());
            try {
                tempFeng2 = fengList.get(j);
            }catch (Exception e){
                tempFeng2 = null;
            }

            //Log.d("feng2", " "+tempFeng2.toString());
            if(tempFeng1 != null && tempFeng2 != null)
            {
                if((tempFeng2.startx - tempFeng1.endx) <= 5)
                {
                    tempFeng1.endx = tempFeng2.endx;
                    tempFeng1.length = (tempFeng2.endx - tempFeng1.startx);
                    tempFeng1.sum += tempFeng2.sum;

                    if(tempFeng1.haszheng || tempFeng2.haszheng)
                        tempFeng1.haszheng = true;
                    else
                        fengList.remove(tempFeng1);

                    fengList.remove(tempFeng2);

                    continue;
                }
                else if(!tempFeng1.haszheng)
                {
                    fengList.remove(tempFeng1);
                    continue;
                }
            }
            else if(tempFeng1 != null)
            {
                if(!tempFeng1.haszheng)
                {
                    fengList.remove(tempFeng1);
                    continue;
                }
            }

            j++;
        }

        Log.d("合并", "-------");
        for (Feng feng : fengList) {
            Log.d("feng", " "+feng.toString());
        }
    }
*/

   //1,计算斜率
    //2，负值写0
    //3，连续正值，只保留其中最大的一个，其他写0
    //4，合并一段距离内的正值，取较大的一个
    public static void imgline(Context context, String picPath)
    {
        Bitmap bitmapSrc = BitmapFactory.decodeFile(picPath);
        int width = bitmapSrc.getWidth();
        int height = bitmapSrc.getHeight();


        //SETP.1 解析二维码
        MyQRCodeDecodeTool.decode(picPath);
        ResultPoint[] resultPoints = MyQRCodeDecodeTool.getResultPoint();
        int startX = 0;
        int startY = 65535;
        int endY = 0;
        if(resultPoints == null)
            return;
        for(ResultPoint resultPoint : resultPoints)
        {
            if(startX < resultPoint.getX())
                startX = (int) resultPoint.getX();

            if(startY > resultPoint.getY())
                startY = (int) resultPoint.getY();

            if(endY < resultPoint.getY())
                endY = (int) resultPoint.getY();
        }

        //起始扫描X，向右偏移30像素
        startX += 30;
        startY += 20;
        endY -= 20;

        Log.d("xsx", "startX:"+startX+"   startY:"+startY+"   endY:"+endY);

        //SETP.2 获取纵向像素颜色值和的曲线数据
        StringBuffer serialPointBuffer = new StringBuffer();
        Map<Integer, Integer> points = new HashMap<>();
        List<Integer> originPoint = new ArrayList<>();

        for (int i = startY; i < endY; i++)
        {

            for (int j = startX; j < width; j++)
            {
                // 得到当前像素的值
                int col = bitmapSrc.getPixel(j, i);
                // 得到alpha通道的值
                //int alpha = col & 0xFF000000;
                // 得到图像的像素RGB的值
                //int red = (col & 0x00FF0000) >> 16;
                int green = (col & 0x0000FF00) >> 8;
                int blue = (col & 0x000000FF);
                // 用公式X = 0.3×R+0.59×G+0.11×B计算出X代替原来的RGB
                int gray = (int) ( green + blue)/2;

                Integer temp = points.get(j);

                if(temp == null)
                    temp = 0;

                gray += temp;
                points.put(j, gray);
            }
        }

        //图像纵向和平均值
        serialPointBuffer.setLength(0);
        int num = endY - startY;
        for (int j = startX; j < width; j++)
        {
            Integer temp = points.get(j);
            temp /= num;
            originPoint.add(temp);
            serialPointBuffer.append(temp);
            serialPointBuffer.append(',');
        }
        serialPointBuffer.append("\r\n");
        writeTxt(serialPointBuffer.toString());


        //setp.3 滤波
        num = originPoint.size();
        Integer sum = 0;
        List<Integer> lvboPointList = new ArrayList<>();
        serialPointBuffer.setLength(0);
        for (int j = 4; j < num; j++)
        {
            Integer value0 = originPoint.get(j-4);
            Integer value1 = originPoint.get(j-3);
            Integer value2 = originPoint.get(j-2);
            Integer value3 = originPoint.get(j-1);
            Integer value4 = originPoint.get(j);
            sum = (value0+value1 +value2 +value3+value4)/5;
            serialPointBuffer.append(sum);
            serialPointBuffer.append(',');
            lvboPointList.add(sum);
        }
        serialPointBuffer.append("\r\n");
        writeTxt(serialPointBuffer.toString());

        //STEP.4 斜率
        List<Integer> xielvList = new ArrayList<>();
        num = lvboPointList.size();
        serialPointBuffer.setLength(0);
        for (int j = 1; j < num; j++)
        {
            int xielv = lvboPointList.get(j) - lvboPointList.get(j-1);
            if(xielv < 0)
                xielv = 0;
            xielvList.add(xielv);
            serialPointBuffer.append(xielv);
            serialPointBuffer.append(',');
        }
        serialPointBuffer.append("\r\n");
        writeTxt(serialPointBuffer.toString());

        //连续正值，保留和

        num = xielvList.size();
        List<Point> pointList = new ArrayList<>();
        Integer lastPoint = -1;
        for (int j = 0; j < num; j++)
        {
            int xielv2 = xielvList.get(j);
            if(xielv2 > 0)
            {
                if(lastPoint < 0)
                {
                    lastPoint = j;
                    continue;
                }
                else if(j - lastPoint <= 10)
                {
                    xielv2 += xielvList.get(lastPoint);
                    //清楚之前的数据
                    xielvList.set(lastPoint, 0);
                    xielvList.set(j, 0);
                    //更新新数据
                    lastPoint = (j+lastPoint)/2;
                    xielvList.set(lastPoint, xielv2);
                }
                else
                {
                    //过滤太小的孤点
                    xielv2 = xielvList.get(lastPoint);
                    if(xielv2 <= 2)
                        xielvList.set(lastPoint, 0);
                    else {
                        Point point = min(lvboPointList, lastPoint-15, lastPoint+15);
                        if(!pointList.contains(point))
                            pointList.add(point);
                    }
                    lastPoint = j;
                }
            }
        }

        serialPointBuffer.setLength(0);
        Map<Integer, Integer> xielvMap = new TreeMap<>();
        for (int j = 0; j < num; j++)
        {
            int xielv2 = xielvList.get(j);
            serialPointBuffer.append(xielv2);
            serialPointBuffer.append(',');

            if(xielv2 > 0)
                xielvMap.put(j, xielv2);
        }
        serialPointBuffer.append("\r\n");
        writeTxt(serialPointBuffer.toString());

        num = pointList.size();
        for (int j = 0; j < num; j++)
        {
            Log.d("xsx", pointList.get(j).toString());
        }

        double tc = pointList.get(1).y;
        tc /= pointList.get(2).y;

        Log.d("T/C", String.format("%.4f", tc));
        //距离过滤
       /* lastPoint = -1;
        for (int j = 0; j < num; j++)
        {
            int xielv2 = xielvList.get(j);
            if(xielv2 > 0)
            {
                if(lastPoint < 0)
                {
                    lastPoint = j;
                    continue;
                }
                else if(j - lastPoint > 55 || j - lastPoint < 30)
                {
                    xielv2 += xielvList.get(lastPoint);
                    //清楚之前的数据
                    xielvList.set(lastPoint, 0);
                    xielvList.set(j, 0);
                    //更新新数据
                    lastPoint = (j+lastPoint)/2;
                    xielvList.set(lastPoint, xielv2);
                }
                else
                {
                    //过滤太小的孤点
                    xielv2 = xielvList.get(lastPoint);
                    if(xielv2 <= 2)
                        xielvList.set(lastPoint, 0);



                    lastPoint = j;
                }
            }
        }
        serialPointBuffer.append("\r\n");
        writeTxt(serialPointBuffer.toString());*/
    }


    private static void writeTxt(String string)
    {
        File filePic;

        try {
            filePic = new File("/mnt/sdcard/whnewcando/serial.txt");

            if(!filePic.exists())
                filePic.createNewFile();

            RandomAccessFile raf = new RandomAccessFile(filePic, "rwd");
            raf.seek(filePic.length());
            raf.write(string.getBytes());
            raf.close();
        } catch (IOException e) { // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static Point min(List<Integer> datas, int start, int end){
        int size = datas.size();

        if(start < 0)
            start = 0;
        if(end > size)
            end = size;

        Point point = new Point();
        point.y = 60000;
        int value = 0;
        for(int i=start; i<end; i++)
        {
            value = datas.get(i);
            if(point.y > value)
            {
                point.y = value;
                point.x = i;
            }
        }

        return point;
    }
}
