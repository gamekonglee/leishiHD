package bc.otlhd.com.controller.product;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import bc.otlhd.com.R;
import bc.otlhd.com.common.BaseFragment;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.cons.NetWorkConst;
import bc.otlhd.com.controller.BaseController;
import bc.otlhd.com.listener.INetworkCallBack;
import bc.otlhd.com.listener.IParamentChooseListener;
import bc.otlhd.com.ui.activity.IssueApplication;
import bc.otlhd.com.ui.activity.buy.ShoppingCartActivity;
import bc.otlhd.com.ui.activity.product.ProDetailActivity;
import bc.otlhd.com.ui.activity.programme.DiyActivity;
import bc.otlhd.com.ui.fragment.DetailGoodsFragmemt;
import bc.otlhd.com.ui.fragment.IntroduceGoodsFragment;
import bc.otlhd.com.ui.fragment.ParameterFragment;
import bc.otlhd.com.ui.view.popwindow.SelectParamentPopWindow;
import bc.otlhd.com.utils.ShareUtil;
import bc.otlhd.com.utils.UIUtils;
import bocang.json.JSONObject;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;
import bocang.utils.IntentUtil;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/2/13 17:58
 * @description :
 */
public class ProductDetailController extends BaseController implements INetworkCallBack, ViewPager.OnPageChangeListener {
    private ProDetailActivity mView;
    private View product_view, detail_view, parament_view;
    private TextView product_tv, detail_tv, parament_tv;
    private ProductContainerAdapter mContainerAdapter;
    private ViewPager container_vp;
    private Intent mIntent;
    private LinearLayout title_ll, product_ll, detail_ll, parament_ll, main_ll;


    public ProductDetailController(ProDetailActivity v) {
        mView = v;
        initView();
        initViewData();
    }


    private void initViewData() {
        sendProductDetail();
        sendCustom();
    }

    private void initView() {
        product_view = mView.findViewById(R.id.product_view);
        detail_view = mView.findViewById(R.id.detail_view);
        parament_view = mView.findViewById(R.id.parament_view);
        product_tv = (TextView) mView.findViewById(R.id.product_tv);
        detail_tv = (TextView) mView.findViewById(R.id.detail_tv);
        parament_tv = (TextView) mView.findViewById(R.id.parament_tv);
        container_vp = (ViewPager) mView.findViewById(R.id.container_vp);
        mContainerAdapter = new ProductContainerAdapter(mView.getSupportFragmentManager());
        container_vp.setAdapter(mContainerAdapter);
        container_vp.setOnPageChangeListener(this);
        container_vp.setCurrentItem(0);
        title_ll = (LinearLayout) mView.findViewById(R.id.title_ll);
        product_ll = (LinearLayout) mView.findViewById(R.id.product_ll);
        main_ll = (LinearLayout) mView.findViewById(R.id.main_ll);
        detail_ll = (LinearLayout) mView.findViewById(R.id.detail_ll);
        parament_ll = (LinearLayout) mView.findViewById(R.id.parament_ll);
        container_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    mFragments.get(position).onStart();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    /**
     * 产品详情不同选择
     *
     * @param type
     */
    public void selectProductType(int type) {
        product_view.setVisibility(View.INVISIBLE);
        detail_view.setVisibility(View.INVISIBLE);
        parament_view.setVisibility(View.INVISIBLE);
        product_tv.setTextColor(mView.getResources().getColor(R.color.txt_black));
        detail_tv.setTextColor(mView.getResources().getColor(R.color.txt_black));
        parament_tv.setTextColor(mView.getResources().getColor(R.color.txt_black));
        switch (type) {
            case R.id.product_ll:
                product_view.setVisibility(View.VISIBLE);
                product_tv.setTextColor(mView.getResources().getColor(R.color.colorPrimaryRed));
                container_vp.setCurrentItem(0, true);
                break;
            case R.id.detail_ll:
                detail_view.setVisibility(View.VISIBLE);
                detail_tv.setTextColor(mView.getResources().getColor(R.color.colorPrimaryRed));
                container_vp.setCurrentItem(1, true);
                break;
            case R.id.parament_ll:
                parament_view.setVisibility(View.VISIBLE);
                parament_tv.setTextColor(mView.getResources().getColor(R.color.colorPrimaryRed));
                container_vp.setCurrentItem(2, true);
                break;
        }

    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    /**
     * 产品详情
     */
    public void sendProductDetail() {
        mNetWork.sendProductDetail(mView.mProductId, this);
    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        switch (requestCode) {
            case NetWorkConst.PRODUCTDETAIL:
                mView.goodses = ans.getJSONObject(Constance.product);
                break;
            case NetWorkConst.CUSTOM:
                NetWorkConst.QQ = ans.getString(Constance.custom);
                break;
            case NetWorkConst.ADDCART:
                MyToast.show(mView, UIUtils.getString(R.string.go_cart_ok));
                sendShoppingCart();
                break;
            case NetWorkConst.GETCART:
                if (ans.getJSONArray(Constance.goods_groups).length() > 0) {
                    IssueApplication.mCartCount = ans.getJSONArray(Constance.goods_groups)
                            .getJSONObject(0).getJSONArray(Constance.goods).length();
                } else {
                    IssueApplication.mCartCount = 0;
                }
                EventBus.getDefault().post(Constance.CARTCOUNT);
                break;
        }
    }

    public void sendShoppingCart() {
        mNetWork.sendShoppingCart(this);
    }

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

    private void sendCustom() {
        mNetWork.sendCustom(this);
    }

    /**
     * 联系客服
     */
    public void sendCall() {
        mView.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(NetWorkConst.QQURL)));
    }

