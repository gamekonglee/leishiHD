package bc.otlhd.com.utils.net;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.SystemClock;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.Request;

import java.util.Iterator;
import java.util.Map;

import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.cons.NetWorkConst;
import bc.otlhd.com.listener.INetworkCallBack;
import bc.otlhd.com.listener.INoHttpNetworkCallBack;
import bc.otlhd.com.ui.activity.IssueApplication;
import bocang.json.JSONObject;
import bocang.net.NetJSONObject;
import bocang.net.NetJSONObject02;
import bocang.utils.AppLog;
import bocang.utils.AppUtils;

/**
 * filename :
 * action : 网络访问
 *
 * @author : Jun
 * @version : 1.0
 * @date : 2016-11-1
 * modify :
 */
public class Network {

    /**
     * 获取产品列表
     */
    public void sendGoodsList(int page, String per_page, String brand, String category, String filter_attr, String shop, String keyword, String sort_key, String sort_value, INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        params.add("page", page);
        params.add("per_page", per_page);
        params.add("brand", brand);
        params.add("category", category);
        params.add("filter_attr", filter_attr);
        params.add("shop", shop);
        params.add("keyword", keyword);
        params.add("sort_key", sort_key);
        params.add("sort_value", sort_value);
        sendRequest(params, NetWorkConst.PRODUCT, 2, 0, iNetworkCallBack);


    }

    /**
     * 获取产品列表
     */
    public void sendRecommendGoodsList(int page, int per_page, String brand, String category, String filter_attr, String shop, String keyword, String sort_key, String sort_value, INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        params.add("page", page);
        params.add("per_page", per_page);
        params.add("brand", brand);
        params.add("category", category);
        params.add("filter_attr", filter_attr);
        params.add("shop", shop);
        params.add("keyword", keyword);
        params.add("sort_key", sort_key);
        params.add("sort_value", sort_value);
        sendRequest(params, NetWorkConst.RECOMMENDPRODUCT, 2, 0, iNetworkCallBack);


    }

    /**
     * 获取产品分类
     *
     * @param iNetworkCallBack
     */
    public void sendGoodsClass(INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        sendRequest(params, NetWorkConst.GOODSCLASS, 1, 0, iNetworkCallBack);
    }

    /**
     * 登录
     *
     * @param iNetworkCallBack
     */
    public void sendLogin(String username, String password, INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        params.add("username", username);
        params.add("password", password);
        params.add("state", "yes");
        sendRequest(params, NetWorkConst.LOGIN, 2, 0, iNetworkCallBack);
    }

    /**
     * 广告
     */
    public void sendBanner(INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        sendRequest(params, NetWorkConst.BANNER, 2, 0, iNetworkCallBack);
    }



    /**
     * 重置密码
     */
    public void sendUpdatePwd(String mobile, String password, String code, INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        params.add("mobile", mobile);
        params.add("password", password);
        params.add("code", code);
        sendRequest(params, NetWorkConst.RESET, 2, 0, iNetworkCallBack);
    }

    /**
     * 获取验证码
     *
     * @param mobile
     * @param iNetworkCallBack
     */
    public void sendRequestYZM(String mobile, INetworkCallBack iNetworkCallBack) {


        JSONObject params = new JSONObject();
        params.add("mobile", mobile);
        sendRequest(params, NetWorkConst.VERIFICATIONCOE, 2, 0, iNetworkCallBack);
    }

    /**
     * 产品类别
     */
    public void sendGoodsType(int page, int per_page, String category, String shop, INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        params.add("page", page);
        params.add("per_page", per_page);
        params.add("category", category);
        params.add("shop", shop);
        sendRequest(params, NetWorkConst.CATEGORY, 2, 0, iNetworkCallBack);
    }

    /**
     * 修改用户信息
     *
     * @param values
     * @param nickname
     * @param gender
     * @param iNetworkCallBack
     */
    public void sendUpdateUser(String values, String nickname, String birthday, int gender, INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        params.add("values", values);
        params.add("nickname", nickname);
        params.add("gender", gender);
        params.add("birthday", birthday);
        sendRequest(params, NetWorkConst.UPDATEPROFILE, 2, 0, iNetworkCallBack);
    }

