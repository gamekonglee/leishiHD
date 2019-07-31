package bc.otlhd.com.ui.activity;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import bc.otlhd.com.R;
import bc.otlhd.com.bean.TimeLogBean;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.controller.MainController;
import bc.otlhd.com.data.TimeLogDao;
import bc.otlhd.com.ui.activity.brand.BrandHomeActivity;
import bc.otlhd.com.ui.activity.product.ProListActivity;
import bc.otlhd.com.ui.activity.programme.ProgrammeActivity;
import bc.otlhd.com.ui.fragment.BrandFragment;
import bc.otlhd.com.ui.fragment.CartFragment;
import bc.otlhd.com.ui.fragment.GoodsHomeFragment;
import bc.otlhd.com.ui.fragment.GoodsSpaceFragment;
import bc.otlhd.com.ui.fragment.GoodsStyleFragment;
import bc.otlhd.com.ui.fragment.GoodsTypeFragment;
import bc.otlhd.com.ui.fragment.HomeHDFragment;
import bc.otlhd.com.ui.fragment.MineFragment;
import bc.otlhd.com.ui.fragment.ProgrammeFragment;
import bc.otlhd.com.ui.fragment.SceneHDFragment;
import bc.otlhd.com.ui.fragment.WebViewFragment;
import bc.otlhd.com.ui.view.BottomBar;
import bc.otlhd.com.ui.view.WarnDialog;
import bc.otlhd.com.ui.view.popwindow.HighSettingPopWindow;
import bc.otlhd.com.utils.DateUtils;
import bc.otlhd.com.utils.LogUtils;
import bc.otlhd.com.utils.MyShare;
import bocang.json.JSONArray;
import bocang.utils.MyToast;
import cn.jpush.android.api.JPushInterface;

public class MainActivity extends bocang.view.BaseActivity {
    private HomeHDFragment mHomeFragment;
    private SceneHDFragment mSceneFragment;
    private CartFragment mCartFragment;
    private ProgrammeFragment mMatchFragment;
    private MineFragment mMineFragment;
    private BrandFragment mBrandFragment;
    private BottomBar mBottomBar;
    private Fragment currentFragmen;
    private int pager = 2;
    private long exitTime;
    public static JSONArray mCategories;
    private MainController mController;
    public String download = DOWNLOAD_SERVICE;
    private EditText et_search;
    public static boolean isForeground = false;
    private int mFragmentState = 0;
    public boolean mIsRefresh=false;
    public LinearLayout frag_tmall_ll;
    private GoodsHomeFragment goodsHomeFragment;
    private GoodsStyleFragment goodsStyleFragment;
    private GoodsTypeFragment goodsTypeFragment;
    private GoodsSpaceFragment goodsSpaceFragment;
    private LinearLayout main_ll;
    private Dialog mSettingDialog;
    private CartFragment cartFragment;
    public TextView tv_cache;
    private WebViewFragment tmFragment;
    private int lastOpenTime;
    private List<TimeLogBean> timeLogBeans;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        EventBus.getDefault().register(this);
    }


    // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
    private void init() {
        //        JPushInterface.init(getApplicationContext());
        JPushInterface.setDebugMode(true);//如果时正式版就改成false
        JPushInterface.init(this);
    }

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new MainController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main_hd);
        mBottomBar = (BottomBar) findViewById(R.id.title_bar);
        main_ll = (LinearLayout) findViewById(R.id.main_ll);
        mBottomBar.setOnClickListener(mBottomBarClickListener);
        initTab();
        et_search = (EditText) findViewById(R.id.et_search);
        Drawable drawable = getResources().getDrawable(R.drawable.search);
        drawable.setBounds(5, 0, 35, 35);
