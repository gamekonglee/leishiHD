package bc.otlhd.com.utils.net;

import android.app.Activity;

import com.alibaba.fastjson.JSONObject;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.error.NetworkError;
import com.yanzhenjie.nohttp.error.NotFoundCacheError;
import com.yanzhenjie.nohttp.error.TimeoutError;
import com.yanzhenjie.nohttp.error.URLError;
import com.yanzhenjie.nohttp.error.UnKnownHostError;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import bc.otlhd.com.R;
import bc.otlhd.com.cons.Constance;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;

/**
 * Created by lishide on 2017/3/1.
 * NoHttp 请求
 */
public class HttpResponseListener<T> implements OnResponseListener<T> {

    private Activity mActivity;
    /**
     * Request
     */
    private Request<?> mRequest;
    /**
     * 结果回调
     */
    private HttpListener callBack;
    private boolean isResult;

    /**
     * @param activity     context用来实例化dialog
     * @param request      请求对象
     * @param httpCallback 回调对象
     * @param canCancel    是否允许用户取消请求
     * @param isLoading    是否显示dialog
     */
    public HttpResponseListener(Activity activity, Request<?> request, HttpListener httpCallback,
                                boolean canCancel, boolean isLoading, boolean isResult) {
        this.mActivity = activity;
        this.mRequest = request;
        if (activity != null && isLoading) {
            //            mWaitDialog = new WaitDialog(activity);
            //            mWaitDialog.setCancelable(canCancel);
            //            mWaitDialog.setOnCancelListener(mRequest.cancel());
        }
        this.callBack = httpCallback;
        this.isResult = isResult;
    }


    @Override
    public void onStart(int what) {

    }

    /**
     * 成功回调
     */
    @Override
    public void onSucceed(int what, Response<T> response) {
        if (callBack != null) {
            // 这里判断一下http响应码，这个响应码问下你们的服务端你们的状态有几种，一般是200成功。
            // w3c标准http响应码：http://www.w3school.com.cn/tags/html_ref_httpmessages.asp
            String val = (String) response.get();
            JSONObject ans = null;
            try {
                ans = JSONObject.parseObject(val);
            } catch (Exception e) {
                MyToast.show(mActivity, R.string.error_data);
                ans=new JSONObject();
                callBack.onFailureListener(what, ans);
                return;
            }

            if (isResult) {
                if (AppUtils.getAns03(ans).equals(Constance.success)) {
                    callBack.onSuccessListener(what, ans);
                    return;
                }
                callBack.onFailureListener(what, ans);
                return;
            }

            callBack.onSuccessListener(what, ans);
        }
    }

    /**
     * 失败回调
     */
    @Override
    public void onFailed(int what, Response<T> response) {
        int error = 0;
        Exception exception = response.getException();
        if (exception instanceof NetworkError) {// 网络不好
            error = 1;
            MyToast.show(mActivity, R.string.error_please_check_network);
        } else if (exception instanceof TimeoutError) {// 请求超时
            error = 1;
            MyToast.show(mActivity, R.string.error_timeout);
        } else if (exception instanceof UnKnownHostError) {// 找不到服务器
            error = 1;
            MyToast.show(mActivity, R.string.error_not_found_server);
        } else if (exception instanceof URLError) {// URL是错的
            error = 1;
            MyToast.show(mActivity, R.string.error_url_error);
        } else if (exception instanceof NotFoundCacheError) {
            error = 1;
            // 这个异常只会在仅仅查找缓存时没有找到缓存时返回
            MyToast.show(mActivity, R.string.error_not_found_cache);
        } else {
            error = 1;
            MyToast.show(mActivity, R.string.error_unknow);
        }
        Logger.e("错误：" + exception.getMessage());

        if (error == 0){
            JSONObject ans=new JSONObject();
            ans.put("result","数据异常");
            callBack.onFailureListener(what, ans);
            return;
        }

        if (callBack != null) {
            String val = (String) response.get();
            JSONObject ans = JSONObject.parseObject(val);
            callBack.onFailureListener(what, ans);
        }

    }

    @Override
    public void onFinish(int what) {

    }
}
