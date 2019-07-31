package bc.otlhd.com.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yjn.swipelistview.swipelistviewlibrary.widget.SwipeMenuListView;

import bc.otlhd.com.R;
import bc.otlhd.com.common.BaseFragment;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.cons.IntentKeys;
import bc.otlhd.com.controller.buy.CartController;
import bc.otlhd.com.ui.activity.IssueApplication;

/**
 * @author Jun
 * @time 2017/1/5  12:00
 * @desc 购物车
 */
public class CartFragment extends BaseFragment implements View.OnClickListener {
    private CartController mController;
    private TextView edit_tv, settle_tv;
    private SwipeMenuListView listView;
    private CheckBox checkAll;
    private Boolean mIsBack=false;
    private LinearLayout back_ll;
    private TextView money_tv;
    private TextView tv_delete;
    public EditText sumAgioTv;


//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fm_cart, null);
//    }

    @Override
    protected int getLayout() {
        return R.layout.fm_cart;
    }

    @Override
    protected void initController() {
        mController = new CartController(this);
    }

    @Override
    protected void initViewData() {
    }

    @Override
    protected void initView() {
        edit_tv = (TextView)  getActivity().findViewById(R.id.edit_tv);
        settle_tv = (TextView) getActivity().findViewById(R.id.settle_tv);
        checkAll = (CheckBox) getActivity().findViewById(R.id.checkAll);
        money_tv = (TextView) getActivity().findViewById(R.id.money_tv);
        tv_delete = (TextView) getView().findViewById(R.id.tv_delete);
        LinearLayout agiolv= (LinearLayout) getView().findViewById(R.id.agiolv);
        sumAgioTv = (EditText) getView().findViewById(R.id.sumAgioTv);
        agiolv.setVisibility(IntentKeys.ISAGIO==true?View.VISIBLE:View.GONE);

        settle_tv.setOnClickListener(this);
        edit_tv.setOnClickListener(this);
        tv_delete.setOnClickListener(this);
        checkAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mController.setCkeckAll(isChecked);
            }
        });
        back_ll = (LinearLayout) getActivity().findViewById(R.id.back_ll);
        if(mIsBack==false){
            back_ll.setVisibility(View.GONE);
        }
        sumAgioTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                money_tv.setText("￥"+ mController.myAdapter.getTotalMoney()+"");
                Log.v("520it","触发:"+money_tv.getText());
            }
        });

    }

    @Override
    protected void initData() {
        if(getArguments()==null) return;
        if(getArguments().get(Constance.product)==null) return;
        mIsBack= (Boolean) getArguments().get(Constance.product);
    }

    @Override
    public void onStart() {
        super.onStart();
        mController.sendShoppingCart();
        checkAll.setChecked(false);

    }

    @Override
    public void onClick(View v) {
        if(!IssueApplication.IsRoot()){
            SystemClock.sleep(IssueApplication.SLEEP_TIME);
        }
        switch (v.getId()) {
            case R.id.edit_tv:
                mController.setEdit();
                break;
            case R.id.settle_tv:
                mController.sendSettle();
                break;
            case R.id.tv_delete:
                mController.sendDeleteCart();
                break;

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mController.onActivityResult(requestCode, resultCode, data);
    }
}
