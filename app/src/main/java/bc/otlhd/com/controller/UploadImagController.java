package bc.otlhd.com.controller;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.pgyersdk.crash.PgyCrashManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import bc.otlhd.com.R;
import bc.otlhd.com.bean.DownLoadImageBean;
import bc.otlhd.com.bean.UploadImage;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.cons.NetWorkConst;
import bc.otlhd.com.data.UploadImageDao;
import bc.otlhd.com.net.DownloadResponseBody;
import bc.otlhd.com.net.OkHttpUtils;
import bc.otlhd.com.ui.activity.IssueApplication;
import bc.otlhd.com.ui.activity.UploadImagAtivity;
import bc.otlhd.com.utils.FileUtil;
import bc.otlhd.com.utils.LogUtils;
import bc.otlhd.com.utils.MyShare;
import bc.otlhd.com.utils.net.HttpListener;
import bc.otlhd.com.utils.upload.UpAppUtils;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author: Jun
 * @date : 2017/4/26 15:51
 * @description :
 */
public class UploadImagController extends BaseController {
    private UploadImagAtivity mView;
    private ProgressBar pb_progressbar, pb_progressbar02;
    private TextView one_tv, sum_tv, result_tv, one_02_tv,result_02_tv;
    private Button bt_click, bt_down_data;
    String name = "";
    Bitmap bitmap;
    String goodsPath = UpAppUtils.UP_SAVEPATH + File.separator + "goods";
    String sceensPath = UpAppUtils.UP_SAVEPATH + File.separator + "sceens";

    String mDataPath = UpAppUtils.UP_SAVEPATH + File.separator + "data";
    private int type = 0;
    private int mDownType = -1;

    JSONArray goodsList;
    JSONArray sceneList;
    int i = 0;
    int j = 0;
//    int k = 0;
    int l = 0;
    int m = 0;
    int n = 0;
    private int mCurrentSpeed = 0;
    private UploadImageDao mImageDao;

    private Task mTask;
    private int id;
    private int newCount;
    private JSONArray galleryList;
    private String galleryPath=UpAppUtils.UP_SAVEPATH+File.separator+"goods";
    private List<DownLoadImageBean> downloadImageList;
    private int total;
    private String mPath;
    private int[] isDownload;
    private OkHttpClient.Builder dlOkhttp;
    private DownloadResponseBody.DownloadListener downloadListener;


