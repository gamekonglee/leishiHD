package bc.otlhd.com.ui.activity.buy;

import android.content.Intent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import bc.otlhd.com.R;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.controller.buy.ConfirmOrderController;
import bc.otlhd.com.ui.view.ShowDialog;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/2/24 16:51
 * @description :
 */
public class ConfirmOrderActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    private ConfirmOrderController mController;
    public JSONArray goodsList;
    public RelativeLayout address_rl;
    private TextView money_tv, settle_tv;
    private RadioGroup group;
    private RelativeLayout logistic_type_rl;
    public   JSONObject mAddressObject;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new ConfirmOrderController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_confirm_order);
        address_rl = getViewAndClick(R.id.address_rl);
        settle_tv = getViewAndClick(R.id.settle_tv);
        money_tv = (TextView) findViewById(R.id.summoney_tv);
        money_tv.setText("￥" + (int)money + "");
        group = (RadioGroup)this.findViewById(R.id.radioGroup);
        group.setOnCheckedChangeListener(this);
        logistic_type_rl = (RelativeLayout)findViewById(R.id.logistic_type_rl);
        logistic_type_rl = getViewAndClick(R.id.logistic_type_rl);

    }

    float money = 0;

    @Override
    protected void initData() {
        Intent intent = getIntent();
        goodsList = (JSONArray) intent.getSerializableExtra(Constance.goods);
        mAddressObject = (JSONObject) intent.getSerializableExtra(Constance.address);
        money = intent.getFloatExtra(Constance.money, 0);

    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.address_rl:
                mController.selectAddress();
                break;
            case R.id.settle_tv:
                mController.settleOrder();
                break;
            case R.id.logistic_type_rl:
                mController.selectLogistic();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mController.ActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        mController.selectCheckType(group.getCheckedRadioButtonId());
    }

    public void goBack(View v){
        ShowDialog mDialog=new ShowDialog();
        mDialog.show(this, "提示", "是否退出当前的确定订单?", new ShowDialog.OnBottomClickListener() {
            @Override
            public void positive() {
                finish();
            }

            @Override
            public void negtive() {

            }
        });
    }
}
