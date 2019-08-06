package bc.otlhd.com.controller.product;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Message;
import android.os.SystemClock;
import androidx.annotation.NonNull;

import androidx.viewpager.widget.PagerAdapter;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lib.common.hxp.view.PullToRefreshLayout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import bc.otlhd.com.R;
import bc.otlhd.com.adapter.BaseAdapterHelper;
import bc.otlhd.com.adapter.QuickAdapter;
import bc.otlhd.com.bean.GoodPrices;
import bc.otlhd.com.bean.Goods;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.cons.NetWorkConst;
import bc.otlhd.com.controller.BaseController;
import bc.otlhd.com.data.GoodsDao;
import bc.otlhd.com.data.SetPriceDao;
import bc.otlhd.com.net.OkHttpUtils;
import bc.otlhd.com.ui.activity.IssueApplication;
import bc.otlhd.com.ui.activity.product.ProListActivity;
import bc.otlhd.com.ui.activity.product.ProductDetailHDActivity;
import bc.otlhd.com.ui.view.EndOfGridView;
import bc.otlhd.com.ui.view.PMSwipeRefreshLayout;
import bc.otlhd.com.ui.view.ShowDialog;
import bc.otlhd.com.utils.FileUtil;
import bc.otlhd.com.utils.LogUtils;
import bc.otlhd.com.utils.MyShare;
import bc.otlhd.com.utils.UIUtils;
import bc.otlhd.com.utils.net.HttpListener;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;

/**
 * Created by gamekonglee on 2019/2/18.
 */

public class ProListController extends BaseController implements PullToRefreshLayout.OnRefreshListener,HttpListener, AdapterView.OnItemClickListener, EndOfGridView.OnEndOfListListener, SwipeRefreshLayout.OnRefreshListener {

    private final ProListActivity mView;
    JSONArray sceneAllAttrs=new JSONArray();
    public int page;
    public String mType;
    public String filter_attr;
    public String filter_attr02;
    public String keyword;
    private ProgressBar pd;
    private PMSwipeRefreshLayout mPullToRefreshLayout;
    public EndOfGridView mGoodsSv;
    public List<Goods> mGoodsList=new ArrayList<>();
    private SetPriceDao mPriceDao;
    private int mScreenWidth;
//    private JSONArray goodses;
public List<Goods> goodses;
    private String imageURL;
    private ProAdapter mProAdapter;
    private JSONArray goodsCategoryHome;
    private JSONArray sceneAllAttrsTemp;
    private PagerAdapter pagerAdapter;
    public boolean isBottom;
    private boolean isSend;
    private boolean isFirst;
    public String on_sale="1";

    public ProListController(ProListActivity proListActivity) {
        mView = proListActivity;
        init();
    }

    public void requestData() {
        page = 0;
        isFirst = true;
        JSONObject userinfo=((IssueApplication) mView.getApplication()).mUserInfo;
        if(userinfo!=null){
                String id =userinfo.getString(Constance.id);
                boolean isSetPrice = MyShare.get(UIUtils.getContext()).getBoolean(Constance.SET_PRICE);
            try {
                int is_price = userinfo.getInteger(Constance.is_price);
                if (!isSetPrice && is_price == 1) {
                    sendReadPrice(id);
                    return;
                }
            }catch (Exception e){
//                    sendReadPrice(id);
            }
        }
        getGoodsFilter();
//        sendGoodsList("0", 0);
//        if (!OkHttpUtils.hashkNewwork()) {
//            mNetWork.sendGoodsList("0", page, 0, keyword, mType, filter_attr, true, 1, mView, this);
//        }
        pd.setVisibility(View.GONE);
    }

