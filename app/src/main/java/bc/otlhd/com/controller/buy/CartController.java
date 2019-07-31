package bc.otlhd.com.controller.buy;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.text.InputType;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.yjn.swipelistview.swipelistviewlibrary.widget.SwipeMenu;
import com.yjn.swipelistview.swipelistviewlibrary.widget.SwipeMenuCreator;
import com.yjn.swipelistview.swipelistviewlibrary.widget.SwipeMenuItem;
import com.yjn.swipelistview.swipelistviewlibrary.widget.SwipeMenuListView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import bc.otlhd.com.R;
import bc.otlhd.com.bean.Goods;
import bc.otlhd.com.cons.Constance;
import bc.otlhd.com.cons.IntentKeys;
import bc.otlhd.com.cons.NetWorkConst;
import bc.otlhd.com.controller.BaseController;
import bc.otlhd.com.data.CartDao;
import bc.otlhd.com.data.SetPriceDao;
import bc.otlhd.com.listener.INetworkCallBack;
import bc.otlhd.com.ui.activity.EditValueActivity;
import bc.otlhd.com.ui.activity.IssueApplication;
import bc.otlhd.com.ui.activity.buy.OrderHDActivity;
import bc.otlhd.com.ui.fragment.CartFragment;
import bc.otlhd.com.ui.view.NumberInputView;
import bc.otlhd.com.utils.FileUtil;
import bc.otlhd.com.utils.UIUtils;
import bc.otlhd.com.utils.net.HttpListener;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/2/21 14:35
 * @description :
 */
public class CartController extends BaseController{
    private CartFragment mView;
    private SwipeMenuListView mListView;
    private JSONArray goodses;
    public MyAdapter myAdapter;
    private CheckBox checkAll;
    private TextView money_tv, settle_tv, edit_tv;
    private boolean isStart = false;
    private LinearLayout sum_ll;
    private Boolean isEdit = false;
    private ArrayList<Goods> mGoodChecks;
    private JSONObject mAddressObject;
    private CartDao mCartDao;
    private List<Goods> mGoodses;
    private int mGoodId;
    private SetPriceDao mPriceDao;
    private com.alibaba.fastjson.JSONArray sceneAllAttrsTemp;
    private com.alibaba.fastjson.JSONArray locations;
    private MyBaseAdapter adapter;

    public CartController(CartFragment v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {
//        mView.showLoadingPage("", R.drawable.ic_loading);
//        sendAddressList();
        mNetWork.sendGoodsList("0", 20, 0, "", "", "", false, 20,"", mView.getActivity(), new HttpListener() {
            @Override
            public void onSuccessListener(int what, com.alibaba.fastjson.JSONObject ans) {
                sceneAllAttrsTemp = ans.getJSONArray(Constance.all_attr_list);
            }

            @Override
            public void onFailureListener(int what, com.alibaba.fastjson.JSONObject ans) {

            }
        });
        mNetWork.sendGoodsList(1, "20", null, null, null, null, null, null, null, new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {

            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {

            }
        });
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                mView.getActivity().getResources().getDisplayMetrics());
    }


    private void initView() {
        mListView = (SwipeMenuListView) mView.getView().findViewById(R.id.cart_lv);
        myAdapter = new MyAdapter();
        mListView.setAdapter(myAdapter);

        final SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(mView.getActivity());

                deleteItem.setBackground(new ColorDrawable(Color.parseColor("#fe3c3a")));
                deleteItem.setWidth(dp2px(80));
                deleteItem.setTitle("删除");
                deleteItem.setTitleColor(Color.WHITE);
                deleteItem.setTitleSize(20);
                menu.addMenuItem(deleteItem);
            }
        };
        mListView.setMenuCreator(creator);

        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                if (index == 0) {
                    int id =mGoodses.get(position).getId();
                    isLastDelete = true;
                    deleteShoppingCart(id);
                    checkAll.setChecked(false);
                    myAdapter.getTotalMoney();
                    IssueApplication.mCartCount-=1;
                }
                return false;
            }
        });
        checkAll = (CheckBox) mView.getActivity().findViewById(R.id.checkAll);

        //取得设置好的drawable对象
//        Drawable drawable = mView.getActivity().getResources().getDrawable(R.drawable.selector_checkbox);

        //设置drawable对象的大小
