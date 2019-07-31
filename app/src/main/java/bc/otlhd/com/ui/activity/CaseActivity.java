package bc.otlhd.com.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import astuetz.MyPagerSlidingTabStrip;
import bc.otlhd.com.R;
import bc.otlhd.com.bean.BrandBean;
import bc.otlhd.com.bean.CaseListBean;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.cons.NetWorkConst;
import bc.otlhd.com.controller.CaseController;
import bc.otlhd.com.net.OkHttpUtils;
import bc.otlhd.com.utils.FileUtil;
import bc.otlhd.com.utils.LogUtils;
import bc.otlhd.com.utils.MyShare;
import bc.otlhd.com.utils.UIUtils;
import bocang.json.JSONObject;
import bocang.view.BaseActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by gamekonglee on 2019/2/26.
 */

public class CaseActivity extends BaseActivity {

    private CaseController mController;
    private MyPagerSlidingTabStrip pagerSlidingTabStrip;
    private ViewPager vp;
    private PagerAdapter pagerAdapter;
    private List<BrandBean> newsClassBeans;
    int current=0;
    private String id="";
    private List<CaseListBean> newsBeans;
    private GridView gv_case;
    private BaseAdapter gvAdapter;
    private String mId;
    private List<CaseListBean> caseListBeans;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_case);
        pagerSlidingTabStrip = (MyPagerSlidingTabStrip) findViewById(R.id.tab_strip);
        vp = (ViewPager) findViewById(R.id.vp);
        gv_case = (GridView) findViewById(R.id.gv_case);
        pagerSlidingTabStrip.selectColor=(getResources().getColor(R.color.colorPrimaryGreen2));
        pagerSlidingTabStrip.defaultColor=getResources().getColor(R.color.txt_black);
        pagerSlidingTabStrip.setDividerColor(Color.TRANSPARENT);
        pagerSlidingTabStrip.setIndicatorColor(getResources().getColor(R.color.colorPrimaryGreen2));
        pagerSlidingTabStrip.setIndicatorHeight(getResources().getDimensionPixelOffset(R.dimen.x1));
        pagerSlidingTabStrip.setUnderlineColor(getResources().getColor(R.color.transparent));
        pagerSlidingTabStrip.setUnderlineHeight(0);
        newsBeans=new ArrayList<>();
        caseListBeans=new ArrayList<>();

        getBrandList();
        gv_case.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!IssueApplication.IsRoot()){
                    SystemClock.sleep(IssueApplication.SLEEP_TIME);
                }

                Intent intent=new Intent(CaseActivity.this,CaseDetailActivity.class);
                if(current<newsBeans.size()&&position<newsBeans.get(current).getData().size()){
                intent.putExtra(Constance.url,newsBeans.get(current).getData().get(position).getFilepath());
                LogUtils.logE("newsbean",new Gson().toJson(newsBeans.get(current).getData(),new TypeToken<List<CaseListBean.Data>>(){}.getType()));
                intent.putExtra(Constance.urlList,new Gson().toJson(newsBeans.get(current).getData(),new TypeToken<List<CaseListBean.Data>>(){}.getType()));
                intent.putExtra(Constance.currentP,position);
                startActivity(intent);
                }
            }
        });
    }

    private void getBrandList() {
        if(!OkHttpUtils.hashkNewwork()){
            String result= MyShare.get(this).getString(Constance.brandList);
            parseData(result);
            return;
        }
            OkHttpUtils.getbrandList(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result=response.body().string();
                    LogUtils.logE("res",result);
                    parseData(result);

                }
            });
    }

    private void parseData(String result) {
        JSONObject jsonObject=new JSONObject(result);
        if(jsonObject==null)return;
        List<BrandBean> newsClassBeans = new Gson().fromJson(jsonObject.getJSONArray(Constance.data).toString(),new TypeToken<List<BrandBean>>(){}.getType());
        for(int i=0;i<newsClassBeans.size();i++){
            if(!newsClassBeans.get(i).getType().equals("4")){
                newsClassBeans.remove(i);
                i--;
            }
        }
        if(newsClassBeans !=null&& newsClassBeans.size()>0){
            mId = newsClassBeans.get(0).getId();
            getNewsClass();
        }
    }

    private void initAdapter() {
        gvAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return newsBeans.get(current).getData().size();
            }

            @Override
            public Object getItem(int position) {
                return newsBeans.get(current).getData().get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Holder holder=new Holder();
                if(convertView==null){
                    convertView=View.inflate(CaseActivity.this, R.layout.item_case,null);
                    holder.iv_img= (ImageView) convertView.findViewById(R.id.iv_img);
                    holder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
                    int width= (UIUtils.getScreenWidth(CaseActivity.this)-UIUtils.dip2PX(90))/3;
                    holder.iv_img.setLayoutParams(new LinearLayout.LayoutParams(width,width*215/290));
                    convertView.setTag(holder);
                }else {
                    holder= (Holder) convertView.getTag();
                }
                holder.tv_name.setText(newsBeans.get(current).getData().get(position).getName());
                if(!OkHttpUtils.hashkNewwork()){
                    ImageLoader.getInstance().displayImage("file://"+ FileUtil.getBrandExternDir(newsBeans.get(current).getData().get(position).getFilepath()),holder.iv_img);
                }else {
                    ImageLoader.getInstance().displayImage(NetWorkConst.API_HOST+"/App/"+NetWorkConst.AppName+"/Public/uploads/"+newsBeans.get(current).getData().get(position).getFilepath(),holder.iv_img);
                }

                return convertView;
            }
            class Holder {
                TextView tv_name;
                ImageView iv_img;
            }
        };
    }

    private void getNewsClass() {
        if(!OkHttpUtils.hashkNewwork()) {


            String result = MyShare.get(this).getString(Constance.brandDetail + mId);
            if (result == null) return;
            newsBeans = new Gson().fromJson(result,new TypeToken<List<CaseListBean>>(){}.getType());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initAdapter();
                    gv_case.setAdapter(gvAdapter);
                    initPageAdapter();
                }
            });
            return;
        }
        OkHttpUtils.getBrandDetail(mId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result=response.body().string();
                LogUtils.logE("caseList",result);
                newsBeans = new Gson().fromJson(result,new TypeToken<List<CaseListBean>>(){}.getType());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initAdapter();
                        gv_case.setAdapter(gvAdapter);
                        initPageAdapter();
                    }
                });
            }
        });
