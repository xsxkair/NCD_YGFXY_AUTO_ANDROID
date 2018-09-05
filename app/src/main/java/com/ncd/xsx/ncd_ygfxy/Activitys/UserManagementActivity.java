package com.ncd.xsx.ncd_ygfxy.Activitys;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ncd.xsx.ncd_ygfxy.Activitys.Adapter.UserAdapter;
import com.ncd.xsx.ncd_ygfxy.Activitys.Adapter.UserDecoration;
import com.ncd.xsx.ncd_ygfxy.Activitys.Dialogs.AddUserDialog;
import com.ncd.xsx.ncd_ygfxy.Activitys.Listeners.DialogSubmittListener;
import com.ncd.xsx.ncd_ygfxy.Activitys.Listeners.OnViewItemClickListener;
import com.ncd.xsx.ncd_ygfxy.Activitys.Listeners.OnViewItemDeleteListener;
import com.ncd.xsx.ncd_ygfxy.Databases.Services.UserService;
import com.ncd.xsx.ncd_ygfxy.Defines.PublicStringDefine;
import com.ncd.xsx.ncd_ygfxy.Logger.LoggerUnits;
import com.ncd.xsx.ncd_ygfxy.R;
import com.ncd.xsx.ncd_ygfxy.Databases.Entity.User;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class UserManagementActivity extends MyActivity {

    ImageButton add_user_imagebutton;
    ImageButton fresh_user_imagebutton;
    RecyclerView userRecyclerView;

    private Observer<String> userActionObserver;

    private UserAdapter userAdapter;
    private Observer<List<User>> userListObserver;

    private AddUserDialog addUserDialog;

    private User selectUser = null;
    private User newUser = new User();
    private User currentUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        super.MyActivityCommonInit();

        add_user_imagebutton = (ImageButton) findViewById(R.id.add_user_imagebutton);
        fresh_user_imagebutton = (ImageButton) findViewById(R.id.fresh_user_imagebutton);
        userRecyclerView = (RecyclerView) findViewById(R.id.userRecyclerView);

        addUserDialog = AddUserDialog.newInstance();
        addUserDialog.setOnDialogValuedSubmit(new DialogSubmittListener<User>() {
            @Override
            public void onValued(User user, int userValue) {
                User queryUser = UserService.getInstance().queryUserByName(user.getName());

                if(queryUser != null)
                {
                    user.setId(queryUser.getId());

                    UserService.getInstance().updateUserService(userActionObserver, user);
                }
                else
                    UserService.getInstance().addNewUserService(userActionObserver, user);
            }
        });

        add_user_imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUserDialog.showMyDialog(getFragmentManager(), UserManagementActivity.class.getSimpleName());
            }
        });

        userActionObserver = new Observer<String>() {
            private Disposable disposable = null;
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(String string) {
                Toast.makeText(UserManagementActivity.this, string, Toast.LENGTH_SHORT).show();
                UserService.getInstance().queryAllUserService(userListObserver);
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(UserManagementActivity.this, PublicStringDefine.ACTION_FAIL_STRING, Toast.LENGTH_SHORT).show();
                LoggerUnits.error("操作人操作失败", e);
                disposable.dispose();
            }

            @Override
            public void onComplete() {
                disposable.dispose();
            }
        };

        userAdapter = new UserAdapter(null, this, R.layout.layout_user_list_item, true);
        userAdapter.setOnItemClickListener(new OnViewItemClickListener<User>() {
            @Override
            public void onItemClick(View view, int position, User user) {

            }
        });
        userAdapter.setOnItemDeleteListener(new OnViewItemDeleteListener<User>() {
            @Override
            public void onItemDeleted(View view, int position, User user) {
                UserService.getInstance().deleteUserService(userActionObserver, user);
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
                Toast.makeText(UserManagementActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                disposable.dispose();
            }

            @Override
            public void onComplete() {
                disposable.dispose();
            }
        };

        fresh_user_imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserService.getInstance().queryAllUserService(userListObserver);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        UserService.getInstance().queryAllUserService(userListObserver);
    }
}
