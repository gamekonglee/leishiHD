package bc.otlhd.com.ui.view.popwindow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import bc.otlhd.com.R;
import bc.otlhd.com.bean.Goods;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.cons.NetWorkConst;
import bc.otlhd.com.data.GoodsDao;
import bc.otlhd.com.data.SetPriceDao;
import bc.otlhd.com.listener.IDiyProductInfoListener;
import bc.otlhd.com.listener.INetworkCallBack;
import bc.otlhd.com.net.OkHttpUtils;
import bc.otlhd.com.ui.activity.IssueApplication;
import bc.otlhd.com.ui.activity.user.UserLogActivity;
import bc.otlhd.com.ui.adapter.ParamentAdapter02;
import bc.otlhd.com.utils.LogUtils;
import bc.otlhd.com.utils.MyShare;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;


/**
 * @author: Jun
 * @date : 2017/2/16 15:12
 * @description :
 */
public class DiyProductInfoPopWindow extends BasePopwindown implements View.OnClickListener, INetworkCallBack {
    private Activity mActivity;
    private IDiyProductInfoListener mListener;
    private ImageView product_iv, close_iv;
    private RelativeLayout two_bar_codes_rl, parameter_rl, logo_rl, card_rl;
    public  com.alibaba.fastjson.JSONObject  productObject;
    public Goods mGoods;
    private ListView attr_lv;
    private ParamentAdapter02 mParamentAdapter;
    private StringBuffer mParamMsg;
    private SetPriceDao mPriceDao;

    public void setListener(IDiyProductInfoListener listener) {
        mListener = listener;
    }

    public DiyProductInfoPopWindow(Context context, Activity activity) {
        super(context);
        mActivity = activity;
    }

    @Override
    protected void initView(Context context) {
        View contentView = View.inflate(mContext, R.layout.pop_diy_product_info, null);
        initUI(contentView);
    }

    public void initViewData() {
        mPriceDao = new SetPriceDao(mActivity);
        String path ="";
        if (OkHttpUtils.hashkNewwork()) {
             path = NetWorkConst.UR_PRODUCT_URL+ productObject.getString(Constance.img_url);

        }else{
            path= "file://"+mGoods.getImg_url();
        }

        ImageLoader.getInstance().displayImage(path, product_iv);
//        String path = productObject.getJSONObject(Constance.app_img).getString(Constance.img);
//        ImageLoader.getInstance().displayImage(path, product_iv);
        getDetail();
    }


