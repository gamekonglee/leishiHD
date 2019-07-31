package bc.otlhd.com.controller.brand;

import android.net.http.SslError;
import android.os.Message;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONObject;

import bc.otlhd.com.R;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.cons.NetWorkConst;
import bc.otlhd.com.controller.BaseController;
import bc.otlhd.com.ui.activity.brand.BrandDetailActivity;
import bc.otlhd.com.utils.net.HttpListener;
import bocang.utils.AppUtils;

/**
 * @author: Jun
 * @date : 2017/5/8 16:41
 * @description :
 */
public class BrandDetailController extends BaseController {
    private BrandDetailActivity mView;
    //    private TextView tv_title;
    private WebView webView;
    private WebView webView02;
    private RelativeLayout web_rl;

    public BrandDetailController(BrandDetailActivity v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {
        if (AppUtils.isEmpty(mView.mData))
            return;
        String id = mView.mData.getString(Constance.id);
        String name = mView.mData.getString(Constance.name);
        sendBrandDetail(id);

    }


    private void sendBrandDetail(String id) {
        mNetWork.sendBrandDetail(id, mView, new HttpListener() {
            @Override
            public void onSuccessListener(int what, JSONObject ans) {
                if (mView.mWebType == 0) {
                    String desc = ans.getString("content");
                    if (!AppUtils.isEmpty(desc)) {
                        getWebView(desc);
                    }
                } else {
                    String path = ans.getJSONArray(Constance.data).getJSONObject(0).getString("filepath");
                    webView02.loadUrl(path);
                    //                    webView02.loadUrl("http://720yun.com/t/aoojand534h7nm8yuj?pano_id=wTutEYpKRPG3IkXH");
                }

            }

            @Override
            public void onFailureListener(int what, JSONObject ans) {

            }
        });
    }


    private void initView() {
        //        tv_title = (TextView) mView.findViewById(R.id.tv_title);
        web_rl = (RelativeLayout) mView.findViewById(R.id.web_rl);
        if (mView.mWebType == 0) {
            initWebView();
        } else {
            initWebView02();
        }
    }

    private void getWebView(String htmlValue) {
        String html = htmlValue;
        html = html.replace("<p><img src=\"", "<img src=\"" + NetWorkConst.UR_HOST);
        html = html.replace("</p>", "");
        html = html.replace("font-size: 12px", "font-size: 20px");
        html = "<meta name=\"viewport\" content=\"width=device-width\">" + html;
        webView.loadData(html, "text/html; charset=UTF-8", null);//这种写法可以正确解析中文
    }

    private void initWebView() {

        webView = (WebView) mView.findViewById(R.id.webView);
        web_rl.setVisibility(View.VISIBLE);
        webView.setVisibility(View.VISIBLE);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
    }

    private void initWebView02() {

        webView02 = (WebView) mView.findViewById(R.id.webView02);
        webView02.setVisibility(View.VISIBLE);
        //        webView02.setWebChromeClient(new WebChromeClient());
        //        webView02.setWebViewClient(new WebViewClient());
        //        webView02.getSettings().setJavaScriptEnabled(true);
        //        webView02.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        //        webView02.getSettings().setSupportZoom(true);
        //        webView02.getSettings().setUseWideViewPort(true);
        //        webView02.getSettings().setLoadWithOverviewMode(true);
        //        webView02.getSettings().setDefaultTextEncodingName("utf-8");
        this.webView02.getSettings().setSupportZoom(false);
        //      this.webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.webView02.getSettings().setJavaScriptEnabled(true);
        this.webView02.getSettings().setDomStorageEnabled(true);
        //      this.webView.loadUrl("http://weibo.cn/");

        webView02.setWebViewClient(new WebViewClient() {
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //handler.cancel(); 默认的处理方式，WebView变成空白页
                //                        //接受证书
                handler.proceed();
                //handleMessage(Message msg); 其他处理
            }
        });
    }


    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }
}
