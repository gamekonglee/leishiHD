package bc.otlhd.com.controller.product;

import android.os.Message;
import android.widget.ListView;

import bc.otlhd.com.R;
import bc.otlhd.com.controller.BaseController;
import bc.otlhd.com.ui.adapter.ParamentAdapter;
import bc.otlhd.com.ui.fragment.ParameterFragment;

/**
 * @author: Jun
 * @date : 2017/2/14 11:05
 * @description :
 */
public class ParameterController extends BaseController {
    private ParameterFragment mView;
    private ParamentAdapter mAdapter;
    private ListView parameter_lv;
    private com.alibaba.fastjson.JSONObject mProductObject;


    public ParameterController(ParameterFragment v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {
        sendProductDetail();
    }


    private void initView() {
        parameter_lv = (ListView) mView.getActivity().findViewById(R.id.parameter_lv);
        parameter_lv.setDivider(null);//去除listview的下划线
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    /**
     * 产品详情
     */
    public void sendProductDetail() {
//        mProductObject= ((ProDetailActivity)mView.getActivity()).mProductObject;
//       if(AppUtils.isEmpty(mProductObject))
//        return;
//        com.alibaba.fastjson.JSONArray attachArray = mProductObject.getJSONArray(Constance.attachments);
//        mAdapter = new ParamentAdapter(attachArray, mView.getContext());
//        parameter_lv.setAdapter(mAdapter);
    }
}