    private void getGoodsFilter() {
        if(!OkHttpUtils.hashkNewwork()){
            String result=MyShare.get(mView).getString(Constance.filter_attr);
            if(result==null)return;
            JSONObject jsonObject= JSON.parseObject(result);
            if(jsonObject!=null){
                        sceneAllAttrsTemp = jsonObject.getJSONArray(Constance.all_attr_list);
                        refreshAttr();
                    return;

            }
            return;
        }

        mNetWork.sendGoodsList("0", 1, 0, "", "", "", false, 20,on_sale, mView, new HttpListener() {
            @Override
            public void onSuccessListener(int what, JSONObject ans) {
                if (!OkHttpUtils.hashkNewwork()) {
                    if (AppUtils.isEmpty(sceneAllAttrsTemp)) {
                        sceneAllAttrsTemp = ans.getJSONArray(Constance.all_attr_list);
                        refreshAttr();
                    }
                    return;
                }

                if (null != mPullToRefreshLayout) {
                    dismissRefesh();
                }

                if (AppUtils.isEmpty(sceneAllAttrsTemp)) {
                    sceneAllAttrsTemp = ans.getJSONArray(Constance.all_attr_list);
                    LogUtils.logE("attrs",sceneAllAttrsTemp.toJSONString());
//                    PgyCrashManager.reportCaughtException(mView,new Exception("get_all_attr_list"));
                    refreshAttr();
                }
            }

            @Override
            public void onFailureListener(int what, JSONObject ans) {

            }
        });
    }

    private void init() {
        pd= (ProgressBar) mView.findViewById(R.id.pd);
        mPullToRefreshLayout = ((PMSwipeRefreshLayout) mView.findViewById(R.id.mFilterContentView));
        mPullToRefreshLayout.setOnRefreshListener(this);
        mGoodsSv = (EndOfGridView) mView.findViewById(R.id.gridView);
        mGoodsSv.setOnEndOfListListener(this);
        mGoodsSv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 8) {
                    mView.iv_top.setVisibility(View.VISIBLE);
                } else {
                    mView.iv_top.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mProAdapter = new ProAdapter(mView,R.layout.item_gridview_fm_product);
        goodses=new ArrayList<>();
        mGoodsSv.setAdapter(mProAdapter);
        mProAdapter.replaceAll(goodses);
        mGoodsSv.setOnItemClickListener(this);
        goodsCategoryHome = new JSONArray();
        mScreenWidth = mView.getResources().getDisplayMetrics().widthPixels;
        try{
            mPriceDao = new SetPriceDao(mView);
        }catch ( Exception e){

        }finally {
            for(int i=0;i<3;i++){
                JSONObject cate=new JSONObject();
                switch (i){
                    case 0:
                        cate.put(Constance.attr_value,"全部商品");
                        break;
                    case 1:
                        cate.put(Constance.attr_value,"热卖精品");
                        break;
//                    case 2:
//                        cate.put(Constance.attr_value,"畅销现货");
//                        break;
                    case 2:
                        cate.put(Constance.attr_value,"新品速递");
                        break;
                }
                goodsCategoryHome.add(cate);
            }
            sceneAllAttrs= goodsCategoryHome;
            initPageAdapter();
        }

    }

