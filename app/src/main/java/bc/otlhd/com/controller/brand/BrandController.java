package bc.otlhd.com.controller.brand;

import android.content.Intent;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.ImageLoader;

import bc.otlhd.com.R;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.cons.NetWorkConst;
import bc.otlhd.com.controller.BaseController;
import bc.otlhd.com.ui.activity.brand.BrandDetailActivity;
import bc.otlhd.com.ui.activity.brand.BrandPlayActivity;
import bc.otlhd.com.ui.activity.brand.ShowRoomWebActivity;
import bc.otlhd.com.ui.fragment.BrandFragment;
import bc.otlhd.com.utils.net.HttpListener;
import bocang.utils.MyToast;
import cn.qqtheme.framework.util.LogUtils;

/**
 * @author: Jun
 * @date : 2017/5/8 13:42
 * @description :
 */
public class BrandController extends BaseController implements HttpListener, AdapterView.OnItemClickListener {
    private BrandFragment mView;
    private RelativeLayout main_rl;
    private GridView grid;
    private JSONArray mBrandArray;
    private ProAdapter mProAdapter;

    public BrandController(BrandFragment v){
        mView=v;
        initView();
        initViewData();
    }

    private void initViewData() {
        sendCategoryList();
    }

    private void initView() {
        main_rl = (RelativeLayout) mView.getView().findViewById(R.id.main_rl);
        grid = (GridView) mView.getView().findViewById(R.id.grid);
        grid.setOnItemClickListener(this);
        mProAdapter=new ProAdapter();

    }


    private void setGridView() {
        int size = mBrandArray.size();
        float size1 = (float) (mBrandArray.size()+0.6);

        int length = 350;

        DisplayMetrics dm = new DisplayMetrics();
        mView.getActivity(). getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size1 * (length + 3) * density);
        int itemWidth = (int) (length * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        grid.setLayoutParams(params); // 重点
        grid.setColumnWidth(itemWidth); // 重点
        grid.setHorizontalSpacing(40); // 间距
        grid.setStretchMode(GridView.NO_STRETCH);
        grid.setNumColumns(size); // 重点
        grid.setAdapter(mProAdapter);
        mProAdapter.notifyDataSetChanged();

    }

    private void  sendCategoryList(){
        mView.setShowDialog(true);
        mView.setShowDialog("正在获取数据...");
        mView.showLoading();
        mNetWork.sendCategoryList(mView.getActivity(),this)
        ;
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    @Override
    public void onSuccessListener(int what, JSONObject ans) {
        mView.hideLoading();
        mBrandArray = ans.getJSONArray(Constance.data);

        setGridView();

    }

    @Override
    public void onFailureListener(int what, JSONObject ans) {
        mView.hideLoading();
        if (null == mView || mView.getActivity().isFinishing())
            return;
        MyToast.show(mView.getActivity(),"数据异常！");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position==mBrandArray.size()-2){
////            Intent intent =new Intent(mView.getActivity(), BrandDetailActivity.class);
////            intent.putExtra(Constance.data,mBrandArray.getJSONObject(position).toJSONString());
////            intent.putExtra(Constance.style,1);
////            mView.getActivity().startActivity(intent);
//            IntentUtil.startActivity(mView.getActivity(), ShowRoomWebActivity.class,false);
            Intent intent =new Intent(mView.getActivity(), ShowRoomWebActivity.class);
            intent.putExtra(Constance.data,mBrandArray.getJSONObject(position).toJSONString());
            mView.getActivity().startActivity(intent);
            return;
        }

        if(mBrandArray.getJSONObject(position).getInteger("type")==3){
            Intent intent =new Intent(mView.getActivity(), BrandPlayActivity.class);
            intent.putExtra(Constance.data,mBrandArray.getJSONObject(position).toJSONString());
            mView.getActivity().startActivity(intent);
            return;
        }
        Intent intent =new Intent(mView.getActivity(), BrandDetailActivity.class);
        intent.putExtra(Constance.data,mBrandArray.getJSONObject(position).toJSONString());
        intent.putExtra(Constance.style,0);
        mView.getActivity().startActivity(intent);

    }

    private class ProAdapter extends BaseAdapter {
        public ProAdapter() {
        }

        @Override
        public int getCount() {
            if (null == mBrandArray)
                return 0;
            return mBrandArray.size();
        }

        @Override
        public JSONObject getItem(int position) {
            if (null == mBrandArray)
                return null;
            return mBrandArray.getJSONObject(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mView.getActivity(), R.layout.item_brand, null);

                holder = new ViewHolder();
                holder.ItemImage = (ImageView) convertView.findViewById(R.id.ItemImage);
                holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                holder.tv_desc = (TextView) convertView.findViewById(R.id.tv_desc);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            JSONObject object = mBrandArray.getJSONObject(position);
            holder.tv_title.setText(object.getString(Constance.name));
            holder.tv_desc.setText(object.getString(Constance.desc));
            ImageLoader.getInstance().displayImage(NetWorkConst.UR_BRAND_URL+object.getString(Constance.path), holder.ItemImage);
            return convertView;
        }

        class ViewHolder {
            ImageView ItemImage;
            TextView tv_title;
            TextView tv_desc;
        }
    }

}
