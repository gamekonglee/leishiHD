package bc.otlhd.com.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.bigkoo.convenientbanner.CBPageAdapter;
import com.bigkoo.convenientbanner.CBViewHolderCreator;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.lib.common.hxp.view.PullToRefreshLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bc.otlhd.com.R;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.cons.NetWorkConst;
import bc.otlhd.com.listener.INetworkCallBack;
import bc.otlhd.com.ui.activity.MainActivity;
import bc.otlhd.com.ui.activity.product.ProDetailActivity;
import bc.otlhd.com.ui.activity.product.SelectGoodsActivity;
import bc.otlhd.com.ui.activity.user.MessageDetailActivity;
import bc.otlhd.com.ui.adapter.ItemAdapter;
import bc.otlhd.com.ui.fragment.HomeFragment;
import bc.otlhd.com.utils.ConvertUtil;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;
import bocang.utils.UniversalUtil;

/**
 * @author Jun
 * @time 2017/1/7  20:31
 * @desc ${TODD}
 */
public class HomeController extends BaseController implements INetworkCallBack, AdapterView.OnItemClickListener, PullToRefreshLayout.OnRefreshListener {
    private final HomeFragment mView;
    private TextSwitcher textSwitcher_title;
    private int curStr;
    private List<String> paths = new ArrayList<>();
    private List<String> ImageLinks = new ArrayList<>();
    private ConvenientBanner mConvenientBanner;
    public int mScreenWidth;
    private TextView typeTv, styleTv, spaceTv;
    private GridView itemGridView, mProGridView;
    private ImageView lineIv;
    private HashMap<String, String> typeMap = new HashMap<>();
    private HashMap<String, String> spaceMap = new HashMap<>();
    private HashMap<String, String> styleMap = new HashMap<>();
    //下级选项名称列表
    private List<String> typeList = new ArrayList<>();
    private List<String> spaceList = new ArrayList<>();
    private List<String> styleList = new ArrayList<>();
    //对应的按钮图片
    private List<String> typeResList = new ArrayList<>();
    private List<String> spaceResList = new ArrayList<>();
    private List<String> styleResList = new ArrayList<>();

    //对应分类ID
    private List<String> typeIdList = new ArrayList<>();
    private List<String> spaceIdList = new ArrayList<>();
    private List<String> styleIdList = new ArrayList<>();

    //对应的筛选值列表
    private List<Integer> typeGoodsIdList = new ArrayList<>();
    private List<Integer> spaceGoodsIdList = new ArrayList<>();
    private List<Integer> styleGoodsIdList = new ArrayList<>();
    private int page = 1;
    private List<String> nameList;
    private List<String> iDList;
    private List<String> imageResList;
    private ItemAdapter mItemAdapter;
    private TextView mCheckedTv, moreTv;//当前被选中的tab
    private ProAdapter mProAdapter;
    private JSONArray goodses;
    private PullToRefreshLayout mPullToRefreshLayout;
    private Intent mIntent;
    private JSONArray mArticlesArray;
    private ProgressBar pd;


    public HomeController(HomeFragment v) {
        super();
        mView = v;
        initView();
        initData();
        initViewData();
        mView.setShowDialog(true);
    }

    private void initData() {
        typeList.clear();
        spaceList.clear();
        styleList.clear();
        typeResList.clear();
        spaceResList.clear();
        styleResList.clear();

        typeMap.put("中式", "type_0");
        typeMap.put("田园", "type_1");
        typeMap.put("美式", "type_2");
        typeMap.put("欧式", "type_3");
        typeMap.put("现代简约", "type_4");
        typeMap.put("新中式", "type_5");
        typeMap.put("后现代奢华", "type_6");
        typeMap.put("更多", "type_7");

        spaceMap.put("25㎡-35㎡", "space_0");
        spaceMap.put("20㎡-30㎡", "space_1");
        spaceMap.put("15㎡-30㎡", "space_2");
        spaceMap.put("30㎡以上", "space_3");

        styleMap.put("客厅", "style_0");
        styleMap.put("书房", "style_1");
        styleMap.put("餐厅", "style_2");
        styleMap.put("会议室", "style_3");
        styleMap.put("阳台", "style_4");
        styleMap.put("卧室", "style_5");
        styleMap.put("卫生间", "style_6");
        styleMap.put("玄关/过道/廊道", "style_7");
        styleMap.put("楼梯/拐角", "style_8");

        //默认选中类型
        nameList = typeList;
        imageResList = typeResList;
        iDList = typeIdList;
        mItemAdapter.setDatas(nameList, imageResList);
    }


