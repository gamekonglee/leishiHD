package bc.otlhd.com.ui.activity.brand;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Handler;
import androidx.core.view.ViewCompat;
import android.transition.Transition;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shuyu.gsyvideoplayer.GSYVideoPlayer;
import com.shuyu.gsyvideoplayer.utils.FileUtils;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;

import java.io.File;

import bc.otlhd.com.R;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.controller.brand.BrandPlayController;
import bc.otlhd.com.ui.activity.IssueApplication;
import bc.otlhd.com.ui.view.LandLayoutVideo;
import bc.otlhd.com.utils.LogUtils;
import bocang.utils.AppUtils;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/5/9 10:22
 * @description :
 */
public class BrandPlayActivity extends BaseActivity {
    public JSONObject mData;
    private BrandPlayController mController;
    private LandLayoutVideo play;
    OrientationUtils orientationUtils;
    private boolean isTransition;
    private Transition transition;
    public final static String IMG_TRANSITION = "IMG_TRANSITION";
    public final static String TRANSITION = "TRANSITION";
    String path = "http://7xt9qi.com1.z0.glb.clouddn.com/juhaogongcheng1";
    public String name="";
    public String id;

    @Override
    protected void InitDataView() {

    }

    /**
     * 播放视频
     * @param path
     */
    public void startPlayVideo(String path){
        LogUtils.logE("path",path);
        play.setUp(path, true, new File(FileUtils.getPath()), "");

        //增加title
        play.getTitleTextView().setVisibility(View.VISIBLE);
        play.getTitleTextView().setText(name);
        //设置返回键
        play.getBackButton().setVisibility(View.VISIBLE);

        //设置旋转
        orientationUtils = new OrientationUtils(this, play);

        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        play.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orientationUtils.resolveByClick();
            }
        });

        GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL);
        //循环播放
        play.setLooping(true);
        //是否可以滑动调整
        play.setIsTouchWiget(true);
        //设置返回按键功能
        play.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        play.setLockLand(true);
        play.setHideKey(true);
        play.setBottomProgressBarDrawable(getResources().getDrawable(R.drawable.video_new_progress));

        //过渡动画
        initTransition();
    }

    private void initTransition() {
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
            ViewCompat.setTransitionName(play, IMG_TRANSITION);
            addTransitionListener();
            startPostponedEnterTransition();
        } else {
            play.startPlayLogic();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private boolean addTransitionListener() {
        transition = getWindow().getSharedElementEnterTransition();
        if (transition != null) {
            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {

                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    play.startPlayLogic();
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });
            return true;
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        IssueApplication.noAd=false;
    }

    protected void onDestroy() {
        if (orientationUtils != null)
            orientationUtils.releaseListener();
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        if(AppUtils.isEmpty(orientationUtils)){
            finish();
            return;

        }

        //先返回正常状态
        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            play.getFullscreenButton().performClick();
            return;
        }
        //释放所有
        play.setStandardVideoAllCallBack(null);
        GSYVideoPlayer.releaseAllVideos();
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.onBackPressed();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                }
            }, 500);
        }
    }

    @Override
    protected void initController() {
        mController=new BrandPlayController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_play);
        play = (LandLayoutVideo) findViewById(R.id.play);

    }

    @Override
    protected void initData() {
        Intent intent=getIntent();
        mData= (JSONObject) JSON.parseObject(intent.getStringExtra(Constance.data));
        id = intent.getStringExtra(Constance.id);
    }

    @Override
    protected void onViewClick(View v) {

    }
}
