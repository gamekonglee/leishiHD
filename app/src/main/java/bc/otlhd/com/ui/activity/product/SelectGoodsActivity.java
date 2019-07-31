package bc.otlhd.com.ui.activity.product;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import bc.otlhd.com.R;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.controller.product.SelectGoodsController;
import bocang.view.BaseActivity;

/**
 * @author: Jun
 * @date : 2017/2/16 17:30
 * @description :选择产品
 */
public class SelectGoodsActivity extends BaseActivity {
    private SelectGoodsController mController;
    private TextView topRightBtn;
    private TextView popularityTv, newTv, saleTv;
    private LinearLayout stylell;
    public String mCategoriesId;
    public boolean isSelectGoods=false;
    public String mFilterAttr="";

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new SelectGoodsController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_select_product);
        topRightBtn = getViewAndClick(R.id.topRightBtn);
        popularityTv = getViewAndClick(R.id.popularityTv);
        newTv = getViewAndClick(R.id.newTv);
        saleTv = getViewAndClick(R.id.saleTv);
        stylell = getViewAndClick(R.id.stylell);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mCategoriesId=intent.getStringExtra(Constance.categories);
        isSelectGoods=intent.getBooleanExtra(Constance.ISSELECTGOODS, false);
        mFilterAttr=intent.getStringExtra(Constance.filter_attr);
    }

    private boolean isPriceSort=false;

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.topRightBtn:
                mController.openClassify();
                break;
            case R.id.newTv:
                mController.selectSortType(R.id.newTv);
                break;
            case R.id.saleTv:
                mController.selectSortType(R.id.saleTv);
                break;
            case R.id.popularityTv:
                mController.selectSortType(R.id.popularityTv);
                break;
            case R.id.stylell:
                if(isPriceSort){
                    isPriceSort=false;
                    mController.selectSortType(2);
                }else{
                    isPriceSort=true;
                    mController.selectSortType(R.id.stylell);
                }
                break;
        }
    }
}
