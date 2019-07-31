package bc.otlhd.com.ui.activity;

import android.view.View;

import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import bc.otlhd.com.R;
import bc.otlhd.com.cons.Constance;
import bocang.view.BaseActivity;

/**
 * Created by gamekonglee on 2019/3/13.
 */

public class  WebViewActivity extends BaseActivity{

    private WebView webview;
    private String url;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_webview);
        webview = (com.tencent.smtt.sdk.WebView) findViewById(R.id.webview);
        webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webview.loadUrl(url);
                return true;
            }
        });
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setAllowFileAccess(true);
        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setPluginState(WebSettings.PluginState.ON);
        webview.getSettings().setAllowContentAccess(true);
        webview.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setDomStorageEnabled(true);
        //支持插件
//        webview.getSettings().setPluginsEnabled(true);
        webview.getSettings().setDefaultTextEncodingName("utf-8");
        webview.loadUrl(url);
    }

    @Override
    protected void initData() {
        url = getIntent().getStringExtra(Constance.url);
    }

    @Override
    protected void onViewClick(View v) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webview.destroy();
    }
}
