package bc.otlhd.com.controller.brand;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bigkoo.convenientbanner.CBPageAdapter;
import com.bigkoo.convenientbanner.CBViewHolderCreator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bc.otlhd.com.R;
import bc.otlhd.com.bean.BrandBean;
import bc.otlhd.com.bean.Data;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.cons.NetWorkConst;
import bc.otlhd.com.controller.BaseController;
import bc.otlhd.com.controller.HomeController;
import bc.otlhd.com.listener.INetworkCallBack;
import bc.otlhd.com.net.OkHttpUtils;
import bc.otlhd.com.ui.activity.CaseActivity;
import bc.otlhd.com.ui.activity.IssueApplication;
import bc.otlhd.com.ui.activity.brand.BrandHomeActivity;
import bc.otlhd.com.ui.activity.programme.ImageDetailActivity;
import bc.otlhd.com.utils.LogUtils;
import bc.otlhd.com.utils.MyShare;
import bc.otlhd.com.utils.net.HttpListener;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by gamekonglee on 2019/2/15.
 */

public class  BrandHomeController extends BaseController{


    private final BrandHomeActivity mView;
    private bocang.json.JSONArray mBrandArray;
    private ProAdapter adapter;
    private int currentP;
    private int video_pos;
    private bocang.json.JSONArray temp;
    private Intent mIntent;
    private List<String> paths;

    public BrandHomeController(BrandHomeActivity brandHomeActivity) {
        mView = brandHomeActivity;
        init();
    }

    private void init() {
        mView.gv_brand_bottom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!IssueApplication.IsRoot()){
                    SystemClock.sleep(IssueApplication.SLEEP_TIME);
                }
                mView.gv_video.setVisibility(View.GONE);
                mView.webview.setVisibility(View.VISIBLE);
                mView.ll_video.setVisibility(View.GONE);
                mView.banner.setVisibility(View.GONE);
                if(mBrandArray.getJSONObject(position).getString(Constance.type).contains("4")){
                    Intent intent=new Intent(mView, CaseActivity.class);
                    intent.putExtra(Constance.id,mBrandArray.getJSONObject(position).getString(Constance.id));
                    mView.startActivity(intent);
                }else if(mBrandArray.getJSONObject(position).getString(Constance.type).contains("3")){
                    mView.gv_video.setVisibility(View.VISIBLE);
                    mView.webview.setVisibility(View.GONE);
                    List<BrandBean> brandBeanList=new ArrayList<>();
                    for(int i=0;i<temp.length();i++){
                        if (temp.getJSONObject(i).getString(Constance.type).equals("3")){
                            brandBeanList.add(new Gson().fromJson(temp.getJSONObject(i).toString(),BrandBean.class));
                        }
                    }
                    if(brandBeanList!=null&&brandBeanList.size()>0){
                        mView.adapter.replaceAll(brandBeanList);
                    }
                    currentP=position;
                    if(adapter!=null){
                        adapter.notifyDataSetChanged();
                    }
                }else if(mBrandArray.getJSONObject(position).getString(Constance.type).contains("2")){
                    currentP=position;
                    if(adapter!=null){
                        adapter.notifyDataSetChanged();
                    }
                    mView.banner.setVisibility(View.VISIBLE);
                    mView.webview.setVisibility(View.GONE);
                    mView.gv_video.setVisibility(View.GONE);
                    getBrandDetail();
                }else {
                    mView.banner.setVisibility(View.GONE);
                    mView.gv_video.setVisibility(View.GONE);
                    mView.webview.setVisibility(View.VISIBLE);
                currentP=position;
                if(adapter!=null){
                    adapter.notifyDataSetChanged();
                }
                getBrandDetail();
                }

            }
        });
        if(!OkHttpUtils.hashkNewwork()){
            String result=MyShare.get(mView).getString(Constance.brandList);
            if(result==null)return;
            JSONObject jsonObject=new JSONObject(result);
            if(jsonObject!=null){
                temp = jsonObject.getJSONArray(Constance.data);

                mBrandArray = new bocang.json.JSONArray();
                LogUtils.logE("brand", temp.toString());
//                for(int i = 0; i< temp.length(); i++){
//                    if(temp.getJSONObject(i).getString(Constance.type).equals("3")){
//                        video_pos = i;
//                        break;
//                    }
//                }
                for(int i = 0; i< temp.length(); i++){
                    if(!temp.getJSONObject(i).getString(Constance.type).equals("3")&&!temp.getJSONObject(i).getString(Constance.type).equals("4")){
                        mBrandArray.add(temp.getJSONObject(i));
                    }
                }
                mView.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setGridView();
                    }
                });
            }
            return;
        }
        OkHttpUtils.getbrandList( new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result=response.body().string();
            JSONObject jsonObject=new JSONObject(result);
            if(jsonObject!=null){
                temp = jsonObject.getJSONArray(Constance.data);

                mBrandArray = new bocang.json.JSONArray();
                LogUtils.logE("brand", temp.toString());
//                for(int i = 0; i< temp.length(); i++){
//                    if(temp.getJSONObject(i).getString(Constance.type).equals("3")){
//                        video_pos = i;
//                        break;
//                    }
//                }
                for(int i = 0; i< temp.length(); i++){
                    if(!temp.getJSONObject(i).getString(Constance.type).equals("3")&&!temp.getJSONObject(i).getString(Constance.type).equals("4")){
                    mBrandArray.add(temp.getJSONObject(i));
                    }
                }
                mView.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setGridView();
                    }
                });
            }
            }
        });
