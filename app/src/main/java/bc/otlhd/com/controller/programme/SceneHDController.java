package bc.otlhd.com.controller.programme;

import android.content.Intent;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baiiu.filter.DropDownMenu;
import com.baiiu.filter.interfaces.OnFilterDoneListener;
import com.lib.common.hxp.view.PullToRefreshLayout;
import com.lib.common.hxp.view.PullableGridView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pgyersdk.crash.PgyCrashManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import bc.otlhd.com.R;
import bc.otlhd.com.bean.SceneBean;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.cons.NetWorkConst;
import bc.otlhd.com.controller.BaseController;
import bc.otlhd.com.data.GoodsDao;
import bc.otlhd.com.data.SceneDao;
import bc.otlhd.com.net.OkHttpUtils;
import bc.otlhd.com.ui.activity.IssueApplication;
import bc.otlhd.com.ui.activity.programme.DiyActivity;
import bc.otlhd.com.ui.adapter.SceneDropMenuAdapter;
import bc.otlhd.com.ui.fragment.SceneHDFragment;
import bc.otlhd.com.utils.FileUtil;
import bc.otlhd.com.utils.MyShare;
import bc.otlhd.com.utils.UIUtils;
import bc.otlhd.com.utils.net.HttpListener;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/3/30 17:26
 * @description :
 */
public class SceneHDController extends BaseController implements OnFilterDoneListener, PullToRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, HttpListener {
    private SceneHDFragment mView;
    private DropDownMenu dropDownMenu;
    private JSONArray sceneAllAttrs;
    private PullToRefreshLayout mPullToRefreshLayout;
    private ProAdapter mProAdapter;
    private PullableGridView order_sv;
    public int page = 1;
    private JSONArray goodses;
    private boolean initFilterDropDownView;
    private Intent mIntent;
    public String keyword;
    private ProgressBar pd;
    private String filter_attr="";
    private List<SceneBean> sceneBeans;

