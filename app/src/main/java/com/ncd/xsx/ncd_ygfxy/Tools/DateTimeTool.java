package com.ncd.xsx.ncd_ygfxy.Tools;

import android.app.AlarmManager;
import android.content.Context;
import android.util.Log;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeTool {

    private static SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");

    public static void setSystemDateTime(Context context, String dateTime)
    {
        Calendar c = Calendar.getInstance();

        try {
            Date date = sf.parse(dateTime);
            c.setTime(date);
            Log.d("xsx",  c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND));

            long when = c.getTimeInMillis();

            if(when / 1000 < Integer.MAX_VALUE)
            {
                ((AlarmManager)context.getSystemService(Context.ALARM_SERVICE)).setTime(when);
            }

        } catch (ParseException e) {
            Log.e("xsx", e.getMessage());
        }
    }
}
