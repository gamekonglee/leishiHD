package bc.otlhd.com.ui.activity.user;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import bc.otlhd.com.R;
import bc.otlhd.com.controller.user.LoginController;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/2/7 13:42
 * @description :登录
 */
public class LoginActivity extends BaseActivity {
    private LoginController mController;
    private TextView typeTv,type02Tv;
    private Button login_bt;
    private TextView regiest_tv,find_pwd_tv;
    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController=new LoginController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_login);
        //沉浸式状态栏
        setColor(this, getResources().getColor(R.color.colorPrimary));
        type02Tv=getViewAndClick(R.id.type02Tv);
        typeTv=getViewAndClick(R.id.typeTv);
        login_bt=getViewAndClick(R.id.login_bt);
        regiest_tv=getViewAndClick(R.id.regiest_tv);
        find_pwd_tv=getViewAndClick(R.id.find_pwd_tv);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()){
            case R.id.typeTv:
                mController.selectType(R.id.typeTv);
                break;
            case R.id.type02Tv:
                mController.selectType(R.id.type02Tv);
                break;
            case R.id.login_bt:
                mController.sendLogin();
//                mController.showShare("http://app.bocang.cc/Ewm/index/url/aikeshidun.bocang.cc","奥克特商城");
                break;
            case R.id.regiest_tv:
                mController.sendRegiest();
                break;
            case R.id.find_pwd_tv:
                mController.sendFindPwd();
                break;
        }
    }
}
