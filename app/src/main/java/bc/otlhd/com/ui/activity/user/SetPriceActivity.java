package bc.otlhd.com.ui.activity.user;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import bc.otlhd.com.R;
import bc.otlhd.com.controller.user.SetPriceController;
import bc.otlhd.com.ui.activity.MainActivity;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/4/21 15:55
 * @description :
 */
public class SetPriceActivity extends BaseActivity {
    private SetPriceController mController;
    private EditText et_search;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController=new SetPriceController(this);

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_set_price);
        et_search = (EditText)findViewById(R.id.et_search);
        et_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                // 修改回车键功能
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(SetPriceActivity.this
                                            .getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    mController.searchData(et_search.getText().toString());
                }

                return false;
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {

    }
}
