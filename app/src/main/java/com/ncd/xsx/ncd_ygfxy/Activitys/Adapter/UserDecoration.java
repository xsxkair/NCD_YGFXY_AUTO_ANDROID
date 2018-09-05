package com.ncd.xsx.ncd_ygfxy.Activitys.Adapter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ncd.xsx.ncd_ygfxy.R;

public class UserDecoration extends RecyclerView.ItemDecoration {
    private final int verticalSpaceHeight;
    private Paint colorPaint;

    public UserDecoration(int verticalSpaceHeight) {
        this.verticalSpaceHeight = verticalSpaceHeight;

        colorPaint = new Paint();
        colorPaint.setColor(0xff959ead);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = verticalSpaceHeight;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < childCount; i++)
        {
            View view = parent.getChildAt(i);
            float top = view.getBottom();
            float bottom = view.getBottom() + verticalSpaceHeight;
            c.drawRect(left, top, right, bottom, colorPaint);
        }
    }
}
