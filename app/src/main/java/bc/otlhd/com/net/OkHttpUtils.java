package bc.otlhd.com.net;


import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import bc.otlhd.com.cons.NetWorkConst;
import bocang.json.JSONObject;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by gamekonglee on 2018/9/12.
 */

public class OkHttpUtils {

    /**
     * 检查网络是否联网
     */
    public static boolean hashkNewwork() {

        boolean hasNetwork = NetworkStateManager.instance().isNetworkConnected();
        if (!hasNetwork) {
//            Toast.makeText(IssueApplication.getInstance(), "您的网络连接已中断", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //获取这个SSLSocketFactory
    public static SSLSocketFactory getSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, getTrustManager(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //获取TrustManager
    private static TrustManager[] getTrustManager() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        };
        return trustAllCerts;
    }
    //获取HostnameVerifier
    public static HostnameVerifier getHostnameVerifier() {
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        };
        return hostnameVerifier;
    }

    public static OkHttpClient getOkHttpInstance(){
        return  new OkHttpClient.Builder()
                .sslSocketFactory(getSSLSocketFactory())
                .hostnameVerifier(getHostnameVerifier())
                .build();
    }
    public static void getNewsClass( Callback callback){

        OkHttpClient okHttpClient=getOkHttpInstance();
        RequestBody requestBody= new FormBody.Builder()
                .build();
        Request request=new Request.Builder().post(requestBody).url(NetWorkConst.NEWSCLASS).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void getNews(String s, Callback callback) {
        OkHttpClient okHttpClient=getOkHttpInstance();
        RequestBody requestBody= new FormBody.Builder()
                .add("id",s)
                .build();
        Request request=new Request.Builder().post(requestBody).url(NetWorkConst.NEWSTLIST).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void getGoodsList(String c_id,String page,String okcat_id,String keywords,String type,String filter_attr,String size,Callback callback) {

        OkHttpClient okHttpClient=getOkHttpInstance();
        RequestBody requestBody= new FormBody.Builder()
                .add("c_id", c_id)
                .add("page", page)
                .add("okcat_id", okcat_id)
                .add("keywords", keywords)
                .add("type", type)
                .add("filter_attr", filter_attr)
                .add("size", size)
                .build();
        Request request=new Request.Builder().post(requestBody).url(NetWorkConst.UR_GOODS_LIST).build();
        okHttpClient.newCall(request).enqueue(callback);

    }

    public static void getAdPic(Callback callback) {
        OkHttpClient okHttpClient=getOkHttpInstance();
        RequestBody requestBody= new FormBody.Builder()
                .add("id", "6")
                .build();
        Request request=new Request.Builder().post(requestBody).url(NetWorkConst.UR_AD).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void getBrandDetail(String id, Callback callback) {
        OkHttpClient okHttpClient=getOkHttpInstance();
        RequestBody requestBody= new FormBody.Builder()
                .add("id", id)
                .build();
        Request request=new Request.Builder().post(requestBody).url(NetWorkConst.BRAND_DETAIL).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void getbrandList(  Callback callback) {
        OkHttpClient okHttpClient=getOkHttpInstance();
        RequestBody requestBody= new FormBody.Builder()
                .add("key","1")
                .build();
        Request request=new Request.Builder().post(requestBody).url(NetWorkConst.GETCATEGRORYLIST).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void getNewsDetail(String id, Callback callback) {
        OkHttpClient okHttpClient=getOkHttpInstance();
        RequestBody requestBody= new FormBody.Builder()
                .add("id",id)
                .build();
        Request request=new Request.Builder().post(requestBody).url(NetWorkConst.NEWSDETAIL).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void getSceneList(String c_id, int page, String keywords, String type, String filter_attr, Callback callBack) {
        OkHttpClient okHttpClient=getOkHttpInstance();
        RequestBody requestBody= new FormBody.Builder()
        .add("c_id", c_id)
        .add("page", ""+page)
        .add("keywords", keywords)
        .add("type", type)
        .add("filter_attr", filter_attr)
                .build();
        Request request=new Request.Builder().post(requestBody).url(NetWorkConst.UR_SCENE_LIST).build();
        okHttpClient.newCall(request).enqueue(callBack);
    }

    public static void getDataVersion(Callback callback) {
        OkHttpClient okHttpClient=getOkHttpInstance();
        RequestBody requestBody= new FormBody.Builder()
                .build();
        Request request=new Request.Builder().post(requestBody).url(NetWorkConst.VERSION_NEW_URL).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void getOcList(Callback callback) {
        OkHttpClient okHttpClient=getOkHttpInstance();
        RequestBody requestBody= new FormBody.Builder()
                .build();
        Request request=new Request.Builder().post(requestBody).url(NetWorkConst.VERSION_OC_URL).build();
        okHttpClient.newCall(request).enqueue(callback);

    }

//    public static void getGoodsCategory(String pid, Callback callback){
//
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        RequestBody requestBody= new FormBody.Builder()
//                .add("parent_id",pid)
//                .build();
//        Request request=new Request.Builder().post(requestBody).url(NetWorkConst.URL_CLASSIFY).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void getGoodsList(String filterUlr, String mCategoriesId, String mSortKey, String mSortValue, String bandid, int page, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//
//        String url=NetWorkConst.URL_PRODUCT+"&id="+mCategoriesId+"&orderby="+mSortKey+"&orderdesc="+mSortValue+"&brand_id="+bandid+"&p="+page;
//        if(filterUlr!=null&& !TextUtils.isEmpty(filterUlr)){
//            url=filterUlr;
//        }
//        Log.e("url:",url);
//        Request request=new Request.Builder().get().url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void getGoodsDetail(String userid, String token, int mProductId, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        if(userid==null)userid="";
//        if(token==null)token="";
//        RequestBody requestBody= new FormBody.Builder()
//                .add("user_id",userid)
//                .add("token",token)
//                .add("id",mProductId+"")
//                .build();
//        String url=NetWorkConst.URL_PRODUCT_DETAIL;
//        Request request=new Request.Builder().post(requestBody).url(url).build();
//
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//    public static void getGoodsContent(int id, Callback callback){
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        String url=NetWorkConst.URL_PRODUCT_CONTENT+"&id="+id;
//        Request request=new Request.Builder().get().url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void getHomePage(Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        String url=NetWorkConst.URL_HOME_PAGE;
//        Request request=new Request.Builder().get().url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void login(String phone, String pwd, int loginByPwd, String pushId, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        RequestBody requestBody= new FormBody.Builder()
//                .add("username",phone+"")
//                .add("password",pwd+"")
//                .add("unique_id","bocang")
//                .add("push_id",pushId)
//                .build();
//        String url=NetWorkConst.URL_LOGIN;
//        Request request=new Request.Builder().post(requestBody).url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void sendSms(String mobile, Callback callback) {
////        OkHttpClient okHttpClient=getOkHttpInstance();
////        RequestBody requestBody= new FormBody.Builder()
////                .add("mobile",mobile+"")
////                .add("unique_id","bocang")
////                .build();
////        String url=NetWorkConst.URL_SEND_SMS+mobile;
////        Request request=new Request.Builder().post(requestBody).url(url).build();
////        okHttpClient.newCall(request).enqueue(callback);
//
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        String url=NetWorkConst.URL_SEND_SMS+mobile;
//        Request request=new Request.Builder().get().url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//
//
//    }
//
//    public static void register(String mPhone, String pwd, String affirmPwd, String code, String regId, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        RequestBody requestBody= new FormBody.Builder()
//                .add("username",mPhone+"")
//                .add("password",pwd+"")
//                .add("password2",affirmPwd+"")
//                .add("code",code+"")
//                .add("unique_id","bocang")
//                .add("is_bind","0")
//                .add("push_id",regId)
//                .build();
//        String url=NetWorkConst.URL_REGISTER;
//        Request request=new Request.Builder().post(requestBody).url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void addToShopCart(String id, String property, int count, String user_id, String token, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        RequestBody requestBody= new FormBody.Builder()
//                .add("user_id",user_id+"")
//                .add("token",token+"")
//                .add("goods_id",id+"")
//                .add("goods_num",count+"")
//                .add("item_id",property+"")
//                .build();
//        String url=NetWorkConst.URL_SHOPCART_ADD;
//        Request request=new Request.Builder().post(requestBody).url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void getShoppingCart(String user_id, String token, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        RequestBody requestBody= new FormBody.Builder()
//                .add("user_id",user_id+"")
//                .add("token",token+"")
//                .add("unique_id","bocang")
//                .build();
//        String url=NetWorkConst.URL_SHOPCART_LIST;
//        Request request=new Request.Builder().post(requestBody).url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void forgetPwd(String account, String verify, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        RequestBody requestBody= new FormBody.Builder()
//                .add("account",account+"")
////                .add("capache",verify+"")
//                .build();
//        String url=NetWorkConst.URL_PWD_FORGET_ONE;
//        Request request=new Request.Builder().post(requestBody).url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//    public static void getYzm(Callback callback){
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        String url=NetWorkConst.URL_SEND_VERIFY_IMG;
//        Request request=new Request.Builder().get().url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void forgetPwd2(String mobile, String check_code, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        RequestBody requestBody= new FormBody.Builder()
//                .add("mobile",mobile+"")
//                .add("check_code",check_code)
//                .add("unique_id","bocang")
////                .add("capache",verify+"")
//                .build();
//        String url=NetWorkConst.URL_PWD_FORGET_TWO;
//        Request request=new Request.Builder().post(requestBody).url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//    public static void forgetPwd3(String mobile, String password, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        RequestBody requestBody= new FormBody.Builder()
//                .add("mobile",mobile+"")
//                .add("password",password)
//                .add("unique_id","bocang")
////                .add("capache",verify+"")
//                .build();
//        String url=NetWorkConst.URL_PWD_FORGET_THREE;
//        Request request=new Request.Builder().post(requestBody).url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void getOrderConfirm(String user_id, String token, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        RequestBody requestBody= new FormBody.Builder()
//                .add("user_id",user_id+"")
//                .add("token",token)
//                .build();
//        String url=NetWorkConst.URL_ORDER_CONFIRM;
//        Request request=new Request.Builder().post(requestBody).url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void addAddress(String user_id, String token, String consgin, String mProvince, String mCity, String mCountry, String address, String mobile, int is_default, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        RequestBody requestBody= new FormBody.Builder()
//                .add("user_id",user_id+"")
//                .add("token",token)
//                .add("consignee",consgin)
//                .add("mobile",mobile)
//                .add("address",address)
//                .add("province",mProvince)
//                .add("is_default",is_default+"")
//                .add("city",mCity)
//                .add("district",mCountry)
//                .build();
//        String url=NetWorkConst.URL_ADDRESS_ADD;
//        Request request=new Request.Builder().post(requestBody).url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//    public static void updateAddress(String user_id, String token, String aid, String consgin, String mProvince, String mCity, String mCountry, String address, String mobile, int isdefault, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        RequestBody requestBody= new FormBody.Builder()
//                .add("user_id",user_id+"")
//                .add("token",token)
//                .add("address_id",aid)
//                .add("consignee",consgin)
//                .add("mobile",mobile)
//                .add("address",address)
//                .add("province",mProvince)
//                .add("is_default",isdefault+"")
//                .add("city",mCity)
//                .add("district",mCountry)
//                .build();
//        String url=NetWorkConst.URL_ADDRESS_ADD;
//        Request request=new Request.Builder().post(requestBody).url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void submitOrder(String user_id, String token, String act, String address_id, String shipping_code, String couponTypeSelect, String coupon_id, String couponCode, String invoice_title, String taxpayer, String user_note, Callback callback) {
//
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        RequestBody requestBody= new FormBody.Builder()
//                .add("user_id",user_id+"")
//                .add("token",token)
//                .add("act",act)
//                .add("address_id",address_id)
//                .add("shipping_code",shipping_code)
//                .add("couponTypeSelect",couponTypeSelect)
//                .add("coupon_id",coupon_id)
//                .add("couponCode",couponCode)
//                .add("invoice_title",invoice_title)
//                .add("taxpayer",taxpayer)
//                .add("user_note",user_note)
//                .build();
//        String url=NetWorkConst.URL_SUBMIT_ORDER;
//        Request request=new Request.Builder().post(requestBody).url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//
//    }
//
//    public static void deleteCart(String token, String user_id, String cart_ids, Callback callback) {
//        JSONArray jsonArray=new JSONArray();
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        RequestBody requestBody= new FormBody.Builder()
//                .add("user_id",user_id+"")
//                .add("token",token)
//                .add("unique_id","bocang")
//                .add("cart_ids",cart_ids)
//                .build();
//        String url=NetWorkConst.URL_DELETE_CART;
//        Request request=new Request.Builder().post(requestBody).url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void getWxPay(String userid, String token, String order_sn, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        RequestBody requestBody= new FormBody.Builder()
//                .add("order_sn",order_sn)
//                .add("token",token)
//                .add("user_id",userid)
//                .build();
//        String url=NetWorkConst.URL_WX_PAY;
//        Request request=new Request.Builder().post(requestBody).url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void getAliPay(String userid, String token, String order_sn, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        RequestBody requestBody= new FormBody.Builder()
//                .add("order_sn",order_sn)
//                .add("token",token)
//                .add("user_id",userid)
//                .build();
//        String url=NetWorkConst.URL_ALI_PAY;
//        Request request=new Request.Builder().post(requestBody).url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void editCart(String ids, Boolean isCheck, String userid, String token, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        RequestBody requestBody= new FormBody.Builder()
//                .add("ids",ids)
//                .add("isCheck",isCheck?"1":"0")
//                .add("user_id",userid)
//                .add("token",token)
//                .build();
//        String url=NetWorkConst.URL_ALI_PAY;
//        Request request=new Request.Builder().post(requestBody).url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void updateCart(String updateStr, String userid, String token, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        RequestBody requestBody= new FormBody.Builder()
//                .add("user_id",userid)
//                .add("token",token)
//                .add("cart_form_data",updateStr)
//                .build();
//        String url=NetWorkConst.URL_CART_LIST;
//        Request request=new Request.Builder().post(requestBody).url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void getAccess_token(String code, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        String url=NetWorkConst.URL_ACCESS_TOKEN+"&appid="+ Constance.APP_ID+"&secret="+Constance.APP_SECRET+"&code="+code;
//        Request request=new Request.Builder().get().url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void loginByWx(String openid, String wx, String unionid, String head , Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
////        PgyCrashManager.reportCaughtException(IssApplication.getContext(),new Exception("http:"+openid+","+wx+","+unionid));
//        if(head==null)head="";
//        RequestBody requestBody= new FormBody.Builder()
//                .add("openid",openid)
//                .add("unique_id","bocang")
//                .add("oauth",wx)
//                .add("push_id","0")
//                .add("unionid",unionid)
//                .add("nickname","")
//                .add("head_pic",head)
//                .build();
//        String url=NetWorkConst.URL_THIRD_LOGIN;
//        Request request=new Request.Builder().post(requestBody).url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void getAddressList(String userid, String token, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        String url=NetWorkConst.URL_ADDRESS_LIST+"&user_id="+userid+"&token="+token;
//        Request request=new Request.Builder().get().url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void getOrderList(String userid, String token, String type, String p, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        String url=NetWorkConst.URL_ORDER_LIST+"&user_id="+userid+"&token="+token+"&type="+type+"&p="+p;
//        Request request=new Request.Builder().get().url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void getRegion(String s, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        String url=NetWorkConst.URL_GET_REGION+s;
//        Request request=new Request.Builder().get().url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void deleteAddress(String user_id, String token, int mCurrentId, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        RequestBody requestBody= new FormBody.Builder()
//                .add("user_id",user_id)
//                .add("token",token)
//                .add("id",mCurrentId+"")
//                .build();
//        String url=NetWorkConst.URL_DELETE_ADDRESS;
//        Request request=new Request.Builder().post(requestBody).url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void getOrderDetail(String user_id, String token, String oid, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        String url=NetWorkConst.URL_ORDER_DETAIL+"&user_id="+user_id+"&token="+token+"&id="+oid;
//        Request request=new Request.Builder().get().url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void searchProcude(String keyword, int page, String s, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        String url=NetWorkConst.URL_SEARCH_GOODS+"&q="+keyword+"&pagesize="+s+"&p="+page;
//        Request request=new Request.Builder().get().url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void buyGoodsNow(String user_id, String token, int goods_id, String mProperty, int mCount, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        RequestBody requestBody= new FormBody.Builder()
//                .add("user_id",user_id)
//                .add("token",token)
//                .add("goods_id",goods_id+"")
//                .add("item_id",mProperty)
//                .add("goods_num",mCount+"")
//                .add("action","buy_now")
//                .build();
//        String url=NetWorkConst.URL_BUY_NOW;
//        Request request=new Request.Builder().post(requestBody).url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void submitOrderBuyNow(String user_id, String token, String goods_id, String item_id, String goods_num, String act, String mAddressId, String shipping_code, String couponTypeSelect, String coupon_id, String couponCode, String invoice_title, String taxpayer, String user_note, Callback callback) {
//
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        RequestBody requestBody= new FormBody.Builder()
//                .add("user_id",user_id+"")
//                .add("token",token)
//                .add("act",act)
//                .add("address_id",mAddressId)
//                .add("shipping_code",shipping_code)
//                .add("couponTypeSelect",couponTypeSelect)
//                .add("coupon_id",coupon_id)
//                .add("couponCode",couponCode)
//                .add("invoice_title",invoice_title)
//                .add("taxpayer",taxpayer)
//                .add("user_note",user_note)
//                .add("goods_id",goods_id)
//                .add("item_id",item_id)
//                .add("goods_num",goods_num)
//                .add("action","buy_now")
//                .build();
//        String url=NetWorkConst.URL_SUBMIT_ORDER;
//        Request request=new Request.Builder().post(requestBody).url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void cancelOrder(String user_id, String token, String order_id, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        String url=NetWorkConst.URL_ORDER_CANCEL+"&user_id="+user_id+"&token="+token+"&order_id="+order_id;
//        Request request=new Request.Builder().get().url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void getPromoteList(int p, int pageSize, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        RequestBody requestBody= new FormBody.Builder()
//                .add("p",p+"")
//                .add("page_size",""+pageSize)
//                .build();
//        String url=NetWorkConst.URL_PROMOTE_LIST;
//        Request request=new Request.Builder().post(requestBody).url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//
//    }
//
//    public static void getGoodsThumb(int mProductId, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        RequestBody requestBody= new FormBody.Builder()
//                .add("id",mProductId+"")
//                .build();
//        String url=NetWorkConst.URL_PRODUCT_THUMB;
//        Request request=new Request.Builder().post(requestBody).url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//
//    }
//
//    public static void changePwd(String user_id, String token, String oldpwd, String newpwd, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        RequestBody requestBody= new FormBody.Builder()
//                .add("user_id",user_id+"")
//                .add("token",token)
//                .add("old_password",oldpwd+"")
//                .add("new_password",newpwd+"")
//                .build();
//        String url=NetWorkConst.URL_CHANGE_PWD;
//        Request request=new Request.Builder().post(requestBody).url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void orderConfirm(String user_id, String token, int order_id, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        RequestBody requestBody= new FormBody.Builder()
//                .add("user_id",user_id+"")
//                .add("token",token)
//                .add("order_id",order_id+"")
//                .build();
//        String url=NetWorkConst.URL_ORDER_RECEIVER_CONFIRM;
//        Request request=new Request.Builder().post(requestBody).url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void getHomeNewGoods(Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        String url=NetWorkConst.URL_NEWGOODS;
//        Request request=new Request.Builder().get().url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void getGoodsWeiHuo(Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        String url=NetWorkConst.URL_TAILGOODS;
//        Request request=new Request.Builder().get().url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void BindAccount(String mobile, String verify, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        RequestBody requestBody= new FormBody.Builder()
//                .add("verify_code",verify+"")
//                .add("mobile",mobile)
//                .add("unique_id","bocang")
//                .build();
//        String url=NetWorkConst.URL_BIND_ACCOUNT;
//        Request request=new Request.Builder().post(requestBody).url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void bindReg(String mPhone, String pwd, String code, String nickname, String push_id, String third_oauth, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        if(third_oauth==null)third_oauth="";
//        RequestBody requestBody= new FormBody.Builder()
//                .add("mobile",mPhone)
//                .add("password",pwd)
//                .add("verify_code",code)
//                .add("nickname",nickname)
//                .add("is_bind","1")
//                .add("push_id",push_id)
//                .add("third_oauth",third_oauth)
//                .build();
//        String url=NetWorkConst.URL_BIND_REGIS;
//        Request request=new Request.Builder().post(requestBody).url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//
//    }
//
//    public static void getSaleTime(Callback callback) {
//       OkHttpClient okHttpClient=getOkHttpInstance();
//    RequestBody requestBody= new FormBody.Builder()
//            .add("unique_id","bocang")
//            .build();
//    String url=NetWorkConst.URL_FLASH_SALE_TIME;
//    LogUtils.logE("sale",url);
//    Request request=new Request.Builder().post(requestBody).url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void getSaleTimeList(String sT, String eT, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        RequestBody requestBody= new FormBody.Builder()
//                .add("start_time",sT)
//                .add("end_time",eT)
//                .build();
//        String url=NetWorkConst.URL_FLASH_SALE_LIST;
//        Request request=new Request.Builder().post(requestBody).url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void getCouponList(String token, String userid, int p, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        String url=NetWorkConst.URL_COUPON_GET+p+"&token="+token+"&user_id="+userid;
//        Request request=new Request.Builder().get().url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void getCouponMineList(String user_id, String token, int p, int type, int store_id, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        String url=NetWorkConst.URL_COUPON_MINE_GET+"&p="+p+"&type="+type+"&store_id="+store_id+"&user_id="+user_id+"&token="+token;
//        Request request=new Request.Builder().get().url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void getCoupon(String token, String user_id, int id, Callback callback) {
//
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        RequestBody requestBody= new FormBody.Builder()
//                .add("user_id",user_id)
//                .add("token",token)
//                .add("coupon_id",id+"")
//                .build();
//        String url=NetWorkConst.URL_COUPON_GET_MINE;
//        Request request=new Request.Builder().post(requestBody).url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void collectGoods(int goods_id, String user_id, String token, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        RequestBody requestBody= new FormBody.Builder()
//                .add("user_id",user_id)
//                .add("token",token)
//                .add("goods_id",goods_id+"")
//                .build();
//        String url=NetWorkConst.URL_GOODS_COLLECT;
//        Request request=new Request.Builder().post(requestBody).url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void getCollect(String user_id, String tokend, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        String url=NetWorkConst.URL_COLLECT_LIST+"&user_id="+user_id+"&token="+tokend;
//        Request request=new Request.Builder().get().url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void getSceneType(Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        String url=NetWorkConst.URL_SCENE_TYPE;
//        Request request=new Request.Builder().get().url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void getSceneList(int p, String attr_values, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        String url=NetWorkConst.URL_SCENE_LIST+"&p="+p+"&attr_values="+attr_values;
//        url.replace("\"[","[");
//        url.replace("]\"","]");
//        LogUtils.logE("url",url);
//        Request request=new Request.Builder().get().url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void getProgramme(int p, String token, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        String url=NetWorkConst.URL_PROGRAMME_LIST+"&p="+p+"&token="+token;
//        Request request=new Request.Builder().get().url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void editProgramme(String token, int id, String is_private, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        RequestBody requestBody= new FormBody.Builder()
//                .add("id",id+"")
//                .add("is_private",is_private)
//                .add("token",token)
//                .build();
//        String url=NetWorkConst.URL_PROGRAMME_EDIT;
//        Request request=new Request.Builder().post(requestBody).url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void deleteProgramme(String token, int id, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        RequestBody requestBody= new FormBody.Builder()
//                .add("id",id+"")
//                .add("token",token)
//                .build();
//        String url=NetWorkConst.URL_PROGRAMME_DELETE;
//        Request request=new Request.Builder().post(requestBody).url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void getExpress(String shipping_code, String invoice_no, String token, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        String url=NetWorkConst.URL_EXPRESS+"&token="+token+"&shipping_code="+shipping_code+"&invoice_no="+invoice_no;
//        Request request=new Request.Builder().get().url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void updateUserInfo(String token, String user_id, String nickname, String sex, String birthday, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        RequestBody requestBody= new FormBody.Builder()
//                .add("user_id",user_id)
//                .add("token",token)
//                .add("nickname",nickname)
//                .add("sex",sex)
//                .add("birthday",birthday)
//                .build();
//        String url=NetWorkConst.URL_UPDATE_USER;
//        Request request=new Request.Builder().post(requestBody).url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void getUserInfo(String token, String user_id, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        String url=NetWorkConst.URL_GET_USER+"&token="+token+"&user_id="+user_id;
//        Request request=new Request.Builder().get().url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void getLikeGoods(int p, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        String url=NetWorkConst.URL_LIKE_GOODS;
//        Request request=new Request.Builder().get().url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//
//    }
//
//    public static void searchSimliarImg(String base64, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        RequestBody requestBody= new FormBody.Builder()
//                .add("img","{"+base64+"}")
//                .build();
//        String url=NetWorkConst.URL_SEARCH_IMG;
//        Request request=new Request.Builder().post(requestBody).url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void getMessage(int p, String category, String token, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        String url=NetWorkConst.URL_GET_MESSAGE+"&token="+token+"&p="+p+"&category="+category;
//        Request request=new Request.Builder().get().url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void getFilterGoods(String filterUrl, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        Request request=new Request.Builder().get().url(filterUrl).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void getGoodsActivity(int goods_id, String mProperty, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        String url=NetWorkConst.URL_PRODUCT_ACTIVTY+"&goods_id="+goods_id+"&item_id="+mProperty;
//        Request request=new Request.Builder().get().url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public static void getUserInfoWx(String access_token, String openid, Callback callback) {
//        OkHttpClient okHttpClient=getOkHttpInstance();
//        String url=NetWorkConst.URL_WX_USER_INFO+"&access_token="+access_token+"&openid="+openid;
//        Request request=new Request.Builder().get().url(url).build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }
}
