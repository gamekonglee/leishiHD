package bc.otlhd.com.ui.activity.product;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import bc.otlhd.com.R;
import bc.otlhd.com.bean.Goods;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.cons.NetWorkConst;
import bc.otlhd.com.controller.product.ProductDetailHDController;
import bc.otlhd.com.data.CartDao;
import bc.otlhd.com.data.SetPriceDao;
import bc.otlhd.com.net.OkHttpUtils;
import bc.otlhd.com.ui.activity.IssueApplication;
import bc.otlhd.com.ui.activity.buy.ShoppingCartActivity;
import bc.otlhd.com.ui.view.MyScrollView;
import bc.otlhd.com.ui.view.ScannerUtils;
import bc.otlhd.com.utils.ImageUtil;
import bc.otlhd.com.utils.MyShare;
import bc.otlhd.com.utils.UIUtils;
import bocang.utils.AppUtils;
import bocang.utils.IntentUtil;
import bocang.utils.MyToast;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/4/1 14:05
 * @description :
 */
public class ProductDetailHDActivity extends BaseActivity {
    private static final int PIC_ONLY = 1;
    private static final int PIC_CODE_BOTH = 2;
    private static final int CODE_ONLY = 3;
    private ProductDetailHDController mController;
//    public static com.alibaba.fastjson.JSONObject goodses;
    public static Goods goodses;
    public static Goods mGoods;
    public String mProperty = "";
    public String mPropertyValue = "";
    public com.alibaba.fastjson.JSONObject mProductObject;
    public int productId;
    private ImageView share_iv;
    private LinearLayout callLl, cart_Ll;
    private Button toDiyBtn, toCartBtn;
    private ImageView top_iv;
    private MyScrollView msv;
    private int mHeigh=0;
    private LinearLayout ll_share;
    private LinearLayout ll_download;
    private int save_type;
    private TextView tv_pic_code;
    private TextView tv_pic;
    private TextView tv_code;
    private TextView tv_save;
    private LinearLayout ll_img;
    private String phone;
    private Bitmap mBitmap;
    private String mLocalPath;
    private Dialog dialog;
    private TextView unMessageReadTv;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new ProductDetailHDController(this);
        getCartCount();

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_detail_hd);
        share_iv = getViewAndClick(R.id.share_iv);
        ll_share = getViewAndClick(R.id.ll_share);
        callLl = getViewAndClick(R.id.callLl);
        cart_Ll = getViewAndClick(R.id.cart_Ll);
        toDiyBtn = getViewAndClick(R.id.toDiyBtn);
        toCartBtn = getViewAndClick(R.id.toCartBtn);
        top_iv = getViewAndClick(R.id.top_iv);
        ll_download = getViewAndClick(R.id.ll_download);
        unMessageReadTv = getViewAndClick(R.id.unMessageReadTv);
        EventBus.getDefault().register(this);

        mHeigh = this.getResources().getDisplayMetrics().heightPixels;

        msv = (MyScrollView) findViewById(R.id.msv);
        msv.setScrollListener(new MyScrollView.ScrollListener() {
            @Override
            public void onScrollToBottom() {

            }

            @Override
            public void onScrollToTop() {

            }

            @Override
            public void onScroll(int scrollY) {
                if(mHeigh<scrollY){
                    top_iv.setVisibility(View.VISIBLE);
                }else{
                    top_iv.setVisibility(View.GONE);
                }
            }

            @Override
            public void notBottom() {

            }
        });
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if(OkHttpUtils.hashkNewwork()){
//            goodses = JSON.parseObject(intent.getStringExtra(Constance.product));
            goodses=new Gson().fromJson(intent.getStringExtra(Constance.product),Goods.class);
            productId = intent.getIntExtra(Constance.product, goodses.getId());
        }else{
            productId = intent.getIntExtra(Constance.id,0);
            mGoods= (Goods) intent.getSerializableExtra(Constance.product);
        }
    }


    @Override
    protected void onViewClick(View v) {
        if(!IssueApplication.IsRoot()){
            SystemClock.sleep(IssueApplication.SLEEP_TIME);
        }

        switch (v.getId()) {
//            case R.id.share_iv://分享
            case R.id.ll_share:
                mController.sendShareProduct();
                break;
            case R.id.callLl://联系客服
                mController.sendCall();
                break;
            case R.id.cart_Ll://购物车
                IntentUtil.startActivity(this, ShoppingCartActivity.class, false);
                break;
            case R.id.toDiyBtn://配配看
                mController.sendDiy();
                break;
            case R.id.toCartBtn://加入购物车
                mController.sendGoCart();
                break;
            case R.id.top_iv:
                msv.scrollTo(10, 10);
                top_iv.setVisibility(View.GONE);
                break;
            case R.id.ll_download:
                showDownLoadDialog();
                break;
            case R.id.tv_top_pic:
                save_type = PIC_ONLY;
                refreshDialog();
                break;
            case R.id.tv_pic_code:
                save_type=PIC_CODE_BOTH;
                refreshDialog();
                break;
            case R.id.tv_code:
                save_type=CODE_ONLY;
                refreshDialog();
                break;
            case R.id.tv_save:
                if(dialog!=null)dialog.dismiss();
                getSaveImage();
                break;

        }
    }
    /**
     * 保存图片
     */
    public void getSaveImage() {
        ActivityCompat.requestPermissions(this,
                new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"},
                1);
        PackageManager packageManager = getPackageManager();
        int permission = packageManager.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", getPackageName());
        if (PackageManager.PERMISSION_GRANTED != permission) {
            return;
        } else {
//            setShowDialog(true);
//            mActivity.setShowDialog("正在保存中..");
//            showLoading();
        }
    }
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mBitmap = ImageUtil.createViewBitmap(ll_img);
                    mLocalPath = ScannerUtils.saveImageToGallery(ProductDetailHDActivity.this, mBitmap, ScannerUtils.ScannerType.RECEIVER);
                    MyToast.show(ProductDetailHDActivity.this,"保存成功！");
