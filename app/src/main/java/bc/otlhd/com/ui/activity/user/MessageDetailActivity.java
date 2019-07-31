package bc.otlhd.com.ui.activity.user;

import android.content.Intent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import bc.otlhd.com.R;
import bc.otlhd.com.cons.Constance;
import bocang.utils.AppUtils;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/3/10 16:20
 * @description :消息内容
 */
public class MessageDetailActivity extends BaseActivity {
    String mUrl;
    WebView mWebView;
    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        if(AppUtils.isEmpty(mUrl)) return;
        mWebView.loadUrl(mUrl);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_sys_message_detail);
        mWebView = (WebView)findViewById(R.id.webview);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mUrl=intent.getStringExtra(Constance.url);
    }

    @Override
    protected void onViewClick(View v) {

    }
}
