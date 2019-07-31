package bc.otlhd.com.controller.product;

import android.content.Context;
import android.graphics.Paint;
import android.os.Message;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.CBPageAdapter;
import com.bigkoo.convenientbanner.CBViewHolderCreator;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import bc.otlhd.com.R;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.cons.NetWorkConst;
import bc.otlhd.com.controller.BaseController;
import bc.otlhd.com.listener.INetworkCallBack;
import bc.otlhd.com.listener.INoHttpNetworkCallBack;
import bc.otlhd.com.listener.IParamentChooseListener;
import bc.otlhd.com.ui.activity.IssueApplication;
import bc.otlhd.com.ui.activity.product.ProDetailActivity;
import bc.otlhd.com.ui.fragment.IntroduceGoodsFragment;
import bc.otlhd.com.ui.view.popwindow.SelectParamentPopWindow;
import bc.otlhd.com.utils.UIUtils;
import bocang.json.JSONObject;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/2/14 17:59
 * @description :
 */
public class IntroduceGoodsController extends BaseController implements INetworkCallBack {
    private IntroduceGoodsFragment mView;
    private ConvenientBanner mConvenientBanner;
    private List<String> paths = new ArrayList<>();
    private WebView mWebView;
    private TextView unPriceTv, proNameTv, proPriceTv;
    private TextView mParamentTv;
    private ImageView collect_iv;
    private int mIsLike = 0;
    private RelativeLayout rl_rl;
    private String mProperty;

    public IntroduceGoodsController(IntroduceGoodsFragment v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {

        if(AppUtils.isEmpty(((ProDetailActivity)mView.getActivity()).mProductObject)){
            sendProductDetail();
        }else{
            getProductDetail(((ProDetailActivity)mView.getActivity()).mProductObject);
        }

    }

    private void initView() {
        int mScreenWidth = mView.getActivity().getResources().getDisplayMetrics().widthPixels;
        mConvenientBanner = (ConvenientBanner) mView.getActivity().findViewById(R.id.convenientBanner);
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) mConvenientBanner.getLayoutParams();
        rlp.width = mScreenWidth;
        rlp.height = mScreenWidth - 20;
        mConvenientBanner.setLayoutParams(rlp);

        mWebView = (WebView) mView.getActivity().findViewById(R.id.webView);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");

        unPriceTv = (TextView) mView.getActivity().findViewById(R.id.unPriceTv);
        proNameTv = (TextView) mView.getActivity().findViewById(R.id.proNameTv);
        proPriceTv = (TextView) mView.getActivity().findViewById(R.id.proPriceTv);
        mParamentTv = (TextView) mView.getActivity().findViewById(R.id.type_tv);
        collect_iv = (ImageView) mView.getActivity().findViewById(R.id.collect_iv);
        rl_rl = (RelativeLayout) mView.getActivity().findViewById(R.id.rl_rl);

    }

