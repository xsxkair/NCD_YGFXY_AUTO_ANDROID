package com.ncd.xsx.ncd_ygfxy.Activitys.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.ncd.xsx.ncd_ygfxy.Activitys.Listeners.OnViewItemClickListener;
import com.ncd.xsx.ncd_ygfxy.Activitys.Listeners.OnViewItemDeleteListener;
import com.ncd.xsx.ncd_ygfxy.R;
import com.ncd.xsx.ncd_ygfxy.Databases.Entity.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {

    private List<User> userList;
    private int userSize;
    private LayoutInflater inflater;
    private int layoutResId;
    private UserHolder selectUserHolder = null;
    private int selectPosition = -1;
    private OnViewItemClickListener<User> myOnItemClickListener;
    private OnViewItemDeleteListener<User> myOnItemDeleteListener;
    private boolean swipeEnable = true;

    public UserAdapter(List<User> userList, Context context, int layoutResId, boolean swipeEnable) {
        this.updateUserData(userList);
        inflater = LayoutInflater.from(context);
        this.layoutResId = layoutResId;
        this.swipeEnable = swipeEnable;
    }

    public void updateUserData(List<User> userList){

        this.userList = userList;

        if(this.userList != null)
            this.userSize = this.userList.size();
        else
            this.userSize = 0;

        setSelectPosition(-1);
        selectUserHolder = null;

        this.notifyDataSetChanged();
    }

    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(this.layoutResId, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(UserHolder holder, int position) {

        ((SwipeMenuLayout)holder.itemView).setSwipeEnable(swipeEnable);

        holder.userNameTextView.setText(userList.get(position).getName());

        holder.userNameTextView.setOnClickListener(v -> {
            if(myOnItemClickListener != null)
                myOnItemClickListener.onItemClick(v, position, userList.get(position));

            if(selectUserHolder != null)
                selectUserHolder.userHolderUnSelectedEvent();

            holder.userHolderSelectedEvent();
            selectUserHolder = holder;
            setSelectPosition(position);
        });

        holder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myOnItemDeleteListener != null)
                    myOnItemDeleteListener.onItemDeleted(null, position, userList.get(position));
            }
        });
        if(position == getSelectPosition())
            holder.userHolderSelectedEvent();
        else
            holder.userHolderUnSelectedEvent();
    }

    @Override
    public int getItemCount() {
        return this.userSize;
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    public void setOnItemClickListener(OnViewItemClickListener<User> onItemClickListener){
        this.myOnItemClickListener = onItemClickListener;
    }

    public void setOnItemDeleteListener(OnViewItemDeleteListener<User> onItemDeleteListener)
    {
        this.myOnItemDeleteListener = onItemDeleteListener;
    }

    class UserHolder extends RecyclerView.ViewHolder{

        private View itemView;
        private TextView userNameTextView;
        private Button delete_button;

        public UserHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            userNameTextView = (TextView) itemView.findViewById(R.id.user_name_textview);
            delete_button = (Button) itemView.findViewById(R.id.user_delete_button);
        }

        public void userHolderSelectedEvent(){
            itemView.setBackgroundResource(R.color.button_pressed);
        }

        public void userHolderUnSelectedEvent(){
            itemView.setBackgroundResource(R.color.white);
        }
    }
}
