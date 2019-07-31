package bc.otlhd.com.ui.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.lib.common.hxp.view.PullToRefreshLayout;
import com.lib.common.hxp.view.PullableScrollView;

import bc.otlhd.com.R;
import bc.otlhd.com.common.BaseFragment;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.controller.HomeController;
import bc.otlhd.com.ui.activity.product.SelectGoodsActivity;
import bc.otlhd.com.ui.activity.user.MessageActivity;
import bc.otlhd.com.ui.activity.user.SimpleScannerActivity;
import bc.otlhd.com.utils.ColorUtil;
import bc.otlhd.com.utils.DensityUtil;
import bocang.utils.AppUtils;
import bocang.utils.IntentUtil;

/**
 * @author Jun
 * @time 2017/1/5  12:00
 * @desc 首页面
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {
    private HomeController mController;
    private TextView typeTv,styleTv,spaceTv,moreTv;
    private ImageView lineIv;
    private float mCurrentCheckedRadioLeft;//当前被选中的Button距离左侧的距离
    private FrameLayout fl_ll;
    private PullableScrollView scrollView;
    private ConvenientBanner convenientBanner;
    private EditText et_search;
    private ImageView topLeftBtn;
    private ImageView topRightBtn;
    private PullToRefreshLayout refresh_view;

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fm_home, null);
//    }

    @Override
    protected int getLayout() {
        return R.layout.fm_home;
    }

    @Override
    protected void initController() {
        mController=new HomeController(this);
    }

    @Override
    protected void initViewData() {
        typeTv.performClick();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void initView() {
        typeTv = (TextView) getActivity().findViewById(R.id.typeTv);
        styleTv = (TextView) getActivity().findViewById(R.id.styleTv);
        spaceTv = (TextView) getActivity().findViewById(R.id.spaceTv);
        moreTv = (TextView) getActivity().findViewById(R.id.moreTv);
        lineIv = (ImageView) getActivity().findViewById(R.id.lineIv);
        typeTv.setOnClickListener(this);
        styleTv.setOnClickListener(this);
        spaceTv.setOnClickListener(this);
        moreTv.setOnClickListener(this);
        et_search = (EditText) getActivity().findViewById(R.id.et_search);
        topLeftBtn = (ImageView) getActivity().findViewById(R.id.topLeftBtn);
        topRightBtn = (ImageView) getActivity().findViewById(R.id.topRightBtn);
        et_search.setOnClickListener(this);
        topLeftBtn.setOnClickListener(this);
        topRightBtn.setOnClickListener(this);

        convenientBanner = (ConvenientBanner) getActivity().findViewById(R.id.convenientBanner);
        fl_ll = (FrameLayout) getActivity().findViewById(R.id.fl_ll);
        scrollView = (PullableScrollView) getActivity().findViewById(R.id.scrollView);
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                if (bann
                if (convenientBanner != null) {

                    int y=scrollY-oldScrollY;
                    bannerViewTopMargin = DensityUtil.px2dip(getActivity(), (convenientBanner.getHeight()-scrollY));
                    Log.v("520it","bannerViewTopMargin:"+bannerViewTopMargin);
                    bannerViewHeight = DensityUtil.px2dip(getActivity(), convenientBanner.getHeight());
                    Log.v("520it","bannerViewHeight:"+bannerViewHeight);
                }

                handleTitleBarColorEvaluate();
            }
        });

        refresh_view = (PullToRefreshLayout) getActivity().findViewById(R.id.refresh_view);
//        et_search.setAlpha(0.5f);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        mController.setResume();
    }

    @Override
    public void onPause() {
        super.onPause();
       mController.setPause();
    }

    @Override
    public void onClick(View v) {
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnimation = null;
        mController.reSetTextColor();
        switch (v.getId()){
            case R.id.typeTv:
                mController.getType();
                translateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, 0f, -20f, -20f);
                break;
            case R.id.styleTv:
                mController.getStyle();
                translateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, mController.mScreenWidth / 4f, -20f, -20f);
                break;
            case R.id.spaceTv:
                mController.getSpace();
                translateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, mController.mScreenWidth * 2f / 4f, -20f, -20f);
                break;
            case R.id.moreTv:
                Intent intent = new Intent(getActivity(), SelectGoodsActivity.class);
                intent.putExtra(Constance.categories, "");
                getActivity().startActivity(intent);
                break;
            case R.id.topLeftBtn://扫描二维码
                IntentUtil.startActivity(this.getActivity(), SimpleScannerActivity.class, false);
                break;
            case R.id.topRightBtn://消息
                IntentUtil.startActivity(this.getActivity(),MessageActivity.class,false);
                break;
            case R.id.et_search://搜索产品
                IntentUtil.startActivity(this.getActivity(),SelectGoodsActivity.class,false);
                break;
        }
        if(AppUtils.isEmpty(translateAnimation))return;
        animationSet.addAnimation(translateAnimation);
        animationSet.setFillBefore(false);
        animationSet.setFillAfter(true);
        animationSet.setDuration(100);
        lineIv.startAnimation(animationSet);

        mCurrentCheckedRadioLeft = mController.getCurrentCheckedRadioLeft(v);//更新当前横条距离左边的距离
    }
    private int bannerViewTopMargin; // 广告视图距离顶部的距离
    private int bannerViewHeight = 150; // 广告视图的高度
    private int titleViewHeight = 65; // 标题栏的高度
    private boolean isStickyTop = false; // 是否吸附在顶部

    // 处理标题栏颜色渐变
    private void handleTitleBarColorEvaluate() {
        float fraction;
        if (bannerViewTopMargin > 0) {
            fraction = 1f - bannerViewTopMargin * 1f / 60;
            if (fraction < 0f) fraction = 0f;
            fl_ll.setAlpha(fraction);
//            et_search.setAlpha(fraction+0.5f);
            return ;
        }

        float space = Math.abs(bannerViewTopMargin) * 1f;
        fraction = space / (bannerViewHeight - titleViewHeight);
        if (fraction < 0f) fraction = 0f;
        if (fraction > 1f) fraction = 1f;
        fl_ll.setAlpha(1f);
//        et_search.setAlpha(1f);



        if (fraction >= 1f || isStickyTop) {
            isStickyTop = true;
            fl_ll.setBackgroundColor(getActivity().getResources().getColor(R.color.yellow));
//            et_search.setBackgroundColor(getActivity().getResources().getColor(R.color.ed_f8));
        } else {
            fl_ll.setBackgroundColor(ColorUtil.getNewColorByStartEndColor(getActivity(), fraction, R.color.transparent, R.color.yellow));
//            et_search.setBackgroundColor(ColorUtil.getNewColorByStartEndColor(getActivity(), fraction, R.color.transparent, R.color.ed_f8));
        }
    }




}
