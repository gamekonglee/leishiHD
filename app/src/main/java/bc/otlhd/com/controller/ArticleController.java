package bc.otlhd.com.controller;

import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lib.common.hxp.view.GridViewForScrollView;
import com.lib.common.hxp.view.PullToRefreshLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bc.otlhd.com.R;
import bc.otlhd.com.adapter.BaseAdapterHelper;
import bc.otlhd.com.adapter.QuickAdapter;
import bc.otlhd.com.bean.ArticlesBean;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.cons.NetWorkConst;
import bc.otlhd.com.data.NewsDao;
import bc.otlhd.com.listener.INetworkCallBack;
import bc.otlhd.com.net.OkHttpUtils;
import bc.otlhd.com.ui.activity.ArticleActivity;
import bc.otlhd.com.ui.activity.NewsDetailActivity;
import bc.otlhd.com.ui.activity.user.MessageDetailActivity;
import bc.otlhd.com.utils.DateUtils;
import bc.otlhd.com.utils.LogUtils;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by gamekonglee on 2019/3/14.
 */

public class ArticleController extends BaseController implements PullToRefreshLayout.OnRefreshListener {

    private final ArticleActivity mView;
    private GridViewForScrollView listview;
    private PullToRefreshLayout mPullToRefreshLayout;
    private QuickAdapter adapter;
    private List<ArticlesBean> mArticlesBeans;
    private int page;
    private int pagePer;
    private List<ArticlesBean> mArticlesArray;

    public ArticleController(ArticleActivity articleActivity) {
        mView = articleActivity;
        initUI();
        mArticlesBeans = new ArrayList<>();
        initData();
    }

    private void initUI() {

        listview = (GridViewForScrollView) mView.findViewById(R.id.listview);
        mPullToRefreshLayout = ((PullToRefreshLayout) mView.findViewById(R.id.refresh_view));
        mPullToRefreshLayout.setOnRefreshListener(this);
        adapter = new QuickAdapter<ArticlesBean>(mView, R.layout.item_article){
            @Override
            protected void convert(BaseAdapterHelper helper, ArticlesBean item) {
                helper.setText(R.id.title,item.getName());
                helper.setText(R.id.tv_des,item.getDesc());
                ImageView iv_img=helper.getView(R.id.iv_img);
                if(item!=null&&item.getTime()!=null){
                helper.setText(R.id.tv_time, DateUtils.getStrTime(item.getTime()));
                }
                ImageLoader.getInstance().displayImage(NetWorkConst.IMAGE_URL+item.getPath(),iv_img);
            }
        };
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String url = mArticlesBeans.get(i).getUrl();
                ArticlesBean articlesBean=mArticlesBeans.get(i);
                Intent intent = new Intent(mView, NewsDetailActivity.class);
                intent.putExtra(Constance.title,articlesBean.getName());
                intent.putExtra(Constance.img,articlesBean.getPath());
                if(!OkHttpUtils.hashkNewwork()){
                intent.putExtra(Constance.content,articlesBean.getContent());
                }
                intent.putExtra(Constance.id,articlesBean.getId()+"");
//                intent.putExtra(Constance.url, url);
                mView.startActivity(intent);
            }
        });
    }
    private void initData() {
        page = 1;
        pagePer = 20;
        sendArticle();
    }
    public void sendArticle(){
//        LogUtils.logE("art:",page+"");
        if(!OkHttpUtils.hashkNewwork()){
                mArticlesArray= NewsDao.getNewsList();
                parseData();
            return;
        }
        OkHttpUtils.getNews("0", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                mArticlesArray = new ArrayList<>();
                try {
                    String result=response.body().string();
                    LogUtils.logE("result",result);
                    mArticlesArray =new Gson().fromJson(result,new TypeToken<List<ArticlesBean>>(){}.getType());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mView.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

//                        JSONObject result= null;
//                        try {
//                            result = new JSONObject(response.body().string());
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                        parseData();
                    }
                });
            }
        });
//        mNetWork.sendArticle(page, 20, new INetworkCallBack() {
//            @Override
//            public void onSuccessListener(String requestCode, JSONObject ans) {
//                if (null != mPullToRefreshLayout) {
//                    dismissRefesh();
//                }
//                JSONArray mArticlesArray = ans.getJSONArray(Constance.articles);
//                if(mArticlesArray!=null&&mArticlesArray.length()>0){
//                LogUtils.logE("articles",mArticlesArray.toString());
//                }
//                if (1 == page){
//                    mArticlesBeans=new ArrayList<>();
//                    for(int i=0;i<mArticlesArray.length();i++){
//                        JSONObject jsonObject = mArticlesArray.getJSONObject(i);
////                        if (jsonObject.getInt(Constance.article_type) == 1) {
//                        mArticlesBeans.add(new Gson().fromJson(String.valueOf(mArticlesArray.getJSONObject(i)), ArticlesBean.class));
////                        }
//                    }
//                }
//                else if (null != mArticlesBeans) {
//                    for (int i = 0; i < mArticlesArray.length(); i++) {
//                        JSONObject jsonObject = mArticlesArray.getJSONObject(i);
////                        if (jsonObject.getInt(Constance.article_type) == 1) {
//                        mArticlesBeans.add(new Gson().fromJson(String.valueOf(mArticlesArray.getJSONObject(i)), ArticlesBean.class));
////                        }
//                    }
//
//                    if (AppUtils.isEmpty(mArticlesArray))
//                        MyToast.show(mView, "没有更多内容了");
//                }
//
//                adapter.replaceAll(mArticlesBeans);
//                adapter.notifyDataSetChanged();
//                if (mArticlesBeans.size() == 0)
//                    return;
//
//            }
//
//            @Override
//            public void onFailureListener(String requestCode, JSONObject ans) {
//                if (null != mPullToRefreshLayout) {
//                    dismissRefesh();
//                }
//            }
//        });
    }

    private void parseData() {
        if (null != mPullToRefreshLayout) {
            dismissRefesh();
        }
        if(mArticlesArray ==null|| mArticlesArray.size()==0){
            return;
        }
        LogUtils.logE("articles", mArticlesArray.toString());
        if (1 == page){
            mArticlesBeans=new ArrayList<>();
            for(int i = 0; i< mArticlesArray.size(); i++){
//                                JSONObject jsonObject = mArticlesArray.getJSONObject(i);
//                        if (jsonObject.getInt(Constance.article_type) == 1) {
//                                mArticlesBeans.add(new Gson().fromJson(String.valueOf(mArticlesArray.get(i)), ArticlesBean.class));
                mArticlesBeans.add(mArticlesArray.get(i));
//                        }
            }
        }
        else if (null != mArticlesBeans) {
            for (int i = 0; i < mArticlesArray.size(); i++) {
//                                JSONObject jsonObject = mArticlesArray.get(i);
//                        if (jsonObject.getInt(Constance.article_type) == 1) {
                mArticlesBeans.add(mArticlesArray.get(i));
//                                mArticlesBeans.add(new Gson().fromJson(String.valueOf(mArticlesArray.getJSONObject(i)), ArticlesBean.class));
//                        }
            }

            if (AppUtils.isEmpty(mArticlesArray))
                MyToast.show(mView, "没有更多内容了");
        }

        adapter.replaceAll(mArticlesBeans);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        page=1;
        sendArticle();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        page++;
        sendArticle();
    }
    private void dismissRefesh() {
        mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }
    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

}