//        mNetWork.sendCategoryList(mView, new HttpListener() {
//            @Override
//            public void onSuccessListener(int what, com.alibaba.fastjson.JSONObject ans) {
//                mView.hideLoading();
//                JSONArray temp=ans.getJSONArray(Constance.data);
//                mBrandArray = new JSONArray();
//                LogUtils.logE("brand",temp.toJSONString());
//                for(int i=0;i<temp.size();i++){
////                    if(!temp.getJSONObject(i).getString(Constance.type).equals("3")){
//                        mBrandArray.add(temp.getJSONObject(i));
////                    }
//                }
//
//                setGridView();
//            }
//
//            @Override
//            public void onFailureListener(int what, com.alibaba.fastjson.JSONObject ans) {
//                mView.hideLoading();
//                if (null == mView || mView.isFinishing())
//                    return;
//                MyToast.show(mView,"数据异常！");
//            }
//        });
    }

    private void getBrandDetail() {
        if(!OkHttpUtils.hashkNewwork()){
            String result=MyShare.get(mView).getString(Constance.brandDetail+mBrandArray.getJSONObject(currentP).getString(Constance.id));
            if (result==null)return;
//            JSONObject ans=new JSONObject(result);
            com.alibaba.fastjson.JSONObject ans=JSON.parseObject(result);
            if(ans!=null){
                parseData(ans);
            }
            return;
        }
        mNetWork.sendBrandDetail(mBrandArray.getJSONObject(currentP).getString(Constance.id), mView, new HttpListener() {
            @Override
            public void onSuccessListener(int what, com.alibaba.fastjson.JSONObject ans) {
//                com.alibaba.fastjson.JSONObject result=ans;
                LogUtils.logE("branddetail",ans.toString());
                parseData(ans);
            }

            @Override
            public void onFailureListener(int what, com.alibaba.fastjson.JSONObject ans) {

            }
        });
    }

    private void parseData(com.alibaba.fastjson.JSONObject ans) {
        String html=ans.getString(Constance.content);
        if(TextUtils.isEmpty(html)){
            JSONArray data=ans.getJSONArray(Constance.data);
            if(data!=null&&data.size()>0){
                List<Data> dataList=new Gson().fromJson(data.toString(),new TypeToken<List<Data>>(){}.getType());
                if(dataList!=null&&dataList.size()>0){
                    paths = new ArrayList<>();
                    for(int i=0;i<dataList.size();i++){
                        paths.add(NetWorkConst.IMAGE_URL+dataList.get(i).getFilepath());
                    }
                    mView.banner.setPages(
                            new CBViewHolderCreator<NetworkImageHolderView>() {
                                @Override
                                public NetworkImageHolderView createHolder() {
                                    return new NetworkImageHolderView();
                                }
                            }, paths);
                    mView.banner.setPageIndicator(new int[]{R.drawable.dot_unfocuse, R.drawable.dot_focuse});

                    StringBuffer sb=new StringBuffer();
                    for(int i=0;i<dataList.size();i++){
                        sb.append("<img src=\""+NetWorkConst.IMAGE_URL+dataList.get(i).getFilepath()+"\">");
                    }
                    String htmlData=sb.toString();
                    htmlData="<p style=\"text-align:center\">"+htmlData+"</p>";
                    mView.webview.loadData(htmlData, "text/html; charset=UTF-8", null);//这种写法可以正确解析中文
                }
            }
        }else {
            html = html.replace("<img src=\"", "<img src=\"" + NetWorkConst.UR_HOST);
            html = html.replace("</p>", "");
            html = html.replace("font-size: 12px", "font-size: 20px");
            html = "<meta name=\"viewport\" content=\"width=device-width\">" + html;
            mView.webview.loadData(html, "text/html; charset=UTF-8", null);//这种写法可以正确解析中文
            LogUtils.logE("branddetail", ans.toString());
        }
    }
    private void parseData(JSONObject ans) {
        String html=ans.getString(Constance.content);
        if(TextUtils.isEmpty(html)){
            bocang.json.JSONArray data=ans.getJSONArray(Constance.data);
            if(data!=null&&data.length()>0){
                List<Data> dataList=new Gson().fromJson(data.toString(),new TypeToken<List<Data>>(){}.getType());
                if(dataList!=null&&dataList.size()>0){
                    paths = new ArrayList<>();
                    for(int i=0;i<dataList.size();i++){
                        paths.add(NetWorkConst.IMAGE_URL+dataList.get(i).getFilepath());
                    }
                    mView.banner.setPages(
                            new CBViewHolderCreator<NetworkImageHolderView>() {
                                @Override
                                public NetworkImageHolderView createHolder() {
                                    return new NetworkImageHolderView();
                                }
                            }, paths);
                    mView.banner.setPageIndicator(new int[]{R.drawable.dot_unfocuse, R.drawable.dot_focuse});

                    StringBuffer sb=new StringBuffer();
                    for(int i=0;i<dataList.size();i++){
                        sb.append("<img src=\""+NetWorkConst.IMAGE_URL+dataList.get(i).getFilepath()+"\">");
                    }
                    String htmlData=sb.toString();
                    htmlData="<p style=\"text-align:center\">"+htmlData+"</p>";
                    mView.webview.loadData(htmlData, "text/html; charset=UTF-8", null);//这种写法可以正确解析中文
                }
            }
        }else {
            html = html.replace("<img src=\"", "<img src=\"" + NetWorkConst.UR_HOST);
            html = html.replace("</p>", "");
            html = html.replace("font-size: 12px", "font-size: 20px");
            html = "<meta name=\"viewport\" content=\"width=device-width\">" + html;
            mView.webview.loadData(html, "text/html; charset=UTF-8", null);//这种写法可以正确解析中文
            LogUtils.logE("branddetail", ans.toString());
        }
    }

    private void setGridView() {
        currentP = 0;
        mView.gv_brand_bottom.setNumColumns(mBrandArray.length()>7?7:mBrandArray.length());
        adapter = new ProAdapter();
        mView.gv_brand_bottom.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mView.bg_brand.setBackground(mView.getResources().getDrawable(R.mipmap.bg_video));
        if(mView.is_video){
            mView.gv_video.setVisibility(View.VISIBLE);
            mView.webview.setVisibility(View.GONE);
            mView.ll_video.setVisibility(View.VISIBLE);
            List<BrandBean> brandBeanList=new ArrayList<>();
            for(int i=0;i<temp.length();i++){
                if (temp.getJSONObject(i).getString(Constance.type).equals("3")){
                    brandBeanList.add(new Gson().fromJson(temp.getJSONObject(i).toString(),BrandBean.class));
                }
            }
            if(brandBeanList!=null&&brandBeanList.size()>0){
                mView.adapter.replaceAll(brandBeanList);
            }

        }else {
            mView.gv_video.setVisibility(View.GONE);
            mView.webview.setVisibility(View.VISIBLE);
            mView.ll_video.setVisibility(View.GONE);
        mView.gv_brand_bottom.performItemClick(null,0,0);
        }

    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    private class ProAdapter extends BaseAdapter {
        public ProAdapter() {
        }

        @Override
        public int getCount() {
            if (null == mBrandArray)
                return 0;
            if(mBrandArray.length()>7){
                return 7;
            }
            return mBrandArray.length();
        }

        @Override
        public JSONObject getItem(int position) {
            if (null == mBrandArray)
                return null;
            return mBrandArray.getJSONObject(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ProAdapter.ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mView, R.layout.item_brand_new, null);

                holder = new ProAdapter.ViewHolder();
//                holder.ItemImage = (ImageView) convertView.findViewById(R.id.ItemImage);
                holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
//                holder.tv_desc = (TextView) convertView.findViewById(R.id.tv_desc);
                convertView.setTag(holder);
            } else {
                holder = (ProAdapter.ViewHolder) convertView.getTag();
            }

            JSONObject object = mBrandArray.getJSONObject(position);
            holder.tv_title.setText(object.getString(Constance.name));
            if(position==currentP){
                holder.tv_title.setBackgroundColor(mView.getResources().getColor(R.color.bg_brand_item_selected));
            }else {
                holder.tv_title.setBackgroundColor(mView.getResources().getColor(R.color.bg_brand_item_normal));
            }
//            holder.tv_desc.setText(object.getString(Constance.desc));
//            ImageLoader.getInstance().displayImage(NetWorkConst.UR_BRAND_URL+object.getString(Constance.path), holder.ItemImage);
            return convertView;
        }

        class ViewHolder {
            ImageView ItemImage;
            TextView tv_title;
            TextView tv_desc;
        }
    }
    class NetworkImageHolderView implements CBPageAdapter.Holder<String> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            // 你可以通过layout文件来创建，也可以像我一样用代码创建z，不一定是Image，任何控件都可以进行翻页
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, String data) {
            imageView.setImageResource(R.drawable.bg_default);
            ImageLoader.getInstance().displayImage(data, imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mView, ImageDetailActivity.class);
                    intent.putExtra(Constance.photo, paths.get(position));
                    mView.startActivity(intent);
                }
            });
        }
    }
}
