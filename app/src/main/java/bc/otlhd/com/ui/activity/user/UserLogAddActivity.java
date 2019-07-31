package bc.otlhd.com.ui.activity.user;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import bc.otlhd.com.R;
import bc.otlhd.com.bean.Logistics;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.controller.user.UserLogAddController;
import bc.otlhd.com.ui.view.ShowDialog;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/1/19 17:05
 * @description
 */
public class UserLogAddActivity extends BaseActivity {
    private UserLogAddController mController;
    private Button btn_save;
    private Intent mIntent;
    public Logistics mLogistics;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new UserLogAddController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_user_logistics_add);
        setColor(this, getResources().getColor(R.color.colorPrimary));
        btn_save = getViewAndClick(R.id.btn_save);
    }

    @Override
    protected void initData() {
        mIntent=getIntent();
        mLogistics= (Logistics) mIntent.getSerializableExtra(Constance.logistics);

    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                mController.sendAddLogistic();
                break;
        }
    }

    public void goBack(View v){
        ShowDialog mDialog=new ShowDialog();
        mDialog.show(this, "提示", "你的物流地址还未保存，是否放弃保存?", new ShowDialog.OnBottomClickListener() {
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
