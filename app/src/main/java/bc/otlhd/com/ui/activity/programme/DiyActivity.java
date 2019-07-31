package bc.otlhd.com.ui.activity.programme;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pgyersdk.crash.PgyCrashManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import bc.otlhd.com.R;
import bc.otlhd.com.bean.Goods;
import bc.otlhd.com.bean.SceneBean;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.cons.NetWorkConst;
import bc.otlhd.com.controller.programme.DiyController;
import bc.otlhd.com.data.SceneDao;
import bc.otlhd.com.net.OkHttpUtils;
import bc.otlhd.com.ui.activity.IssueApplication;
import bc.otlhd.com.ui.activity.buy.ShoppingCartActivity;
import bc.otlhd.com.utils.FileUtil;
import bc.otlhd.com.utils.LogUtils;
import bc.otlhd.com.utils.MyShare;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.IntentUtil;
import bocang.utils.PermissionUtils;
import bocang.view.BaseActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * @author: Jun
 * @date : 2017/2/18 10:31
 * @description :场景配灯
 */
public class DiyActivity extends BaseActivity implements View.OnLongClickListener {
    private DiyController mController;
    private ImageView goproductIv, goscreenctIv, goPhotoIv, goImageIv, goBrightnessIv, cartIv, goTowCodeIv, goShareIv, goSaveIv;
    private LinearLayout image_ll, goCart_ll, detail_ll, delete_ll,fuwei_ll;
    private FrameLayout sceneFrameLayout;
    //    public String mPath;
    public SparseArray<com.alibaba.fastjson.JSONObject> mSelectedLightSA = new SparseArray<>();
    public SparseArray<Goods> mSelectedLightSA02 = new SparseArray<>();
    public com.alibaba.fastjson.JSONObject mGoodsObject;
    public Goods mGoodsObject02;
    public String mProperty = "";
    public String mProductId = "";
    private LinearLayout seekbar_ll;
    private RelativeLayout brigth_rl;
    private View data_rl, detail_rl;
    private EditText et_search;
    private Button toDetailBtn, toCollectBtn;
    public String mSceenPath;
    private View left_ll, right_ll;

    private List<Integer> mHelpImages;
    private int mIndexHelp = 0;
    private ImageView yindao_iv;
    public RelativeLayout yindao_rl;
    private LinearLayout list_ll;
    private View iv_jingxiang;
    private View iv_liangdu;
    private View goFangAn;
    private View ll_product;
    private View ll_screen;
    private View ll_photo;
    private View ll_save;
    private String path;
    private View sceneBgIv;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new DiyController(this);
        if(TextUtils.isEmpty(mSceenPath)){
            if(!OkHttpUtils.hashkNewwork()){
                List<SceneBean> temp= SceneDao.getSceneList("",1);
                if(temp!=null&&temp.size()>0){
                    int max=new Random().nextInt(temp.size()-1);
//                    int mId= Integer.parseInt(temp.get(max).getId());
                    String path1 = FileUtil.getSceenExternDir(temp.get(max).getPath());
                    File imageFile = new File(path1);
                    if (imageFile.exists()) {
                        path = "file://" + imageFile.toString();
                        mController.displaySceneBg(path);
                    }
                }
                return;
            }
            OkHttpUtils.getSceneList("0", 1, "", "", "", new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    JSONObject reuslt=new JSONObject(response.body().string());
                    if(reuslt!=null){
                        JSONArray scenelist=reuslt.getJSONArray(Constance.scenelist);
                        if(scenelist!=null&&scenelist.length()>0){
                            int max=new Random().nextInt(scenelist.length()-1);
                            path = "";
                            if(OkHttpUtils.hashkNewwork()){
                                int mId= Integer.parseInt(scenelist.getJSONObject(max).getString(Constance.id));
                                if(mId<=1551){
                                    path =NetWorkConst.URL_SCENE+scenelist.getJSONObject(max).getString(Constance.path);
                                }else {
                                    path = NetWorkConst.UR_SCENE_URL + scenelist.getJSONObject(max).getString(Constance.path);
                                }
                                String path1 = FileUtil.getSceenExternDir(scenelist.getJSONObject(max).getString(Constance.path));
//                                File imageFile = new File(path1 );
//                                if (imageFile.exists()) {
//                                    path = "file://" + imageFile.toString();
//                                }
                                LogUtils.logE("fileexists://",path);

                            }else {
                                path ="file://"+scenelist.getJSONObject(max).getString(Constance.path);
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mController.displaySceneBg(path);
                                }
                            });
                        }
                    }

                }
            });
        }

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_diy);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        //沉浸式状态栏
//        setColor(this, getResources().getColor(R.color.colorPrimary));
        goproductIv = getViewAndClick(R.id.goproductIv);
        goscreenctIv = getViewAndClick(R.id.goscreenctIv);
        goPhotoIv = getViewAndClick(R.id.goPhotoIv);
        goImageIv = getViewAndClick(R.id.goImageIv);
