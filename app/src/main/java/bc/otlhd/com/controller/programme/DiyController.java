package bc.otlhd.com.controller.programme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Environment;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baiiu.filter.DropDownMenu;
import com.baiiu.filter.interfaces.OnFilterDoneListener;
import com.lib.common.hxp.view.PullToRefreshLayout;
import com.lib.common.hxp.view.PullableGridView;
import com.lidroid.xutils.BitmapUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.pgyersdk.crash.PgyCrashManager;

import org.greenrobot.eventbus.EventBus;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import bc.otlhd.com.R;
import bc.otlhd.com.adapter.BaseAdapterHelper;
import bc.otlhd.com.adapter.QuickAdapter;
import bc.otlhd.com.bean.Goods;
import bc.otlhd.com.bean.Programme;
import bc.otlhd.com.bean.SceneBean;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.cons.NetWorkConst;
import bc.otlhd.com.controller.BaseController;
import bc.otlhd.com.data.CartDao;
import bc.otlhd.com.data.GoodsDao;
import bc.otlhd.com.data.ProgrammeDao;
import bc.otlhd.com.data.SceneDao;
import bc.otlhd.com.data.SetPriceDao;
import bc.otlhd.com.listener.IDiyProductInfoListener;
import bc.otlhd.com.listener.INetworkCallBack;
import bc.otlhd.com.listener.ISelectScreenListener;
import bc.otlhd.com.listener.ITwoCodeListener;
import bc.otlhd.com.net.OkHttpUtils;
import bc.otlhd.com.ui.activity.IssueApplication;
import bc.otlhd.com.ui.activity.product.ProductDetailHDActivity;
import bc.otlhd.com.ui.activity.programme.DiyActivity;
import bc.otlhd.com.ui.activity.programme.SelectSchemeActivity;
import bc.otlhd.com.ui.adapter.ParamentAdapter;
import bc.otlhd.com.ui.adapter.ProductDropMenuAdapter;
import bc.otlhd.com.ui.adapter.SceneDropMenuAdapter;
import bc.otlhd.com.ui.view.StickerView;
import bc.otlhd.com.ui.view.TouchView;
import bc.otlhd.com.ui.view.TouchView02;
import bc.otlhd.com.ui.view.popwindow.DiyProductInfoPopWindow;
import bc.otlhd.com.ui.view.popwindow.SelectScreenPopWindow;
import bc.otlhd.com.ui.view.popwindow.TwoCodePopWindow;
import bc.otlhd.com.ui.view.popwindow.TwoCodeSharePopWindow;
import bc.otlhd.com.utils.FileUtil;
import bc.otlhd.com.utils.ImageUtil;
import bc.otlhd.com.utils.LogUtils;
import bc.otlhd.com.utils.MyShare;
import bc.otlhd.com.utils.UIUtils;
import bc.otlhd.com.utils.net.HttpListener;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;
import bocang.utils.PermissionUtils;


/**
 * @author: Jun
 * @date : 2017/2/18 10:33
 * @description :场景配灯
 */
public class DiyController extends BaseController implements INetworkCallBack, PullToRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, OnFilterDoneListener, HttpListener {
    private DiyActivity mView;
    private ImageView sceneBgIv;
    private String imageURL = "";
    private View diyContainerRl, data_rl, detail_rl;
    private Boolean isFullScreen = false;
    private Intent mIntent;
    private int mScreenWidth;
    private FrameLayout mFrameLayout, main_fl;
    private ProgressBar pd2;
    private String mStyle = "";
    private String mSpace = "";
    private int TIME_OUT = 10 * 1000;   //超时时间
    private String CHARSET = "utf-8"; //设置编码
    private String mTitle = "";
    private String mContent = "";
    private SeekBar seekbar;
    private int mSeekNum = 50;
    private LinearLayout seekbar_ll, botton_ll;
    private RelativeLayout brigth_rl;

    private DropDownMenu dropDownMenu;
    private com.alibaba.fastjson.JSONArray sceneAllAttrs;
    private com.alibaba.fastjson.JSONArray goodsAllAttrs;
    private PullToRefreshLayout mPullToRefreshLayout;
    private ProAdapter mProAdapter;
    private PullableGridView mGoodsSv;
    private int page = 1;
    private com.alibaba.fastjson.JSONArray goodses;
    private boolean initFilterDropDownView;
    public String keyword;
    private ProgressBar pd;
    public String mSortKey;
    public String mSortValue;
    public int mSelectType = 0;
    private String filter_attr = "";
    private ImageView two_code_iv;
    private JSONArray propertiesList;
    private ListView attr_lv;
    private ParamentAdapter mParamentAdapter;
    private Integer[] goodsIds;
    private Integer[] sceneIds;
    private SetPriceDao mPriceDao;
    private List<Goods> mGoodsList;
    public String filter_attr02;
    private String mId;
    private String path;
    private LinearLayout ll_search;
    private List<SceneBean> sceneBeans;


    public DiyController(DiyActivity v) {
        mView = v;
        thiss = new ArrayList<>();
        initView();
        initViewData();
    }


    private void initViewData() {

        initFilterDropDownView = true;
        if (OkHttpUtils.hashkNewwork()) {
            if (!AppUtils.isEmpty(mView.mGoodsObject)) {
                displayCheckedGoods(mView.mGoodsObject);
            }
            if (AppUtils.isEmpty(mView.mSceenPath))
                return;
            displaySceneBg(mView.mSceenPath);
        } else {
            if (!AppUtils.isEmpty(mView.mGoodsObject02)) {
                displayCheckedGoods02(mView.mGoodsObject02);
            }
            if (AppUtils.isEmpty(mView.mSceenPath))
                return;
            displaySceneBg(mView.mSceenPath);
        }


    }


