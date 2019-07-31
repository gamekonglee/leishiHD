package bc.otlhd.com.ui.activity.programme;

import android.view.View;

import bc.otlhd.com.R;
import bc.otlhd.com.ui.fragment.SceneHDFragment;
import bocang.view.BaseActivity;

/**
 * Created by gamekonglee on 2019/2/19.
 */

public class SceneHDActivity extends BaseActivity {
    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_scene_home);
        SceneHDFragment sceneHDFragment=new SceneHDFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_content,sceneHDFragment).commit();

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {

    }
}
