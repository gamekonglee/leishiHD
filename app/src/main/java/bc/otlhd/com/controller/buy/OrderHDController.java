package bc.otlhd.com.controller.buy;

import android.app.ActionBar;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

import bc.otlhd.com.R;
import bc.otlhd.com.bean.Goods;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.cons.NetWorkConst;
import bc.otlhd.com.controller.BaseController;
import bc.otlhd.com.data.CartDao;
import bc.otlhd.com.listener.ITwoCodeListener;
import bc.otlhd.com.ui.activity.IssueApplication;
import bc.otlhd.com.ui.activity.buy.OrderHDActivity;
import bc.otlhd.com.ui.view.popwindow.TwoCodeSharePopWindow;
import bc.otlhd.com.utils.SimpleMoneyFormat;
import bc.otlhd.com.utils.net.HttpListener;
import bocang.utils.AppUtils;
import bocang.utils.CommonUtil;
import bocang.utils.MyToast;
import cn.qqtheme.framework.picker.DatePicker;

/**
 * @author: Jun
 * @date : 2017/4/19 17:37
 * @description :
 */
public class OrderHDController extends BaseController {
    private OrderHDActivity mView;
    private TableLayout table_tl;
    private TableLayout table_head;
    private String[] mlistHead = {"编号", "物品名称", "数量", "单价", "金额", "备注"};
    private EditText client_et, tel_et, date_et, address_et;
    private TextView money_tv, money_big_tv, service_tel_tv;
    private LinearLayout main_ll;
    public TextView tv_money_discount;
    public TextView tv_discount_total;


    public OrderHDController(OrderHDActivity v) {
        this.mView = v;
        initView();
        initViewData();
    }


