package bc.otlhd.com.ui.activity.product;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import androidx.annotation.NonNull;

//import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import astuetz.MyPagerSlidingTabStrip;
import bc.otlhd.com.R;
import bc.otlhd.com.bean.Goods;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.controller.product.ProListController;
import bc.otlhd.com.data.CartDao;
import bc.otlhd.com.ui.activity.IssueApplication;
import bc.otlhd.com.ui.activity.buy.ShoppingCartActivity;
import bocang.utils.IntentUtil;
import bocang.view.BaseActivity;

/**
 * Created by gamekonglee on 2019/2/18.
 */

public class ProListActivity extends BaseActivity {

    public ViewPager vp;
    public MyPagerSlidingTabStrip pagerSlidingTabStrip;
    public int current;
    private ProListController proListController;
    private TextView tv_hot;
    private TextView tv_style;
    private TextView tv_space;
    private TextView tv_type;
    public int currentCatogoryMain;
    public ImageView iv_top;
    private EditText et_search;
    public String attr;
    public String attr_value;
    public boolean is_hot;
    public boolean is_hot_send;
    public String keyword;
    private TextView unMessageReadTv;
    private View cart_ll;
    private TextView tv_brand;
    private boolean isOnSale;
    private TextView tv_is_on_sale;
    private TextView tv_not_sale;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        proListController = new ProListController(this);
        proListController.requestData();
        getCartCount();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_product_list);
        EventBus.getDefault().register(this);
        vp = (ViewPager) findViewById(R.id.vPager);
        pagerSlidingTabStrip = (MyPagerSlidingTabStrip) findViewById(R.id.tab_strip);
        tv_hot = (TextView) findViewById(R.id.tv_hot);
        tv_brand = (TextView) findViewById(R.id.tv_brand);
        tv_style = (TextView) findViewById(R.id.tv_style);
        tv_space = (TextView) findViewById(R.id.tv_space);
        tv_type = (TextView) findViewById(R.id.tv_type);
        iv_top = (ImageView) findViewById(R.id.iv_top);
        et_search = (EditText) findViewById(R.id.et_search);
        unMessageReadTv = (TextView) findViewById(R.id.unMessageReadTv);
        cart_ll = findViewById(R.id.cart_Ll);
        tv_is_on_sale = (TextView) findViewById(R.id.tv_on_sale);
        tv_not_sale = (TextView) findViewById(R.id.tv_not_sale);
        tv_hot.setOnClickListener(this);
        tv_brand.setOnClickListener(this);
        tv_style.setOnClickListener(this);
        tv_space.setOnClickListener(this);
        tv_type.setOnClickListener(this);
        iv_top.setOnClickListener(this);
        cart_ll.setOnClickListener(this);
        tv_is_on_sale.setOnClickListener(this);
        tv_not_sale.setOnClickListener(this);
        tv_hot.performClick();
        pagerSlidingTabStrip.setVisibility(View.INVISIBLE);
        pagerSlidingTabStrip.selectColor=(getResources().getColor(R.color.colorPrimaryGreen2));
        pagerSlidingTabStrip.defaultColor=getResources().getColor(R.color.txt_black);
        pagerSlidingTabStrip.setDividerColor(Color.TRANSPARENT);
        pagerSlidingTabStrip.setIndicatorColor(getResources().getColor(R.color.colorPrimaryGreen2));
        pagerSlidingTabStrip.setIndicatorHeight(getResources().getDimensionPixelOffset(R.dimen.x1));
        pagerSlidingTabStrip.setUnderlineColor(getResources().getColor(R.color.transparent));
        pagerSlidingTabStrip.setUnderlineHeight(0);


        et_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                // 修改回车键功能
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(ProListActivity.this
                                            .getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                        //接下来在这里做你自己想要做的事，实现自己的业务。
                        if(proListController!=null){
                            proListController.searchData(et_search.getText().toString());
                        }
                }

                return false;
            }
        });

    }

    @Override
    protected void initData() {
        current = 0;
        currentCatogoryMain = 0;
        attr =getIntent().getStringExtra(Constance.attr);
        attr_value = getIntent().getStringExtra(Constance.attr_value);
        is_hot = getIntent().getBooleanExtra(Constance.is_hot,false);
        keyword = getIntent().getStringExtra(Constance.key_word);
    }

    @Override
    protected void onViewClick(View v) {
        if(!IssueApplication.IsRoot()){
            SystemClock.sleep(IssueApplication.SLEEP_TIME);
        }
    switch (v.getId()){
        case R.id.tv_hot:
            currentCatogoryMain=0;
            refreshCategory();
            break;
        case R.id.tv_brand:
            currentCatogoryMain=1;
            refreshCategory();
            break;
        case R.id.tv_style:
            currentCatogoryMain=2;
            refreshCategory();
            break;
        case R.id.tv_type:
            currentCatogoryMain=3;
            refreshCategory();
            break;
        case R.id.tv_space:
            currentCatogoryMain=4;
            refreshCategory();
            break;
        case R.id.iv_top:
            proListController.mGoodsSv.setSelection(0);
            iv_top.setVisibility(View.VISIBLE);
            break;
        case R.id.cart_Ll:
            IntentUtil.startActivity(this, ShoppingCartActivity.class, false);
            break;
        case R.id.tv_on_sale:
//            if(isOnSale)return;
            isOnSale = true;
            refreshOnSaleUI();
            break;
        case R.id.tv_not_sale:
//            if(!isOnSale)return;
            isOnSale = false;
            refreshOnSaleUI();
            break;

    }
    }

    private void refreshOnSaleUI() {
            tv_is_on_sale.setBackground(getResources().getDrawable(R.drawable.bg_corner_green_5_left));
            tv_not_sale.setBackground(getResources().getDrawable(R.drawable.bg_corner_green_5_right));
            tv_is_on_sale.setTextColor(getResources().getColor(R.color.white));
            tv_not_sale.setTextColor(getResources().getColor(R.color.white));
            if(isOnSale){
                tv_is_on_sale.setTextColor(getResources().getColor(R.color.colorPrimaryGreen2));
                tv_is_on_sale.setBackground(getResources().getDrawable(R.drawable.bg_corner_white_5_left));
                proListController.on_sale="1";
            }else {
                tv_not_sale.setTextColor(getResources().getColor(R.color.colorPrimaryGreen2));
                tv_not_sale.setBackground(getResources().getDrawable(R.drawable.bg_corner_white_5_right));
                proListController.on_sale="2";
            }
            proListController.page=1;
            proListController.goodses=new ArrayList<>();
            proListController.isBottom=false;
            proListController.sendGoodsList("0",0);

    }

    private void refreshCategory() {
        Drawable drawable=null;
        tv_hot.setTextColor(getResources().getColor(R.color.color_555555));
        tv_style.setTextColor(getResources().getColor(R.color.color_555555));
        tv_space.setTextColor(getResources().getColor(R.color.color_555555));
        tv_type.setTextColor(getResources().getColor(R.color.color_555555));
        tv_brand.setTextColor(getResources().getColor(R.color.color_555555));
        drawable=getResources().getDrawable(R.mipmap.tab_icon_recommended_default);
        drawable.setBounds(0,0, getResources().getDimensionPixelOffset(R.dimen.x19),getResources().getDimensionPixelOffset(R.dimen.x19));
        tv_hot.setCompoundDrawables(null,drawable,null,null);
        drawable=getResources().getDrawable(R.mipmap.tab_icon_style_default);
        drawable.setBounds(0,0, getResources().getDimensionPixelOffset(R.dimen.x16),getResources().getDimensionPixelOffset(R.dimen.x19));
        tv_style.setCompoundDrawables(null,drawable,null,null);
        drawable=getResources().getDrawable(R.mipmap.tab_icon_type_default);
        drawable.setBounds(0,0, getResources().getDimensionPixelOffset(R.dimen.x19),getResources().getDimensionPixelOffset(R.dimen.x19));
        tv_type.setCompoundDrawables(null,drawable,null,null);
        drawable=getResources().getDrawable(R.mipmap.tab_icon_space_default);
        drawable.setBounds(0,0, getResources().getDimensionPixelOffset(R.dimen.x19),getResources().getDimensionPixelOffset(R.dimen.x19));
        tv_space.setCompoundDrawables(null,drawable,null,null);
        drawable=getResources().getDrawable(R.mipmap.tab_icon_brand_default);
        drawable.setBounds(0,0, getResources().getDimensionPixelOffset(R.dimen.x19),getResources().getDimensionPixelOffset(R.dimen.x19));
        tv_brand.setCompoundDrawables(null,drawable,null,null);
        switch (currentCatogoryMain){
            case 0:
                drawable=getResources().getDrawable(R.mipmap.tab_icon_recommended_selected);
                drawable.setBounds(0,0, getResources().getDimensionPixelOffset(R.dimen.x19),getResources().getDimensionPixelOffset(R.dimen.x19));
                tv_hot.setCompoundDrawables(null,drawable,null,null);
                tv_hot.setTextColor(getResources().getColor(R.color.colorPrimaryGreen2));
                break;
            case 1:
                drawable=getResources().getDrawable(R.mipmap.tab_icon_brand_selected);
                drawable.setBounds(0,0, getResources().getDimensionPixelOffset(R.dimen.x19),getResources().getDimensionPixelOffset(R.dimen.x19));
                tv_brand.setCompoundDrawables(null,drawable,null,null);
                tv_brand.setTextColor(getResources().getColor(R.color.colorPrimaryGreen2));
                break;
            case 2:
                drawable=getResources().getDrawable(R.mipmap.tab_icon_style_selected);
                drawable.setBounds(0,0, getResources().getDimensionPixelOffset(R.dimen.x16),getResources().getDimensionPixelOffset(R.dimen.x19));
                tv_style.setCompoundDrawables(null,drawable,null,null);
                tv_style.setTextColor(getResources().getColor(R.color.colorPrimaryGreen2));
                break;
            case 3:
                drawable=getResources().getDrawable(R.mipmap.tab_icon_type_selected);
                drawable.setBounds(0,0, getResources().getDimensionPixelOffset(R.dimen.x19),getResources().getDimensionPixelOffset(R.dimen.x19));
                tv_type.setCompoundDrawables(null,drawable,null,null);
                tv_type.setTextColor(getResources().getColor(R.color.colorPrimaryGreen2));
                break;
            case 4:
                drawable=getResources().getDrawable(R.mipmap.tab_icon_space_selected);
                drawable.setBounds(0,0, getResources().getDimensionPixelOffset(R.dimen.x19),getResources().getDimensionPixelOffset(R.dimen.x19));
                tv_space.setCompoundDrawables(null,drawable,null,null);
                tv_space.setTextColor(getResources().getColor(R.color.colorPrimaryGreen2));
                break;
        }
        if(proListController!=null)proListController.getCategoryData(currentCatogoryMain);

    }

    public void refreshAttr() {
        switch (currentCatogoryMain){
            case 0:
                tv_hot.performClick();
                break;
            case 1:
                tv_brand.performClick();
                break;
            case 2:
                tv_style.performClick();
                break;
            case 3:
                tv_type.performClick();
                break;
            case 4:
                tv_space.performClick();
                break;
        }

    }
    //在主线程执行
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserEvent(Integer action) {
        if (action == Constance.CARTCOUNT) {
          getCartCount();
        }
    }

    private void getCartCount() {
        CartDao dao = new CartDao(this);

        int count =0;
        List<Goods> goodsList=dao.getAll();
        if(goodsList!=null&&goodsList.size()>0){
            for(int i=0;i<goodsList.size();i++){
                count+=goodsList.get(i).getBuyCount();
            }
        }
        IssueApplication.mCartCount = count;
        unMessageReadTv.setVisibility(IssueApplication.mCartCount == 0 ? View.GONE : View.VISIBLE);
        unMessageReadTv.setText(IssueApplication.mCartCount + "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
