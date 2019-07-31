package bc.otlhd.com.common;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import bc.otlhd.com.R;
import bc.otlhd.com.ui.view.LoadingDialog;

/**
 * @author Jun
 * @time 2017/1/5  11:56
 * @desc ${TODD}
 */
public abstract class BaseFragment extends Fragment {
    public LoadingDialog mDialog;
    private boolean isDestroy;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDiaLog();
        initData();
        initView();
        initController();
        initViewData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayout(),null);
    }

    protected abstract int getLayout();

    protected abstract void initController();

    /**
     * 初始化获取数据
     */
    protected abstract void initViewData();

    /**
     * 初始化视图
     */
    protected abstract void initView();

    /**
     * 初始化控件
     */
    protected abstract void initData();


    @Override
    public void onStart() {
        super.onStart();

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroy = true;
    }

    private void initDiaLog() {
    }

    private boolean showDialog;// 显示加载对话框

    /**
     * 显示加载对话框
     *
     * @param showDialog 是否显示加载对话框
     */
    public void setShowDialog(boolean showDialog) {
        this.showDialog = showDialog;
    }

    /**
     * 加载框文本
     */
    public void setShowDialog(String msg) {
        if (isDestroy)
            return;
        if (msg == null) {
            showDialog = false;
            return;
        }
        showDialog = true;
        if (mDialog == null) {
            setLoadingDialog(new LoadingDialog(getContext(), msg));
        } else {
            mDialog.setLoadMsg(msg);
        }
    }

    public void setLoadingDialog(LoadingDialog loadingDialog) {
        this.mDialog = loadingDialog;
    }

    /**
     * 隐藏加载框
     */
    public void hideLoading() {
        if (isDestroy)
            return;
        if (mDialog != null)
            mDialog.dismiss();
    }

    /**
     * 显示加载框
     */
    public void showLoading() {
        if (isDestroy)
            return;
        if (showDialog) {
            if (mDialog == null) {
                setLoadingDialog(new LoadingDialog(getContext()));
            }
            try {
                mDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    private View errorView, contentView;
    private TextView error_tv;
    private ImageView error_iv;
    private RotateAnimation animation;


    /**
     * 加载页面
     *
     * @param resId
     */
    public void showLoadingPage(String tip, int resId) {
        errorinit();
        if (errorView == null) {
            return;
        }
        if (error_iv == null) {
            return;
        }
        if (error_tv == null) {
            return;
        }
        if (contentView == null) {
            return;
        }

        errorView.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(tip)) {
            error_tv.setText(tip);
        } else {
            error_tv.setText("数据正在加载...");
        }
        error_iv.setImageResource(resId);
        /** 设置旋转动画 */
        if (animation == null) {
            animation = new RotateAnimation(0f, 359f, Animation.RELATIVE_TO_SELF,
                    0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(1000);//设置动画持续时间
            /** 常用方法 */
            animation.setRepeatCount(Integer.MAX_VALUE);//设置重复次数
            animation.startNow();
        }
        error_iv.setAnimation(animation);
    }


    /**
     * 显示错误页面
     *
     * @param message
     * @param resId
     */
    public void showErrorView(String message, int resId) {
        errorinit();
        if (errorView == null) {
            return;
        }
        if (error_iv == null) {
            return;
        }
        if (error_tv == null) {
            return;
        }
        if (contentView == null) {
            return;
        }

        error_iv.setImageResource(resId);
        if (!TextUtils.isEmpty(message)) {
            error_tv.setText(message);
        } else {
            error_tv.setText("数据加载失败！");
        }
        error_iv.setAnimation(null);
        errorView.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.GONE);
    }

    private void errorinit() {
        errorView = getView().findViewById(R.id.errorView);
        error_iv = (ImageView) getView().findViewById(R.id.error_iv);
        error_tv = (TextView) getView().findViewById(R.id.error_tv);
        contentView = getView().findViewById(R.id.contentView);
    }


    /**
     * 显示内容区域
     */
    public void showContentView() {
        errorinit();
        if (errorView == null) {
            return;
        }
        if (error_iv == null) {
            return;
        }
        if (error_tv == null) {
            return;
        }
        if (contentView == null) {
            return;
        }
        contentView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
    }

}
