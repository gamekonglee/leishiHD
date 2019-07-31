package bc.otlhd.com.ui.activity.user;

import android.view.View;
import android.widget.Button;

import bc.otlhd.com.R;
import bc.otlhd.com.controller.user.UpdatePasswordController;
import bc.otlhd.com.ui.view.ShowDialog;
import bc.otlhd.com.ui.view.TimerButton;
import bocang.view.BaseActivity;
import cn.smssdk.SMSSDK;

/**
 * @author Jun
 * @time 2017/1/23  19:51
 * @desc
 */
public class UpdatePasswordActivity extends BaseActivity implements View.OnClickListener {
    public TimerButton find_pwd_btnGetCode;
    private Button find_pwd_btnConfirm;
    private UpdatePasswordController mController;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new UpdatePasswordController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_update_password);

        setColor(this, getResources().getColor(R.color.colorPrimary));
        find_pwd_btnGetCode = (TimerButton) findViewById(R.id.find_pwd_btnGetCode);
        find_pwd_btnConfirm = (Button) findViewById(R.id.find_pwd_btnConfirm);
        find_pwd_btnGetCode.setOnClickListener(this);
        find_pwd_btnConfirm.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.find_pwd_btnGetCode:
                mController.requestYZM();
                break;
            case R.id.find_pwd_btnConfirm:
                mController.sendUpdatePwd();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }

    @Override
    protected void onViewClick(View v) {

    }

    public void goBack(View v){
        ShowDialog mDialog=new ShowDialog();
        mDialog.show(this, "提示", "你是否放弃修改密码?", new ShowDialog.OnBottomClickListener() {
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
