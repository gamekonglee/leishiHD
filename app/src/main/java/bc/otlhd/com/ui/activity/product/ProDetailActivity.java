package bc.otlhd.com.ui.activity.product;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import bc.otlhd.com.R;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.controller.product.ProductDetailController;
import bocang.json.JSONObject;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/2/13 17:50
 * @description :
 */
public class ProDetailActivity extends BaseActivity {
    public int mProductId;
    private ProductDetailController mController;
    private LinearLayout product_ll, detail_ll, parament_ll,callLl,shopping_cart_Ll;
    private Button toDiyBtn,toCartBtn;
    private ImageView share_iv;
    public static JSONObject goodses;
    public String mProperty="";
    public String mPropertyValue="";

    public  com.alibaba.fastjson.JSONObject mProductObject;


    @Override
    protected void InitDataView() {
//        mController.sendProductDetail();
    }

    @Override
    protected void initController() {
        mController = new ProductDetailController(this);
    }


    @Override
    protected void initView() {
        setContentView(R.layout.activity_product_detail);
        product_ll = getViewAndClick(R.id.product_ll);
        detail_ll = getViewAndClick(R.id.detail_ll);
        parament_ll = getViewAndClick(R.id.parament_ll);
        callLl = getViewAndClick(R.id.callLl);
        shopping_cart_Ll = getViewAndClick(R.id.shopping_cart_Ll);
        toDiyBtn = getViewAndClick(R.id.toDiyBtn);
        toCartBtn = getViewAndClick(R.id.toCartBtn);
        share_iv = getViewAndClick(R.id.share_iv);

    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mProductId = intent.getIntExtra(Constance.product, 0);

    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.product_ll:
                mController.selectProductType(R.id.product_ll);
                break;
            case R.id.detail_ll:
                mController.selectProductType(R.id.detail_ll);
                break;
            case R.id.parament_ll:
                mController.selectProductType(R.id.parament_ll);
                break;
            case R.id.callLl:
                mController.sendCall();
                break;
            case R.id.shopping_cart_Ll:
                mController.getShoopingCart();
                break;
            case R.id.toDiyBtn:
                mController.GoDiyProduct();
                break;
            case R.id.toCartBtn:
                mController.GoShoppingCart();
                break;
            case R.id.share_iv:
                mController.setShare();
                break;
        }
    }


}
