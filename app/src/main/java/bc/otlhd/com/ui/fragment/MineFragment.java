package bc.otlhd.com.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;

import bc.otlhd.com.R;
import bc.otlhd.com.common.BaseFragment;
import bc.otlhd.com.controller.user.MineController;
import bc.otlhd.com.ui.activity.IssueApplication;
import bc.otlhd.com.ui.activity.MainActivity;
import bc.otlhd.com.ui.view.popwindow.HighSettingPopWindow;
import bc.otlhd.com.utils.SignIdUtil;
import bocang.utils.MyToast;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * @author Jun
 * @time 2017/1/5  12:00
 * @desc 我的页面
 */
public class MineFragment extends BaseFragment implements View.OnClickListener, OnItemClickListener {
    private CircleImageView head_cv;
    private RelativeLayout collect_rl, version_rl, clear_cache_rl, cotact_cutomer_rl,set_price_rl,down_data_rl;
    private MineController mController;
    private TextView image_version_tv,qq_tv;
    private LinearLayout main_ll;
    private AlertView mAlertViewExt;//窗口拓展例子
    private EditText etName;//拓展View内容
    private InputMethodManager imm;

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fm_mine, null);
//    }

    @Override
    protected int getLayout() {
        return R.layout.fm_mine;
    }

    @Override
    protected void initController() {
        mController=new MineController(this);
    }

    @Override
    public void onStart() {
        super.onStart();
       mController.getTotalCacheSize();
        if(IssueApplication.isUploadTip){
            IssueApplication.isUploadTip=false;
            image_version_tv.setText("");
        }
    }

    @Override
    protected void initViewData() {

    }

    @Override
    protected void initView() {
        head_cv = (CircleImageView) getActivity().findViewById(R.id.head_cv);
        collect_rl = (RelativeLayout) getActivity().findViewById(R.id.collect_rl);
        version_rl = (RelativeLayout) getActivity().findViewById(R.id.version_rl);
        clear_cache_rl = (RelativeLayout) getActivity().findViewById(R.id.clear_cache_rl);
        cotact_cutomer_rl = (RelativeLayout) getActivity().findViewById(R.id.cotact_cutomer_rl);
        set_price_rl = (RelativeLayout) getActivity().findViewById(R.id.set_price_rl);
        down_data_rl = (RelativeLayout) getActivity().findViewById(R.id.down_data_rl);
        main_ll = (LinearLayout) getActivity().findViewById(R.id.main_ll);
        collect_rl.setOnClickListener(this);
        version_rl.setOnClickListener(this);
        clear_cache_rl.setOnClickListener(this);
        cotact_cutomer_rl.setOnClickListener(this);
        set_price_rl.setOnClickListener(this);
        down_data_rl.setOnClickListener(this);
        image_version_tv = (TextView) getActivity().findViewById(R.id.image_version_tv);
        qq_tv = (TextView) getActivity().findViewById(R.id.qq_tv);
        qq_tv.setText(SignIdUtil.getSignId(getActivity()));

        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //拓展窗口
        mAlertViewExt = new AlertView("提示", "请输入高级设置密码!", "取消", null, new String[]{"完成"}, getActivity(), AlertView.Style.Alert, this);
        ViewGroup extView = (ViewGroup) LayoutInflater.from(getActivity()).inflate(R.layout.alertext_form, null);
        etName = (EditText) extView.findViewById(R.id.etName);
        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                //输入框出来则往上移动
                boolean isOpen = imm.isActive();
                mAlertViewExt.setMarginBottom(isOpen && focus ? 120 : 0);
                System.out.println(isOpen);
            }
        });
        mAlertViewExt.addExtView(extView);

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.collect_rl://我的收藏
                mController.setCollect();
                break;
            case R.id.version_rl://当前版本
//                mController.setOrder();
                break;
            case R.id.clear_cache_rl://清楚缓存
                mController.clearCache();
                break;
            case R.id.cotact_cutomer_rl://联系客服
                mController.sendCall();
                break;
            case R.id.down_data_rl://下载数据包
                mController.downData();
                ((MainActivity)getActivity()).mIsRefresh=true;

                break;
            case R.id.set_price_rl://设置价格
                final EditText inputServer = new EditText(MineFragment.this.getActivity());
                inputServer.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                AlertDialog.Builder builder = new AlertDialog.Builder(MineFragment.this.getActivity());
                builder.setTitle("请输入密码,进入高级设置!").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                        .setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        String password = inputServer.getText().toString();
                        if (password.toLowerCase().contains("otl")) {
                            dialog.dismiss();
                            HighSettingPopWindow popWindow = new HighSettingPopWindow(getContext(), getActivity());
                            popWindow.onShow(main_ll);
                        } else {
                            MyToast.show(MineFragment.this.getActivity(), "密码错误,请重新输入!");
                        }
                    }
                });
                builder.show();

               break;
        }
    }

    private void closeKeyboard() {
        //关闭软键盘
        imm.hideSoftInputFromWindow(etName.getWindowToken(), 0);
        //恢复位置
        mAlertViewExt.setMarginBottom(0);
    }

    @Override
    public void onItemClick(Object o, int position) {

    }
}
