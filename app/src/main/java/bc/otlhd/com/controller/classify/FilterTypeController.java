package bc.otlhd.com.controller.classify;

import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import bc.otlhd.com.R;
import bc.otlhd.com.bean.AttrList;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.controller.BaseController;
import bc.otlhd.com.ui.activity.buy.FilterTypeActivity;
import bc.otlhd.com.ui.adapter.FilterTypeAdapter;
import bc.otlhd.com.ui.fragment.FilterGoodsFragment;
import bocang.json.JSONObject;

/**
 * @author: Jun
 * @date : 2017/3/8 10:51
 * @description :
 */
public class FilterTypeController extends BaseController implements AdapterView.OnItemClickListener {
    private FilterTypeActivity mView;
    public FilterTypeAdapter mAdapter;
    private ListView listView;

    public FilterTypeController(FilterTypeActivity v){
        mView=v;
        initView();
        initViewData();
    }

    private void initViewData() {

    }

    private void initView() {
        listView = (ListView) mView.findViewById(R.id.type_lv);
        mAdapter=new FilterTypeAdapter(mView);
        listView.setAdapter(mAdapter);
        mAdapter.setData(mView.mAttrArray);
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JSONObject attrList = mView.mAttrArray.getJSONObject(position);
        mView.mIntent=new Intent();
        mView.mIntent.putExtra(Constance.attr_value, attrList.getString(Constance.attr_value));
        mView.mIntent.putExtra(Constance.id, attrList.getString(Constance.id));
        mView.setResult(Constance.FROMFILTER, mView.mIntent);//告诉原来的Activity 将数据传递给它
        AttrList attr=new AttrList();
        attr.setId(attrList.getInt(Constance.id));
        attr.setAttr_value(attrList.getString(Constance.attr_value));
        FilterGoodsFragment.mAttrList=attr;

        mView.finish();//一定要调用该方法 关闭新的AC 此时 老是AC才能获取到Itent里面的值
    }
}
