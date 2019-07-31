package bc.otlhd.com.controller.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alipay.sdk.app.PayTask;
import com.lib.common.hxp.view.ListViewForScrollView;
import com.lib.common.hxp.view.PullToRefreshLayout;

import bc.otlhd.com.R;
import bc.otlhd.com.bean.OrderInfo;
import bc.otlhd.com.bean.PayResult;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.cons.NetWorkConst;
import bc.otlhd.com.controller.BaseController;
import bc.otlhd.com.listener.INetworkCallBack;
import bc.otlhd.com.listener.INoHttpNetworkCallBack;
import bc.otlhd.com.ui.activity.user.OrderDetailActivity;
import bc.otlhd.com.ui.adapter.OrderGvAdapter;
import bc.otlhd.com.ui.fragment.OrderFragment;
import bc.otlhd.com.utils.DateUtils;
import bocang.json.JSONObject;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/2/6 15:13
 * @description :
 */
public class OrderController extends BaseController implements PullToRefreshLayout.OnRefreshListener, INetworkCallBack {
    private OrderFragment mView;
    private com.alibaba.fastjson.JSONArray goodses;
    private PullToRefreshLayout mPullToRefreshLayout;
    private ProAdapter mProAdapter;
    private ListViewForScrollView order_sv;
    private int page = 1;