//        drawable.setBounds(0, 0, 30, 30);

        //设置CheckBox对象的位置，对应为左、上、右、下
//        checkAll.setCompoundDrawables(null,null,drawable,null);
        money_tv = (TextView) mView.getActivity().findViewById(R.id.money_tv);
        settle_tv = (TextView) mView.getActivity().findViewById(R.id.settle_tv);
        edit_tv = (TextView) mView.getActivity().findViewById(R.id.edit_tv);
        sum_ll = (LinearLayout) mView.getActivity().findViewById(R.id.sum_ll);
        //        pd = (ProgressBar) mView.getActivity().findViewById(R.id.pd);
        //        pd.setVisibility(View.VISIBLE);


        mExtView = (ViewGroup) LayoutInflater.from(mView.getContext()).inflate(R.layout.alertext_form, null);
        etNum = (EditText) mExtView.findViewById(R.id.etName);
        etNum.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        imm = (InputMethodManager) mView.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mCartDao = new CartDao(mView.getActivity());
        mPriceDao=new SetPriceDao(mView.getActivity());
    }


    int mDeleteIndex = -1;


    private void deleteShoppingCart(int goodId) {
        mCartDao.deleteOne(goodId);
        if(isLastDelete==true){
            sendShoppingCart();
        }
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    /**
     * 编辑
     */
    public void setEdit() {
        if (!isEdit) {
            sum_ll.setVisibility(View.GONE);
            settle_tv.setText("删除");
            edit_tv.setText("完成");
            isEdit = true;

        } else {
            sum_ll.setVisibility(View.VISIBLE);
            settle_tv.setText("结算");
            edit_tv.setText("编辑");
            isEdit = false;
        }
    }

    public void sendShoppingCart() {
        mGoodses = mCartDao.getAll();

        for(int i=0;i<mGoodses.size();i++){
            float setPrice = mPriceDao.getProductPrice(mGoodses.get(i).getId() + "").getShop_price();
            mGoodses.get(i).setShop_price(setPrice==0?mGoodses.get(i).getShop_price():setPrice);
        }

        myAdapter.addIsCheckAll(false);
        myAdapter.notifyDataSetChanged();


    }


    private ArrayList<Boolean> isCheckList = new ArrayList<>();

    public void setCkeckAll(Boolean isCheck) {
        myAdapter.setIsCheckAll(isCheck);
        myAdapter.getTotalMoney();
        myAdapter.notifyDataSetChanged();

    }


    /**
     * 结算/删除
     */
    public void sendSettle() {
        if (!isEdit) {
            myAdapter.getCartGoodsCheck();
            if(mGoodChecks.size()==0){
                MyToast.show(mView.getActivity(), "请选择产品!");
                return;
            }
            Intent intent=new Intent(mView.getActivity(), OrderHDActivity.class);
            intent.putExtra(Constance.goods,mGoodChecks);
            intent.putExtra(Constance.money,mMoney);

            intent.putExtra(IntentKeys.SHOPCARDATAS, mGoodChecks);
            intent.putExtra(IntentKeys.SHOPCARTOTALPRICE,mMoney);
            intent.putExtra(IntentKeys.SHOPCARTOTALCOUNT,myAdapter.getTotalCount());
            String sumAgio = mView.sumAgioTv.getText().toString();
            intent.putExtra(IntentKeys.SHOPORDERDISCOUNT,sumAgio);

            mView.startActivity(intent);

        } else {

            sendDeleteCart();
        }
    }

    private Boolean isLastDelete = false;

    /**
     * 删除购物车数据
     */
    public void sendDeleteCart() {
        isLastDelete=false;
        ArrayList<Integer> deleteList = new ArrayList<>();
        for (int i = 0; i < isCheckList.size(); i++) {
            if (isCheckList.get(i)) {
                int id = mGoodses.get(i).getId();
                deleteList.add(id);
            }
        }
        for (int j = 0; j < deleteList.size(); j++) {
            if (j == deleteList.size() - 1) {
                isLastDelete = true;
            }
            deleteShoppingCart(deleteList.get(j));
            IssueApplication.mCartCount-=1;
        }
        checkAll.setChecked(false);
        myAdapter.getTotalMoney();
        EventBus.getDefault().post(Constance.CARTCOUNT);
    }

    float mMoney = 0;
    private String imageURL = "";

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 007) {
            String value = data.getStringExtra(Constance.VALUE);
            mCartDao.updateRemark(mGoodId, value);
        }
    }

    public class MyAdapter extends BaseAdapter {
        private DisplayImageOptions options;
        private ImageLoader imageLoader;

        public MyAdapter() {
            options = new DisplayImageOptions.Builder()
                    // 设置图片下载期间显示的图片
                    .showImageOnLoading(R.drawable.bg_default)
                            // 设置图片Uri为空或是错误的时候显示的图片
                    .showImageForEmptyUri(R.drawable.bg_default)
                            // 设置图片加载或解码过程中发生错误显示的图片
                            // .showImageOnFail(R.drawable.ic_error)
                            // 设置下载的图片是否缓存在内存中
                    .cacheInMemory(true)
                            // 设置下载的图片是否缓存在SD卡中
                    .cacheOnDisk(true)
                            // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                            // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                    .considerExifParams(true)
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片可以放大（要填满ImageView必须配置memoryCacheExtraOptions大于Imageview）
                            // .displayer(new FadeInBitmapDisplayer(100))//
                            // 图片加载好后渐入的动画时间
                    .build(); // 构建完成

            // 得到ImageLoader的实例(使用的单例模式)
            imageLoader = ImageLoader.getInstance();
        }

        public void setIsCheckAll(Boolean isCheck) {
            if (AppUtils.isEmpty(mGoodses))
                return;
            for (int i = 0; i < mGoodses.size(); i++) {
                isCheckList.set(i, isCheck);
            }
        }

        public void addIsCheckAll(Boolean isCheck) {
            isCheckList = new ArrayList<>();
            for (int i = 0; i < mGoodses.size(); i++) {
                isCheckList.add(isCheck);
            }

        }

        public void getCartGoodsCheck() {
            mGoodChecks =new ArrayList<>();
            for (int i = 0; i < isCheckList.size(); i++) {
                if (isCheckList.get(i)) {
                    mGoodChecks.add(mGoodses.get(i));
                }
            }
        }


        public void setIsCheck(int poistion, Boolean isCheck) {

            isCheckList.set(poistion, isCheck);
            getTotalMoney();


        }

        /**
         * 获取到总金额
         */
        public int getTotalMoney() {
            float isSumMoney = 0;
            if (AppUtils.isEmpty(mGoodses)) {
                money_tv.setText("￥" + 0 + "");
                return 0;
            }
            int goodsNum=0;
            for (int i = 0; i < mGoodses.size(); i++) {
                if (isCheckList.get(i) == true) {
                    goodsNum++;
                    double price = mGoodses.get(i).getShop_price();
                    int num = mGoodses.get(i).getBuyCount();
                    isSumMoney += (num * price);
                }
            }
            mMoney = isSumMoney;
            checkAll.setText("    共选"+goodsNum+"件商品");
            money_tv.setText("￥" + (int)isSumMoney + "");
            return (int) isSumMoney;
        }

        @Override
        public int getCount() {
            if (null == mGoodses)
                return 0;
            return mGoodses.size();
        }


        @Override
        public Goods getItem(int position) {
            if (null == mGoodses)
                return null;
            return mGoodses.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mView.getActivity(), R.layout.item_lv_cart, null);

                holder = new ViewHolder();
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
//                //取得设置好的drawable对象
//                Drawable drawable = mView.getActivity().getResources().getDrawable(R.drawable.selector_checkbox);
//
//                //设置drawable对象的大小
//                drawable.setBounds(0,0,30,30);
//
//                //设置CheckBox对象的位置，对应为左、上、右、下
//                holder.checkBox.setCompoundDrawables(drawable,null,null,null);
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                holder.leftTv = (ImageView) convertView.findViewById(R.id.leftTv);
                holder.rightTv = (ImageView) convertView.findViewById(R.id.rightTv);
                holder.nameTv = (TextView) convertView.findViewById(R.id.nameTv);
                holder.marketTv = (TextView) convertView.findViewById(R.id.marketTv);
                holder.numTv = (EditText) convertView.findViewById(R.id.numTv);
                holder.priceTv = (TextView) convertView.findViewById(R.id.priceTv);
                holder.add_remark_bt = (TextView) convertView.findViewById(R.id.add_remark_bt);
                holder.iv_edit= (ImageView) convertView.findViewById(R.id.iv_edit);
                holder.number_input_et = (NumberInputView) convertView.findViewById(R.id.number_input_et);
                holder.check_ll = (RelativeLayout) convertView.findViewById(R.id.check_ll);
                holder.tv_install_localtion= (TextView) convertView.findViewById(R.id.tv_install_localtion);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final Goods goods = mGoodses.get(position);
            holder.nameTv.setText(goods.getName());

            String path = FileUtil.getGoodsExternDir(goods.getImg_url());
            File imageFile = new File(path + "!400X400.png");
            if (imageFile.exists()) {
                imageURL = "file://" + imageFile.toString();
                ImageLoader.getInstance().displayImage(imageURL, holder.imageView);

            } else {
                imageLoader.displayImage(NetWorkConst.UR_PRODUCT_URL + goods.getImg_url()+ "!400X400.png", holder.imageView, options);
            }

            holder.marketTv.setText("市场价:" + goods.getMarket_price());
            float price =goods.getShop_price();

            holder.priceTv.setText("￥" + (int)price);
            holder.number_input_et.setMax(10000);//设置数量的最大值

            holder.numTv.setText(mGoodses.get(position).getBuyCount() + "");
            final String remark=goods.getGoods_desc();
            final int id=mGoodses.get(position).getId();
            Boolean ss=false;
            try{
                ss=isCheckList.get(position);
                holder.checkBox.setChecked(ss);
            }catch (Exception e){
                ss=false;
            }

//            holder.add_remark_bt.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent=new Intent(mView.getActivity(), EditValueActivity.class);
//                    mGoodId=id;
//                    intent.putExtra(Constance.TITLE,"产品备注");
//                    intent.putExtra(Constance.CODE,007);
//                    intent.putExtra(Constance.REMARK,remark);
//                    mView.startActivityForResult(intent, 007);
//                }
//            });
            holder.iv_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent=new Intent(mView.getActivity(), EditValueActivity.class);
                    mGoodId=id;
//                    intent.putExtra(Constance.TITLE,"产品备注");
//                    intent.putExtra(Constance.CODE,007);
//                    intent.putExtra(Constance.REMARK,remark);
//                    mView.startActivityForResult(intent, 007);
                    final Dialog dialog=UIUtils.showRightInDialog(mView.getActivity(),R.layout.dialog_remark,mView.getResources().getDimensionPixelSize(R.dimen.x390));
                    TextView tv_cancel= (TextView) dialog.findViewById(R.id.tv_cancel);
                    TextView tv_ensure= (TextView) dialog.findViewById(R.id.tv_ensure);
                    GridView gv_install= (GridView) dialog.findViewById(R.id.gv_install);
                    final EditText et_remark= (EditText) dialog.findViewById(R.id.et_remark);

                    tv_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    tv_ensure.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            String value = et_remark.getText().toString();
                            mCartDao.updateRemark(mGoodId, value);
                            String install_location=locations.getJSONObject(adapter.currentP).getString(Constance.attr_value);
                            mCartDao.updateRemarkInstall(mGoodId,install_location);
                            holder.add_remark_bt.setText("备注："+value);
                            holder.tv_install_localtion.setText("安装位置："+install_location);
                        }
                    });
                    locations = new com.alibaba.fastjson.JSONArray();
                    if(sceneAllAttrsTemp!=null){
                        for(int i=0;i<sceneAllAttrsTemp.size();i++){
                            if(sceneAllAttrsTemp.getJSONObject(i).getString(Constance.filter_attr_name).equals("空间")){
                                locations =sceneAllAttrsTemp.getJSONObject(i).getJSONArray(Constance.attr_list);
                                break;
                            }
                        }
                    }
                    adapter = new MyBaseAdapter();
                    gv_install.setAdapter(adapter);
                    gv_install.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            adapter.currentP=position;
                            adapter.notifyDataSetChanged();

                        }
                    });




                }
            });
