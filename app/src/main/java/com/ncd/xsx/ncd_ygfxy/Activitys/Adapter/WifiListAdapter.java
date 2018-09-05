package com.ncd.xsx.ncd_ygfxy.Activitys.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ncd.xsx.ncd_ygfxy.Activitys.Listeners.OnViewItemClickListener;
import com.ncd.xsx.ncd_ygfxy.Databases.Entity.MyWifiInfo;

import java.util.List;

public class WifiListAdapter extends RecyclerView.Adapter<WifiViewHolder> {

    private List<MyWifiInfo> dataList;
    private int dataSize;
    private LayoutInflater inflater;
    private int layoutResId;
    private WifiViewHolder selectItemHolder = null;
    private int selectItemPosition = -1;
    private OnViewItemClickListener<MyWifiInfo> myOnViewItemClickListener;

    public WifiListAdapter(List<MyWifiInfo> dataList, Context context, int layoutResId) {
        this.updateData(dataList);
        inflater = LayoutInflater.from(context);
        this.layoutResId = layoutResId;
    }

    public void updateData(List<MyWifiInfo> dataList){

        this.dataList = dataList;

        if(this.dataList != null)
            this.dataSize = this.dataList.size();
        else
            this.dataSize = 0;

        setSelectItemPosition(-1);
        selectItemHolder = null;

        this.notifyDataSetChanged();
    }

    @Override
    public WifiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(this.layoutResId, parent, false);
        return new WifiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WifiViewHolder holder, int position) {
        holder.fillViewHolderContent(dataList.get(position));

        holder.getView().setOnClickListener(v -> {
            if(myOnViewItemClickListener != null)
                myOnViewItemClickListener.onItemClick(v, position, dataList.get(position));

            selectItemHolder = holder;
        });

    }

    @Override
    public int getItemCount() {
        return dataSize;
    }

    public int getSelectItemPosition() {
        return selectItemPosition;
    }

    public void setSelectItemPosition(int selectItemPosition) {
        this.selectItemPosition = selectItemPosition;
    }

    public void setOnItemClickListener(OnViewItemClickListener<MyWifiInfo> onItemClickListener){
        this.myOnViewItemClickListener = onItemClickListener;
    }
}
