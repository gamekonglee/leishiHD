package bc.otlhd.com.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import bc.otlhd.com.R;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.controller.SettingController;
import bc.otlhd.com.ui.activity.user.LoginActivity;
import bc.otlhd.com.ui.view.ShowDialog;
import bc.otlhd.com.utils.MyShare;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/2/6 9:43
 * @description :
 */
public class SettingActivity extends BaseActivity {
    private SettingController mController;
    private RelativeLayout cache_rl;
    private Button outlogin_bt;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new SettingController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_setting);
        //沉浸式状态栏
        setColor(this, getResources().getColor(R.color.colorPrimary));
        outlogin_bt = getViewAndClick(R.id.outlogin_bt);
        cache_rl = getViewAndClick(R.id.cache_rl);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.outlogin_bt:
                ShowDialog mDialog=new ShowDialog();
                mDialog.show(this, "提示", "确定退出登录?", new ShowDialog.OnBottomClickListener() {
                    @Override
                    public void positive() {
                        MyShare.get(SettingActivity.this).putString(Constance.TOKEN, "");
                        Intent logoutIntent = new Intent(SettingActivity.this, LoginActivity.class);
                        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(logoutIntent);
                    }

                    @Override
                    public void negtive() {

                    }
                });
                break;
            case R.id.cache_rl:
                mController.clearCache();
                break;
        }
    }
}
