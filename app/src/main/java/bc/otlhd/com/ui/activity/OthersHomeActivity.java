package bc.otlhd.com.ui.activity;

import android.content.Intent;
import android.os.SystemClock;
import android.view.View;

import bc.otlhd.com.R;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.ui.activity.brand.BrandHomeActivity;
import bc.otlhd.com.ui.activity.brand.BrandVideoListActivity;
import bc.otlhd.com.ui.activity.programme.SceneHDActivity;
import bocang.utils.IntentUtil;
import bocang.view.BaseActivity;

/**
 * Created by gamekonglee on 2019/5/8.
 */

public class OthersHomeActivity extends BaseActivity {

    private Intent intent;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
    setContentView(R.layout.activity_others_home);
    View view_video=findViewById(R.id.rl_video);
    View view_screen=findViewById(R.id.rl_scene);
    View view_case =findViewById(R.id.rl_case);
    View view_news=findViewById(R.id.rl_xinwen);
    View view_360=findViewById(R.id.rl_360);
        view_video.setOnClickListener(this);
        view_screen.setOnClickListener(this);
        view_case.setOnClickListener(this);
        view_news.setOnClickListener(this);
        view_360.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {
        if(!IssueApplication.IsRoot()){
            SystemClock.sleep(IssueApplication.SLEEP_TIME);
        }
    switch (v.getId()){
        case R.id.rl_video:
            intent = new Intent(this, BrandVideoListActivity.class);
            intent.putExtra(Constance.is_video,true);
            startActivity(intent);
            break;
        case R.id.rl_scene:
            intent = new Intent(this, SceneHDActivity.class);
            startActivity(intent);
            break;
        case R.id.rl_case:
            intent = new Intent(this, CaseActivity.class);
            startActivity(intent);
            break;
        case R.id.rl_xinwen:
            IntentUtil.startActivity(this, ArticleActivity.class, false);
            break;
        case R.id.rl_360:
            intent=new Intent(this,WebViewActivity.class);
            intent.putExtra(Constance.url,"https://720yun.com/t/b8dj50yfOv2?scene_id=11270470");
            startActivity(intent);
            break;
    }
    }
}
