package bc.otlhd.com.ui.fragment;

import android.content.Intent;
import android.os.SystemClock;
import android.view.View;

import bc.otlhd.com.R;
import bc.otlhd.com.common.BaseFragment;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.ui.activity.IssueApplication;
import bc.otlhd.com.ui.activity.product.ProListActivity;

/**
 * Created by gamekonglee on 2019/2/15.
 */

public class GoodsTypeFragment extends BaseFragment implements View.OnClickListener {
    @Override
    protected int getLayout() {
        return R.layout.frag_goods_type;
    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initViewData() {

    }

    @Override
    protected void initView() {
    View rl_diaoxiandeng=getView().findViewById(R.id.rl_diaoxiandeng);
    View rl_xidingdeng=getView().findViewById(R.id.rl_xidingdeng);
    View rl_taideng=getView().findViewById(R.id.rl_taideng);
    View rl_jingqiandeng=getView().findViewById(R.id.rl_jingqiandeng);
    View rl_luodideng=getView().findViewById(R.id.rl_luodideng);
    View rl_shangyezhaoming=getView().findViewById(R.id.rl_shangyezhaoming);
    View rl_bideng=getView().findViewById(R.id.rl_bideng);
    View rl_quanbu=getView().findViewById(R.id.rl_quanbu);

        rl_diaoxiandeng.setOnClickListener(this);
        rl_xidingdeng.setOnClickListener(this);
        rl_taideng.setOnClickListener(this);
        rl_jingqiandeng.setOnClickListener(this);
        rl_luodideng.setOnClickListener(this);
        rl_shangyezhaoming.setOnClickListener(this);
        rl_bideng.setOnClickListener(this);
        rl_quanbu.setOnClickListener(this);


    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        if(!IssueApplication.IsRoot()){
            SystemClock.sleep(IssueApplication.SLEEP_TIME);
        }
        Intent intent=new Intent(getActivity(), ProListActivity.class);
        intent.putExtra(Constance.attr,"类型");
        switch (v.getId()){
            case R.id.rl_diaoxiandeng:
                intent.putExtra(Constance.attr_value,"餐吊灯");
                break;
            case R.id.rl_xidingdeng:
                intent.putExtra(Constance.attr_value,"吸顶灯");
                break;
            case R.id.rl_taideng:
                intent.putExtra(Constance.attr_value,"台灯");
                break;
            case R.id.rl_jingqiandeng:
                intent.putExtra(Constance.attr_value,"镜前灯");
                break;
            case R.id.rl_luodideng:
                intent.putExtra(Constance.attr_value,"落地灯");
                break;
            case R.id.rl_shangyezhaoming:
                intent.putExtra(Constance.attr_value,"吊灯");
                break;
            case R.id.rl_bideng:
                intent.putExtra(Constance.attr_value,"壁灯");
                break;
            case R.id.rl_quanbu:
                intent.putExtra(Constance.attr_value,"全部");
                break;
        }
        startActivity(intent);
    }
}
