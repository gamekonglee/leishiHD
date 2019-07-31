package bc.otlhd.com.controller;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import bc.otlhd.com.R;
import bc.otlhd.com.bean.AppVersion;
import bc.otlhd.com.bean.Goods;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.cons.NetWorkConst;
import bc.otlhd.com.data.CartDao;
import bc.otlhd.com.listener.INetworkCallBack;
import bc.otlhd.com.net.OkHttpUtils;
import bc.otlhd.com.ui.activity.IssueApplication;
import bc.otlhd.com.ui.activity.MainActivity;
import bc.otlhd.com.ui.activity.UploadImagAtivity;
import bc.otlhd.com.ui.view.ShowDialog;
import bc.otlhd.com.utils.MyShare;
import bc.otlhd.com.utils.net.NetWorkUtils;
import bc.otlhd.com.utils.upload.UpAppUtils;
import bocang.json.JSONObject;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;
import bocang.utils.CommonUtil;
import bocang.utils.DataCleanUtil;
import bocang.utils.IntentUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author: Jun
 * @date : 2017/3/17 10:24
 * @description :
 */
public class MainController extends BaseController implements INetworkCallBack {
    private TextView unMessageReadTv;
    private MainActivity mView;
    private String mAppVersion;
    public String mTotalCacheSize;
    private ShowDialog mDialog;
    private String oldDataVersion;
    private String newDataVersion;

