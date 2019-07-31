package bc.otlhd.com.ui.view.popwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.PopupWindow;

import bc.otlhd.com.R;
import bc.otlhd.com.listener.IParamentChooseListener;


/**
 * @author: Jun
 * @date : 2017/2/16 15:12
 * @description :
 */
public class SelectProductPopWindow extends BasePopwindown {
    private IParamentChooseListener mListener;

    public void setListener(IParamentChooseListener listener) {
        mListener = listener;
    }

    public SelectProductPopWindow(Context context) {
        super(context);
        initViewData();

    }

    @Override
    protected void initView(Context context) {
        View contentView = View.inflate(mContext, R.layout.pop_select_product, null);
        initUI(contentView);

    }

    private void initViewData() {
    }

    private void initUI(View contentView) {
        mPopupWindow = new PopupWindow(contentView,-1, -1);
        // 1.让mPopupWindow内部的控件获取焦点
        mPopupWindow.setFocusable(true);
        // 2.mPopupWindow内部获取焦点后 外部的所有控件就失去了焦点
        mPopupWindow.setOutsideTouchable(true);
        //只有加载背景图还有效果
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        // 3.如果不马上显示PopupWindow 一般建议刷新界面
        mPopupWindow.update();
        mPopupWindow.setAnimationStyle(R.style.AnimBottom);
    }

}
