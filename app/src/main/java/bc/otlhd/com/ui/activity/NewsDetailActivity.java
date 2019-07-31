package bc.otlhd.com.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.smtt.sdk.WebView;

import java.io.IOException;
import java.util.List;

import bc.otlhd.com.R;
import bc.otlhd.com.bean.NewsDetail;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.cons.NetWorkConst;
import bc.otlhd.com.net.OkHttpUtils;
import bc.otlhd.com.utils.LogUtils;
import bocang.view.BaseActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by gamekonglee on 2019/3/14.
 */

public class NewsDetailActivity extends BaseActivity{

    private TextView tv_title;
    private WebView webview_content;
    private ImageView iv_img;
    private String title;
    private String img;
    private String content;
    private String id;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_news_detail);
        tv_title = (TextView) findViewById(R.id.tv_title);
        webview_content = (WebView) findViewById(R.id.webview_content);
        iv_img = (ImageView) findViewById(R.id.iv_img);
        tv_title.setText(""+title);
        ImageLoader.getInstance().displayImage(NetWorkConst.API_HOST+"/App/leishi/Public/uploads/"+img,iv_img);
//        LogUtils.logE("content",content);
        if(!OkHttpUtils.hashkNewwork()){
            content=content.replace("<img src=\"","<img src=\"http://nvc.bocang.cc/");
            content=content.replace("<img alt=\"\" src=\"","<img src=\"http://nvc.bocang.cc/");
            content = "<meta name=\"viewport\" content=\"width=device-width\">" + content;
            webview_content.loadData(content, "text/html; charset=UTF-8", null);//这种写法可以正确解析中文
            return;
        }
        OkHttpUtils.getNewsDetail(id, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result=response.body().string();
                LogUtils.logE("result",result);
                final List<NewsDetail> newsDetails=new Gson().fromJson(result,new TypeToken<List<NewsDetail>>(){}.getType());
                if(newsDetails!=null&&newsDetails.size()>0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String content=newsDetails.get(0).getContent();
                            content=content.replace("<img src=\"","<img src=\"http://nvc.bocang.cc/");
                            content=content.replace("<img alt=\"\" src=\"","<img src=\"http://nvc.bocang.cc/");
                            content = "<meta name=\"viewport\" content=\"width=device-width\">" + content;
                            webview_content.loadData(content, "text/html; charset=UTF-8", null);//这种写法可以正确解析中文
                        }
                    });
                }

            }
        });
//        webview_content.loadData(content, "text/html; charset=UTF-8", null);//这种写法可以正确解析中文
    }

    @Override
    protected void initData() {
        title = getIntent().getStringExtra(Constance.title);
        img = getIntent().getStringExtra(Constance.img);
        content = getIntent().getStringExtra(Constance.content);
        id = getIntent().getStringExtra(Constance.id);
    }

    @Override
    protected void onViewClick(View v) {

    }
}
