package bc.otlhd.com.ui.view.popwindow;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import java.util.Timer;
import java.util.TimerTask;

import bc.otlhd.com.R;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.cons.NetWorkConst;
import bc.otlhd.com.listener.INetworkCallBack;
import bc.otlhd.com.listener.ITwoCodeListener;
import bc.otlhd.com.utils.ImageUtil;
import bc.otlhd.com.utils.net.Network;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;


/**
 * @author: Jun
 * @date : 2017/2/16 15:12
 * @description :
 */
public class TwoCodePopWindow extends BasePopwindown implements View.OnClickListener {
    private ImageView two_code_iv;
    private RelativeLayout main_rl;
    protected Network mNetWork;
    private Timer timer;
    private Activity mActivity;
    private ITwoCodeListener mListener;

    public void setListener(ITwoCodeListener listener) {
        mListener = listener;
    }

    public TwoCodePopWindow(Context context,Activity view) {
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

    private String getToken() {
        String token = "";
        String chars = "ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678";
        int len = chars.length();
        for (int i = 0; i < 32; i++) {
            token += chars.charAt((int) Math.floor(Math.random() * len));
        }
        return token;
    }

    private void initViewData() {
        two_code_iv.setVisibility(View.VISIBLE);
        final String token=getToken();
        String path = NetWorkConst.TWOCOE + token;
        two_code_iv.setImageBitmap(ImageUtil.createQRImage(path, 600, 600));

        timer=new Timer();
        TimerTask task= new TimerTask() {
            @Override
            public void run() {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
////                        MyToast.show(mContext, System.currentTimeMillis() + "");
//                        Log.v("520it",System.currentTimeMillis() + "");
                        senMineDiy(token);

                    }
                });
            }
        };

        //time为Date类型：在指定时间执行一次。

        //firstTime为Date类型,period为long，表示从firstTime时刻开始，每隔period毫秒执行一次。
        //delay 为long类型：从现在起过delay毫秒执行一次。
        timer.schedule(task, 3000,3000);
    }

    /**
     * 获取自定义场景列表
     */
    private void senMineDiy(String tolen){
        mNetWork.sendMineDiy(tolen, new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
               String path= ans.getString(Constance.path);
                if(!AppUtils.isEmpty(path)){
                    mListener.onTwoCodeChanged("http://www.juhao.com/"+path);
                    Log.v("520it",path);
                    onDismiss();
                    timer.cancel();
                }
            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {

            }
        });

    }


    private void initUI(View contentView) {
        two_code_iv = (ImageView) contentView.findViewById(R.id.two_code_iv);
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
                timer.cancel();
                break;
        }
    }




}