    private void initView() {
        textSwitcher_title = (TextSwitcher) mView.getActivity().findViewById(R.id.textSwitcher_title);
        typeTv = (TextView) mView.getActivity().findViewById(R.id.typeTv);
        styleTv = (TextView) mView.getActivity().findViewById(R.id.styleTv);
        spaceTv = (TextView) mView.getActivity().findViewById(R.id.spaceTv);
        moreTv = (TextView) mView.getActivity().findViewById(R.id.moreTv);
        lineIv = (ImageView) mView.getActivity().findViewById(R.id.lineIv);
        itemGridView = (GridView) mView.getActivity().findViewById(R.id.itemGridView);
        mItemAdapter = new ItemAdapter(mView.getContext());
        itemGridView.setAdapter(mItemAdapter);
        itemGridView.setOnItemClickListener(this);
        mProGridView = (GridView) mView.getActivity().findViewById(R.id.priductGridView);
        mProGridView.setOnItemClickListener(this);
        mProAdapter = new ProAdapter();
        mProGridView.setAdapter(mProAdapter);

        mPullToRefreshLayout = ((PullToRefreshLayout) mView.getActivity().findViewById(R.id.refresh_view));
        mPullToRefreshLayout.setOnRefreshListener(this);

        mScreenWidth = mView.getActivity().getResources().getDisplayMetrics().widthPixels;
        mConvenientBanner = (ConvenientBanner) mView.getActivity().findViewById(R.id.convenientBanner);
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) mConvenientBanner.getLayoutParams();
        rlp.width = mScreenWidth;
        rlp.height = (int) (mScreenWidth * (360f / 640f));
        mConvenientBanner.setLayoutParams(rlp);
        pd = (ProgressBar) mView.getActivity().findViewById(R.id.pd);

    }

    /**
     * 初始化加载数据
     */
    private void initViewData() {
        sendBanner();
        sendGoodsType();
        if(AppUtils.isEmpty(mArticlesArray)){
            sendArticle();
        }
        page = 1;
        sendGoodsList(page, 20, null, null, null, null, null, null);
    }

    /**
     * 产品类别
     */
    private void sendGoodsType() {
        mNetWork.sendGoodsType(1, 20, null, null, this);
    }

    /**
     * @param page
     * @param per_page
     * @param brand      品牌
     * @param category   类型
     * @param shop
     * @param keyword
     * @param sort_key
     * @param sort_value
     */
    private void sendGoodsList(int page, int per_page, String brand, String category, String shop, String keyword, String sort_key, String sort_value) {
        mNetWork.sendRecommendGoodsList(page, per_page, brand, category, null, shop, keyword, sort_key, sort_value, this);
    }

    /**
     * 滚动新闻
     */
    private void getNews(final JSONArray jsonArray) {
//        if(!AppUtils.isEmpty(textSwitcher_title)) return;
        textSwitcher_title.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                final TextView tv = new TextView(mView.getActivity());
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                tv.setGravity(Gravity.CENTER_VERTICAL);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mNewsPoistion == 0)
                            return;
                        String url = jsonArray.getJSONObject(mNewsPoistion).getString(Constance.url);
                        Intent intent = new Intent(mView.getActivity(), MessageDetailActivity.class);
                        intent.putExtra(Constance.url, url);
                        mView.startActivity(intent);
                    }
                });
                return tv;
            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mNewsPoistion = curStr++ % jsonArray.length();
                textSwitcher_title.setText(jsonArray.getJSONObject(mNewsPoistion).getString(Constance.title));
                //                textSwitcher_title.setText(titles.get(curStr++ % titles.size()));

                handler.postDelayed(this, 5000);
            }
        }, 1000);
    }


    private int mNewsPoistion = 0;


    private void sendBanner() {
        mNetWork.sendBanner(this)
        ;
    }

    /**
     * 广告图
     */
    private void getAd() {
        mConvenientBanner.setPages(
                new CBViewHolderCreator<NetworkImageHolderView>() {
                    @Override
                    public NetworkImageHolderView createHolder() {
                        return new NetworkImageHolderView();
                    }
                }, paths);
        mConvenientBanner.setPageIndicator(new int[]{R.drawable.dot_unfocuse, R.drawable.dot_focuse});
    }

    /**
     * 类型
     */
    public void getType() {
        mCheckedTv = typeTv;
        typeTv.setTextColor(mView.getResources().getColor(R.color.colorPrimaryRed));

        nameList = typeList;
        iDList = typeIdList;
        imageResList = typeResList;
        mItemAdapter.setDatas(nameList, imageResList);

    }

    /**
     * 获得当前被选中的Button距离左侧的距离
     */
    public float getCurrentCheckedRadioLeft(View v) {
        if (v == typeTv) {
            return 0f;
        } else if (v == spaceTv) {
            return mScreenWidth / 3f;
        } else if (v == styleTv) {
            return mScreenWidth * 2f / 3f;
        }
        return 0f;
    }

    /**
     * 风格
     */
    public void getStyle() {
        mCheckedTv = styleTv;
        styleTv.setTextColor(mView.getResources().getColor(R.color.colorPrimaryRed));
        nameList = styleList;
        iDList = styleIdList;
        imageResList = styleResList;
        mItemAdapter.setDatas(nameList, imageResList);
    }

    /**
     * 空间
     */
    public void getSpace() {
        mCheckedTv = spaceTv;
        spaceTv.setTextColor(mView.getResources().getColor(R.color.colorPrimaryRed));
        nameList = spaceList;
        iDList = spaceIdList;
        imageResList = spaceResList;
        mItemAdapter.setDatas(nameList, imageResList);
    }

    public void reSetTextColor() {
        typeTv.setTextColor(Color.GRAY);
        spaceTv.setTextColor(Color.GRAY);
        styleTv.setTextColor(Color.GRAY);
        typeTv.setBackgroundColor(Color.WHITE);
        spaceTv.setBackgroundColor(Color.WHITE);
        styleTv.setBackgroundColor(Color.WHITE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == itemGridView) {
            Intent intent = new Intent(mView.getActivity(), SelectGoodsActivity.class);
            String categoriesId = iDList.get(position);
            intent.putExtra(Constance.categories, categoriesId);
            mView.getActivity().startActivity(intent);

        } else if (parent == mProGridView) {
            Intent intent = new Intent(mView.getActivity(), ProDetailActivity.class);
            int productId = goodses.getJSONObject(position).getInt(Constance.id);
            intent.putExtra(Constance.product, productId);
            mView.getActivity().startActivity(intent);
        }
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        page = 1;
        int per_page = 20;
        String brand = "";
        String category = "";
        String shop = "";
        String keyword = "";
        String sort_key = "";
        String sort_value = "";
        sendGoodsList(page, per_page, brand, category, shop, keyword, sort_key, sort_value);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        int per_page = 20;
        String brand = "";
        String category = "";
        String shop = "";
        String keyword = "";
        String sort_key = "";
        String sort_value = "";
        sendGoodsList(++page, per_page, brand, category, shop, keyword, sort_key, sort_value);
    }

    class NetworkImageHolderView implements CBPageAdapter.Holder<String> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, String data) {
            imageView.setImageResource(R.drawable.bg_default);
            ImageLoader.getInstance().displayImage(data, imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //跳转到知道的网址
                    String link = ImageLinks.get(position);
                    if (!AppUtils.isEmpty(link)) {
                        mIntent = new Intent();
                        mIntent.setAction(Intent.ACTION_VIEW);
                        mIntent.setData(Uri.parse(ImageLinks.get(position)));
                        mView.startActivity(mIntent);
                    }

                }
            });
        }
    }

    public void setResume() {
        // 开始自动翻页
        mConvenientBanner.startTurning(UniversalUtil.randomA2B(3000, 5000));
    }

    public void setPause() {
        // 停止翻页
        mConvenientBanner.stopTurning();
    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        switch (requestCode) {
            case NetWorkConst.RECOMMENDPRODUCT:
                if (null == mView.getActivity() || mView.getActivity().isFinishing())
                    return;
                if (null != mPullToRefreshLayout) {
                    mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                    mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
                getGoodsList(ans);
                break;
            case NetWorkConst.BANNER:
                if (null == mView.getActivity() || mView.getActivity().isFinishing())
                    return;
                JSONArray babbersArray = ans.getJSONArray(Constance.banners);
                for (int i = 0; i < babbersArray.length(); i++) {
                    JSONObject photoObject = babbersArray.getJSONObject(i).getJSONObject(Constance.photo);
                    String imageUri = photoObject.getString(Constance.large);
                    paths.add(imageUri);
                    ImageLinks.add(babbersArray.getJSONObject(i).getString(Constance.link));
                }
                getAd();
                break;
            case NetWorkConst.CATEGORY:
                getGoodsType(ans);
                pd.setVisibility(View.INVISIBLE);
                break;
            case NetWorkConst.ARTICLELIST:
                mArticlesArray = ans.getJSONArray(Constance.articles);

                getNews(mArticlesArray);
                break;

        }
    }

    /**
     * 获取文章列表
     */
    private void sendArticle() {
        int page = 1;
        int per_page = 20;
        mNetWork.sendArticle(page, per_page, this);
    }


    /**
     * 获取产品分类列表
     *
     * @param ans
     */
    private void getGoodsList(JSONObject ans) {

        JSONArray goodsList = ans.getJSONArray(Constance.goodsList);

        if (1 == page)
            goodses = goodsList;
        else if (null != goodses) {
            for (int i = 0; i < goodsList.length(); i++) {
                goodses.add(goodsList.getJSONObject(i));
            }

            if (AppUtils.isEmpty(goodsList))
                MyToast.show(mView.getActivity(), "没有更多内容了");
        }

        mProAdapter.notifyDataSetChanged();
    }

    /**
     * 获取产品分类列表
     *
     * @param ans
     */
    private void getGoodsType(JSONObject ans) {
        //重新获取，先清空列表
        typeList.clear();
        spaceList.clear();
        styleList.clear();
        typeResList.clear();
        spaceResList.clear();
        styleResList.clear();
        nameList.clear();
        imageResList.clear();

        JSONArray all_attr_list = ans.getJSONArray(Constance.categories);
        ((MainActivity) mView.getActivity()).mCategories = all_attr_list;

        if (AppUtils.isEmpty(all_attr_list))
            return;
        for (int i = 0; i < all_attr_list.length(); i++) {
            JSONObject goodsAllAttr = all_attr_list.getJSONObject(i);
            if (i == 0) {//类型
                String attrName = goodsAllAttr.getString(Constance.name);
                String attrId = goodsAllAttr.getString(Constance.id);
                typeTv.setText(attrName);
                JSONArray attr_list = goodsAllAttr.getJSONArray(Constance.categories);
                for (int j = 0; j < attr_list.length(); j++) {
                    if (j <= 6) {
                        typeList.add(attr_list.getJSONObject(j).getString(Constance.name));
                        typeIdList.add( attr_list.getJSONObject(j).getString(Constance.id));
                        typeResList.add(attr_list.getJSONObject(j).getString(Constance.thumbs));
                    }
                }
                if (attr_list.length() >= 7) {
                    typeList.add("更多");
                    typeIdList.add(attrId);
                    typeResList.add("");
                }

            } else if (i == 1) {//空间
                String attrName = goodsAllAttr.getString(Constance.name);
                String attrId = goodsAllAttr.getString(Constance.id);
                spaceTv.setText(attrName);
                JSONArray attr_list = goodsAllAttr.getJSONArray(Constance.categories);
                for (int j = 0; j < attr_list.length(); j++) {
                    if (j <= 6) {
                        spaceList.add(attr_list.getJSONObject(j).getString(Constance.name));
                        spaceIdList.add( attr_list.getJSONObject(j).getString(Constance.id));
                        spaceResList.add(attr_list.getJSONObject(j).getString(Constance.thumbs));
                    }
                }
                if (attr_list.length() >= 7) {
                    spaceList.add("更多");
                    spaceIdList.add(attrId);
                    spaceResList.add("");
                }

            } else if (i == 2) {//风格
                String attrName = goodsAllAttr.getString(Constance.name);
                String attrId = goodsAllAttr.getString(Constance.id);
                styleTv.setText(attrName);
                JSONArray attr_list = goodsAllAttr.getJSONArray(Constance.categories);
                for (int j = 0; j < attr_list.length(); j++) {
                    if (j <= 6) {
//                        String name = attr_list.getJSONObject(j).getString(Constance.name);
//                        String id = attr_list.getJSONObject(j).getString(Constance.id);
//                        styleList.add(name);
//                        styleIdList.add(id);
//                        String ss = styleMap.get(name);
//                        if (ss != null) {
//                            styleResList.add(ResUtil.getResMipmap(mView.getActivity(), ss));
//                        } else {
//                            styleResList.add(ResUtil.getResMipmap(mView.getActivity(), "type_0"));
//                        }
                        styleList.add(attr_list.getJSONObject(j).getString(Constance.name));
                        styleIdList.add( attr_list.getJSONObject(j).getString(Constance.id));
                        styleResList.add(attr_list.getJSONObject(j).getString(Constance.thumbs));
                    }
                }
                if (attr_list.length() >= 7) {
                    styleList.add("更多");
                    styleIdList.add(attrId);
                    styleResList.add("");
                }

            }
        }

        if (AppUtils.isEmpty(moreTv.getText().toString())) {
            moreTv.setText("更多");
        }

        mItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        //        mView.hideLoading();
        if (null == mView.getActivity() || mView.getActivity().isFinishing())
            return;
        this.page--;
        if (null != mPullToRefreshLayout) {
            mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        }

        if(requestCode.equals(NetWorkConst.CATEGORY)){
            pd.setVisibility(View.INVISIBLE);
        }
    }

    private class ProAdapter extends BaseAdapter {
        public ProAdapter() {
        }

        @Override
        public int getCount() {
            if (null == goodses)
                return 0;
            return goodses.length();
        }

        @Override
        public JSONObject getItem(int position) {
            if (null == goodses)
                return null;
            return goodses.getJSONObject(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mView.getActivity(), R.layout.item_gridview_fm_product, null);

                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                holder.textView = (TextView) convertView.findViewById(R.id.name_tv);
                holder.old_price_tv = (TextView) convertView.findViewById(R.id.old_price_tv);
                holder.price_tv = (TextView) convertView.findViewById(R.id.price_tv);
                RelativeLayout.LayoutParams lLp = (RelativeLayout.LayoutParams) holder.imageView.getLayoutParams();
                float h = (mScreenWidth - ConvertUtil.dp2px(mView.getActivity(), 45.8f)) / 2;
                lLp.height = (int) h;
                holder.imageView.setLayoutParams(lLp);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.textView.setText(goodses.getJSONObject(position).getString(Constance.name));
            String imagePath = goodses.getJSONObject(position).getJSONObject(Constance.default_photo).getString(Constance.large);
            ImageLoader.getInstance().displayImage(imagePath, holder.imageView);
            holder.old_price_tv.setText("￥" + goodses.getJSONObject(position).getString(Constance.price));
            holder.old_price_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.price_tv.setText("￥" + goodses.getJSONObject(position).getString(Constance.current_price));
            return convertView;
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
}