//        goBrightnessIv = getViewAndClick(R.id.goBrightnessIv);
        cartIv = getViewAndClick(R.id.cartIv);
        goTowCodeIv = getViewAndClick(R.id.goTowCodeIv);
        goSaveIv = getViewAndClick(R.id.goSaveIv);
        image_ll = getViewAndClick(R.id.image_ll);
        goCart_ll = getViewAndClick(R.id.goCart_ll);
        detail_ll = getViewAndClick(R.id.detail_ll);
        delete_ll = getViewAndClick(R.id.delete_ll);
        et_search = getViewAndClick(R.id.et_search);
        fuwei_ll = getViewAndClick(R.id.fuwei_ll);
        toDetailBtn = getViewAndClick(R.id.toDetailBtn);
        toCollectBtn = getViewAndClick(R.id.toCollectBtn);
        goShareIv = getViewAndClick(R.id.goShareIv);
        sceneFrameLayout = getViewAndClick(R.id.sceneFrameLayout);
        list_ll = getViewAndClick(R.id.list_ll);
        iv_jingxiang = getViewAndClick(R.id.iv_jingxiang);
        iv_liangdu = getViewAndClick(R.id.iv_liangdu);
        goFangAn = getViewAndClick(R.id.goFangAn);
        ll_product = getViewAndClick(R.id.ll_product);
        ll_screen = getViewAndClick(R.id.ll_screen);
        ll_photo = getViewAndClick(R.id.ll_photo);
        ll_save = getViewAndClick(R.id.ll_save);
        sceneBgIv = getViewAndClick(R.id.sceneBgIv);

        sceneFrameLayout.setOnLongClickListener(this);
        seekbar_ll = (LinearLayout) findViewById(R.id.seekbar_ll);
        brigth_rl = (RelativeLayout) findViewById(R.id.brigth_rl);
        data_rl = (LinearLayout) findViewById(R.id.data_rl);
        detail_rl = (RelativeLayout) findViewById(R.id.detail_rl);
        Drawable drawable = getResources().getDrawable(R.drawable.search);
        drawable.setBounds(5, 0, 35, 35);
        et_search.setCompoundDrawables(drawable, null, null, null);
        et_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                // 修改回车键功能
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(DiyActivity.this
                                            .getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    mController.searchData(et_search.getText().toString());
                    data_rl.setVisibility(View.VISIBLE);
                }

                return false;
            }
        });

        yindao_iv = (ImageView) findViewById(R.id.yindao_iv);
        yindao_rl = (RelativeLayout) findViewById(R.id.yindao_rl);

        boolean showHelp= MyShare.get(this).getBoolean(Constance.SHOWHELP);
        if(!showHelp){
            yindao_rl.setVisibility(View.VISIBLE);
            MyShare.get(this).putBoolean(Constance.SHOWHELP, true);
        }else{
            yindao_rl.setVisibility(View.GONE);
        }

        left_ll = getViewAndClick(R.id.left_ll);
        right_ll = getViewAndClick(R.id.right_ll);

    }

    @Override
    protected void initData() {
        mHelpImages = new ArrayList<>();
        mHelpImages.add(R.mipmap.design1);
        mHelpImages.add(R.mipmap.design2);
        mHelpImages.add(R.mipmap.design3);
        mHelpImages.add(R.mipmap.design4);
        mHelpImages.add(R.mipmap.design5);
        mHelpImages.add(R.mipmap.design6);
        mHelpImages.add(R.mipmap.design7);
        mHelpImages.add(R.mipmap.design8);
        mHelpImages.add(R.mipmap.design9);
        mHelpImages.add(R.mipmap.design10);
        Intent intent = getIntent();
        mSceenPath = intent.getStringExtra(Constance.path);

        if (OkHttpUtils.hashkNewwork()) {
            mProperty = intent.getStringExtra(Constance.property);
            mGoodsObject = JSON.parseObject(intent.getStringExtra(Constance.product));
            if (AppUtils.isEmpty(mGoodsObject))
                return;
//            PgyCrashManager.reportCaughtException(this,new Exception("hasNet,Goods:"+mGoodsObject.toJSONString()));
            mProductId = mGoodsObject.getString(Constance.id);
        } else {
            mGoodsObject02 = (Goods) intent.getSerializableExtra(Constance.product);
            if(!AppUtils.isEmpty(mGoodsObject02)){
                mProductId = mGoodsObject02.getId() + "";
            }
//            PgyCrashManager.reportCaughtException(this,new Exception("hasNoNet,Goods:"+mGoodsObject02.getImg_url()));

        }


    }

    @Override
    protected void onViewClick(View v) {
        if(!IssueApplication.IsRoot()){
            SystemClock.sleep(IssueApplication.SLEEP_TIME);
        }

        brigth_rl.setVisibility(View.GONE);
        detail_rl.setVisibility(View.GONE);
        switch (v.getId()) {
            case R.id.goproductIv:
            case R.id.ll_product:
                mController.selectProduct();
                break;
            case R.id.goscreenctIv:
            case R.id.ll_screen:
                mController.selectScreen();
                break;
            case R.id.goPhotoIv:
            case R.id.ll_photo:
                mController.goPhotoImage();
                break;
            case R.id.goImageIv:
                mController.goPhotoImage();
                break;
            case R.id.iv_liangdu:
                mController.goBrightness();
                break;
            case R.id.iv_jingxiang:
                mController.sendBackgroudImage();
                break;
            case R.id.cartIv:
            case R.id.list_ll:
                IntentUtil.startActivity(this, ShoppingCartActivity.class, false);
                break;
            case R.id.sceneFrameLayout:
                mController.selectIsFullScreen();
                break;
            case R.id.goTowCodeIv:
                yindao_rl.setVisibility(View.VISIBLE);
                mIndexHelp=0;
                yindao_iv.setImageResource(mHelpImages.get(mIndexHelp));
//                mController.goPhoto();
                break;
            case R.id.goSaveIv:
            case R.id.ll_save:
                mController.saveDiy();
                break;
            case R.id.image_ll:
                mController.sendProductImage();
                break;
            case R.id.goCart_ll:
                mController.goShoppingCart();
                break;
            case R.id.detail_ll:
                mController.getProductDetail();
                break;
            case R.id.delete_ll:
                mController.goDetele();
                break;
            case R.id.toDetailBtn:
                mController.goDetail();
                break;
            case R.id.toCollectBtn:
                mController.sendCollect();
                break;
            case R.id.goShareIv:
                mController.getShareDiy(0);
                break;
            case R.id.fuwei_ll:
                mController.setfuwei();
                break;
            case R.id.left_ll:
                getYindaoImage(0);
                break;
            case R.id.right_ll:
                getYindaoImage(1);
                break;
            case R.id.goFangAn:
                startActivity(new Intent(this,ProgrammeActivity.class));
                break;
            case R.id.sceneBgIv:
                mController.selectIsFullScreen();
                break;
        }
    }

    /**
     * 引导页图片
     * @param type
     */
    private void getYindaoImage(int type) {
        if (type == 0) {//左边
            if (mIndexHelp != 0) {
                mIndexHelp = mIndexHelp - 1;
                yindao_iv.setImageResource(mHelpImages.get(mIndexHelp));
            } else {
                yindao_rl.setVisibility(View.GONE);
            }

        } else {//右边
            if (mIndexHelp != mHelpImages.size() - 1) {
                mIndexHelp = mIndexHelp + 1;
                yindao_iv.setImageResource(mHelpImages.get(mIndexHelp));
            } else {
                yindao_rl.setVisibility(View.GONE);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mController.ActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, new PermissionUtils.PermissionGrant() {
            @Override
            public void onPermissionGranted(int requestCode) {
                FileUtil.openImage(DiyActivity.this);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mController.onBackPressed();
    }

    @Override
    public boolean onLongClick(View v) {
        mController.setIsFullScene();
        return true;
    }
}
