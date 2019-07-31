package bc.otlhd.com.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import bc.otlhd.com.R;
import bc.otlhd.com.cons.Constance;
import bocang.utils.AppUtils;

/**
 * @author: Jun
 * @date : 2017/1/21 13:46
 * @description : 选择方案类型
 */
public class OrderGvAdapter extends BaseAdapter {
    private JSONArray  mOrderes;
    private Activity mContext;
    private Intent mIntent;
    private List<Boolean> mIsClick;

    public OrderGvAdapter(Activity context, JSONArray orderes) {
        mContext=context;
        mOrderes=orderes;
        mIsClick=new ArrayList<>();
        for(int i=0;i<mOrderes.size();i++){
            mIsClick.add(false);
        }

    }


    @Override
    public int getCount() {
        if (null == mOrderes)
            return 0;
        return mOrderes.size();
    }

    @Override
    public JSONObject getItem(int position) {
        if (null == mOrderes)
            return null;
        return mOrderes.getJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_order, null);

            holder = new ViewHolder();

            holder.name_tv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.goods_summoney_tv = (TextView) convertView.findViewById(R.id.goods_summoney_tv);
            holder.goods_sum_tv = (TextView) convertView.findViewById(R.id.goods_sum_tv);
            holder.property_tv = (TextView) convertView.findViewById(R.id.property_tv);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        JSONObject object = mOrderes.getJSONObject(position);
        String num=object.getString(Constance.total_amount);
        String totalMoney=object.getString(Constance.total_price);
        holder.goods_sum_tv.setText("X" +num+ "件");
        holder.goods_summoney_tv.setText("￥"+object.getString( Constance.product_price ));
        holder.name_tv.setText(object.getJSONObject(Constance.product).getString(Constance.name));
        String property=object.getJSONObject(Constance.product).getString(Constance.property);
        if(!AppUtils.isEmpty(property)){
            holder.property_tv.setText(property);
        }
        ImageLoader.getInstance().displayImage(object.getJSONObject(Constance.product).
                getJSONArray(Constance.photos).getJSONObject(0).getString(Constance.thumb), holder.imageView);
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        TextView name_tv;
        TextView goods_summoney_tv;
        TextView goods_sum_tv;
        TextView property_tv;
    }




}
