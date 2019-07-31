package bc.otlhd.com.ui.activity.programme;

import android.view.View;
import android.widget.RelativeLayout;

import bc.otlhd.com.R;
import bc.otlhd.com.controller.programme.SelectSchemeController;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/3/14 11:01
 * @description :选择方案类型
 */
public class SelectSchemeActivity extends BaseActivity {
    private SelectSchemeController mController;
    private RelativeLayout save_rl;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController=new SelectSchemeController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_scheme_type);
        save_rl=getViewAndClick(R.id.save_rl);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()){
            case R.id.save_rl:
            mController.saveScheme();
            break;
        }
    }
}