    public MainController(MainActivity v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {
        sendShoppingCart();
        sendVersion();
        getTotalCacheSize();
        File file = new File(UpAppUtils.UP_SAVEPATH + File.separator + "data");
//        if (file.exists()) {
//            OkHttpUtils.hashkNewwork() = false;
//
//        } else {
//            OkHttpUtils.hashkNewwork() = true;
//            //            MyToast.show(mView,"请下载数据包!");
//            //            IntentUtil.startActivity(mView, UploadImagAtivity.class,false);
//        }
//        Log.v("520it", "11" + OkHttpUtils.hashkNewwork() + "");
        sendPicList();
//        upgradeData();

    }

    public void sendPicList() {
        if(!OkHttpUtils.hashkNewwork()){
            return;
        }
        if(mDialog!=null&&mDialog.isShowing()){
            return;
        }
        OkHttpUtils.getDataVersion(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONObject jsonObject=new JSONObject(response.body().string());
                newDataVersion = jsonObject.getString(Constance.vertion);
                oldDataVersion = MyShare.get(mView).getString(Constance.vertion);
                if(AppUtils.isEmpty(oldDataVersion)){
                    showDowmDataDialog();
                    return;
                }
                boolean isNeedUpdate = CommonUtil.isNeedUpdate(oldDataVersion, newDataVersion);
                if (isNeedUpdate ) {
                    showDowmDataDialog();
                }
            }
        });
//        mNetWork.sendPicList(mView, new HttpListener() {
//            @Override
//            public void onSuccessListener(int what, com.alibaba.fastjson.JSONObject ans) {
//                int newCount = ans.getInteger(Constance.count);
//                int currenCount = MyShare.get(mView).getInt(Constance.count);//下载类型
//                if (currenCount < newCount) {
//                    mDialog = new ShowDialog();
//                    mDialog.show(mView, "提示", "有新的数据下载，是否下载?", new ShowDialog.OnBottomClickListener() {
//                        @Override
//                        public void positive() {
//                            IntentUtil.startActivity(mView, UploadImagAtivity.class, false);
//                            mView.mIsRefresh = true;
//                        }
//
//                        @Override
//                        public void negtive() {
//                        }
//                    });
//                }
//
//            }
//
//            @Override
//            public void onFailureListener(int what, com.alibaba.fastjson.JSONObject ans) {
//
//            }
//        });
    }

    private void showDowmDataDialog() {
        IssueApplication.pic_version=newDataVersion;
        mView.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDialog = new ShowDialog();
                mDialog.show(mView, "提示", "有新的数据下载，是否下载?", new ShowDialog.OnBottomClickListener() {
                    @Override
                    public void positive() {
                        mView.countDownTimer.cancel();
                        IssueApplication.noAd=true;
                        IntentUtil.startActivity(mView, UploadImagAtivity.class, false);
                        mView.mIsRefresh = true;
                    }

                    @Override
                    public void negtive() {
                    }
                });
            }
        });

    }

    /**
     * 升级数据
     */
    private void upgradeData() {
        long currentTime = System.currentTimeMillis();
        long oldTime = MyShare.get(mView).getLong("time");
        oldTime +=  1 * 60 * 1000;
        if (currentTime > oldTime) {
            MyShare.get(mView).putLong("time", currentTime);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final boolean isResult = GetDataInputStream(NetWorkConst.DOWN_SQL_URL);

                    mView.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isResult) {

//                                OkHttpUtils.hashkNewwork()=false;

                            }else{

                            }
                        }
                    });
                }
            }).start();
        }

    }
    String mDataPath = UpAppUtils.UP_SAVEPATH + File.separator + "data1";
    String mDataPath02 = UpAppUtils.UP_SAVEPATH + File.separator + "data";
    /**
     * 获取网络图片
     *
     * @return Bitmap 返回位图
     */
    public boolean GetDataInputStream(String dataUrl) {
        Boolean isFinished = false; //用户完成下载
        URL url;
        HttpURLConnection connection = null;
        try {
            url = new URL(dataUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(6000); //超时设置
            connection.setDoInput(true);
            connection.setUseCaches(false); //设置不使用缓存
            InputStream inputStream = connection.getInputStream();
            final int length = connection.getContentLength();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                File file = null;
                file = new File(mDataPath);
                FileOutputStream fos = null;
                //文件夹不存在，则创建它
                if (!file.exists()) {
                    file.mkdir();
                }
                try {
                    fos = new FileOutputStream(file + "/data.sqlite");
                    Log.v("520it", file + "/data.sqlite");
                    byte buf[] = new byte[1024];

                    do {
                        int numRead = inputStream.read(buf);
                        if (numRead <= 0) {
                            isFinished = true;
                        } else {
                            fos.write(buf, 0, numRead);
                        }
                    }
                    while (!isFinished);// 点击取消或下载完成就停止下载.
                    fos.close();
                    inputStream.close();

                } catch (Exception e) {
                    return false;
                }
            }


        } catch (Exception e) {
            return false;
        }
        return true;
    }


    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {


        }
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /*
         * 将时间戳转换为时间
         */
    public String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public void sendShoppingCart() {
        CartDao dao = new CartDao(mView);

        int count =0;
        List<Goods> goodsList=dao.getAll();
        if(goodsList!=null&&goodsList.size()>0){
            for(int i=0;i<goodsList.size();i++){
                count+=goodsList.get(i).getBuyCount();
            }
        }
        IssueApplication.mCartCount = count;
        setIsShowCartCount();
    }

    private void initView() {
        unMessageReadTv = (TextView) mView.findViewById(R.id.unMessageReadTv);
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    int REQUEST_EXTERNAL_STORAGE = 1;
    String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    /**
     * 获取版本号
     */
    public void sendVersion() {
        if(!OkHttpUtils.hashkNewwork()){
            return;
        }
        if(mDialog!=null&&mDialog.isShowing()){
            return;
        }
        mNetWork.sendVersion(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mAppVersion = NetWorkUtils.doGet(NetWorkConst.VERSION_URL);
                if (AppUtils.isEmpty(mAppVersion))
                    return;
                String localVersion = CommonUtil.localVersionName(mView);
                if ("-1".equals(mAppVersion)) {

                } else {
                    boolean isNeedUpdate = CommonUtil.isNeedUpdate(localVersion, mAppVersion);
                    if (isNeedUpdate) {
                        mView.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mDialog = new ShowDialog();

                                mDialog.show(mView, "升级提示", "有最新的升级包，是否升级?", new ShowDialog.OnBottomClickListener() {
                                    @Override
                                    public void positive() {
                                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                                            int permission = ActivityCompat.checkSelfPermission(mView, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                                            if (permission != PackageManager.PERMISSION_GRANTED) {
                                                ActivityCompat.requestPermissions(
                                                        mView,
                                                        PERMISSIONS_STORAGE,
                                                        REQUEST_EXTERNAL_STORAGE
                                                );
                                            }else {
                                                showUpdateDialog();
                                            }
                                        }else {
                                            showUpdateDialog();
                                        }


//                                        Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(NetWorkConst.DOWN_APK_URL));
//                                        mView.startActivity(intent);
                                    }

                                    @Override
                                    public void negtive() {
                                        mView.finish();
                                    }
                                });
                            }
                        });


                    }
                }

            }
        }).start();

    }

    private void showUpdateDialog() {
        AppVersion appVersion = new AppVersion();
        appVersion.setVersion(mAppVersion);
        appVersion.setName(NetWorkConst.UR_APP_NAME);
        appVersion.setDes(Constance.APP_DES);
        appVersion.setForcedUpdate("0");
        appVersion.setUrl(NetWorkConst.DOWN_APK_URL);
        if (appVersion != null) {
            new UpAppUtils(mView, appVersion);
        }
    }

    /**
     * 清理缓存
     */
    public void clearCache() {
        new AlertDialog.Builder(mView).setTitle("清除缓存?").setMessage("确认清除您所有的缓存？")
                .setPositiveButton("清除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mTotalCacheSize.equals("0K")){
                            AppDialog.messageBox("没有缓存可以清除!");
                            return;
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                DataCleanUtil.clearAllCache(mView);
                                mView.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AppDialog.messageBox("清除缓存成功!");

                                        getTotalCacheSize();
                                    }
                                });
                            }
                        }).start();
                    }
                })
                .setNegativeButton("取消", null).show();
    }
    /**
     * 查看缓存大小
     */
    private void getTotalCacheSize(){
        try {
            mTotalCacheSize = DataCleanUtil.getTotalCacheSize(mView);
            mView.tv_cache.setText(""+mTotalCacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取版本号名称
     *
     * @param context 上下文
     * @return
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }
    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        switch (requestCode) {
            case NetWorkConst.GETCART:
                if (ans.getJSONArray(Constance.goods_groups).length() > 0) {
                    IssueApplication.mCartCount = ans.getJSONArray(Constance.goods_groups).getJSONObject(0).getJSONArray(Constance.goods).length();
                    setIsShowCartCount();
                }
                break;
            case NetWorkConst.VERSION_URL:
                mAppVersion = ans.getString(Constance.JSON);
                break;
        }
    }

    private UpdateApkBroadcastReceiver broadcastReceiver;

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode==REQUEST_EXTERNAL_STORAGE){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                showUpdateDialog();
            }
        }
    }

    private class UpdateApkBroadcastReceiver extends BroadcastReceiver {

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void onReceive(Context context, final Intent intent) {
            // 判断是否下载完成的广播
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                // 注销广播
                mView.unregisterReceiver(broadcastReceiver);
                broadcastReceiver = null;

                // 获取下载的文件id
                long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                DownloadManager down = (DownloadManager) mView.getSystemService(mView.download);
                final Uri uri = down.getUriForDownloadedFile(downId);
            }
        }
    }


    public void setIsShowCartCount() {

        unMessageReadTv.setVisibility(IssueApplication.mCartCount == 0 ? View.GONE : View.VISIBLE);
        unMessageReadTv.setText(IssueApplication.mCartCount + "");
    }


    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {

    }

    //    /**
    //     * 拷贝数据库
    //     * @param name
    //     */
    //    private void copyDb(final String name) {
    //        new Thread() {
    //            public void run() {
    //                try {
    //                    File file = new File(mView.getFilesDir(), name);
    //                    if (file.exists() & file.length() > 0) {
    //                        mView.runOnUiThread(new Runnable() {
    //                            @Override
    //                            public void run() {
    //                                MyToast.show(mView, "数据库已经存在");
    //                            }
    //                        });
    //
    //                    } else {
    //                        InputStream is = mView.getAssets().open(name);
    //                        FileOutputStream fos = new FileOutputStream(file);
    //                        byte[] buffer = new byte[1024];
    //                        int len = -1;
    //                        while ((len = is.read(buffer)) != -1) {
    //                            fos.write(buffer, 0, len);
    //                        }
    //                        is.close();
    //                        fos.close();
    //                    }
    //                } catch (IOException e) {
    //                    e.printStackTrace();
    //                }
    //            }
    //        }.start();
    //    }
}
