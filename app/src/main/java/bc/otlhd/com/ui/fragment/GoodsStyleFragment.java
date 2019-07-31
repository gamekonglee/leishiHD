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

public class GoodsStyleFragment extends BaseFragment implements View.OnClickListener {

    private View rl_xiandaijianyue;
    private View rl_zhongshi;
    private View rl_xinzhongshi;
    private View rl_quanbu;
    private View rl_oushi;
    private View rl_tianyuan;
    private View rl_qingshe;
    private View rl_meishi;

    @Override
    protected int getLayout() {
        return R.layout.frag_goods_style;
    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initViewData() {

    }

    @Override
    protected void initView() {
        rl_xiandaijianyue = getView().findViewById(R.id.rl_xiandaijianyue);
        rl_zhongshi = getView().findViewById(R.id.rl_zhongshi);
        rl_xinzhongshi = getView().findViewById(R.id.rl_xinzhongshi);
        rl_quanbu = getView().findViewById(R.id.rl_quanbu);
        rl_oushi = getView().findViewById(R.id.rl_oushi);
        rl_tianyuan = getView().findViewById(R.id.rl_tianyuan);
        rl_qingshe = getView().findViewById(R.id.rl_qingshe);
        rl_meishi = getView().findViewById(R.id.rl_meishi);

        rl_xiandaijianyue.setOnClickListener(this);
        rl_zhongshi.setOnClickListener(this);
        rl_xinzhongshi.setOnClickListener(this);
        rl_quanbu.setOnClickListener(this);
        rl_oushi.setOnClickListener(this);
        rl_tianyuan.setOnClickListener(this);
        rl_qingshe.setOnClickListener(this);
        rl_meishi.setOnClickListener(this);




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
        intent.putExtra(Constance.attr,"风格");
        switch (v.getId()){
            case R.id.rl_xiandaijianyue:
                intent.putExtra(Constance.attr_value,"现代简约");
                break;
            case R.id.rl_zhongshi:
                intent.putExtra(Constance.attr_value,"中式");
                break;
            case R.id.rl_xinzhongshi:
                intent.putExtra(Constance.attr_value,"新中式");
                break;
            case R.id.rl_quanbu:
                intent.putExtra(Constance.attr_value,"全部");
                break;
            case R.id.rl_oushi:
                intent.putExtra(Constance.attr_value,"欧式");
                break;
            case R.id.rl_tianyuan:
                intent.putExtra(Constance.attr_value,"田园");
                break;
            case R.id.rl_qingshe:
                intent.putExtra(Constance.attr_value,"轻奢");
                break;
            case R.id.rl_meishi:
                intent.putExtra(Constance.attr_value,"美式");
                break;
        }
        startActivity(intent);
    }
}
