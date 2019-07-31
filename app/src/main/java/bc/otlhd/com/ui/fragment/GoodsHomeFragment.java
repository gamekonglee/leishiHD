package bc.otlhd.com.ui.fragment;

import android.content.Intent;
import android.os.SystemClock;
import android.view.View;

import bc.otlhd.com.R;
import bc.otlhd.com.common.BaseFragment;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.ui.activity.ArticleActivity;
import bc.otlhd.com.ui.activity.BrandProductActivity;
import bc.otlhd.com.ui.activity.CaseActivity;
import bc.otlhd.com.ui.activity.IssueApplication;
import bc.otlhd.com.ui.activity.MainActivity;
import bc.otlhd.com.ui.activity.OthersHomeActivity;
import bc.otlhd.com.ui.activity.WebViewActivity;
import bc.otlhd.com.ui.activity.brand.BrandHomeActivity;
import bc.otlhd.com.ui.activity.buy.ShoppingCartActivity;
import bc.otlhd.com.ui.activity.product.ProListActivity;
import bc.otlhd.com.ui.activity.programme.DiyActivity;
import bc.otlhd.com.ui.activity.programme.SceneHDActivity;
import bocang.utils.IntentUtil;
import bocang.utils.MyToast;
import bocang.view.BaseActivity;

/**
 * Created by gamekonglee on 2019/2/14.
 */

public class GoodsHomeFragment extends BaseFragment implements View.OnClickListener {

    private Intent intent;

    @Override
    protected int getLayout() {
        return R.layout.frag_goods_home;
    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initViewData() {

    }

    @Override
    protected void initView() {
        View rl_diy=getView().findViewById(R.id.rl_diy);
        View rl_video=getView().findViewById(R.id.rl_video);
        View rl_360=getView().findViewById(R.id.rl_360);
        View rl_case=getView().findViewById(R.id.rl_case);
        View rl_product=getView().findViewById(R.id.rl_product);
        View rl_scene=getView().findViewById(R.id.rl_scene);
        View rl_cart=getView().findViewById(R.id.rl_cart);
        View rl_rm=getView().findViewById(R.id.rl_rm);
        View rl_style=getView().findViewById(R.id.rl_style);
        View rl_type=getView().findViewById(R.id.rl_type);
        View rl_space=getView().findViewById(R.id.rl_space);
        View rl_brand_product=getView().findViewById(R.id.rl_brand_product);
        getView().findViewById(R.id.rl_others).setOnClickListener(this);
        rl_style.setOnClickListener(this);
        rl_type.setOnClickListener(this);
        rl_space.setOnClickListener(this);
        rl_rm.setOnClickListener(this);
        rl_diy.setOnClickListener(this);
        rl_video.setOnClickListener(this);
        rl_360.setOnClickListener(this);
        rl_case.setOnClickListener(this);
        rl_product.setOnClickListener(this);
        rl_scene.setOnClickListener(this);
        rl_cart.setOnClickListener(this);
        rl_brand_product.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        if(!IssueApplication.IsRoot()){
            SystemClock.sleep(IssueApplication.SLEEP_TIME);
        }
        switch (v.getId()){
            case R.id.rl_diy:
                intent = new Intent(getActivity(), DiyActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_video:

                intent = new Intent(getActivity(), BrandHomeActivity.class);
                intent.putExtra(Constance.is_video,true);
                startActivity(intent);
                break;
            case R.id.rl_360:
                intent=new Intent(getActivity(),WebViewActivity.class);
                intent.putExtra(Constance.url,"https://720yun.com/t/b8dj50yfOv2?scene_id=11270470");
                startActivity(intent);
                break;
            case R.id.rl_case:
                intent = new Intent(getActivity(), CaseActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_product:
                intent = new Intent(getActivity(), ProListActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_scene:
                intent = new Intent(getActivity(), SceneHDActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_cart:
                IntentUtil.startActivity(getActivity(), ArticleActivity.class, false);
                break;
            case R.id.rl_rm:
                Intent intent=new Intent(getActivity(), ProListActivity.class);
                intent.putExtra(Constance.is_hot,true);
                startActivity(intent);
                break;
            case R.id.rl_style:
                intent=new Intent(getActivity(), ProListActivity.class);
                intent.putExtra(Constance.attr,"风格");
                intent.putExtra(Constance.attr_value,"全部");
                startActivity(intent);
                break;
            case R.id.rl_space:
                intent=new Intent(getActivity(), ProListActivity.class);
                intent.putExtra(Constance.attr,"空间");
                intent.putExtra(Constance.attr_value,"全部");
                startActivity(intent);
                break;
            case R.id.rl_type:
                intent=new Intent(getActivity(), ProListActivity.class);
                intent.putExtra(Constance.attr,"类型");
                intent.putExtra(Constance.attr_value,"全部");
                startActivity(intent);
                break;
            case R.id.rl_others:
                intent=new Intent(getActivity(), OthersHomeActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_brand_product:
                intent=new Intent(getActivity(), BrandProductActivity.class);
                startActivity(intent);
                break;
        }
    }
}
