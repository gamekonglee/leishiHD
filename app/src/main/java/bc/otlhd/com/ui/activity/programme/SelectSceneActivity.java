package bc.otlhd.com.ui.activity.programme;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import bc.otlhd.com.R;
import bc.otlhd.com.controller.programme.SelectSceneController;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/2/18 11:55
 * @description :
 */
public class SelectSceneActivity extends BaseActivity {
    private SelectSceneController mController;
    private TextView tv_album;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new SelectSceneController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_select_scene);
        tv_album = getViewAndClick(R.id.tv_album);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_album:
                mController.goPhoto();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mController.onBackPressed();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mController.ActivityResult(requestCode, resultCode, data);
    }
}
