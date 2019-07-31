package bc.otlhd.com.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.pgyersdk.crash.PgyCrashManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.OkHttpNetworkExecutor;
import com.yanzhenjie.nohttp.cache.DBCacheStore;
import com.yanzhenjie.nohttp.cookie.DBCookieStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;

import bc.otlhd.com.utils.ImageLoadProxy;
import bc.otlhd.com.utils.LogUtils;
import bocang.json.JSONObject;
import bocang.view.BaseApplication;
import cn.jpush.android.api.JPushInterface;


/**
 * @author Jun
 * @time 2017/1/6  16:06
 * @desc 全局
 */
public class IssueApplication extends BaseApplication {
    public static final long SLEEP_TIME = 500;
    protected static Context mContext = null;
    public static boolean noAd=false;
    private static DisplayImageOptions options;
    private static ImageLoaderConfiguration config;
    private static DisplayImageOptions defaultOptions;
    public static String pic_version="";
    public static boolean DataLoadFinish=false;
    public static boolean PicLoadFinish=false;


    @Override
    public void onCreate() {
//        Thread.currentThread().setUncaughtExceptionHandler(new MyExceptionHander());
        super.onCreate();
        mContext = getApplicationContext();
        super.mInstance = this;
        initImageLoader();
        ImageLoadProxy.initImageLoader(mContext);
        noAd=false;
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush'

        initNoHttp();
        PgyCrashManager.register(this);
        /**
         * 注意: 即使您已经在AndroidManifest.xml中配置过appkey和channel值，也需要在App代码中调
         * 用初始化接口（如需要使用AndroidManifest.xml中配置好的appkey和channel值，
         * UMConfigure.init调用中appkey和channel参数请置为null）。
         */

        UMConfigure.init(this, "5cf72c0e0cafb2f4cd00122b", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
        // 支持在子进程中统计自定义事件
        UMConfigure.setProcessEvent(true);

    }

    private void initNoHttp() {

        NoHttp.initialize(this);
        NoHttp.initialize(this, new NoHttp.Config()
                // 设置全局连接超时时间，单位毫秒
                .setConnectTimeout(30 * 1000)
                        // 设置全局服务器响应超时时间，单位毫秒
                .setReadTimeout(30 * 1000)
                .setCacheStore(new DBCacheStore(this))//配置缓存，控制开关
                .setCookieStore(new DBCookieStore(this))//配置Cookie保存的位置，默认保存在数据库
                .setNetworkExecutor(new OkHttpNetworkExecutor())//配置网络层
        );
        Logger.setDebug(true);// 开启NoHttp的调试模式, 配置后可看到请求过程、日志和错误信息。
        Logger.setTag("NoHttpSample");// 设置NoHttp打印Log的tag。
    }

    public static boolean IsRoot() {
        LogUtils.logE("signid",IssueApplication.mSignId);
        if(IssueApplication.mSignId.equals("18:d2:76:0b:38:2e")){
            return true;
        }else if(IssueApplication.mSignId.equals("a36669764956181")){
            return true;
        }else if(IssueApplication.mSignId.equals("db9b39a9bbbb42dd")){
            return true;
        }
        return false;
    }
    public static boolean IsSlow() {
        return false;
    }

    private class MyExceptionHander implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            // Logger.i("MobileSafeApplication", "发生了异常,但是被哥捕获了..");
            //            LogUtils.d("MobileSafeApplication","发生了异常,但是被哥捕获了..");
            //并不能把异常消灭掉,只是在应用程序关掉前,来一个留遗嘱的事件
            //获取手机硬件信息
            try {
                Field[] fields = Build.class.getDeclaredFields();
                StringBuffer sb = new StringBuffer();
                for(Field field:fields){
                    String value = field.get(null).toString();
                    String name  = field.getName();
                    sb.append(name);
                    sb.append(":");
                    sb.append(value);
                    sb.append("\n");
                }
                File file=new File(getFilesDir(),"error.log");
                FileOutputStream out = new FileOutputStream(file);
                StringWriter wr = new StringWriter();
                PrintWriter err = new PrintWriter(wr);
                //获取错误信息
                ex.printStackTrace(err);
                String errorlog = wr.toString();
                sb.append(errorlog);
                out.write(sb.toString().getBytes());
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //杀死页面进程
            //            android.os.Process.killProcess(android.os.Process.myPid());
            restartApp();
        }
    }

    public void restartApp(){
        Intent intent = new Intent(mContext,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());  //结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
    }


    /**
     * 获得全局上下文
     *
     * @return
     */
    public static Context getcontext() {
        return mContext;
    }

