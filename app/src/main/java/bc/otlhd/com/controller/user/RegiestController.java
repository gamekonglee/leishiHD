package bc.otlhd.com.controller.user;

import android.content.Intent;
import android.os.Message;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;

import bc.otlhd.com.R;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.controller.BaseController;
import bc.otlhd.com.net.OkHttpUtils;
import bc.otlhd.com.ui.activity.IssueApplication;
import bc.otlhd.com.ui.activity.MainActivity;
import bc.otlhd.com.ui.activity.OpenScreenActivity;
import bc.otlhd.com.ui.activity.user.RegiestActivity;
import bc.otlhd.com.ui.view.ShowDialog;
import bc.otlhd.com.utils.MyShare;
import bc.otlhd.com.utils.SignIdUtil;
import bc.otlhd.com.utils.UIUtils;
import bc.otlhd.com.utils.net.HttpListener;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;
import bocang.utils.CommonUtil;
import bocang.utils.IntentUtil;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/2/8 17:25
 * @description :注册第二步
 */
public class RegiestController extends BaseController implements HttpListener {
    private RegiestActivity mView;
    private EditText edtPhone,distributorEt,addressEv;
    private String mPhone;

    public RegiestController(RegiestActivity v){
        mView=v;
        initView();
        initViewData();
    }

    private void initViewData() {

    }

    private void initView() {
        edtPhone = (EditText) mView.findViewById(R.id.edtPhone);
        distributorEt = (EditText) mView.findViewById(R.id.distributorEt);
        addressEv = (EditText) mView.findViewById(R.id.addressEv);
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    public void sendRegiest() {
        mPhone=edtPhone.getText().toString();
        String distributor=distributorEt.getText().toString();
        String address=addressEv.getText().toString();


        if (AppUtils.isEmpty(mPhone)) {
            AppDialog.messageBox(UIUtils.getString(R.string.isnull_phone));
            return;
        }
        if (AppUtils.isEmpty(distributor)) {
            AppDialog.messageBox(UIUtils.getString(R.string.isnull_distributor_name));
            return;
        }
        if (AppUtils.isEmpty(address)) {
            AppDialog.messageBox(UIUtils.getString(R.string.isnull_distributor_address));
            return;
        }

        // 做个正则验证手机号
        if (!CommonUtil.isMobileNO(mPhone)) {
            AppDialog.messageBox(UIUtils.getString(R.string.mobile_assert));
            return;
        }
        if("-1".equals(mView.currentId)){
            AppDialog.messageBox("请选择运营中心");
            return;
        }

        mView.setShowDialog(true);
        mView.setShowDialog("正在注册中..");
        mView.showLoading();
        mNetWork.sendRegiest(0, distributor, address, mPhone,mView.currentId, IssueApplication.mSignId, Constance.YAOQINGMA, mView, this);

    }
    /**
     * show 激活 dialog
     */
    public void showActivateDialog() {
        ShowDialog mDialog = new ShowDialog();
        mDialog.setNoDismiss(true);
        mDialog.show(mView, "申请提示", "审核中，请耐心等待！\n备注：如需快速审核，请联系该区域办事处报备总部\n(标识码："+ SignIdUtil.getSignId(mView)+")", new ShowDialog.OnBottomClickListener() {
            @Override
            public void positive() {
//                mView.finish();
            }

            @Override
            public void negtive() {
//                mView.finish();
            }
        });
        mHandler.post(runnable);
    }
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
           getUserInfo();
           mHandler.postDelayed(runnable,3000);

        }
    };


    private void getUserInfo(){
        if(mView==null||mView.isFinishing()){
            return;
        }
        mNetWork.sendUser(IssueApplication.mSignId, mView, new HttpListener() {
            @Override
            public void onSuccessListener(int requestCode, com.alibaba.fastjson.JSONObject ans) {
                String result = ans.getString(Constance.result);
                if (result.equals("success")) {
                    mHandler.removeCallbacks(runnable);
                    ((IssueApplication) mView.getApplication()).mUserInfo = ans.getJSONObject(Constance.data);
                    MyShare.get(mView).putString(Constance.data, ans.getJSONObject(Constance.data).toJSONString());
                    goOn();
                }
            }

            @Override
            public void onFailureListener(int requestCode, com.alibaba.fastjson.JSONObject ans) {
            }
        });
    }

    private void goOn() {
        mView.startActivity(new Intent(mView, MainActivity.class));
//                        startActivity(new Intent(ct, OrderHDActivity.class));
        mView.finish();
    }


    @Override
    public void onSuccessListener(int what, JSONObject ans) {
        mView.hideLoading();
        String result = ans.getString(Constance.result);
        if (result.equals("success")) {
            showActivateDialog();
        } else {
            int data = ans.getInteger(Constance.data);
            if (data == 0) {
                MyToast.show(mView, "注册失败");
            } else if (data == 2) {
                MyToast.show(mView,"邀请码错误");
            }
        }
    }

    @Override
    public void onFailureListener(int what, JSONObject ans) {
        mView.hideLoading();
        if(!AppUtils.isEmpty(ans)){
            MyToast.show(mView, ans.toJSONString());
        }
    }
}
