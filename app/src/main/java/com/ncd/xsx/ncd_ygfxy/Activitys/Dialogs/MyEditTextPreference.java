package com.ncd.xsx.ncd_ygfxy.Activitys.Dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.ncd.xsx.ncd_ygfxy.R;

public class MyEditTextPreference extends DialogPreference implements OnClickListener {

    EditText editText1;
    EditText editText2;

    private String mText;

    public MyEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public MyEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MyEditTextPreference(Context context, AttributeSet attrs) {
        this(context, attrs, com.android.internal.R.attr.editTextPreferenceStyle);
    }

    public MyEditTextPreference(Context context) {
        this(context, null);
    }

    /**
     * Saves the text to the {@link SharedPreferences}.
     *
     * @param text The text to save
     */
    public void setText(String text) {
        final boolean wasBlocking = shouldDisableDependents();

        mText = text;

        persistString(text);

        final boolean isBlocking = shouldDisableDependents();
        if (isBlocking != wasBlocking) {
            notifyDependencyChange(isBlocking);
        }
    }

    /**
     * Gets the text from the {@link SharedPreferences}.
     *
     * @return The current preference value.
     */
    public String getText() {
        return mText;
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        setText(restoreValue ? getPersistedString(mText) : (String) defaultValue);
        Log.d("xsx", "init value");
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        editText1 = (EditText)view.findViewById(R.id.editText1);
        editText2 = (EditText)view.findViewById(R.id.editText2);
        editText2.setText(getText());
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        super.onClick(dialog, which);

        if(DialogInterface.BUTTON_POSITIVE == which)
        {
            String[] value = new String[2];
            value[0] = editText1.getText().toString();
            value[1] = editText2.getText().toString();

            if( callChangeListener(value))
            {
                setText(value[1]);
                Log.d("xsx", "return true");
            }
            else
                Log.d("xsx", "return false");
        }
    }

}
