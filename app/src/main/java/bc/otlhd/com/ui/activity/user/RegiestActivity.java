package bc.otlhd.com.ui.activity.user;

import android.app.Dialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import bc.otlhd.com.R;
import bc.otlhd.com.adapter.BaseAdapterHelper;
import bc.otlhd.com.adapter.QuickAdapter;
import bc.otlhd.com.bean.OcBean;
import bc.otlhd.com.controller.user.RegiestController;
import bc.otlhd.com.net.OkHttpUtils;
import bc.otlhd.com.ui.view.ShowDialog;
import bocang.view.BaseActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author: Jun
 * @date : 2017/2/7 15:35
 * @description :
 */
public class RegiestActivity extends BaseActivity {
    public RegiestController mController;
    private Button sure_bt;
    private TextView tvRun;
    private List<OcBean> ocBeanList;
    public String currentId="-1";

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController=new RegiestController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_regiest);
        sure_bt=getViewAndClick(R.id.sure_bt);
        tvRun = getViewAndClick(R.id.tvRun);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()){
            case R.id.sure_bt:
                mController.sendRegiest();
                break;
            case R.id.tvRun:
                if(ocBeanList!=null&&ocBeanList.size()>0){
                    showOcSelectDialog();
                 return;
                }
                OkHttpUtils.getOcList(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        ocBeanList = new Gson().fromJson(response.body().string(),new TypeToken<List<OcBean>>(){}.getType());
                        if(ocBeanList !=null&& ocBeanList.size()>0){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showOcSelectDialog();

                                }
                            });
                        }
                    }
                });
                break;
        }
    }

    private void showOcSelectDialog() {
        final Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.dialog_oc_select);
        ListView lv_oc= (ListView) dialog.findViewById(R.id.lv_oc_list);
        QuickAdapter<OcBean> ocBeanQuickAdapter=new QuickAdapter<OcBean>(this,R.layout.item_oc_dialog) {
            @Override
            protected void convert(BaseAdapterHelper helper, OcBean item) {
            helper.setText(R.id.tv_name,item.getName());
            }
        };
        lv_oc.setAdapter(ocBeanQuickAdapter);
        ocBeanQuickAdapter.replaceAll(ocBeanList);
        lv_oc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tvRun.setText(ocBeanList.get(position).getName());
                currentId = ocBeanList.get(position).getId();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void goBack(View v){
        ShowDialog mDialog=new ShowDialog();
        mDialog.show(this, "提示", "你是否放弃当前注册?", new ShowDialog.OnBottomClickListener() {
            @Override
            public void positive() {
                finish();
            }

            @Override
            public void negtive() {

            }
        });
    }
}
