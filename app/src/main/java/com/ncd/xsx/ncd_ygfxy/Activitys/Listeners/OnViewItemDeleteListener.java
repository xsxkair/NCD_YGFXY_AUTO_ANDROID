package com.ncd.xsx.ncd_ygfxy.Activitys.Listeners;

import android.view.View;

public interface OnViewItemDeleteListener<T> {

    void onItemDeleted(View view, int position, T t);
}
