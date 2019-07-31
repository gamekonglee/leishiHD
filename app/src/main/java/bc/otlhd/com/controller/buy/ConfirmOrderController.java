package bc.otlhd.com.controller.buy;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.lib.common.hxp.view.PullableScrollView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.yjn.swipelistview.swipelistviewlibrary.widget.SwipeMenuListView;

import bc.otlhd.com.R;
import bc.otlhd.com.bean.Logistics;
import bc.otlhd.com.bean.PayResult;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.cons.NetWorkConst;
import bc.otlhd.com.controller.BaseController;
import bc.otlhd.com.listener.ILogisticsChooseListener;
import bc.otlhd.com.listener.INetworkCallBack;
import bc.otlhd.com.listener.INoHttpNetworkCallBack;
import bc.otlhd.com.ui.activity.buy.ConfirmOrderActivity;
import bc.otlhd.com.ui.activity.user.UserAddrActivity;
import bc.otlhd.com.ui.view.popwindow.SelectLogisticsPopWindow;
import bc.otlhd.com.utils.UIUtils;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/2/24 16:53
 * @description :提交订单
 */
public class ConfirmOrderController extends BaseController implements INetworkCallBack {
    private ConfirmOrderActivity mView;
    private TextView consignee_tv, phone_tv, address_tv, money_tv;
    private Intent mIntent;
    private SwipeMenuListView listView;
    private MyAdapter mMyAdapter;
    private String mConsigneeId = "";
    private RelativeLayout logistic_type_rl;
    private LinearLayout logistic_ll, main_ll;
    private TextView logistic_title_tv, loginstic_tv, loginstic_phone_tv, loginstic_address_tv;
    private Logistics mlogistics;
    private SelectLogisticsPopWindow mPopWindow;
    private PullableScrollView scrollView;

    public ConfirmOrderController(ConfirmOrderActivity v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {
        mMyAdapter = new MyAdapter();
        listView.setAdapter(mMyAdapter);
        if (!AppUtils.isEmpty(mView.mAddressObject)) {
            String name = mView.mAddressObject.getString(Constance.name);
            String address = mView.mAddressObject.getString(Constance.address);
            String phone = mView.mAddressObject.getString(Constance.mobile);
            mConsigneeId = mView.mAddressObject.getString(Constance.id);
            consignee_tv.setText(name);
            address_tv.setText("收货地址:" + address);
            phone_tv.setText(phone);
        } else {
            sendAddressList();
        }
    }

    private void initView() {
        consignee_tv = (TextView) mView.findViewById(R.id.consignee_tv);
        phone_tv = (TextView) mView.findViewById(R.id.phone_tv);
        address_tv = (TextView) mView.findViewById(R.id.address_tv);
        money_tv = (TextView) mView.findViewById(R.id.money_tv);
        listView = (SwipeMenuListView) mView.findViewById(R.id.listView);
        logistic_type_rl = (RelativeLayout) mView.findViewById(R.id.logistic_type_rl);
        logistic_ll = (LinearLayout) mView.findViewById(R.id.logistic_ll);
        main_ll = (LinearLayout) mView.findViewById(R.id.main_ll);

        logistic_title_tv = (TextView) mView.findViewById(R.id.logistic_title_tv);
        loginstic_tv = (TextView) mView.findViewById(R.id.loginstic_tv);
        loginstic_phone_tv = (TextView) mView.findViewById(R.id.loginstic_phone_tv);
        loginstic_address_tv = (TextView) mView.findViewById(R.id.loginstic_address_tv);
        scrollView = (PullableScrollView) mView.findViewById(R.id.scrollView);
        scrollView.smoothScrollTo(0,20);
        listView.setFocusable(false);
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    public void sendAddressList() {
        mNetWork.sendAddressList(this);
    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        switch (requestCode) {
            case NetWorkConst.CONSIGNEELIST:
                JSONArray consigneeList = ans.getJSONArray(Constance.consignees);
                if (consigneeList.length() == 0)
                    return;
                String name = consigneeList.getJSONObject(0).getString(Constance.name);
                String address = consigneeList.getJSONObject(0).getString(Constance.address);
                String phone = consigneeList.getJSONObject(0).getString(Constance.mobile);
                consignee_tv.setText(name);
                address_tv.setText("收货地址:" + address);
                phone_tv.setText(phone);
                mConsigneeId = consigneeList.getJSONObject(0).getString(Constance.id);
                break;
            case NetWorkConst.CheckOutCart:
                JSONObject orderObject = ans.getJSONObject(Constance.order);
               String order= orderObject.getString(Constance.id);
                sendPayment(order, "alipay.app");
                break;
        }
    }

    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须上传到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息


                    String resultStatus = payResult.getResultStatus();
                    Log.d("TAG", "resultStatus=" + resultStatus);
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        MyToast.show(mView,"支付成功");
                        mView.finish();

                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            AppDialog.messageBox("支付结果确认中");

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            AppDialog.messageBox("支付失败");
                            mView.hideLoading();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };



    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        if (null == mView || mView.isFinishing())
            return;
        if (AppUtils.isEmpty(ans)) {
            AppDialog.messageBox(UIUtils.getString(R.string.server_error));
            return;
        }
        AppDialog.messageBox(ans.getString(Constance.error_desc));

    }


