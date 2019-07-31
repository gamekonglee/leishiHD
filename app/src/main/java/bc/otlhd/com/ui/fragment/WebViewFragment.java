package bc.otlhd.com.ui.fragment;

import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import bc.otlhd.com.R;
import bc.otlhd.com.common.BaseFragment;
import bc.otlhd.com.cons.Constance;

/**
 * Created by gamekonglee on 2019/3/18.
 */

public class WebViewFragment extends BaseFragment {

    private WebView webview;
    private String url;

    @Override
    protected int getLayout() {
        return R.layout.frag_webview;
    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initViewData() {

    }

    @Override
    protected void initView() {
        webview = (WebView) getView().findViewById(R.id.webview);
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
        if(getArguments()!=null){
        url = getArguments().getString(Constance.url);
        }

    }
}