    private void initView() {
        sceneBgIv = (ImageView) mView.findViewById(R.id.sceneBgIv);
        diyContainerRl = (RelativeLayout) mView.findViewById(R.id.diyContainerRl);
        data_rl = (View) mView.findViewById(R.id.data_rl);
        detail_rl = (RelativeLayout) mView.findViewById(R.id.detail_rl);
        mScreenWidth = mView.getResources().getDisplayMetrics().widthPixels;
        mFrameLayout = (FrameLayout) mView.findViewById(R.id.sceneFrameLayout);
        main_fl = (FrameLayout) mView.findViewById(R.id.main_fl);
        pd2 = (ProgressBar) mView.findViewById(R.id.pd2);
        seekbar = (SeekBar) mView.findViewById(R.id.seekbar);
        seekbar_ll = (LinearLayout) mView.findViewById(R.id.seekbar_ll);
        brigth_rl = (RelativeLayout) mView.findViewById(R.id.brigth_rl);

        initImageLoader();
//        sceneBgIv.setImageBitmap(ImageUtil.drawable2Bitmap(mView.getResources().getDrawable(R.mipmap.bg_diy)));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSeekNum = progress;
                if (!AppUtils.isEmpty(mSceneBg)){
                    ImageUtil.changeLight02(sceneBgIv, mSceneBg, mSeekNum);
                }else {
                    mSceneBg=ImageUtil.drawable2Bitmap(sceneBgIv.getDrawable());
                }

                for (int i = 1; i < mFrameLayout.getChildCount(); i++) {
                    try{
                        TouchView view = (TouchView) ((FrameLayout) mFrameLayout.getChildAt(i)).getChildAt(0);
                        Log.v("520it1", view.path.contains(NetWorkConst.UR_PRODUCT_URL) + "");
                        if (!view.path.contains(NetWorkConst.UR_PRODUCT_URL)) {
                            ImageUtil.changeLight02(view, BitmapFactory.decodeFile(view.path), mSeekNum);
                        } else {

                        }
                    }catch (Exception e){

                    }


                    //                    ImageUtil.changeLight02(view, ImageUtil.drawable2Bitmap(view.getDrawable()), 63);
                    //                    ImageUtil.changeLight02(view, ImageUtil.drawable2Bitmap(view.getDrawable()), mSeekNum);

                    //                                        ImageUtil.changeLight(view, mSeekNum - 50);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        dropDownMenu = (DropDownMenu) mView.findViewById(R.id.dropDownMenu);
        mPullToRefreshLayout = ((PullToRefreshLayout) mView.findViewById(R.id.mFilterContentView));
        mPullToRefreshLayout.setOnRefreshListener(this);
        ll_search = (LinearLayout) mView.findViewById(R.id.ll_search);
        mGoodsSv = (PullableGridView) mView.findViewById(R.id.gridView);
        mProAdapter = new ProAdapter();
        mGoodsSv.setAdapter(mProAdapter);
        mGoodsSv.setOnItemClickListener(this);
        pd = (ProgressBar) mView.findViewById(R.id.pd);
        botton_ll = (LinearLayout) mView.findViewById(R.id.botton_ll);
        two_code_iv = (ImageView) mView.findViewById(R.id.two_code_iv);
        attr_lv = (ListView) mView.findViewById(R.id.attr_lv);
        mPriceDao = new SetPriceDao(mView);
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    /**
     * 保存
     */
    public void saveDiy() {
        mIntent = new Intent(mView, SelectSchemeActivity.class);
        mView.startActivityForResult(mIntent, Constance.FROMSCHEME);
    }

    //    /**
    //     * 保存方案
    //     */
    //    private void saveData(int type) {
    //        //                产品ID的累加
    //        StringBuffer goodsid = new StringBuffer();
    //        for (int i = 0; i < mView.mSelectedLightSA.size(); i++) {
    //            goodsid.append(mView.mSelectedLightSA.get(i).getString(Constance.id) + "");
    //            if (i < mView.mSelectedLightSA.size() - 1) {
    //                goodsid.append(",");
    //            }
    //        }
    //
    //        diyContainerRl.setVisibility(View.INVISIBLE);
    //        //截图
    //        final Bitmap imageData = ImageUtil.compressImage(ImageUtil.takeScreenShot(mView));
    //
    //        diyContainerRl.setVisibility(View.VISIBLE);
    //        mView.setShowDialog(true);
    //        mView.setShowDialog("正在分享中...");
    //        mView.showLoading();
    //        final String url = NetWorkConst.FANGANUPLOAD;//地址
    //        final Map<String, String> params = new HashMap<String, String>();
    //        params.put("name", mTitle);
    //        params.put("goods_id", goodsid.toString());
    //        params.put("content", mContent);
    //        params.put("style", mStyle);
    //        params.put("space", mSpace);
    //
    //        final String imageName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + ".png";
    //        new Thread(new Runnable() { //开启线程上传文件
    //            @Override
    //            public void run() {
    //                final String resultJson = uploadFile(imageData, url, params, imageName);
    //                //分享的操作
    //                mView.runOnUiThread(new Runnable() {
    //                    @Override
    //                    public void run() {
    //                        mView.hideLoading();
    //                        if (AppUtils.isEmpty(resultJson)) {
    //                            MyToast.show(mView, "保存失败!");
    //                            return;
    //                        }
    //
    //                        com.alibaba.fastjson.JSONObject object = JSON.parseObject(resultJson);
    //                        int isResult = object.getInteger(Constance.error_code);
    //                        if (isResult != 0) {
    //                            MyToast.show(mView, "保存失败!");
    //                            return;
    //                        }
    //                        final String path = NetWorkConst.SCENE_HOST + object.getJSONObject(Constance.fangan).getString(Constance.path);
    //
    //                        new AlertView(null, null, "取消", null,
    //                                new String[]{"复制链接", "分享链接"},
    //                                mView, AlertView.Style.ActionSheet, new OnItemClickListener() {
    //                            @Override
    //                            public void onItemClick(Object o, int position) {
    //                                switch (position) {
    //                                    case 0:
    //                                        if (!mView.isToken()) {
    //                                            ClipboardManager cm = (ClipboardManager) mView.getSystemService(Context.CLIPBOARD_SERVICE);
    //                                            cm.setText(path);
    //                                        }
    //                                        break;
    //                                    case 1:
    //                                        String title = "来自 " + mTitle + " 方案的分享";
    //                                        ShareUtil.showShare(mView, title, path, mView.mSceenPath);
    //                                        break;
    //                                }
    //                            }
    //                        }).show();
    //
    //
    //                    }
    //                });
    //
    //            }
    //        }).start();
    //    }

    /**
     * 照相
     */
    public void goPhoto() {
        FileUtil.openImage(mView);
    }

    /**
     * 删除
     */
    public void goDetele() {
        mFrameLayout.removeView(mFrameLayout.findViewWithTag(IssueApplication.mLightIndex));
        if(OkHttpUtils.hashkNewwork()){
        mView.mSelectedLightSA.remove(IssueApplication.mLightIndex);
        }else {
            mView.mSelectedLightSA02.remove(IssueApplication.mLightIndex);
        }
        thiss.remove(mCurrentView);
        //        FrameLayout productFl = (FrameLayout) mFrameLayout.findViewWithTag(IssueApplication.mLightIndex);
        //        TouchView productTv = (TouchView) productFl.getChildAt(0);
        //        productTv.setss();
    }


    private boolean isGoCart = false;

    /**
     * 保存购物车
     */
    public void goShoppingCart() {
        CartDao dao = new CartDao(mView);
        if (OkHttpUtils.hashkNewwork()) {
            if (mView.mSelectedLightSA.size() == 0) {
                MyToast.show(mView, "请选择产品");
                return;
            }
            List<Goods> carts=dao.getAll();
            for (int i = 0; i < mView.mSelectedLightSA.size(); i++) {
                com.alibaba.fastjson.JSONObject object = mView.mSelectedLightSA.valueAt(i);
                Goods goods = new Goods();
                goods.setName(object.getString(Constance.name));
                goods.setId(object.getInteger(Constance.id));
//                goods.setBuyCount(1);
                goods.setBuyCount(1);
                goods.setGoods_number("1");
                for(int j=0;j<carts.size();j++){
                    if(carts.get(j).getId()==goods.getId()){
                        int x=carts.get(j).getBuyCount();
                        goods.setBuyCount(x+1);
                        goods.setGoods_number(""+(x+1));
                        break;
                    }
                }
//                goods.setGoods_number("1");
                goods.setShop_price(Float.parseFloat(object.getString(Constance.shop_price)));

                goods.setMarket_price(((object.getString(Constance.market_price))));
                goods.setImg_url(object.getString(Constance.img_url));

                if (-1 != dao.replaceOne(goods)) {
                    if (i == mView.mSelectedLightSA.size() - 1) {
                        Toast.makeText(mView, "已添加到购物车!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(mView, "添加到购物车失败!", Toast.LENGTH_LONG).show();
                    return;
                }
            }

        } else {
            if (mView.mSelectedLightSA02.size() == 0) {
                MyToast.show(mView, "请选择产品");
                return;
            }
            List<Goods> carts=dao.getAll();
            for (int i = 0; i < mView.mSelectedLightSA02.size(); i++) {
                Goods object = mView.mSelectedLightSA02.valueAt(i);

                Goods goods = object;
                goods.setBuyCount(1);
                goods.setGoods_number("1");
                for(int j=0;j<carts.size();j++){
                    if(carts.get(j).getId()==goods.getId()){
                        int x=carts.get(i).getBuyCount();
                        goods.setBuyCount(x+1);
                        goods.setGoods_number(""+(x+1));
                        break;
                    }
                }
                if (-1 != dao.replaceOne(goods)) {
                    if (i == mView.mSelectedLightSA02.size() - 1) {
                        Toast.makeText(mView, "已添加到购物车!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(mView, "添加到购物车失败!", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }


        IssueApplication.mCartCount = dao.getCount();
        EventBus.getDefault().post(Constance.CARTCOUNT);
    }

    /**
     * android上传文件到服务器
     *
     * @param file       需要上传的文件
     * @param RequestURL 请求的rul
     * @return 返回响应的内容
     */
    private String uploadFile(Bitmap file, String RequestURL, Map<String, String> param, String imageName) {
        String result = null;
        String BOUNDARY = UUID.randomUUID().toString();  //边界标识   随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data";   //内容类型
        // 显示进度框
        //      showProgressDialog();
        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true);  //允许输入流
            conn.setDoOutput(true); //允许输出流
            conn.setUseCaches(false);  //不允许使用缓存
            conn.setRequestMethod("POST");  //请求方式
            conn.setRequestProperty("Charset", CHARSET);  //设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            if (file != null) {
                /**
                 * 当文件不为空，把文件包装并且上传
                 */
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                StringBuffer sb = new StringBuffer();

                String params = "";
                if (param != null && param.size() > 0) {
                    Iterator<String> it = param.keySet().iterator();
                    while (it.hasNext()) {
                        sb = null;
                        sb = new StringBuffer();
                        String key = it.next();
                        String value = param.get(key);
                        sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                        sb.append("Content-Disposition: form-data; name=\"").append(key).append("\"").append(LINE_END).append(LINE_END);
                        sb.append(value).append(LINE_END);
                        params = sb.toString();
                        dos.write(params.getBytes());
                    }
                }
                sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                /**
                 * 这里重点注意：
                 * name里面的值为服务器端需要key   只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的   比如:abc.png
                 */
                sb.append("Content-Disposition: form-data; name=\"").append("file").append("\"")
                        .append(";filename=\"").append(imageName).append("\"\n");
                sb.append("Content-Type: image/png");
                sb.append(LINE_END).append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = ImageUtil.Bitmap2InputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }


                is.close();
                //                dos.write(file);
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                dos.write(end_data);

                dos.flush();
                /**
                 * 获取响应码  200=成功
                 * 当响应成功，获取响应的流
                 */

                int res = conn.getResponseCode();
                System.out.println("res=========" + res);
                if (res == 200) {
                    InputStream input = conn.getInputStream();
                    StringBuffer sb1 = new StringBuffer();
                    int ss;
                    while ((ss = input.read()) != -1) {
                        sb1.append((char) ss);
                    }
                    result = sb1.toString();
                } else {
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 选产品
     */
    public void selectProduct() {
        mSelectType = 0;
        data_rl.setVisibility(View.VISIBLE);
        goodses = null;
        mGoodsList = null;
        filter_attr = "";
        filter_attr02 = "";
        ll_search.setVisibility(View.VISIBLE);
        keyword = "";
        mProAdapter.notifyDataSetChanged();
        page = 1;
        goodsIds = new Integer[]{0, 0, 0,0};
        dropDownMenu.setFixedTabIndicatorShow(false);
        initFilterDropDownView = true;

        //        if (!AppUtils.isEmpty(goodsAllAttrs)) {
        //            if (initFilterDropDownView)//重复setMenuAdapter会报错
        //                initFilterDropDownView(goodsAllAttrs);
        //        }

//        if (!OkHttpUtils.hashkNewwork()) {
//            pd.setVisibility(View.VISIBLE);
//            mNetWork.sendGoodsList("0", page, 0, keyword, null, filter_attr, true, 1, "",mView, this);
//        }
        if(!OkHttpUtils.hashkNewwork()){
            String object=MyShare.get(mView).getString(Constance.filter_attr);
            if(TextUtils.isEmpty(object)){
                return;
            }
            goodsAllAttrs= JSON.parseObject(object).getJSONArray(Constance.all_attr_list);
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        Thread.sleep(1000);
                        mView.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (initFilterDropDownView)//重复setMenuAdapter会报错
                                    initFilterDropDownView(goodsAllAttrs);
                                dropDownMenu.setFixedTabIndicatorShow(true);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }

        sendGoodsList("0", 0, false);

        //        sendAttrList();

    }


    /**
     * 获取产品列表
     *
     * @param c_id
     * @param okcat_id
     */
    public void sendGoodsList(final String c_id, int okcat_id, boolean isHidepd) {
        //
        if (OkHttpUtils.hashkNewwork()) {
            mNetWork.sendGoodsList(c_id, page, okcat_id, keyword, null, filter_attr, false, 20, "",mView, this);
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final List<Goods> goodsList = GoodsDao.getGoodsList(keyword, c_id, filter_attr02, null, 20, page);

                    mView.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.hideLoading();

                            if (null == mView || mView.isFinishing())
                                return;
                            if (null != mPullToRefreshLayout) {
                                dismissRefesh();
                            }

                            if (goodsList.size() == 0) {
                                if (page == 1) {
                                    MyToast.show(mView, "数据查询不到1");
                                    mGoodsList = new ArrayList<Goods>();
                                } else {
                                    MyToast.show(mView, "数据已经到底啦!");
                                }
                                dismissRefesh();
                                return;
                            }
                            getDataSuccess02(goodsList);
                        }
                    });

                }
            }).start();
            if (isHidepd) {
                pd.setVisibility(View.GONE);
            }
            //
        }


    }


    @Override
    public void onSuccessListener(int what, com.alibaba.fastjson.JSONObject ans) {
        pd.setVisibility(View.GONE);

        //        if (!OkHttpUtils.hashkNewwork()) {
        //            if (AppUtils.isEmpty(goodsAllAttrs)) {
        //                goodsAllAttrs = ans.getJSONArray(Constance.all_attr_list);
        //                if (initFilterDropDownView)//重复setMenuAdapter会报错
        //                    initFilterDropDownView(goodsAllAttrs);
        //            }
        //            return;
        //        }

        if (null == mView || mView.isFinishing())
            return;

        if (null != mPullToRefreshLayout) {
            dismissRefesh();
        }
        com.alibaba.fastjson.JSONArray goodsList = ans.getJSONArray(Constance.goodslist);

        dropDownMenu.setFixedTabIndicatorShow(true);
        if(goodsAllAttrs==null||goodsAllAttrs.size()==0){
        goodsAllAttrs = ans.getJSONArray(Constance.all_attr_list);
        }
        if (initFilterDropDownView)//重复setMenuAdapter会报错
            initFilterDropDownView(goodsAllAttrs);


        if (AppUtils.isEmpty(goodsList)) {
            if (page == 1) {
                MyToast.show(mView, "数据查询不到");
                goodses = new com.alibaba.fastjson.JSONArray();
            } else {
                MyToast.show(mView, "数据已经到底啦!");
            }
            //

            dismissRefesh();
            return;
        }

        getDataSuccess(goodsList);

    }

    @Override
    public void onFailureListener(int what, com.alibaba.fastjson.JSONObject ans) {
        pd.setVisibility(View.GONE);
        page--;

        if (null != mPullToRefreshLayout) {
            mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        }
    }


    /**
     * 选场景
     */
    public void selectScreen() {

        SelectScreenPopWindow popWindow = new SelectScreenPopWindow(mView, mView);
        popWindow.onShow(main_fl);
        popWindow.setListener(new ISelectScreenListener() {
            @Override
            public void onSelectScreenChanged(int type) {
                if(type==0){
                    PermissionUtils.requestPermission(mView, PermissionUtils.CODE_READ_EXTERNAL_STORAGE, new PermissionUtils.PermissionGrant() {
                        @Override
                        public void onPermissionGranted(int requestCode) {
                            data_rl.setVisibility(View.VISIBLE);
                            goodses = null;
                            mGoodsList = null;
                            keyword = "";
                            mProAdapter.notifyDataSetChanged();
                            mSelectType = 1;
                            page = 1;
                            filter_attr = "";
                            sceneIds = new Integer[]{0, 0};
                            initFilterDropDownView = true;
                            dropDownMenu.setFixedTabIndicatorShow(true);
                            ll_search.setVisibility(View.GONE);
                            if(!OkHttpUtils.hashkNewwork()){
                                sceneAllAttrs= JSON.parseObject(MyShare.get(mView).getString(Constance.scene_all_attr_list)).getJSONArray(Constance.all_attr_list);
                                if(sceneAllAttrs!=null&&sceneAllAttrs.size()>0){

                                    for(int i=0;i<sceneAllAttrs.size();i++){
                                        if(!sceneAllAttrs.getJSONObject(i).getString(Constance.filter_attr_name).equals("空间")&&
                                                !sceneAllAttrs.getJSONObject(i).getString(Constance.filter_attr_name).equals("风格")){
                                            sceneAllAttrs.remove(i);
                                            i--;
                                        }
                                    }
                                }
                                if (initFilterDropDownView)//重复setMenuAdapter会报错
                                    initFilterDropDownView(sceneAllAttrs);
                            }
                            pd.setVisibility(View.VISIBLE);
                            sendSceneList();

                        }
                    });
                }else{
                    TwoCodePopWindow popWindow = new TwoCodePopWindow(mView, mView);
                    popWindow.onShow(mFrameLayout);
                    popWindow.setListener(new ITwoCodeListener() {
                        @Override
                        public void onTwoCodeChanged(String path) {
                            if (AppUtils.isEmpty(path))
                                return;
                            mView.mSceenPath = path;
                            displaySceneBg(mView.mSceenPath);
                        }
                    });
                }
            }
        });

//        new AlertView(null, null, "取消", null,
//                new String[]{"场景", "请上传自家场景"},
//                mView, AlertView.Style.ActionSheet, new OnItemClickListener() {
//            @Override
//            public void onItemClick(Object o, int position) {
//                switch (position) {
//                    case 0:
//
//                        break;
//                    case 1:
//
//                        break;
//                }
//            }
//        }).show();

    }

    /**
     * 场景列表
     */
    public void sendSceneList() {
        if(!OkHttpUtils.hashkNewwork()){
            pd.setVisibility(View.GONE);
            List<SceneBean> temp= SceneDao.getSceneList(filter_attr,page);
            if (AppUtils.isEmpty(temp)) {
                MyToast.show(mView, "数据已经到底啦!");
                return;
            }
            getSceneDataSuccess02(temp);
            return;
        }
        mNetWork.sendSceneList("0", page, null, null, filter_attr, mView, new HttpListener() {
            @Override
            public void onSuccessListener(int what, com.alibaba.fastjson.JSONObject ans) {
                pd.setVisibility(View.GONE);
                Log.v("520it", "aaa");
                if (null == mView || mView.isFinishing())
                    return;

                if (null != mPullToRefreshLayout) {
                    dismissRefesh();
                }
                com.alibaba.fastjson.JSONArray sceneList = ans.getJSONArray(Constance.scenelist);
                if (AppUtils.isEmpty(sceneList)) {
                    if (page == 1) {

                    }
                    MyToast.show(mView, "数据已经到底啦!");
                    dismissRefesh();
                    return;
                }
                Log.v("520it", "eee");
                if (AppUtils.isEmpty(sceneAllAttrs)) {
                    sceneAllAttrs = ans.getJSONArray(Constance.all_attr_list);
                    if (initFilterDropDownView) {//重复setMenuAdapter会报错
                        Log.v("520it", "ccc");
                        dropDownMenu.setFixedTabIndicatorShow(true);
                        initFilterDropDownView(sceneAllAttrs);
                    }
                }
                //                sceneAllAttrs = ans.getJSONArray(Constance.all_attr_list);
                //                if (initFilterDropDownView) {//重复setMenuAdapter会报错
                //                    Log.v("520it", "ccc");
                //                    dropDownMenu.setFixedTabIndicatorShow(true);
                //                    initFilterDropDownView(sceneAllAttrs);
                //                }


                getDataSuccess(sceneList);
            }

            @Override
            public void onFailureListener(int what, com.alibaba.fastjson.JSONObject ans) {
                pd.setVisibility(View.GONE);
                page--;

                if (null != mPullToRefreshLayout) {
                    mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                    mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
            }
        });
    }

    private void getSceneDataSuccess02(List<SceneBean> temp) {
        if (1 == page)
            sceneBeans = temp;
        else if (null != temp) {
            for (int i = 0; i < temp.size(); i++) {
                sceneBeans.add(temp.get(i));
            }

            if (AppUtils.isEmpty(temp))
                MyToast.show(mView, "没有更多内容了");
        }
        mProAdapter.notifyDataSetChanged();
    }


    public void ActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == mView.RESULT_OK) { // 返回成功
            switch (requestCode) {
                case Constance.PHOTO_WITH_CAMERA: {// 拍照获取图片
                    String status = Environment.getExternalStorageState();
                    if (status.equals(Environment.MEDIA_MOUNTED)) { // 是否有SD卡
                        File imageFile = new File(IssueApplication.cameraPath, IssueApplication.imagePath + ".jpg");
                        if (imageFile.exists()) {
                            imageURL = "file://" + imageFile.toString();
                            IssueApplication.imagePath = null;
                            IssueApplication.cameraPath = null;
                            mView.mSceenPath = imageURL;
                            displaySceneBg(imageURL);
                        } else {
                            AppDialog.messageBox("读取图片失败！");
                        }
                    } else {
                        AppDialog.messageBox("没有SD卡！");
                    }
                    break;
                }
                case Constance.PHOTO_WITH_DATA: // 从图库中选择图片
                    // 照片的原始资源地址
                    imageURL = data.getData().toString();
                    mView.mSceenPath = imageURL;
                    displaySceneBg(imageURL);
                    break;
            }
        } else if (requestCode == Constance.FROMDIY) {
            if (AppUtils.isEmpty(data))
                return;
            mView.mGoodsObject = (com.alibaba.fastjson.JSONObject) data.getSerializableExtra(Constance.product);
            displayCheckedGoods(mView.mGoodsObject);
        } else if (requestCode == Constance.FROMDIY02) {
            if (AppUtils.isEmpty(data))
                return;
            mView.mSceenPath = data.getStringExtra(Constance.SCENE);
            if (!AppUtils.isEmpty(mView.mSceenPath)) {
                displaySceneBg(mView.mSceenPath);
            }
        } else if (requestCode == Constance.FROMSCHEME) {
            if (AppUtils.isEmpty(data))
                return;
            if (!AppUtils.isEmpty(mCurrentView))
                mCurrentView.setInEdit(false);
            mStyle = data.getStringExtra(Constance.style);
            mSpace = data.getStringExtra(Constance.space);
            mContent = data.getStringExtra(Constance.content);
            mTitle = data.getStringExtra(Constance.title);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                        mView.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getShareDiy(1);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();


        }
    }

    private void initImageLoader() {
        options = new DisplayImageOptions.Builder()
                // 设置图片下载期间显示的图片
                .showImageOnLoading(R.drawable.bg_default)
                        // 设置图片Uri为空或是错误的时候显示的图片
                .showImageForEmptyUri(R.drawable.bg_default)
                        // 设置图片加载或解码过程中发生错误显示的图片
                .showImageOnFail(R.drawable.bg_default)
                        // 设置下载的图片是否缓存在内存中
                .cacheInMemory(false)
                        //设置图片的质量
                .bitmapConfig(Bitmap.Config.RGB_565)
                        // 设置下载的图片是否缓存在SD卡中
                .cacheOnDisk(true)
                        // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                        // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                        //                .considerExifParams(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)// 设置图片可以放大（要填满ImageView必须配置memoryCacheExtraOptions大于Imageview）
                        // 图片加载好后渐入的动画时间
                        // .displayer(new FadeInBitmapDisplayer(100))
                .build(); // 构建完成

        // 得到ImageLoader的实例(使用的单例模式)
        imageLoader = ImageLoader.getInstance();
    }

    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private int mLightNumber = -1;// 点出来的灯的编号
    private int mLightId = 0;//点出来的灯的序号
    private int leftMargin = 0;

    private void displayCheckedGoods(final com.alibaba.fastjson.JSONObject goods) {
        if (AppUtils.isEmpty(goods))
            return;

        int type = 0;
        String path = FileUtil.getGoodsExternDir(goods.getString(Constance.img_url));
        final File imageFile = new File(path);
//        if (imageFile.exists()) {
//            path = "file://" + imageFile.toString();
//            type = 0;
//
//        } else {
//
//        }
        path = NetWorkConst.UR_PRODUCT_URL + goods.getString(Constance.img_url);
        type = 1;
        final int finalType = type;
        final String finalPath = path;
//        PgyCrashManager.reportCaughtException(mView,new Exception("hasNet,displayCheckedGoods:"+path));
        imageLoader.loadImage(path, options,
                new SimpleImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        pd2.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view,
                                                FailReason failReason) {
                        pd2.setVisibility(View.GONE);
                        MyToast.show(mView, failReason.getCause() + "请重试！");
                    }


                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        pd2.setVisibility(View.GONE);
                        // 被点击的灯的编号加1
                        mLightNumber++;
                        // 把点击的灯放到集合里
                        mView.mSelectedLightSA.put(mLightNumber, goods);
//                        PgyCrashManager.reportCaughtException(mView,new Exception("hasNet,onLoadingComplete:"+imageUri));
                        addStickerView(loadedImage);
//                        if (finalType == 0) {
//                            SetTouchView(loadedImage, imageFile.toString());

//                        } else {
//                            SetTouchView(loadedImage, finalPath);
//                        }


                    }

                });
    }

    private void displayCheckedGoods02(final Goods goods) {
        if (AppUtils.isEmpty(goods))
            return;


        //        String path = FileUtil.getGoodsExternDir(goods.getImg_url());
        //        File imageFile = new File(path);
        //        if (imageFile.exists()) {
        //            path = "file://" + imageFile.toString();
        //
        //        } else {
        //            path = NetWorkConst.UR_PRODUCT_URL + goods.getImg_url();
        //        }

        int type = 0;
        String path = FileUtil.getGoodsExternDir(goods.getImg_url());
        final File imageFile = new File(path);
        if (imageFile.exists()) {
            path = "file://" + imageFile.toString();
            type = 0;
        } else {
            path = NetWorkConst.UR_PRODUCT_URL + goods.getImg_url();
            type = 1;
        }

        final int finalType = type;
        final String finalPath = path;
//        PgyCrashManager.reportCaughtException(mView,new Exception("display02,"+path));
        imageLoader.loadImage(path, options,
                new SimpleImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        pd2.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view,
                                                FailReason failReason) {
                        pd2.setVisibility(View.GONE);
                        MyToast.show(mView, failReason.getCause() + "请重试！");
                    }


                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        pd2.setVisibility(View.GONE);
                        // 被点击的灯的编号加1
                        mLightNumber++;
                        // 把点击的灯放到集合里
                        mView.mSelectedLightSA02.put(mLightNumber, goods);
//                        PgyCrashManager.reportCaughtException(mView,new Exception("display02,loadingComplete"+imageUri));
                        addStickerView(loadedImage);
//                        if (finalType == 0) {
//                            SetTouchView(loadedImage, imageFile.toString());
//                        } else {
//                            SetTouchView(loadedImage, finalPath);
//                        }
                    }
                });
    }

    private void displayCheckedGoods03(String path) {
        if (AppUtils.isEmpty(path))
            return;
        imageLoader.loadImage(path, options,
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        pd2.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view,
                                                FailReason failReason) {
                        pd2.setVisibility(View.GONE);
                        MyToast.show(mView, failReason.getCause() + "请重试！");
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        pd2.setVisibility(View.GONE);
                        // 被点击的灯的编号加1
                        mLightNumber++;
                        // 把点击的灯放到集合里

                        //                        // 设置灯图的ImageView的初始宽高和位置
                        //                        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                        //                                mScreenWidth / 3 * 2 / 3,
                        //                                (mScreenWidth / 3 * 2 / 3 * loadedImage.getHeight()) / loadedImage.getWidth());
                        //                        // 设置灯点击出来的位置
                        //                        if (mView.mSelectedLightSA.size() == 1) {
                        //                            leftMargin = mScreenWidth / 3 * 2 / 3;
                        //                        } else if (mView.mSelectedLightSA.size() == 2) {
                        //                            leftMargin = mScreenWidth / 3 * 2 / 3 * 2;
                        //                        } else if (mView.mSelectedLightSA.size() == 3) {
                        //                            leftMargin = 0;
                        //                        }
                        //                        lp.setMargins(leftMargin, 0, 0, 0);
                        // 设置灯图的ImageView的初始宽高和位置
                        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                                mScreenWidth / 3 * 2 / 3,
                                (mScreenWidth / 3 * 2 / 3 * loadedImage.getHeight()) / loadedImage.getWidth());
                        // 设置灯点击出来的位置
                        if (mView.mSelectedLightSA.size() == 1) {
                            leftMargin = mScreenWidth / 3 * 2 / 3;
                        } else if (mView.mSelectedLightSA.size() == 2) {
                            leftMargin = mScreenWidth / 3 * 2 / 3 * 2;
                        } else if (mView.mSelectedLightSA.size() == 3) {
                            leftMargin = 0;
                        }
                        lp.setMargins(mScreenWidth / 2 - (mScreenWidth / 3 * 2 / 6), 20, 0, 0);


                        TouchView02 touchView = new TouchView02(mView);
                        touchView.setLayoutParams(lp);
                        touchView.setImageBitmap(loadedImage);// 设置被点击的灯的图片
                        touchView.setmLightCount(mLightNumber);// 设置被点击的灯的编号
                        touchView.setTag(mLightNumber);
                        FrameLayout.LayoutParams newLp = new FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.MATCH_PARENT);

                        FrameLayout newFrameLayout = new FrameLayout(mView);
                        newFrameLayout.setLayoutParams(newLp);
                        newFrameLayout.addView(touchView);
                        newFrameLayout.setTag(mLightNumber);
                        mFrameLayout.addView(newFrameLayout);

                        touchView.setContainer(mFrameLayout, newFrameLayout);


                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        pd2.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * 添加图片
     *
     * @param bitmap
     */
    private void addImageView(Bitmap bitmap) {
        pd2.setVisibility(View.GONE);
        // 被点击的灯的编号加1
        mLightNumber++;
        // 把点击的灯放到集合里
        //        mView.mSelectedLightSA.put(mLightNumber, goods);

        //        // 设置灯图的ImageView的初始宽高和位置
        //        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
        //                mScreenWidth / 3 * 2 / 3,
        //                (mScreenWidth / 3 * 2 / 3 * bitmap.getHeight()) / bitmap.getWidth());
        //        // 设置灯点击出来的位置
        //        if (mView.mSelectedLightSA.size() == 1) {
        //            leftMargin = mScreenWidth / 3 * 2 / 3;
        //        } else if (mView.mSelectedLightSA.size() == 2) {
        //            leftMargin = mScreenWidth / 3 * 2 / 3 * 2;
        //        } else if (mView.mSelectedLightSA.size() == 3) {
        //            leftMargin = 0;
        //        }
        //        lp.setMargins(leftMargin, 0, 0, 0);
        // 设置灯图的ImageView的初始宽高和位置
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                mScreenWidth / 3 * 2 / 3,
                (mScreenWidth / 3 * 2 / 3 * bitmap.getHeight()) / bitmap.getWidth());
        // 设置灯点击出来的位置
        if (mView.mSelectedLightSA.size() == 1) {
            leftMargin = mScreenWidth / 3 * 2 / 3;
        } else if (mView.mSelectedLightSA.size() == 2) {
            leftMargin = mScreenWidth / 3 * 2 / 3 * 2;
        } else if (mView.mSelectedLightSA.size() == 3) {
            leftMargin = 0;
        }
        lp.setMargins(mScreenWidth / 2 - (mScreenWidth / 3 * 2 / 6), 20, 0, 0);


        TouchView02 touchView = new TouchView02(mView);
        touchView.setLayoutParams(lp);
        touchView.setImageBitmap(bitmap);// 设置被点击的灯的图片
        touchView.setmLightCount(mLightNumber);// 设置被点击的灯的编号
        touchView.setTag(mLightNumber);
        FrameLayout.LayoutParams newLp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);

        FrameLayout newFrameLayout = new FrameLayout(mView);
        newFrameLayout.setLayoutParams(newLp);
        newFrameLayout.addView(touchView);
        newFrameLayout.setTag(mLightNumber);
        mFrameLayout.addView(newFrameLayout);

        touchView.setContainer(mFrameLayout, newFrameLayout);
    }


    /**
     * 加载场景背景图
     */

    private void SetTouchView(Bitmap loadedImage, String path) {
        // 设置灯图的ImageView的初始宽高和位置
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                mScreenWidth / 3 * 2 / 3,
                (mScreenWidth / 3 * 2 / 3 * loadedImage.getHeight()) / loadedImage.getWidth());
        // 设置灯点击出来的位置
        if (mView.mSelectedLightSA.size() == 1) {
            leftMargin = mScreenWidth / 3 * 2 / 3;
        } else if (mView.mSelectedLightSA.size() == 2) {
            leftMargin = mScreenWidth / 3 * 2 / 3 * 2;
        } else if (mView.mSelectedLightSA.size() == 3) {
            leftMargin = 0;
        }
        lp.setMargins(mScreenWidth / 2 - (mScreenWidth / 3 * 2 / 6), 20, 0, 0);

        TouchView touchView = new TouchView(mView);
        touchView.setLayoutParams(lp);
        touchView.setImageBitmap(loadedImage);// 设置被点击的灯的图片
        touchView.setmLightCount(mLightNumber);// 设置被点击的灯的编号
        touchView.setTag(mLightNumber);
        FrameLayout.LayoutParams newLp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        FrameLayout newFrameLayout = new FrameLayout(mView);
        newFrameLayout.setLayoutParams(newLp);
        newFrameLayout.addView(touchView);
        newFrameLayout.setTag(mLightNumber);
        touchView.path = path;
        //                        newFrameLayout.setOnClickListener(new View.OnClickListener() {
        //                            @Override
        //                            public void onClick(View v) {
        //                                botton_ll.setVisibility(View.VISIBLE);
        //                            }
        //                        });
        mFrameLayout.addView(newFrameLayout);

        touchView.setContainer(mFrameLayout, newFrameLayout);
    }

    private List<View> thiss;
    //当前处于编辑状态的贴纸
    private StickerView mCurrentView;
    //添加表情
    private void addStickerView(Bitmap bitmap) {

        final StickerView stickerView = new StickerView(mView);
        stickerView.setBitmap(ImageUtil.changeBitmapSize(bitmap));
        stickerView.mLightCount = mLightNumber;
        stickerView.setOperationListener(new StickerView.OperationListener() {
            @Override
            public void onDeleteClick() {
                thiss.remove(stickerView);
                mFrameLayout.removeView(stickerView);
                if(OkHttpUtils.hashkNewwork()){
                mView.mSelectedLightSA.remove(IssueApplication.mLightIndex);
                }else {
                    mView.mSelectedLightSA02.remove(IssueApplication.mLightIndex);
                }
            }

            @Override
            public void onEdit(StickerView stickerView) {
                mCurrentView.setInEdit(false);
                mCurrentView = stickerView;
                mCurrentView.setInEdit(true);
            }

            @Override
            public void onTop(StickerView stickerView) {
                int position = thiss.indexOf(stickerView);
                if (position == thiss.size() - 1) {
                    return;
                }
                StickerView stickerTemp = (StickerView) thiss.remove(position);
                thiss.add(thiss.size(), stickerTemp);
            }
        });
        FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        stickerView.setTag(mLightNumber);
        mFrameLayout.addView(stickerView, lp2);
        thiss.add(stickerView);
        setCurrentEdit(stickerView);
    }

    /**
     * 设置当前处于编辑模式的贴纸
     */
    private void setCurrentEdit(StickerView stickerView) {
        if (mCurrentView != null) {
            mCurrentView.setInEdit(false);
        }
        mCurrentView = stickerView;
        stickerView.setInEdit(true);
    }



    private Bitmap mSceneBg;

    public void displaySceneBg(String path) {
        imageURL = path;
//        PgyCrashManager.reportCaughtException(mView,new Exception("diy"+path));
        imageLoader.displayImage(path, sceneBgIv, options,
                new ImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        pd2.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view,
                                                FailReason failReason) {
                        pd2.setVisibility(View.GONE);
                        MyToast.show(mView, failReason.getCause() + "请重试！");
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap loadedImage) {
                        pd2.setVisibility(View.GONE);
                        if(!AppUtils.isEmpty(mSceneBg))
                            mSceneBg.recycle();
                        mSceneBg = ImageUtil.drawable2Bitmap(sceneBgIv.getDrawable());
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        pd2.setVisibility(View.GONE);
                    }
                });

    }

    /**
     * 判断是否满屏
     */
    public void selectIsFullScreen() {
        brigth_rl.setVisibility(View.GONE);
        data_rl.setVisibility(View.GONE);
        sceneAllAttrs = null;
        dropDownMenu.close();
        filter_attr = "";
        if (isFullScreen) {
            diyContainerRl.setVisibility(View.VISIBLE);
            isFullScreen = false;
        }
        if (AppUtils.isEmpty(mCurrentView))
            return;
        mCurrentView.setInEdit(false);
    }

    public void setIsFullScene() {
        if (!isFullScreen) {
            diyContainerRl.setVisibility(View.INVISIBLE);
            isFullScreen = true;
        }
    }

    /**
     * 获取购物车列表
     */
    public void sendShoppingCart() {
        mNetWork.sendShoppingCart(this);
    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {

        switch (requestCode) {
            case NetWorkConst.ADDCART:
                if (isGoCart) {
                    mView.hideLoading();
                    MyToast.show(mView, "加入购物车成功!");
                    sendShoppingCart();
                }
                break;
            case NetWorkConst.GETCART:
                if (ans.getJSONArray(Constance.goods_groups).length() > 0) {
                    IssueApplication.mCartCount = ans.getJSONArray(Constance.goods_groups)
                            .getJSONObject(0).getJSONArray(Constance.goods).length();
                } else {
                    IssueApplication.mCartCount = 0;
                }
                EventBus.getDefault().post(Constance.CARTCOUNT);
                break;

            case NetWorkConst.PRODUCT:
                //                pd.setVisibility(View.GONE);
                //                if (null == mView || mView.isFinishing())
                //                    return;
                //
                //                if (null != mPullToRefreshLayout) {
                //                    dismissRefesh();
                //                }
                //                com.alibaba.fastjson.JSONArray goodsList = ans.getJSONArray(Constance.goodsList);
                //                if (AppUtils.isEmpty(goodsList)) {
                //                    if (page == 1) {
                //
                //                    }
                //                    goodses = new com.alibaba.fastjson.JSONArray();
                //                    dismissRefesh();
                //                    return;
                //                }
                //
                //                getDataSuccess(goodsList);
                //
                //                break;
            case NetWorkConst.SCENECATEGORY:
                //                pd.setVisibility(View.GONE);
                //                sceneAllAttrs = ans.getJSONArray(Constance.categories);
                //                if (initFilterDropDownView)//重复setMenuAdapter会报错
                //                    initFilterDropDownView(sceneAllAttrs);
                //                break;

            case NetWorkConst.SCENELIST:
                //                if (null == mView || mView.isFinishing())
                //                    return;
                //
                //                if (null != mPullToRefreshLayout) {
                //                    dismissRefesh();
                //                }
                //                JSONArray sceneList = ans.getJSONArray(Constance.scene);
                //                if (AppUtils.isEmpty(sceneList)) {
                //                    if (page == 1) {
                //
                //                    }
                //                    MyToast.show(mView, "数据已经到底啦!");
                //                    dismissRefesh();
                //                    return;
                //                }
                //
                //                getDataSuccess(sceneList);

                break;
            case NetWorkConst.ADDLIKEDPRODUCT:
                mView.hideLoading();
                MyToast.show(mView, "加入成功!");
                break;
            case NetWorkConst.ATTRLIST:
                //                sceneAllAttrs = ans.getJSONArray(Constance.goods_attr_list);
                //                if (initFilterDropDownView)//重复setMenuAdapter会报错
                //                    initFilterDropDownView(sceneAllAttrs);
                //                break;
        }
    }


    private void dismissRefesh() {
        mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }

    private void getDataSuccess(com.alibaba.fastjson.JSONArray array) {
        if (1 == page)
            goodses = array;
        else if (null != goodses) {
            for (int i = 0; i < array.size(); i++) {
                goodses.add(array.getJSONObject(i));
            }

            if (AppUtils.isEmpty(array))
                MyToast.show(mView, "没有更多内容了");
        }
        mProAdapter.notifyDataSetChanged();
    }

    private void getDataSuccess02(List<Goods> array) {
        if (1 == page)
            mGoodsList = array;
        else if (null != mGoodsList) {
            for (int i = 0; i < array.size(); i++) {
                mGoodsList.add(array.get(i));
            }

            if (AppUtils.isEmpty(array))
                MyToast.show(mView, "没有更多内容了");
        }
        mProAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        pd.setVisibility(View.GONE);
    }

    /**
     * 背景图镜像
     */
    public void sendBackgroudImage() {
        ImageUtil.changeLight(sceneBgIv, 0);
        Bitmap mBitmap = ImageUtil.drawable2Bitmap(sceneBgIv.getDrawable());
        if (mBitmap != null) {

            Bitmap temp = ImageUtil.convertBmp(mBitmap);
            if (temp != null) {
                sceneBgIv.setImageBitmap(temp);
                mBitmap.recycle();
            }
            ImageUtil.changeLight(sceneBgIv, mSeekNum - 50);
        }

    }


    /**
     * 产品镜像
     */
    public void sendProductImage() {
        try {
            FrameLayout productFl = (FrameLayout) mFrameLayout.findViewWithTag(IssueApplication.mLightIndex);
            TouchView productTv = (TouchView) productFl.getChildAt(0);
            ImageUtil.changeLight(productTv, 0);
            Bitmap mBitmap = ImageUtil.drawable2Bitmap(productTv.getDrawable());
            if (mBitmap != null) {

                Bitmap temp = ImageUtil.convertBmp(mBitmap);
                if (temp != null) {
                    productTv.setImageBitmap(temp);
                    mBitmap.recycle();
                }
                ImageUtil.changeLight(productTv, mSeekNum - 50);
            }
        } catch (Exception e) {

        }

    }

    /**
     * 调节图片亮度
     */
    public void goBrightness() {
        brigth_rl.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(!IssueApplication.IsRoot()){
            SystemClock.sleep(IssueApplication.SLEEP_TIME);
        }

        if (mSelectType == 0) {
            if (OkHttpUtils.hashkNewwork()) {
                mView.mGoodsObject = goodses.getJSONObject(position);
                displayCheckedGoods(mView.mGoodsObject);
            } else {
                mView.mGoodsObject02 = mGoodsList.get(position);
                displayCheckedGoods02(mView.mGoodsObject02);
            }

        } else {

//            String path1 = FileUtil.getSceenExternDir(goodses.getJSONObject(position).getString(Constance.path));
//            File imageFile = new File(path1);
//            if (imageFile.exists()) {
//                imageURL = "file://" + imageFile.toString();
//                mView.mSceenPath = imageURL;
//                ImageLoader.getInstance().displayImage(imageURL, sceneBgIv);
//                if(!AppUtils.isEmpty(mSceneBg))
//                    mSceneBg.recycle();
//                mSceneBg = BitmapFactory.decodeFile(imageFile.toString());
//            } else {
//
//            }
            if(!OkHttpUtils.hashkNewwork()){
                mView.mSceenPath="file://"+new File(FileUtil.getSceenExternDir(sceneBeans.get(position).getPath())).toString();
            }else {
                int mId= Integer.parseInt(goodses.getJSONObject(position).getString(Constance.id));
                if(mId<=1551){
                    mView.mSceenPath =NetWorkConst.URL_SCENE+goodses.getJSONObject(position).getString(Constance.path);
                }else {
                    mView.mSceenPath  = NetWorkConst.UR_SCENE_URL + goodses.getJSONObject(position).getString(Constance.path);
                }
            }


//            mView.mSceenPath = NetWorkConst.UR_SCENE_URL + goodses.getJSONObject(position).getString(Constance.path);
            displaySceneBg(mView.mSceenPath);
        }
    }

    private List<Integer> itemPosList;//有选中值的itemPos列表，长度为3

    private void initFilterDropDownView(com.alibaba.fastjson.JSONArray sceneAllAttrs) {
        itemPosList = new ArrayList<>();

        if (mSelectType == 0) {
            ProductDropMenuAdapter dropMenuAdapter = new ProductDropMenuAdapter(mView, sceneAllAttrs, itemPosList, this);
            dropDownMenu.setMenuAdapter(dropMenuAdapter, true);
            if (itemPosList.size() < sceneAllAttrs.size()) {
                itemPosList.add(0);
                itemPosList.add(0);
                 itemPosList.add(0);
                itemPosList.add(0);
            }
        } else {
            if (itemPosList.size() < sceneAllAttrs.size()) {
                itemPosList.add(0);
                itemPosList.add(0);
//            itemPosList.add(0);
            }
            for(int i=0;i<sceneAllAttrs.size();i++){
                if(!sceneAllAttrs.getJSONObject(i).getString(Constance.filter_attr_name).contains("风格")&&
                        !sceneAllAttrs.getJSONObject(i).getString(Constance.filter_attr_name).contains("空间")){
                    sceneAllAttrs.remove(i);
                    i--;
                }
            }

            SceneDropMenuAdapter dropMenuAdapter = new SceneDropMenuAdapter(mView, sceneAllAttrs, itemPosList, this);
            dropDownMenu.setMenuAdapter(dropMenuAdapter, true);

        }

    }

    private String[] goodsNames = new String[]{"", "", "",""};
    private String[] sceneIdsStr = new String[]{"",""};
    @Override
    public void onFilterDone(int titlePos, int itemPos, String itemStr) {
        dropDownMenu.close();

        if (mSelectType == 0) {
            if (0 == itemPos)
                itemStr = goodsAllAttrs.getJSONObject(titlePos).getString(Constance.filter_attr_name);
            dropDownMenu.setPositionIndicatorText(titlePos, itemStr);
            Log.v("520it", itemStr);

            if (titlePos < itemPosList.size())
                itemPosList.remove(titlePos);
            itemPosList.add(titlePos, itemPos);

            if (OkHttpUtils.hashkNewwork()) {
                if (AppUtils.isEmpty(goodsAllAttrs.getJSONObject(titlePos).getJSONArray(Constance.attr_list).getJSONObject(itemPos).getInteger(Constance.goods_id))) {
                    goodsIds[titlePos] = 0;
                } else {
                    int index = goodsAllAttrs.getJSONObject(titlePos).getJSONArray(Constance.attr_list).getJSONObject(itemPos).getInteger(Constance.goods_id);
                    if (index == 0) {
                        goodsIds[titlePos] = 999;
                    } else {
                        goodsIds[titlePos] = index;
                    }
                }

                filter_attr = goodsIds[0] + "." + goodsIds[1] + "." + goodsIds[2]+"."+goodsIds[3];

                if (AppUtils.isEmpty(filter_attr))
                    return;
            } else {

                //                if (0 == itemPos)
                //                    itemStr = goodsAllAttrs.getJSONObject(titlePos).getString(Constance.filter_attr_name);
                //                dropDownMenu.setPositionIndicatorText(titlePos, itemStr);


                if (AppUtils.isEmpty(goodsAllAttrs.getJSONObject(titlePos).getJSONArray(Constance.attr_list).getJSONObject(itemPos).getInteger(Constance.goods_id))) {
                    goodsNames[titlePos] = "";
                } else {
                    String indexValue = goodsAllAttrs.getJSONObject(titlePos).getJSONArray(Constance.attr_list).getJSONObject(itemPos).getString(Constance.attr_value);
                    goodsNames[titlePos] = indexValue;
                }
                filter_attr02 = goodsNames[0] + "." + goodsNames[1] + "." + goodsNames[2]+"."+goodsNames[3];
                if (AppUtils.isEmpty(filter_attr02))
                    return;
            }

            pd.setVisibility(View.VISIBLE);
            page = 1;
            sendGoodsList("0", 0, true);

        } else {
            //            if (0 == itemPos)
            //                itemStr = goodsAllAttrs.getJSONObject(titlePos).getString(Constance.attr_name);
            //            dropDownMenu.setPositionIndicatorText(titlePos, itemStr);
            //            Log.v("520it", itemStr);
            //            if (titlePos < itemPosList.size())
            //                itemPosList.remove(titlePos);
            //            itemPosList.add(titlePos, itemPos);


            if (AppUtils.isEmpty(sceneAllAttrs.getJSONObject(titlePos).getJSONArray(Constance.attr_list).getJSONObject(itemPos).getInteger(Constance.scene_id))) {
                sceneIds[titlePos] = 0;
                sceneIdsStr[titlePos]="";
            } else {
                int index = sceneAllAttrs.getJSONObject(titlePos).getJSONArray(Constance.attr_list).getJSONObject(itemPos).getInteger(Constance.scene_id);
                if (index == 0) {
                    sceneIds[titlePos] = 999;
                    sceneIdsStr[titlePos]="";
                } else {
                    sceneIds[titlePos] = index;
                    if(sceneAllAttrs.getJSONObject(titlePos).getString(Constance.filter_attr_name).equals("风格")){
                        sceneIdsStr[0]=sceneAllAttrs.getJSONObject(titlePos).getJSONArray(Constance.attr_list).getJSONObject(itemPos).getString(Constance.attr_value);
                    }else {
                        sceneIdsStr[1]=sceneAllAttrs.getJSONObject(titlePos).getJSONArray(Constance.attr_list).getJSONObject(itemPos).getString(Constance.attr_value);
                    }
                }
            }

            if(OkHttpUtils.hashkNewwork()){
                filter_attr = sceneIds[0] + "." + sceneIds[1];
            }else {
                filter_attr=sceneIdsStr[0]+sceneIdsStr[1];
            }
            if (AppUtils.isEmpty(filter_attr))
                return;
            pd.setVisibility(View.VISIBLE);
            page = 1;
            sendSceneList();
            if (0 == itemPos&&goodsAllAttrs!=null&&goodsAllAttrs.size()>titlePos&&goodsAllAttrs.getJSONObject(titlePos)!=null){
                itemStr = goodsAllAttrs.getJSONObject(titlePos).getString(Constance.filter_attr_name);
            }


            dropDownMenu.setPositionIndicatorText(titlePos, itemStr);
            Log.v("520it", itemStr);
        }


    }

    public void onBackPressed() {
        dropDownMenu.close();
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        page = 1;
        initFilterDropDownView = false;
        if (mSelectType == 0) {
            sendGoodsList("0", 0, true);
        } else {
            sendSceneList();
        }
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        initFilterDropDownView = false;

        if (mSelectType == 0) {
            page = page + 1;
            sendGoodsList("0", 0, true);
        } else {
            page = page + 1;
            sendSceneList();
        }
    }

    /**
     * 获取相册
     */
    public void goPhotoImage() {
        PermissionUtils.requestPermission(mView, PermissionUtils.CODE_READ_EXTERNAL_STORAGE, new PermissionUtils.PermissionGrant() {
            @Override
            public void onPermissionGranted(int requestCode) {
                FileUtil.pickPhoto(mView);
            }
        });
    }

    private DiyProductInfoPopWindow mPopWindow;

    /**
     * 产品详情
     */
    public void getProductDetail() {
        mPopWindow = new DiyProductInfoPopWindow(mView, mView);
        com.alibaba.fastjson.JSONObject jsonObject = null;
        Goods goods = null;
        if (OkHttpUtils.hashkNewwork()) {
            jsonObject = mView.mSelectedLightSA.get(IssueApplication.mLightIndex);
            if (AppUtils.isEmpty(jsonObject)) {
                MyToast.show(mView, "请选择产品!");
                return;
            }
            mPopWindow.productObject = jsonObject;
        } else {
            goods = mView.mSelectedLightSA02.get(IssueApplication.mLightIndex);
            if (AppUtils.isEmpty(goods)) {
                MyToast.show(mView, "请选择产品!");
                return;
            }
            mPopWindow.mGoods = goods;
        }


        mPopWindow.initViewData();
        mPopWindow.onShow(main_fl);
        final com.alibaba.fastjson.JSONObject finalJsonObject = jsonObject;
        final Goods finalGoods = goods;
        mPopWindow.setListener(new IDiyProductInfoListener() {
            @Override
            public void onDiyProductInfo(int type, String msg) {
                getShowProductType(finalJsonObject, finalGoods, type, msg);
            }
        });

    }

    private void getShowProductType(com.alibaba.fastjson.JSONObject jsonObject, Goods goods, int type, final String msg) {
        String productId = "";
        switch (type) {
            case 0://二维码
                String name = "";
                String path = "";
                String phone="";
                String price="";
                if(IssueApplication.mUserInfo!=null){
                    phone=IssueApplication.mUserInfo.getString(Constance.phone);
                }

                if (OkHttpUtils.hashkNewwork()) {
                    if(jsonObject==null){
                        return;
                    }
                    productId = jsonObject.getString(Constance.id);
                    name = jsonObject.getString(Constance.name);
                    if(MyShare.get(mView).getBoolean(Constance.SET_PRICE)){
                        price=mPriceDao.getProductPrice(jsonObject.getInteger(Constance.id)+"").getShop_price()+"";
                    }
                    if(TextUtils.isEmpty(price)){
                        price=jsonObject.getString(Constance.shop_price)+"";
                    }
                    path = NetWorkConst.SHAREPRODUCT + productId+"&phone="+phone+"&price="+price;
                } else {
                    if(goods==null){
                        MyToast.show(mView,"请检查网络");
                        return;
                    }
                    productId = goods.getId() + "";
                    name = goods.getName();
                    if(MyShare.get(mView).getBoolean(Constance.SET_PRICE)){
                        price=mPriceDao.getProductPrice(goods.getId()+"").getShop_price()+"";
                    }
                    if(TextUtils.isEmpty(price)){
                        price=goods.getShop_price()+"";
                    }
                    path = NetWorkConst.SHAREPRODUCT + productId+"&phone="+phone+"&price="+price;
                }

                addImageView(ImageUtil.getTowCodeImage(ImageUtil.createQRImage(path, 150, 150), name));
                break;
            case 1://参数
                addImageView(ImageUtil.textAsBitmap(msg));
                break;
            case 2://Logo
//                String logoPath = NetWorkConst.SHAREIMAGE_LOGO;
//                displayCheckedGoods03(logoPath);
                mLightNumber++;
                Bitmap loadedImage=UIUtils.drawableToBitmap(UIUtils.dip2PX(300),UIUtils.dip2PX(113),mView.getResources().getDrawable(R.mipmap.logo_img));
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                        mScreenWidth / 3 * 2 / 3,
                        (mScreenWidth / 3 * 2 / 3 * loadedImage.getHeight()) / loadedImage.getWidth());
                // 设置灯点击出来的位置
                if (mView.mSelectedLightSA.size() == 1) {
                    leftMargin = mScreenWidth / 3 * 2 / 3;
                } else if (mView.mSelectedLightSA.size() == 2) {
                    leftMargin = mScreenWidth / 3 * 2 / 3 * 2;
                } else if (mView.mSelectedLightSA.size() == 3) {
                    leftMargin = 0;
                }
                lp.setMargins(mScreenWidth / 2 - (mScreenWidth / 3 * 2 / 6), 20, 0, 0);
                TouchView02 touchView = new TouchView02(mView);
                touchView.setLayoutParams(lp);
                touchView.setImageBitmap(loadedImage);// 设置被点击的灯的图片
                touchView.setmLightCount(mLightNumber);// 设置被点击的灯的编号
                touchView.setTag(mLightNumber);
                FrameLayout.LayoutParams newLp = new FrameLayout.LayoutParams(
                        UIUtils.dip2PX(300),
                        UIUtils.dip2PX(113));
                mFrameLayout.addView(touchView,newLp);

                break;
            case 3://产品卡
                path = "";
                String markPrice="";
                String shopPrice="";
                String url="";
                try {

                    if (OkHttpUtils.hashkNewwork()) {
                        productId = jsonObject.getString(Constance.id);
                        name = jsonObject.getString(Constance.name);
                        markPrice = jsonObject.getString(Constance.market_price);
                        if(MyShare.get(mView).getBoolean(Constance.SET_PRICE)){
                         shopPrice=mPriceDao.getProductPrice(jsonObject.getInteger(Constance.id)+"").getShop_price()+"";
                        }else {
                        shopPrice = jsonObject.getString(Constance.shop_price);
                        }

                        path = NetWorkConst.SHAREPRODUCT + productId;
                        com.alibaba.fastjson.JSONArray array=jsonObject.getJSONArray(Constance.gallery);
                        if(array!=null&&array.size()>0&&array.get(0)!=null){
                            url = array.getJSONObject(0).getString(Constance.img_url);
                        }else {
                            url=jsonObject.getString(Constance.img_url);
                        }
                    } else {
                        productId = goods.getId() + "";
                        name = goods.getName();
                        path = NetWorkConst.SHAREPRODUCT + productId;
                        shopPrice = goods.getShop_price() + "";
                        markPrice = goods.getMarket_price() + "";
                        if(goods.getGallery()==null||goods.getGallery().size()==0){
                            url = goods.getImg_url();
                        }else {
                        url = goods.getGallery().get(0).getImg_url();
                        }
                    }
//                String cardPath = NetWorkConst.WEB_PRODUCT_CARD + productId;
//                displayCheckedGoods03(cardPath);
                    final View viewCpk = View.inflate(mView, R.layout.layout_share_01_small, null);
                    viewCpk.setLayoutParams(new FrameLayout.LayoutParams(UIUtils.dip2PX(300), UIUtils.dip2PX(450)));
                    final ImageView iv_share_01 = (ImageView) viewCpk.findViewById(R.id.iv_share_01);
                    LinearLayout ll_share_01 = (LinearLayout) viewCpk.findViewById(R.id.ll_attr);
                    final ImageView iv_code_01 = (ImageView) viewCpk.findViewById(R.id.iv_code);
                    final TextView lv_param = (TextView) viewCpk.findViewById(R.id.tv_paramter);
                    TextView tv_mark_price = (TextView) viewCpk.findViewById(R.id.tv_mark_price);
                    TextView tv_shop_price = (TextView) viewCpk.findViewById(R.id.tv_shop_price);
                    TextView tv_name_mobile = (TextView) viewCpk.findViewById(R.id.tv_name_mobile);
                    TextView tv_address = (TextView) viewCpk.findViewById(R.id.tv_address);

                    tv_mark_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    tv_shop_price.setText("¥" + shopPrice + "/个");
                    tv_mark_price.setText("市场价：¥" + markPrice + "/个");
                    tv_mark_price.setVisibility(View.GONE);
//                    LogUtils.logE("userinfo", ((IssueApplication) mView.getApplicationContext()).mUserInfo.toString());
                    com.alibaba.fastjson.JSONObject userInfo = ((IssueApplication) mView.getApplicationContext()).mUserInfo;
                    if(userInfo!=null){
                    tv_name_mobile.setText("专属热线：" + userInfo.getString(Constance.name) + userInfo.getString(Constance.phone));
                    tv_address.setText("" + userInfo.getString(Constance.address));
                    }
                    final String finalPath = path;
                    String newMsg = msg;
                    lv_param.setText(newMsg);
                    String codePath=NetWorkConst.UR_PRODUCT_URL +url;
                    if(!OkHttpUtils.hashkNewwork()){
                        File file=new File(FileUtil.getGoodsExternDir(url));
                        if(file.exists()){
                        codePath="file://"+file.toString();
                        }
                    }
                    ImageLoader.getInstance().loadImage(codePath, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String s, final View view, final Bitmap bitmap) {

                            final Bitmap bitmap01 = ImageUtil.createQRImage(finalPath, 128, 128);

//                        final QuickAdapter adapter = new QuickAdapter<Goods_attr_list>(mView,R.layout.item_share_params_small) {
//                            @Override
//                            protected void convert(BaseAdapterHelper helper, Goods_attr_list item) {
//                                helper.setText(R.id.tv_key,item.getAttr_name()+":");
//                                helper.setText(R.id.tv_value,item.getAttr_value());
//                            }
//                        };
                            mView.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    iv_code_01.setImageBitmap(bitmap01);
//                                lv_param.setAdapter(adapter);
//                                adapter.replaceAll(goods_attr_lists);
//                                    mFrameLayout.addView(view);
                                    iv_share_01.setImageBitmap(bitmap);
                                    Bitmap temp = UIUtils.view2Bitmap(viewCpk);
                                    addImageView(temp);

                                }
                            });


                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {

                        }
                    });
                }catch (Exception e){
                    LogUtils.logE("ex",e.getMessage());

                }
                break;
        }
    }


    /**
     * 获取产品详情
     */
    public void senDetail() {
        detail_rl.setVisibility(View.VISIBLE);
        if (OkHttpUtils.hashkNewwork()) {
            com.alibaba.fastjson.JSONObject productObject = mView.mSelectedLightSA.get(IssueApplication.mLightIndex);
            if (AppUtils.isEmpty(productObject))
                return;
            String name = productObject.getString(Constance.name);
            String price = productObject.getString(Constance.shop_price);
            com.alibaba.fastjson.JSONArray attrs = null;

            try {
                attrs = productObject.getJSONArray(Constance.attr);
            } catch (Exception e) {
                attrs = productObject.getJSONObject(Constance.attr).getJSONArray(Constance.pro);
            }

            final int goodId = productObject.getInteger(Constance.id);
            float setPrice = mPriceDao.getProductPrice(goodId + "").getShop_price();
            price = setPrice == 0 ? price : setPrice + "";
            List<String> attachs = new ArrayList<>();
            attachs.add("名称:  " + name);
            attachs.add("价格:  ￥" + price);
            for (int i = 0; i < attrs.size(); i++) {
                if (!AppUtils.isEmpty(attrs.getJSONObject(i).getString(Constance.VALUE))) {
                    String attrName = attrs.getJSONObject(i).getString(Constance.name);
                    String attrValeu = attrs.getJSONObject(i).getString(Constance.VALUE);
                    attachs.add(attrName + ":  " + attrValeu);
                }

            }
            mParamentAdapter = new ParamentAdapter(attachs, mView, 1);
        } else {
            Goods goods = mView.mSelectedLightSA02.get(IssueApplication.mLightIndex);
            if (AppUtils.isEmpty(goods))
                return;
            String name = goods.getName();
            String price = goods.getShop_price() + "";
            final int goodId = goods.getId();
            float setPrice = mPriceDao.getProductPrice(goodId + "").getShop_price();
            price = setPrice == 0 ? price : setPrice + "";
            List<String> attr = getAttrList(goodId + "");
            attr.add(0, "名称:  " + name);
            attr.add(1, "价格:  ￥" + price);
            mParamentAdapter = new ParamentAdapter(attr, mView, 1);
        }

        attr_lv.setAdapter(mParamentAdapter);
        attr_lv.setDivider(null);//去除listview的下划线
    }

    private List<String> getAttrList(String id) {
        return GoodsDao.getAttrForGood(id);
    }

    /**
     * 跳转产品详情页面
     */
    public void goDetail() {
        if (OkHttpUtils.hashkNewwork()) {
            if (AppUtils.isEmpty(mView.mSelectedLightSA) || mView.mSelectedLightSA.size() == 0) {
                MyToast.show(mView, "还没有选择产品!");
                return;
            }
            mIntent = new Intent(mView, ProductDetailHDActivity.class);
            String productString = mView.mSelectedLightSA.get(IssueApplication.mLightIndex).toJSONString();
            mIntent.putExtra(Constance.product, productString);
            mView.startActivity(mIntent);
        } else {
            if (AppUtils.isEmpty(mView.mSelectedLightSA02) || mView.mSelectedLightSA02.size() == 0) {
                MyToast.show(mView, "还没有选择产品!");
                return;
            }
            mIntent = new Intent(mView, ProductDetailHDActivity.class);
            mIntent.putExtra(Constance.id, mView.mSelectedLightSA02.get(IssueApplication.mLightIndex).getId());
            mIntent.putExtra(Constance.product, mView.mSelectedLightSA02.get(IssueApplication.mLightIndex));
            mView.startActivity(mIntent);
        }

    }

    /**
     * 加入收藏
     */
    public void sendCollect() {
        mView.setShowDialog(true);
        mView.setShowDialog("正在收藏中!");
        mView.showLoading();
        mIntent = new Intent(mView, ProductDetailHDActivity.class);
        int productId = mView.mSelectedLightSA.get(IssueApplication.mLightIndex).getInteger(Constance.id);
        mNetWork.sendAddLikeCollect(productId + "", this);

    }

    public void searchData(String value) {
        page = 1;
        keyword = value;
        sendGoodsList("0", 0, true);
    }

    /**
     * 分享
     */
    public void getShareDiy(final int type) {
        if(!OkHttpUtils.hashkNewwork()){
            MyToast.show(mView,"请先连接网络");
            return;
        }
        final StringBuffer goodsid = new StringBuffer();

        if (OkHttpUtils.hashkNewwork()) {
            for (int i = 0; i < mView.mSelectedLightSA.size(); i++) {
                goodsid.append(mView.mSelectedLightSA.valueAt(i).getString(Constance.id) + "");
                if (i < mView.mSelectedLightSA.size() - 1) {
                    goodsid.append(",");
                }
            }
        } else {
            for (int i = 0; i < mView.mSelectedLightSA02.size(); i++) {
                Goods goods = mView.mSelectedLightSA02.valueAt(i);
                goodsid.append(goods.getId() + "");
                if (i < mView.mSelectedLightSA.size() - 1) {
                    goodsid.append(",");
                }
            }
        }

        diyContainerRl.setVisibility(View.INVISIBLE);
        //截图
        final Bitmap imageData = ImageUtil.compressImage(ImageUtil.takeScreenShot(mView));
        com.alibaba.fastjson.JSONObject userinfo=((IssueApplication) mView.getApplication()).mUserInfo;
        if(userinfo!=null){
            mId = userinfo.getString(Constance.id);
        }else {
            mId="1";
        }

        diyContainerRl.setVisibility(View.VISIBLE);
        mView.setShowDialog(true);
        mView.setShowDialog("正在分享中...");
        mView.showLoading();
        Log.v("520", "后时间：" + System.currentTimeMillis());
        final String url = NetWorkConst.SUBMITPLAN;//地址
        final Map<String, String> params = new HashMap<String, String>();
        params.put("goods_id", goodsid.toString());
        params.put("phone", "androidHD");
        params.put("title", "share");
        params.put("user_id", mId + "");
        params.put("village", "unknown");

        final String imageName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + ".png";
        if(OkHttpUtils.hashkNewwork()){
            new Thread(new Runnable() { //开启线程上传文件
                @Override
                public void run() {

                    final String resultJson = uploadFile(imageData, url, params, imageName);
                    com.alibaba.fastjson.JSONObject resultObject = JSON.parseObject(resultJson);
                    int result = 0;
                    try {
                        result = resultObject.getInteger(Constance.result);
                        Log.v("520", "上传时间：" + System.currentTimeMillis());
                        //分享的操作
                        final int finalResult = result;
                        mView.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mView.hideLoading();
                                Log.v("520it", "分享成功!");
                                if (type == 0) {
                                    String title = "来自 " + UIUtils.getString(R.string.app_name) + " 配灯的分享";
                                    //                                            ShareUtil.showShare(mView, title, NetWorkConst.SHAREPLAN + "id=" + finalResult, mView.mSceenPath);
                                    IssueApplication.sharePath = NetWorkConst.SHAREPLAN + "id=" + finalResult;
                                    IssueApplication.shareRemark = title;
                                    TwoCodeSharePopWindow popWindow = new TwoCodeSharePopWindow(mView, mView);
                                    popWindow.onShow(mFrameLayout);
                                    popWindow.setListener(new ITwoCodeListener() {
                                        @Override
                                        public void onTwoCodeChanged(String path) {
                                        }
                                    });
                                } else {
                                    ProgrammeDao dao = new ProgrammeDao(mView);
                                    Programme bean = new Programme();
                                    bean.setId(finalResult);
                                    bean.setTitle(mTitle);
                                    bean.setContent(mContent);
                                    bean.setStyle(mStyle);
                                    bean.setShareid(finalResult + "");
                                    bean.setSpace(mSpace);
                                    bean.setSceenpath(imageName);
                                    bean.setGoods_id(goodsid.toString());
//                                bean.setpImage(ImageUtil.getBitmapByte(imageData));
                                    if (-1 != dao.replaceOne(bean)) {
                                        Toast.makeText(mView, "已保存到方案库!", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(mView, "网络异常,保存到方案库失败!", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    imageData.recycle();

                                }
                            }
                        });


                    } catch (Exception e) {
                        mView.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyToast.show(mView, "网络异常, 保存到方案库失败!");
                                mView.hideLoading();
                                return;
                            }
                        });

                    }


                }
            }).start();
        }else {
            int finalResult= (int) SystemClock.currentThreadTimeMillis();
            ProgrammeDao dao = new ProgrammeDao(mView);
            Programme bean = new Programme();
            bean.setId(finalResult);
            bean.setTitle(mTitle);
            bean.setContent(mContent);
            bean.setStyle(mStyle);
            bean.setShareid(finalResult + "");
            bean.setSpace(mSpace);
            bean.setSceenpath(imageName);
            bean.setGoods_id(goodsid.toString());
//                                bean.setpImage(ImageUtil.getBitmapByte(imageData));
            if (-1 != dao.replaceOne(bean)) {
                Toast.makeText(mView, "已保存到方案库!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(mView, "网络异常,保存到方案库失败!", Toast.LENGTH_LONG).show();
                return;
            }
            imageData.recycle();
        }

    }

    /**
     * 复位
     */
    public void setfuwei() {
        try {
            FrameLayout productFl = (FrameLayout) mFrameLayout.findViewWithTag(IssueApplication.mLightIndex);
            TouchView productTv = (TouchView) productFl.getChildAt(0);
            productTv.setss();
        } catch (Exception e) {

        }
    }


    private class ProAdapter extends BaseAdapter {
        public ProAdapter() {
        }

        @Override
        public int getCount() {
            if (!OkHttpUtils.hashkNewwork()&& mSelectType == 0) {
                if (null == mGoodsList)
                    return 0;
                return mGoodsList.size();

            } else {

                if(OkHttpUtils.hashkNewwork()){
                if (null == goodses)
                    return 0;
                return goodses.size();
                }else {
                    if (null == sceneBeans)
                        return 0;
                    return sceneBeans.size();
                }
            }

        }

        @Override
        public com.alibaba.fastjson.JSONObject getItem(int position) {
            if (null == goodses)
                return null;
            return goodses.getJSONObject(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mView, R.layout.item_gridview_diy, null);

                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                holder.ll_goods= (LinearLayout) convertView.findViewById(R.id.ll_goods);
                holder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_price= (TextView) convertView.findViewById(R.id.tv_price);
                holder.iv_cart= (ImageView) convertView.findViewById(R.id.iv_cart);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.imageView.setImageResource(R.drawable.bg_default);
            holder.imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            if (mSelectType == 0) {

                holder.ll_goods.setVisibility(View.VISIBLE);

                if (!OkHttpUtils.hashkNewwork()&&mGoodsList!=null&&mGoodsList.size()>position) {
                    String path = FileUtil.getGoodsExternDir(mGoodsList.get(position).getImg_url());
                    File imageFile = new File(path );
                    if (imageFile.exists()) {
                        imageURL = "file://" + imageFile.toString();
                        ImageLoader.getInstance().displayImage(imageURL, holder.imageView);
                    }
                    holder.tv_name.setText(mGoodsList.get(position).getName());
                    holder.tv_price.setText("¥"+mGoodsList.get(position).getShop_price());
                    holder.iv_cart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addToCart(position);
                        }
                    });
                } else {
                    String path = FileUtil.getGoodsExternDir(goodses.getJSONObject(position).getString(Constance.img_url));
                    File imageFile = new File(path );
                    if (imageFile.exists()) {
                        imageURL = "file://" + imageFile.toString();
                        ImageLoader.getInstance().displayImage(imageURL, holder.imageView);
                    } else {
                        ImageLoader.getInstance().displayImage(NetWorkConst.UR_PRODUCT_URL +
                                        goodses.getJSONObject(position).getString(Constance.img_url)
                                , holder.imageView);
                    }

                    holder.tv_name.setText(goodses.getJSONObject(position).getString(Constance.name));
                    holder.tv_price.setText("¥"+goodses.getJSONObject(position).getString(Constance.shop_price));
                    holder.iv_cart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addToCart(position);
                        }
                    });
                }
            } else {

                holder.imageView.setLayoutParams(new LinearLayout.LayoutParams(UIUtils.dip2PX(160),UIUtils.dip2PX(120)));
                holder.ll_goods.setVisibility(View.GONE);
                holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                String path = "";
                if(!OkHttpUtils.hashkNewwork()){
                    String pathName=FileUtil.getSceenExternDir(sceneBeans.get(position).getPath());
                    File file=new File(pathName);
                    if(file.exists()){
                        path="file://"+file.toString();
                    }
                }else {

                int mId= Integer.parseInt(goodses.getJSONObject(position).getString(Constance.id));
                if(mId<=1551){
                    path=NetWorkConst.URL_SCENE+goodses.getJSONObject(position).getString(Constance.path);
                }else {
                    path = NetWorkConst.UR_SCENE_URL + goodses.getJSONObject(position).getString(Constance.path);
                }

//                String path1 = FileUtil.getSceenExternDir(goodses.getJSONObject(position).getString(Constance.path));
//                File imageFile = new File(path1 + "!400X400.png");
//                if (imageFile.exists()) {
//                    imageURL = "file://" + imageFile.toString();
//                    ImageLoader.getInstance().displayImage(imageURL, holder.imageView);
//                } else {
//                }
                }
                ImageLoader.getInstance().displayImage(path, holder.imageView);
            }
            return convertView;
        }

        class ViewHolder {
            ImageView imageView;
            LinearLayout ll_goods;
            TextView tv_name;
            TextView tv_price;
            ImageView iv_cart;
        }
    }

    private void addToCart(int position) {
        CartDao dao = new CartDao(mView);
        List<Goods> cartList=dao.getAll();
        if (!OkHttpUtils.hashkNewwork()) {
            if(mGoodsList==null||mGoodsList.size()==0||mGoodsList.size()<=position){
                MyToast.show(mView,"没有离线数据，请检查网络");
                return;
            }
            Goods goods = mGoodsList.get(position);
            goods.setBuyCount(1);
            goods.setGoods_number("1");
            if(cartList!=null&&cartList.size()>0){
            for(int i=0;i<cartList.size();i++){
            if(cartList.get(i).getId()==goods.getId()){
                goods.setBuyCount(cartList.get(i).getBuyCount()+1);
                goods.setGoods_number((cartList.get(i).getBuyCount()+1)+"");
                break;
            }
            }
            }
            if (-1 != dao.replaceOne(goods)) {
                    Toast.makeText(mView, "已添加到购物车!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(mView, "添加到购物车失败!", Toast.LENGTH_LONG).show();
                return;
            }
        } else {
            Goods goods = new Goods();
            goods.setName(goodses.getJSONObject(position).getString(Constance.name));
            goods.setId(Integer.parseInt(goodses.getJSONObject(position).getString(Constance.id)));
            goods.setShop_price(goodses.getJSONObject(position).getFloat(Constance.shop_price));
            goods.setMarket_price(goodses.getJSONObject(position).getString(Constance.market_price));
            goods.setImg_url(goodses.getJSONObject(position).getString(Constance.img_url));
            goods.setBuyCount(1);
            goods.setGoods_number("1");
            for(int i=0;i<cartList.size();i++){
                if(cartList.get(i).getId()==goods.getId()){
                    goods.setBuyCount(cartList.get(i).getBuyCount()+1);
                    goods.setGoods_number((cartList.get(i).getBuyCount()+1)+"");
                    break;
                }
            }
            if (-1 != dao.replaceOne(goods)) {
                    Toast.makeText(mView, "已添加到购物车!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(mView, "添加到购物车失败!", Toast.LENGTH_LONG).show();
                return;
            }

        }


        IssueApplication.mCartCount = dao.getCount();
        EventBus.getDefault().post(Constance.CARTCOUNT);
    }
}
