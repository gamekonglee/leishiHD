package bc.otlhd.com.ui.fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import bc.otlhd.com.R;
import bc.otlhd.com.common.BaseFragment;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.controller.product.IntroduceGoodsController;
import bc.otlhd.com.ui.activity.product.ProDetailActivity;
import bc.otlhd.com.ui.view.PullUpToLoadMore;
import bocang.utils.AppUtils;

/**
 * @author: Jun
 * @date : 2017/2/14 17:57
 * @description :
 */
public class IntroduceGoodsFragment extends BaseFragment implements View.OnClickListener {
    private IntroduceGoodsController mController;
    public String productId;
    private RelativeLayout collect_rl, rl_2, rl_rl;
    private ConvenientBanner mConvenientBanner;
    private TextView mParamentTv;

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fm_product_introduce, null);
//    }

    @Override
    protected int getLayout() {
        return R.layout.fm_product_introduce;
    }

    @Override
    protected void initController() {
        mController = new IntroduceGoodsController(this);
    }

    @Override
    protected void initViewData() {

    }

    @Override
    protected void initView() {
        collect_rl = (RelativeLayout) getActivity().findViewById(R.id.collect_rl);
        rl_2 = (RelativeLayout) getActivity().findViewById(R.id.rl_2);
        collect_rl.setOnClickListener(this);
        rl_2.setOnClickListener(this);
        mConvenientBanner = (ConvenientBanner) getActivity().findViewById(R.id.convenientBanner);
        rl_rl = (RelativeLayout) getActivity().findViewById(R.id.rl_rl);
        mParamentTv = (TextView) getActivity().findViewById(R.id.type_tv);
        final PullUpToLoadMore ptlm = (PullUpToLoadMore) getActivity().findViewById(R.id.ptlm);
        ptlm.setmListener(new PullUpToLoadMore.ScrollListener() {
            @Override
            public void onScrollToBottom(int currPosition) {
                if (currPosition == 0) {
                    mListener.onScrollToBottom(0);
                } else {
                    mListener.onScrollToBottom(1);
                }
            }
        });

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initData() {
        productId = (String) getArguments().get(Constance.product);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.collect_rl:
                mController.sendCollectGoods();
                break;
            case R.id.rl_2:
                mController.selectParament();
            break;
        }
    }


    private ScrollListener mListener;

    public void setmListener(ScrollListener mListener) {
        this.mListener = mListener;
    }


    public interface ScrollListener {
        void onScrollToBottom(int currPosition);

    }

    //在主线程执行
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserEvent(Integer action) {
        if (action == Constance.PROPERTY) {
            String property = ((ProDetailActivity) getActivity()).mPropertyValue;
            if (AppUtils.isEmpty(property))
                return;
            mParamentTv.setText("已选 " + (property));
        }
    }


}
