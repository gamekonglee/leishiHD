package bc.otlhd.com.controller.product;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.convenientbanner.CBPageAdapter;
import com.bigkoo.convenientbanner.CBViewHolderCreator;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.google.gson.Gson;
import com.lib.common.hxp.view.GridViewForScrollView;
import com.lib.common.hxp.view.ListViewForScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shuyu.gsyvideoplayer.GSYVideoPlayer;
import com.shuyu.gsyvideoplayer.utils.FileUtils;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import bc.otlhd.com.R;
import bc.otlhd.com.bean.Goods;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.cons.NetWorkConst;
import bc.otlhd.com.controller.BaseController;
import bc.otlhd.com.data.CartDao;
import bc.otlhd.com.data.GoodsDao;
import bc.otlhd.com.data.SetPriceDao;
import bc.otlhd.com.listener.INumberInputListener;
import bc.otlhd.com.listener.ITwoCodeListener;
import bc.otlhd.com.net.OkHttpUtils;
import bc.otlhd.com.ui.activity.IssueApplication;
import bc.otlhd.com.ui.activity.product.ProductDetailHDActivity;
import bc.otlhd.com.ui.activity.programme.DiyActivity;
import bc.otlhd.com.ui.activity.programme.ImageDetailActivity;
import bc.otlhd.com.ui.adapter.ParamentAdapter;
import bc.otlhd.com.ui.view.LandLayout02Video;
import bc.otlhd.com.ui.view.NumberInputView;
import bc.otlhd.com.ui.view.popwindow.TwoCodeSharePopWindow;
import bc.otlhd.com.utils.FileUtil;
import bc.otlhd.com.utils.LogUtils;
import bc.otlhd.com.utils.MyShare;
import bc.otlhd.com.utils.net.HttpListener;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/4/1 14:39
 * @description :
 */
public class ProductDetailHDController extends BaseController {
    private ProductDetailHDActivity mView;
    private ConvenientBanner mConvenientBanner;
    public List<String> paths = new ArrayList<>();
    private ListViewForScrollView properties_lv;
    private JSONArray propertiesList;
    private ProAdapter mAdapter;
    private JSONObject itemObject;
    private TextView name_tv, proPriceTv;
    private ParamentAdapter mParamentAdapter;
    private GridViewForScrollView parameter_lv;
    private WebView mWebView;
    private ImageView collectIv;
    private NumberInputView mNumberInputView;
    private String mPrice = "";
    private Intent mIntent;
    private int mAmount = 1;
    private String mProperty = "";
    private SetPriceDao mPriceDao;
    private LinearLayout main_ll;
    private String phone;
    public String imageURL;
    private LandLayout02Video play;
    private String video_url;


