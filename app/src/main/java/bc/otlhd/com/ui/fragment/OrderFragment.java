package bc.otlhd.com.ui.fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import bc.otlhd.com.R;
import bc.otlhd.com.common.BaseFragment;
import bc.otlhd.com.controller.user.OrderController;

/**
 * @author: Jun
 * @date : 2017/2/6 13:50
 * @description :
 */
public class OrderFragment extends BaseFragment {
    private OrderController mController;
    public List<String> list = new ArrayList<String>();
    public int flag;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if(bundle != null){
            list = bundle.getStringArrayList("content");
            flag = bundle.getInt("flag");
        }
    }



//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fm_order, null);
//    }

    @Override
    protected int getLayout() {
        return R.layout.fm_order;
    }

    @Override
    protected void initController() {
        mController=new OrderController(this);
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

    public static OrderFragment newInstance(List<String> contentList,int flag){
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("content", (ArrayList<String>) contentList);
        bundle.putInt("flag", flag);
        OrderFragment orderFm = new OrderFragment();
        orderFm.setArguments(bundle);
        return orderFm;

    }
}
