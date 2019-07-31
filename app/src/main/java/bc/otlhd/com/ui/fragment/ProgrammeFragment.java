package bc.otlhd.com.ui.fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import bc.otlhd.com.R;
import bc.otlhd.com.bean.Programme;
import bc.otlhd.com.common.BaseFragment;
import bc.otlhd.com.controller.programme.ProgrammeController;

/**
 * @author Jun
 * @time 2017/1/5  12:00
 * @desc 配灯方案页面
 */
public class ProgrammeFragment extends BaseFragment implements View.OnClickListener {
    private ProgrammeController mController;
//    private RelativeLayout add_rl;
    public List<Programme> mProgrammes;

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fm_programme, null);
//    }

    @Override
    protected int getLayout() {
        return R.layout.fm_programme;
    }

    @Override
    protected void initController() {
        mController = new ProgrammeController(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mController.sendFangAnList();
    }

    @Override
    protected void initViewData() {

    }

    @Override
    protected void initView() {
//        add_rl = (RelativeLayout) getActivity().findViewById(R.id.add_rl);
//        add_rl.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mController.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.add_rl://新增
//                IntentUtil.startActivity(this.getActivity(), DiyActivity.class,false);
//                break;
        }
    }
}