//        et_search.setCompoundDrawables(drawable, null, null, null);

        //        et_search.setImeOptions(EditorInfo.);
        et_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                // 修改回车键功能
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(MainActivity.this
                                            .getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    Intent intent=new Intent(MainActivity.this,ProListActivity.class);
                    intent.putExtra(Constance.key_word,et_search.getText().toString());
                    startActivity(intent);
                }

                return false;
            }
        });
        frag_tmall_ll = (LinearLayout)findViewById(R.id.frag_tmall_ll);
        boolean isShowTianMao= MyShare.get(this).getBoolean(Constance.SHOWTIANMAO);
        frag_tmall_ll.setVisibility(isShowTianMao?View.VISIBLE:View.GONE);
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        //最大分配内存
        int memory = activityManager.getMemoryClass();
        System.out.println("memory: "+memory);
        //最大分配内存获取方法2
        float maxMemory = (float) (Runtime.getRuntime().maxMemory() * 1.0/ (1024 * 1024));
        //当前分配的总内存
        float totalMemory = (float) (Runtime.getRuntime().totalMemory() * 1.0/ (1024 * 1024));
        //剩余内存
        float freeMemory = (float) (Runtime.getRuntime().freeMemory() * 1.0/ (1024 * 1024));
        System.out.println("maxMemory: "+maxMemory);
        System.out.println("totalMemory: "+totalMemory);
        System.out.println("freeMemory: "+freeMemory);
        final TimeLogDao dao=new TimeLogDao(this);
        TimeLogBean timeLogBean=new TimeLogBean();
        timeLogBean.setSignId(IssueApplication.mSignId);
        timeLogBean.setLogTime(DateUtils.getStrTime(System.currentTimeMillis()/1000L+""));
        timeLogBean.setLogType("1");
        dao.replaceOne(timeLogBean);
        timeLogBeans = dao.getAll();
        if(timeLogBeans !=null&& timeLogBeans.size()>0){

            long time=DateUtils.getTimeStamp(timeLogBeans.get(timeLogBeans.size()-1).getLogTime(),"yyyy-MM-dd HH:mm:ss");
            for(int i = 0; i< timeLogBeans.size(); i++){
//                LogUtils.logE("timelg",  "机器码:"+timeLogBeans.get(i).getSignId()+" 记录类型："+(timeLogBeans.get(i).getLogType().equals("1")?"打开时间":"在线时间  ")+timeLogBeans.get(i).getLogTime());
            }
            for(int i = timeLogBeans.size()-1; i>=0; i--){
                if(timeLogBeans.get(i).getLogType().equals("1")){
                    lastOpenTime = i;
                    LogUtils.logE("最后一次打开时间：", timeLogBeans.get(i).getLogTime());
                    break;
                }
            }

        }
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                LogUtils.logE("timetask","logTime");
                TimeLogBean timeLogBean=new TimeLogBean();
                timeLogBean.setSignId(IssueApplication.mSignId);
                timeLogBean.setLogTime(DateUtils.getStrTime(System.currentTimeMillis()/1000L+""));
                timeLogBean.setLogType("2");
                dao.replaceOne(timeLogBean);