//                    getShareApp();
//                    if(mBitmap !=null&& mBitmap.isRecycled()) mBitmap.recycle();
                }
            });


        }
    };
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mBitmap !=null&& !mBitmap.isRecycled()) mBitmap.recycle();
        EventBus.getDefault().unregister(this);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1&&grantResults[0]==0){
            new Thread(runnable).start();
        }
    }
    private void refreshDialog() {
        Drawable drawable=getResources().getDrawable(R.drawable.line_bottom);
        drawable.setBounds(0,0,getResources().getDimensionPixelOffset(R.dimen.x50), UIUtils.dip2PX(1));
        tv_pic_code.setTextColor(getResources().getColor(R.color.color_555555));
        tv_pic.setTextColor(getResources().getColor(R.color.color_555555));
        tv_code.setTextColor(getResources().getColor(R.color.color_555555));
        tv_pic_code.setCompoundDrawables(null,null,null,null);
        tv_pic.setCompoundDrawables(null,null,null,null);
        tv_code.setCompoundDrawables(null,null,null,null);
        switch (save_type){
            case PIC_ONLY:
                tv_pic.setCompoundDrawables(null,null,null,drawable);
                tv_pic.setTextColor(getResources().getColor(R.color.colorPrimaryGreen2));
                SetImgA();
                break;
            case CODE_ONLY:
                tv_code.setCompoundDrawables(null,null,null,drawable);
                tv_code.setTextColor(getResources().getColor(R.color.colorPrimaryGreen2));
                SetImgB();
                break;
            case PIC_CODE_BOTH:
                tv_pic_code.setCompoundDrawables(null,null,null,drawable);
                tv_pic_code.setTextColor(getResources().getColor(R.color.colorPrimaryGreen2));
                SetImgC();
                break;
        }

    }



    private void showDownLoadDialog() {
        dialog = new Dialog(this, R.style.customDialog);
        dialog.setContentView(R.layout.dialog_download);//这行一定要写在前面
        dialog.setCancelable(true);//点击外部不可dismiss
        dialog.setCanceledOnTouchOutside(true);
        tv_pic_code = (TextView) dialog.findViewById(R.id.tv_pic_code);
        tv_pic = (TextView) dialog.findViewById(R.id.tv_top_pic);
        tv_code = (TextView) dialog.findViewById(R.id.tv_code);
        tv_save = (TextView) dialog.findViewById(R.id.tv_save);
        ll_img = (LinearLayout) dialog.findViewById(R.id.ll_img);
        tv_pic.setOnClickListener(this);
        tv_pic_code.setOnClickListener(this);
        tv_code.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        if(mController.imageURL==null){
            MyToast.show(this,"数据加载中，请稍后");
            return;
        }
        SetImgA();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.RIGHT);
        window.setBackgroundDrawable(getResources().getDrawable(android.R.color.white));
        window.setWindowAnimations(R.style.dialogRightInStyle);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = getResources().getDimensionPixelSize(R.dimen.x400);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.y=0;
        params.x=0;
