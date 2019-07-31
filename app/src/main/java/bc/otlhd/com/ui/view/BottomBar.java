package bc.otlhd.com.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import bc.otlhd.com.R;


/**
 * @author Jun
 * @time 2016/11/2 15:54
 */
public class BottomBar extends LinearLayout implements View.OnClickListener {
    private TextView frag_top_tv;
    private TextView frag_product_tv;
    private TextView frag_match_tv;
    private TextView frag_cart_tv;
    private TextView frag_mine_tv;
    private TextView frag_goods_tv;
    private TextView frag_tmall_tv;
    private ImageView frag_goods_iv;
    private ImageView frag_top_iv;
    private ImageView frag_product_iv;
    private ImageView frag_match_iv;
    private ImageView frag_cart_iv;
    private ImageView frag_mine_iv;
    private ImageView frag_tmall_iv;
    private IBottomBarItemClickListener mListener;

    private int mCurrenTabId;
    private TextView tv_top_home;
    private TextView tv_top_stlye;
    private TextView tv_top_space;
    private TextView tv_top_type;
    private TextView tv_top_setting;
    private TextView tv_top_hot;

    public BottomBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        frag_top_tv = (TextView) findViewById(R.id.frag_top_tv);
        frag_product_tv = (TextView) findViewById(R.id.frag_product_tv);
        frag_match_tv = (TextView) findViewById(R.id.frag_match_tv);
        frag_cart_tv = (TextView) findViewById(R.id.frag_cart_tv);
        frag_mine_tv = (TextView) findViewById(R.id.frag_mine_tv);
        frag_goods_tv = (TextView) findViewById(R.id.frag_goods_tv);
        frag_tmall_tv = (TextView) findViewById(R.id.frag_tmall_tv);
        tv_top_home = (TextView) findViewById(R.id.frag_top_tv_home);
        tv_top_stlye = (TextView) findViewById(R.id.frag_top_tv_stlye);
        tv_top_space = (TextView) findViewById(R.id.frag_top_tv_space);
        tv_top_type = (TextView) findViewById(R.id.frag_top_tv_type);
        tv_top_setting = (TextView) findViewById(R.id.frag_top_tv_setting);
        tv_top_hot = (TextView) findViewById(R.id.frag_top_tv_hot);


        frag_top_iv = (ImageView) findViewById(R.id.frag_top_iv);
        frag_product_iv = (ImageView) findViewById(R.id.frag_product_iv);
        frag_match_iv = (ImageView) findViewById(R.id.frag_match_iv);
        frag_cart_iv = (ImageView) findViewById(R.id.frag_cart_iv);
        frag_mine_iv = (ImageView) findViewById(R.id.frag_mine_iv);
        frag_goods_iv = (ImageView) findViewById(R.id.frag_goods_iv);
        frag_tmall_iv = (ImageView) findViewById(R.id.frag_tmall_iv);


