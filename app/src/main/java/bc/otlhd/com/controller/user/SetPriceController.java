package bc.otlhd.com.controller.user;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Message;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baiiu.filter.DropDownMenu;
import com.baiiu.filter.interfaces.OnFilterDoneListener;
import com.bigkoo.alertview.AlertView;
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
import bc.otlhd.com.ui.activity.user.SetPriceActivity;
import bc.otlhd.com.ui.adapter.ProductDropMenuAdapter;
import bc.otlhd.com.utils.FileUtil;
import bc.otlhd.com.utils.MyShare;
import bc.otlhd.com.utils.UIUtils;
import bc.otlhd.com.utils.net.HttpListener;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/4/24 9:49
 * @description :
 */
public class SetPriceController extends BaseController implements HttpListener, OnFilterDoneListener, PullToRefreshLayout.OnRefreshListener {
    private SetPriceActivity mView;
    private DropDownMenu dropDownMenu;
    private JSONArray sceneAllAttrs;
    private PullToRefreshLayout mPullToRefreshLayout;
    private ProAdapter mProAdapter;
    private PullableGridView mGoodsSv;
    public int page = 1;
    private JSONArray goodses;
    private boolean initFilterDropDownView;
    private String imageURL = "";
    private Intent mIntent;
    public String keyword;
    private ProgressBar pd;
    public String mType;
    public String filter_attr;
    private float mScreenWidth;
    private SetPriceDao mPriceDao;
    private List<GoodPrices> mGoodPricees;

    public String filter_attr02;

    private List<Goods> mGoodsList;


