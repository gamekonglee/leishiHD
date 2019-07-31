package bc.otlhd.com.cons;

import bc.otlhd.com.utils.net.Network;

/**
 * @author Jun
 * @time 2017/1/7  21:46
 * @desc 地址
 */
public class NetWorkConst {


    //主地址
    public static final String API_HOST = "http://nvc.bocang.cc";

    //主地址
    public static final String UR_HOST = "http://nvc.bocang.cc";
    public static final String UR_APP_NAME = "nvcHD";
    //图片主地址
    public static final String SCENE_HOST = "http://nvc.bocang.cc";
    /**
     * 产品url
     */
    public static final String UR_PRODUCT_URL = "http://bocang.oss-cn-shenzhen.aliyuncs.com/leishi/";

    /**
     * 场景url(旧)
     */
    public static final String URL_SCENE="http://bocang.oss-cn-shenzhen.aliyuncs.com/scene/";
    /**
     * 场景url
     */
    public static final String UR_SCENE_URL = API_HOST+"/App/leishi/Public/uploads//scene/";

    /**
     * 品牌图片地址
     */
    public static  final String UR_BRAND_URL=UR_HOST+"/App/leishi/Public/uploads/";

    /**
     * 分享订单
     */
    public final static  String  UR_SHAREORDER=UR_HOST+"/index.php/Interface/order_show?id=";
    /**
     * 分享icon
     */
    public final static  String  UR_SHAREICON=UR_HOST+"/App/leishi/Public/uploads/ic_launcher.png";
    //屏保广告图
    public static final String UR_AD = UR_HOST + "/Interface/get_adv";
    //方案图片
    public final static  String  UR_PLANIMAGE=UR_HOST+"/App/leishi/Public/uploads/plan/";


    // 获取用户信息
    public static final String UR_USER_INFO=UR_HOST+"/Interface/get_user_info";
    public static  final int WHAT_USER_INFO=0x001;

    // 添加用户信息
    public static final String UR_USER_ADD=UR_HOST+"/Interface/add_user";
    public static  final int WHAT_USER_ADD=0x002;

    // 商品列表信息
    public static final String UR_GOODS_LIST=UR_HOST+"/Interface/get_goods_list";
    public static  final int WHAT_GOODS_LIST=0x003;

    // 场景列表信息
    public static final String UR_SCENE_LIST=UR_HOST+"/Interface/get_scene_list";
    public static  final int WHAT_SCENE_LIST=0x004;

    // 产品详情信息
    public static final String UR_GOODS_INFO=UR_HOST+"/Interface/get_goods_info";
    public static  final int WHAT_GOODS_INFO=0x005;

    // 提交订单
    public static final String UR_UPLOAD_GOODS=UR_HOST+"/Interface/upload_order";
    public static  final int WHAT_UPLOAD_GOODS=0x006;

    // 提交自定义产品价格资料
    public static final String UR_UPLOAD_GOODSPRICES=UR_HOST+"/index.php/Interface/Inset_price";
    public static  final int WHAT_UPLOAD_GOODSPRICES=0x007;

    // 获取自定义产品价格资料
    public static final String UR_GET_GOODSPRICES=UR_HOST+"/index.php/Interface/Read_price";
    public static  final int WHAT_GET_GOODSPRICES=0x008;

    //图片名称
    public final static  String GETPICLIST=UR_HOST+"/index.php/Interface/get_pic_list";
    public static  final int WHAT_GETPICLIST=0x009;

    //品牌列表
    public final static  String GETCATEGRORYLIST=UR_HOST+"/Interface/Category_list";
    public static  final int WHAT_GETCATEGRORYLIST=0x010;

    //品牌详情
    public final static String BRAND_DETAIL=UR_HOST+"/Interface/Category_info";
    public static  final int WHAT_BRAND_DETAIL=0x011;

    //分享场景
    public final static  String SUBMITPLAN=UR_HOST+"/index.php/Interface/upload_plan";
    public static final int NEWSCLASS_INT = 0x12;
    public static final String IMAGE_URL = NetWorkConst.API_HOST + "/App/leishi/Public/uploads/";
    //新闻详情
    public static final String NEWSDETAIL = NetWorkConst.API_HOST + "/Interface/News_text";
    public static final String VERSION_NEW_URL=NetWorkConst.API_HOST+"/Interface/get_version";
    public static final String VERSION_OC_URL = API_HOST + "/Interface/oc_list";


    //分享产品
     public static String SHAREPRODUCT=UR_HOST+"/Interface/goods_show?id=";


    public static String SHAREPLAN=UR_HOST+"/index.php/Interface/plan_show?";



    //分享APP
    public static final String APK_URL ="http://app.bocang.cc/Ewm/index/url/"+UR_APP_NAME+".bocang.cc";
    public static final String DOWN_APK_URL ="http://app.08138.com/leishiHD.apk";

    public static String APK_NAME=UR_APP_NAME+"_v";

    //分享APP图片
    public final static  String SHAREIMAGE="http://app.08138.com/icon.jpg";

    //获取app最新版本号接口
    public static final String VERSION_URL ="http://app.08138.com/version/versioninfo.php?bc_ver_name2="+UR_APP_NAME+"a";


    //下载数据
    public static final String DOWN_SQL_URL =UR_HOST+"/App/leishi/Public/data.sqlite";

    //获取产品列表
    public static final String GOODSLIST = API_HOST + "/Interface/get_goods_list";

    //产品url
    public static final String PRODUCT_URL = API_HOST + "/App/simon/Public/uploads/goods/";

    //产品类别
    public static final String GOODSCLASS = API_HOST + "/Interface/get_goods_class";

