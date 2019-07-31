package bc.otlhd.com.ui.activity.brand;

import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import bc.otlhd.com.R;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.controller.brand.ShowRoomWebController;
import bocang.view.BaseActivity;


/**
 * @author: Jun
 * @date : 2017/5/9 17:13
 * @description :
 */
public class ShowRoomWebActivity extends BaseActivity {
    private ShowRoomWebController mController;
    private ImageView iv_close;
    private WebView webView;
    public JSONObject mData;
    public String path;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new ShowRoomWebController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_test_web);
        webView = (WebView) findViewById(R.id.web);
        iv_close = (ImageView) findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initWebView("");
    }

    public static String getImagePath(Context paramContext, String ss) {
        return paramContext.getFilesDir() + "/db" + ss;
    }


    public void getWebView02(String path) {
        setShowDialog(true);
        setShowDialog("正在搜索中!");
        showLoading();

        //WebView加载web资源
        webView.loadUrl(path);
        this.webView.getSettings().setSupportZoom(false);
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= 16) {
            this.webView.getSettings().setAllowFileAccessFromFileURLs(true);
            this.webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        }
        this.webView.getSettings().setDomStorageEnabled(true);
        this.webView.getSettings().setAppCacheMaxSize(1024 * 1024 * 10);//设置缓冲大小，10M
        String appCacheDir = this.getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        this.webView.getSettings().setAppCachePath(appCacheDir);
        this.webView.getSettings().setAppCacheEnabled(true);
        this.webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        webView.setInitialScale(200);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        this.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stubi
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }

    public void initWebView(String path) {
        setShowDialog(true);
        setShowDialog("正在加载中,请等待!");
        showLoading();
        webView = (WebView) findViewById(R.id.web);
        //        webView.loadUrl("http://720yun.com/t/aoojand534h7nm8yuj?pano_id=wTutEYpKRPG3IkXH");
        webView.loadUrl(path);
        this.webView.getSettings().setSupportZoom(false);
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= 16) {
            this.webView.getSettings().setAllowFileAccessFromFileURLs(true);
            this.webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        }
        WebSettings webSettings = webView.getSettings();
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);

        this.webView.getSettings().setUserAgentString("PC");
        this.webView.getSettings().setDefaultTextEncodingName("GBK");//设置编码格式
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                if (newProgress == 100) {
                    // 网页加载完成
                   hideLoading();
                } else {
                    // 加载中

                }

            }
        });

        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //接受证书
                handler.proceed();
            }
        });
    }


    @Override
    protected void initData() {
        Intent intent = getIntent();
        mData = (JSONObject) JSON.parseObject(intent.getStringExtra(Constance.data));
        path=intent.getStringExtra(Constance.path);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.webView.removeAllViews();
        this.webView.destroy();
    }

    @Override
    protected void onViewClick(View v) {

    }
}
