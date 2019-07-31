package bc.otlhd.com.controller.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import bc.otlhd.com.R;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.cons.NetWorkConst;
import bc.otlhd.com.controller.BaseController;
import bc.otlhd.com.ui.activity.IssueApplication;
import bc.otlhd.com.ui.activity.UploadImagAtivity;
import bc.otlhd.com.ui.activity.user.CollectActivity;
import bc.otlhd.com.ui.fragment.MineFragment;
import bc.otlhd.com.utils.MyShare;
import bc.otlhd.com.utils.ShareUtil;
import bc.otlhd.com.utils.UIUtils;
import bc.otlhd.com.utils.net.HttpListener;
import bocang.json.JSONObject;
import bocang.utils.AppDialog;
import bocang.utils.CommonUtil;
import bocang.utils.DataCleanUtil;
import bocang.utils.IntentUtil;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author: Jun
 * @date : 2017/1/21 15:08
 * @description :
 */
public class MineController extends BaseController {
    private MineFragment mView;
    private CircleImageView head_cv;
    private TextView nickname_tv, cache_num_tv, version_tv,image_version_tv;
    public JSONObject mUserObject;
    public Intent mIntent;
    private String mTotalCacheSize;
    private RelativeLayout set_price_rl;

    public MineController(MineFragment v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {
        //        getTotalCacheSize();
        String localVersion = CommonUtil.localVersionName(mView.getActivity());
        version_tv.setText("V" + localVersion);
        String name = ((IssueApplication) mView.getActivity().getApplication()).mUserInfo.getString(Constance.name);
        nickname_tv.setText(name);
        int diy_price = ((IssueApplication) mView.getActivity().getApplication()).mUserInfo.getInteger(Constance.diy_price);
        if (diy_price == 0) {
            set_price_rl.setVisibility(View.GONE);
        } else {
            set_price_rl.setVisibility(View.VISIBLE);
        }
        sendPicList();

    }

    private void initView() {
        head_cv = (CircleImageView) mView.getActivity().findViewById(R.id.head_cv);
        nickname_tv = (TextView) mView.getActivity().findViewById(R.id.nickname_tv);
        cache_num_tv = (TextView) mView.getActivity().findViewById(R.id.cache_num_tv);
        version_tv = (TextView) mView.getActivity().findViewById(R.id.version_tv);
        image_version_tv = (TextView) mView.getActivity().findViewById(R.id.image_version_tv);
        set_price_rl = (RelativeLayout) mView.getActivity().findViewById(R.id.set_price_rl);
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }


    public void sendPicList(){
        mNetWork.sendPicList(mView.getActivity(), new HttpListener() {
            @Override
            public void onSuccessListener(int what, com.alibaba.fastjson.JSONObject ans) {
                int newCount=ans.getInteger(Constance.count);
                int currenCount=MyShare.get(mView.getActivity()).getInt(Constance.count);//下载类型
                if(currenCount<newCount){
                    image_version_tv.setText("有新的数据包下载，请下载!!");
                }

            }

            @Override
            public void onFailureListener(int what, com.alibaba.fastjson.JSONObject ans) {

            }
        });
    }

    /**
     * 清理缓存
     */
    public void clearCache() {
        new AlertDialog.Builder(mView.getActivity()).setTitle("清除缓存?").setMessage("确认清除您所有的缓存？")
                .setPositiveButton("清除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mTotalCacheSize.equals("0K")) {
                            AppDialog.messageBox("没有缓存可以清除!");
                            return;
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                DataCleanUtil.clearAllCache(mView.getActivity());
                                mView.getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AppDialog.messageBox("清除缓存成功!");
                                        getTotalCacheSize();
                                    }
                                });
                            }
                        }).start();

                    }
                })
                .setNegativeButton("取消", null).show();
    }

    /**
     * 查看缓存大小
     */
    public void getTotalCacheSize() {
        try {
            mTotalCacheSize = DataCleanUtil.getTotalCacheSize(mView.getActivity());
            cache_num_tv.setText(mTotalCacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    /**
    //     * 头像
    //     */
    //    public void setHead() {
    //        IntentUtil.startActivity(mView.getActivity(),PerfectMydataActivity.class,false);
    //    }


    /**
     * 我的收藏
     */
    public void setCollect() {
        IntentUtil.startActivity(mView.getActivity(), CollectActivity.class, false);

    }

    /**
     * 联系客服
     */
    public void sendCall() {
        mView.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(NetWorkConst.QQURL)));
    }

    /**
     * 分享给好友
     */
    public void getShareApp() {
        String title = "来自 " + UIUtils.getString(R.string.app_name) + " App的分享";
        ShareUtil.showShare(mView.getActivity(), title, NetWorkConst.APK_URL, NetWorkConst.SHAREIMAGE);
    }

    int i = 0;

    /**
     * 数据下载
     */
    public void downData() {
       IntentUtil.startActivity(mView.getActivity(), UploadImagAtivity.class,false);
    }
}
