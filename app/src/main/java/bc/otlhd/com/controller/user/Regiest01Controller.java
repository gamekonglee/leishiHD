package bc.otlhd.com.controller.user;

import android.content.Intent;
import android.os.Message;
import android.widget.EditText;

import bc.otlhd.com.R;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.controller.BaseController;
import bc.otlhd.com.ui.activity.user.Regiest01Activity;
import bc.otlhd.com.ui.activity.user.RegiestActivity;
import bc.otlhd.com.utils.UIUtils;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;

/**
 * @author: Jun
 * @date : 2017/2/7 16:06
 * @description :注册第一步
 */
public class Regiest01Controller extends BaseController {
    private Regiest01Activity mView;
    private EditText invitation_code;

    public Regiest01Controller(Regiest01Activity v) {
        mView = v;
        initView();
    }


    private void initView() {
        invitation_code = (EditText) mView.findViewById(R.id.invitation_code);
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    /**
     * 验证邀请码
     */
    public void sendInvitationCode() {
        String code = invitation_code.getText().toString();
        if (AppUtils.isEmpty(code)) {
            AppDialog.messageBox(UIUtils.getString(R.string.isnull_invitation_code));
            return;
        }

        Intent intent=new Intent(mView, RegiestActivity.class);
        intent.putExtra(Constance.yaoqing,code);
        mView.startActivity(intent);
        mView.finish();
    }
}
