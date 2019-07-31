package bc.otlhd.com.controller.programme;

import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baiiu.filter.DropDownMenu;
import com.baiiu.filter.interfaces.OnFilterDoneListener;
import com.lib.common.hxp.view.PullToRefreshLayout;
import com.lib.common.hxp.view.PullableGridView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bc.otlhd.com.R;
import bc.otlhd.com.bean.Programme;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.cons.NetWorkConst;
import bc.otlhd.com.controller.BaseController;
import bc.otlhd.com.data.ProgrammeDao;
import bc.otlhd.com.listener.INetworkCallBack;
import bc.otlhd.com.listener.ITwoCodeListener;
import bc.otlhd.com.ui.activity.IssueApplication;
import bc.otlhd.com.ui.activity.programme.ImageDetailActivity;
import bc.otlhd.com.ui.adapter.ProgrammeDropMenuAdapter;
import bc.otlhd.com.ui.fragment.ProgrammeFragment;
import bc.otlhd.com.ui.view.popwindow.TwoCodeSharePopWindow;
import bc.otlhd.com.utils.UIUtils;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/3/10 17:49
 * @description :
 */
public class ProgrammeController extends BaseController implements INetworkCallBack, PullToRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, OnFilterDoneListener {
    private ProgrammeFragment mView;
    private DropDownMenu dropDownMenu;
    private PullToRefreshLayout mPullToRefreshLayout;
    private ProAdapter mProAdapter;
    private PullableGridView order_sv;
    public int page = 1;
    private int per_page = 20;
    public JSONArray mSchemes;
    private boolean initFilterDropDownView;
    private ProgressBar pd;
    private String mStyle;
    private String mSpace;
    private int mDeleteIndex = 0;
    private  List<Programme> mProgrammes;
    private  ProgrammeDao mProgrammeDao;
    private RelativeLayout main_ll;


    public ProgrammeController(ProgrammeFragment v) {
        mView = v;
        initView();
        initViewData();
    }


    private void initViewData() {

        initFilterDropDownView = true;
        setropownMenuData();
        if (initFilterDropDownView)//重复setMenuAdapter会报错
            initFilterDropDownView(mView.mProgrammes);
    }


    private void setropownMenuData() {
        mView.mProgrammes = new ArrayList<>();
        String[] styleArrs = UIUtils.getStringArr(R.array.style);
        String[] spaceArrs = UIUtils.getStringArr(R.array.space);
        Programme programme = new Programme();
        programme.setAttr_name(UIUtils.getString(R.string.style_name));
        programme.setAttrVal(Arrays.asList(styleArrs));
        mView.mProgrammes.add(programme);
        Programme programme2 = new Programme();
        programme2.setAttr_name(UIUtils.getString(R.string.splace_name));
        programme2.setAttrVal(Arrays.asList(spaceArrs));
        mView.mProgrammes.add(programme2);
    }

    private void initView() {
        dropDownMenu = (DropDownMenu) mView.getView().findViewById(R.id.dropDownMenu);
        mPullToRefreshLayout = ((PullToRefreshLayout) mView.getView().findViewById(R.id.mFilterContentView));
        mPullToRefreshLayout.setOnRefreshListener(this);
        order_sv = (PullableGridView) mView.getView().findViewById(R.id.gridView);
        mProAdapter = new ProAdapter();
        order_sv.setAdapter(mProAdapter);
        order_sv.setOnItemClickListener(this);
        pd = (ProgressBar) mView.getView().findViewById(R.id.pd2);

        mProgrammeDao=new ProgrammeDao(mView.getActivity());
        main_ll = (RelativeLayout) mView.getView().findViewById(R.id.main_ll);
        //        mNullView = mView.getActivity().findViewById(R.id.null_view);
        //        mNullNet = mView.getActivity().findViewById(R.id.null_net);
        //        mRefeshBtn = (Button) mNullNet.findViewById(R.id.refesh_btn);
        //        mNullNetTv = (TextView) mNullNet.findViewById(R.id.tv);
        //        mNullViewTv = (TextView) mNullView.findViewById(R.id.tv);
        //        go_btn = (Button) mNullView.findViewById(R.id.go_btn);
    }