    private void setTableHead() {
        table_head.setStretchAllColumns(true);

        TableRow tableRow = new TableRow(mView);

        for (int i = 0; i < mlistHead.length; i++) {
            TextView tv = new TextView(mView);
            tv.setBackgroundResource(R.drawable.table_row);
            tv.setText(mlistHead[i]);
            tv.setTextColor(mView.getResources().getColor(R.color.txt_black));
            tv.setTextSize(18);
            tv.setGravity(Gravity.CENTER);
            tv.setSingleLine();
            tv.setWidth(50);
            tv.setHeight(50);
            tv.getPaint().setFakeBoldText(true);
            if (i == 1) {
                tv.setWidth(230);
            } else if (i == 5) {
                tv.setWidth(280);
            }
            tableRow.addView(tv);

        }

        table_head.addView(tableRow, new TableLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.FILL_PARENT));

    }


    public void setGoodsDate() {
        Calendar c = Calendar.getInstance();
        DatePicker picker = new DatePicker(mView);
        picker.setRange(CommonUtil.getYear(), 1920);//年份范围
        picker.setDateRangeStart(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override

            public void onDatePicked(String year, String month, String day) {
                date_et.setText(year + "-" + month + "-" + day);
            }

        });
        picker.show();
    }

    private void initViewData() {
        setTableHead();
        money_tv.setText("￥" + mView.mMoney);
        float discount=Float.parseFloat(mView.mOrderDiscount);
        if(discount==100){
            tv_money_discount.setText((mView.mShopCarTotalPrice)+"");
        }else {
            tv_money_discount.setText((int)(mView.mShopCarTotalPrice*0.01f*Float.parseFloat(mView.mOrderDiscount))+"");
        }

        tv_discount_total.setText(mView.mOrderDiscount+"%");
        money_big_tv.setText(SimpleMoneyFormat.getInstance().format(mView.mMoney));
        JSONObject userInfo=((IssueApplication)mView.getApplication()).mUserInfo;
        if(userInfo!=null){
            String phone = ((IssueApplication) mView.getApplication()).mUserInfo.getString(Constance.phone);
            service_tel_tv.setText("服务热线:" + phone);
        }else {
            service_tel_tv.setText("服务热线:" );
        }



        table_tl.setStretchAllColumns(true);
        for (int i = 0; i < mView.mGoodChecks.size(); i++) {
            TableRow tableRow = new TableRow(mView);

            for (int j = 0; j < mlistHead.length; j++) {
                TextView tv = new TextView(mView);
                tv.setBackgroundResource(R.drawable.table_row);
                tv.setTextColor(mView.getResources().getColor(R.color.txt_black));
                tv.setTextSize(18);
                tv.setGravity(Gravity.CENTER);
                tv.setSingleLine();
                tv.setWidth(50);
                tv.setHeight(50);
                switch (j) {
                    case 0:
                        tv.setText((i + 1) + "");
                        break;
                    case 1:
                        tv.setText(mView.mGoodChecks.get(i).getName());
                        tv.setWidth(230);
                        break;
                    case 2:
                        tv.setText(mView.mGoodChecks.get(i).getBuyCount() + "");
                        break;
                    case 3:
                        tv.setText(mView.mGoodChecks.get(i).getShop_price() + "");
                        break;
                    case 4:
                        tv.setText(mView.mGoodChecks.get(i).getBuyCount() * mView.mGoodChecks.get(i).getShop_price() + "");
                        break;
                    case 5:
                        if (!AppUtils.isEmpty(mView.mGoodChecks.get(i).getGoods_desc())) {
                            tv.setText(mView.mGoodChecks.get(i).getGoods_desc());
                        } else {
                            tv.setText("-------");
                        }

                        tv.setWidth(280);
                        break;
                }

                tableRow.addView(tv);

            }
            table_tl.addView(tableRow, new TableLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.FILL_PARENT));
        }


    }

    private void initView() {
        table_tl = (TableLayout) mView.findViewById(R.id.table_tl);
        table_head = (TableLayout) mView.findViewById(R.id.table_head);
        client_et = (EditText) mView.findViewById(R.id.client_et);
        tel_et = (EditText) mView.findViewById(R.id.tel_et);
        date_et = (EditText) mView.findViewById(R.id.date_et);
        address_et = (EditText) mView.findViewById(R.id.address_et);
        money_tv = (TextView) mView.findViewById(R.id.money_tv);
        money_big_tv = (TextView) mView.findViewById(R.id.money_big_tv);
        service_tel_tv = (TextView) mView.findViewById(R.id.service_tel_tv);
        main_ll = (LinearLayout) mView.findViewById(R.id.main_ll);
        tv_money_discount = (TextView) mView.findViewById(R.id.money_tv_discount);
        tv_discount_total = (TextView) mView.findViewById(R.id.tv_discount_total);

    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    /**
     * 分享订单
     */
    public void getShareOrder() {
        String order_name = client_et.getText().toString();
        String order_address = address_et.getText().toString();
        String order_phone = tel_et.getText().toString();
        String delivery_time = date_et.getText().toString();
        String order_sum = mView.mMoney + "";
        if(((IssueApplication) mView.getApplication()).mUserInfo==null){
            MyToast.show(mView,"没有当前用户信息，请重新登录");
            return;
        }
        String user_id = ((IssueApplication) mView.getApplication()).mUserInfo.getString(Constance.id);


        if (AppUtils.isEmpty(order_name)) {
            MyToast.show(mView, "请输入客户名称!");
            return;
        }
        if (AppUtils.isEmpty(order_address)) {
            MyToast.show(mView, "请输入地址!");
            return;
        }
        if (AppUtils.isEmpty(order_phone)) {
            MyToast.show(mView, "请输入电话!");
            return;
        }
        if (AppUtils.isEmpty(date_et)) {
            MyToast.show(mView, "请输入交货时间!");
            return;
        }

        mView.setShowDialog(true);
        mView.setShowDialog("正在分享中...");
        mView.showLoading();

        String orderJson = "";
        orderJson = "{   \"order_name\" : \"" + order_name + "\",   \"order_sum" +
                "\" : \"" + order_sum + "\",   \"order_phone\" : \"" + order_phone + "\",   \"delivery_time\" :  \"" + delivery_time + "\"" +
                ",   \"user_id\" : \"" + user_id + "\",   \"order_address\" : \"" + order_address + "\" }";

        StringBuffer productJson = new StringBuffer();
        productJson.append("[\n");

        //订单商品信息
        for (int i = 0; i < mView.mGoodChecks.size(); i++) {
            productJson.append("  \"{\\n  \\\"msg\\\" : \\\"" + mView.mGoodChecks.get(i).getGoods_desc() + "\\\",");
            productJson.append("\\n  \\\"goodsPath\\\" : \\\"" + NetWorkConst.UR_PRODUCT_URL + mView.mGoodChecks.get(i).getImg_url() + "\\\",");
            productJson.append("\\n  \\\"goods_id\\\" : \\\"" + mView.mGoodChecks.get(i).getId() + "\\\",");
            productJson.append("\\n  \\\"goods_name\\\" : \\\"" + mView.mGoodChecks.get(i).getName() + "\\\",");
            productJson.append("\\n  \\\"goods_price\\\" : \\\"" + mView.mGoodChecks.get(i).getBuyCount() * mView.mGoodChecks.get(i).getShop_price() + "\\\",");
            productJson.append("\\n  \\\"number\\\" : \\\"" + mView.mGoodChecks.get(i).getBuyCount() + "\\\"");
            if (i == mView.mGoodChecks.size() - 1) {
                productJson.append("\\n}\"\n");
            } else {
                productJson.append("\\n}\",\n");
            }

        }

        productJson.append("]");
        Log.v("520", "xx:" + productJson.toString());
        submitOrder(orderJson, productJson.toString());


    }


    private void submitOrder(String order, String product) {
        mNetWork.sendOrder(order, product, mView, new HttpListener() {
            @Override
            public void onSuccessListener(int what, JSONObject ans) {
                String result = ans.getString(Constance.result);
                final String name = ((IssueApplication) mView.getApplication()).mUserInfo.getString(Constance.name);
                final String imgPath = NetWorkConst.UR_SHAREICON;
                if (!result.equals("0")) {
                    mView.hideLoading();
                    final String path = NetWorkConst.UR_SHAREORDER + result;
//                    new AlertView(null, null, "取消", null,
                    //                            new String[]{"复制链接", "分享链接"},
                    //                            mView, AlertView.Style.ActionSheet, new OnItemClickListener() {
                    //                        @Override
                    //                        public void onItemClick(Object o, int position) {
                    //                            switch (position) {
                    //                                case 0:
                    //                                    ClipboardManager cm = (ClipboardManager) mView.getSystemService(Context.CLIPBOARD_SERVICE);
                    //                                    cm.setText(path);
                    //                                    break;
                    //                                case 1:
                    //                                    String title1 = "来自 " + name + " 订单的分享";
                    //                                    ShareUtil.showShare(mView, title1, path, imgPath);
                    //                                    break;
                    //                            }
                    //                        }
                    //                    }).show();
                    String title1 = "来自 " + name + " 订单的分享";
                    //                                    ShareUtil.showShare(mView, title1, path, imgPath);
                    IssueApplication.sharePath=path;
                    IssueApplication.shareRemark=title1;
                    TwoCodeSharePopWindow popWindow = new TwoCodeSharePopWindow(mView, mView);
                    popWindow.onShow(main_ll);
                    popWindow.setListener(new ITwoCodeListener() {
                        @Override
                        public void onTwoCodeChanged(String path) {
                        }
                    });

                    CartDao dao = new CartDao(mView);
                    for (Goods good : mView.mGoodChecks) {
                        dao.deleteOne(good.getId());

                    }
                    IssueApplication.mCartCount = dao.getCount();
                    EventBus.getDefault().post(Constance.CARTCOUNT);

                } else {
                    mView.hideLoading();
                    MyToast.show(mView, "提交失败!");
                }
            }

            @Override
            public void onFailureListener(int what, JSONObject ans) {
                mView.hideLoading();
                MyToast.show(mView, "提交失败!");
            }
        });
    }
}
