package bc.otlhd.com.ui.activity.brand;

import android.content.Intent;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import bc.otlhd.com.R;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.controller.brand.BrandDetailController;

import bc.otlhd.com.ui.activity.IssueApplication;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/5/8 16:40
 * @description :
 */
public class BrandDetailActivity extends BaseActivity {
    private BrandDetailController mController;
    public JSONObject mData;
    private ImageView iv_close;
    public int mWebType=0;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController=new BrandDetailController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_brand_detail);
        iv_close=getViewAndClick(R.id.iv_close);

    }

    @Override
    protected void initData() {
        Intent intent=getIntent();
        mData= (JSONObject) JSON.parseObject(intent.getStringExtra(Constance.data));
        mWebType=intent.getIntExtra(Constance.style,0);
    }

    @Override
    protected void onViewClick(View v) {
        if(!IssueApplication.IsRoot()){
            SystemClock.sleep(IssueApplication.SLEEP_TIME);
        }

        switch (v.getId()){
            case R.id.iv_close:
                finish();
            break;
        }
    }
}
