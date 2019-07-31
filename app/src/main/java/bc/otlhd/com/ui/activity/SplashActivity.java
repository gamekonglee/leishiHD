package bc.otlhd.com.ui.activity;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

import bc.otlhd.com.R;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.ui.activity.user.LoginActivity;
import bc.otlhd.com.utils.MyShare;
import bc.otlhd.com.utils.UIUtils;
import bocang.utils.AppUtils;
import bocang.utils.IntentUtil;
import bocang.view.BaseActivity;

/**
 * @author Jun
 * @time 2017/1/5  10:29
 * @desc 启动页
 */
public class SplashActivity extends BaseActivity {
    private ImageView mLogoIv;
    private AlphaAnimation mAnimation;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_splash);
        mLogoIv = (ImageView) findViewById(R.id.logo_iv);
        mLogoIv.setScaleType(ImageView.ScaleType.FIT_XY);
        //布置透明度动画
        mAnimation.setDuration(2500);
        mAnimation.setFillAfter(true);
        mLogoIv.startAnimation(mAnimation);
    }

    @Override
    protected void initData() {
        mAnimation = new AlphaAnimation(0.2f, 1.0f);
        new Timer().schedule(new TimerSchedule(), 2600);
    }

    @Override
    protected void onViewClick(View v) {

    }

    private class TimerSchedule extends TimerTask {
        @Override
        public void run() {
            String token = MyShare.get(UIUtils.getContext()).getString(Constance.TOKEN);
            if (AppUtils.isEmpty(token)) {
                IntentUtil.startActivity(SplashActivity.this, LoginActivity.class, true);
            } else {
                IntentUtil.startActivity(SplashActivity.this, LoginActivity.class, true);
            }
        }
    }
}