    /**
     * 用户信息
     *
     * @param iNetworkCallBack
     */
    public void sendUser(INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        sendRequest(params, NetWorkConst.PROFILE, 2, 0, iNetworkCallBack);
    }

    /**
     * 收藏列表
     */
    public void sendCollectProduct(int page, int per_page, INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        params.add("page", page);
        params.add("per_page", per_page);
        sendRequest(params, NetWorkConst.LIKEDPRODUCT, 2, 0, iNetworkCallBack);
    }

    /**
     * 取消收藏
     */
    public void sendUnLikeCollect(String productId, INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        params.add("product", productId);
        sendRequest(params, NetWorkConst.ULIKEDPRODUCT, 2, 0, iNetworkCallBack);
    }

    /**
     * 添加取消收藏
     */
    public void sendAddLikeCollect(String productId, INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        params.add("product", productId);
        sendRequest(params, NetWorkConst.ADDLIKEDPRODUCT, 2, 0, iNetworkCallBack);
    }

    /**
     * 订单列表
     */
    public void sendorderList(int page, int per_page, String status, INoHttpNetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        params.add("page", page);
        params.add("per_page", per_page);
        if (!status.equals("-1")) {
            params.add("status", status);
        }
        sendRequest02(params, NetWorkConst.ORDERLIST, 2, iNetworkCallBack);
    }

    /**
     * 产品详情
     */
    public void sendProductDetail(int product, INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        params.add("product", product);
        sendRequest(params, NetWorkConst.PRODUCTDETAIL, 2, 0, iNetworkCallBack);
    }

    /**
     * 产品详情02
     */
    public void sendProductDetail02(String product, INoHttpNetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        params.add("product", product);
        sendRequest02(params, NetWorkConst.PRODUCTDETAIL, 2, iNetworkCallBack);
    }

    /**
     * 购物车列表
     *
     * @param iNetworkCallBack
     */
    public void sendShoppingCart(INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        sendRequest(params, NetWorkConst.GETCART, 2, 0, iNetworkCallBack);
    }

    /**
     * 场景列表
     */
    public void sendSceneList(int page, String per_page, String keyword, INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        params.add("page", page);
        params.add("per_page", per_page);
        params.add("keyword", keyword);
        sendRequest(params, NetWorkConst.SCENELIST, 2, 0, iNetworkCallBack);
    }

    /**
     * 加入购物车
     */
    public void sendShoppingCart(String product, String property, int amount, INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        params.add("product", product);
        params.add("property", property);
        params.add("amount", amount);
        sendRequest(params, NetWorkConst.ADDCART, 2, 0, iNetworkCallBack);
    }

    /**
     * 删除购物车
     */
    public void sendDeleteCart(String good, INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        params.add("good", good);
        sendRequest(params, NetWorkConst.DeleteCART, 2, 0, iNetworkCallBack);
    }

    /**
     * 修改购物车
     */
    public void sendUpdateCart(String good, String amount, INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        params.add("good", good);
        params.add("amount", amount);
        sendRequest(params, NetWorkConst.UpdateCART, 2, 0, iNetworkCallBack);
    }

    /**
     * 获取收货地址
     */
    public void sendAddressList(INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        sendRequest(params, NetWorkConst.CONSIGNEELIST, 2, 0, iNetworkCallBack);
    }

    /**
     * 添加收货地址
     */
    public void sendAddAddress(String name, String mobile, String tel, String zip_code, String region, String address, String identity, INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        params.add("name", name);
        params.add("mobile", mobile);
        params.add("tel", tel);
        params.add("zip_code", zip_code);
        params.add("region", region);
        params.add("address", address);
        params.add("identity", identity);
        sendRequest(params, NetWorkConst.CONSIGNEEADD, 2, 0, iNetworkCallBack);
    }

    public void sendAddressList1(INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        sendRequest(params, NetWorkConst.ADDRESSlIST, 2, 0, iNetworkCallBack);
    }

