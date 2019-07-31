package bc.otlhd.com.utils.net;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by lishide on 2017/3/1.
 * <p>接受回调结果</p>
 */
public interface HttpListener {

    /**
     * 请求成功,ans必定有值
     *
     * @param ans
     */
    void onSuccessListener(int what, JSONObject ans);

    /**
     * 请求失败,ans可能有值,可能为null
     *
     * @param ans
     */
    void onFailureListener(int what, JSONObject ans);

}
