package bc.otlhd.com.ui.view.popwindow;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import bc.otlhd.com.R;
import bc.otlhd.com.listener.ITwoCodeListener;
import bc.otlhd.com.ui.activity.IssueApplication;
import bc.otlhd.com.ui.activity.MainActivity;
import bc.otlhd.com.ui.activity.TianMaoActivity;
import bc.otlhd.com.ui.activity.user.SetPriceActivity;
import bc.otlhd.com.utils.net.Network;
import bocang.utils.IntentUtil;


/**
 * @author: Jun
 * @date : 2017/2/16 15:12
 * @description :
 */
public class HighSettingPopWindow extends BasePopwindown implements View.OnClickListener {
    private RelativeLayout main_rl,update_price_rl,tianmao_rl;
    private Activity mActivity;
    private ITwoCodeListener mListener;


    public void setListener(ITwoCodeListener listener) {
        mListener = listener;
    }

    public HighSettingPopWindow(Context context, Activity view) {
        super(context);
        mActivity=view;

    }

    @Override
    protected void initView(Context context) {
        View contentView = View.inflate(mContext, R.layout.pop_high_setting, null);
        mNetWork=new Network();
        initUI(contentView);
        initViewData();

    }

    private void initViewData() {
    }



    private void initUI(View contentView) {
        main_rl = (RelativeLayout) contentView.findViewById(R.id.main_rl);
        main_rl.setOnClickListener(this);
        update_price_rl = (RelativeLayout) contentView.findViewById(R.id.update_price_rl);
        update_price_rl.setOnClickListener(this);
        tianmao_rl = (RelativeLayout) contentView.findViewById(R.id.tianmao_rl);
        tianmao_rl.setOnClickListener(this);
        mPopupWindow = new PopupWindow(contentView, -1, -1);
        // 1.让mPopupWindow内部的控件获取焦点
        mPopupWindow.setFocusable(true);
        // 2.mPopupWindow内部获取焦点后 外部的所有控件就失去了焦点
        mPopupWindow.setOutsideTouchable(true);
        //只有加载背景图还有效果
        // 3.如果不马上显示PopupWindow 一般建议刷新界面
        mPopupWindow.update();
        // 设置弹出窗体显示时的动画，从底部向上弹出
        mPopupWindow.setAnimationStyle(R.style.AnimBottom);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_rl:
                onDismiss();
                break;
            case R.id.update_price_rl:
                ((MainActivity)mActivity).mIsRefresh=true;
                IntentUtil.startActivity(mActivity, SetPriceActivity.class, false);
                onDismiss();
                break;
            case R.id.tianmao_rl:
                IssueApplication.IsMainActibity=true;
                IntentUtil.startActivity(mActivity, TianMaoActivity.class, false);
                onDismiss();
                break;
        }
    }




}
