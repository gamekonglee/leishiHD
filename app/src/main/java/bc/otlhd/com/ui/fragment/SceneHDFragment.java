package bc.otlhd.com.ui.fragment;

import android.os.Bundle;
//
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bc.otlhd.com.R;
import bc.otlhd.com.common.BaseFragment;
import bc.otlhd.com.controller.programme.SceneHDController;
import bc.otlhd.com.utils.UIUtils;


/**
 * @author: Jun
 * @date : 2017/3/30 13:50
 * @description :
 */
public class SceneHDFragment extends BaseFragment {
    private SceneHDController mController;


//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fm_scene_hd, null);
//    }

    @Override
    protected int getLayout() {
        return R.layout.fm_scene_hd;
    }


    @Override
    protected void initController() {
        mController = new SceneHDController(this);
    }

    @Override
    protected void initViewData() {

    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mController.onBackPressed();
    }


}
