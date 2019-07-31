package bc.otlhd.com.ui.activity.user;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import bc.otlhd.com.R;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.controller.user.OrderDetailController;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/3/20 15:30
 * @description :
 */
public class OrderDetailActivity extends BaseActivity {
    private OrderDetailController mController;
    public JSONObject mOrderObject;
    private Intent mIntent;
    private TextView do_tv, do02_tv, do03_tv;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new OrderDetailController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_order_detail);
        do_tv = getViewAndClick(R.id.do_tv);
        do02_tv = getViewAndClick(R.id.do02_tv);
        do03_tv = getViewAndClick(R.id.do03_tv);
    }

    @Override
    protected void initData() {
        mIntent = getIntent();
        String jsonString =mIntent.getStringExtra(Constance.order);
        mOrderObject=JSON.parseObject(jsonString);
    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.do_tv:
                mController.doOrder();
                break;
            case R.id.do02_tv:
                mController.do02Order();
                break;
            case R.id.do03_tv:
                mController.do03Order();
                break;

        }
    }
}
