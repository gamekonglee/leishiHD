package bc.otlhd.com.controller.product;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lib.common.hxp.view.GridViewForScrollView;
import com.lib.common.hxp.view.PullToRefreshLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import bc.otlhd.com.R;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.cons.NetWorkConst;
import bc.otlhd.com.controller.BaseController;
import bc.otlhd.com.listener.INetworkCallBack;
import bc.otlhd.com.ui.activity.IssueApplication;
import bc.otlhd.com.ui.activity.product.ClassifyGoodsActivity;
import bc.otlhd.com.ui.activity.product.ProDetailActivity;
import bc.otlhd.com.ui.activity.product.SelectGoodsActivity;
import bc.otlhd.com.utils.ConvertUtil;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.IntentUtil;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/2/16 17:30
 * @description :产品列表
 */
public class SelectGoodsController extends BaseController implements INetworkCallBack, PullToRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {
    private SelectGoodsActivity mView;
    private EditText et_search;
    private JSONArray goodses;
    private PullToRefreshLayout mPullToRefreshLayout;
    private ProAdapter mProAdapter;
    private GridViewForScrollView order_sv;
    private int page = 1;
    private View mNullView;
    private View mNullNet;
    private Button mRefeshBtn;
    private TextView mNullNetTv;
    private TextView mNullViewTv;
    private String per_pag = "20";
    public int mScreenWidth;
    public String mSortKey;
    public String mSortValue;
    private TextView popularityTv, priceTv, newTv, saleTv;
    private ImageView price_iv;
    private  Intent mIntent;
    private ProgressBar pd;


    public SelectGoodsController(SelectGoodsActivity v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {
        selectProduct(1,per_pag,null,null,null);
    }

    private void initView() {
        et_search = (EditText) mView.findViewById(R.id.et_search);
        mPullToRefreshLayout = ((PullToRefreshLayout) mView.findViewById(R.id.refresh_view));
        mPullToRefreshLayout.setOnRefreshListener(this);
        order_sv = (GridViewForScrollView) mView.findViewById(R.id.priductGridView);
        mProAdapter = new ProAdapter();
        order_sv.setAdapter(mProAdapter);
        order_sv.setOnItemClickListener(this);

        mNullView = mView.findViewById(R.id.null_view);
        mNullNet = mView.findViewById(R.id.null_net);
        mRefeshBtn = (Button) mNullNet.findViewById(R.id.refesh_btn);
        mNullNetTv = (TextView) mNullNet.findViewById(R.id.tv);
        mNullViewTv = (TextView) mNullView.findViewById(R.id.tv);
        mScreenWidth = mView.getResources().getDisplayMetrics().widthPixels;
        popularityTv = (TextView) mView.findViewById(R.id.popularityTv);
        priceTv = (TextView) mView.findViewById(R.id.priceTv);
        newTv = (TextView) mView.findViewById(R.id.newTv);
        saleTv = (TextView) mView.findViewById(R.id.saleTv);
        price_iv = (ImageView) mView.findViewById(R.id.price_iv);
        pd = (ProgressBar) mView.findViewById(R.id.pd);

    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    /**
     * 筛选产品
     * @param type
     */
    public void selectSortType(int type) {
        popularityTv.setTextColor(mView.getResources().getColor(R.color.fontColor60));
        priceTv.setTextColor(mView.getResources().getColor(R.color.fontColor60));
        newTv.setTextColor(mView.getResources().getColor(R.color.fontColor60));
        saleTv.setTextColor(mView.getResources().getColor(R.color.fontColor60));
        price_iv.setImageResource(R.drawable.arror);

        switch (type) {
            case R.id.popularityTv:
                popularityTv.setTextColor(mView.getResources().getColor(R.color.colorPrimaryRed));
                mSortKey="2";//人气
                mSortValue="1";//排序
                break;
            case R.id.stylell:
                priceTv.setTextColor(mView.getResources().getColor(R.color.colorPrimaryRed));
                price_iv.setImageResource(R.drawable.arror_top);
                mSortKey="1";//价格
                mSortValue="2";//排序
                break;
            case 2:
                priceTv.setTextColor(mView.getResources().getColor(R.color.colorPrimaryRed));
                price_iv.setImageResource(R.drawable.arror_button);
                mSortKey="1";//价格
                mSortValue="1";//排序
                break;
            case R.id.newTv:
                newTv.setTextColor(mView.getResources().getColor(R.color.colorPrimaryRed));
                mSortKey="5";//新品
                mSortValue="1";//排序
                break;
            case R.id.saleTv:
                saleTv.setTextColor(mView.getResources().getColor(R.color.colorPrimaryRed));
                mSortKey="4";//销售
                mSortValue="1";//排序
                break;
        }
        page=1;
        selectProduct(page, per_pag, null, null, null);
    }


    public void selectProduct(int page, String per_page, String brand, String category, String shop) {
        String keyword = et_search.getText().toString();
//        mView.setShowDialog(true);
//        mView.setShowDialog("正在搜索中!");
//        mView.showLoading();
        pd.setVisibility(View.VISIBLE);
        mNetWork.sendGoodsList(page, per_page, brand, mView.mCategoriesId,mView.mFilterAttr, shop, keyword, mSortKey, mSortValue, this);
    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        pd.setVisibility(View.INVISIBLE);
        switch (requestCode) {
            case NetWorkConst.PRODUCT:
                if (null == mView || mView.isFinishing())
                    return;
                if (null != mPullToRefreshLayout) {
                    dismissRefesh();
                }

                JSONArray goodsList = ans.getJSONArray(Constance.goodsList);
                if (AppUtils.isEmpty(goodsList)) {
                    if (page == 1) {
                        mNullView.setVisibility(View.VISIBLE);
                    }

                    dismissRefesh();
                    return;
                }

                mNullView.setVisibility(View.GONE);
                mNullNet.setVisibility(View.GONE);
                getDataSuccess(goodsList);
                break;
        }
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        pd.setVisibility(View.INVISIBLE);
        if (null == mView || mView.isFinishing())
            return;
        this.page--;

        if (AppUtils.isEmpty(ans)) {
            mNullNet.setVisibility(View.VISIBLE);
            mRefeshBtn.setOnClickListener(mRefeshBtnListener);
            return;
        }

        if (null != mPullToRefreshLayout) {
            mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        }
    }

    private void dismissRefesh() {
        mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }

    private void getDataSuccess(JSONArray array) {
        if (1 == page)
            goodses = array;
        else if (null != goodses) {
            for (int i = 0; i < array.length(); i++) {
                goodses.add(array.getJSONObject(i));
            }

            if (AppUtils.isEmpty(array))
                MyToast.show(mView, "没有更多内容了");
        }
        mProAdapter.notifyDataSetChanged();
    }

    private View.OnClickListener mRefeshBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onRefresh();
        }
    };

