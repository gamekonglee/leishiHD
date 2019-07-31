package bc.otlhd.com.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.List;

import bc.otlhd.com.R;
import bc.otlhd.com.bean.CaseListBean;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.cons.NetWorkConst;
import bc.otlhd.com.net.OkHttpUtils;
import bc.otlhd.com.utils.FileUtil;
import bocang.view.BaseActivity;

/**
 * Created by gamekonglee on 2019/2/27.
 */

public class CaseDetailActivity extends BaseActivity {

    private List<CaseListBean.Data> data;
    private ImageView iv_left;
    private ImageView iv_right;
    private int currnetP;
    private ImageView iv_img;
    private TextView tv_name;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_case_detail);
        iv_img = (ImageView) findViewById(R.id.iv_img);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        tv_name = (TextView) findViewById(R.id.tv_name);
        iv_left.setOnClickListener(this);
        iv_right.setOnClickListener(this);
        if(data==null||data.size()==0||data.get(currnetP)==null||data.get(currnetP).getName()==null)return;
        if(!OkHttpUtils.hashkNewwork()){
            ImageLoader.getInstance().displayImage("file://"+ FileUtil.getBrandExternDir(data.get(currnetP).getFilepath()), iv_img);
        }else {
            ImageLoader.getInstance().displayImage(NetWorkConst.API_HOST+"/App/"+NetWorkConst.AppName+"/Public/uploads/"+data.get(currnetP).getFilepath(), iv_img);
        }
        tv_name.setText(data.get(currnetP).getName());
    }

    @Override
    protected void initData() {
        String urlList = getIntent().getStringExtra(Constance.urlList);
        currnetP=getIntent().getIntExtra(Constance.currentP,0);
        data = new Gson().fromJson(urlList,new TypeToken<List<CaseListBean.Data>>(){}.getType());

    }

    @Override
    protected void onViewClick(View v) {
        if(data==null||data.size()==0||data.get(currnetP)==null||data.get(currnetP).getName()==null)return;
    switch (v.getId()){
        case R.id.iv_left:
            if(currnetP==0){
                return;
            }
            currnetP--;

            break;
        case R.id.iv_right:
            if(currnetP==data.size()-1){
                return;
            }
            currnetP++;
            break;

    }
        if(!OkHttpUtils.hashkNewwork()){
            ImageLoader.getInstance().displayImage("file://"+ FileUtil.getBrandExternDir(data.get(currnetP).getFilepath()),iv_img);
        }else {
            ImageLoader.getInstance().displayImage(NetWorkConst.API_HOST+"/App/"+NetWorkConst.AppName+"/Public/uploads/"+data.get(currnetP).getFilepath(),iv_img);
        }
        tv_name.setText(data.get(currnetP).getName());
    }
}