    public ProductDetailHDController(ProductDetailHDActivity v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {
        if (OkHttpUtils.hashkNewwork()) {
            getProductDetail(JSON.parseObject(new Gson().toJson(mView.goodses,Goods.class)), 1);
            sendProductDetail();
        } else {
            getProductDetail02(mView.mGoods);
        }
    }


    /**
     * 产品详情
     */
    public void sendProductDetail() {

        int id = mView.productId;
        if (AppUtils.isEmpty(id))
            return;

        mNetWork.sendProductDetail(id + "", mView, new HttpListener() {
            @Override
            public void onSuccessListener(int what, JSONObject ans) {
                mView.hideLoading();
                mView.goodses = new Gson().fromJson(ans.toString(),Goods.class);
                getProductDetail(ans, 0);
            }

            @Override
            public void onFailureListener(int what, JSONObject ans) {
                mView.hideLoading();
                MyToast.show(mView, "数据异常!");
            }
        });
    }


    private List<String> getAttrList() {
        return GoodsDao.getAttrForGood(mView.productId+"");
    }


    /**
     * 获取产品详情信息
     */
    private void getProductDetail02(Goods productObject) {
        if (AppUtils.isEmpty(productObject))
            return;
        paths = new ArrayList<>();
        final String productName = productObject.getName();
        mPrice = productObject.getShop_price() + "";
        name_tv.setText(productName);
        imageURL = "";
        File imageFile = new File(FileUtil.getGoodsExternDir(productObject.getImg_url()));
        if (imageFile.exists()) {
            imageURL = "file://" + imageFile.toString();
        } else {
            imageURL = NetWorkConst.UR_PRODUCT_URL + productObject.getImg_url();
        }


        paths.add(imageURL);


        mConvenientBanner.setPages(
                new CBViewHolderCreator<NetworkImageHolderView>() {
                    @Override
                    public NetworkImageHolderView createHolder() {
                        return new NetworkImageHolderView();
                    }
                }, paths);
        mConvenientBanner.setPageIndicator(new int[]{R.drawable.dot_unfocuse, R.drawable.dot_focuse});
        mParamentAdapter = new ParamentAdapter(getAttrList(), mView, 0);
        parameter_lv.setAdapter(mParamentAdapter);
//        parameter_lv.setDivider(null);//去除listview的下划线
        final int goodId = productObject.getId();
        float setPrice = mPriceDao.getProductPrice(goodId + "").getShop_price();
        proPriceTv.setText(setPrice == 0 ? "￥" + mPrice : "￥" + setPrice);
//        getWebView(productObject.getGoods_desc());

    }

    /**
     * 获取产品详情信息
     */
    private void getProductDetail(com.alibaba.fastjson.JSONObject productObject, int type) {
        LogUtils.logE("proDetail",productObject.toString());
        paths = new ArrayList<>();
        JSONArray attachArray = null;
        final String value = productObject.getString(Constance.goods_desc);
        final String productName = productObject.getString(Constance.name);
        String video_urlT=productObject.getString(Constance.video_url);
        if(!TextUtils.isEmpty(video_urlT)){
            video_url="https://bocang.oss-cn-shenzhen.aliyuncs.com/leishi/"+video_urlT;
            LogUtils.logE("video_url",video_url);
//            startPlayVideo("https://bocang.oss-cn-shenzhen.aliyuncs.com/leishi/"+video_url);
        }
//        startPlayVideo(video_url);
        mPrice = productObject.getString(Constance.shop_price);
        name_tv.setText(productName);
        imageURL = "";
        File imageFile = new File(FileUtil.getGoodsExternDir(productObject.getString(Constance.img_url)));
        if (imageFile.exists()) {
            imageURL = "file://" + imageFile.toString();
        } else {
            imageURL = NetWorkConst.UR_PRODUCT_URL + productObject.getString(Constance.img_url);
        }
        JSONObject attr=productObject.getJSONObject(Constance.attr);

        if (type == 0) {
            com.alibaba.fastjson.JSONArray array = productObject.getJSONArray(Constance.gallery);
            paths.add(imageURL);
//            if (array.size() == 0) {
//
//            } else {
//                for (int i = 0; i < array.size(); i++) {
//                    paths.add(array.getJSONObject(0).getString(Constance.img_url));
//                }
//            }
            getWebView(value);
            attachArray = productObject.getJSONObject(Constance.attr).getJSONArray(Constance.pro);
        } else {
            paths.add(imageURL);
            try {
                attachArray = productObject.getJSONArray(Constance.attr);
            } catch (Exception e) {
                attachArray = productObject.getJSONObject(Constance.attr).getJSONArray(Constance.pro);
            }

        }

        mConvenientBanner.setPages(
                new CBViewHolderCreator<NetworkImageHolderView>() {
                    @Override
                    public NetworkImageHolderView createHolder() {
                        return new NetworkImageHolderView();
                    }
                }, paths);
        mConvenientBanner.setPageIndicator(new int[]{R.drawable.dot_unfocuse, R.drawable.dot_focuse});
        List<String> attachs = new ArrayList<>();
        if(attr!=null){
            JSONArray spe=attr.getJSONArray(Constance.spe);
            if(spe!=null&&spe.size()>0){
                String name=spe.getJSONObject(0).getString(Constance.name);
                JSONArray values=spe.getJSONObject(0).getJSONArray(Constance.values);
                if(values!=null&&values.size()>0){
                    String label=values.getJSONObject(0).getString(Constance.label);
                    attachs.add(name+":"+label);
                }
                LogUtils.logE("spe",spe.toJSONString());
            }
        }

        if(attachArray!=null) {
            for(int i=0;i<attachArray.size();i++){
                for(int j=0;j<attachArray.size();j++){
                    if(i!=j){
                    if(attachArray.getJSONObject(i).getString(Constance.name).equals(attachArray.getJSONObject(j).getString(Constance.name))){
                        String value1=attachArray.getJSONObject(i).getString(Constance.VALUE);
                        attachArray.getJSONObject(i).put(Constance.VALUE,value1+"+"+attachArray.getJSONObject(j).getString(Constance.VALUE));
                        attachArray.remove(j);
                        j--;
                        i--;
                    }
                    }
                }
            }
            for (int i = 0; i < attachArray.size(); i++) {
                if (!AppUtils.isEmpty(attachArray.getJSONObject(i).getString(Constance.VALUE))) {
                    String name = attachArray.getJSONObject(i).getString(Constance.name);
                    String valeu = attachArray.getJSONObject(i).getString(Constance.VALUE);
                    attachs.add(name + ":" + valeu);
                }

            }
        }


        mParamentAdapter = new ParamentAdapter(attachs, mView, 0);
        parameter_lv.setAdapter(mParamentAdapter);
//        parameter_lv.setDivider(null);//去除listview的下划线
        final int goodId = productObject.getInteger(Constance.id);
        float setPrice = mPriceDao.getProductPrice(goodId + "").getShop_price();
        proPriceTv.setText(setPrice == 0 ? "￥" + mPrice : "￥" + setPrice);

    }

    /**
     * 分享产品
     */
    public void sendShareProduct() {
        setShare();
    }

    /**
     * 联系客服
     */
    public void sendCall() {
        mView.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(NetWorkConst.QQURL)));
    }

    /**
     * 配配看
     */
    public void sendDiy() {
        mIntent = new Intent(mView, DiyActivity.class);
        if (OkHttpUtils.hashkNewwork()) {
            if (AppUtils.isEmpty(mView.goodses)) {
                MyToast.show(mView, "还没加载完毕，请稍后再试");
                return;
            }
//            mIntent.putExtra(Constance.product, mView.goodses.toJSONString());
            LogUtils.logE("gJson",new Gson().toJson(mView.goodses,Goods.class));
            mIntent.putExtra(Constance.product, new Gson().toJson(mView.goodses,Goods.class));
            mIntent.putExtra(Constance.property, mView.mProperty);
            mView.startActivity(mIntent);
            mView.finish();
        }else{
            mIntent.putExtra(Constance.product, mView.mGoods);
            mView.startActivity(mIntent);
            mView.finish();
        }
    }

    /**
     * 加入购物车
     */
    public void sendGoCart() {

        Goods goods = new Goods();

        if (OkHttpUtils.hashkNewwork()) {
            if (AppUtils.isEmpty(mView.goodses))
                return;
            goods.setName(mView.goodses.getName());

            goods.setBuyCount(mAmount);
            goods.setGoods_number(mAmount + "");
            goods.setShop_price(mView.goodses.getShop_price());
            goods.setId(mView.goodses.getId());
            goods.setMarket_price(mView.goodses.getMarket_price());
            goods.setImg_url(mView.goodses.getImg_url());
        } else {
            goods = mView.mGoods;
            goods.setBuyCount(mAmount);
            goods.setGoods_number(mAmount + "");
        }

        CartDao dao = new CartDao(mView);
        List<Goods> carts=dao.getAll();
        for(int i=0;i<carts.size();i++){
            if(carts.get(i).getId()==goods.getId()){
                int j=goods.getBuyCount();
                goods.setBuyCount(j+carts.get(i).getBuyCount());
            }
        }
        if (-1 != dao.replaceOne(goods)) {
            Toast.makeText(mView, "已添加到购物车!", Toast.LENGTH_LONG).show();
            IssueApplication.mCartCount = dao.getCount();
            //            IssueApplication.mCartCount += 1;
            EventBus.getDefault().post(Constance.CARTCOUNT);
        } else {
            Toast.makeText(mView, "添加到购物车失败!", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 分享
     */
    public void setShare() {
        JSONObject userinfo=((IssueApplication) mView.getApplication()).mUserInfo;
        if(userinfo!=null){
            phone = ((IssueApplication) mView.getApplication()).mUserInfo.getString(Constance.phone);
        }else {
            phone="13927273545";
        }

         String path="";
         String title="";
        String imagePath="";
        String price="";
        if(MyShare.get(mView).getBoolean(Constance.SET_PRICE)){
            mPriceDao = new SetPriceDao(mView);
            if(OkHttpUtils.hashkNewwork()){
                price=mPriceDao.getProductPrice(mView.goodses.getId()+"").getShop_price()+"";
            }else {
                price=mPriceDao.getProductPrice(mView.mGoods.getId()+"").getShop_price()+"";
            }
        }
        if(OkHttpUtils.hashkNewwork()){
            if (AppUtils.isEmpty(mView.goodses))
                return;
            title = "来自 " + mView.goodses.getName()+ " 产品的分享";
            if(TextUtils.isEmpty(price)){
            price=mView.goodses.getShop_price()+"";
            }
            path = NetWorkConst.SHAREPRODUCT+mView.goodses.getId()+"&phone="+phone+"&price="+price;
            imagePath = NetWorkConst.PRODUCT_URL+mView.goodses.getImg_url();
        }else{
            if(TextUtils.isEmpty(price)){
                price=mView.mGoods.getShop_price()+"";
            }
            title = "来自 " + mView.mGoods.getName() + " 产品的分享";
            path = NetWorkConst.SHAREPRODUCT+mView.mGoods.getId()+"&phone="+phone+"&price="+price;
            imagePath =NetWorkConst.UR_PRODUCT_URL+mView.mGoods.getImg_url();
        }

        final String finalPath = path;
        final String finalTitle = title;
        final String finalImagePath = imagePath;
        IssueApplication.sharePath=path;
        IssueApplication.shareRemark=finalTitle;
        TwoCodeSharePopWindow popWindow = new TwoCodeSharePopWindow(mView, mView);
        popWindow.onShow(main_ll);
        popWindow.setListener(new ITwoCodeListener() {
            @Override
            public void onTwoCodeChanged(String path) {
            }
        });
    }

    class NetworkImageHolderView implements CBPageAdapter.Holder<String> {
        private View layout;
        //        private ImageView imageView;

        @Override
        public View createView(Context context) {
            // 你可以通过layout文件来创建，也可以像我一样用代码创建z，不一定是Image，任何控件都可以进行翻页
            layout = View.inflate(context, R.layout.layout_conven,null);
            ImageView iv_open= (ImageView) layout.findViewById(R.id.iv_open);
            iv_open.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(TextUtils.isEmpty(video_url)){
                        return;
                    }
                    play.setVisibility(View.VISIBLE);
                    mConvenientBanner.setVisibility(View.GONE);
                    startPlayVideo(video_url);
                }
            });
            ImageView imageView = (ImageView) layout.findViewById(R.id.iv_img);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            return layout;
        }

        @Override
        public void UpdateUI(Context context, final int position, String data) {
            ImageView imageView= (ImageView) layout.findViewById(R.id.iv_img);
            imageView.setImageResource(R.drawable.bg_default);
            ImageLoader.getInstance().displayImage(data, imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mView, ImageDetailActivity.class);
                    intent.putExtra(Constance.photo, paths.get(position));
                    mView.startActivity(intent);
                }
            });
        }
    }


    private void initView() {
        mConvenientBanner = (ConvenientBanner) mView.findViewById(R.id.convenientBanner);
        properties_lv = (ListViewForScrollView) mView.findViewById(R.id.properties_lv);
        name_tv = (TextView) mView.findViewById(R.id.name_tv);
        parameter_lv = (GridViewForScrollView) mView.findViewById(R.id.parameter_lv);
        proPriceTv = (TextView) mView.findViewById(R.id.proPriceTv);
        collectIv = (ImageView) mView.findViewById(R.id.collectIv);
        main_ll = (LinearLayout) mView.findViewById(R.id.main_ll);
        mWebView = (WebView) mView.findViewById(R.id.webView);
        play = (LandLayout02Video) mView.findViewById(R.id.play);

        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        mNumberInputView = (NumberInputView) mView.findViewById(R.id.number_input_et);
        mNumberInputView.setMax(10000);
        mNumberInputView.setListener(new INumberInputListener() {
            @Override
            public void onTextChange(int index) {
                mAmount = index;
            }
        });
        mPriceDao = new SetPriceDao(mView);

    }

    /**
     * 加载网页
     *
     * @param htmlValue
     */
    private void getWebView(String htmlValue) {
        String html = htmlValue;
        if(TextUtils.isEmpty(html))return;
        html = html.replace("<p><img src=\"", "<img src=\"" + NetWorkConst.UR_HOST);
        html = html.replace("</p>", "");
        html = "<meta name=\"viewport\" content=\"width=device-width\"> <div style=\"text-align:center\">" + html + " </div>";
        mWebView.loadData(html, "text/html; charset=UTF-8", null);//这种写法可以正确解析中文
    }
    /**
     * 播放视频
     * @param path
     */
    public void startPlayVideo(String path){
        play.setUp(path, true, new File(FileUtils.getPath()), "");

        //增加title
        play.getTitleTextView().setVisibility(View.GONE);
//        play.getTitleTextView().setText(name);
        //设置返回键
        play.getBackButton().setVisibility(View.VISIBLE);
        play.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play.setStandardVideoAllCallBack(null);
                GSYVideoPlayer.releaseAllVideos();
                play.setVisibility(View.GONE);
                mConvenientBanner.setVisibility(View.VISIBLE);
            }
        });