    /**
     * 删除收货地址
     */
    public void sendDeleteAddress(String consignee, INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        params.add("consignee", consignee);
        sendRequest(params, NetWorkConst.CONSIGNEEDELETE, 2, 0, iNetworkCallBack);
    }

    /**
     * 修改收货地址
     */
    public void sendUpdateAddress(String consignee, String name, String mobile, String tel, String zip_code, String region, String address, String identity, INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        params.add("consignee", consignee);
        params.add("name", name);
        params.add("mobile", mobile);
        params.add("tel", tel);
        params.add("zip_code", zip_code);
        params.add("region", region);
        params.add("address", address);
        params.add("identity", identity);
        sendRequest(params, NetWorkConst.CONSIGNEEUPDATE, 2, 0, iNetworkCallBack);
    }

    /**
     * 默认收货地址
     */
    public void sendDefaultAddress(String consignee, INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        params.add("consignee", consignee);
        sendRequest(params, NetWorkConst.CONSIGNEEDEFAULT, 2, 0, iNetworkCallBack);
    }

    /**
     * 结算购物车
     */
    public void sendCheckOutCart(String consignee, String shipping, String logistics_tel, String logistics_address, String cart_good_id, INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        params.add("consignee", consignee);
        params.add("shipping_name", shipping);
        params.add("logistics_tel", logistics_tel);
        params.add("logistics_address", logistics_address);
        params.add("cart_good_id", cart_good_id);
        sendRequest(params, NetWorkConst.CheckOutCart, 1, 0, iNetworkCallBack);
    }


    /**
     * 场景分类
     */
    public void sendSceneType(INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        sendRequest(params, NetWorkConst.SCENECATEGORY, 2, 0, iNetworkCallBack);
    }

    /**
     * 获取物流列表
     *
     * @param iNetworkCallBack
     */
    public void sendlogistics(INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        sendRequest(params, NetWorkConst.LOGISTICS, 2, 0, iNetworkCallBack);
    }

    /**
     * 帅选列表
     */
    public void sendAttrList(String index, INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        params.add("index", index);
        sendRequest(params, NetWorkConst.ATTRLIST, 2, 0, iNetworkCallBack);
    }

    /**
     * 附近商家列表
     */
    public void sendNearbyList(String lng, String lat, INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        params.add("page", 1);
        params.add("per_page", 1);
        params.add("lng", lng);
        params.add("lat", lat);
        params.add("radius", 5000);
        sendRequest(params, NetWorkConst.NEARBYLIST, 2, 0, iNetworkCallBack);
    }

    /**
     * 最新动态
     *
     * @param page
     * @param per_page
     * @param iNetworkCallBack
     */
    public void sendArticle(int page, int per_page, INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
//        params.add("id", 2);
        params.add("page", page);
        params.add("per_page", per_page);
        sendRequest(params, NetWorkConst.ARTICLELIST, 2, 0, iNetworkCallBack);

    }

    /**
     * 新闻分类
     *
     * @param page
     * @param per_page
     * @param iNetworkCallBack
     */
    public void sendNewsClass(int page, int per_page, Activity view,HttpListener iNetworkCallBack) {
        JSONObject params = new JSONObject();
//        params.add("id", 2);
//        params.add("page", page);
//        params.add("per_page", per_page);
        sendRequest(NetWorkConst.NEWSCLASS, params, 0, RequestMethod.GET,NetWorkConst.NEWSCLASS_INT,false, view, iNetworkCallBack);
//        sendRequest(params,NetWorkConst.NEWSCLASS,1,0,iNetworkCallBack);
    }
    /**
     * 消息中心
     */
    public void sendNotice(int page, int per_page, INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        params.add("page", page);
        params.add("per_page", per_page);
        sendRequest(params, NetWorkConst.NOTICELIST, 2, 0, iNetworkCallBack);
    }

    /**
     * 方案列表
     */
    public void sendFangAnList(int page, int per_page, String style, String space, INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        params.add("page", page);
        params.add("per_page", per_page);
        params.add("style", style);
        params.add("space", space);
        sendRequest(params, NetWorkConst.FANGANLIST, 2, 0, iNetworkCallBack);
    }

