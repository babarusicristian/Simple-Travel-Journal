package cristian.babarusi.simpletraveljournal.utils;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class Snack extends AppCompatActivity {
    public static void bar(View view, String message) {
        if (!TextUtils.isEmpty(message)) {
            Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(Color.parseColor("#7f0000"));
            TextView tv =  snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.parseColor("#FFFFFF"));
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            } else {
                tv.setGravity(Gravity.CENTER_HORIZONTAL);
            }
            
            snackbar.show();
        }
    }
}