    public SetPriceController(SetPriceActivity v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {
        page = 1;
        initFilterDropDownView = true;


//        sendGoodsList("0", 0);

        sendGoodsList("0", 0);
//        if (!OkHttpUtils.hashkNewwork()) {
//            mNetWork.sendGoodsList("0", page, 0, keyword, mType, filter_attr, true, 1, "",mView, this);
//        }


        pd.setVisibility(View.GONE);
        List<GoodPrices> all = mPriceDao.getAll();

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


        if (OkHttpUtils.hashkNewwork()) {
            mNetWork.sendGoodsList(c_id, page, okcat_id, keyword, mType, filter_attr, false,21,"", mView, this);
            return;
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final List<Goods> goodsList = GoodsDao.getGoodsList(keyword, c_id, filter_attr02, mType, 21, page);

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
        }



    }

    private void initView() {
        dropDownMenu = (DropDownMenu) mView.findViewById(R.id.dropDownMenu);
        mPullToRefreshLayout = ((PullToRefreshLayout) mView.findViewById(R.id.mFilterContentView));
        mPullToRefreshLayout.setOnRefreshListener(this);
        mGoodsSv = (PullableGridView) mView.findViewById(R.id.gridView);
        mProAdapter = new ProAdapter();
        mGoodsSv.setAdapter(mProAdapter);
        //        mGoodsSv.setOnItemClickListener(this);
        pd = (ProgressBar) mView.findViewById(R.id.pd);

        mExtView = (ViewGroup) LayoutInflater.from(mView).inflate(R.layout.alertext_form, null);
        etNum = (EditText) mExtView.findViewById(R.id.etName);
        etNum.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        imm = (InputMethodManager) mView.getSystemService(Context.INPUT_METHOD_SERVICE);
        mPriceDao = new SetPriceDao(mView);
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
                        MyToast.show(mView, "数据查询不到");
                        goodses = new JSONArray();
                    } else {
                        MyToast.show(mView, "数据已经到底啦!");
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
        MyToast.show(mView, "数据异常!");
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


    private List<Integer> itemPosList = new ArrayList<>();//有选中值的itemPos列表，长度为3

    private void initFilterDropDownView(JSONArray sceneAllAttrs) {

        if (itemPosList.size() < sceneAllAttrs.size()) {
            itemPosList.add(0);
        }
        JSONArray goodtypes = new JSONArray();


        for (int i = 0; i < sceneAllAttrs.size(); i++) {
            goodtypes.add(sceneAllAttrs.get(i));
            break;
        }

        ProductDropMenuAdapter dropMenuAdapter = new ProductDropMenuAdapter(mView, goodtypes, itemPosList, this);
        dropDownMenu.setMenuAdapter(dropMenuAdapter);
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
        if(OkHttpUtils.hashkNewwork()) {
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
        }else{
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

    /**
     * 查询产品
     * @param value
     */
    public void searchData(String value) {
        page = 1;
        keyword = value;
        sendGoodsList("0", 0);
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mView, R.layout.item_gridview_product_price, null);

                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                holder.textView = (TextView) convertView.findViewById(R.id.name_tv);
                holder.old_price_tv = (TextView) convertView.findViewById(R.id.old_price_tv);
                holder.price_tv = (TextView) convertView.findViewById(R.id.price_tv);
                holder.set_price_bt = (Button) convertView.findViewById(R.id.set_price_bt);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            int goodId=0;
            if(OkHttpUtils.hashkNewwork()){
                holder.textView.setText(goodses.getJSONObject(position).getString(Constance.name));
                String path = FileUtil.getGoodsExternDir(goodses.getJSONObject(position).getString(Constance.img_url));
                goodId = goodses.getJSONObject(position).getInteger(Constance.id);
                File imageFile = new File(path + "!400X400.png");
                if (imageFile.exists()) {
                    imageURL = "file://" + imageFile.toString();
                    ImageLoader.getInstance().displayImage(imageURL, holder.imageView);

                } else {
                    ImageLoader.getInstance().displayImage(NetWorkConst.UR_PRODUCT_URL +
                            goodses.getJSONObject(position).getString(Constance.img_url) + "!400X400.png"
                            , holder.imageView);
                }
                holder.old_price_tv.setText("￥" + goodses.getJSONObject(position).getString(Constance.market_price));
                holder.old_price_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                holder.price_tv.setText("￥" + goodses.getJSONObject(position).getString(Constance.shop_price));
            }else{
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
                goodId = mGoodsList.get(position).getId();
                holder.old_price_tv.setText("￥" + mGoodsList.get(position).getMarket_price());
                holder.old_price_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                holder.price_tv.setText("￥" + mGoodsList.get(position).getShop_price());
            }


            final float setPrice=mPriceDao.getProductPrice(goodId+"").getShop_price();
            holder.set_price_bt.setText(setPrice==0?"设置":"￥"+setPrice);
            final int finalGoodId = goodId;
            holder.set_price_bt.setOnClickListener(new View.OnClickListener() {
                                                       @Override
                                                       public void onClick(View v) {
                                                           final EditText inputServer = new EditText(mView);
                                                           inputServer.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                                                           AlertDialog.Builder builder = new AlertDialog.Builder(mView);
                                                           builder.setTitle("设置").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                                                                   .setNegativeButton("取消", null);
                                                           builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                               public void onClick(DialogInterface dialog, int which) {
                                                                   mView.setShowDialog(true);
                                                                   mView.setShowDialog("正在处理中..");
                                                                   mView.showLoading();
                                                                   MyShare.get(UIUtils.getContext()).putBoolean(Constance.SET_PRICE, true);
//                                                                   MyShare.get(mView).putString(Constance.goods_id+finalGoodId,setPrice+"");
                                                                   String price = inputServer.getText().toString();
                                                                   mGood= new GoodPrices();
                                                                   mGood.setId(finalGoodId);
                                                                   oldPrice=setPrice==0?"0":setPrice+"";
                                                                   if (AppUtils.isEmpty(price)) {
                                                                       mGood.setShop_price(0);
                                                                   } else {
                                                                       mGood.setShop_price(Float.parseFloat(price));
                                                                   }

                                                                   if (-1 != mPriceDao.replaceOne(mGood)) {
                                                                       new Thread(new Runnable() {
                                                                           @Override
                                                                           public void run() {
                                                                               mGoodPricees = mPriceDao.getAll();
                                                                               mView.runOnUiThread(new Runnable() {
                                                                                   @Override
                                                                                   public void run() {
                                                                                       sendUploadPrice(JSON.toJSONString(mGoodPricees));
                                                                                   }
                                                                               });
                                                                           }
                                                                       }).start();
                                                                       holder.set_price_bt.setText(AppUtils.isEmpty(price) ? "设置" : "￥" + price);
                                                                   } else {
                                                                       Toast.makeText(mView, "设置失败!", Toast.LENGTH_LONG).show();
                                                                   }

                                                               }
                                                           });
                                                           builder.show();

                                                       }
                                                   }

            );
            return convertView;
        }

        class ViewHolder {
            ImageView imageView;
            TextView textView;
            TextView old_price_tv;
            TextView price_tv;
            Button set_price_bt;
        }
    }

    private String oldPrice="";
    private GoodPrices mGood;

    private void sendUploadPrice(String productPrice){
        if(((IssueApplication)mView.getApplication()).mUserInfo==null){
            MyToast.show(mView,"请先连接网络");
            return;
        }
        String userId=((IssueApplication)mView.getApplication()).mUserInfo.getString(Constance.id);
       String value="{\"result\":\"success\",\"data\":"+productPrice+"}";
        mNetWork.sendUploadPrice(userId, value, mView, new HttpListener() {
            @Override
            public void onSuccessListener(int what, JSONObject ans) {
                mView.hideLoading();
                mGood.setShop_price(Float.parseFloat(oldPrice));
                if (-1 != mPriceDao.replaceOne(mGood)) {
                    mProAdapter.notifyDataSetChanged();
                }
                String res=ans.getString(Constance.res);
                if(res.equals("true")){
                    Toast.makeText(mView, "设置成功!", Toast.LENGTH_LONG).show();
                }else {

                    Toast.makeText(mView, "设置失败!", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailureListener(int what, JSONObject ans) {
                mView.hideLoading();
                mGood.setShop_price(Float.parseFloat(oldPrice));
                if (-1 != mPriceDao.replaceOne(mGood)) {
                    mProAdapter.notifyDataSetChanged();
                }
                Toast.makeText(mView, "设置失败!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private AlertView mAlertViewExt;//窗口拓展例子
    private EditText etNum;//拓展View内容
    private InputMethodManager imm;
    private ViewGroup mExtView;
}
