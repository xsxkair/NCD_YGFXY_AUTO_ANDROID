package com.ncd.xsx.ncd_ygfxy.Activitys.Dialogs;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.ncd.xsx.ncd_ygfxy.Activitys.Listeners.DialogCancelListener;
import com.ncd.xsx.ncd_ygfxy.Activitys.Listeners.DialogSubmittListener;
import com.ncd.xsx.ncd_ygfxy.Defines.PublicStringDefine;
import com.ncd.xsx.ncd_ygfxy.R;
import com.ncd.xsx.ncd_ygfxy.Databases.Entity.User;

public class AddUserDialog extends DialogFragment {

    private EditText userNameEditView;
    private EditText userIdEditView;
    private EditText userPhoneEditText;
    private EditText user_job_EditText;
    private EditText userDepEditText;
    private Button cancelButton;
    private Button submitButton;

    private Bundle args = null;

    private DialogSubmittListener<User> dialogSubmittListener = null;
    private DialogCancelListener dialogCancelListener = null;

    public void setOnDialogValuedSubmit(DialogSubmittListener<User> listener){
        dialogSubmittListener = listener;
    }

    public void setOnDialogCancel(DialogCancelListener listener){
        dialogCancelListener = listener;
    }

    public static AddUserDialog newInstance() {
        AddUserDialog f = new AddUserDialog();

        // Supply num input as an argument.
        f.args = new Bundle();

        f.setArguments(f.args);

        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_add_user, container);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);


        userNameEditView = (EditText) view.findViewById(R.id.userNameEditView);
        userIdEditView = (EditText) view.findViewById(R.id.userIdEditView);
        userPhoneEditText = (EditText) view.findViewById(R.id.userPhoneEditText);
        user_job_EditText = (EditText) view.findViewById(R.id.user_job_EditText);
        userDepEditText = (EditText) view.findViewById(R.id.userDepEditText);
        cancelButton = (Button) view.findViewById(R.id.cancelButton);
        submitButton = (Button) view.findViewById(R.id.submitButton);

        userNameEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0)
                    submitButton.setEnabled(true);
                else
                    submitButton.setEnabled(false);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dismiss();
                if (dialogSubmittListener != null)
                    dialogSubmittListener.onValued(makeUser(), DialogDefine.DIALOG_INT_NONE);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (dialogCancelListener != null)
                    dialogCancelListener.onCancel();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        userNameEditView.setText(PublicStringDefine.EMPTY_STRING);
        userIdEditView.setText(PublicStringDefine.EMPTY_STRING);
        userPhoneEditText.setText(PublicStringDefine.EMPTY_STRING);
        user_job_EditText.setText(PublicStringDefine.EMPTY_STRING);
        userDepEditText.setText(PublicStringDefine.EMPTY_STRING);

        getDialog().getWindow().setLayout(700, 600);//设置高宽
    }

    private User makeUser()
    {
        User user = new User();

        user.setName(userNameEditView.getText().toString());
        user.setUserid(userIdEditView.getText().toString());
        user.setDep(userDepEditText.getText().toString());
        user.setJob(user_job_EditText.getText().toString());
        user.setPhone(userPhoneEditText.getText().toString());

        return user;
    }

    public void showMyDialog(FragmentManager manager, String tag) {

        show(manager, tag);

    }

}
