package com.ncd.xsx.ncd_ygfxy.Activitys.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.ncd.xsx.ncd_ygfxy.Activitys.Listeners.DialogSubmittListener;
import com.ncd.xsx.ncd_ygfxy.Databases.Entity.Patient;
import com.ncd.xsx.ncd_ygfxy.Defines.PublicStringDefine;
import com.ncd.xsx.ncd_ygfxy.R;

import java.util.List;

public class PatientSpinnerAdapter extends BaseAdapter implements Filterable {

    private MyFilter myFilter;
    private Context context ;
    private List<Patient> list;
    private int dataSize = 0;

    private DialogSubmittListener<Patient> dialogSubmittListener;

    public PatientSpinnerAdapter(Context context, List<Patient> list){
        this.context=context;
        updateData(list);
    }

    public void updateData(List<Patient> dataList){

        this.list = dataList;

        if(this.list != null)
            this.dataSize = this.list.size();
        else
            this.dataSize = 0;

        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataSize;
    }

    @Override
    public Patient getItem(int position) {
        if(list == null)
            return null;
        Log.d(PublicStringDefine.OWNER_TAG, "getItem:"+position);
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_spiner_view, parent, false);
        TextView tvgetView=(TextView) view.findViewById(R.id.value_textview);
        tvgetView.setText(getItem(position).getName());
        /*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialogSubmittListener != null)
                    dialogSubmittListener.onValued(getItem(position), position);
            }
        });*/
        return view;
    }

    public void setDialogSubmittListener(DialogSubmittListener<Patient> dialogSubmittListener) {
        this.dialogSubmittListener = dialogSubmittListener;
    }

    @Override
    public Filter getFilter() {
        if(myFilter == null)
            myFilter = new MyFilter();
        return myFilter;
    }

    class MyFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            results.values = list;
            results.count = dataSize;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // 重新将与适配器相关联的List重赋值一下
            list = (List<Patient>) results.values;

            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }


    }
}