    /**
     * 购物车
     */
    public void getShoopingCart() {
        IntentUtil.startActivity(mView, ShoppingCartActivity.class, false);
    }

    /**
     * 马上配配看
     */
    public void GoDiyProduct() {
        if (AppUtils.isEmpty(mView.goodses)) {
            MyToast.show(mView, "还没加载完毕，请稍后再试");
            return;
        }
        mIntent = new Intent(mView, DiyActivity.class);
        mIntent.putExtra(Constance.product, mView.goodses);
        mIntent.putExtra(Constance.property, mView.mProperty);
        mView.startActivity(mIntent);
    }

    /**
     * 加入购物车
     */
    public void GoShoppingCart() {

        if (AppUtils.isEmpty(mView.mProductObject))
            return;
        if (mView.mProductObject.getJSONArray(Constance.properties).size() == 0) {
            mView.setShowDialog(true);
            mView.setShowDialog("正在加入购物车中...");
            mView.showLoading();
            sendGoShoppingCart(mView.mProductId + "", "", 1);
        } else {
            selectParament();
        }

    }

    private SelectParamentPopWindow mPopWindow;

    /*
         * 选择参数
         */
    public void selectParament() {
        if (AppUtils.isEmpty(mView.mProductObject))
            return;
        mPopWindow = new SelectParamentPopWindow(mView, mView.mProductObject);
        mPopWindow.onShow(main_ll);
        mPopWindow.setListener(new IParamentChooseListener() {
            @Override
            public void onParamentChanged(String text, Boolean isGoCart, String property, int mount) {
                if (!AppUtils.isEmpty(text)) {
                    mView.mProperty=property;
                    mView.mPropertyValue=text;
                }
                if (isGoCart == true) {
                    mView.setShowDialog(true);
                    mView.setShowDialog("正在加入购物车中...");
                    mView.showLoading();
                    sendGoShoppingCart(mView.mProductId + "", property, mount);
                }

                EventBus.getDefault().post(Constance.PROPERTY);
            }
        });

    }

    private void sendGoShoppingCart(String product, String property, int mount) {
        mNetWork.sendShoppingCart(product, property, mount, this);
    }


    /**
     *
     */
    public void setShare() {
        if (AppUtils.isEmpty(mView.mProductObject))
            return;
        final String title = "来自 " + mView.mProductObject.getString(Constance.name) + " 产品的分享";
        final String path = mView.mProductObject.getString(Constance.share_url);
        final String imagePath = mView.mProductObject.getJSONObject(Constance.default_photo).getString(Constance.thumb);

        new AlertView(null, null, "取消", null,
                new String[]{"复制链接", "分享图片", "分享链接"},
                mView, AlertView.Style.ActionSheet, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                switch (position) {
                    case 0:
                        ClipboardManager cm = (ClipboardManager) mView.getSystemService(Context.CLIPBOARD_SERVICE);
                        cm.setText(path);
                        break;
                    case 1:
                        ShareUtil.showShare01(mView, title, "1", imagePath);
                        break;
                    case 2:
                        ShareUtil.showShare(mView, title, path, imagePath);
                        break;
                }
            }
        }).show();



//        ShareUtil.showShare(mView, title, path, imagePath);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                selectProductType(R.id.product_ll);
                break;
            case 1:
                selectProductType(R.id.detail_ll);
                break;
            case 2:
                selectProductType(R.id.parament_ll);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private ArrayList<BaseFragment> mFragments;

    public class ProductContainerAdapter extends FragmentPagerAdapter {


        public ProductContainerAdapter(FragmentManager fm) {
            super(fm);
            initFragment();
        }

        private void initFragment() {
            mFragments = new ArrayList<>();
            Bundle bundle = new Bundle();
            bundle.putString(Constance.product, mView.mProductId + "");
            IntroduceGoodsFragment matchFragment = new IntroduceGoodsFragment();
            matchFragment.setArguments(bundle);
            matchFragment.setmListener(new IntroduceGoodsFragment.ScrollListener() {
                @Override
                public void onScrollToBottom(int currPosition) {
                    if (currPosition == 0) {
                        title_ll.setVisibility(View.GONE);
                        product_ll.setVisibility(View.VISIBLE);
                        detail_ll.setVisibility(View.VISIBLE);
                        parament_ll.setVisibility(View.VISIBLE);
                    } else {
                        title_ll.setVisibility(View.VISIBLE);
                        product_ll.setVisibility(View.GONE);
                        detail_ll.setVisibility(View.GONE);
                        parament_ll.setVisibility(View.GONE);
                    }
                }
            });


            DetailGoodsFragmemt detailFragment = new DetailGoodsFragmemt();
            detailFragment.setArguments(bundle);

            ParameterFragment parameterFragment = new ParameterFragment();
            parameterFragment.setArguments(bundle);

            mFragments.add(matchFragment);
            mFragments.add(detailFragment);
            mFragments.add(parameterFragment);

        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

    }
}