    private View mNullView;
    private View mNullNet;
    private Button mRefeshBtn;
    private TextView mNullNetTv;
    private TextView mNullViewTv;
    private int per_pag = 20;
    private ProgressBar pd;


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
                        page = 1;
                        sendOrderList(page);
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            AppDialog.messageBox("支付结果确认中");

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            AppDialog.messageBox("支付失败");


                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };


    public OrderController(OrderFragment v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {
        page = 1;
        pd.setVisibility(View.VISIBLE);
        sendOrderList(page);
    }

    private void sendOrderList(final int page) {
        mNetWork.sendorderList(page, per_pag, mView.list.get(mView.flag), new INoHttpNetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                mView.hideLoading();
                pd.setVisibility(View.GONE);
                switch (requestCode) {
                    case NetWorkConst.ORDERLIST:
                        if (null == mView.getActivity() || mView.getActivity().isFinishing())
                            return;
                        if (null != mPullToRefreshLayout) {
                            dismissRefesh();
                        }
                        break;
                }

                com.alibaba.fastjson.JSONArray goodsList = ans.getJSONArray(Constance.orders);
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
            }

            @Override
            public void onFailureListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                mView.hideLoading();
                pd.setVisibility(View.GONE);
                MyToast.show(mView.getActivity(), "数据异常!");
            }
        });
    }

    private void initView() {
        mPullToRefreshLayout = ((PullToRefreshLayout) mView.getView().findViewById(R.id.refresh_view));
        mPullToRefreshLayout.setOnRefreshListener(this);
        order_sv = (ListViewForScrollView) mView.getView().findViewById(R.id.order_sv);
        order_sv.setDivider(null);//去除listview的下划线
        mProAdapter = new ProAdapter();
        order_sv.setAdapter(mProAdapter);
        mNullView = mView.getView().findViewById(R.id.null_view);
        mNullNet = mView.getView().findViewById(R.id.null_net);
        mRefeshBtn = (Button) mNullNet.findViewById(R.id.refesh_btn);
        mNullNetTv = (TextView) mNullNet.findViewById(R.id.tv);
        mNullViewTv = (TextView) mNullView.findViewById(R.id.tv);
        pd = (ProgressBar) mView.getView().findViewById(R.id.pd);

    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        page = 1;
        sendOrderList(page);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        sendOrderList(++page);
    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
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
                MyToast.show(mView.getActivity(), "没有更多内容了");
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
        sendOrderList(page);
        //        sendGoodsList(IssueApplication.mCId, page, 1, "is_best", null);
    }


    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        if (null == mView.getActivity() || mView.getActivity().isFinishing())
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

    /**
     * 支付订单
     *
     * @param order
     * @param code
     */
    private void sendPayment(String order, String code) {
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
                MyToast.show(mView.getActivity(), "支付失败!");
            }
        });

    }

    //标记是支付
    private static final int SDK_PAY_FLAG = 1;
    private static final String TAG = "PayActivity";

    /**
     * 支付宝支付
     */
    private void SubmitAliPay(String notifyUrl) {
        //开始支付
        aliPay(notifyUrl);
    }

    /**
     * create the order info. 创建订单信息
     */
    private String createOrderInfo(OrderInfo order) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + order.getPartner() + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + order.getSeller_id() + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + order.getOut_trade_no() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + order.getSubject() + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + order.getBody() + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + order.getTotal_fee() + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + order.getNotify_url() + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * 开始-支付宝支付
     */
    private void aliPay(final String ss) {
        //        try {
        //            /**
        //             * 仅需对sign 做URL编码
        //             */
        //            sign = URLEncoder.encode(sign, "UTF-8");
        //        } catch (UnsupportedEncodingException e) {
        //            e.printStackTrace();
        //        }
        //
        //        /**
        //         * 完整的符合支付宝参数规范的订单信息
        //         */
        //        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(mView.getActivity());
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
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

    String mBody = "";

    private class ProAdapter extends BaseAdapter implements INetworkCallBack {
        public ProAdapter() {
        }

        @Override
        public int getCount() {
            if (null == goodses)
                return 0;
            return goodses.size();
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
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mView.getActivity(), R.layout.item_order_one, null);

                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                holder.state_tv = (TextView) convertView.findViewById(R.id.state_tv);
                holder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
                holder.do_tv = (TextView) convertView.findViewById(R.id.do_tv);
                holder.do02_tv = (TextView) convertView.findViewById(R.id.do02_tv);
                holder.do03_tv = (TextView) convertView.findViewById(R.id.do03_tv);
                holder.code_tv = (TextView) convertView.findViewById(R.id.code_tv);
                holder.total_tv = (TextView) convertView.findViewById(R.id.total_tv);
                holder.lv = (ListView) convertView.findViewById(R.id.lv);
                holder.order_lv = (LinearLayout) convertView.findViewById(R.id.order_lv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final com.alibaba.fastjson.JSONObject orderobject = goodses.getJSONObject(position);
            final int state = orderobject.getInteger(Constance.status);
            int tatalNum = 0;
            String total = orderobject.getString(Constance.total);
            final String orderId = orderobject.getString(Constance.id);
            getState(state, holder.state_tv, holder.do_tv, holder.do02_tv, holder.do03_tv);
            holder.code_tv.setText("订单号:" + orderobject.getString(Constance.sn));
            final JSONArray array = orderobject.getJSONArray(Constance.goods);
            holder.time_tv.setText(DateUtils.getStrTime(orderobject.getString(Constance.created_at)));

            for (int i = 0; i < array.size(); i++) {
                tatalNum += array.getJSONObject(i).getInteger(Constance.total_amount);
            }
            holder.total_tv.setText("共计 " + tatalNum + " 件商品 合计" + total + "元");
            OrderGvAdapter maGvAdapter = new OrderGvAdapter(mView.getActivity(), array);
            holder.lv.setAdapter(maGvAdapter);


            holder.do03_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mView.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(NetWorkConst.QQURL)));
                }
            });
            holder.do_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO
                    if (state == 1) {
                        mView.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(NetWorkConst.QQURL)));
                    } else if (state == 0) {

                        for (int i = 0; i < array.size(); i++) {
                            mBody += array.getJSONObject(i).getJSONObject(Constance.product).getString(Constance.name) + "  ";
                        }
                        mView.setShowDialog(true);
                        mView.setShowDialog("正在付款中!");
                        mView.showLoading();
                        sendPayment(orderId, "alipay.app");
                    } else if (state == 2) {
                        //TODO 确认收货
                        MyToast.show(mView.getActivity(), "确认收货");
                    } else if (state == 5) {
                        mView.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(NetWorkConst.QQURL)));
                    } else if (state == 4) {
                        mView.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(NetWorkConst.QQURL)));
                    }
                }
            });
            holder.do02_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (state == 0) {
                        mView.setShowDialog(true);
                        mView.setShowDialog("正在取消中!");
                        mView.showLoading();
                        sendOrderCancel(orderId, "1");
                    }

                }
            });

            holder.order_lv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mView.getActivity(), OrderDetailActivity.class);
                    intent.putExtra(Constance.order, orderobject.toJSONString());
                    mView.getActivity().startActivity(intent);
                }
            });

            holder.lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(mView.getActivity(), OrderDetailActivity.class);
                    intent.putExtra(Constance.order, orderobject.toJSONString());
                    mView.getActivity().startActivity(intent);
                }
            });

            return convertView;
        }


        private void sendOrderCancel(String order, String reason) {
            mNetWork.sendOrderCancel(order, reason, this);
        }

        /**
         * 订单状态
         *
         * @param type
         * @param state_tv
         * @param do_tv
         * @param do02_tv
         */
        private void getState(int type, TextView state_tv, TextView do_tv, TextView do02_tv, TextView do03_tv) {
            do_tv.setVisibility(View.GONE);
            do02_tv.setVisibility(View.GONE);
            do03_tv.setVisibility(View.GONE);
            String stateValue = "";
            switch (type) {
                case 0:
                    do03_tv.setVisibility(View.VISIBLE);
                    stateValue = "【待付款】";
                    do_tv.setVisibility(View.VISIBLE);
                    do02_tv.setVisibility(View.VISIBLE);
                    do_tv.setText("付款");
                    do02_tv.setText("取消订单");

                    break;
                case 1:
                    //                    do03_tv.setVisibility(View.VISIBLE);
                    stateValue = "【待发货】";
                    do_tv.setVisibility(View.VISIBLE);
                    do_tv.setText("联系商家");
                    break;
                case 2:
                    do03_tv.setVisibility(View.VISIBLE);
                    stateValue = "【待收货】";
                    do_tv.setVisibility(View.VISIBLE);
                    do_tv.setText("确认收货");
                    break;
                case 4:
                    do_tv.setVisibility(View.VISIBLE);
                    do_tv.setText("联系商家");
                    stateValue = "【已完成】";
                    break;
                case 5:
                    do_tv.setVisibility(View.VISIBLE);
                    do_tv.setText("联系商家");
                    stateValue = "【已取消】";
                    break;
            }
            state_tv.setText(stateValue);
        }

        @Override
        public void onSuccessListener(String requestCode, JSONObject ans) {
            switch (requestCode) {
                case NetWorkConst.ORDERCANCEL:
                    if (ans.getInt(Constance.error_code) == 0) {
                        page = 1;
                        sendOrderList(page);
                    } else {
                        mView.hideLoading();
                        MyToast.show(mView.getActivity(), "订单取消失败!");
                    }

                    break;
                case NetWorkConst.PAYMENT:
                    break;


            }
        }

        @Override
        public void onFailureListener(String requestCode, JSONObject ans) {
            MyToast.show(mView.getActivity(), "2");
            mView.hideLoading();
            MyToast.show(mView.getActivity(), "支付成功!");

        }

        class ViewHolder {
            ImageView imageView;
            TextView state_tv;
            TextView time_tv;
            TextView do_tv;
            TextView do02_tv;
            TextView do03_tv;
            TextView code_tv;
            TextView total_tv;
            ListView lv;
            LinearLayout order_lv;

        }
    }

}
