package com.livetv.normal.view.custom_views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

import com.livetv.normal.listeners.EditTextDoneListener;

public class CustomEditText extends EditText {

    private EditTextDoneListener editTextDoneListener;

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setEditTextDoneListener(EditTextDoneListener etdl) {
        editTextDoneListener = etdl;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        ;//Log.d("liveTV", "onKeyPreIme");
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK &&
                event.getAction() == KeyEvent.FLAG_EDITOR_ACTION) {
            ;//Log.d("liveTV", "DONE HERE");
                editTextDoneListener.onEditTextDone();
//                editTextDoneListener.onImeBack(this, this.getText().toString());
        }
        return super.dispatchKeyEvent(event);
    }
}