//            holder.numTv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mAlertViewExt == null) {
//                        mAlertViewExt = new AlertView("提示", "修改购买数量！", "取消", null, new String[]{"完成"},
//                                mView.getActivity(), AlertView.Style.Alert, new OnItemClickListener() {
//                            @Override
//                            public void onItemClick(Object o, int position) {
//                                if (position != AlertView.CANCELPOSITION) {
//                                    String num = etNum.getText().toString();
//                                    if (num.equals("0")) {
//                                        MyToast.show(mView.getContext(), "不能等于0");
//                                        return;
//                                    }
//                                    mView.setShowDialog(true);
//                                    mView.setShowDialog("正在处理中...");
//                                    mView.showLoading();
////                                    sendUpdateCart(goodsObject.getString(Constance.id), num);
//                                }
//                            }
//                        });
//
//                        etNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                            @Override
//                            public void onFocusChange(View v, boolean hasFocus) {
//                                //输入框出来则往上移动
//                                boolean isOpen = imm.isActive();
//                                mAlertViewExt.setMarginBottom(isOpen && hasFocus ? 120 : 0);
//                                System.out.println(isOpen);
//                            }
//                        });
//                        mAlertViewExt.addExtView(mExtView);
//                    }
////                    etNum.setText(goodsObject.getInt(Constance.amount) + "");
//                    mAlertViewExt.show();
//
//                }
//            });




            holder.rightTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int num= mGoodses.get(position).getBuyCount();
                    mGoodses.get(position).setBuyCount(num+1);
                    mCartDao.updateNumData(id, num + 1);
                    holder.numTv.setText((num + 1) + "");
                    getTotalMoney();

                }
            });
            holder.leftTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int num= mGoodses.get(position).getBuyCount();
                    if (num == 1) {
                        MyToast.show(mView.getActivity(), "亲,已经到底啦!");
                        return;
                    }
                    mCartDao.updateNumData(id, num - 1);
                    mGoodses.get(position).setBuyCount(num - 1);
                    holder.numTv.setText((num-1)+"");
                    getTotalMoney();
                }
            });

            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setIsCheck(position, !isCheckList.get(position));
                    getTotalMoney();
                }
            });
