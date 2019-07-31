package bc.otlhd.com.controller.product;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baiiu.filter.DropDownMenu;
import com.baiiu.filter.interfaces.OnFilterDoneListener;
import com.lib.common.hxp.view.PullToRefreshLayout;
import com.lib.common.hxp.view.PullableGridView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import bc.otlhd.com.R;
import bc.otlhd.com.bean.GoodPrices;
import bc.otlhd.com.bean.Goods;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.cons.NetWorkConst;
import bc.otlhd.com.controller.BaseController;
import bc.otlhd.com.data.GoodsDao;
import bc.otlhd.com.data.SetPriceDao;
import bc.otlhd.com.net.OkHttpUtils;
import bc.otlhd.com.ui.activity.IssueApplication;
import bc.otlhd.com.ui.activity.product.ProductDetailHDActivity;
import bc.otlhd.com.ui.adapter.ProductDropMenuAdapter;
import bc.otlhd.com.ui.fragment.HomeHDFragment;
import bc.otlhd.com.ui.view.ShowDialog;
import bc.otlhd.com.utils.ConvertUtil;
import bc.otlhd.com.utils.FileUtil;
import bc.otlhd.com.utils.MyShare;
import bc.otlhd.com.utils.UIUtils;
import bc.otlhd.com.utils.net.HttpListener;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/3/30 17:26
 * @description :
 */
public class HomeHDController extends BaseController implements OnFilterDoneListener, PullToRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, HttpListener {
    private HomeHDFragment mView;
    private DropDownMenu dropDownMenu;
    private JSONArray sceneAllAttrs;
    private PullToRefreshLayout mPullToRefreshLayout;
    private ProAdapter mProAdapter;
    private PullableGridView mGoodsSv;
    public int page = 1;
    private JSONArray goodses;
    private List<Goods> mGoodsList;
    private boolean initFilterDropDownView;
    private String imageURL = "";
    private Intent mIntent;
    public String keyword;
    private ProgressBar pd;
    public String mType;
    public String filter_attr;
    public String filter_attr02;
    private float mScreenWidth;
    private SetPriceDao mPriceDao;
    private Boolean isSetPrice;

    public HomeHDController(HomeHDFragment v) {
        mView = v;
        initView();
        initViewData();

    }

    private void initViewData() {
        page = 1;
        initFilterDropDownView = true;
        JSONObject userinfo=((IssueApplication) mView.getActivity().getApplication()).mUserInfo;
        if(userinfo!=null){
            int is_price = ((IssueApplication) mView.getActivity().getApplication()).mUserInfo.getInteger(Constance.is_price);
            String id = ((IssueApplication) mView.getActivity().getApplication()).mUserInfo.getString(Constance.id);
            isSetPrice = MyShare.get(UIUtils.getContext()).getBoolean(Constance.SET_PRICE);

            if (!isSetPrice && is_price == 1) {
                sendReadPrice(id);
                return;
            }
        }



        sendGoodsList("0", 0);
//        if (!OkHttpUtils.hashkNewwork()) {
//            mNetWork.sendGoodsList("0", page, 0, keyword, mType, filter_attr, true, 1, "",mView.getActivity(), this);
//        }


        pd.setVisibility(View.GONE);

    }

    private void initView() {
        dropDownMenu = (DropDownMenu) mView.getView().findViewById(R.id.dropDownMenu);
        mPullToRefreshLayout = ((PullToRefreshLayout) mView.getView().findViewById(R.id.mFilterContentView));
        mPullToRefreshLayout.setOnRefreshListener(this);
        mGoodsSv = (PullableGridView) mView.getView().findViewById(R.id.gridView);
        mProAdapter = new ProAdapter();
        mGoodsSv.setAdapter(mProAdapter);
        mGoodsSv.setOnItemClickListener(this);
        pd = (ProgressBar) mView.getView().findViewById(R.id.pd);
        mScreenWidth = mView.getResources().getDisplayMetrics().widthPixels;
        mPriceDao = new SetPriceDao(mView.getActivity());

    }

    private List<Integer> itemPosList = new ArrayList<>();//有选中值的itemPos列表，长度为3

    private void initFilterDropDownView(JSONArray sceneAllAttrs) {
        if (itemPosList.size() < sceneAllAttrs.size()) {
            itemPosList.add(0);
            itemPosList.add(0);
            itemPosList.add(0);
        }
        try{
            ProductDropMenuAdapter dropMenuAdapter = new ProductDropMenuAdapter(mView.getContext(), sceneAllAttrs, itemPosList, this);
            dropDownMenu.setMenuAdapter(dropMenuAdapter);
        }catch (Exception e){
        }

    }


