package bc.otlhd.com.ui.fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bc.otlhd.com.R;
import bc.otlhd.com.common.BaseFragment;
import bc.otlhd.com.controller.brand.BrandController;

/**
 * @author: Jun
 * @date : 2017/5/8 10:45
 * @description :
 */
public class BrandFragment extends BaseFragment{
    private BrandController mController;

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fm_brand, null);
//    }

    @Override
    protected int getLayout() {
        return R.layout.fm_brand;
    }

    @Override
    protected void initController() {
        mController=new BrandController(this);
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
}
