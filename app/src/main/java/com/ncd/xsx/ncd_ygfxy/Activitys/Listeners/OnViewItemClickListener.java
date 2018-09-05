package com.ncd.xsx.ncd_ygfxy.Activitys.Listeners;

import android.view.View;

public interface OnViewItemClickListener<T> {

    void onItemClick(View view, int position, T t);
}
