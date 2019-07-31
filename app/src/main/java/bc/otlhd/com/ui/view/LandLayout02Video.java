package bc.otlhd.com.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import bc.otlhd.com.R;

/**
 * @author: Jun
 * @date : 2017/5/9 12:00
 * @description :
 */
public class LandLayout02Video extends StandardGSYVideoPlayer {

    /**
     * 1.5.0开始加入，如果需要不同布局区分功能，需要重载
     */
    public LandLayout02Video(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public LandLayout02Video(Context context) {
        super(context);
    }

    public LandLayout02Video(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    //这个必须配置最上面的构造才能生效
    @Override
    public int getLayoutId() {
        return R.layout.sample_video_normal_02;
    }

    @Override
    protected void updateStartImage() {
        if (mIfCurrentIsFullscreen) {
            ImageView imageView = (ImageView) mStartButton;
            if (mCurrentState == CURRENT_STATE_PLAYING) {
                imageView.setImageResource(R.drawable.video_click_pause_selector);
            } else if (mCurrentState == CURRENT_STATE_ERROR) {
                imageView.setImageResource(R.drawable.video_click_play_selector);
            } else {
                imageView.setImageResource(R.drawable.video_click_play_selector);
            }
        } else {
            super.updateStartImage();
        }
    }


}

