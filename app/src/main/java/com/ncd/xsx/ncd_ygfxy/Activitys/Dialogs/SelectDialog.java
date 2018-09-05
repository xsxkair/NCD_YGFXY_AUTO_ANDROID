package com.ncd.xsx.ncd_ygfxy.Activitys.Dialogs;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ncd.xsx.ncd_ygfxy.Activitys.Listeners.DialogCancelListener;
import com.ncd.xsx.ncd_ygfxy.Activitys.Listeners.DialogSubmittListener;
import com.ncd.xsx.ncd_ygfxy.R;

import java.util.ArrayList;

public class SelectDialog extends DialogFragment {

    private TextView dialog_title_textview;
    private TextView dialog_content_textview;
    private Button dialog_submit_button_1;
    private View dialog_splite_line2;
    private Button dialog_submit_button_2;
    private View dialog_splite_line3;
    private Button dialog_submit_button_3;

    private Bundle args = null;

    private DialogSubmittListener<Integer> dialogSubmittListener = null;

    public void setDialogSubmittListener(DialogSubmittListener<Integer> listener){
        dialogSubmittListener = listener;
    }

    public static SelectDialog newInstance(Context context) {
        SelectDialog f = new SelectDialog();

        // Supply num input as an argument.
        f.args = new Bundle();

        f.setArguments(f.args);

        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_select_layout, container);

        //隐藏title
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置dialog的 进出 动画
        getDialog().getWindow().setWindowAnimations(R.style.dialog_anim);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog_title_textview = (TextView) view.findViewById(R.id.dialog_title_textview);
        dialog_content_textview = (TextView) view.findViewById(R.id.dialog_content_textview);
        dialog_submit_button_1 = (Button) view.findViewById(R.id.dialog_submit_button_1);
        dialog_splite_line2 = (View) view.findViewById(R.id.dialog_splite_line2);
        dialog_submit_button_2 = (Button) view.findViewById(R.id.dialog_submit_button_2);
        dialog_splite_line3 = (View) view.findViewById(R.id.dialog_splite_line3);
        dialog_submit_button_3 = (Button) view.findViewById(R.id.dialog_submit_button_3);

        dialog_submit_button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (dialogSubmittListener != null)
                    dialogSubmittListener.onValued(DialogDefine.DIALOG_SUBMMIT_COM_VALUE_1, args.getInt(DialogDefine.DIALOG_ARGS_USER_VALUE_KEY_STRING, DialogDefine.DIALOG_INT_NONE));
            }
        });

        dialog_submit_button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(dialogSubmittListener != null)
                    dialogSubmittListener.onValued(DialogDefine.DIALOG_SUBMMIT_COM_VALUE_2, args.getInt(DialogDefine.DIALOG_ARGS_USER_VALUE_KEY_STRING, DialogDefine.DIALOG_INT_NONE));
            }
        });

        dialog_submit_button_3.setOnClickListener(new View.OnClickListener() {
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

        dialog_content_textview.setText(args.getString(DialogDefine.DIALOG_ARGS_CONFIRM_CONTENT_KEY_STRING));

        String tempstring =  args.getString(DialogDefine.DIALOG_ARGS_TITLE_KEY_STRING);
        if(tempstring != null)
        {
            dialog_title_textview.setText(tempstring);
            dialog_title_textview.setVisibility(View.VISIBLE);
        }
        else
            dialog_title_textview.setVisibility(View.GONE);

        tempstring =  args.getString(DialogDefine.DIALOG_ARGS_BUTTON1_KEY_STRING);
        if(tempstring != null)
        {
            dialog_submit_button_1.setText(tempstring);
            dialog_submit_button_1.setVisibility(View.VISIBLE);
        }
        else
            dialog_submit_button_1.setVisibility(View.GONE);

        tempstring =  args.getString(DialogDefine.DIALOG_ARGS_BUTTON2_KEY_STRING);
        if(tempstring != null)
        {
            dialog_submit_button_2.setText(tempstring);
            dialog_submit_button_2.setVisibility(View.VISIBLE);
            dialog_splite_line2.setVisibility(View.VISIBLE);
        }
        else
        {
            dialog_submit_button_2.setVisibility(View.GONE);
            dialog_splite_line2.setVisibility(View.GONE);
        }

        tempstring =  args.getString(DialogDefine.DIALOG_ARGS_BUTTON3_KEY_STRING);
        if(tempstring != null)
        {
            dialog_submit_button_3.setText(tempstring);
            dialog_submit_button_3.setVisibility(View.VISIBLE);
            dialog_splite_line3.setVisibility(View.VISIBLE);
        }
        else
        {
            dialog_submit_button_3.setVisibility(View.GONE);
            dialog_splite_line3.setVisibility(View.GONE);
        }
    }

    public void showDialog(FragmentManager manager, String tag, String content, String button_1_text, String button_2_text, String button_3_text) {

        args.putString(DialogDefine.DIALOG_ARGS_CONFIRM_CONTENT_KEY_STRING, content);
        args.putString(DialogDefine.DIALOG_ARGS_BUTTON1_KEY_STRING, button_1_text);
        args.putString(DialogDefine.DIALOG_ARGS_BUTTON2_KEY_STRING, button_2_text);
        args.putString(DialogDefine.DIALOG_ARGS_BUTTON3_KEY_STRING, button_3_text);

        show(manager, tag);
    }

    public void showDialog(FragmentManager manager, String tag, String content, String button_1_text, String button_2_text, String button_3_text, int userValue) {

        args.putString(DialogDefine.DIALOG_ARGS_CONFIRM_CONTENT_KEY_STRING, content);
        args.putString(DialogDefine.DIALOG_ARGS_BUTTON1_KEY_STRING, button_1_text);
        args.putString(DialogDefine.DIALOG_ARGS_BUTTON2_KEY_STRING, button_2_text);
        args.putString(DialogDefine.DIALOG_ARGS_BUTTON3_KEY_STRING, button_3_text);
        args.putInt(DialogDefine.DIALOG_ARGS_USER_VALUE_KEY_STRING, userValue);

        show(manager, tag);
    }
}
