package bc.otlhd.com.ui.activity;

import android.view.View;
import android.widget.Button;

import bc.otlhd.com.R;
import bc.otlhd.com.controller.UploadImagController;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/3/24 11:37
 * @description :
 */
public class UploadImagAtivity extends BaseActivity {
    private Button bt_click,bt_down_data;
    private UploadImagController mController;
    private boolean isStart;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController=new UploadImagController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_upload_image);
        bt_click=getViewAndClick(R.id.bt_click);
        bt_down_data=getViewAndClick(R.id.bt_down_data);
        IssueApplication.noAd=true;
        isStart = false;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()){
            case R.id.bt_click:
                mController.sendImage();
                isStart=true;
            break;
            case R.id.bt_down_data:
                mController.sendData();
            break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mController.sendImage();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mController.cancle();
    }
    @Override
    protected void onPause() {
        super.onPause();
        IssueApplication.noAd=false;
    }

}
