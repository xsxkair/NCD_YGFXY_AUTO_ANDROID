package com.ncd.xsx.ncd_ygfxy.Activitys.Listeners;

import android.view.View;

public interface OnViewItemLongClickListener<T> {

    void onItemLongClick(View view, int position, T t);
}
