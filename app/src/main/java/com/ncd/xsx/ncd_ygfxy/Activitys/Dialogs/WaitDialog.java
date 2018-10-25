package com.ncd.xsx.ncd_ygfxy.Activitys.Dialogs;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ncd.xsx.ncd_ygfxy.Activitys.Listeners.DialogSubmittListener;
import com.ncd.xsx.ncd_ygfxy.R;

public class WaitDialog extends DialogFragment {

    private TextView wait_info_textview;

    private Bundle args = null;

    public static WaitDialog newInstance(Context context) {
        WaitDialog f = new WaitDialog();

        // Supply num input as an argument.
        f.args = new Bundle();

        f.setArguments(f.args);

        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_wait, container);

        //隐藏title
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置dialog的 进出 动画
        getDialog().getWindow().setWindowAnimations(R.style.dialog_anim);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        getDialog().setCanceledOnTouchOutside(false);

        wait_info_textview = (TextView) view.findViewById(R.id.wait_info_textview);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        wait_info_textview.setText(args.getString(DialogDefine.DIALOG_ARGS_CONFIRM_CONTENT_KEY_STRING));
    }

    public void showDialog(FragmentManager manager, String tag, String content) {

        args.putString(DialogDefine.DIALOG_ARGS_CONFIRM_CONTENT_KEY_STRING, content);

        show(manager, tag);
    }
}