//                LogUtils.logE("str02",Constance.online_time+DateUtils.getStrTime02(System.currentTimeMillis()/1000L+""));
                long time=MyShare.get(MainActivity.this).getLong(Constance.online_time+DateUtils.getStrTime02(System.currentTimeMillis()/1000L+""));
                LogUtils.logE("history",time+"");
                long timeCurrent=System.currentTimeMillis()-DateUtils.getTimeStamp(timeLogBeans.get(lastOpenTime).getLogTime(),"yyyy-MM-dd HH:mm:ss");
                LogUtils.logE("tc",timeCurrent+"");
                long totaltime=time+timeCurrent;
                LogUtils.logE("totalTime",totaltime+"");
                MyShare.get(MainActivity.this).putLong(Constance.online_time+DateUtils.getStrTime02(System.currentTimeMillis()/1000L+""),totaltime);
                LogUtils.logE("当前在线时长",(totaltime)+"ms"+"("+(totaltime)/1000+"s)");
            }
        },1000,1000*60*5);
        new  Thread(){
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                super.run();
                for(int i=0;i>=0;i++){
            SystemClock.sleep(500);
                    //剩余内存
                    float freeMemory = (float) (Runtime.getRuntime().freeMemory() * 1.0/ (1024 * 1024));
//                    if(freeMemory>=2) {
//                        ImageLoader.getInstance().loadImage("http://nvc.bocang.cc/App/leishi/Public/uploads/ad/1551430553.jpg", IssueApplication.getImageLoaderOption(), new ImageLoadingListener() {
//                            @Override
//                            public void onLoadingStarted(String s, View view) {
//
//                            }
//
//                            @Override
//                            public void onLoadingFailed(String s, View view, FailReason failReason) {
//
//                            }
//
//                            @Override
//                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//
//                            }
//
//                            @Override
//                            public void onLoadingCancelled(String s, View view) {
//
//                            }
//                        });
//                    }
//                    LogUtils.logE("wait","memory:"+freeMemory );
                }
            }
        }.start();

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static int getMemory() {
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);
        // dalvikPrivateClean + nativePrivateClean + otherPrivateClean;
        int totalPrivateClean = memoryInfo.getTotalPrivateClean();
        // dalvikPrivateDirty + nativePrivateDirty + otherPrivateDirty;
        int totalPrivateDirty = memoryInfo.getTotalPrivateDirty();
        // dalvikPss + nativePss + otherPss;
        int totalPss = memoryInfo.getTotalPss();
        // dalvikSharedClean + nativeSharedClean + otherSharedClean;
        int totalSharedClean = memoryInfo.getTotalSharedClean();
        // dalvikSharedDirty + nativeSharedDirty + otherSharedDirty;
        int totalSharedDirty = memoryInfo.getTotalSharedDirty();
        // dalvikSwappablePss + nativeSwappablePss + otherSwappablePss;
        int totalSwappablePss = memoryInfo.getTotalSwappablePss();

        int total = totalPrivateClean + totalPrivateDirty + totalPss + totalSharedClean + totalSharedDirty + totalSwappablePss;
        return total ;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mController.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private BottomBar.IBottomBarItemClickListener mBottomBarClickListener = new BottomBar.IBottomBarItemClickListener() {
        @Override
        public void OnItemClickListener(int resId) {
            if(!IssueApplication.IsRoot()){
                SystemClock.sleep(IssueApplication.SLEEP_TIME);
            }
            switch (resId) {
                case R.id.frag_top_ll:
                case R.id.frag_top_tv:
                    clickTab6Layout();
                    break;
//                case R.id.frag_product_ll:
//                case R.id.frag_product_tv:
//                    mFragmentState = 1;
//                    clickTab2Layout();
//                    break;
                case R.id.frag_match_ll:
                case R.id.frag_match_tv:
//                    mFragmentState = 5;
                    clickTab5Layout();
                    break;
                case R.id.frag_cart_ll:
                case R.id.frag_cart_tv:
//                    mFragmentState = 6;
                    clickTab7Layout();
                    break;
//                case R.id.frag_mine_ll:
//                    mFragmentState = 4;
//                    clickTab5Layout();
//                    break;
//                case R.id.frag_goods_ll:
//                    mFragmentState = 0;
//                    clickTab1Layout();
//                    break;
//                case R.id.frag_tmall_ll:
//                    clickTab7Layout();
//                    //                    mFragmentState = 0;
//                    //                    clickTab1Layout();
//                    break;
                case R.id.frag_top_ll_home:
                case R.id.frag_top_tv_home:
                    mFragmentState=0;
                    clickTab1Layout();
                    break;
                case R.id.frag_top_ll_stlye:
                case R.id.frag_top_tv_stlye:
                    mFragmentState=1;
                    clickTab2Layout();
                    break;
                case R.id.frag_top_ll_type:
                case R.id.frag_top_tv_type:
                    mFragmentState=2;
                    clickTab3Layout();
                    break;
                case R.id.frag_top_ll_space:
                case R.id.frag_top_tv_space:
                    mFragmentState=3;
                    clickTab4Layout();
                    break;
                case R.id.frag_top_ll_setting:
                case R.id.frag_top_tv_setting:
//                    mFragmentState=7;
                    showSettingDialog();
                    break;
                case R.id.frag_tmall_iv:
                case R.id.frag_tmall_ll:
                case R.id.frag_tmall_tv:
                    clickTab5();
                    break;
                case R.id.frag_top_ll_hot:
                case R.id.frag_top_tv_hot:
                    Intent intent=new Intent(MainActivity.this, ProListActivity.class);
                    intent.putExtra(Constance.is_hot,true);
                    startActivity(intent);
                    break;



            }
        }
    };

    private void clickTab5() {

        if(tmFragment ==null)  tmFragment = new WebViewFragment();
        Bundle bundle=new Bundle();
//        bundle.putString(Constance.url,"https://nvc.tmall.com");
        bundle.putString(Constance.url,"http://www.nvc-lighting.com.cn/");
        tmFragment.setArguments(bundle);
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), tmFragment);
    }

    private void showSettingDialog() {
        mSettingDialog = new Dialog(this);
        mSettingDialog.setContentView(R.layout.dialog_setting);
        ImageView iv_dimiss= (ImageView) mSettingDialog.findViewById(R.id.iv_dismiss);
        View rl_version= mSettingDialog.findViewById(R.id.rl_current_version);
        TextView tv_version= (TextView) mSettingDialog.findViewById(R.id.tv_version_name);
        View rl_setting= mSettingDialog.findViewById(R.id.rl_security_setting);
        View rl_clear = mSettingDialog.findViewById(R.id.rl_clear_cache);
        tv_cache = (TextView) mSettingDialog.findViewById(R.id.tv_cache);
        TextView tv_sgin= (TextView) mSettingDialog.findViewById(R.id.tv_sgin);
        tv_sgin.setText(""+IssueApplication.mSignId);
        iv_dimiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSettingDialog.dismiss();
            }
        });
        tv_cache.setText(mController.mTotalCacheSize);
        rl_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mController.clearCache();
            }
        });
        tv_version.setText(mController.getVerName(this));
        rl_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mController.sendVersion();
            }
        });
        rl_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText inputServer = new EditText(MainActivity.this);
                inputServer.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("请输入密码,进入高级设置!").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                        .setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        String password = inputServer.getText().toString();
                        String level_password=MyShare.get(MainActivity.this).getString(Constance.level_password);
                        if(TextUtils.isEmpty(level_password)){
                            level_password="nvc2019nvc";
                        }

                        if (password.toLowerCase().contains(level_password)) {
                            dialog.dismiss();
                            HighSettingPopWindow popWindow = new HighSettingPopWindow(MainActivity.this, MainActivity.this);
                            popWindow.onShow(main_ll);
                            mSettingDialog.dismiss();
                        } else {
                            MyToast.show(MainActivity.this, "密码错误,请重新输入!");
                        }
                    }
                });
                builder.show();

            }
        });
        mSettingDialog.show();

    }

    /**
     * 初始化底部标签
     */
    private void initTab() {
        if(goodsHomeFragment==null){
            goodsHomeFragment=new GoodsHomeFragment();
        }
//        if (mHomeFragment == null) {
//            mHomeFragment = new HomeHDFragment();
//        }
        if (!goodsHomeFragment.isAdded()) {
            // 提交事务
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.top_bar, goodsHomeFragment).commit();

            // 记录当前Fragment
            currentFragmen = goodsHomeFragment;
        }


    }


    /**
     * 点击第1个tab
     */
    private void clickTab1Layout() {
//        if (mHomeFragment == null) {
//            mHomeFragment = new HomeHDFragment();
//        }
//
//        addOrShowFragment(getSupportFragmentManager().beginTransaction(), mHomeFragment);
//        mHomeFragment.refreshData();

        if(goodsHomeFragment==null)goodsHomeFragment = new GoodsHomeFragment();
        addOrShowFragment(getSupportFragmentManager().beginTransaction(),goodsHomeFragment);

    }

    /**
     * 点击第2个tab
     */
    private void clickTab2Layout() {
//        if (mSceneFragment == null) {
//            mSceneFragment = new SceneHDFragment();
//        }
//        addOrShowFragment(getSupportFragmentManager().beginTransaction(), mSceneFragment);
        if(goodsStyleFragment ==null)goodsStyleFragment = new GoodsStyleFragment();
        addOrShowFragment(getSupportFragmentManager().beginTransaction(),goodsStyleFragment);

    }

    /**
     * 点击第3个tab
     */
    private void clickTab3Layout() {
//        if (mMatchFragment == null) {
//            mMatchFragment = new ProgrammeFragment();
//        }
//        addOrShowFragment(getSupportFragmentManager().beginTransaction(), mMatchFragment);

        if(goodsTypeFragment==null)goodsTypeFragment = new GoodsTypeFragment();
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), goodsTypeFragment);
    }

    /**
     * 点击第4个tab
     */
    private void clickTab4Layout() {
//        if (mCartFragment == null) {
//            mCartFragment = new CartFragment();
//        }
//        addOrShowFragment(getSupportFragmentManager().beginTransaction(), mCartFragment);
        if(goodsSpaceFragment ==null)goodsSpaceFragment = new GoodsSpaceFragment();
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), goodsSpaceFragment);
    }

    /**
     * 点击第5个tab
     */
    private void clickTab5Layout() {
        startActivity(new Intent(this, ProgrammeActivity.class));
//        if (mMineFragment == null) {
//            mMineFragment = new MineFragment();
//        }
//        addOrShowFragment(getSupportFragmentManager().beginTransaction(), mMineFragment);

    }
    /**
     * 点击6个tab
     */
    private void clickTab6Layout() {
//        if (mBrandFragment == null) {
//            mBrandFragment = new BrandFragment();
//        }
//        addOrShowFragment(getSupportFragmentManager().beginTransaction(), mBrandFragment);
        startActivity(new Intent(this, BrandHomeActivity.class));
    }
    /**
     * 点击7个tab
     */
    private void clickTab7Layout() {
//        Intent intent =new Intent(this, ShowRoomWebActivity.class);
//        intent.putExtra(Constance.path,"https://otljiaju.tmall.com/");
//        IssueApplication.IsMainActibity=true;
//        this.startActivity(intent);
        if(cartFragment ==null) cartFragment = new CartFragment();
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), cartFragment);

    }

    /**
     * 添加或者显示碎片
     *
     * @param transaction
     * @param fragment
     */
    private void addOrShowFragment(FragmentTransaction transaction,
                                   Fragment fragment) {
        if (currentFragmen == fragment)
            return;

        if (!fragment.isAdded()) { // 如果当前fragment未被添加，则添加到Fragment管理器中
            transaction.hide(currentFragmen)
                    .add(R.id.top_bar, fragment).commit();
        } else {
            transaction.hide(currentFragmen).show(fragment).commit();
        }

        currentFragmen = fragment;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(IssueApplication.IsMainActibity){
            mBottomBar.selectItem(R.id.frag_goods_ll);
            clickTab1Layout();
            IssueApplication.IsMainActibity=false;
            boolean isShowTianMao= MyShare.get(this).getBoolean(Constance.SHOWTIANMAO);
            frag_tmall_ll.setVisibility(isShowTianMao==true?View.VISIBLE:View.GONE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (pager == 2) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    exitTime = System.currentTimeMillis();
                    MyToast.show(this, R.string.back_desktop);
                } else {
                    finish();
                    //                    MyToast.cancelToast();
                    //                    getApp().toDesktop();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * show 激活 dialog
     */
    public void showActivateDialog() {
        WarnDialog activateDialog = new WarnDialog(this, "请激活该设备", "确定", true, false, false);
        activateDialog.setListener(new bc.otlhd.com.ui.view.BaseDialog.IConfirmListener() {
            @Override
            public void onDlgConfirm(bc.otlhd.com.ui.view.BaseDialog dlg, int flag) {
                if (flag == 0) {
                    MyToast.show(MainActivity.this, "激活成功!!");
                }
            }
        });
        activateDialog.show();
    }


    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
        mController.sendShoppingCart();
    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //在主线程执行
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserEvent(Integer action) {
        if (action == Constance.CARTCOUNT) {
            mController.setIsShowCartCount();
        }
    }

}