//            holder.check_ll.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    boolean ischeck = holder.checkBox.isChecked();
//                    setIsCheck(position, ischeck);
//                    getTotalMoney();
//                    holder.checkBox.setChecked(!ischeck);
//                }
//            });

            return convertView;
        }

        /**
         * 合计数量
         * @return
         */
        public int getTotalCount() {
            int count = 0;
            for (int i = 0; i < mGoodses.size(); i++) {
                //判断该商品是不是选择状态
                if (mGoodses.get(i).delete ) {
                    count += mGoodses.get(i).getBuyCount();
                }
            }
            return count;
        }

        class ViewHolder {
            CheckBox checkBox;
            ImageView imageView;
            TextView nameTv;
            TextView priceTv;
            TextView marketTv;
            NumberInputView number_input_et;
            EditText numTv;
            ImageView leftTv, rightTv;
            TextView add_remark_bt;
            RelativeLayout check_ll;
            ImageView iv_edit;
            TextView tv_install_localtion;
        }
    }

    private AlertView mAlertViewExt;//窗口拓展例子
    private EditText etNum;//拓展View内容
    private InputMethodManager imm;
    private ViewGroup mExtView;


    class  MyBaseAdapter extends BaseAdapter{
        public int currentP=0;
        @Override
        public int getCount() {
            return locations.size();
        }
        public int getCurrentP(){
            return currentP;
        }
        @Override
        public Object getItem(int position) {
            return locations.getJSONObject(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder=new Holder();
            if(convertView==null){
                convertView=View.inflate(mView.getActivity(),R.layout.item_remark_dialog,null);
                holder.tv_location= (TextView) convertView.findViewById(R.id.tv_location);
//                int width=mView.getResources().getDimensionPixelSize(R.dimen.x340)/3;
//                holder.tv_location.setLayoutParams(new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT));
                convertView.setTag(holder);
            }else {
                holder= (Holder) convertView.getTag();
            }
            holder.tv_location.setText(locations.getJSONObject(position).getString(Constance.attr_value));
            if(currentP==position){
                holder.tv_location.setTextColor(mView.getResources().getColor(R.color.white));
                holder.tv_location.setBackground(mView.getResources().getDrawable(R.drawable.bg_corner_red_5));
            }else {
                holder.tv_location.setTextColor(mView.getResources().getColor(R.color.color_333333));
                holder.tv_location.setBackground(mView.getResources().getDrawable(R.drawable.bg_corner_dddddd_5));
            }
            return convertView;
        }
        class  Holder{
            TextView tv_location;
        }
    }

}