    /**
     * 删除方案
     */
    public void sendDeleteFangan(int id, INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        params.add("id", id);
        sendRequest(params, NetWorkConst.FANGANDELETE, 2, 0, iNetworkCallBack);
    }

    /**
     * 取消订单
     */
    public void sendOrderCancel(String order, String reason, INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        params.add("order", order);
        params.add("reason", reason);
        sendRequest(params, NetWorkConst.ORDERCANCEL, 1, 0, iNetworkCallBack);
    }

    /**
     * 支付订单
     */
    public void sendPayment(String order, String code, INoHttpNetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        params.add("order", order);
        params.add("code", code);
        sendRequest02(params, NetWorkConst.PAYMENT, 2, iNetworkCallBack);
    }

    /**
     * 支付参数信息
     */
    public void sendPaymentInfo(INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        sendRequest(params, NetWorkConst.PAYMENTINFO, 1, 0, iNetworkCallBack);
    }

    /**
     * 客服
     *
     * @param iNetworkCallBack
     */
    public void sendCustom(INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        sendRequest(params, NetWorkConst.CUSTOM, 1, 1, iNetworkCallBack);
    }

    /**
     * 获取版本号
     *
     * @param iNetworkCallBack
     */
    public void sendVersion(INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        sendRequest(params, NetWorkConst.VERSION_URL, 1, 1, iNetworkCallBack);
    }

    /**
     * 获取自定义场景列表
     */
    public void sendMineDiy(String token, INetworkCallBack iNetworkCallBack) {
        JSONObject params = new JSONObject();
        sendRequest(params, NetWorkConst.QEAPI + token, 1, 1, iNetworkCallBack);
    }



    //-----------------------------------------------------------------------------------------------------------------------------

    /**
     * 获取用户信息HD
     *
     * @param signid
     * @param view
     * @param callBack
     */
    public void sendUser(String signid, Activity view, HttpListener callBack) {
        JSONObject params = new JSONObject();
        params.add("signid", signid);
        params.add("status","1");
        sendRequest(NetWorkConst.UR_USER_INFO, params, 1, RequestMethod.POST, NetWorkConst.WHAT_USER_INFO, false, view, callBack);
    }

    /**
     * 注册HD
     */
    public void sendRegiest(int type,String name,String address,String phone,String oc_id,String signid,String invite_code, Activity view, HttpListener callBack) {
        JSONObject params = new JSONObject();
        params.add("type", type);
        params.add("name", name);
        params.add("address", address);
        params.add("phone", phone);
        params.add("signid", signid);
        params.add("oc_id",""+oc_id);
        params.add("invite_code", invite_code);
        sendRequest(NetWorkConst.UR_USER_ADD, params, 0, RequestMethod.POST, NetWorkConst.WHAT_USER_ADD, false, view, callBack);
    }

    /**
     * 商品信息列表HD
     */
    public void sendGoodsList(String c_id,int page,int okcat_id,String keywords,String type,String filter_attr, boolean isCanche,int size,String on_sale,Activity view, HttpListener callBack) {
        JSONObject params = new JSONObject();
        params.add("c_id", c_id);
        params.add("page", page);
        params.add("okcat_id", okcat_id);
        params.add("keywords", keywords);
        params.add("type", type);
        params.add("filter_attr", filter_attr);
        params.add("size", size);
        params.add("on_sale",on_sale);
        if(isCanche){
            sendRequest(NetWorkConst.UR_GOODS_LIST, params, 2, RequestMethod.POST,NetWorkConst.WHAT_GOODS_LIST,false, view, callBack);
        }else{
            sendRequest(NetWorkConst.UR_GOODS_LIST, params, 1, RequestMethod.POST,NetWorkConst.WHAT_GOODS_LIST,false, view, callBack);
        }

    }

    /**
     * 场景列表HD
     */
    public void sendSceneList(String c_id,int page,String keywords,String type,String filter_attr, Activity view, HttpListener callBack) {
        JSONObject params = new JSONObject();
        params.add("c_id", c_id);
        params.add("page", page);
        params.add("keywords", keywords);
        params.add("type", type);
        params.add("filter_attr", filter_attr);
        sendRequest(NetWorkConst.UR_SCENE_LIST, params, 2, RequestMethod.POST, NetWorkConst.WHAT_SCENE_LIST, false, view, callBack);
    }


