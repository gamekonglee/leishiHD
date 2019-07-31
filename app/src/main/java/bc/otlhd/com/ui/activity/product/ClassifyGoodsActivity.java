package bc.otlhd.com.ui.activity.product;

import android.app.Activity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bc.otlhd.com.R;
import bc.otlhd.com.ui.fragment.ClassifyGoodsFragment;
import bc.otlhd.com.ui.fragment.FilterGoodsFragment;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/2/17 14:55
 * @description :
 */
public class ClassifyGoodsActivity extends BaseActivity {

    private ViewPager mPager;//页卡内容
    private List<Fragment> listViews; // Tab页面列表
    private TextView t1, t2;// 页卡头标
    private ClassifyGoodsFragment mClassifyGoodsFragment;
    private FilterGoodsFragment mFilterGoodsFragment;
    public static Activity mActivity;
    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.fm_classify);
        t1 = (TextView) findViewById(R.id.text2);
        t2 = (TextView) findViewById(R.id.text3);
        t1.setOnClickListener(new MyOnClickListener(0));
        t2.setOnClickListener(new MyOnClickListener(1));
        mPager = (ViewPager) findViewById(R.id.vPager);
        listViews = new ArrayList<>();
        mClassifyGoodsFragment=new ClassifyGoodsFragment();
        mFilterGoodsFragment=new FilterGoodsFragment();
        listViews.add(mClassifyGoodsFragment);
        listViews.add(mFilterGoodsFragment);
        mPager.setAdapter(new MyFrageStatePagerAdapter(getSupportFragmentManager()));
        mPager.setCurrentItem(0);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                getCurrentTv(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mActivity=this;

    }

    class MyFrageStatePagerAdapter extends FragmentStatePagerAdapter
    {

        public MyFrageStatePagerAdapter(FragmentManager fm)
        {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            return listViews.get(position);
        }

        @Override
        public int getCount() {
            return listViews.size();
        }

    }

    /**
     * 头标点击监听
     */
    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            getCurrentTv(index);
            mPager.setCurrentItem(index);
        }
    }

    private void getCurrentTv(int type){
        regiestTv();
        switch (type){
            case 0:
                t1.setBackgroundResource(R.drawable.classify_shape_pressed);
                t1.setTextColor(getResources().getColor(R.color.white));
                break;
            case 1:
                t2.setBackgroundResource(R.drawable.classify_shape_pressed);
                t2.setTextColor(getResources().getColor(R.color.white));
                break;
        }
    }

    private void regiestTv(){
        t1.setBackgroundResource(R.drawable.classify_shape_active);
        t2.setBackgroundResource(R.drawable.classify_shape_active);
        t1.setTextColor(getResources().getColor(R.color.green));
        t2.setTextColor(getResources().getColor(R.color.green));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {

    }
}
