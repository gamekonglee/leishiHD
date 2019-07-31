package bc.otlhd.com.controller;

import android.os.Message;

import com.google.android.exoplayer.C;

import java.io.IOException;

import bc.otlhd.com.listener.INetworkCallBack;
import bc.otlhd.com.listener.INoHttpNetworkCallBack;
import bc.otlhd.com.net.OkHttpUtils;
import bc.otlhd.com.ui.activity.CaseActivity;
import bc.otlhd.com.utils.LogUtils;
import bc.otlhd.com.utils.net.HttpListener;
import bocang.json.JSONObject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by gamekonglee on 2019/2/26.
 */

public class CaseController extends BaseController{

    public final CaseActivity mView;
    private int page=1;

    public CaseController(CaseActivity caseActivity) {
        mView = caseActivity;
        init();
    }

    private void init() {


    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }
}