    private List<Integer> itemPosList = new ArrayList<>();//有选中值的itemPos列表，长度为3

    private void initFilterDropDownView(List<Programme> programmes) {
        if (itemPosList.size() < programmes.size()) {
            itemPosList.add(0);
            itemPosList.add(0);
        }
        ProgrammeDropMenuAdapter dropMenuAdapter = new ProgrammeDropMenuAdapter(mView.getActivity(), programmes, itemPosList, this);
        dropDownMenu.setMenuAdapter(dropMenuAdapter);
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    public void sendFangAnList() {
        mView.setShowDialog(true);
        mView.setShowDialog("获取数据中..");
        mView.showLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mProgrammes= mProgrammeDao.getData(mStyle,mSpace,0);
                mView.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProAdapter.notifyDataSetChanged();
                        mView.hideLoading();
                    }
                });
            }
        }).start();


    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        mView.showContentView();
        //        go_btn.setVisibility(View.GONE);
        switch (requestCode) {
            case NetWorkConst.FANGANLIST:
                if (null == mView || mView.getActivity().isFinishing())
                    return;

                if (null != mPullToRefreshLayout) {
                    dismissRefesh();
                }
                JSONArray goodsList = ans.getJSONArray(Constance.fangan);
                if (AppUtils.isEmpty(goodsList)) {
                    if (page == 1) {
                        //                        mNullView.setVisibility(View.VISIBLE);
                        mPullToRefreshLayout.isMove = true;
                    }
                    mSchemes = new JSONArray();
                    dismissRefesh();
                    pd.setVisibility(View.GONE);
                    return;
                }
                //                mNullView.setVisibility(View.GONE);
                //                mNullNet.setVisibility(View.GONE);
                getDataSuccess(goodsList);
                pd.setVisibility(View.GONE);
                break;
            case NetWorkConst.FANGANDELETE:
                mView.showContentView();
                mSchemes.delete(mDeleteIndex);
                mProAdapter.notifyDataSetChanged();
                //                sendFangAnList();
                break;

        }
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        if (null == mView || mView.getActivity().isFinishing())
            return;
        if (AppUtils.isEmpty(ans)) {
            //            AppDialog.messageBox(UIUtils.getString(R.string.server_error));
            //            mNullNet.setVisibility(View.VISIBLE);
            mPullToRefreshLayout.isMove = true;
            //            mRefeshBtn.setOnClickListener(mRefeshBtnListener);
            return;
        }
        //        go_btn.setVisibility(View.GONE);
        this.page--;
        if (null != mPullToRefreshLayout) {
            dismissRefesh();
        }
    }

    private void dismissRefesh() {
        mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }

    private View.OnClickListener mRefeshBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onRefresh();
        }
    };

    public void onRefresh() {
        sendFangAnList();
        dismissRefesh();
}

    private void getDataSuccess(JSONArray array) {
        if (1 == page)
            mSchemes = array;
        else if (null != mSchemes) {
            for (int i = 0; i < array.length(); i++) {
                mSchemes.add(array.getJSONObject(i));
            }

            if (AppUtils.isEmpty(array))
                MyToast.show(mView.getActivity(), "没有更多内容了");
        }
        mProAdapter.notifyDataSetChanged();
    }

    public void ActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onFilterDone(int titlePos, int itemPos, String itemStr) {
        dropDownMenu.close();
        if (0 == itemPos)
        itemStr = mView.mProgrammes.get(titlePos).getAttr_name();
        dropDownMenu.setPositionIndicatorText(titlePos, itemStr);

        if (titlePos < itemPosList.size())
            itemPosList.remove(titlePos);
        itemPosList.add(titlePos, itemPos);
        if (titlePos == 0) {
            mStyle = mView.mProgrammes.get(titlePos).getAttrVal().get(itemPos);
        } else if (titlePos == 1) {
            mSpace = mView.mProgrammes.get(titlePos).getAttrVal().get(itemPos);
        }

        if (itemPos == 0) {
            mStyle = "";
            mSpace = "";
        }

        sendFangAnList();

    }

    public void onBackPressed() {
        dropDownMenu.close();
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        sendFangAnList();
        dismissRefesh();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        sendFangAnList();
        dismissRefesh();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //        String path= NetWorkConst.SCENE_HOST+
        //                goodses.getJSONObject(position).getJSONObject(Constance.scene).getString(Constance.original_img);
        //        mIntent=new Intent();
        //        mIntent.putExtra(Constance.SCENE, path);
        //        mView.setResult(Constance.FROMDIY02, mIntent);//告诉原来的Activity 将数据传递给它
        //        mView.finish();//一定要调用该方法 关闭新的AC 此时 老是AC才能获取到Itent里面的值
    }


    public void sendDeleteFangan(int id) {
        if(mProgrammeDao.deleteOne(id)>0){
            sendFangAnList();
            MyToast.show(mView.getActivity(), "删除方案成功!");
        }else{
            MyToast.show(mView.getActivity(),"删除方案失败!");
        }
    }


    private class ProAdapter extends BaseAdapter {
        public ProAdapter() {
        }

        @Override
        public int getCount() {
            if (null == mProgrammes)
                return 0;
            return mProgrammes.size();
        }

        @Override
        public Programme getItem(int position) {
            if (null == mProgrammes)
                return null;
            return mProgrammes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mView.getActivity(), R.layout.item_match, null);

                holder = new ViewHolder();
                holder.close_iv = (ImageView) convertView.findViewById(R.id.close_iv);
                holder.share_iv = (ImageView) convertView.findViewById(R.id.share_iv);
                holder.match_iv = (ImageView) convertView.findViewById(R.id.match_iv);
                holder.match_name_tv = (TextView) convertView.findViewById(R.id.match_name_tv);
                holder.match_name02_tv = (TextView) convertView.findViewById(R.id.match_name02_tv);
                holder.ll_share=convertView.findViewById(R.id.ll_share);
                holder.ll_delete=convertView.findViewById(R.id.ll_delete);
                holder.ll_like=convertView.findViewById(R.id.ll_like);
                holder.ll_main=convertView.findViewById(R.id.ll_view);
                int width=(UIUtils.getScreenWidth(mView.getActivity())-mView.getResources().getDimensionPixelSize(R.dimen.x90))/3;
                holder.ll_main.setLayoutParams(new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));

