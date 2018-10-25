package com.ncd.xsx.ncd_ygfxy.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.ncd.xsx.ncd_ygfxy.Activitys.Adapter.UserAdapter;
import com.ncd.xsx.ncd_ygfxy.Activitys.Adapter.UserDecoration;
import com.ncd.xsx.ncd_ygfxy.Activitys.Listeners.OnViewItemClickListener;
import com.ncd.xsx.ncd_ygfxy.Databases.Services.UserService;
import com.ncd.xsx.ncd_ygfxy.R;
import com.ncd.xsx.ncd_ygfxy.Databases.Entity.User;
import com.ncd.xsx.ncd_ygfxy.Services.TestService.TestFunction;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class SelectUserActivity extends MyActivity {

    ImageView editUserImageView;
    RecyclerView userRecyclerView;

    private UserAdapter userAdapter;
    private Observer<List<User>> userListObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);

        super.MyActivityCommonInit();

        editUserImageView = (ImageView) findViewById(R.id.editUserImageView);
        userRecyclerView = (RecyclerView) findViewById(R.id.userRecyclerView);

        userAdapter = new UserAdapter(null, this, R.layout.layout_user_list_item, false);
        userAdapter.setOnItemClickListener(new OnViewItemClickListener<User>() {
            @Override
            public void onItemClick(View view, int position, User user) {
                Log.d("xsx", "click: "+user.getName());
                TestFunction.getInstance().setCurrentTester(user);
                startActivity(new Intent(SelectUserActivity.this, SampleInputActivity.class));
            }
        });
        userRecyclerView.setAdapter(userAdapter);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userRecyclerView.addItemDecoration(new UserDecoration(2));
        userListObserver = new Observer<List<User>>() {
            private Disposable disposable = null;
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(List<User> users) {
                userAdapter.updateUserData(users);
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(SelectUserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                disposable.dispose();
            }

            @Override
            public void onComplete() {
                disposable.dispose();
            }
        };

        /*freshUserListImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBaseMethods.getInstance().queryAllUser(userListObserver);
            }
        });*/

        editUserImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectUserActivity.this, UserManagementActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        UserService.getInstance().queryAllUserService(userListObserver);
    }
}