    private void initPageAdapter() {
        pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return sceneAllAttrs.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view==object;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return sceneAllAttrs.getJSONObject(position).getString(Constance.attr_value);
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                TextView textView=new TextView(mView);
                container.addView(textView);
                return textView;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }
        };
        mView.vp.setAdapter(pagerAdapter);
        mView.pagerSlidingTabStrip.setViewPager(mView.vp);
        mView.vp.setCurrentItem(mView.current);
        mView.pagerSlidingTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(!IssueApplication.IsRoot()){
                    SystemClock.sleep(IssueApplication.SLEEP_TIME);
                }
               mView.current=position;
               LogUtils.logE("pageSelect",position+"");
               if(mView.currentCatogoryMain==0){
//                   PgyCrashManager.reportCaughtException(mView,new Exception("pageSeleced:homefilter"));
                    getHome_Filter();
               }else {
                    getAttr_Filter();
               }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void getHome_Filter() {
        switch (mView.current) {
            case 0:
                mType="";
                break;
            case 1:
                mType = "is_hot";//精品
                break;
//            case 2:
//                mType = "is_best";//热销
//                break;
            case 2:
                mType = "is_new";//新品
                break;
        }
//        if(mView.is_hot_send){
//            return;
//        }
        page = 1;
        goodses=new ArrayList<>();
//        PgyCrashManager.reportCaughtException(mView,new Exception("getHome_filter:"+page));
        sendGoodsList("0", 0);
    }

    private Integer[] goodsIds = new Integer[]{0, 0, 0,0};
    private String[] goodsNames = new String[]{"", "", "",""};
    int titlePos=0;
    private void getAttr_Filter() {
        goodsIds = new Integer[]{0, 0, 0,0};
        goodsNames = new String[]{"", "", "",""};
        if (OkHttpUtils.hashkNewwork()) {
            if (AppUtils.isEmpty(sceneAllAttrs.getJSONObject(mView.current).getInteger(Constance.goods_id))) {
                if(mView.current!=0){
                    goodsIds[titlePos] = 999;
                }else {
                goodsIds[titlePos] = 0;
                }
            } else {
                int index = sceneAllAttrs.getJSONObject(mView.current).getInteger(Constance.goods_id);
                if (index == 0) {
                    if(mView.current!=0){
                        goodsIds[titlePos] = 999;
                    }else {
                    goodsIds[titlePos] = 0;
                    }
                } else {
                    goodsIds[titlePos] = index;
                }
            }
            filter_attr = goodsIds[0] + "." + goodsIds[1] + "." + goodsIds[2]+"."+goodsIds[3];
            if (AppUtils.isEmpty(filter_attr))
                return;
        } else {
            if(sceneAllAttrs!=null&&sceneAllAttrs.getJSONObject(mView.current)!=null){
            if (AppUtils.isEmpty(sceneAllAttrs.getJSONObject(mView.current).getInteger(Constance.goods_id))) {
                goodsNames[titlePos] = "";
                if(mView.current!=0){
                    goodsIds[titlePos] = 999;
                }else {
                goodsIds[titlePos] = 0;
                }
            } else {
                String indexValue = sceneAllAttrs.getJSONObject(mView.current).getString(Constance.attr_value);
                goodsNames[titlePos] = indexValue;
                int index = sceneAllAttrs.getJSONObject(mView.current).getInteger(Constance.goods_id);
                if (index == 0) {
                    if(mView.current!=0){
                        goodsIds[titlePos] = 999;
                    }else {
                    goodsIds[titlePos] = 0;
                    }
                } else {
                    goodsIds[titlePos] = index;
                }
            }
            }
            filter_attr02 = goodsNames[0] + "." + goodsNames[1] + "." + goodsNames[2]+"."+goodsNames[3];
//            filter_attr = goodsIds[0] + "." + goodsIds[1] + "." + goodsIds[2]+"."+goodsIds[3];
//            if (AppUtils.isEmpty(filter_attr02))
//                return;
        }
        pd.setVisibility(View.VISIBLE);
        page = 1;
        mGoodsList=new ArrayList<>();
        isBottom=false;
//        LogUtils.logE("filterStr",filter_attr);
        sendGoodsList("0", 0);
    }

    private void sendReadPrice(final String id) {
        mView.setShowDialog(true);
        mView.setShowDialog("正在获取自定义价格..");
        mView.showLoading();
        mNetWork.sendReadPrice(id, mView, new HttpListener() {
            @Override
            public void onSuccessListener(int what, JSONObject ans) {

                JSONArray jsonArray = ans.getJSONArray(Constance.data);
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    GoodPrices good = new GoodPrices();
                    good.setId(jsonObject.getInteger(Constance.id));
                    good.setShop_price(jsonObject.getFloatValue(Constance.shop_price));
                    mPriceDao.replaceOne(good);
                }
                MyShare.get(UIUtils.getContext()).putBoolean(Constance.SET_PRICE, true);
                mView.hideLoading();
                sendGoodsList("0", 0);
                pd.setVisibility(View.GONE);

            }

            @Override
            public void onFailureListener(int what, JSONObject ans) {
                ShowDialog mDialog = new ShowDialog();
                mDialog.show(mView, "提示", "获取自定义价格失败,是否重新获取？", new ShowDialog.OnBottomClickListener() {
                    @Override
                    public void positive() {
                        sendReadPrice(id);
                    }

                    @Override
                    public void negtive() {
                        sendGoodsList("0", 0);
                        pd.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    /**
     * 获取产品列表
     *
     * @param c_id
     * @param okcat_id
     */
    public void sendGoodsList(final String c_id, int okcat_id) {
        mView.setShowDialog(true);
        mView.setShowDialog("正在加载...");
        mView.showLoading();
        pd.setVisibility(View.GONE);

//        if(mView.is_hot){
//            filter_attr="is_hot";
//            mView.is_hot=false;
//        }
        Log.v("520it", OkHttpUtils.hashkNewwork() + "");
//        LogUtils.logE("page",page+","+goodses);

        if (OkHttpUtils.hashkNewwork()) {
//            PgyCrashManager.reportCaughtException(mView,new Exception("c_id:"+c_id+",okcai_id:"+okcat_id+",key"+keyword+",mType:"+mType+",filter:"+filter_attr+",page:"+page+",goodses"+goodses.toJSONString()));
            mNetWork.sendGoodsList(c_id, page, okcat_id, keyword, mType, filter_attr, false, 20, on_sale, mView,  this);
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final List<Goods> goodsList = GoodsDao.getGoodsList(keyword, c_id, filter_attr02, mType, 20, page,on_sale);

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
                                isBottom = true;
                                if (page == 1) {
                                    MyToast.show(mView, "数据查询不到");
                                    mGoodsList = new ArrayList<Goods>();
                                } else {
                                    MyToast.show(mView, "数据已经到底啦!");
                                }
                                dismissRefesh();
                                mProAdapter.replaceAll(mGoodsList);
                                return;
                            }
                            getDataSuccess02(goodsList);
                        }
                    });

                }
            }).start();
        }


    }


    private void dismissRefesh() {
        mPullToRefreshLayout.setRefreshing(false);
//        mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }
    private void getDataSuccess02(List<Goods> array) {
        ImageLoader.getInstance().clearDiskCache();//清除磁盘缓存
        ImageLoader.getInstance().clearMemoryCache();//清除内存缓存
        if (1 == page){
            mGoodsList.clear();
            mGoodsList=null;
            mGoodsList = array;
        }
        else if (null != mGoodsList) {
            for (int i = 0; i < array.size(); i++) {
                mGoodsList.add(array.get(i));
            }

            if (AppUtils.isEmpty(array))
                MyToast.show(mView, "没有更多内容了");
        }
        mView.runOnUiThread(new Runnable() {
            @Override
            public void run() {

//                mProAdapter=new ProAdapter(mView,R.layout.item_gridview_fm_product);
//                mGoodsSv.setAdapter(mProAdapter);
//                PgyCrashManager.reportCaughtException(mView,new Exception("mProAdapter:setAdapter"+page+","+goodses.toJSONString()));
                mProAdapter.replaceAll(mGoodsList);

//                mProAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(!IssueApplication.IsRoot()){
        SystemClock.sleep(IssueApplication.SLEEP_TIME);
        }

        Intent mIntent = new Intent(mView, ProductDetailHDActivity.class);
//        position-=4;
        if (OkHttpUtils.hashkNewwork()) {
//            HashMap<String,String> map = new HashMap<String,String>();
//            map.put("product",""+goodses.get(position).getId());
//            MobclickAgent.onEvent(mView, "product2", map);
//            MobclickAgent.onEvent(mView,"product2","产品id");
//            MobclickAgent.onEventValue(mView,"product2",map,200);
//            MobclickAgent.onEvent(mView, "product1", map);
//            MobclickAgent.onEvent(mView,"product1","产品id");
//            MobclickAgent.onEventValue(mView,"product1",map,200);
//            MobclickAgent.onEvent(mView,"123",""+goodses.get(position).getId());
            MobclickAgent.onEvent(mView,"product",""+goodses.get(position).getName());
//            MobclickAgent.onEvent(mView,"productId",""+goodses.get(position).getId());
            mIntent.putExtra(Constance.product, new Gson().toJson(goodses.get(position),Goods.class));
        } else {
            MobclickAgent.onEvent(mView,"product",""+mGoodsList.get(position).getName());
            mIntent.putExtra(Constance.id, mGoodsList.get(position).getId());
            mIntent.putExtra(Constance.product, mGoodsList.get(position));
        }
        mView.startActivity(mIntent);
    }

    public void getCategoryData(int currentCatogoryMain) {
        if(sceneAllAttrs!=null){
//            LogUtils.logE("category",sceneAllAttrsTemp.toJSONString());
            if(sceneAllAttrsTemp==null||sceneAllAttrsTemp.size()==0){
                MyToast.show(mView,"正在加载中...");
                return;
            }
            isBottom=false;
            switch (currentCatogoryMain){
                case 0:
                    sceneAllAttrs=goodsCategoryHome;
                    break;
                case 1:
                    for(int i=0;i<sceneAllAttrsTemp.size();i++){
                        if(sceneAllAttrsTemp.getJSONObject(i).getString(Constance.filter_attr_name).equals("品牌")){
                            sceneAllAttrs=sceneAllAttrsTemp.getJSONObject(i).getJSONArray(Constance.attr_list);
                            for(int j=0;j<sceneAllAttrs.size();j++){
                                if(sceneAllAttrs.getJSONObject(j).getString(Constance.attr_value).equals("全部")){
                                    sceneAllAttrs.getJSONObject(j).put(Constance.attr_value,"雷士照明");
                                    break;
                                }
                            }
                        }
                    }

                    for(int i=0;i<sceneAllAttrsTemp.size();i++){
                        if(sceneAllAttrsTemp.getJSONObject(i).getString(Constance.filter_attr_name).equals("品牌")){
                            sceneAllAttrs=sceneAllAttrsTemp.getJSONObject(i).getJSONArray(Constance.attr_list);
                            titlePos=i;
                            break;
                        }
                    }
                    break;
                case 2:
                    for(int i=0;i<sceneAllAttrsTemp.size();i++){
                        if(sceneAllAttrsTemp.getJSONObject(i).getString(Constance.filter_attr_name).equals("风格")){
                            sceneAllAttrs=sceneAllAttrsTemp.getJSONObject(i).getJSONArray(Constance.attr_list);
                            titlePos=i;
                            break;
                        }
                    }
                    break;
                case 3:
                    for(int i=0;i<sceneAllAttrsTemp.size();i++){
                        if(sceneAllAttrsTemp.getJSONObject(i).getString(Constance.filter_attr_name).equals("类型")){
                            sceneAllAttrs=sceneAllAttrsTemp.getJSONObject(i).getJSONArray(Constance.attr_list);
                            titlePos=i;
                            break;
                        }
                    }
                    break;
                case 4:
                    for(int i=0;i<sceneAllAttrsTemp.size();i++){
                        if(sceneAllAttrsTemp.getJSONObject(i).getString(Constance.filter_attr_name).equals("空间")){
                            sceneAllAttrs=sceneAllAttrsTemp.getJSONObject(i).getJSONArray(Constance.attr_list);
                            titlePos=i;
                            break;
                        }
                    }
                    break;
            }
            mView.pagerSlidingTabStrip.setVisibility(View.VISIBLE);
            initPageAdapter();
            if(!TextUtils.isEmpty(mView.attr_value)){
                mView.current=-1;
                for(int i=0;i<sceneAllAttrs.size();i++){
                    if(sceneAllAttrs.getJSONObject(i).getString(Constance.attr_value).equals(mView.attr_value)){
                        mView.current=i;
                        break;
                    }
                }
                mView.attr_value="";
//                mView.vp.setCurrentItem(mView.current);
//                if(mView.currentCatogoryMain==0){
//                    getHome_Filter();
//                }else {
//                    getAttr_Filter();
//                }
//                sendGoodsList("0",0);
                if(mView.current==-1){
                    MyToast.show(mView,"没有查询到数据");
                    return;
                }
            }else if(mView.is_hot){
                mView.is_hot=false;
                mType="";
//                mView.vp.setCurrentItem(mView.current);
            }
            if(mView.current==0){
                if(mView.currentCatogoryMain==0){
                    getHome_Filter();
                }else {
                    getAttr_Filter();
                }
            }

//            PgyCrashManager.reportCaughtException(mView,new Exception("setCurrent"));
            mView.vp.setCurrentItem(mView.current);
        }

    }

    public void searchData(String s) {
        keyword=s;
        page=1;
//        mView.currentCatogoryMain=0;
        mView.current=0;
        mView.attr="";
        mView.attr_value="";
        getCategoryData(mView.currentCatogoryMain);
//        sendGoodsList("0",0);
    }




    private class ProAdapter extends QuickAdapter<Goods> {


        public ProAdapter(Context context, int layoutResId) {
            super(context, layoutResId);
        }

        @Override
        public int getCount() {
//            PgyCrashManager.reportCaughtException(mView,new Exception("getCount:0"));
            if (OkHttpUtils.hashkNewwork()) {
                if (null == goodses)
                    return 0;
//                PgyCrashManager.reportCaughtException(mView,new Exception("getCount"+goodses.size()+","+goodses.toJSONString()));
                return goodses.size();
            } else {
                if (null == mGoodsList)
                    return 0;
                return mGoodsList.size();
            }

        }

        @Override
        public Goods getItem(int position) {
            if (OkHttpUtils.hashkNewwork()) {
//            PgyCrashManager.reportCaughtException(mView,new Exception("getItem:null"));
                if (null == goodses)
                    return null;
//            PgyCrashManager.reportCaughtException(mView,new Exception("getItem"+goodses.getJSONObject(position)+","+goodses.toJSONString()));
                if(position<goodses.size()){

                return goodses.get(position);
                }else {
                    return null;
                }
            }else {
                if (null == mGoodsList)
                    return null;
                return mGoodsList.get(position);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
////            PgyCrashManager.reportCaughtException(mView,new Exception("getView"));
//            ProAdapter.ViewHolder holder;
//            if (convertView == null) {
//                convertView = View.inflate(mView, R.layout.item_gridview_fm_product, null);
//
//                holder = new ProAdapter.ViewHolder();
//                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
//                holder.textView = (TextView) convertView.findViewById(R.id.name_tv);
//                holder.old_price_tv = (TextView) convertView.findViewById(R.id.old_price_tv);
//                holder.price_tv = (TextView) convertView.findViewById(R.id.price_tv);
//
//                RelativeLayout.LayoutParams lLp = (RelativeLayout.LayoutParams) holder.imageView.getLayoutParams();
//                lLp.width=(UIUtils.getScreenWidth(mView)-UIUtils.dip2PX(135))/5;
//                float h = (mScreenWidth - ConvertUtil.dp2px(mView, 45.8f)) / 6;
//                lLp.height = lLp.width;
//                holder.imageView.setLayoutParams(lLp);
//                convertView.setTag(holder);
//            } else {
//                holder = (ProAdapter.ViewHolder) convertView.getTag();
//            }
//
//            return convertView;
//        }

        @Override
        protected void convert(BaseAdapterHelper helper, Goods item) {
            final ImageView imageView=helper.getView(R.id.imageView);
            RelativeLayout.LayoutParams lLp = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
            lLp.width=(UIUtils.getScreenWidth(mView)-UIUtils.dip2PX(135))/5;
            lLp.height = lLp.width;
            imageView.setLayoutParams(lLp);
            if(item.getIs_on_sale()==null||!item.getIs_on_sale().equals("1")){
                helper.setVisible(R.id.iv_is_on_sale,true);
            }else {
                helper.setVisible(R.id.iv_is_on_sale,false);
            }
            if (OkHttpUtils.hashkNewwork()) {
                helper.setText(R.id.name_tv,item.getName());
//                PgyCrashManager.reportCaughtException(mView,new Exception("isNetData"+position+","+goodses.toJSONString()));
                String url="";
                List<Goods.Gallery> gallery=item.getGallery();
                if(gallery!=null&&gallery.size()>0){
                    url=gallery.get(0).getImg_url();
                }else {
                    url=item.getImg_url();
                }
                String path = FileUtil.getGoodsExternDir(url);
                File imageFile = new File(path);
                if (imageFile.exists()) {
                    LogUtils.logE("exists",imageFile.toString());
                    imageURL = "file://" + imageFile.toString();
                    ImageLoader.getInstance().displayImage(imageURL, imageView);
                } else {
                    ImageLoader.getInstance().displayImage(NetWorkConst.UR_PRODUCT_URL +
                                    url+"!400X400.png"
                            , imageView,IssueApplication.getCacheImageloaderOpstion());
                }

                final int goodId = item.getId();
                helper.setText(R.id.old_price_tv,"￥" +item.getMarket_price());
                ((TextView)helper.getView(R.id.old_price_tv)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                try {
                    float setPrice = mPriceDao.getProductPrice(goodId + "").getShop_price();
                    helper.setText(R.id.price_tv,setPrice == 0 ? "￥" + (int)item.getShop_price(): "￥" + (int)setPrice);
                }catch (Exception e){
                    helper.setText(R.id.price_tv,"￥" + (int)item.getShop_price());
                }

            }else {
                helper.setText(R.id.name_tv,mGoodsList.get(helper.getPosition()).getName());
                int position=helper.getPosition();
                String url="";
                List<Goods.Gallery> gallery=mGoodsList.get(position).getGallery();
                if(gallery!=null&&gallery.size()>0){
                    url=gallery.get(0).getImg_url();
                }else {
                    url=item.getImg_url();
                }

                String path = FileUtil.getGoodsExternDir(url+"!400X400.png");
                File imageFile = new File(path );
                if (imageFile.exists()) {
                    imageURL = "file://" + imageFile.toString()+"";
                    LogUtils.logE("fileEis",imageURL);
                    ImageLoader.getInstance().loadImage(imageURL, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                            imageView.setImageBitmap(bitmap);
                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {

                        }
                    });
//                    ImageLoader.getInstance().displayImage(imageURL, imageView,IssueApplication.getImageLoaderOption());
                } else {
//                    ImageLoader.getInstance().displayImage(NetWorkConst.UR_PRODUCT_URL +
//                                    mGoodsList.get(position).getImg_url() + "!400X400.png"
//                            , holder.imageView);
//                    ImageLoader.getInstance().displayImage(NetWorkConst.UR_PRODUCT_URL +
//                                    mGoodsList.get(position).getImg_url()
//                            , imageView);
                }
                final int goodId = mGoodsList.get(position).getId();
//                helper.setText(R.id.old_price_tv,"￥" + mGoodsList.get(position).getMarket_price());
                ((TextView)helper.getView(R.id.old_price_tv)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                float setPrice = mPriceDao.getProductPrice(goodId + "").getShop_price();
                helper.setText(R.id.price_tv,setPrice == 0 ? "￥" + (int)mGoodsList.get(position).getShop_price() : "￥" + (int)setPrice);
//                holder.price_tv.setText(setPrice == 0 ? "￥" + mGoodsList.get(position).getShop_price() : "￥" + setPrice);
            }
        }

        class ViewHolder {
            ImageView imageView;
            TextView textView;
            TextView old_price_tv;
            TextView price_tv;
        }
    }
    
    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }



    @Override
    public void onSuccessListener(int what, JSONObject ans) {
        mView.hideLoading();
        switch (what) {
            case NetWorkConst.WHAT_GOODS_LIST:
                isSend = false;
                pd.setVisibility(View.GONE);

//                PgyCrashManager.reportCaughtException(mView,new Exception("onSuccessListener:"+page+","+ans.toString()));
                if(isFirst){
                    page=1;
                    isFirst=false;
                }

                JSONArray goodsList = ans.getJSONArray(Constance.goodslist);
                dismissRefesh();
                if (AppUtils.isEmpty(goodsList)) {
                    LogUtils.logE("goodslist","empty");
                    isBottom = true;
                    if (page == 1) {
                        MyToast.show(mView, "数据查询不到");
//                        PgyCrashManager.reportCaughtException(mView,new Exception("数据查询不到: goodses = new JSONArray();"+page+","+goodses.toString()));
                        goodses = new ArrayList<>();
                        mProAdapter.replaceAll(goodses);
                    } else {
//                        PgyCrashManager.reportCaughtException(mView,new Exception("数据已经到底啦:"+page+","+ans.toString()));
                        MyToast.show(mView, "数据已经到底啦!");
                    }
                    //
//                    mProAdapter.notifyDataSetChanged();
                    return;
                }
//                LogUtils.logE("goodslist",goodsList.toJSONString());
                getDataSuccess(goodsList);

                break;

        }
    }

    private void refreshAttr() {
        if(!TextUtils.isEmpty(mView.attr)){
            for(int i=0;i<sceneAllAttrsTemp.size();i++){
                if("空间".equals(mView.attr)){
                    mView.currentCatogoryMain=4;
                    break;
                }else if("风格".equals(mView.attr)){
                    mView.currentCatogoryMain=2;
                    break;
                }else if("类型".equals(mView.attr)){
                    mView.currentCatogoryMain=3;
                    break;
                }else if("品牌".equals(mView.attr)){
                    mView.currentCatogoryMain=1;
                    break;
                }
            }
            mView.attr="";
            mView.refreshAttr();
        }else if(mView.is_hot){
            mView.currentCatogoryMain=0;
            mView.current=1;
            mView.refreshAttr();
        }else if(!TextUtils.isEmpty(mView.keyword)){
            keyword=mView.keyword;
            mView.keyword="";
            mView.currentCatogoryMain=0;
            mView.current=0;
            mView.refreshAttr();
        }else {
            mView.currentCatogoryMain=0;
            mView.current=0;
            mView.refreshAttr();
        }
    }


    @Override
    public void onFailureListener(int what, JSONObject ans) {
        mView.hideLoading();
        pd.setVisibility(View.GONE);
        this.page--;

        if (null != mPullToRefreshLayout) {
            mPullToRefreshLayout.setRefreshing(false);
        }
        MyToast.show(mView, "数据异常!");

    }


    private void getDataSuccess(JSONArray array) {
//        LogUtils.logE("goodsarray",array.toJSONString());
        List<Goods> temp=new Gson().fromJson(array.toString(),new TypeToken<List<Goods>>(){}.getType());
        if (1 == page){
//            goodses = array;

            goodses.clear();
//            goodses=new JSONArray();
            goodses.addAll(temp);
//            PgyCrashManager.reportCaughtException(mView,new Exception("1 == page:"+goodses.toJSONString()+","));
        }
        else if (null != goodses) {
//            PgyCrashManager.reportCaughtException(mView,new Exception("null != goodses:"+goodses.toJSONString()+","+array.toJSONString()));
//            for (int i = 0; i < temp.size(); i++) {
//                goodses.add(temp.get(i));
//            }
            goodses.addAll(temp);

            if (AppUtils.isEmpty(array)){
//                PgyCrashManager.reportCaughtException(mView,new Exception("没有更多内容了:"+page+","));
                MyToast.show(mView, "没有更多内容了");
            }
        }
//        PgyCrashManager.reportCaughtException(mView,new Exception("mProAdapter:notifyDataSetChanged"+page+","+goodses.toJSONString()));
        mView.runOnUiThread(new Runnable() {
            @Override
            public void run() {

//                mProAdapter=new ProAdapter(mView,R.layout.item_gridview_fm_product);
//                mGoodsSv.setAdapter(mProAdapter);
//                PgyCrashManager.reportCaughtException(mView,new Exception("mProAdapter:setAdapter"+page+","+goodses.toJSONString()));
                mProAdapter.replaceAll(goodses);

//                mProAdapter.notifyDataSetChanged();
            }
        });

    }


    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        page = 1;
        sendGoodsList("0", 0);

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        page = page + 1;
        sendGoodsList("0", 0);
    }

    @Override
    public void onEndOfList(Object lastItem) {
        if(isBottom ||page==0){
            return;
        }
        if(mView.is_hot&&!mView.is_hot_send){
            mView.is_hot_send=true;
            filter_attr="is_best";
        }

        if(page==1&&(OkHttpUtils.hashkNewwork()?goodses.size()==0:mGoodsList.size()==0)){
            return;
        }
        page = page + 1;
        sendGoodsList("0", 0);
    }

    @Override
    public void onRefresh() {
        page = 1;
        isBottom=false;
        sendGoodsList("0", 0);
    }
}