//                holder.horizon_listview = (HorizontalListView) convertView.findViewById(R.id.horizon_listview);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final Programme programme= mProgrammes.get(position);
//            final JSONObject jsonObject = mSchemes.getJSONObject(position);
            String style = programme.getStyle();
            String space = programme.getSpace();
//            final String path = NetWorkConst.SCENE_HOST + jsonObject.getString(Constance.path);
            final String titleName=programme.getTitle();
            holder.match_name_tv.setText(titleName);
            holder.match_name02_tv.setText(style + "/" + space);
            holder.match_iv.setImageResource(R.drawable.bg_default);

            final String imageUrl=NetWorkConst.UR_PLANIMAGE+programme.getSceenpath();
            ImageLoader.getInstance().displayImage(imageUrl, holder.match_iv);
            final String path=NetWorkConst.SHAREPLAN + "id=" +programme.getShareid();

            holder.ll_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDeleteIndex = position;
                    sendDeleteFangan(programme.getId());
                }
            });

            //分享
            holder.ll_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = "来自 " +titleName + " 方案的分享";
                    IssueApplication.sharePath=path;
                    IssueApplication.shareRemark=title;
                    TwoCodeSharePopWindow popWindow = new TwoCodeSharePopWindow(mView.getActivity(), mView.getActivity());
                    popWindow.onShow(main_ll);
                    popWindow.setListener(new ITwoCodeListener() {
                        @Override
                        public void onTwoCodeChanged(String path) {
                        }
                    });


                }
            });
            holder.match_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mView.getActivity(), ImageDetailActivity.class);
                    intent.putExtra(Constance.photo,imageUrl);
                    mView.getActivity().startActivity(intent);
                }
            });
            holder.ll_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyToast.show(mView.getActivity(),"点赞成功！");
                }
            });
            return convertView;
        }

        class ViewHolder {
            ImageView close_iv, match_iv, share_iv;
//            HorizontalListView horizon_listview;
            TextView match_name_tv, match_name02_tv;
            View ll_share,ll_delete,ll_like,ll_main;
        }
    }


}
