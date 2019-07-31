package bc.otlhd.com.ui.activity;

import android.view.View;

import bc.otlhd.com.R;
import bc.otlhd.com.controller.ArticleController;
import bocang.view.BaseActivity;

/**
 * Created by gamekonglee on 2019/3/14.
 */

public class ArticleActivity extends BaseActivity {

    private ArticleController mController;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new ArticleController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_article);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {

    }
}