//        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;//就是这个属性导致window后所有的东西都成暗淡
        window.setAttributes(params);
        dialog.show();
    }

    private void SetImgA() {
        final ImageView iv_img=new ImageView(this);
        ImageLoader.getInstance().loadImage(mController.imageURL, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                if(iv_img!=null){
                iv_img.setImageBitmap(bitmap);
                }
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
        if(ll_img!=null){
            ll_img.removeAllViews();
            LinearLayout.LayoutParams paramsImg=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ll_img.addView(iv_img,paramsImg);
        }
    }

    private void SetImgB() {
        JSONObject userinfo=((IssueApplication) getApplication()).mUserInfo;
        if(userinfo!=null){
            phone = ((IssueApplication) getApplication()).mUserInfo.getString(Constance.phone);
        }else {
            phone="13927273545";
        }

        String path="";
        String title="";
        String imagePath="";
        String price="";
        if(MyShare.get(this).getBoolean(Constance.SET_PRICE)){
            SetPriceDao mPriceDao = new SetPriceDao(this);
            if(OkHttpUtils.hashkNewwork()){
                price=mPriceDao.getProductPrice(goodses.getId()+"").getShop_price()+"";
            }else {
                price=mPriceDao.getProductPrice(mGoods.getId()+"").getShop_price()+"";
            }
        }

        if(OkHttpUtils.hashkNewwork()){
            if (AppUtils.isEmpty(goodses))
                return;
            title =  goodses.getName() ;
            if(TextUtils.isEmpty(price)){
                price=goodses.getShop_price()+"";
            }
            path = NetWorkConst.SHAREPRODUCT+goodses.getId()+"&phone="+phone+"&price="+price;
            imagePath = NetWorkConst.PRODUCT_URL+goodses.getImg_url();
        }else{
            title =  mGoods.getName() ;
            if(TextUtils.isEmpty(price)){
                price=mGoods.getShop_price()+"";
            }
            path = NetWorkConst.SHAREPRODUCT+mGoods.getId()+"&phone="+phone+"&price="+price;
            imagePath =NetWorkConst.UR_PRODUCT_URL+mGoods.getImg_url();
        }
        final ImageView iv_img=new ImageView(this);
        iv_img.setImageBitmap(ImageUtil.createQRImage(path, 600, 600));
        TextView tv_name=new TextView(this);
        tv_name.setText(title);
        LinearLayout.LayoutParams ll_tv=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ll_tv.setMargins(0,15,0,0);
        tv_name.setLayoutParams(ll_tv);
        ll_img.setGravity(Gravity.CENTER);
        if(ll_img!=null){
            ll_img.removeAllViews();
            LinearLayout.LayoutParams paramsImg=new LinearLayout.LayoutParams(400,400);
            ll_img.addView(iv_img,paramsImg);
            ll_img.addView(tv_name,ll_tv);
        }
    }


    private void SetImgC() {
        final ImageView iv_img=new ImageView(this);
        ImageLoader.getInstance().loadImage(mController.imageURL, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                if(iv_img!=null){
                    iv_img.setImageBitmap(bitmap);
                }
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
        if(ll_img!=null){
            ll_img.removeAllViews();
            LinearLayout.LayoutParams paramsImg=new LinearLayout.LayoutParams(UIUtils.dip2PX(200),UIUtils.dip2PX(200));
            paramsImg.gravity=Gravity.CENTER;
            ll_img.addView(iv_img,paramsImg);
        }
        JSONObject userinfo=((IssueApplication) getApplication()).mUserInfo;
        if(userinfo!=null){
            phone = ((IssueApplication) getApplication()).mUserInfo.getString(Constance.phone);
        }else {
            phone="13927273545";
        }

        String path="";
        String title="";
        String imagePath="";
        String price="";
        if(MyShare.get(this).getBoolean(Constance.SET_PRICE)){
            SetPriceDao mPriceDao = new SetPriceDao(this);
            if(OkHttpUtils.hashkNewwork()){
                price=mPriceDao.getProductPrice(goodses.getId()+"").getShop_price()+"";
            }else {
                price=mPriceDao.getProductPrice(mGoods.getId()+"").getShop_price()+"";
            }
        }

        if(OkHttpUtils.hashkNewwork()){
            if (AppUtils.isEmpty(goodses))
                return;
            title = goodses.getName();
            if(TextUtils.isEmpty(price)){
                price=goodses.getShop_price()+"";
            }
            path = NetWorkConst.SHAREPRODUCT+"id="+goodses.getId()+"&phone="+phone+"&price="+price;
            imagePath = NetWorkConst.PRODUCT_URL+goodses.getImg_url();
        }else{
            title = mGoods.getName() ;
            if(TextUtils.isEmpty(price)){
                price=mGoods.getShop_price()+"";
            }
            path = NetWorkConst.SHAREPRODUCT+"id="+mGoods.getId()+"&phone="+phone+"&price="+price;
            imagePath =NetWorkConst.UR_PRODUCT_URL+mGoods.getImg_url();
        }
        final ImageView iv_code=new ImageView(this);
        iv_code.setImageBitmap(ImageUtil.createQRImage(path, 300, 300));
        TextView tv_name=new TextView(this);
        tv_name.setText(title);
        LinearLayout.LayoutParams ll_tv=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ll_tv.setMargins(0,15,0,0);
        ll_img.setGravity(Gravity.CENTER);
        if(ll_img!=null){
            LinearLayout.LayoutParams paramsImg=new LinearLayout.LayoutParams(UIUtils.dip2PX(200),UIUtils.dip2PX(200));
            paramsImg.gravity=Gravity.CENTER;
            ll_img.addView(iv_code,paramsImg);
            ll_img.addView(tv_name,ll_tv);
        }
    }
    //在主线程执行
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserEvent(Integer action) {
        if (action == Constance.CARTCOUNT) {
          getCartCount();
        }
    }

    private void getCartCount() {
        CartDao dao = new CartDao(this);
        int count =0;
        List<Goods> goodsList=dao.getAll();
        if(goodsList!=null&&goodsList.size()>0){
            for(int i=0;i<goodsList.size();i++){
                count+=goodsList.get(i).getBuyCount();
            }
        }
        IssueApplication.mCartCount = count;
        unMessageReadTv.setVisibility(IssueApplication.mCartCount == 0 ? View.GONE : View.VISIBLE);
        unMessageReadTv.setText(IssueApplication.mCartCount + "");
    }
}
