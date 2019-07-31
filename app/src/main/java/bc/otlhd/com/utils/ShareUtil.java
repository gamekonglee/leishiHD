package bc.otlhd.com.utils;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;

import bc.otlhd.com.R;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.tencent.qq.QQ;

/**
 * @author: Jun
 * @date : 2017/3/15 15:54
 * @description :
 */
public class ShareUtil {
    /**
     * 分享操作
     */
    public static void showShare(final Activity activity,String title,final String path,final String imagePath) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        ShareSDK.initSDK(activity);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(path);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(title);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(path);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(title);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(UIUtils.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(path);
        //图片地址
        //        mImgUrl= Constant.PRODUCT_URL+mImgUrl+ "!400X400.png";
        //        Log.v("520it","'分享:"+mImgUrl);
        //        Log.v("520it","产品地址:"+Constant.SHAREPLAN+"id="+id));
        oks.setImageUrl(imagePath);

        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, final Platform.ShareParams paramsToShare) {
                if ("QZone".equals(platform.getName())) {
                    paramsToShare.setTitle(null);
                    paramsToShare.setTitleUrl(null);
                }
                if ("SinaWeibo".equals(platform.getName())) {
                    paramsToShare.setUrl(null);
                    paramsToShare.setText("分享文本 " + path);
                }
                if ("Wechat".equals(platform.getName())) {
                    ImageView img = new ImageView(activity);
                    ImageLoader.getInstance().displayImage(imagePath, img);
                    paramsToShare.setImageData(img.getDrawingCache());
                }
                if ("WechatMoments".equals(platform.getName())) {
                    ImageView img = new ImageView(activity);
                    ImageLoader.getInstance().displayImage(imagePath, img);
                    paramsToShare.setImageData(img.getDrawingCache());
                }

            }
        });
        // 启动分享GUI
        oks.show(activity);
    }
    /**
     * 分享操作
     */
    public static void showShare01(final Activity activity,String title,final String path,final String imagePath) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        ShareSDK.initSDK(activity);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(path);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(title);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(path);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(title);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(UIUtils.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(path);
        //图片地址
        //        mImgUrl= Constant.PRODUCT_URL+mImgUrl+ "!400X400.png";
        //        Log.v("520it","'分享:"+mImgUrl);
        //        Log.v("520it","产品地址:"+Constant.SHAREPLAN+"id="+id));
        oks.setImageUrl(imagePath);

        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, final Platform.ShareParams paramsToShare) {
                if ("QZone".equals(platform.getName())) {
                    paramsToShare.setTitle(null);
                    paramsToShare.setTitleUrl(null);
                }
                if ("SinaWeibo".equals(platform.getName())) {
                    paramsToShare.setUrl(null);
                    paramsToShare.setText("分享文本 " + path);
                }
                if ("Wechat".equals(platform.getName())) {

                    paramsToShare.setText(null);
                    paramsToShare.setTitle(null);
                    paramsToShare.setTitleUrl("");
                    paramsToShare.setImageUrl(imagePath);
                    //                    ImageView img = new ImageView(activity);
                    //                    ImageLoader.getInstance().displayImage(imagePath, img);
                    //                    paramsToShare.setImageData(img.getDrawingCache());
                }
                if ("WechatMoments".equals(platform.getName())) {
                    ImageView img = new ImageView(activity);
                    ImageLoader.getInstance().displayImage(imagePath, img);
                    paramsToShare.setImageData(img.getDrawingCache());
                }
                if (platform.getName().equalsIgnoreCase(QQ.NAME)) {
                    paramsToShare.setText(null);
                    paramsToShare.setTitle(null);
                    paramsToShare.setTitleUrl("");
                    paramsToShare.setImageUrl(imagePath);
                }

                platform.setPlatformActionListener(new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                        activity.finish();
                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {
                        activity.finish();
                    }

                    @Override
                    public void onCancel(Platform platform, int i) {

                    }
                });

            }


        });
        // 启动分享GUI
        oks.show(activity);


    }
}