    public UploadImagController(UploadImagAtivity v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {
        //        mDownType = MyShare.get(mView).getInt("DownType");//下载类型
        //        mCurrentSpeed = MyShare.get(mView).getInt("CurrentSpeed");//当前进度
        //        i = MyShare.get(mView).getInt("i");//i数量
        //        j = MyShare.get(mView).getInt("j");//j数量
        //        k = MyShare.get(mView).getInt("k");//k数量
        //        l = MyShare.get(mView).getInt("l");//l数量
        //        m = MyShare.get(mView).getInt("m");//m数量
        //        n = MyShare.get(mView).getInt("n");//n数量
        mImageDao = new UploadImageDao(mView);
        //        MyToast.show(mView,mImageDao.getAll().size()+"");

    }

    private void initView() {
        pb_progressbar = (ProgressBar) mView.findViewById(R.id.pb_progressbar);
        pb_progressbar02 = (ProgressBar) mView.findViewById(R.id.pb_progressbar02);
        one_tv = (TextView) mView.findViewById(R.id.one_tv);
        one_02_tv = (TextView) mView.findViewById(R.id.one_02_tv);
        sum_tv = (TextView) mView.findViewById(R.id.sum_tv);
        result_tv = (TextView) mView.findViewById(R.id.result_tv);
        result_02_tv = (TextView) mView.findViewById(R.id.result_02_tv);
        one_tv.setVisibility(View.GONE);
        sum_tv.setVisibility(View.GONE);
        result_tv.setVisibility(View.GONE);
        result_02_tv.setVisibility(View.GONE);
        bt_click = (Button) mView.findViewById(R.id.bt_click);
        bt_down_data = (Button) mView.findViewById(R.id.bt_down_data);

    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }
    public void sendImage() {
        getPicList();
    }

    public void sendData() {
        bt_down_data.setClickable(false);
        bt_down_data.setBackgroundColor(mView.getResources().getColor(R.color.divider_gray));
        bt_down_data.setText("正在下载中,请耐心等待..");
        pb_progressbar02.setMax(100);
        result_02_tv.setVisibility(View.GONE);
        getBrandList();
        getFilterAttr();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final boolean isResult = GetDataInputStream(NetWorkConst.DOWN_SQL_URL);

                mView.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isResult) {
                            bt_down_data.setText("下载完毕!");
                            IssueApplication.DataLoadFinish=true;
//                            OkHttpUtils.hashkNewwork()=false;
                            if(IssueApplication.DataLoadFinish&&IssueApplication.PicLoadFinish){
                                MyShare.get(mView).putString(Constance.vertion,IssueApplication.pic_version);
                            }
                        }else{
                            bt_down_data.setClickable(true);
                            bt_down_data.setText("重试");
                            bt_down_data.setBackgroundColor(mView.getResources().getColor(R.color.colorPrimaryRed));
                            result_02_tv.setText("下载失败,请重试! ");
                            result_02_tv.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        }).start();


    }

    private void getFilterAttr() {
        OkHttpUtils.getGoodsList("0", "1", "0", "", "", "", "20", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            MyShare.get(mView).putString(Constance.filter_attr,response.body().string());
            }
        });
        OkHttpUtils.getSceneList("0", 1, "", "", "", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                    MyShare.get(mView).putString(Constance.scene_all_attr_list,response.body().string());
            }
        });

    }

    private void getBrandList() {
        OkHttpUtils.getbrandList(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result=response.body().string();
                MyShare.get(mView).putString(Constance.brandList,result);
                JSONObject jsonObject=new JSONObject(result);
                if(jsonObject!=null){
                    bocang.json.JSONArray data=jsonObject.getJSONArray(Constance.data);
                    if(data!=null){
                        for(int i=0;i<data.length();i++){
                            final String id=data.getJSONObject(i).getString(Constance.id);
                        OkHttpUtils.getBrandDetail(id, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                    String detail=response.body().string();
                                    MyShare.get(mView).putString(Constance.brandDetail+id,detail);
                            }
                        });
                        }
                    }
                }
            }
        });
    }

    public void getPicList() {
        result_tv.setVisibility(View.GONE);
        bt_click.setClickable(false);
        bt_click.setBackgroundColor(mView.getResources().getColor(R.color.divider_gray));
        bt_click.setText("正在下载中,请耐心等待..");

        mNetWork.sendPicList(mView, new HttpListener() {

            @Override
            public void onSuccessListener(int what, com.alibaba.fastjson.JSONObject ans) {
                newCount = ans.getInteger(Constance.count);

                IssueApplication.isUploadTip = true;
                goodsList = ans.getJSONArray(Constance.goods_list);
                sceneList = ans.getJSONArray(Constance.scene_list);
                galleryList = ans.getJSONArray(Constance.goods_gallery);
//                LogUtils.logE("picresult",ans.toJSONString());
                downloadImageList = new ArrayList<>();
                for(int i=0;i<goodsList.size();i++){
                    DownLoadImageBean map=new DownLoadImageBean();
                    map.setPath(goodsPath);
                    map.setUrl(NetWorkConst.UR_PRODUCT_URL+goodsList.getJSONObject(i).getString(Constance.img_url));
                    map.setLocal(goodsList.getJSONObject(i).getString(Constance.img_url));
                    downloadImageList.add(map);
                }
                for(int i=0;i<goodsList.size();i++){
                    DownLoadImageBean map=new DownLoadImageBean();
                    map.setPath(goodsPath);
                    map.setUrl(NetWorkConst.UR_PRODUCT_URL+goodsList.getJSONObject(i).getString(Constance.img_url)+"!400X400.png");
                    map.setLocal(goodsList.getJSONObject(i).getString(Constance.img_url)+"!400X400.png");
                    downloadImageList.add(map);
                }
                for(int i=0;i<sceneList.size();i++){
                    DownLoadImageBean map=new DownLoadImageBean();
                    map.setPath(sceensPath);
                    int mId= Integer.parseInt(sceneList.getJSONObject(i).getString(Constance.id));
                    String path="";
                    if (mId <= 1551) {
                        path = NetWorkConst.URL_SCENE + sceneList.getJSONObject(i).getString(Constance.path);
                    } else {
                        path = NetWorkConst.UR_SCENE_URL + sceneList.getJSONObject(i).getString(Constance.path);
                    }
//                    LogUtils.logE("path",path);
                    map.setUrl(path);
                    map.setLocal(sceneList.getJSONObject(i).getString(Constance.path));
                    downloadImageList.add(map);
                }
//                for(int i=0;i<sceneList.size();i++){
//                    DownLoadImageBean map=new DownLoadImageBean();
//                    map.setPath(sceensPath);
//                    map.setUrl(NetWorkConst.URL_SCENE+sceneList.getJSONObject(i).getString(Constance.path));
//                    map.setLocal(sceneList.getJSONObject(i).getString(Constance.path));
//                    downloadImageList.add(map);
//                }
                for(int i=0;i<galleryList.size();i++){
                    DownLoadImageBean map=new DownLoadImageBean();
                    map.setPath(galleryPath);
                    map.setUrl(NetWorkConst.UR_PRODUCT_URL+galleryList.getJSONObject(i).getString(Constance.img_url));
                    map.setLocal(galleryList.getJSONObject(i).getString(Constance.img_url));
                    downloadImageList.add(map);
                }
//                int total = (downloadImageList.size());
                isDownload = new int []{0,0};
                startImageloadMission();

//                pb_progressbar.setMax(goodsList.size() * 2 + sceneList.size() +galleryList.size());
//                if (mCurrentSpeed == total) {
//                    IssueApplication.PicLoadFinish=true;
//                    if(IssueApplication.DataLoadFinish&&IssueApplication.PicLoadFinish){
//                        MyShare.get(mView).putString(Constance.vertion,IssueApplication.pic_version);
//                    }
//                    pb_progressbar.setProgress(mCurrentSpeed);
//                    MyToast.show(mView, "下载完毕!");
//                    bt_click.setText("下载完毕!");
//                    one_tv.setText(mCurrentSpeed + "");
//                    sum_tv.setText(total + "");
//                    one_tv.setVisibility(View.VISIBLE);
//                    sum_tv.setVisibility(View.VISIBLE);
//                    return;
//                }
//                if (i < goodsList.size()) {
//                    setImage(0, i);
//                } else if (j < goodsList.size()) {
//                    setImage(1, j);
////                } else if (k < goodsList.size()) {
////                    setImage(2, k);
//                } else if (l < sceneList.size()) {
//                    setImage(3, l);
//                }else if(m<galleryList.size()){
//                    setImage(4,m);
//                }
//                one_tv.setVisibility(View.VISIBLE);
//                sum_tv.setVisibility(View.VISIBLE);
//                one_tv.setText(0 + "");
//                sum_tv.setText((goodsList.size() * 2 + sceneList.size() )+galleryList.size() + "");
//                pb_progressbar.setProgress(mCurrentSpeed);
//                one_tv.setText(mCurrentSpeed + "");
            }

            @Override
            public void onFailureListener(int what, com.alibaba.fastjson.JSONObject ans) {
                bt_click.setClickable(true);
                bt_click.setText("重试");
                bt_click.setBackgroundColor(mView.getResources().getColor(R.color.colorPrimaryRed));
                result_tv.setText("下载失败,请重试! ");
                result_tv.setVisibility(View.VISIBLE);
            }
        });
    }

    private void startImageloadMission() {
        mCurrentSpeed=0;
        total = downloadImageList.size();
        pb_progressbar.setMax(total);
        one_tv.setVisibility(View.VISIBLE);
                sum_tv.setVisibility(View.VISIBLE);
                one_tv.setText(0 + "");
                sum_tv.setText(total+ "");
                one_tv.setText(mCurrentSpeed + "");

        mPath = downloadImageList.get(mCurrentSpeed).getPath();
//        String url="";
//        if(mPath.equals(goodsPath)){
//            url=NetWorkConst.UR_PRODUCT_URL+downloadImageList.get(mCurrentSpeed).get("url");
//        }
        downloadListener = new DownloadResponseBody.DownloadListener() {
            @Override
            public void start(long max) {

            }

            @Override
            public void loading(int progress) {

            }

            @Override
            public void complete(String path) {
                mCurrentSpeed++;
//                LogUtils.logE("currentSp",""+mCurrentSpeed+","+path);
//                    try {
//                        File filePic = new File(FileUtil.getBrandExternDir(path));
//                        if (!filePic.exists()) {
//                            filePic.getParentFile().mkdirs();
//                            filePic.createNewFile();
////                            PgyCrashManager.reportCaughtException(UploadImagAtivity.this,new Exception("createNewFile:"+s+";"+ finalI));
//                            FileOutputStream fos = new FileOutputStream(filePic);
//                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                            fos.flush();
//                            fos.close();
//                        }

                mView.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(mCurrentSpeed==downloadImageList.size()){
                            pb_progressbar.setProgress(mCurrentSpeed);
                            MyToast.show(mView, "下载完毕!");
                            bt_click.setText("下载完毕!");
                            one_tv.setText(mCurrentSpeed + "");
                            sum_tv.setText(total + "");
                            one_tv.setVisibility(View.VISIBLE);
                            sum_tv.setVisibility(View.VISIBLE);
                            IssueApplication.PicLoadFinish=true;
                            if(IssueApplication.DataLoadFinish&&IssueApplication.PicLoadFinish){
                                MyShare.get(mView).putString(Constance.vertion,IssueApplication.pic_version);
                            }
//                                            handler.sendEmptyMessage(2);
                        }else {
                            if(mCurrentSpeed<downloadImageList.size()){
                                mPath=downloadImageList.get(mCurrentSpeed).getPath();
                                for(int z=mCurrentSpeed;z<downloadImageList.size();z++){
                                    if(!new File(mPath+"/"+downloadImageList.get(z).getLocal()).exists()){
                                        LogUtils.logE("localNotExi",mPath+"/"+downloadImageList.get(z).getLocal());
                                        mCurrentSpeed=z;
                                        break;
                                    }
                                }
                                mPath=downloadImageList.get(mCurrentSpeed).getPath();
                                downloadMission(downloadImageList.get(mCurrentSpeed).getUrl());
                            }
                            one_tv.setVisibility(View.VISIBLE);
                            sum_tv.setVisibility(View.VISIBLE);
                            pb_progressbar.setProgress(mCurrentSpeed);
                            one_tv.setText(mCurrentSpeed + "");
//                                    one_tv.setText(0 + "");
//                                    sum_tv.setText(""+total);

                        }
                    }
                });

