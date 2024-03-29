package bc.otlhd.com.utils;

import android.util.Log;

/**
 * Created by gamekonglee on 2018/9/17.
 */

public class LogUtils {
    public static void logE(String tag, String content) {
        int p = 2048;
        long length = content.length();
        if (length < p || length == p)
            Log.e(tag, content);
        else {
            while (content.length() > p) {
                String logContent = content.substring(0, p);
                content = content.replace(logContent, "");
                Log.e(tag, logContent);
            }
            Log.e(tag, content);
        }
    }
}