    public SceneHDController(SceneHDFragment v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {
        page = 1;
        initFilterDropDownView = true;
        pd.setVisibility(View.VISIBLE);
        sendSceneList();
    }

    private void initView() {
        dropDownMenu = (DropDownMenu) mView.getView().findViewById(R.id.dropDownMenu);
        mPullToRefreshLayout = ((PullToRefreshLayout) mView.getView().findViewById(R.id.mFilterContentView));
        mPullToRefreshLayout.setOnRefreshListener(this);
        order_sv = (PullableGridView) mView.getView().findViewById(R.id.gridView);
        mProAdapter = new ProAdapter();
        order_sv.setAdapter(mProAdapter);
        order_sv.setOnItemClickListener(this);
        pd = (ProgressBar) mView.getView().findViewById(R.id.pd);
    }

    private List<Integer> itemPosList = new ArrayList<>();//有选中值的itemPos列表，长度为3

    private void initFilterDropDownView(JSONArray sceneAllAttrs) {
        if (itemPosList.size() < sceneAllAttrs.size()) {
            itemPosList.add(0);
            itemPosList.add(0);
        }
        SceneDropMenuAdapter dropMenuAdapter = new SceneDropMenuAdapter(mView.getContext(), sceneAllAttrs, itemPosList, this);
        dropDownMenu.setMenuAdapter(dropMenuAdapter);
    }

    /**
     * 场景列表
     */
    public void sendSceneList() {
        if(!OkHttpUtils.hashkNewwork()){
//            sceneBeans = SceneDao.getSceneList(filter_attr,page);
            List<SceneBean> temp=SceneDao.getSceneList(filter_attr,page);
            sceneAllAttrs= JSON.parseObject(MyShare.get(mView.getActivity()).getString(Constance.scene_all_attr_list)).getJSONArray(Constance.all_attr_list);
            if(sceneAllAttrs!=null&&sceneAllAttrs.size()>0){

                for(int i=0;i<sceneAllAttrs.size();i++){
                    if(!sceneAllAttrs.getJSONObject(i).getString(Constance.filter_attr_name).equals("空间")&&
                            !sceneAllAttrs.getJSONObject(i).getString(Constance.filter_attr_name).equals("风格")){
                        sceneAllAttrs.remove(i);
                        i--;
                    }
                }
            }
            if (initFilterDropDownView)//重复setMenuAdapter会报错
                initFilterDropDownView(sceneAllAttrs);

            dismissRefesh();
            pd.setVisibility(View.GONE);
            if (AppUtils.isEmpty(temp)) {
                if (page == 1) {
                }
                MyToast.show(mView.getActivity(), "数据已经到底啦!");
                return;
            }
            getDataSuccess02(temp);
            return;
        }
        mNetWork.sendSceneList("0", page, null, null, filter_attr, mView.getActivity(),this);
    }

    private void getDataSuccess02(List<SceneBean> temp) {
        if (1 == page)
            sceneBeans = temp;
        else if (null != temp) {
            for (int i = 0; i < temp.size(); i++) {
                sceneBeans.add(temp.get(i));
            }

            if (AppUtils.isEmpty(temp))
                MyToast.show(mView.getActivity(), "没有更多内容了");
        }
        mProAdapter.notifyDataSetChanged();
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    private Integer[] sceneIds = new Integer[]{0, 0, 0};
    private String[] sceneIdsStr = new String[]{"",""};
    @Override
    public void onFilterDone(int titlePos, int itemPos, String itemStr) {
        dropDownMenu.close();
         if (0 == itemPos)
        itemStr = sceneAllAttrs.getJSONObject(titlePos).getString(Constance.filter_attr_name);
        dropDownMenu.setPositionIndicatorText(titlePos, itemStr);

        if (titlePos < itemPosList.size())
            itemPosList.remove(titlePos);
        itemPosList.add(titlePos, itemPos);



        if (AppUtils.isEmpty(sceneAllAttrs.getJSONObject(titlePos).getJSONArray(Constance.attr_list).getJSONObject(itemPos).getInteger(Constance.scene_id))) {
            sceneIds[titlePos] = 0;
            sceneIdsStr[titlePos]="";
        } else {
            int index = sceneAllAttrs.getJSONObject(titlePos).getJSONArray(Constance.attr_list).getJSONObject(itemPos).getInteger(Constance.scene_id);
            if (index == 0) {
                sceneIds[titlePos] = 999;
                sceneIdsStr[titlePos]="";
            } else {
                if(sceneAllAttrs.getJSONObject(titlePos).getString(Constance.filter_attr_name).equals("风格")){
                    sceneIdsStr[0]=sceneAllAttrs.getJSONObject(titlePos).getJSONArray(Constance.attr_list).getJSONObject(itemPos).getString(Constance.attr_value);
                }else {
                    sceneIdsStr[1]=sceneAllAttrs.getJSONObject(titlePos).getJSONArray(Constance.attr_list).getJSONObject(itemPos).getString(Constance.attr_value);
                }

                sceneIds[titlePos] = index;
            }
        }
        if(OkHttpUtils.hashkNewwork()){
            filter_attr = sceneIds[0] + "." + sceneIds[1];
        }else {
            filter_attr=sceneIdsStr[0]+sceneIdsStr[1];
        }

        if (AppUtils.isEmpty(filter_attr))
            return;
        pd.setVisibility(View.VISIBLE);
        page=1;
        sendSceneList();

    }

    public void onBackPressed() {
        dropDownMenu.close();
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        page = 1;
        initFilterDropDownView = false;
        sendSceneList();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        initFilterDropDownView = false;
        page=page+1;
        sendSceneList();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(!IssueApplication.IsRoot()){
            SystemClock.sleep(IssueApplication.SLEEP_TIME);
        }
        mIntent = new Intent(mView.getActivity(), DiyActivity.class);
        String path="";
//        String path1 = FileUtil.getSceenExternDir(goodses.getJSONObject(position).getString(Constance.path));
//        File imageFile = new File(path1);
//        if (imageFile.exists()) {
//            path = "file://" + imageFile.toString();
//        } else {
//
//        }
        if(OkHttpUtils.hashkNewwork()){
            int mId= Integer.parseInt(goodses.getJSONObject(position).getString(Constance.id));
            if(mId<=1551){
                path=NetWorkConst.URL_SCENE+goodses.getJSONObject(position).getString(Constance.path);
            }else {
                path = NetWorkConst.UR_SCENE_URL + goodses.getJSONObject(position).getString(Constance.path);
            }
        }else {
            path="file://"+new File(FileUtil.getSceenExternDir(sceneBeans.get(position).getPath())).toString();
        }


//        PgyCrashManager.reportCaughtException(mView.getActivity(),new Exception("scene"+path));
        mIntent.putExtra(Constance.path, path);
        mView.startActivity(mIntent);
    }

    private void dismissRefesh() {
        mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }

    private void getDataSuccess(JSONArray array) {
        if (1 == page)
            goodses = array;
        else if (null != goodses) {
            for (int i = 0; i < array.size(); i++) {
                goodses.add(array.getJSONObject(i));
            }

            if (AppUtils.isEmpty(array))
                MyToast.show(mView.getActivity(), "没有更多内容了");
        }
        mProAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccessListener(int what, JSONObject ans) {
        mView.hideLoading();
        pd.setVisibility(View.GONE);
        switch (what) {
            case NetWorkConst.WHAT_SCENE_LIST:
                if (null == mView || mView.getActivity().isFinishing())
                    return;

                if (null != mPullToRefreshLayout) {
                    dismissRefesh();
                }

                if (AppUtils.isEmpty(sceneAllAttrs)) {
                    sceneAllAttrs = ans.getJSONArray(Constance.all_attr_list);
                    if(sceneAllAttrs!=null&&sceneAllAttrs.size()>0){

                    for(int i=0;i<sceneAllAttrs.size();i++){
                        if(!sceneAllAttrs.getJSONObject(i).getString(Constance.filter_attr_name).equals("空间")&&
                                !sceneAllAttrs.getJSONObject(i).getString(Constance.filter_attr_name).equals("风格")){
                            sceneAllAttrs.remove(i);
                            i--;
                        }
                    }
                    }
                    if (initFilterDropDownView)//重复setMenuAdapter会报错
                        initFilterDropDownView(sceneAllAttrs);
                }

                JSONArray goodsList = ans.getJSONArray(Constance.scenelist);
                if (AppUtils.isEmpty(goodsList)) {
                    if (page == 1) {

                    }
                    MyToast.show(mView.getActivity(), "数据已经到底啦!");
                    dismissRefesh();
                    return;
                }

                getDataSuccess(goodsList);

                break;

        }
    }

    @Override
    public void onFailureListener(int what, JSONObject ans) {
        if (null == mView || mView.getActivity().isFinishing())
            return;
        if (AppUtils.isEmpty(ans)) {
            AppDialog.messageBox(UIUtils.getString(R.string.server_error));
            return;
        }
        page--;

        if (null != mPullToRefreshLayout) {
            dismissRefesh();
        }
    }
    private String imageURL = "";

     class ProAdapter extends BaseAdapter {
        public ProAdapter() {
        }

        @Override
        public int getCount() {
            if(!OkHttpUtils.hashkNewwork()){
                if (null == sceneBeans)
                    return 0;
                return sceneBeans.size();
            }else {
                if (null == goodses)
                    return 0;
                return goodses.size();
            }

        }

        @Override
        public Object getItem(int position) {
            if(!OkHttpUtils.hashkNewwork()){
            if (null == goodses)
                return null;
            return goodses.getJSONObject(position);}
            else {
                if (null == sceneBeans)
                    return null;
                return sceneBeans.get(position);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mView.getActivity(), R.layout.item_gridview_fm_scene02, null);

                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                holder.textView = (TextView) convertView.findViewById(R.id.textView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if(OkHttpUtils.hashkNewwork()) {
                JSONObject object = goodses.getJSONObject(position);
                String name = object.getString(Constance.name);
                holder.textView.setText(name);
                String path = NetWorkConst.UR_SCENE_URL + object.getString(Constance.path);
                int mId = Integer.parseInt(object.getString(Constance.id));
                if (mId <= 1551) {
                    path = NetWorkConst.URL_SCENE + goodses.getJSONObject(position).getString(Constance.path);
                } else {
                    path = NetWorkConst.UR_SCENE_URL + goodses.getJSONObject(position).getString(Constance.path);
                }
            String path1 = FileUtil.getSceenExternDir(goodses.getJSONObject(position).getString(Constance.path));
            File imageFile = new File(path1 + "!400X400.png");
            if (imageFile.exists()) {
                imageURL = "file://" + imageFile.toString();
                ImageLoader.getInstance().displayImage(imageURL, holder.imageView);
            } else {
                ImageLoader.getInstance().displayImage(path
                        + "!400X400.png", holder.imageView);
            }

            }else {
                holder.textView.setText(sceneBeans.get(position).getName());
                String path="";
                    path = "file://"+ FileUtil.getSceenExternDir(sceneBeans.get(position).getPath());
                ImageLoader.getInstance().displayImage(path
                        , holder.imageView);
            }
            return convertView;
        }

        class ViewHolder {
            ImageView imageView;
            TextView textView;


        }
    }
}
