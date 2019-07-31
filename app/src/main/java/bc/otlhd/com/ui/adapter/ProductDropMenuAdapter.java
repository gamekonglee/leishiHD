package bc.otlhd.com.ui.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baiiu.filter.adapter.MenuAdapter;
import com.baiiu.filter.adapter.SimpleTextAdapter;
import com.baiiu.filter.interfaces.OnFilterDoneListener;
import com.baiiu.filter.interfaces.OnFilterItemClickListener;
import com.baiiu.filter.typeview.SingleGridView;
import com.baiiu.filter.util.UIUtil;
import com.baiiu.filter.view.FilterCheckedTextView;

import java.util.ArrayList;
import java.util.List;

import bc.otlhd.com.R;
import bc.otlhd.com.cons.Constance;



public class ProductDropMenuAdapter implements MenuAdapter {
    private final Context mContext;
    private OnFilterDoneListener onFilterDoneListener;
    private JSONArray sceneAllAttrs;
    private List<Integer> itemPosList;

    public ProductDropMenuAdapter(Context context, JSONArray sceneAllAttrs, List<Integer> itemPosList, OnFilterDoneListener onFilterDoneListener) {
        this.mContext = context;
        this.sceneAllAttrs = sceneAllAttrs;
        this.onFilterDoneListener = onFilterDoneListener;
        this.itemPosList = itemPosList;
    }

    @Override
    public int getMenuCount() {
        return sceneAllAttrs.size();
    }

    @Override
    public String getMenuTitle(int position) {
//        if (position < itemPosList.size()) {
//            int itemPos = itemPosList.get(position);
////            if (itemPos != 0) {
////                return sceneAllAttrs.getJSONObject(position).getJSONArray(Constance.attr_list).getJSONObject(itemPos).getString(Constance.attr_value);
////            }
//        }
        String name=sceneAllAttrs.getJSONObject(position).getString(Constance.filter_attr_name);
        return name;
    }

    @Override
    public int getBottomMargin(int position) {

        return 0;
    }

    @Override
    public View getView(int position, FrameLayout parentContainer) {
        return createSingleGridView(position);
    }

    private View createSingleGridView(final int position) {
        SingleGridView<String> singleGridView = new SingleGridView<String>(mContext)
                .adapter(new SimpleTextAdapter<String>(null, mContext) {
                    @Override
                    public String provideText(String s) {
                        return s;
                    }

                    @Override
                    protected void initCheckedTextView(FilterCheckedTextView checkedTextView) {
                        checkedTextView.setPadding(0, UIUtil.dp(context, 3), 0, UIUtil.dp(context, 3));
                        checkedTextView.setGravity(Gravity.CENTER);
                        checkedTextView.setBackgroundResource(R.drawable.selector_filter_grid);
                    }
                })
                .onItemClick(new OnFilterItemClickListener<String>() {
                    @Override
                    public void onItemClick(int itemPos, String itemStr) {

                        if (onFilterDoneListener != null) {
                            onFilterDoneListener.onFilterDone(position, itemPos, itemStr);
                        }

                    }
                });

        List<String> list = new ArrayList<>();
        JSONObject goodsAllAttr = sceneAllAttrs.getJSONObject(position);
        JSONArray attr_list = goodsAllAttr.getJSONArray(Constance.attr_list);
        for (int i = 0; i < attr_list.size(); ++i) {
            list.add(attr_list.getJSONObject(i).getString(Constance.attr_value));
        }
        int itemPos = 0;
        if (position < itemPosList.size())
            itemPos = itemPosList.get(position);
        singleGridView.setList(list, itemPos);

        return singleGridView;
    }
}