    /**
     * 产品详情HD
     */
    public void sendProductDetail(String id, Activity view, HttpListener callBack) {
        JSONObject params = new JSONObject();
        params.add("id", id);
        sendRequest(NetWorkConst.UR_GOODS_INFO, params, 1, RequestMethod.POST, NetWorkConst.WHAT_GOODS_INFO, false, view, callBack);
    }

    /**
     * 提交订单
     */
    public  void sendOrder(String order,String product, Activity view, HttpListener callBack) {
        JSONObject params = new JSONObject();
        params.add("order", order);
        params.add("product", product);
        sendRequest(NetWorkConst.UR_UPLOAD_GOODS, params, 0, RequestMethod.POST, NetWorkConst.WHAT_UPLOAD_GOODS, false, view, callBack);
    }

    /**
     * 提交自定义产品价格资料
     */
    public void sendUploadPrice(String uid,String content, Activity view, HttpListener callBack) {
        JSONObject params = new JSONObject();
        params.add("uid", uid);
        params.add("content", content);
        sendRequest(NetWorkConst.UR_UPLOAD_GOODSPRICES, params, 0, RequestMethod.POST, NetWorkConst.WHAT_UPLOAD_GOODSPRICES, false, view, callBack);
    }

    /**
     * 获取自定义产品价格资料
     */
    public void sendReadPrice(String uid, Activity view, HttpListener callBack) {
        JSONObject params = new JSONObject();
        params.add("uid", uid);
        sendRequest(NetWorkConst.UR_GET_GOODSPRICES, params, 0, RequestMethod.POST, NetWorkConst.WHAT_GET_GOODSPRICES, false, view, callBack);

    }

    /**
     * 图片名称
     */
    public void sendPicList(Activity view, HttpListener callBack) {
        JSONObject params = new JSONObject();
        sendRequest(NetWorkConst.GETPICLIST, params, 0, RequestMethod.POST, NetWorkConst.WHAT_GETPICLIST, false, view, callBack);
    }


    /**
     * 品牌列表
     * @param view
     * @param callBack
     */
    public void sendCategoryList(Activity view, HttpListener callBack) {
        JSONObject params = new JSONObject();
        params.add("key", 1);
        sendRequest(NetWorkConst.GETCATEGRORYLIST, params,  0, RequestMethod.POST, NetWorkConst.WHAT_GETCATEGRORYLIST, false, view, callBack);
    }

    /**
     * 品牌详情
     * @param id
     * @param view
     * @param callBack
     */
    public void sendBrandDetail(String id,Activity view, HttpListener callBack) {
        JSONObject params = new JSONObject();
        params.add("id", id);
        sendRequest(NetWorkConst.BRAND_DETAIL, params,  0, RequestMethod.POST, NetWorkConst.WHAT_BRAND_DETAIL, false, view, callBack);
    }



