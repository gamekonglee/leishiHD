package bc.otlhd.com.ui.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.ActionBar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import bc.otlhd.com.R;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.net.OkHttpUtils;
import bc.otlhd.com.ui.activity.user.RegiestActivity;
import bc.otlhd.com.ui.view.ShowDialog;
import bc.otlhd.com.utils.LogUtils;
import bc.otlhd.com.utils.MyShare;
import bc.otlhd.com.utils.SignIdUtil;
import bc.otlhd.com.utils.net.HttpListener;
import bc.otlhd.com.utils.net.Network;
import bocang.utils.AppUtils;
import bocang.utils.IntentUtil;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class OpenScreenActivity extends AppCompatActivity {
    private final String TAG = OpenScreenActivity.class.getSimpleName();
    private OpenScreenActivity ct = this;
    private Network mNetwork;
    private long mBeginTime;
    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_openscreen);

        mVisible = true;
        mContentView = findViewById(R.id.fullscreen_content);

//        Bitmap bmp = ImageUtil.getBitmapById(ct, R.drawable.bg_openscreen);
//        mContentView.setBackgroundDrawable(new BitmapDrawable(bmp));
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.push_in);
        mContentView.startAnimation(anim);
        mNetwork=new Network();

        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });


        mBeginTime = System.currentTimeMillis();
        IssueApplication.mSignId=MyShare.get(this).getString("SignId");
        if(AppUtils.isEmpty(IssueApplication.mSignId)){
            MyShare.get(this).putString("SignId", SignIdUtil.getSignId(ct));
            IssueApplication.mSignId= SignIdUtil.getSignId(ct);
        }
        if(!OkHttpUtils.hashkNewwork()){
            String userinfo=MyShare.get(this).getString(Constance.user_info);
            if(userinfo!=null){
                IssueApplication.mUserInfo= JSON.parseObject(userinfo);
            goOn();
            return;
            }
        }
        getUserInfo();
//       String data= MyShare.get(OpenScreenActivity.this).getString(Constance.data);
//        if(AppUtils.isEmpty(data)){
//            getUserInfo();
//        }else{
//            ((IssueApplication)getApplication()).mUserInfo= JSON.parseObject(data);
//            startActivity(new Intent(ct, MainActivity.class));
//            finish();
//        }


    }


    private void getUserInfo(){
        mNetwork.sendUser(IssueApplication.mSignId, this, new HttpListener() {
            @Override
            public void onSuccessListener(int requestCode, com.alibaba.fastjson.JSONObject ans) {
                if (null == ct || ct.isFinishing())
                    return;
                String result = ans.getString(Constance.result);
                if (result.equals("success")) {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            if (System.currentTimeMillis() - mBeginTime < 1800) {
                                try {
                                    Thread.sleep(1800 + mBeginTime - System.currentTimeMillis());
                                    goOn();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }else{
                                goOn();
                            }
                        }
                    });
                    LogUtils.logE("userdata",ans.toString());
                    ((IssueApplication)getApplication()).mUserInfo=ans.getJSONObject(Constance.data);
                    MyShare.get(OpenScreenActivity.this).putString(Constance.user_info,ans.getJSONObject(Constance.data).toJSONString());
                    MyShare.get(OpenScreenActivity.this).putString(Constance.data,ans.getJSONObject(Constance.data).toJSONString());
                    MyShare.get(OpenScreenActivity.this).putString(Constance.level_password,ans.getJSONObject(Constance.data).getString(Constance.level_password));
                    thread.setName("SleepThread");
                    thread.start();
                } else {
                    int data = ans.getInteger(Constance.data);
                    if (data == 0) {
                        showActivateDialog();
                    } else if (data == 2) {
                        IntentUtil.startActivity(OpenScreenActivity.this,RegiestActivity.class,true);
                    }
                }


            }

            @Override
            public void onFailureListener(int requestCode, com.alibaba.fastjson.JSONObject ans) {
//                if(!AppUtils.isEmpty(ans)){
//                    MyToast.show(OpenScreenActivity.this, ans.toJSONString());
//                }
//                finish();
                ShowDialog mDialog = new ShowDialog();
                mDialog.show(OpenScreenActivity.this, "提示", "无法连接服务器!", new ShowDialog.OnBottomClickListener() {
                    @Override
                    public void positive() {
                        OpenScreenActivity.this.finish();
                    }

                    @Override
                    public void negtive() {
                        OpenScreenActivity.this.finish();
                    }
                });
            }
        });
    }


    /**
     * show 激活 dialog
     */
    public void showActivateDialog() {
        ShowDialog mDialog = new ShowDialog();
        mDialog.setNoDismiss(true);
//        mDialog.show(this, "申请提示", "系统已注册未审核通过，请联系相关人员开通使用权限"+"\n标识码："+IssueApplication.mSignId, new ShowDialog.OnBottomClickListener() {
        mDialog.show(this, "申请提示", "审核中，请耐心等待！\n备注：如需快速审核，请联系该区域办事处报备总部\n(标识码："+ SignIdUtil.getSignId(this)+")", new ShowDialog.OnBottomClickListener() {
            @Override
            public void positive() {
//                OpenScreenActivity.this.finish();
            }

            @Override
            public void negtive() {
//                OpenScreenActivity.this.finish();
            }
        });
        mHandler.post(runnable);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(0);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mVisible = false;

        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    private final int GO_ON = 1;
    private Handler mHandler = new Handler() {

        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        @Override
        public void handleMessage(Message msg) {
            if (null != ct && !ct.isFinishing()) {
                switch (msg.what) {
                    case GO_ON:
                        startActivity(new Intent(ct, MainActivity.class));
//                        startActivity(new Intent(ct, OrderHDActivity.class));
                        finish();
                        break;
                }
            }
        }
    };

    private void goOn() {
        mHandler.sendEmptyMessage(GO_ON);
    }
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            getUserIn();
            mHandler.postDelayed(runnable,3000);

        }
    };


    private void getUserIn(){
        if(OpenScreenActivity.this==null||isFinishing()){
            return;
        }
        mNetwork.sendUser(IssueApplication.mSignId, this, new HttpListener() {
            @Override
            public void onSuccessListener(int requestCode, com.alibaba.fastjson.JSONObject ans) {
                String result = ans.getString(Constance.result);
                if (result.equals("success")) {
                    mHandler.removeCallbacks(runnable);
                    ((IssueApplication)getApplication()).mUserInfo = ans.getJSONObject(Constance.data);
                    MyShare.get(OpenScreenActivity.this).putString(Constance.data, ans.getJSONObject(Constance.data).toJSONString());
                    goOn();
                }
            }

            @Override
            public void onFailureListener(int requestCode, com.alibaba.fastjson.JSONObject ans) {
            }
        });
    }

}