    /**
     * 产品详情
     */
    public void sendProductDetail() {
        mView.setShowDialog(true);
        mView.setShowDialog("载入中...");
        mView.showLoading();

        String id = mView.productId;
        if (AppUtils.isEmpty(id))
            return;


        mNetWork.sendProductDetail02(id, new INoHttpNetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                mView.hideLoading();
                ((ProDetailActivity) mView.getActivity()).mProductObject = ans.getJSONObject(Constance.product);
                getProductDetail(((ProDetailActivity) mView.getActivity()).mProductObject);
            }

            @Override
            public void onFailureListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                mView.hideLoading();
                MyToast.show(mView.getActivity(), "数据异常!");
            }
        });

    }


    /**
     * 获取产品详情信息
     */
    private void getProductDetail(com.alibaba.fastjson.JSONObject productObject) {
        final String value = productObject.getString(Constance.goods_desc);
        mIsLike = productObject.getInteger(Constance.is_liked);
        final String productName = productObject.getString(Constance.name);
        final String current_price = productObject.getString(Constance.current_price);
        final String price = productObject.getString(Constance.price);


        com.alibaba.fastjson.JSONArray array = productObject.getJSONArray(Constance.photos);
        for (int i = 0; i < array.size(); i++) {
            paths.add(array.getJSONObject(0).getString(Constance.large));
        }

        getWebView(value);
        mConvenientBanner.setPages(
                new CBViewHolderCreator<NetworkImageHolderView>() {
                    @Override
                    public NetworkImageHolderView createHolder() {
                        return new NetworkImageHolderView();
                    }
                }, paths);
        mConvenientBanner.setPageIndicator(new int[]{R.drawable.dot_unfocuse, R.drawable.dot_focuse});
        proNameTv.setText(productName);
        proPriceTv.setText("￥" + current_price);
        unPriceTv.setText("￥" + price);
        unPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        selectCollect();
    }


    /**
     * 收藏图标状态
     */
    private void selectCollect() {
        if (mIsLike == 0) {
            collect_iv.setImageResource(R.drawable.ic_collect_normal);
        } else {
            collect_iv.setImageResource(R.drawable.ic_collect_press);
        }
    }

    /**
     * 加载网页
     *
     * @param htmlValue
     */
    private void getWebView(String htmlValue) {
        String html = htmlValue;
        html = html.replace("<img src=\"", "<img src=\"" + NetWorkConst.SCENE_HOST);
        html = "<meta name=\"viewport\" content=\"width=device-width\">" + html;
        mWebView.loadData(html, "text/html; charset=UTF-8", null);//这种写法可以正确解析中文
    }

    /**
     * 加入收藏
     */
    public void sendCollectGoods() {
        mView.setShowDialog(true);
        mView.setShowDialog("正在收藏中!");
        mView.showLoading();
        String id = mView.productId;
        if (mIsLike == 0) {
            mNetWork.sendAddLikeCollect(id, this);
            mIsLike = 1;
        } else {
            mNetWork.sendUnLikeCollect(id, this);
            mIsLike = 0;
        }
    }

    private SelectParamentPopWindow mPopWindow;

    /*
     * 选择参数
     */
    public void selectParament() {
        if (AppUtils.isEmpty(((ProDetailActivity)mView.getActivity()).mProductObject))
            return;
        mPopWindow = new SelectParamentPopWindow(mView.getContext(), ((ProDetailActivity)mView.getActivity()).mProductObject);
        mPopWindow.onShow(rl_rl);
        mPopWindow.setListener(new IParamentChooseListener() {
            @Override
            public void onParamentChanged(String text, Boolean isGoCart, String property, int mount) {
                if (!AppUtils.isEmpty(text)) {
                    mParamentTv.setText("已选 " + text);
                    ((ProDetailActivity)mView.getActivity()).mProperty=property;
                }
                if(isGoCart==true){
                    mView.setShowDialog(true);
                    mView.setShowDialog("正在加入购物车中...");
                    mView.showLoading();
                    mProperty=property;
                    sendGoShoppingCart(mView.productId,property,mount);
                }
            }
        });

    }



    private void sendGoShoppingCart(String product,String property,int mount){

        mNetWork.sendShoppingCart(product, property, mount, this);
    }


    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        switch (requestCode) {
            case NetWorkConst.ADDLIKEDPRODUCT:
                selectCollect();
                break;
            case NetWorkConst.ULIKEDPRODUCT:
                selectCollect();
                break;
            case NetWorkConst.ADDCART:
                MyToast.show(mView.getActivity(), UIUtils.getString(R.string.go_cart_ok));
                sendShoppingCart();
                break;
            case NetWorkConst.GETCART:
                if (ans.getJSONArray(Constance.goods_groups).length() > 0) {
                    IssueApplication.mCartCount = ans.getJSONArray(Constance.goods_groups)
                            .getJSONObject(0).getJSONArray(Constance.goods).length();
                } else {
                    IssueApplication.mCartCount=0;
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
        if (null == mView || mView.getActivity().isFinishing())
            return;
        if (AppUtils.isEmpty(ans)) {
            AppDialog.messageBox(UIUtils.getString(R.string.server_error));
            return;
        }
        AppDialog.messageBox(ans.getString(Constance.error_desc));
    }

    class NetworkImageHolderView implements CBPageAdapter.Holder<String> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            // 你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setPadding(30,30,30,30);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, String data) {
            imageView.setImageResource(R.drawable.bg_default);
            ImageLoader.getInstance().displayImage(data, imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "点击了第" + position + "个", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }
}
