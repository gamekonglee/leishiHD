package bc.otlhd.com.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import bc.otlhd.com.R;
import bc.otlhd.com.ui.activity.IssueApplication;

/**
 * @author Jun
 * @time 2016/8/19  10:37
 * @desc ${TODD}
 */
public class UIUtils {

    /**
     * 得到上下文
     * @return
     */
    public static Context getContext(){
        return IssueApplication.getcontext();
    }


    public static String getDeviceId(){
        return ((TelephonyManager) getContext().getSystemService(getContext().TELEPHONY_SERVICE))
                .getDeviceId();
    }

    public static int getScreenWidth(Activity activity){
        if(activity==null||activity.isFinishing()||activity.isDestroyed()){
            return UIUtils.dip2PX(480);
        }
        WindowManager manager = activity.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        return width;
    }
    public static int getScreenHeight(Activity activity){
        WindowManager manager = activity.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        return height;
    }
    /**
     *mac
     * @param context
     * @return String
     */
    public static String getLocalMac(Context context){
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String mac=info.getMacAddress();
        if(mac.equals("02:00:00:00:00:00")){
            return getInterfaceLocalmac();
        }else {
            return mac;
        }
    }



    public static String  getInterfaceLocalmac(){
        String mac="";
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iF = interfaces.nextElement();
                byte[] addr = iF.getHardwareAddress();
                if (addr == null || addr.length == 0) {
                    continue;
                }
                StringBuilder buf = new StringBuilder();
                for (byte b : addr) {
                    buf.append(String.format("%02X:", b));
                }
                if (buf.length() > 0) {
                    buf.deleteCharAt(buf.length() - 1);
                }
                mac = buf.toString();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return mac;
    }


    /**
     * 得到Resources对象
     * @return
     */
    public static Resources getResources(){
        return getContext().getResources();
    }

    /**
     * 得到包名
     * @return
     */
    public static String getpackageName(){
        return  getContext().getPackageName();
    }

    /**
     * 得到配置的String信息
     * @param resId
     * @return
     */
    public static String getString(int resId){
        return getResources().getString(resId);
    }

    /**
     * 得到配置的String信息
     * @param resId
     * @return
     */
    public static String getString(int resId,Object ...formatAgs){
        return getResources().getString(resId,formatAgs);
    }

    /**
     * 得到配置String数组
     * @param resId
     * @return
     */
    public static String[] getStringArr(int resId){
        return getResources().getStringArray(resId);
    }
    public static int dip2PX(int dip) {
        //拿到设备密度
        float density=getResources().getDisplayMetrics().density;

        int px= (int) (dip*density+.5f);
        return px;
    }

    public static Dialog showRightInDialog(Activity activity,int res,int layoutwidth) {
        Dialog dialog=new Dialog(activity, R.style.customDialog);
        dialog.setContentView(res);//这行一定要写在前面
        dialog.setCancelable(true);//点击外部不可dismiss
        dialog.setCanceledOnTouchOutside(true);

        Window window = dialog.getWindow();
        window.setGravity(Gravity.RIGHT);
        window.setBackgroundDrawable(getResources().getDrawable(android.R.color.white));
        window.setWindowAnimations(R.style.dialogRightInStyle);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = layoutwidth;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.y=0;
        params.x=0;
//        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;//就是这个属性导致window后所有的东西都成暗淡
        window.setAttributes(params);
        dialog.show();
        return dialog;
    }

    public static Bitmap drawableToBitmap(int width, int height, Drawable drawable) {

        int w = width;
        int h =height;
//        System.out.println("Drawable转Bitmap");
        Bitmap.Config config = Bitmap.Config.ARGB_8888;

        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        //注意，下面三行代码要用到，否则在View或者SurfaceView里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);

        return bitmap;
    }

    public static Bitmap view2Bitmap(View view) {
//        Bitmap b = Bitmap.createBitmap(UIUtils.dip2PX(200),UIUtils.dip2PX(320), Bitmap.Config.RGB_565);
//        Canvas c = new Canvas(b);
//        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
//        // Draw background
//        Drawable bgDrawable = v.getBackground();
//        if (bgDrawable != null)
//            bgDrawable.draw(c);
//        else
//            c.drawColor(Color.WHITE);
//        // Draw view to canvas
//        v.draw(c);
//        return b;
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());view.buildDrawingCache();Bitmap bitmap=view.getDrawingCache();return bitmap;
    }

}