    private void sendReadPrice(final String id) {
        mView.setShowDialog(true);
        mView.setShowDialog("正在获取自定义价格..");
        mView.showLoading();
        mNetWork.sendReadPrice(id, mView.getActivity(), new HttpListener() {
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
                mDialog.show(mView.getActivity(), "提示", "获取自定义价格失败,是否重新获取？", new ShowDialog.OnBottomClickListener() {
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
        mView.setShowDialog("获取数据中..");
        mView.showLoading();
        pd.setVisibility(View.GONE);


//        Log.v("520it", OkHttpUtils.hashkNewwork() + "");
        if (OkHttpUtils.hashkNewwork()) {
            mNetWork.sendGoodsList(c_id, page, okcat_id, keyword, mType, filter_attr, false, 20,"", mView.getActivity(), this);
            return;
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final List<Goods> goodsList = GoodsDao.getGoodsList(keyword, c_id, filter_attr02, mType, 20, page);

                    mView.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.hideLoading();

                            if (null == mView || mView.getActivity().isFinishing())
                                return;
                            if (null != mPullToRefreshLayout) {
                                dismissRefesh();
                            }

                            if (goodsList.size() == 0) {
                                if (page == 1) {
                                    MyToast.show(mView.getActivity(), "数据查询不到1");
                                    mGoodsList = new ArrayList<Goods>();
                                } else {
                                    MyToast.show(mView.getActivity(), "数据已经到底啦!");
                                }
                                dismissRefesh();
                                return;
                            }
                            getDataSuccess02(goodsList);
                        }
                    });

                }
            }).start();
        }


    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    private Integer[] goodsIds = new Integer[]{0, 0, 0};
    private String[] goodsNames = new String[]{"", "", ""};

    @Override
    public void onFilterDone(int titlePos, int itemPos, String itemStr) {
        dropDownMenu.close();
        if (0 == itemPos)
            itemStr = sceneAllAttrs.getJSONObject(titlePos).getString(Constance.filter_attr_name);
        dropDownMenu.setPositionIndicatorText(titlePos, itemStr);

        if (titlePos < itemPosList.size())
            itemPosList.remove(titlePos);
        itemPosList.add(titlePos, itemPos);

        if (OkHttpUtils.hashkNewwork()) {
            if (AppUtils.isEmpty(sceneAllAttrs.getJSONObject(titlePos).getJSONArray(Constance.attr_list).getJSONObject(itemPos).getInteger(Constance.goods_id))) {
                goodsIds[titlePos] = 0;
            } else {
                int index = sceneAllAttrs.getJSONObject(titlePos).getJSONArray(Constance.attr_list).getJSONObject(itemPos).getInteger(Constance.goods_id);
                if (index == 0) {
                    goodsIds[titlePos] = 999;
                } else {
                    goodsIds[titlePos] = index;
                }
            }
            filter_attr = goodsIds[0] + "." + goodsIds[1] + "." + goodsIds[2];
            if (AppUtils.isEmpty(filter_attr))
                return;
        } else {
            if (AppUtils.isEmpty(sceneAllAttrs.getJSONObject(titlePos).getJSONArray(Constance.attr_list).getJSONObject(itemPos).getInteger(Constance.goods_id))) {
                goodsNames[titlePos] = "";
            } else {
                String indexValue = sceneAllAttrs.getJSONObject(titlePos).getJSONArray(Constance.attr_list).getJSONObject(itemPos).getString(Constance.attr_value);
                goodsNames[titlePos] = indexValue;
            }
            filter_attr02 = goodsNames[0] + "." + goodsNames[1] + "." + goodsNames[2];
            if (AppUtils.isEmpty(filter_attr02))
                return;
        }

        pd.setVisibility(View.VISIBLE);
        page = 1;
        sendGoodsList("0", 0);

    }

    public void onBackPressed() {
        dropDownMenu.close();
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        page = 1;
        initFilterDropDownView = false;
        sendGoodsList("0", 0);

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        initFilterDropDownView = false;
        page = page + 1;
        sendGoodsList("0", 0);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mIntent = new Intent(mView.getActivity(), ProductDetailHDActivity.class);
        if (OkHttpUtils.hashkNewwork()) {
            mIntent.putExtra(Constance.product, goodses.getJSONObject(position).toJSONString());
        } else {
            mIntent.putExtra(Constance.id, mGoodsList.get(position).getId());
            mIntent.putExtra(Constance.product, mGoodsList.get(position));
        }

        mView.startActivity(mIntent);
    }

    private void dismissRefesh() {
        mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }

    private void getDataSuccess(JSONArray array) {
        if (1 == page)
            goodses = array;
        else if (null != goodses) {
            for (int i = 0; i < array.size(); i++) {
                goodses.add(array.getJSONObject(i));
            }

            if (AppUtils.isEmpty(array))
                MyToast.show(mView.getActivity(), "没有更多内容了");
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
                MyToast.show(mView.getActivity(), "没有更多内容了");
        }
        mProAdapter.notifyDataSetChanged();
    }


    public void selectSortType(int type) {

        switch (type) {
            case R.id.competitive_tv:
                mType = "is_best";//精品
                break;
            case R.id.new_tv:
                mType = "is_new";//新品
                break;
            case R.id.hot_tv:
                mType = "is_hot";//热销
                break;
        }
        page = 1;
        sendGoodsList("0", 0);
    }

    @Override
    public void onSuccessListener(int what, JSONObject ans) {
        mView.hideLoading();
        switch (what) {
            case NetWorkConst.WHAT_GOODS_LIST:
                pd.setVisibility(View.GONE);


                if (!OkHttpUtils.hashkNewwork()) {
                    if (AppUtils.isEmpty(sceneAllAttrs)) {
                        sceneAllAttrs = ans.getJSONArray(Constance.all_attr_list);
                        if (initFilterDropDownView)//重复setMenuAdapter会报错
                            initFilterDropDownView(sceneAllAttrs);
                    }
                    return;
                }

                if (null != mPullToRefreshLayout) {
                    dismissRefesh();
                }

                if (AppUtils.isEmpty(sceneAllAttrs)) {
                    sceneAllAttrs = ans.getJSONArray(Constance.all_attr_list);
                    if (initFilterDropDownView)//重复setMenuAdapter会报错
                        initFilterDropDownView(sceneAllAttrs);
                }

                JSONArray goodsList = ans.getJSONArray(Constance.goodslist);
                if (AppUtils.isEmpty(goodsList)) {
                    if (page == 1) {
                        MyToast.show(mView.getActivity(), "数据查询不到");
                        goodses = new JSONArray();
                    } else {
                        MyToast.show(mView.getActivity(), "数据已经到底啦!");
                    }
                    //

                    dismissRefesh();
                    return;
                }
                getDataSuccess(goodsList);

                break;

        }
    }

    @Override
    public void onFailureListener(int what, JSONObject ans) {
        mView.hideLoading();
        pd.setVisibility(View.GONE);
        this.page--;

        if (null != mPullToRefreshLayout) {
            mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        }
        MyToast.show(mView.getActivity(), "数据异常!");

    }


    private class ProAdapter extends BaseAdapter {


        public ProAdapter() {
        }

        @Override
        public int getCount() {
            if (OkHttpUtils.hashkNewwork()) {
                if (null == goodses)
                    return 0;
                return goodses.size();
            } else {
                if (null == mGoodsList)
                    return 0;
                return mGoodsList.size();
            }

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
                float h = (mScreenWidth - ConvertUtil.dp2px(mView.getActivity(), 45.8f)) / 6;
                lLp.height = (int) h;
                holder.imageView.setLayoutParams(lLp);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            if (OkHttpUtils.hashkNewwork()) {
                holder.textView.setText(goodses.getJSONObject(position).getString(Constance.name));
                String path = FileUtil.getGoodsExternDir(goodses.getJSONObject(position).getString(Constance.img_url));
                File imageFile = new File(path + "!400X400.png");
                if (imageFile.exists()) {
                    imageURL = "file://" + imageFile.toString();
                    ImageLoader.getInstance().displayImage(imageURL, holder.imageView);

                } else {
                    ImageLoader.getInstance().displayImage(NetWorkConst.UR_PRODUCT_URL +
                            goodses.getJSONObject(position).getString(Constance.img_url) + "!400X400.png"
                            , holder.imageView);
                }
                final int goodId = goodses.getJSONObject(position).getInteger(Constance.id);
                holder.old_price_tv.setText("￥" + goodses.getJSONObject(position).getString(Constance.market_price));
                holder.old_price_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);


                float setPrice = mPriceDao.getProductPrice(goodId + "").getShop_price();
                holder.price_tv.setText(setPrice == 0 ? "￥" + goodses.getJSONObject(position).getString(Constance.shop_price) : "￥" + setPrice);
            } else {
                holder.textView.setText(mGoodsList.get(position).getName());
                String path = FileUtil.getGoodsExternDir(mGoodsList.get(position).getImg_url());
                File imageFile = new File(path + "!400X400.png");
                if (imageFile.exists()) {
                    imageURL = "file://" + imageFile.toString();
                    ImageLoader.getInstance().displayImage(imageURL, holder.imageView);

                } else {
                    ImageLoader.getInstance().displayImage(NetWorkConst.UR_PRODUCT_URL +
                            mGoodsList.get(position).getImg_url() + "!400X400.png"
                            , holder.imageView);
                }
                final int goodId = mGoodsList.get(position).getId();
                holder.old_price_tv.setText("￥" + mGoodsList.get(position).getMarket_price());
                holder.old_price_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);


                float setPrice = mPriceDao.getProductPrice(goodId + "").getShop_price();
                holder.price_tv.setText(setPrice == 0 ? "￥" + mGoodsList.get(position).getShop_price() : "￥" + setPrice);
            }
            return convertView;
        }

        class ViewHolder {
            ImageView imageView;
            TextView textView;
            TextView old_price_tv;
            TextView price_tv;
        }
    }
}
