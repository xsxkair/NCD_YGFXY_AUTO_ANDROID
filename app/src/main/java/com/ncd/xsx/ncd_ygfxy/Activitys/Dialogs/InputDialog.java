package com.ncd.xsx.ncd_ygfxy.Activitys.Dialogs;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ncd.xsx.ncd_ygfxy.Activitys.Listeners.DialogCancelListener;
import com.ncd.xsx.ncd_ygfxy.R;

public class InputDialog extends DialogFragment {

    private TextView dialogTitleTextView;
    private TextView inputDialogItemTextView;
    private TextView inputDialogItemEditView;
    private Button dialogSubmitButtoon;
    private Button dialogCancelButtoon;

    private Bundle args = null;

    private InputDialogSubmitListener inputDialogSubmitListener = null;
    private DialogCancelListener inputDialogCancelListener = null;


    public interface InputDialogSubmitListener {
        void onSubmit(String msg, int userValue);
    }

    public void setOnInputDialogSubmit(InputDialogSubmitListener listener){
        inputDialogSubmitListener = listener;
    }

    public void setOnInputDialogCancel(DialogCancelListener listener){
        inputDialogCancelListener = listener;
    }

    public static InputDialog newInstance() {
        InputDialog f = new InputDialog();

        // Supply num input as an argument.
        f.args = new Bundle();

        f.setArguments(f.args);

        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_input_layout, container);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialogTitleTextView = (TextView) view.findViewById(R.id.inputDialogTitleTextView);

        inputDialogItemTextView = (TextView) view.findViewById(R.id.inputDialogInputItemTextView);

        inputDialogItemEditView = (TextView) view.findViewById(R.id.inputDialogInputItemEditView);
        if(getArguments().getBoolean(DialogDefine.DIALOG_ARGS_INPUT_PASSWORD_KEY_STRING))
            inputDialogItemEditView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        else
            inputDialogItemEditView.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

        dialogSubmitButtoon = (Button) view.findViewById(R.id.inputDialogSubmitButton);
        dialogSubmitButtoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (inputDialogSubmitListener != null)
                    inputDialogSubmitListener.onSubmit(inputDialogItemEditView.getText().toString(), args.getInt(DialogDefine.DIALOG_ARGS_USER_VALUE_KEY_STRING));
            }
        });

        dialogCancelButtoon = (Button) view.findViewById(R.id.inputDialogCancelButton);
        dialogCancelButtoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (inputDialogCancelListener != null)
                    inputDialogCancelListener.onCancel();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        dialogTitleTextView.setText(getArguments().getString(DialogDefine.DIALOG_ARGS_TITLE_KEY_STRING));
        inputDialogItemTextView.setText(getArguments().getString(DialogDefine.DIALOG_ARGS_INPUT_LABEL_KEY_STRING));
        inputDialogItemEditView.setText("");
    }

    public void showInputDialog(FragmentManager manager, String tag, String title, String inputItemString, boolean isPassword) {

        args.putString(DialogDefine.DIALOG_ARGS_TITLE_KEY_STRING, title);
        args.putString(DialogDefine.DIALOG_ARGS_INPUT_LABEL_KEY_STRING, inputItemString);
        args.putBoolean(DialogDefine.DIALOG_ARGS_INPUT_PASSWORD_KEY_STRING, isPassword);

        show(manager, tag);
    }

    public void showInputDialog(FragmentManager manager, String tag, String title, String inputItemString, boolean isPassword, int userValue) {

        args.putString(DialogDefine.DIALOG_ARGS_TITLE_KEY_STRING, title);
        args.putString(DialogDefine.DIALOG_ARGS_INPUT_LABEL_KEY_STRING, inputItemString);
        args.putBoolean(DialogDefine.DIALOG_ARGS_INPUT_PASSWORD_KEY_STRING, isPassword);
        args.putInt(DialogDefine.DIALOG_ARGS_USER_VALUE_KEY_STRING, userValue);

        show(manager, tag);
    }
}