    //初始化网络图片缓存库
    private void initImageLoader() {
        //网络图片例子,结合常用的图片缓存库UIL,你可以根据自己需求自己换其他网络图片库
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).defaultDisplayImageOptions(defaultOptions)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }


    public  static DisplayImageOptions getImageLoaderOption() {
        defaultOptions = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory(false).imageScaleType(ImageScaleType.EXACTLY)
                .cacheOnDisk(false).build();
        // default
// .memoryCache(new LruMemoryCache((int) (6 * 1024 * 1024)))
// Remove
        // .memoryCache(new LruMemoryCache((int) (6 * 1024 * 1024)))
// Remove
        //                    .memoryCache(new WeakMemoryCache())
// 设置内存图片的宽高
//                    .memoryCacheExtraOptions(453, 328)
// default = device screen dimensions
// 缓存到磁盘中的图片宽高
//                    .diskCacheExtraOptions(453, 328, null)
// .memoryCache(new LruMemoryCache((int) (6 * 1024 * 1024)))
//                    .memoryCache(new LruMemoryCache(5 * 1024 * 1024))
//                    .memoryCacheSize((int) (2 * 1024 * 1024))
// Remove
        config = new ImageLoaderConfiguration.Builder(
                getInstance())
                .threadPoolSize(4)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .denyCacheImageMultipleSizesInMemory()
//                    .memoryCache(new WeakMemoryCache())
                .imageDownloader(new BaseImageDownloader(getcontext()))
                // 设置内存图片的宽高
//                    .memoryCacheExtraOptions(453, 328)
// default = device screen dimensions
// 缓存到磁盘中的图片宽高
//                    .diskCacheExtraOptions(453, 328, null)
// .memoryCache(new LruMemoryCache((int) (6 * 1024 * 1024)))

//                    .memoryCache(new LruMemoryCache(5 * 1024 * 1024))
//                    .memoryCacheSize((int) (2 * 1024 * 1024))
//                .memoryCacheSizePercentage(25)
//                .diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(200)
//                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .defaultDisplayImageOptions(defaultOptions).writeDebugLogs() // Remove
                .build();
// Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);

//        if(options==null){
            // resource or
// drawable
// resource or
// drawable
// resource or
// drawable
// default
// default
// default
// default
// default
// default
// default
// default
            //                    .showImageOnLoading(R.mipmap.bg_default) // resource or
// drawable
//                    .showImageForEmptyUri(R.mipmap.bg_default) // resource or
// drawable
//                    .showImageOnFail(R.mipmap.bg_default) // resource or
// drawable
// default
// default
// default
// default
// default
// default
// default
            options = new DisplayImageOptions.Builder()
//                    .showImageOnLoading(R.mipmap.bg_default) // resource or
                    // drawable
//                    .showImageForEmptyUri(R.mipmap.bg_default) // resource or
                    // drawable
//                    .showImageOnFail(R.mipmap.bg_default) // resource or
                    .cacheInMemory(false)// drawable
                    .resetViewBeforeLoading(false) // default
                    .delayBeforeLoading(1000)
                    .cacheOnDisk(false) // default
                    .considerExifParams(false) // default
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                    .bitmapConfig(Bitmap.Config.RGB_565) // default
                    .displayer(new SimpleBitmapDisplayer()) // default
                    .handler(new Handler()) // default
                    .build();
//        }
        return options;
    }
    public static DisplayImageOptions getCacheImageloaderOpstion(){
//        if(defaultOptions==null) {
//            defaultOptions = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565)
//                    .cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY)
//                    .cacheOnDisk(true).build();
//        }
        options = new DisplayImageOptions.Builder()
//                    .showImageOnLoading(R.mipmap.bg_default) // resource or
                // drawable
//                    .showImageForEmptyUri(R.mipmap.bg_default) // resource or
                // drawable
//                    .showImageOnFail(R.mipmap.bg_default) // resource or
                .cacheInMemory(true)// drawable
                .resetViewBeforeLoading(true) // default
                .delayBeforeLoading(1000)
                .cacheOnDisk(true) // default
                .considerExifParams(true) // default
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                .bitmapConfig(Bitmap.Config.RGB_565) // default
                .displayer(new SimpleBitmapDisplayer()) // default
                .handler(new Handler()) // default
                .build();
        config = new ImageLoaderConfiguration.Builder(
                getInstance())
                .threadPoolSize(5)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
//                    .memoryCache(new WeakMemoryCache())
                .imageDownloader(new BaseImageDownloader(getcontext()))
                // 设置内存图片的宽高
//                    .memoryCacheExtraOptions(453, 328)
                // 缓存到磁盘中的图片宽高
//                    .diskCacheExtraOptions(453, 328, null)
                .memoryCache(new LruMemoryCache((int) (6 * 1024 * 1024)))
                .memoryCacheSize((int) (2 * 1024 * 1024))
                .memoryCacheSizePercentage(25)
                .diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(200)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .defaultDisplayImageOptions(options).writeDebugLogs() // Remove
                .build();
        ImageLoader.getInstance().init(config);

        return options;
    }
    /**
     * 返回桌面
     */
    public void toDesktop() {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
    }

    public static String mCId = "";

//    public static JSONObject mUserObject;

    public static String imagePath = "";

    public static File cameraPath;

    public static boolean isClassify = false;

    public static int mCartCount = 0;

    public static int mLightIndex = 0;//点出来的灯的序号

    public static com.alibaba.fastjson.JSONObject mUserInfo;

    public static boolean isUploadTip=false;


    public static boolean isNetData=true;

    public static String sharePath="";

    public static String shareRemark="";

    public static Boolean IsMainActibity=false;

    //标识码
    public static String mSignId="";

}