    private void initUI(View contentView) {

        product_iv = (ImageView) contentView.findViewById(R.id.product_iv);
        two_bar_codes_rl = (RelativeLayout) contentView.findViewById(R.id.two_bar_codes_rl);
        parameter_rl = (RelativeLayout) contentView.findViewById(R.id.parameter_rl);
        logo_rl = (RelativeLayout) contentView.findViewById(R.id.logo_rl);
        card_rl = (RelativeLayout) contentView.findViewById(R.id.card_rl);
        close_iv = (ImageView) contentView.findViewById(R.id.close_iv);
        attr_lv = (ListView) contentView.findViewById(R.id.attr_lv);
        product_iv.setOnClickListener(this);
        two_bar_codes_rl.setOnClickListener(this);
        parameter_rl.setOnClickListener(this);
        logo_rl.setOnClickListener(this);
        card_rl.setOnClickListener(this);
        close_iv.setOnClickListener(this);


        mPopupWindow = new PopupWindow(contentView, -1, -1);
        // 1.让mPopupWindow内部的控件获取焦点
        mPopupWindow.setFocusable(true);
        // 2.mPopupWindow内部获取焦点后 外部的所有控件就失去了焦点
        mPopupWindow.setOutsideTouchable(true);
        //只有加载背景图还有效果
        // 3.如果不马上显示PopupWindow 一般建议刷新界面
        mPopupWindow.update();
        // 设置弹出窗体显示时的动画，从底部向上弹出
        mPopupWindow.setAnimationStyle(R.style.AnimBottom);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_iv:
                onDismiss();
                break;
            case R.id.bg_ll:
                onDismiss();
                break;
            case R.id.btn_logistic:
                Intent intent = new Intent(mContext, UserLogActivity.class);
                intent.putExtra(Constance.isSelectLogistice, true);
                mActivity.startActivityForResult(intent, Constance.FROMLOG);
                onDismiss();
                break;
            case R.id.two_bar_codes_rl://二维码
                mListener.onDiyProductInfo(0,null);
                onDismiss();
                break;
            case R.id.parameter_rl://参数
                mListener.onDiyProductInfo(1,mParamMsg.toString());
                onDismiss();
                break;
            case R.id.logo_rl://LOGO
                mListener.onDiyProductInfo(2,null);
                onDismiss();
                break;
            case R.id.card_rl://产品卡
                mListener.onDiyProductInfo(3,mParamMsg.toString());
                onDismiss();
                break;
        }
    }

    /**
     * 产品详情
     */
    public void getDetail() {

        mParamMsg=new StringBuffer();
        if (OkHttpUtils.hashkNewwork()) {
            LogUtils.logE("product",productObject.toJSONString());
            String name = productObject.getString(Constance.name);
            String price = productObject.getString(Constance.shop_price);
            if(MyShare.get(mContext).getBoolean(Constance.SET_PRICE)){
                float setPrice = mPriceDao.getProductPrice( productObject.getInteger(Constance.id) + "").getShop_price();
                price=setPrice+"";
            }
            com.alibaba.fastjson.JSONArray attrs = null;

            try {
                attrs = productObject.getJSONArray(Constance.attr);
            } catch (Exception e) {
                attrs = productObject.getJSONObject(Constance.attr).getJSONArray(Constance.pro);
            }
            for(int i=0;i<attrs.size();i++){
                for(int j=0;j<attrs.size();j++){
                    if(i!=j){
                        if(attrs.getJSONObject(i).getString(Constance.name).equals(attrs.getJSONObject(j).getString(Constance.name))){
                            String value1=attrs.getJSONObject(i).getString(Constance.VALUE);
                            attrs.getJSONObject(i).put(Constance.VALUE,value1+"+"+attrs.getJSONObject(j).getString(Constance.VALUE));
                            attrs.remove(j);
                            j--;
                            i--;
                        }
                    }
                }
            }

            final int goodId = productObject.getInteger(Constance.id);
            float setPrice = mPriceDao.getProductPrice(goodId + "").getShop_price();
            price = setPrice == 0 ? price : setPrice + "";
            List<String> attachs = new ArrayList<>();
            attachs.add("名称:  " + name);

//            mParamMsg.append("名称:  " + name+ "\n");

            for (int i = 0; i < attrs.size(); i++) {
                if (!AppUtils.isEmpty(attrs.getJSONObject(i).getString(Constance.VALUE))) {
                    String attrName = attrs.getJSONObject(i).getString(Constance.name);
                    String attrValeu = attrs.getJSONObject(i).getString(Constance.VALUE);
                    attachs.add(attrName + ":  " + attrValeu);
                        mParamMsg.append(attrName + ": " + attrValeu+"\n");
//                    if(i<attrs.size()-1){
//                    }else{
//                        mParamMsg.append(attrName + ": " + attrValeu);
//                    }
                }

            }
            attachs.add( "价格:  ￥" + price);
            mParamMsg.append("价格:  ￥" + price+ "\n");
            mParamentAdapter = new ParamentAdapter02(attachs, mContext, 1);
            attr_lv.setAdapter(mParamentAdapter);
            attr_lv.setDivider(null);//去除listview的下划线

        }else{
            String name = mGoods.getName();
            String price = mGoods.getShop_price() + "";
            final int goodId = mGoods.getId();
            float setPrice = mPriceDao.getProductPrice(goodId + "").getShop_price();
            price = setPrice == 0 ? price : setPrice + "";
            List<String> attr = getAttrList(goodId + "");


//            attr.add(0, "名称:  " + name);
//            attr.add(1, "价格:  ￥" + price);
            mParamMsg.append("名称:  " + name+ "\n");
            mParamMsg.append("价格:  ￥" + price+ "\n");
            for(int i=0;i<attr.size();i++){
                mParamMsg.append(attr.get(i)+"\n");
//                if(i<attr.size()-1){
//
//                }else{
//                    mParamMsg.append(attr.get(i));
//                }
            }

            mParamentAdapter = new ParamentAdapter02(attr, mContext, 1);
            attr_lv.setAdapter(mParamentAdapter);
            attr_lv.setDivider(null);//去除listview的下划线
        }
    }

    private List<String> getAttrList(String id) {
        return GoodsDao.getAttrForGood(id);
    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
    }

}
