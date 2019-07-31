package bc.otlhd.com.ui.view.popwindow;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;

import bc.otlhd.com.R;
import bc.otlhd.com.listener.ITwoCodeListener;
import bc.otlhd.com.ui.activity.IssueApplication;
import bc.otlhd.com.utils.ImageUtil;
import bc.otlhd.com.utils.net.Network;


/**
 * @author: Jun
 * @date : 2017/2/16 15:12
 * @description :
 */
public class TwoCodeSharePopWindow extends BasePopwindown implements View.OnClickListener {
    private ImageView two_code_iv;
    private RelativeLayout main_rl;
    protected Network mNetWork;
    private Timer timer;
    private Activity mActivity;
    private ITwoCodeListener mListener;
    private TextView remark_tv;

    public void setListener(ITwoCodeListener listener) {
        mListener = listener;
    }

    public TwoCodeSharePopWindow(Context context, Activity view) {
        super(context);
        mActivity=view;


    }

    @Override
    protected void initView(Context context) {
        View contentView = View.inflate(mContext, R.layout.pop_two_code, null);
        mNetWork=new Network();
        initUI(contentView);
        initViewData();

    }

    private void initViewData() {
        two_code_iv.setVisibility(View.VISIBLE);
        two_code_iv.setImageBitmap(ImageUtil.createQRImage(IssueApplication.sharePath, 600, 600));
        remark_tv.setText(IssueApplication.shareRemark);
    }

    private void initUI(View contentView) {
        two_code_iv = (ImageView) contentView.findViewById(R.id.two_code_iv);
        remark_tv = (TextView) contentView.findViewById(R.id.remark_tv);
        main_rl = (RelativeLayout) contentView.findViewById(R.id.main_rl);
        main_rl.setOnClickListener(this);
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
        }
    }




}
