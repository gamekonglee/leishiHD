package bc.otlhd.com.ui.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.pgyersdk.crash.PgyCrashManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bc.otlhd.com.R;
import bc.otlhd.com.bean.AdBean;
import bc.otlhd.com.cons.NetWorkConst;
import bc.otlhd.com.net.OkHttpUtils;
import bc.otlhd.com.ui.view.AutoScrollViewPager;
import bc.otlhd.com.utils.FileUtil;
import bc.otlhd.com.utils.LogUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class AdWaitActivity extends Activity {

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==3){
                for (int i = 0; i < bitmaps.size(); i++) {
                    ImageView imageView = new ImageView(AdWaitActivity.this);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setImageBitmap(bitmaps.get(i));
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AdWaitActivity.this.finish();
                        }
                    });
                    imageViews.add(imageView);
                }
                mCount=imageViews.size();
                myPagerAdapter = new MyPagerAdapter();
                vp.setAdapter(myPagerAdapter);
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(runnableForViewPager, TIME);

            }else if(msg.what==2){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        count=0;
                        for(int i=0;i<uri.size();i++){
                            ImageLoader.getInstance().loadImage("file://" + uri.get(i), new ImageLoadingListener() {
                                @Override
                                public void onLoadingStarted(String s, View view) {

                                }

                                @Override
                                public void onLoadingFailed(String s, View view, FailReason failReason) {

                                }

                                @Override
                                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                    count++;
                                    bitmaps.add(bitmap);
                                    if(count==uri.size()){
                                        handler.sendEmptyMessage(3);
                                    }
                                }

                                @Override
                                public void onLoadingCancelled(String s, View view) {

                                }
                            });
                        }
                    }
                });
            }
        }
    };
    private AutoScrollViewPager vp;
    private List<ImageView> imageViews;
    private static final int TIME = 10*1000;
    private int itemPosition;
    private List<Bitmap> bitmaps;
    private List<AdBean> adBeans;
    private MyPagerAdapter myPagerAdapter;
    private int count;
    private List<String> uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        setContentView(R.layout.activity_ad_wait);
        vp = (AutoScrollViewPager) findViewById(R.id.vp);

        imageViews = new ArrayList<>();
        bitmaps = new ArrayList<>();
        uri = new ArrayList<>();
        if(!OkHttpUtils.hashkNewwork()){
            AdWaitActivity.this.finish();
            return;
        }
        OkHttpUtils.getAdPic(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result=response.body().string();
                adBeans = new Gson().fromJson(result,new TypeToken<List<AdBean>>(){}.getType());
                if(adBeans!=null&&adBeans.size()>0){
                            try {
                                boolean isNeedRefresh=false;
                                for(int i=0;i<adBeans.size();i++){
                                    String url=adBeans.get(i).getPath();
                                    String path = FileUtil.getAdExternDir(url);
                                    File imageFile = new File(path );
                                    if (!imageFile.exists()) {
                                        isNeedRefresh=true;
                                        uri=new ArrayList<>();
                                        break;
//                                        imageURL = "file://" + imageFile.toString();
//                                        ImageLoader.getInstance().displayImage(imageURL, holder.imageView);
                                    }else {
                                        LogUtils.logE("file","isExists");
                                        uri.add(imageFile.toString());
                                    }
                                }
                                if(isNeedRefresh){
                                    count = 0;
                                for (int i = 0; i < adBeans.size(); i++) {
//                                    ImageView imageView = new ImageView(AdWaitActivity.this);
//                                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            bitmap = ImageUtil.getBitmapById(AdWaitActivity.this, R.mipmap.ad_1);
                                    final int finalI = i;
                                    ImageLoader.getInstance().loadImage(NetWorkConst.API_HOST + "/App/leishi/Public/uploads/ad/" + adBeans.get(i).getPath(), new ImageLoadingListener() {
                                                @Override
                                                public void onLoadingStarted(String s, View view) {

                                                }

                                                @Override
                                                public void onLoadingFailed(String s, View view, FailReason failReason) {

                                                }

                                                @Override
                                                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                                                    bitmaps.add(bitmap);
                                                    LogUtils.logE("imguri",s);
                                                    count++;
                                                    try {
                                                    File filePic = new File(FileUtil.getAdExternDir(adBeans.get(finalI).getPath()));
                                                    if (!filePic.exists()) {
                                                        filePic.getParentFile().mkdirs();
                                                        filePic.createNewFile();
                                                    }
                                                    FileOutputStream fos = new FileOutputStream(filePic);
                                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                                    uri.add(filePic.toString());
                                                    fos.flush();
                                                    fos.close();
                                                    if(count==adBeans.size()){
//                                                        handler.sendEmptyMessage(2);
                                                        AdWaitActivity.this.finish();
                                                    }
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }

                                                }

                                                @Override
                                                public void onLoadingCancelled(String s, View view) {

                                                }
                                            });

//            imageView.setImageBitmap(bitmaps.get(i));
//                                    imageView.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            AdWaitActivity.this.finish();
//                                        }
//                                    });
//                                    imageViews.add(imageView);

                                }

                                }else {
                                    //不需要更新
                                    handler.sendEmptyMessage(2);
                                }

                            }catch (Exception e){
                                PgyCrashManager.reportCaughtException(AdWaitActivity.this,e);
                            }


                }
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView v=imageViews.get(position);
            try {
//            switch (position){
//                case 0:
//                    bitmaps.set(0,ImageUtil.compressBgImage(ImageUtil.getBitmapById(AdWaitActivity.this,R.mipmap.ad_1)));
//                    break;
//                case 1:
//                    bitmaps.set(1,ImageUtil.compressBgImage(ImageUtil.getBitmapById(AdWaitActivity.this,R.mipmap.ad_2)));
//                    break;
//                case 2:
//                    bitmaps.set(2,ImageUtil.compressBgImage(ImageUtil.getBitmapById(AdWaitActivity.this,R.mipmap.ad_3)));
//                    break;
////                case 3:
////                    bitmap =ImageUtil.getBitmapById(AdWaitActivity.this,R.mipmap.ad_4);
////                    break;
//            }
                if (bitmaps.get(position)!=null&&!bitmaps.get(position).isRecycled()){
                    v.setImageBitmap(bitmaps.get(position));
                }
                ViewGroup parent = (ViewGroup) v.getParent();
                //Log.i("ViewPaperAdapter", parent.toString());
                if (parent != null) {
                    parent.removeAllViews();
                }
                container.addView(v);
                return v;
            }catch (Exception e){
                PgyCrashManager.reportCaughtException(AdWaitActivity.this,new Exception("instantiateItem"));
            }
            return v;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            bitmaps.set(position,null);
            container.removeView((View) object);
        }
    }

    private int mCount=(imageViews==null?0:imageViews.size());
    /**
     * ViewPager的定时器
     */
    Runnable runnableForViewPager = new Runnable() {
        @Override
        public void run() {
            try {
                itemPosition++;
                handler.postDelayed(this, TIME);
//                if(mCount==0)mCount=bitmaps.size();
                vp.setCurrentItem(itemPosition % mCount);
            } catch (Exception e) {
                e.printStackTrace();
                finish();
            }
        }
    };

    @Override
    protected void onDestroy() {
        for(int i=0;i<bitmaps.size();i++){
            Bitmap bitmap=bitmaps.get(i);
            if(bitmap != null ){
                bitmap.recycle();
                bitmap = null;
            }
            System.gc();
        }
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
