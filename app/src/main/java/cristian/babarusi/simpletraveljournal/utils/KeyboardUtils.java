package cristian.babarusi.simpletraveljournal.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public abstract class KeyboardUtils extends AppCompatActivity {

    public static void hideKeyboard(Activity activity) {
        View view = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        Screen.hideNavigationBar(activity);
    }

    public void showKeyboard(Activity activity, EditText editText) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }
}