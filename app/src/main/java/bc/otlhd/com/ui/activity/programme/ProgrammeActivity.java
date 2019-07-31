package bc.otlhd.com.ui.activity.programme;

import android.view.View;

import bc.otlhd.com.R;
import bc.otlhd.com.ui.fragment.ProgrammeFragment;
import bocang.view.BaseActivity;

/**
 * Created by gamekonglee on 2019/2/21.
 */

public class ProgrammeActivity extends BaseActivity {
    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
    setContentView(R.layout.activity_programme);
    ProgrammeFragment programmeFragment=new ProgrammeFragment();
    getSupportFragmentManager().beginTransaction().add(R.id.fl_content,programmeFragment).commit();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {

    }
}