        findViewById(R.id.frag_top_ll).setOnClickListener(this);
        findViewById(R.id.frag_product_ll).setOnClickListener(this);
        findViewById(R.id.frag_match_ll).setOnClickListener(this);
        findViewById(R.id.frag_cart_ll).setOnClickListener(this);
        findViewById(R.id.frag_mine_ll).setOnClickListener(this);
        findViewById(R.id.frag_goods_ll).setOnClickListener(this);
        findViewById(R.id.frag_tmall_ll).setOnClickListener(this);
        findViewById(R.id.frag_top_ll_home).setOnClickListener(this);
        findViewById(R.id.frag_top_ll_type).setOnClickListener(this);
        findViewById(R.id.frag_top_ll_stlye).setOnClickListener(this);
        findViewById(R.id.frag_top_ll_space).setOnClickListener(this);
        findViewById(R.id.frag_top_ll_setting).setOnClickListener(this);
        findViewById(R.id.frag_top_ll_hot).setOnClickListener(this);
        tv_top_home.setOnClickListener(this);
        tv_top_stlye.setOnClickListener(this);
        tv_top_space.setOnClickListener(this);
        tv_top_type.setOnClickListener(this);
        tv_top_setting.setOnClickListener(this);
        tv_top_hot.setOnClickListener(this);
        frag_match_tv.setOnClickListener(this);
        frag_cart_tv.setOnClickListener(this);
        frag_mine_tv.setOnClickListener(this);
        frag_top_tv.setOnClickListener(this);
        frag_tmall_iv.setOnClickListener(this);
        frag_tmall_tv.setOnClickListener(this);
        findViewById(R.id.frag_top_ll_home).performClick();
    }

    @Override
    public void onClick(View v) {
        //	设置 如果电机的是当前的的按钮 再次点击无效
        if (mCurrenTabId != 0 && mCurrenTabId == v.getId()) {
            return;
        }
        //点击前先默认全部不被选中
        defaultTabStyle();
        int id=v.getId();
        if(id!=R.id.frag_top_ll&&id!=R.id.frag_top_tv&&id!=R.id.frag_match_ll&&id!=R.id.frag_match_tv&&id!=R.id.frag_top_ll_hot&&id!=R.id.frag_top_tv_hot){
            mCurrenTabId = v.getId();
        }
        switch (v.getId()) {
            case R.id.frag_top_ll:
            case R.id.frag_top_tv:
                frag_top_tv.setSelected(true);
                frag_top_iv.setSelected(true);
                break;
            case R.id.frag_product_ll:
                frag_product_tv.setSelected(true);
                frag_product_iv.setSelected(true);
                break;
            case R.id.frag_match_ll:
            case R.id.frag_match_tv:
                frag_match_tv.setSelected(true);
                frag_match_iv.setSelected(true);
                break;
            case R.id.frag_cart_ll:
            case R.id.frag_cart_tv:
                frag_cart_tv.setSelected(true);
                frag_cart_iv.setSelected(true);
                break;
            case R.id.frag_mine_ll:
            case R.id.frag_mine_tv:
                frag_mine_tv.setSelected(true);
                frag_mine_iv.setSelected(true);
                break;
            case R.id.frag_goods_ll:
                frag_goods_tv.setSelected(true);
                frag_goods_iv.setSelected(true);
                break;
            case R.id.frag_tmall_ll:
                frag_tmall_tv.setSelected(true);
                frag_tmall_iv.setSelected(true);
                break;
            case R.id.frag_top_ll_home:
            case R.id.frag_top_tv_home:
                tv_top_home.setSelected(true);
                break;
            case R.id.frag_top_ll_space:
            case R.id.frag_top_tv_space:
                tv_top_space.setSelected(true);
                break;
            case R.id.frag_top_ll_stlye:
            case R.id.frag_top_tv_stlye:
                tv_top_stlye.setSelected(true);
                break;
            case R.id.frag_top_ll_type:
            case R.id.frag_top_tv_type:
                tv_top_type.setSelected(true);
                break;
            case R.id.frag_top_ll_setting:
            case R.id.frag_top_tv_setting:
                tv_top_setting.setSelected(true);
                break;
        }

        if (mListener != null) {
            mListener.OnItemClickListener(v.getId());
        }

    }

    /**
     * 选择指定的item
     * @param currenTabId
     */
    public void selectItem(int currenTabId){
        //	设置 如果电机的是当前的的按钮 再次点击无效
        if (mCurrenTabId != 0 && mCurrenTabId ==currenTabId) {
            return;
        }
        //点击前先默认全部不被选中
        defaultTabStyle();
        int id=currenTabId;
        if(id!=R.id.frag_top_ll&&id!=R.id.frag_top_tv&&id!=R.id.frag_match_ll&&id!=R.id.frag_match_tv&&id!=R.id.frag_top_ll_hot&&id!=R.id.frag_top_tv_hot){
            mCurrenTabId = id;
        }
        switch (id) {
            case R.id.frag_top_ll:
                frag_top_tv.setSelected(true);
                frag_top_iv.setSelected(true);
                break;
            case R.id.frag_product_ll:
                frag_product_tv.setSelected(true);
                frag_product_iv.setSelected(true);
                break;
            case R.id.frag_match_ll:
                frag_match_tv.setSelected(true);
                frag_match_iv.setSelected(true);
                break;
            case R.id.frag_cart_ll:
                frag_cart_tv.setSelected(true);
                frag_cart_iv.setSelected(true);
                break;
            case R.id.frag_mine_ll:
                frag_mine_tv.setSelected(true);
                frag_mine_iv.setSelected(true);
                break;
            case R.id.frag_goods_ll:
                frag_goods_tv.setSelected(true);
                frag_goods_iv.setSelected(true);
                break;
            case R.id.frag_tmall_ll:
                frag_tmall_tv.setSelected(true);
                frag_tmall_iv.setSelected(true);
                break;
            case R.id.frag_top_ll_home:
                tv_top_home.setSelected(true);
                break;
            case R.id.frag_top_ll_space:
                tv_top_space.setSelected(true);
                break;
            case R.id.frag_top_ll_stlye:
                tv_top_stlye.setSelected(true);
                break;
            case R.id.frag_top_ll_type:
                tv_top_type.setSelected(true);
                break;
            case R.id.frag_top_ll_setting:
                tv_top_setting.setSelected(true);
                break;
        }

        if (mListener != null) {
            mListener.OnItemClickListener(currenTabId);
        }
    }


    public void setOnClickListener(IBottomBarItemClickListener listener) {
        this.mListener = listener;
    }

    public interface IBottomBarItemClickListener {
        void OnItemClickListener(int resId);
    }

    /**
     * 默认全部不被选中
     */
    private void defaultTabStyle() {
        frag_top_tv.setSelected(false);
        frag_top_iv.setSelected(false);
        frag_product_tv.setSelected(false);
        frag_product_iv.setSelected(false);
        frag_match_tv.setSelected(false);
        frag_match_iv.setSelected(false);
        frag_cart_tv.setSelected(false);
        frag_cart_iv.setSelected(false);
        frag_mine_tv.setSelected(false);
        frag_mine_iv.setSelected(false);
        frag_goods_tv.setSelected(false);
        frag_goods_iv.setSelected(false);
        frag_tmall_tv.setSelected(false);
        frag_tmall_iv.setSelected(false);

        tv_top_home.setSelected(false);
        tv_top_space.setSelected(false);
        tv_top_stlye.setSelected(false);
        tv_top_type.setSelected(false);
        tv_top_setting.setSelected(false);
        tv_top_hot.setSelected(false);
    }
}