//        play.setBackgroundColor(Color.TRANSPARENT);
        //设置旋转
//        orientationUtils = new OrientationUtils(this, play);

        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
//        play.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                orientationUtils.resolveByClick();
//            }
//        });

        GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_DEFAULT);
        //循环播放
        play.setLooping(true);
        //是否可以滑动调整
        play.setIsTouchWiget(true);
        //设置返回按键功能
//        play.getBackButton().setVisibility(View.GONE);
//        play.setLockLand(true);
        play.setHideKey(true);
//        play.setBottomProgressBarDrawable(getResources().getDrawable(R.drawable.video_new_progress));

        //过渡动画
        initTransition();
    }
//
    private void initTransition() {
//        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            postponeEnterTransition();
//            ViewCompat.setTransitionName(play, IMG_TRANSITION);
//            addTransitionListener();
//            startPostponedEnterTransition();
//        } else {
            play.startPlayLogic();
//        }
    }

//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    private boolean addTransitionListener() {
//        transition = getWindow().getSharedElementEnterTransition();
//        if (transition != null) {
//            transition.addListener(new Transition.TransitionListener() {
//                @Override
//                public void onTransitionStart(Transition transition) {
//
//                }
//
//                @Override
//                public void onTransitionEnd(Transition transition) {
//                    play.startPlayLogic();
//                    transition.removeListener(this);
//                }
//
//                @Override
//                public void onTransitionCancel(Transition transition) {
//
//                }
//
//                @Override
//                public void onTransitionPause(Transition transition) {
//
//                }
//
//                @Override
//                public void onTransitionResume(Transition transition) {
//
//                }
//            });
//            return true;
//        }
//        return false;
//    }
    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    private class ProAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if (null == propertiesList)
                return 0;
            return propertiesList.size();
        }


        @Override
        public JSONObject getItem(int position) {
            if (null == propertiesList)
                return null;
            return propertiesList.getJSONObject(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mView, R.layout.item_properties, null);
                //
                holder = new ViewHolder();
                holder.properties_name = (TextView) convertView.findViewById(R.id.properties_name);
                holder.itemGridView = (GridViewForScrollView) convertView.findViewById(R.id.itemGridView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final String name = propertiesList.getJSONObject(position).getString(Constance.name);
            holder.properties_name.setText(name + ":");

            if (holder.itemGridView != null) {
                final JSONArray attrsList = propertiesList.getJSONObject(position).getJSONArray(Constance.values);
                if (attrsList.size() > 0 && AppUtils.isEmpty(attrsList.getJSONObject(0).getString("label")))
                    return convertView;
                final ItemProAdapter gridViewAdapter = new ItemProAdapter(attrsList);
                holder.itemGridView.setAdapter(gridViewAdapter);
                holder.itemGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        itemObject = attrsList.getJSONObject(position);
                        int ProperPrice = itemObject.getInteger(Constance.price);
                        gridViewAdapter.mCurrentPoistion = position;
                        gridViewAdapter.notifyDataSetChanged();
                        proPriceTv.setText("￥" + (Double.parseDouble(mPrice) + ProperPrice));
                        mProperty = itemObject.toJSONString();
                    }
                });
            }

            return convertView;
        }


        class ViewHolder {
            TextView properties_name;
            GridViewForScrollView itemGridView;
        }
    }


    private class ItemProAdapter extends BaseAdapter {
        public int mCurrentPoistion = 0;
        JSONArray mDatas;

        public ItemProAdapter(JSONArray datas) {
            this.mDatas = datas;
        }

        @Override
        public int getCount() {
            if (null == mDatas)
                return 0;
            return mDatas.size();
        }


        @Override
        public JSONObject getItem(int position) {
            if (null == mDatas)
                return null;
            return mDatas.getJSONObject(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mView, R.layout.item_properties02, null);
                //
                holder = new ViewHolder();
                holder.item_tv = (TextView) convertView.findViewById(R.id.item_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.item_tv.setText(mDatas.getJSONObject(position).getString("label"));
            holder.item_tv.setSelected(mCurrentPoistion == position ? true : false);

            return convertView;
        }

        class ViewHolder {
            TextView item_tv;
        }
    }
}
