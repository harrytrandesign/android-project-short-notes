package us.wetpaws.essentialnotes;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * Created by Wetpaws Studio AppLab on 4/25/16.
 */
public class customEditText extends EditText {

    Context context;
    private KeyImeChange keyImeChangeListener;

    public customEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public void setKeyImeChangeListener(KeyImeChange listener){
        keyImeChangeListener = listener;
    }

    public interface KeyImeChange {
        public boolean onKeyIme(int keyCode, KeyEvent event);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {

        if (keyImeChangeListener != null) {

            return keyImeChangeListener.onKeyIme(keyCode, event);

        }

        return false;

        // This previous code logged the down button press into the log file.
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
//
//            Log.i("notes", "The back key works if this message shows");
//
//        }
//        return true;
    }

}