    /**
     * NOHTTP请求
     *
     * @param url         网络地址
     * @param params      请求参数
     * @param requestType 请求类型
     * @param view        activity
     * @param callBack
     */
    private void sendRequest(String url, JSONObject params, int cancheType, RequestMethod requestType,int what,boolean isResult, Activity view, HttpListener callBack) {
        if(IssueApplication.IsSlow()){
        SystemClock.sleep(2000);
        }

        Request<String> request = null;
        request = NoHttp.createStringRequest(url, requestType);
        //传递参数
        Map<String, Object> data = params.getAll();
        Iterator<Map.Entry<String, Object>> itr = data.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) itr.next();
            if (!AppUtils.isEmpty(entry)) {
                if (AppUtils.isEmpty(entry.getValue())) {
                    request.add(entry.getKey(), "");
                } else {
                    request.add(entry.getKey(), entry.getValue().toString());
                }

            }
        }

        request.setCancelSign(object);

        if (cancheType==1) {
            request.setCacheKey(url+params.toString());//
            // 这里的key是缓存数据的主键，默认是url，使用的时候要保证全局唯一，否则会被其他相同url数据覆盖。
            request.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);
            // 添加到请求队列
            CallServer.getInstance().add(view, what, request, callBack, false, true,isResult);
        } else if (cancheType==0){
            // 添加到请求队列
            CallServer.getInstance().add(view, what, request, callBack, true, true,isResult);
        }else if(cancheType==2){
            request.setCacheKey(url+params.toString());//
            // 这里的key是缓存数据的主键，默认是url，使用的时候要保证全局唯一，否则会被其他相同url数据覆盖。
            request.setCacheMode(CacheMode.NONE_CACHE_REQUEST_NETWORK);
            // 添加到请求队列
            CallServer.getInstance().add(view, what, request, callBack, false, true,isResult);
        }
    }


    /**
     * 用来标记取消
     */
    private static Object object = new Object();

    //-----------------------------------------------------------------------------------------------------------------------------

    /**
     * 发送请求
     *
     * @param params
     */
    private void sendRequest(JSONObject params, final String urlpath, final int type, final int style, final INetworkCallBack callBack) {

        if (!AppUtils.checkNetwork()) {

            callBack.onFailureListener(urlpath, null);
            return;
        }
        if(IssueApplication.IsSlow()){
            SystemClock.sleep(2000);
        }
        NetJSONObject net = new NetJSONObject(style, new NetJSONObject.Callback() {
            @Override
            public void onCallback(int style, JSONObject ans, String sem) {
//                AppLog.info(ans);
                // 1:没返回state,2:有返回state
                switch (type) {
                    case 1:
                        if (!AppUtils.isEmpty(ans)) {
                            callBack.onSuccessListener(urlpath, ans);
                        } else {
                            callBack.onFailureListener(urlpath, null);
                        }
                        break;
                    case 2:
                        if (sem == null) {
                            if (AppUtils.getAns(ans).equals(Constance.OK)) {
                                callBack.onSuccessListener(urlpath, ans);
                            } else {
                                callBack.onFailureListener(urlpath, ans);
                            }
                        } else {
                            callBack.onFailureListener(urlpath, ans);
                        }
                        break;
                }

            }
        });

        //传递参数
        Map<String, Object> data = params.getAll();
        Iterator<Map.Entry<String, Object>> itr = data.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) itr.next();
            if (!AppUtils.isEmpty(entry)) {
                if (AppUtils.isEmpty(entry.getValue())) {
                    net.addParameter(entry.getKey(), "");
                } else {
                    net.addParameter(entry.getKey(), entry.getValue().toString());
                }

            }
        }

        //传递地址
        net.addURLPath(urlpath);

        net.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /**
     * 发送请求
     *
     * @param params
     */
    private void sendRequest02(JSONObject params, final String urlpath, final int type, final INoHttpNetworkCallBack callBack) {

        if (!AppUtils.checkNetwork()) {

            callBack.onFailureListener(urlpath, null);
            return;
        }

        NetJSONObject02 net = new NetJSONObject02(0, new NetJSONObject02.Callback() {
            @Override
            public void onCallback(int style, com.alibaba.fastjson.JSONObject ans, String sem) {
                AppLog.info(ans);
                // 1:没返回state,2:有返回state
                if (sem == null) {
                    if (AppUtils.getAns02(ans).equals(Constance.OK)) {
                        callBack.onSuccessListener(urlpath, ans);
                    } else {
                        callBack.onFailureListener(urlpath, ans);
                    }
                } else {
                    callBack.onFailureListener(urlpath, ans);
                }

            }
        });

        //传递参数
        Map<String, Object> data = params.getAll();
        Iterator<Map.Entry<String, Object>> itr = data.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) itr.next();
            if (!AppUtils.isEmpty(entry)) {
                if (AppUtils.isEmpty(entry.getValue())) {
                    net.addParameter(entry.getKey(), "");
                } else {
                    net.addParameter(entry.getKey(), entry.getValue().toString());
                }

            }
        }

        //传递地址
        net.addURLPath(urlpath);

        net.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }



}
