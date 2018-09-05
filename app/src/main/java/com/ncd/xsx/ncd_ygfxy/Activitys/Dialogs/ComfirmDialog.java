package com.ncd.xsx.ncd_ygfxy.Activitys.Dialogs;

import android.app.DialogFragment;
import android.app.FragmentManager;
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

public class ComfirmDialog extends DialogFragment {

    private TextView dialogTitleTextView;
    private TextView dialogContentTextView;
    private Button dialogButtoon_1;
    private View dialog_splite_line2;
    private Button dialogButtoon_2;
    private View dialog_splite_line3;
    private Button dialogButtoon_3;

    private Bundle args = null;
    private DialogSubmittListener<Integer> dialogSubmittListener = null;

    public void setDialogSubmittListener(DialogSubmittListener<Integer> dialogSubmittListener) {
        this.dialogSubmittListener = dialogSubmittListener;
    }

    public static ComfirmDialog newInstance() {
        ComfirmDialog f = new ComfirmDialog();

        // Supply num input as an argument.
        f.args = new Bundle();

        f.setArguments(f.args);

        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_confirm_layout, container);

        //隐藏title
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置dialog的 进出 动画
        getDialog().getWindow().setWindowAnimations(R.style.dialog_anim);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialogTitleTextView = (TextView) view.findViewById(R.id.inputDialogTitleTextView);
        dialog_splite_line2 = (View) view.findViewById(R.id.dialog_splite_line2);
        dialog_splite_line3 = (View) view.findViewById(R.id.dialog_splite_line3);
        dialogContentTextView = (TextView) view.findViewById(R.id.confirmDialogContentTextView);

        dialogButtoon_1 = (Button) view.findViewById(R.id.comfirm_dialog_button_1);
        dialogButtoon_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (dialogSubmittListener != null)
                    dialogSubmittListener.onValued(DialogDefine.DIALOG_SUBMMIT_COM_VALUE_1, args.getInt(DialogDefine.DIALOG_ARGS_USER_VALUE_KEY_STRING, DialogDefine.DIALOG_INT_NONE));
            }
        });

        dialogButtoon_2 = (Button) view.findViewById(R.id.comfirm_dialog_button_2);
        dialogButtoon_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(dialogSubmittListener != null)
                    dialogSubmittListener.onValued(DialogDefine.DIALOG_SUBMMIT_COM_VALUE_2, args.getInt(DialogDefine.DIALOG_ARGS_USER_VALUE_KEY_STRING, DialogDefine.DIALOG_INT_NONE));
            }
        });

        dialogButtoon_3 = (Button) view.findViewById(R.id.comfirm_dialog_button_3);
        dialogButtoon_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (dialogSubmittListener != null)
                    dialogSubmittListener.onValued(DialogDefine.DIALOG_SUBMMIT_COM_VALUE_3, args.getInt(DialogDefine.DIALOG_ARGS_USER_VALUE_KEY_STRING, DialogDefine.DIALOG_INT_NONE));
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        dialogContentTextView.setText(args.getString(DialogDefine.DIALOG_ARGS_CONFIRM_CONTENT_KEY_STRING));

        String tempstring =  args.getString(DialogDefine.DIALOG_ARGS_TITLE_KEY_STRING);
        if(tempstring != null)
        {
            dialogTitleTextView.setText(tempstring);
            dialogTitleTextView.setVisibility(View.VISIBLE);
        }
        else
            dialogTitleTextView.setVisibility(View.GONE);

        tempstring =  args.getString(DialogDefine.DIALOG_ARGS_BUTTON1_KEY_STRING);
        if(tempstring != null)
        {
            dialogButtoon_1.setText(tempstring);
            dialogButtoon_1.setVisibility(View.VISIBLE);
        }
        else
            dialogButtoon_1.setVisibility(View.GONE);

        tempstring =  args.getString(DialogDefine.DIALOG_ARGS_BUTTON2_KEY_STRING);
        if(tempstring != null)
        {
            dialogButtoon_2.setText(tempstring);
            dialogButtoon_2.setVisibility(View.VISIBLE);
            dialog_splite_line2.setVisibility(View.VISIBLE);
        }
        else
        {
            dialogButtoon_2.setVisibility(View.GONE);
            dialog_splite_line2.setVisibility(View.GONE);
        }

        tempstring =  args.getString(DialogDefine.DIALOG_ARGS_BUTTON3_KEY_STRING);
        if(tempstring != null)
        {
            dialogButtoon_3.setText(tempstring);
            dialogButtoon_3.setVisibility(View.VISIBLE);
            dialog_splite_line3.setVisibility(View.VISIBLE);
        }
        else
        {
            dialogButtoon_3.setVisibility(View.GONE);
            dialog_splite_line3.setVisibility(View.GONE);
        }
    }

    public void showComfirmDialog(FragmentManager manager, String tag, String title, String content, String button_1_text)
    {
        args.putString(DialogDefine.DIALOG_ARGS_TITLE_KEY_STRING, title);
        args.putString(DialogDefine.DIALOG_ARGS_CONFIRM_CONTENT_KEY_STRING, content);
        args.putString(DialogDefine.DIALOG_ARGS_BUTTON1_KEY_STRING, button_1_text);
        args.putString(DialogDefine.DIALOG_ARGS_BUTTON2_KEY_STRING, null);
        args.putString(DialogDefine.DIALOG_ARGS_BUTTON3_KEY_STRING, null);

        show(manager, tag);
    }

    public void showComfirmDialog(FragmentManager manager, String tag, String content, String button_1_text)
    {
        args.putString(DialogDefine.DIALOG_ARGS_TITLE_KEY_STRING, null);
        args.putString(DialogDefine.DIALOG_ARGS_CONFIRM_CONTENT_KEY_STRING, content);
        args.putString(DialogDefine.DIALOG_ARGS_BUTTON1_KEY_STRING, button_1_text);
        args.putString(DialogDefine.DIALOG_ARGS_BUTTON2_KEY_STRING, null);
        args.putString(DialogDefine.DIALOG_ARGS_BUTTON3_KEY_STRING, null);

        show(manager, tag);
    }

    public void showComfirmDialog(FragmentManager manager, String tag, String title, String content, String button_1_text, String button_2_text, String button_3_text) {

        args.putString(DialogDefine.DIALOG_ARGS_TITLE_KEY_STRING, title);
        args.putString(DialogDefine.DIALOG_ARGS_CONFIRM_CONTENT_KEY_STRING, content);
        args.putString(DialogDefine.DIALOG_ARGS_BUTTON1_KEY_STRING, button_1_text);
        args.putString(DialogDefine.DIALOG_ARGS_BUTTON2_KEY_STRING, button_2_text);
        args.putString(DialogDefine.DIALOG_ARGS_BUTTON3_KEY_STRING, button_3_text);

        show(manager, tag);
    }
}
