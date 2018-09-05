package com.ncd.xsx.ncd_ygfxy.Activitys.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.ncd.xsx.ncd_ygfxy.Activitys.Listeners.OnViewItemClickListener;
import com.ncd.xsx.ncd_ygfxy.Activitys.Listeners.OnViewItemLongClickListener;
import com.ncd.xsx.ncd_ygfxy.Databases.Entity.TestData;

import java.util.List;

public class TestDataAdapter extends RecyclerView.Adapter<TestDataViewHolder> {

    private List<TestData> dataList;
    private int dataSize;
    private LayoutInflater inflater;
    private int layoutResId;
    private TestDataViewHolder selectItemHolder = null;
    private int selectItemPosition = -1;
    private OnViewItemClickListener<TestData> myOnItemClickListener;
    private OnViewItemLongClickListener<TestData> myOnItemLongClickListener;

    public TestDataAdapter(List<TestData> testDataList, Context context, int layoutResId) {
        this.updateTestData(testDataList);
        inflater = LayoutInflater.from(context);
        this.layoutResId = layoutResId;
    }

    public void updateTestData(List<TestData> userList){

        this.dataList = userList;

        if(this.dataList != null)
            this.dataSize = this.dataList.size();
        else
            this.dataSize = 0;

        setSelectItemPosition(-1);
        selectItemHolder = null;

        this.notifyDataSetChanged();
    }

    @Override
    public TestDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(this.layoutResId, parent, false);
        return new TestDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TestDataViewHolder holder, int position) {
        holder.fillViewHolderContent(dataList.get(position));

        holder.getItemView().setOnClickListener( v -> {
            if(myOnItemClickListener != null)
                myOnItemClickListener.onItemClick(v, position, dataList.get(position));

            if(selectItemHolder != null)
                selectItemHolder.userHolderUnSelectedEvent();

            holder.userHolderSelectedEvent();
            selectItemHolder = holder;
            setSelectItemPosition(position);
        });

        holder.getItemView().setOnLongClickListener( v->{
            if(myOnItemLongClickListener != null)
                myOnItemLongClickListener.onItemLongClick(v, position, dataList.get(position));

            if(selectItemHolder != null)
                selectItemHolder.userHolderUnSelectedEvent();

            holder.userHolderSelectedEvent();
            selectItemHolder = holder;
            setSelectItemPosition(position);

            return true;
        });

        if(position == getSelectItemPosition())
            holder.userHolderSelectedEvent();
        else
            holder.userHolderUnSelectedEvent();
    }

    @Override
    public int getItemCount() {
        return this.dataSize;
    }

    public int getSelectItemPosition() {
        return selectItemPosition;
    }

    public void setSelectItemPosition(int selectItemPosition) {
        this.selectItemPosition = selectItemPosition;
    }

    public void setOnItemClickListener(OnViewItemClickListener<TestData> onItemClickListener){
        this.myOnItemClickListener = onItemClickListener;
    }
    public void setOnItemLongClickListener(OnViewItemLongClickListener<TestData> onItemLongClickListener){
        this.myOnItemLongClickListener = onItemLongClickListener;
    }

}
