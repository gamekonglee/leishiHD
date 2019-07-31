package bc.otlhd.com.controller.brand;

import android.os.Message;

import com.alibaba.fastjson.JSONObject;

import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.controller.BaseController;
import bc.otlhd.com.ui.activity.brand.BrandPlayActivity;
import bc.otlhd.com.utils.net.HttpListener;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/5/9 10:30
 * @description :
 */
public class BrandPlayController extends BaseController {
    private BrandPlayActivity mView;



    public BrandPlayController(BrandPlayActivity v){
        mView=v;
        initView();
        initViewData();

    }


    private void sendBrandDetail(String id) {
        mView.setShowDialog(true);
        mView.setShowDialog("正在获取数据...");
        mView.showLoading();
        mNetWork.sendBrandDetail(id, mView, new HttpListener() {
            @Override
            public void onSuccessListener(int what, JSONObject ans) {
                mView.hideLoading();
                try{
                    if(ans.getJSONArray(Constance.data).size()>0){
                        mView.startPlayVideo(ans.getJSONArray(Constance.data).getJSONObject(0).getString("filepath"));
                    }else{
                        MyToast.show(mView,"没有可播放的资源!");
                    }
                }catch (Exception e){

                }


            }

            @Override
            public void onFailureListener(int what, JSONObject ans) {
                mView.hideLoading();
                MyToast.show(mView,"网络不好，请重试!");
            }
        });
    }


    private void initViewData() {
//        String id = mView.mData.getString(Constance.id);
//        mView.name = mView.mData.getString(Constance.name);
        sendBrandDetail(mView.id);
    }

    private void initView() {

    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }
}
