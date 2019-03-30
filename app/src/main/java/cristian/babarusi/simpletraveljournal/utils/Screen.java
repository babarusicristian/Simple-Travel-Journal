package cristian.babarusi.simpletraveljournal.utils;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Screen extends AppCompatActivity {
    public static void hideNavigationBar(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
