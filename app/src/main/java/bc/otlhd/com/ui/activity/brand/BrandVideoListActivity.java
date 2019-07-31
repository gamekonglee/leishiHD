package bc.otlhd.com.ui.activity.brand;

import android.content.Intent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bc.otlhd.com.R;
import bc.otlhd.com.adapter.BaseAdapterHelper;
import bc.otlhd.com.adapter.QuickAdapter;
import bc.otlhd.com.bean.BrandBean;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.cons.NetWorkConst;
import bc.otlhd.com.net.OkHttpUtils;
import bc.otlhd.com.ui.activity.IssueApplication;
import bc.otlhd.com.utils.FileUtil;
import bc.otlhd.com.utils.LogUtils;
import bc.otlhd.com.utils.MyShare;
import bc.otlhd.com.utils.UIUtils;
import bc.otlhd.com.utils.net.HttpListener;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.view.BaseActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by gamekonglee on 2019/3/1.
 */

public class BrandVideoListActivity extends BaseActivity {

    private String id;
    private QuickAdapter adapter;
    private JSONArray mBrandArray;

    @Override
    protected void InitDataView() {
        id = getIntent().getStringExtra(Constance.id);
    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
    setContentView(R.layout.activity_video_list);
    GridView gv_video= (GridView) findViewById(R.id.gv_video);
        adapter = new QuickAdapter<BrandBean>(this, R.layout.item_video) {
                @Override
                protected void convert(BaseAdapterHelper helper, final BrandBean item) {
                    ImageView iv_img=helper.getView(R.id.iv_img);
                    int width= (UIUtils.getScreenWidth(BrandVideoListActivity.this)-UIUtils.dip2PX(90))/3;
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
                                String result= MyShare.get(BrandVideoListActivity.this).getString(Constance.brandDetail+item.getId());
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
        OkHttpUtils.getbrandList( new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result=response.body().string();
                JSONObject jsonObject=new JSONObject(result);
                if(jsonObject!=null){
                    JSONArray temp = jsonObject.getJSONArray(Constance.data);

//                    LogUtils.logE("brand", temp.toString());
//                for(int i = 0; i< temp.length(); i++){
//                    if(temp.getJSONObject(i).getString(Constance.type).equals("3")){
//                        video_pos = i;
//                        break;
//                    }
//                }
                    final List<BrandBean> brandBeanList=new ArrayList<>();
                    for(int i=0;i<temp.length();i++){
                        if (temp.getJSONObject(i).getString(Constance.type).equals("3")){
                            brandBeanList.add(new Gson().fromJson(temp.getJSONObject(i).toString(),BrandBean.class));
                        }
                    }
                    if(brandBeanList!=null&&brandBeanList.size()>0){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                        adapter.replaceAll(brandBeanList);
                            }
                        });
                    }


                }
            }
        });


    }
    private void getBrandDetail() {
        OkHttpUtils.getBrandDetail(id, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
             String result=response.body().string();
             LogUtils.logE("result",result);
            }
        });
    }
    @Override
    protected void initData() {

    }
    private void parseData(String result,String id) {
        JSONObject jsonObject=new JSONObject(result);
        if(jsonObject!=null){
            JSONArray data=jsonObject.getJSONArray(Constance.data);
            if(data!=null&&data.length()>0){
                countDownTimer.cancel();
                IssueApplication.noAd=true;
                Intent intent=new Intent(BrandVideoListActivity.this,BrandPlayActivity.class);
//                                        intent.putExtra(Constance.url,data.getJSONObject().getString(Constance.filepath));
                intent.putExtra(Constance.id,id);
                startActivity(intent);
            }
        }
    }
    @Override
    protected void onViewClick(View v) {

    }
}
