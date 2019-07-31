package bc.otlhd.com.ui.activity.user;

import android.view.View;
import android.widget.Button;

import bc.otlhd.com.R;
import bc.otlhd.com.controller.user.Regiest01Controller;
import bc.otlhd.com.ui.view.ShowDialog;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/2/7 16:03
 * @description :
 */
public class Regiest01Activity extends BaseActivity {
     private Regiest01Controller mController;
    private Button find_pwd_btnConfirm;

    @Override
    protected void InitDataView() {
    }

    @Override
    protected void initController() {
        mController=new Regiest01Controller(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_regiest01);
        //沉浸式状态栏
        setColor(this, getResources().getColor(R.color.colorPrimary));
        find_pwd_btnConfirm=getViewAndClick(R.id.find_pwd_btnConfirm);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()){
            case R.id.find_pwd_btnConfirm:
                mController.sendInvitationCode();
            break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void goBack(View v){
        ShowDialog mDialog=new ShowDialog();
        mDialog.show(this, "提示", "你是否放弃当前注册?", new ShowDialog.OnBottomClickListener() {
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
