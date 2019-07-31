package bc.otlhd.com.ui.activity.buy;

import android.content.Intent;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import bc.otlhd.com.R;
import bc.otlhd.com.bean.Goods;
import bc.otlhd.com.bean.Logistics;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.cons.IntentKeys;
import bc.otlhd.com.controller.buy.OrderHDController;
import bc.otlhd.com.data.LogisticDao;
import bc.otlhd.com.data.SetPriceDao;
import bc.otlhd.com.ui.activity.IssueApplication;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/4/19 16:10
 * @description :
 */
public class OrderHDActivity extends BaseActivity {
    private OrderHDController mController;
    public ArrayList<Goods> mGoodChecks;
    public int mMoney;
    private Button back_bt, share_bt;
    private EditText date_et;
    private SetPriceDao mPriceDao;
    private LogisticDao mLogisticDao;
    public int mShopCarTotalPrice;
    public int mShopCarTotalCount;
    public String mOrderDiscount;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new OrderHDController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_order_hd);
        back_bt = getViewAndClick(R.id.back_bt);
        share_bt = getViewAndClick(R.id.share_bt);
        date_et = getViewAndClick(R.id.date_et);

    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mPriceDao=new SetPriceDao(this);
        mGoodChecks = (ArrayList<Goods>) intent.getSerializableExtra(Constance.goods);
        for(int i=0;i<mGoodChecks.size();i++){
            float setPrice = mPriceDao.getProductPrice(mGoodChecks.get(i).getId() + "").getShop_price();
            mGoodChecks.get(i).setShop_price(setPrice==0?mGoodChecks.get(i).getShop_price():setPrice);
        }

        mMoney = (int) intent.getFloatExtra(Constance.money, 0);


        Serializable serializableExtra = intent.getSerializableExtra(IntentKeys.SHOPCARDATAS);
        //订单总金额
        mShopCarTotalPrice = (int) intent.getFloatExtra(IntentKeys.SHOPCARTOTALPRICE, 0);
        //订单总数量
        mShopCarTotalCount = intent.getIntExtra(IntentKeys.SHOPCARTOTALCOUNT, 0);
        //订单总折扣
        mOrderDiscount = TextUtils.isEmpty(intent.getStringExtra(IntentKeys.SHOPORDERDISCOUNT)) ? "100" :
                intent.getStringExtra(IntentKeys.SHOPORDERDISCOUNT);
        if (serializableExtra != null || mShopCarTotalCount >= 0) {
            List<Goods> mShopCarBeans = (ArrayList<Goods>) serializableExtra;
            Log.v("520it", "mShopCarBeans" + mShopCarBeans.toString() + "");
        } else {
            finish();
        }
        getReceiverAdress();
//        mMoneytv.setText(mShopCarTotalPrice + "");
//        mProductnumtv.setText(mShopCarTotalCount + "");
//        if (!TextUtils.isEmpty(mOrderDiscount))
//            mSumAge.setText(mOrderDiscount + "%");

    }

    @Override
    protected void onViewClick(View v) {
        if(!IssueApplication.IsRoot()){
            SystemClock.sleep(IssueApplication.SLEEP_TIME);
        }

        switch (v.getId()) {
            case R.id.back_bt:
                finish();
                break;
            case R.id.share_bt:
                mController.getShareOrder();
                break;
            case R.id.date_et:
                mController.setGoodsDate();
                break;

        }
    }
    /**
     * 显示收货地址信息
     */
    private void getReceiverAdress() {
        //获取到用户数据
        JSONObject mInfo = ((IssueApplication) getApplication()).mUserInfo;
        if (mInfo == null)
            return;
        mLogisticDao = new LogisticDao(this);
        List<Logistics> all = mLogisticDao.getAll();
        if (all!=null&&all.size() > 0) {
            Logistics mLogistics = all.get(0);
//            mConsigneetv.setText(mLogistics.getName());
//            mPhonetv.setText(mLogistics.getTel());
//            mAdresstv.setText(mLogistics.getAddress());
        }

    }
}
