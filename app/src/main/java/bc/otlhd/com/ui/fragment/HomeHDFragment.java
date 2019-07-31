package bc.otlhd.com.ui.fragment;

import android.os.Bundle;
//
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lib.common.hxp.view.PullableGridView;

import bc.otlhd.com.R;
import bc.otlhd.com.common.BaseFragment;
import bc.otlhd.com.controller.product.HomeHDController;
import bc.otlhd.com.ui.activity.MainActivity;

/**
 * @author: Jun
 * @date : 2017/3/30 13:50
 * @description :
 */
public class HomeHDFragment extends BaseFragment implements View.OnClickListener {
    private HomeHDController mController;
    private TextView competitive_tv, new_tv, hot_tv;
    private ImageView top_iv;
    private PullableGridView mGoodsSv;
    private int mHeigh=0;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fm_home_hd, null);
//    }

    @Override
    protected int getLayout() {
        return R.layout.fm_home_hd;
    }


    @Override
    protected void initController() {
        mController = new HomeHDController(this);
    }

    @Override
    protected void initViewData() {

    }

    @Override
    protected void initView() {
        competitive_tv = (TextView) getView().findViewById(R.id.competitive_tv);
        new_tv = (TextView) getView().findViewById(R.id.new_tv);
        hot_tv = (TextView) getView().findViewById(R.id.hot_tv);
        competitive_tv.setOnClickListener(this);
        new_tv.setOnClickListener(this);
        hot_tv.setOnClickListener(this);
        mGoodsSv = (PullableGridView) getView().findViewById(R.id.gridView);
        mHeigh = this.getResources().getDisplayMetrics().heightPixels;
        top_iv = (ImageView) getView().findViewById(R.id.top_iv);
        top_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mGoodsSv.setSelection(0);
                top_iv.setVisibility(View.VISIBLE);
            }
        });

        mGoodsSv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("520it", position + "");
                if (position == 8) {
                    top_iv.setVisibility(View.VISIBLE);
                } else {
                    top_iv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onStart() {
        super.onStart();

    }

    public void refreshData(){
        if(((MainActivity)getActivity()).mIsRefresh){
            mController.page = 1;
            mController.sendGoodsList("0", 0);
            ((MainActivity)getActivity()).mIsRefresh=false;
        }
    }


    public void searchData(String keyword){
        mController.keyword=keyword;
        mController.page=1;
        mController.sendGoodsList("0",0);
//        mController.selectProduct(mController.page, "20", null, null, null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mController.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        selectTypeProduct(v.getId());
        mController.selectSortType(v.getId());
    }

    private void selectTypeProduct(int type) {
        competitive_tv.setTextColor(getActivity().getResources().getColor(R.color.black));
        new_tv.setTextColor(getActivity().getResources().getColor(R.color.txt_black));
        hot_tv.setTextColor(getActivity().getResources().getColor(R.color.txt_black));
        switch (type) {
            case R.id.competitive_tv:
                competitive_tv.setTextColor(getActivity().getResources().getColor(R.color.colorPrimaryRed));
                break;
            case R.id.new_tv:
                new_tv.setTextColor(getActivity().getResources().getColor(R.color.colorPrimaryRed));
                break;
            case R.id.hot_tv:
                hot_tv.setTextColor(getActivity().getResources().getColor(R.color.colorPrimaryRed));
                break;
        }
    }

}
