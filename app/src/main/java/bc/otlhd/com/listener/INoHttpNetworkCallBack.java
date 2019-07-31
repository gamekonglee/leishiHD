package bc.otlhd.com.listener;


import com.alibaba.fastjson.JSONObject;

/**
 * Copyright (C) 2016
 * This file is part of the Epiphyllum B7 System.
 * <p/>
 * filename :
 * action : 网络访问回调接口
 *
 * @author : Jun
 * @version : 7.1
 * @date : 2016-09-12
 * modify :
 */
public interface INoHttpNetworkCallBack {
    /**
     * 请求成功,ans必定有值
     *
     * @param ans
     */
    void onSuccessListener(String what, JSONObject ans);

    /**
     * 请求失败,ans可能有值,可能为null
     *
     * @param ans
     */
    void onFailureListener(String what, JSONObject ans);
}
