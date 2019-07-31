package bc.otlhd.com.ui.activity;

import android.content.Intent;
import android.view.View;

import bc.otlhd.com.R;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.ui.activity.product.ProListActivity;
import bocang.view.BaseActivity;

/**
 * Created by gamekonglee on 2019/5/8.
 */

public class BrandProductActivity extends BaseActivity {

    private Intent intent;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_brand_product);
        getViewAndClick(R.id.rl_lszm);
        getViewAndClick(R.id.rl_nvc);
        getViewAndClick(R.id.rl_bkl);
        getViewAndClick(R.id.rl_lzcb);
        getViewAndClick(R.id.rl_zdf);
        getViewAndClick(R.id.rl_xzqd);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {
        intent = new Intent(this, ProListActivity.class);
        intent.putExtra(Constance.attr,"品牌");

        switch (v.getId()){
            case R.id.rl_lszm:
                intent.putExtra(Constance.attr_value,"雷士照明");
                break;
            case R.id.rl_nvc:
                intent.putExtra(Constance.attr_value,"NVC");
                break;
            case R.id.rl_bkl:
                intent.putExtra(Constance.attr_value,"伯克丽");
                break;
            case R.id.rl_lzcb:
                intent.putExtra(Constance.attr_value,"利兹城堡");
                break;
            case R.id.rl_zdf:
                intent.putExtra(Constance.attr_value,"致東方");
                break;
            case R.id.rl_xzqd:
                intent.putExtra(Constance.attr_value,"乡镇渠道");
                break;

        }
        startActivity(intent);
    }
}
