package cristian.babarusi.simpletraveljournal.utils;

import android.util.Log;
import cristian.babarusi.simpletraveljournal.BuildConfig;

public class Logging {

    public static void show (Object obj, String message)
    {
        if(BuildConfig.DEBUG)
        {
            Log.e(obj.getClass().getName(), message);
        }
    }
}