    public void onRefresh() {
        page = 1;
        selectProduct(page, per_pag, null, null, null);
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        page = 1;
        selectProduct(page, per_pag, null, null, null);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        selectProduct(++page, per_pag, null, null, null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(mView.isSelectGoods==true){
            mIntent=new Intent();
            mIntent.putExtra(Constance.product, goodses.getJSONObject(position));
            mView.setResult(Constance.FROMDIY, mIntent);//告诉原来的Activity 将数据传递给它
            mView.finish();//一定要调用该方法 关闭新的AC 此时 老是AC才能获取到Itent里面的值
        }else{
            mIntent= new Intent(mView, ProDetailActivity.class);
            int productId=goodses.getJSONObject(position).getInt(Constance.id);
            mIntent.putExtra(Constance.product,productId);
            mView.startActivity(mIntent);
        }

    }

    /**
     * 筛选
     */
    public void openClassify() {
        IssueApplication.isClassify=true;
        IntentUtil.startActivity(mView, ClassifyGoodsActivity.class,true);
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
                convertView = View.inflate(mView, R.layout.item_gridview_fm_product, null);

                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                holder.textView = (TextView) convertView.findViewById(R.id.name_tv);
                holder.old_price_tv = (TextView) convertView.findViewById(R.id.old_price_tv);
                holder.price_tv = (TextView) convertView.findViewById(R.id.price_tv);
                RelativeLayout.LayoutParams lLp = (RelativeLayout.LayoutParams) holder.imageView.getLayoutParams();
                float h = (mScreenWidth - ConvertUtil.dp2px(mView, 45.8f)) / 2;
                lLp.height = (int) h;
                holder.imageView.setLayoutParams(lLp);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.textView.setText(goodses.getJSONObject(position).getString(Constance.name));
            ImageLoader.getInstance().displayImage(goodses.getJSONObject(position).getJSONObject(Constance.default_photo).getString(Constance.large)
                    , holder.imageView);
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
}