//        OkHttpUtils.getbrandList(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String result=response.body().string();
//                LogUtils.logE("res",result);
//                JSONObject jsonObject=new JSONObject(result);
//                if(jsonObject==null)return;
//                newsClassBeans = new Gson().fromJson(jsonObject.getJSONArray(Constance.data).toString(),new TypeToken<List<BrandBean>>(){}.getType());
//                for(int i=0;i<newsClassBeans.size();i++){
//                    if(!newsClassBeans.get(i).getType().equals("4")){
//                        newsClassBeans.remove(i);
//                        i--;
//                    }
//                }
//                if(newsClassBeans !=null&& newsClassBeans.size()>0){
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        initPageAdapter();
//                    }
//                });
//                }
//
//            }
//        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {

    }


    private void initPageAdapter() {
            pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return newsBeans.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view==object;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return newsBeans.get(position).getName();
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                TextView textView=new TextView(CaseActivity.this);
                container.addView(textView);
                return textView;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }
        };
        vp.setAdapter(pagerAdapter);
        pagerSlidingTabStrip.setViewPager(vp);
        pagerSlidingTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                current=position;
            }

            @Override
            public void onPageSelected(int position) {
                current=position;
                if(newsBeans!=null&&position<newsBeans.size()){
                 gvAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vp.setCurrentItem(current);
        gvAdapter.notifyDataSetChanged();
//        if(newsBeans!=null&&0<newsBeans.size()){
//            id = newsBeans.get(0).getId();
//            getNews();
//        }
    }

    private void getNews() {
        OkHttpUtils.getBrandDetail(id, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result=response.body().string();
                LogUtils.logE("caseListBeans",result);
                caseListBeans = new Gson().fromJson(result,new TypeToken<List<CaseListBean>>(){}.getType());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gvAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

}
