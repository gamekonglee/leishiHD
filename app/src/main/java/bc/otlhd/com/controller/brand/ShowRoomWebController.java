package bc.otlhd.com.controller.brand;

import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

import bc.otlhd.com.R;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.controller.BaseController;
import bc.otlhd.com.ui.activity.brand.ShowRoomWebActivity;
import bc.otlhd.com.ui.view.HorizontalListView;
import bc.otlhd.com.utils.net.HttpListener;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author: Jun
 * @date : 2017/5/11 9:15
 * @description :
 */
public class ShowRoomWebController extends BaseController implements AdapterView.OnItemClickListener {
    private ShowRoomWebActivity mView;
    private HorizontalListView lv;
    private List<Integer> tabList;
    private ProAdapter mAdapter;
    private List<String> namePathList;
    private List<Boolean> isClickShow;

    public ShowRoomWebController(ShowRoomWebActivity v) {
        mView = v;
        initView();
        initViewData();
    }


    private void initViewData() {
        if(AppUtils.isEmpty(mView.mData)){
            mView.initWebView(mView.path);
        }else{
            String id = mView.mData.getString(Constance.id);
            sendBrandDetail(id);
        }

//        String id = mView.mData.getString(Constance.id);
//        sendBrandDetail(id);
    }

    private void sendBrandDetail(String id) {
        mView.setShowDialog(true);
        mView.setShowDialog("正在获取数据...");
        mView.showLoading();
        mNetWork.sendBrandDetail(id, mView, new HttpListener() {
            @Override
            public void onSuccessListener(int what, JSONObject ans) {
                mView.hideLoading();

                if(ans.getJSONArray(Constance.data).size()>0){
                    String path=ans.getJSONArray(Constance.data).getJSONObject(0).getString("filepath");
                    mView.initWebView(path);
                }else{
                    MyToast.show(mView,"没有可播放的资源!");
                }


            }

            @Override
            public void onFailureListener(int what, JSONObject ans) {
                mView.hideLoading();
                MyToast.show(mView,"网络不好，请重试!");
            }
        });
    }

    //    private void initViewData() {
//        tabList = new ArrayList<>();
//        isClickShow=new ArrayList<>();
//        for (int i = 1; i < 12; i++) {
//            tabList.add(ResUtil.getResDrawable(mView,"web_"+i));
//            isClickShow.add(false);
//        }
//        namePathList=new ArrayList<>();
//        namePathList.add("file:///android_asset/effect/01/index.html");
//        namePathList.add("file:///android_asset/effect/02/index.html");
//        namePathList.add("file:///android_asset/effect/03/index.html");
//        namePathList.add("file:///android_asset/effect/04/index.html");
//        namePathList.add("file:///android_asset/effect/05/index.html");
//        namePathList.add("file:///android_asset/effect/06/index.html");
//        namePathList.add("file:///android_asset/effect/07/index.html");
//        namePathList.add("file:///android_asset/effect/08/index.html");
//        namePathList.add("file:///android_asset/effect/09/index.html");
//        namePathList.add("file:///android_asset/effect/10/index.html");
//        namePathList.add("file:///android_asset/effect/11/index.html");
//
//        mAdapter=new ProAdapter();
//        lv.setAdapter(mAdapter);
//        mAdapter.notifyDataSetChanged();
//        mView.getWebView02(namePathList.get(0));
//    }

    private void initView() {
//        lv = (HorizontalListView) mView.findViewById(R.id.lv);
//        lv.setOnItemClickListener(this);
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mView.getWebView02(namePathList.get(position));
        mAdapter.setClickShow(position);
    }

    private class ProAdapter extends BaseAdapter {


        public ProAdapter() {
        }

        @Override
        public int getCount() {
            if (null == tabList)
                return 0;
            return tabList.size();

        }

        @Override
        public Integer getItem(int position) {
            if (null == tabList)
                return 0;
            return tabList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        public void setClickShow(int position){
            for(int i=0;i<isClickShow.size();i++){
                isClickShow.set(i,false);
            }
            isClickShow.set(position,true);
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mView, R.layout.item_showroom_web, null);
                holder = new ViewHolder();
                holder.iv_web = (CircleImageView) convertView.findViewById(R.id.iv_web);
                holder.background_ll = (LinearLayout) convertView.findViewById(R.id.background_ll);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.iv_web.setImageResource(tabList.get(position));
            holder.background_ll.setBackgroundColor(isClickShow.get(position) ? mView.getResources().getColor(R.color.colorPrimaryRed) : mView.getResources().getColor(R.color.transparent));
            return convertView;
        }

        class ViewHolder {
            CircleImageView iv_web;
            LinearLayout background_ll;
        }
    }
}
