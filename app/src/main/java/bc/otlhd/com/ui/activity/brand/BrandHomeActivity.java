package bc.otlhd.com.ui.activity.brand;

import android.content.Intent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;

import bc.otlhd.com.R;
import bc.otlhd.com.adapter.BaseAdapterHelper;
import bc.otlhd.com.adapter.QuickAdapter;
import bc.otlhd.com.bean.BrandBean;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.cons.NetWorkConst;
import bc.otlhd.com.controller.brand.BrandHomeController;
import bc.otlhd.com.net.OkHttpUtils;
import bc.otlhd.com.ui.activity.IssueApplication;
import bc.otlhd.com.utils.FileUtil;
import bc.otlhd.com.utils.MyShare;
import bc.otlhd.com.utils.UIUtils;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.view.BaseActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by gamekonglee on 2019/2/15.
 */

public class BrandHomeActivity extends BaseActivity {

    public GridView gv_brand_bottom;
    private BrandHomeController brandHomeController;
    public WebView webview;
    public GridView gv_video;
    public QuickAdapter adapter;
    public boolean is_video;
    public View bg_brand;
    public View ll_video;
    public ConvenientBanner banner;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        brandHomeController = new BrandHomeController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_brand_home);
        gv_brand_bottom = (GridView) findViewById(R.id.gv_brand_bottom);
        gv_video = (GridView) findViewById(R.id.gv_video);
        webview = (WebView) findViewById(R.id.webview);
        bg_brand = findViewById(R.id.rl_brand);
        ll_video = findViewById(R.id.ll_video);
        banner = (ConvenientBanner) findViewById(R.id.banner);
        banner.setVisibility(View.GONE);
        webview.setVisibility(View.VISIBLE);
        gv_video.setVisibility(View.GONE);
        adapter = new QuickAdapter<BrandBean>(this, R.layout.item_video) {
            @Override
            protected void convert(BaseAdapterHelper helper, final BrandBean item) {
                ImageView iv_img=helper.getView(R.id.iv_img);
                int width= (UIUtils.getScreenWidth(BrandHomeActivity.this)-UIUtils.dip2PX(90))/3;
                iv_img.setLayoutParams(new RelativeLayout.LayoutParams(width,width*450/800));
                if(!OkHttpUtils.hashkNewwork()){
                    ImageLoader.getInstance().displayImage("file://"+ FileUtil.getBrandExternDir(item.getPath()),iv_img);
                }else {
                    ImageLoader.getInstance().displayImage(NetWorkConst.API_HOST+"/App/leishi/Public/uploads/"+item.getPath(),iv_img);
                }

                iv_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!OkHttpUtils.hashkNewwork()){
                            String result= MyShare.get(BrandHomeActivity.this).getString(Constance.brandDetail+item.getId());
                            if (result==null)return;
                            parseData(result,item.getId());
                            return;
                        }
                        OkHttpUtils.getBrandDetail(item.getId(), new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String result=response.body().string();
                                parseData(result,item.getId());
                            }
                        });
                    }
                });
            }
        };
        gv_video.setAdapter(adapter);
        webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setDefaultTextEncodingName("utf-8");


    }

    private void parseData(String result,String id) {
        JSONObject jsonObject=new JSONObject(result);
        if(jsonObject!=null){
            JSONArray data=jsonObject.getJSONArray(Constance.data);
            if(data!=null&&data.length()>0){
                countDownTimer.cancel();
                IssueApplication.noAd=true;
                Intent intent=new Intent(BrandHomeActivity.this,BrandPlayActivity.class);
//                                        intent.putExtra(Constance.url,data.getJSONObject().getString(Constance.filepath));
                intent.putExtra(Constance.id,id);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!IssueApplication.noAd){
            if(countDownTimer!=null)countDownTimer.start();
        }
    }

    @Override
    protected void initData() {
        is_video = getIntent().getBooleanExtra(Constance.is_video,false);
    }

    @Override
    protected void onViewClick(View v) {

    }
}