    /**
     * 支付订单
     * @param order
     * @param code
     */
    private void sendPayment(String order,String code){
        mNetWork.sendPayment(order, code, new INoHttpNetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                String notify_url = ans.getString(Constance.alipay);
                if (AppUtils.isEmpty(notify_url))
                    return;
                SubmitAliPay(notify_url);
            }

            @Override
            public void onFailureListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                mView.hideLoading();
                MyToast.show(mView, "支付失败!");
            }
        });

    }

    /**
     * 支付宝支付
     */
    private void SubmitAliPay(String notifyUrl) {
        //开始支付
        aliPay(notifyUrl);
    }

    //标记是支付
    private static final int SDK_PAY_FLAG = 1;
    private static final String TAG = "PayActivity";

    /**
     * 开始-支付宝支付
     */
    private void aliPay(final String ss) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(mView);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(ss, true);
                //异步处理支付结果
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    /**
     * 选择地址
     */
    public void selectAddress() {
        mIntent = new Intent(mView, UserAddrActivity.class);
        mIntent.putExtra(Constance.isSELECTADDRESS, true);
        mView.startActivityForResult(mIntent, Constance.FROMADDRESS);
    }

    public void ActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constance.FROMADDRESS && !AppUtils.isEmpty(data)) {
            JSONObject consignee = (JSONObject) data.getSerializableExtra(Constance.address);
            if (AppUtils.isEmpty(consignee))
                return;
            String name = consignee.getString(Constance.name);
            String address = consignee.getString(Constance.address);
            String phone = consignee.getString(Constance.mobile);
            mConsigneeId = consignee.getString(Constance.id);
            consignee_tv.setText(name);
            address_tv.setText("收货地址:" + address);
            phone_tv.setText(phone);
        } else if (requestCode == Constance.FROMLOG && !AppUtils.isEmpty(data)) {
            mlogistics = (Logistics) data.getSerializableExtra(Constance.logistics);
            logistic_title_tv.setVisibility(View.GONE);
            loginstic_tv.setVisibility(View.VISIBLE);
            loginstic_phone_tv.setVisibility(View.VISIBLE);
            loginstic_address_tv.setVisibility(View.VISIBLE);
            loginstic_tv.setText(mlogistics.getName());
            loginstic_phone_tv.setText(mlogistics.getTel());
            loginstic_address_tv.setText("提货地址: " + mlogistics.getAddress());
        }
    }

    /**
     * 结算订单
     */
    public void settleOrder() {
        if (AppUtils.isEmpty(mConsigneeId)) {
            MyToast.show(mView, "请选择收货地址!");
            return;
        }
        sendCheckOutCart();
    }

    /**
     * 结算购物车
     */
    private void sendCheckOutCart() {
        String consignee = mConsigneeId;
        String shipping = "";
        String tel = "";
        String address = "";

        if (mType == 0 && AppUtils.isEmpty(mlogistics)) {
            MyToast.show(mView, "请选择物流地址!");
            return;
        }
        if (mType == 1 && AppUtils.isEmpty(mConsigneeId)) {
            MyToast.show(mView, "请选择收货地址!");
            return;
        }

        if (!AppUtils.isEmpty(mlogistics)) {
            shipping = mlogistics.getName();
            tel = mlogistics.getTel();
            address = mlogistics.getAddress();
        }
        String idArray = "[";
        for (int i = 0; i < mView.goodsList.length(); i++) {
            String id = mView.goodsList.getJSONObject(i).getString(Constance.id);
            if (i == mView.goodsList.length() - 1) {
                idArray += id + "]";
            } else {
                idArray += id + ",";
            }
        }
        mView.setShowDialog(true);
        mView.setShowDialog("正在结算中...");
        mView.showLoading();
        mNetWork.sendCheckOutCart(consignee, shipping, tel, address, idArray, this);
    }

    private int mType = 0;

    /**
     * 选择运输方式
     */
    public void selectCheckType(int type) {
        if (type == R.id.radioLogistic) {
            mType = 0;
            logistic_type_rl.setVisibility(View.VISIBLE);
            if (AppUtils.isEmpty(mlogistics)) {
                logistic_title_tv.setVisibility(View.VISIBLE);
                loginstic_tv.setVisibility(View.GONE);
                loginstic_phone_tv.setVisibility(View.GONE);
                loginstic_address_tv.setVisibility(View.GONE);
            } else {
                logistic_title_tv.setVisibility(View.GONE);
                loginstic_tv.setVisibility(View.VISIBLE);
                loginstic_phone_tv.setVisibility(View.VISIBLE);
                loginstic_address_tv.setVisibility(View.VISIBLE);
            }

        } else {
            mType = 1;
            logistic_type_rl.setVisibility(View.GONE);
        }
    }

    /**
     * 选择物流公司
     */
    public void selectLogistic() {
        mPopWindow = new SelectLogisticsPopWindow(mView, mView);
        mPopWindow.onShow(main_ll);
        mPopWindow.setListener(new ILogisticsChooseListener() {
            @Override
            public void onLogisticsChanged(JSONObject object) {
                if(AppUtils.isEmpty(object)) return;
                logistic_title_tv.setVisibility(View.GONE);
                loginstic_tv.setVisibility(View.VISIBLE);
                loginstic_phone_tv.setVisibility(View.VISIBLE);
                loginstic_address_tv.setVisibility(View.VISIBLE);
                mlogistics=new Logistics();
                mlogistics.setAddress(object.getString(Constance.address));
                mlogistics.setTel(object.getString(Constance.tel));
                mlogistics.setName(object.getString(Constance.name));
                loginstic_tv.setText(mlogistics.getName());
                loginstic_phone_tv.setText(mlogistics.getTel());
                loginstic_address_tv.setText("提货地址: " + mlogistics.getAddress());
            }
        });
    }

    private class MyAdapter extends BaseAdapter {
        private DisplayImageOptions options;
        private ImageLoader imageLoader;

        public MyAdapter() {
            options = new DisplayImageOptions.Builder()
                    // 设置图片下载期间显示的图片
                    .showImageOnLoading(R.drawable.bg_default)
                            // 设置图片Uri为空或是错误的时候显示的图片
                    .showImageForEmptyUri(R.drawable.bg_default)
                            // 设置图片加载或解码过程中发生错误显示的图片
                            // .showImageOnFail(R.drawable.ic_error)
                            // 设置下载的图片是否缓存在内存中
                    .cacheInMemory(true)
                            // 设置下载的图片是否缓存在SD卡中
                    .cacheOnDisk(true)
                            // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                            // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                    .considerExifParams(true)
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片可以放大（要填满ImageView必须配置memoryCacheExtraOptions大于Imageview）
                            // .displayer(new FadeInBitmapDisplayer(100))//
                            // 图片加载好后渐入的动画时间
                    .build(); // 构建完成

            // 得到ImageLoader的实例(使用的单例模式)
            imageLoader = ImageLoader.getInstance();
        }


        @Override
        public int getCount() {
            if (null == mView.goodsList)
                return 0;
            return mView.goodsList.length();
        }


        @Override
        public JSONObject getItem(int position) {
            if (null == mView.goodsList)
                return null;
            return mView.goodsList.getJSONObject(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mView, R.layout.item_lv_order, null);
                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                holder.nameTv = (TextView) convertView.findViewById(R.id.nameTv);
                holder.contact_service_tv = (TextView) convertView.findViewById(R.id.contact_service_tv);
                holder.SpecificationsTv = (TextView) convertView.findViewById(R.id.SpecificationsTv);
                holder.priceTv = (TextView) convertView.findViewById(R.id.priceTv);
                holder.numTv = (TextView) convertView.findViewById(R.id.numTv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final JSONObject goodsObject = mView.goodsList.getJSONObject(position);
            holder.nameTv.setText(goodsObject.getJSONObject(Constance.product).getString(Constance.name));
            imageLoader.displayImage(goodsObject.getJSONObject(Constance.product).getJSONObject(Constance.default_photo).getString(Constance.large)
                    , holder.imageView, options);

            String property = goodsObject.getString(Constance.property);
            holder.SpecificationsTv.setText(property);
            String price = goodsObject.getString(Constance.price);
            String num = goodsObject.getString(Constance.amount);
            holder.priceTv.setText("￥" + Integer.parseInt(price));
            holder.numTv.setText("x" + num);
            return convertView;
        }

        class ViewHolder {
            ImageView imageView;
            TextView nameTv;
            TextView priceTv;
            TextView SpecificationsTv;
            TextView numTv;
            TextView contact_service_tv;
        }
    }

}
