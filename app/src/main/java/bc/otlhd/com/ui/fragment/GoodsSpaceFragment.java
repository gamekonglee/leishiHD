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

public class GoodsSpaceFragment extends BaseFragment implements View.OnClickListener {

    private View rl_keting;
    private View rl_canting;
    private View rl_woshi;
    private View rl_shufang;
    private View rl_ertongfang;
    private View rl_chuweijian;
    private View rl_yangtai;
    private View rl_quanbu;

    @Override
    protected int getLayout() {
        return R.layout.frag_goods_space;
    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initViewData() {

    }

    @Override
    protected void initView() {
        rl_keting = getView().findViewById(R.id.rl_keting);
        rl_canting = getView().findViewById(R.id.rl_canting);
        rl_woshi = getView().findViewById(R.id.rl_woshi);
        rl_shufang = getView().findViewById(R.id.rl_shufang);
        rl_ertongfang = getView().findViewById(R.id.rl_ertongfang);
        rl_chuweijian = getView().findViewById(R.id.rl_chuweijian);
        rl_yangtai = getView().findViewById(R.id.rl_yangtai);
        rl_quanbu = getView().findViewById(R.id.rl_quanbu);

        rl_keting.setOnClickListener(this);
        rl_canting.setOnClickListener(this);
        rl_woshi.setOnClickListener(this);
        rl_shufang.setOnClickListener(this);
        rl_ertongfang.setOnClickListener(this);
        rl_chuweijian.setOnClickListener(this);
        rl_yangtai.setOnClickListener(this);
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
        intent.putExtra(Constance.attr,"空间");
        switch (v.getId()){
            case R.id.rl_keting:
                intent.putExtra(Constance.attr_value,"客厅");
                break;
            case R.id.rl_canting:
                intent.putExtra(Constance.attr_value,"餐厅");
                break;
            case R.id.rl_woshi:
                intent.putExtra(Constance.attr_value,"卧室");
                break;
            case R.id.rl_shufang:
                intent.putExtra(Constance.attr_value,"书房");
                break;
            case R.id.rl_ertongfang:
                intent.putExtra(Constance.attr_value,"复式/别墅");
                break;
            case R.id.rl_chuweijian:
                intent.putExtra(Constance.attr_value,"玄关/过道");
                break;
            case R.id.rl_yangtai:
                intent.putExtra(Constance.attr_value,"阳台");
                break;
            case R.id.rl_quanbu:
                intent.putExtra(Constance.attr_value,"全部");
                break;

        }
        startActivity(intent);
    }
}