//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
            }

            @Override
            public void fail(int code, String message) {

            }

            @Override
            public void loadfail(String message) {
                mCurrentSpeed++;
                if(mCurrentSpeed>=downloadImageList.size()){
                    return;
                }
                mPath=downloadImageList.get(mCurrentSpeed).getPath();
                downloadMission(downloadImageList.get(mCurrentSpeed).getUrl());
//                LogUtils.logE("loadFail",message+","+mCurrentSpeed+","+ NetWorkConst.URL_SCENE+downloadImageList.get(mCurrentSpeed).getLocal());
//                if(mCurrentSpeed==isDownload[0]&&isDownload[1]==1){
//                    if(mCurrentSpeed==downloadImageList.size()-1){
//                        return;
//                    }
//                }else {
//                    isDownload[0]=mCurrentSpeed;
//                    isDownload[1]=1;
//                    mPath=downloadImageList.get(mCurrentSpeed).getPath();
//                    downloadMission(NetWorkConst.URL_SCENE+downloadImageList.get(mCurrentSpeed).getLocal());
//                }
            }
        };
        downloadMission(downloadImageList.get(mCurrentSpeed).getUrl());
    }

    private void downloadMission(String url) {
        //                LogUtils.logE("currentSp",""+mCurrentSpeed+","+path);
//                    try {
//                        File filePic = new File(FileUtil.getBrandExternDir(path));
//                        if (!filePic.exists()) {
//                            filePic.getParentFile().mkdirs();
//                            filePic.createNewFile();
////                            PgyCrashManager.reportCaughtException(UploadImagAtivity.this,new Exception("createNewFile:"+s+";"+ finalI));
//                            FileOutputStream fos = new FileOutputStream(filePic);
//                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                            fos.flush();
//                            fos.close();
//                        }
//                                            handler.sendEmptyMessage(2);
//                                    one_tv.setText(0 + "");
//                                    sum_tv.setText(""+total);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
        final File filePic = new File(mPath+"/"+downloadImageList.get(mCurrentSpeed).getLocal());
//        final long start=1;
        LogUtils.logE("load",url);
        download(url, downloadListener, 0, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                downloadListener.loadfail(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtils.logE("loadRespon","OnRespon");
                long length = response.body().contentLength();
                if (length == -1){
                    // 说明文件已经下载完，直接跳转安装就好
                    LogUtils.logE("loadfail","OnComplete loadfail");
                    downloadListener.complete(String.valueOf(filePic.getAbsoluteFile()));
                    filePic.delete();
                    downloadListener.loadfail("");
                    return;
                }
//                downloadListener.start(length);
                // 保存文件到本地
                InputStream is = null;
                RandomAccessFile randomAccessFile = null;
                BufferedInputStream bis = null;

                byte[] buff = new byte[2048];
                int len = 0;
                try {
                    is = response.body().byteStream();
                    bis  =new BufferedInputStream(is);

                    File file = filePic;
                    LogUtils.logE("filePath",file.getAbsolutePath());
                    if(!file.exists()){
                        filePic.getParentFile().mkdirs();
                        filePic.createNewFile();
                    }
                    // 随机访问文件，可以指定断点续传的起始位置
                    randomAccessFile =  new RandomAccessFile(file, "rwd");
                    randomAccessFile.seek (0);

                    while ((len = bis.read(buff)) != -1) {
                        randomAccessFile.write(buff, 0, len);
                    }

                    // 下载完成
                    LogUtils.logE("loadRespon","complete");
                    downloadListener.complete(String.valueOf(file.getAbsoluteFile()));
                } catch (Exception e) {
                    LogUtils.logE("loadResponcatch","loadfail");
                    e.printStackTrace();
                    filePic.delete();
                    downloadListener.loadfail(e.getMessage());
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (bis != null){
                            bis.close();
                        }
                        if (randomAccessFile != null) {
                            randomAccessFile.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }


    private void setImage(int type, int index) {
        String path = "";
        switch (type) {
            case 0:
                name = goodsList.getJSONObject(index).getString(Constance.img_url);
                path = NetWorkConst.UR_PRODUCT_URL + name;
                break;
            case 1:
                name = goodsList.getJSONObject(index).getString(Constance.img_url) + "!400X400.png";
                path = NetWorkConst.UR_PRODUCT_URL + name;
                break;
            case 2:
//                name = goodsList.getJSONObject(index).getString(Constance.img_url) + "!280X280.png";
//                path = NetWorkConst.UR_PRODUCT_URL + name;
                break;
            case 3:
                name = sceneList.getJSONObject(index).getString(Constance.path);
                path = NetWorkConst.URL_SCENE + name;

                break;
            case 4:
                name=galleryList.getJSONObject(index).getString(Constance.img_url);
                path=NetWorkConst.UR_PRODUCT_URL+name;
                break;
//            case 4:
//                path = NetWorkConst.URL_SCENE + name;
//                name = sceneList.getJSONObject(index).getString(Constance.path) + "!400X400.png";
//                break;
//            case 5:
//                path = NetWorkConst.URL_SCENE + name;
//                name = sceneList.getJSONObject(index).getString(Constance.path) + "!280X280.png";
//                break;

        }
        mTask = new Task();
        mTask.execute(path);


    }

    boolean isNullImag = false;

    /**
     * 获取网络图片
     *
     * @param imageurl 图片网络地址
     * @return Bitmap 返回位图
     */
    public boolean GetImageInputStream(String imageurl) {
        Boolean isFinished = false; //用户完成下载
        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(imageurl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(6000); //超时设置
            connection.setDoInput(true);
            connection.setUseCaches(false); //设置不使用缓存
            InputStream inputStream = connection.getInputStream();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                File file = null;
                if (type == 0) {
                    file = new File(goodsPath);
                } else if(type==1){
                    file = new File(sceensPath);
                }else if(type==2){
                    file=new File(galleryPath);
                }
                FileOutputStream fos = null;
                //文件夹不存在，则创建它
                if (!file.exists()) {
                    file.mkdir();
                }
                try {
                    fos = new FileOutputStream(file + "/" + name);
                    Log.v("520it", file + "/" + name);
                    LogUtils.logE("progress",pb_progressbar.getProgress()+"");
                    int count = 0;
                    byte buf[] = new byte[1024];

                    do {

                        int numRead = inputStream.read(buf);
                        count += numRead;
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
                    PgyCrashManager.reportCaughtException(mView,new Exception("FileOutputStreamFaile,"+url.getPath()));
                    return false;
            }

        }

        UploadImage bean = new UploadImage();
        bean.setName(name);
        mImageDao.replaceOne(bean);

        return true;
        } catch (Exception e) {
            PgyCrashManager.reportCaughtException(mView,new Exception("downloadImageFailedException,"+e.getMessage()));
            try {
                url = new URL(imageurl.replace(NetWorkConst.URL_SCENE,NetWorkConst.UR_SCENE_URL));
                LogUtils.logE("newUrl",url.toString());

                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(6000); //超时设置
                connection.setDoInput(true);
                connection.setUseCaches(false); //设置不使用缓存
                InputStream inputStream2 = connection.getInputStream();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    File file2 = null;
                    if (type == 0) {
                        file2 = new File(goodsPath);
                    } else {
                        file2 = new File(sceensPath);
                    }
                    FileOutputStream fos2 = null;
                    //文件夹不存在，则创建它
                    if (!file2.exists()) {
                        file2.mkdir();
                    }

                    fos2 = new FileOutputStream(file2 + "/" + name);
                    Log.v("520it", file2 + "/" + name);
                    int count2 = 0;
                    byte buf2[] = new byte[1024];

                    do {

                        int numRead2 = inputStream2.read(buf2);
                        count2 += numRead2;
                        if (numRead2 <= 0) {
                            isFinished = true;
                        } else {
                            fos2.write(buf2, 0, numRead2);
                        }
                    }
                    while (!isFinished);// 点击取消或下载完成就停止下载.
                    fos2.close();
                    inputStream2.close();
                }
            }catch (Exception e2){
                PgyCrashManager.reportCaughtException(mView,new Exception("downloadImageFailed2,"+url.getPath()));
            }
            finally {
            return true;
            }
        }
    }

        /**
     * 获取网络图片
     *
     * @return Bitmap 返回位图
     */
    public boolean  GetDataInputStream(String dataUrl) {
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
                    int count = 0;
                    byte buf[] = new byte[1024];

                    do {

                        int numRead = inputStream.read(buf);
                        count += numRead;
                        pb_progressbar02.setProgress((int) (((float) count / length) * 100) + 1);
                        final int finalCount = count;
                        mView.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                one_02_tv.setText(((int) (((float) finalCount / length) * 100) + 1) + "");
                            }
                        });
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
                    PgyCrashManager.reportCaughtException(mView,e);
                    LogUtils.logE("excpA",e.toString());
                    return false;
                }
            }


        } catch (Exception e) {
            PgyCrashManager.reportCaughtException(mView,e);
            LogUtils.logE("excpB",e.toString());
            return false;
        }

        UploadImage bean = new UploadImage();
        bean.setName(name);
        mImageDao.replaceOne(bean);
        LogUtils.logE("excptrue","true");
        return true;
    }

    Boolean isDown = false;


    /**
     * 异步线程下载图片
     */
    class Task extends AsyncTask<String, Integer, Void> {

        protected Void doInBackground(String... params) {
            if (isCancelled())
                return null;
            if (mImageDao.getImage(name)) {
                LogUtils.logE("progress",pb_progressbar.getProgress()+"，"+name);
                isDown = true;
            } else {
                isDown = GetImageInputStream((String) params[0]);
            }

            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //            if (type == 0) {
            //                //点击图片后将图片保存到SD卡跟目录下的Test文件夹内
            //                SavaImage(bitmap, goodsPath);
            //            } else if (type == 1) {
            //                //点击图片后将图片保存到SD卡跟目录下的Test文件夹内
            //                SavaImage(bitmap, sceensPath);
            //            }
            //
            MyShare.get(mView).putInt("DownType", mDownType);//下载类型
            MyShare.get(mView).putInt("CurrentSpeed", mCurrentSpeed);//当前进度
            MyShare.get(mView).putInt("i", i);//i数量
            MyShare.get(mView).putInt("j", j);//j数量
//            MyShare.get(mView).putInt("k", k);//k数量
            MyShare.get(mView).putInt("l", l);//l数量
//            MyShare.get(mView).putInt("m", m);//m数量
//            MyShare.get(mView).putInt("n", n);//n数量
            if (!isDown) {
                mView.hideLoading();
                result_tv.setText("下载失败,请重试! ");
                result_tv.setVisibility(View.VISIBLE);
                bt_click.setClickable(true);
                bt_click.setText("重试");
                bt_click.setBackgroundColor(mView.getResources().getColor(R.color.colorPrimaryRed));
                return;

            }


            if (i == goodsList.size() - 1) {
                if (j == goodsList.size() - 1) {
//                    if (k == goodsList.size() - 1) {
                        if (l == sceneList.size() - 1) {
                            if(m==galleryList.size()-1){
                                    mView.hideLoading();
                                    MyToast.show(mView, "下载完毕!");
                                    bt_click.setText("下载完毕!");
                                IssueApplication.PicLoadFinish=true;
                                MyShare.get(mView).putInt(Constance.count, newCount);
                                if(IssueApplication.DataLoadFinish&&IssueApplication.PicLoadFinish){
                                    MyShare.get(mView).putString(Constance.vertion,IssueApplication.pic_version);
                                }
                                    return;
                            }else {
                                type=2;
                                setImage(4,m);
                                m=m+1;
                                mDownType=4;
                            }
                        } else {
                            type = 1;
                            setImage(3, l);
                            l = l + 1;
                            mDownType = 3;
                        }
//                    } else {
//                        setImage(2, k);
//                        k = k + 1;
//                        mDownType = 2;
//                    }

                } else {
                    setImage(1, j);
                    j = j + 1;
                    mDownType = 1;
                }
            } else {
                type = 0;
                i = i + 1;
                setImage(0, i);
                mDownType = 0;

            }

            mCurrentSpeed = i + j  + l + m + n;
            pb_progressbar.setProgress(mCurrentSpeed);
            one_tv.setText((mCurrentSpeed) + "");

        }

    }

    /**
     * 保存位图到本地
     *
     * @param bitmap
     * @param path   本地路径
     * @return void
     */
    public void SavaImage(Bitmap bitmap, String path) {

        if (AppUtils.isEmpty(bitmap)) {
            return;
        }
        File file = new File(path);
        FileOutputStream fileOutputStream = null;
        //文件夹不存在，则创建它
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            fileOutputStream = new FileOutputStream(path + "/" + name);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            Log.v("520it", e.getMessage());
            file.delete();
        }
    }
    public Call download(String url, final DownloadResponseBody.DownloadListener downloadListener, final long startsPoint, Callback callback){
        Request request = new Request.Builder()
                .url(url)
//                .header("RANGE", "bytes=" + startsPoint + "-")//断点续传
                .build();

//        // 重写ResponseBody监听请求
//        Interceptor interceptor = new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Response originalResponse = chain.proceed(chain.request());
//                return originalResponse.newBuilder()
//                        .body(new DownloadResponseBody(originalResponse, startsPoint, downloadListener))
//                        .build();
//            }
//        };

        if(dlOkhttp==null)
            dlOkhttp = new OkHttpClient.Builder()
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .connectTimeout(10, TimeUnit.SECONDS);
//                .addNetworkInterceptor(interceptor);
//        // 绕开证书
//        try {
//            setSSL(dlOkhttp);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        // 发起请求
        Call call = dlOkhttp.build().newCall(request);
        call.enqueue(callback);
        return call;
    }
    public void cancle() {
        if (!AppUtils.isEmpty(mTask))
            mTask.cancel(true);
    }
}
