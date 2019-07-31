package bc.otlhd.com.ui.activity;

import android.view.View;

import bc.otlhd.com.R;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.ui.view.SwitchView;
import bc.otlhd.com.utils.MyShare;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/7/3 11:35
 * @description :
 */
public class TianMaoActivity extends BaseActivity implements SwitchView.OnStateChangedListener {
    private SwitchView show_tianmao_sv;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_tianmao);
        show_tianmao_sv = (SwitchView) findViewById(R.id.show_tianmao_sv);
        show_tianmao_sv.setOnStateChangedListener(this);
        boolean isShowTianMao = MyShare.get(this).getBoolean(Constance.SHOWTIANMAO);
        show_tianmao_sv.setState(isShowTianMao);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {

    }

    @Override
    public void toggleToOn() {
        MyShare.get(this).putBoolean(Constance.SHOWTIANMAO, true);
        show_tianmao_sv.setState(true);
    }

    @Override
    public void toggleToOff() {
        MyShare.get(this).putBoolean(Constance.SHOWTIANMAO, false);
        show_tianmao_sv.setState(false);
    }
}