    //登录
    public static final String LOGIN = API_HOST + "/v2/ecapi.auth.signin";

    //广告
    public static final String BANNER = API_HOST + "/v2/ecapi.banner.list";

    //注册
    public static final String REGIEST = API_HOST + "/v2/ecapi.auth.mobile.signup";

    //重置密码
    public static final String RESET = API_HOST + "/v2/ecapi.auth.mobile.reset";

    //验证码
    public static final String VERIFICATIONCOE = API_HOST + "/v2/ecapi.auth.mobile.send";

    //产品
    public static final String PRODUCT = API_HOST + "/v2/ecapi.product.list";

    //推荐产品
    public static final String RECOMMENDPRODUCT = API_HOST + "/v2/ecapi.recommend.product.list";

    //产品分类
    public static final String CATEGORY = API_HOST + "/v2/ecapi.category.list";

    //返回用户信息
    public static final String PROFILE = API_HOST + "/v2/ecapi.user.profile.get";

    //返回用户信息
    public static final String UPDATEPROFILE = API_HOST + "/v2/ecapi.user.profile.update";

    //获取收藏产品列表
    public static final String LIKEDPRODUCT = API_HOST + "/v2/ecapi.product.liked.list";

    //取消收藏产品
    public static final String ULIKEDPRODUCT = API_HOST + "/v2/ecapi.product.unlike";

    //添加收藏产品
    public static final String ADDLIKEDPRODUCT = API_HOST + "/v2/ecapi.product.like";

    //订单列表
    public static final String ORDERLIST = API_HOST + "/v2/ecapi.order.list";

    //取消订单
    public static final String ORDERCANCEL = API_HOST + "/v2/ecapi.order.cancel";

    //产品详情
    public static final String PRODUCTDETAIL = API_HOST + "/v2/ecapi.product.get";

    //场景列表
    public static final String SCENELIST = API_HOST + "/v2/ecapi.scene.list";

    //场景分类
    public static final String SCENECATEGORY = API_HOST + "/v2/ecapi.scene.category.list";

    //加入购物车
    public static final String ADDCART = API_HOST + "/v2/ecapi.cart.add";

    //购物车列表
    public static final String GETCART = API_HOST + "/v2/ecapi.cart.get";

    //删除购物车
    public static final String DeleteCART = API_HOST + "/v2/ecapi.cart.delete";

    //修改购物车
    public static final String UpdateCART = API_HOST + "/v2/ecapi.cart.update";

    //结算购物车
    public static final String CheckOutCart = API_HOST + "/v2/ecapi.cart.checkout";

    //收货地址列表
    public static final String CONSIGNEELIST = API_HOST + "/v2/ecapi.consignee.list";

    //新增收货地址
    public static final String CONSIGNEEADD = API_HOST + "/v2/ecapi.consignee.add";

    //删除收货地址
    public static final String CONSIGNEEDELETE = API_HOST + "/v2/ecapi.consignee.delete";

    //默认收货地址
    public static final String CONSIGNEEDEFAULT = API_HOST + "/v2/ecapi.consignee.setDefault";

    //修改收货地址
    public static final String CONSIGNEEUPDATE = API_HOST + "/v2/ecapi.consignee.update";

    //查询区域
    public static final String ADDRESSlIST = API_HOST + "/v2/ecapi.region.list";

    //货物物流列表
    public static final String LOGISTICS = API_HOST + "/v2/ecapi.logistics.list";

    //上传头像
    public static final String UPLOADAVATAR = API_HOST + "/v2/ecapi.user.avatar.upload";

    //筛选列表
    public static final String ATTRLIST = API_HOST + "/v2/ecapi.goods.attr.list";

    //附近商家
    public static final String NEARBYLIST = API_HOST + "/v2/ecapi.server.nearby.list";

    //文章列表
    public static final String ARTICLELIST = API_HOST + "/v2/ecapi.article.list";

    //文章列表
    public static final String NOTICELIST = API_HOST + "/v2/ecapi.notice.list";

    //新闻分类
    public static final String NEWSCLASS=API_HOST+"/Interface/News_class_list";
    //新闻列表
    public static final String NEWSTLIST=API_HOST+"/Interface/News_list?";

    //上传方案
    public static final String FANGANUPLOAD = API_HOST + "/v2/ecapi.fangan.upload";

    //方案列表
    public static final String FANGANLIST = API_HOST + "/v2/ecapi.fangan.list";

    //删除方案
    public static final String FANGANDELETE = API_HOST + "/v2/ecapi.fangan.delete";

    //支付订单
    public static final String PAYMENT = API_HOST + "/v2/ecapi.payment.pay";

    //支付参数信息
    public static final String PAYMENTINFO = API_HOST + "/v2/ecapi.payment.types.list";

    //场景图地址
    public static final String SCENEPATH ="http://bocang.oss-cn-shenzhen.aliyuncs.com/scene/";

    public static String  QQ="194701";

    //场景图地址
    public static final String QQURL ="mqqwpa://im/chat?chat_type=wpa&uin="+QQ+"&version=1";

    //客服QQ
    public static final String CUSTOM =API_HOST+"/v2/ecapi.get.custom";

    //上传自定义场景
    public static final String TWOCOE =SCENE_HOST+"/qr_post.php?token=";

    //监听自定义场景
    public static final String QEAPI =SCENE_HOST+"/qr_api.php?token=";


    public final static  String SHAREIMAGE_LOGO=UR_HOST+"/App/leishi/Public/uploads/logo.png";

    //产品卡
    public final static  String WEB_PRODUCT_CARD="http://browser.edsmall.cn/webimg?url="+UR_HOST+"/index.php/Interface/phone_goods_show/id/";
    public static String AppName="leishi";
}



