package bc.otlhd.com.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import bc.otlhd.com.bean.Goods;
import bc.otlhd.com.utils.upload.UpAppUtils;
import bocang.utils.AppUtils;

/**
 * @author: Jun
 * @date : 2017/3/2 16:44
 * @description :
 */
public class GoodsDao {

    private static SQLiteDatabase mDatabase = null;

    //    public GoodsDao() {
    //        mDatabase = SQLiteDatabase.openDatabase(UpAppUtils.UP_SAVEPATH + File.separator + "data.db", null, SQLiteDatabase.OPEN_READWRITE);
    //    }

    /**
     *获取产品列表
     * @return
     */
    public static List<Goods> getGoodsList(String keywords, String class_id, String filter_attr_str, String sort, int size, int page) {
        String classIdSql = " ";

        if (!AppUtils.isEmpty(class_id) && !class_id.equals("0")) {
            classIdSql = "and class_id=" + class_id + " ";
        }
        String filterAttrStr = "";
        String havingSql = " ";
        int count = -1;

        if (!AppUtils.isEmpty(filter_attr_str)) {
            String[] split = filter_attr_str.split("\\.");

            for (int i = 0; i < split.length; i++) {
                if (!split[i].equals("")) {
                    filterAttrStr += "'" + split[i] + "',";
                    count += 1;
                }
            }
            if (count != -1) {
                filterAttrStr = "and attr_value in(" + filterAttrStr.substring(0, filterAttrStr.length() - 1) + ") ";
                havingSql = "having count(*) > " + count + " ";
            }
        }


        String sortsql = "";
        if (!AppUtils.isEmpty(sort)) {
            switch (sort) {
                case "is_best":
                    sortsql = "and g.is_best=1 ";
                    break;
                case "is_new":
                    sortsql = "and g.is_new=1 ";
                    break;
                case "is_hot":
                    sortsql = "and g.is_hot=1 ";
                    break;
            }
        }

        if (AppUtils.isEmpty(keywords)) {
            keywords = "";
        }

        String sql = "SELECT  g.*  FROM bc_goods AS g left join bc_goods_attr AS a  on g.id = a.goods_id   where g.name like '%" + keywords + "%' " + filterAttrStr + sortsql
                + classIdSql + "group by g.id " + havingSql + "order by g.id desc limit " + size + " offset " + (page - 1) * size + "";

        Log.v("520it", sql);

        List<Goods> proList = new ArrayList<Goods>();
        try {
            mDatabase = SQLiteDatabase.openDatabase(UpAppUtils.UP_SAVEPATH + File.separator +"data"+ "/data.sqlite", null, SQLiteDatabase.OPEN_READWRITE);
            Cursor cursor = mDatabase.rawQuery(sql, null);
            if (null != cursor) {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String img_url = cursor.getString(cursor.getColumnIndex("img_url"));
                    float market_price = cursor.getFloat(cursor.getColumnIndex("market_price"));
                    float shop_price = cursor.getFloat(cursor.getColumnIndex("shop_price"));
                    int goods_number = cursor.getInt(cursor.getColumnIndex("goods_number"));
                    int is_on_sale=cursor.getInt(cursor.getColumnIndex("is_on_sale"));

                    Goods bean = new Goods();
                    bean.setId(id);
                    bean.setName(name);
                    bean.setShop_price(shop_price);
                    bean.setMarket_price(""+market_price);
                    bean.setImg_url(img_url);
                    bean.setGoods_number(goods_number + "");
                    bean.setIs_on_sale(is_on_sale+"");
                    proList.add(bean);
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != mDatabase) {
                mDatabase.close();
            }
        }
        return proList;
    }
    /**
     *获取产品列表
     * @return
     */
    public static List<Goods> getGoodsList(String keywords, String class_id, String filter_attr_str, String sort, int size, int page,String is_on_sale) {
        String classIdSql = " ";

        if (!AppUtils.isEmpty(class_id) && !class_id.equals("0")) {
            classIdSql = "and class_id=" + class_id + " ";
        }
        String filterAttrStr = "";
        String havingSql = " ";
        int count = -1;

        if (!AppUtils.isEmpty(filter_attr_str)) {
            String[] split = filter_attr_str.split("\\.");

            for (int i = 0; i < split.length; i++) {
                if (!split[i].equals("")) {
                    filterAttrStr += "'" + split[i] + "',";
                    count += 1;
                }
            }
            if (count != -1) {
                filterAttrStr = "and attr_value in(" + filterAttrStr.substring(0, filterAttrStr.length() - 1) + ")  " ;
                havingSql = "having count(*) > " + count + " ";
            }
        }


        String sortsql = "";
        if (!AppUtils.isEmpty(sort)) {
            switch (sort) {
                case "is_best":
                    sortsql = "and g.is_best=1 ";
                    break;
                case "is_new":
                    sortsql = "and g.is_new=1 ";
                    break;
                case "is_hot":
                    sortsql = "and g.is_hot=1 ";
                    break;
            }
        }

        if (AppUtils.isEmpty(keywords)) {
            keywords = "";
        }

        String sql = "SELECT  g.*  FROM bc_goods AS g left join bc_goods_attr AS a  on g.id = a.goods_id   where g.name like '%" + keywords + "%' " + filterAttrStr+" and is_on_sale = " +is_on_sale + " "+sortsql
                + classIdSql + "group by g.id " + havingSql + "order by g.id desc limit " + size + " offset " + (page - 1) * size + "";

        Log.v("520it", sql);

        List<Goods> proList = new ArrayList<Goods>();
        try {
            mDatabase = SQLiteDatabase.openDatabase(UpAppUtils.UP_SAVEPATH + File.separator +"data"+ "/data.sqlite", null, SQLiteDatabase.OPEN_READWRITE);
            Cursor cursor = mDatabase.rawQuery(sql, null);
            if (null != cursor) {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String img_url = cursor.getString(cursor.getColumnIndex("img_url"));
                    float market_price = cursor.getFloat(cursor.getColumnIndex("market_price"));
                    float shop_price = cursor.getFloat(cursor.getColumnIndex("shop_price"));
                    int goods_number = cursor.getInt(cursor.getColumnIndex("goods_number"));
                    int is_onsale=cursor.getInt(cursor.getColumnIndex("is_on_sale"));

                    Goods bean = new Goods();
                    bean.setId(id);
                    bean.setName(name);
                    bean.setShop_price(shop_price);
                    bean.setMarket_price(""+market_price);
                    bean.setImg_url(img_url);
                    bean.setGoods_number(goods_number + "");
                    bean.setIs_on_sale(is_onsale+"");
                    proList.add(bean);
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != mDatabase) {
                mDatabase.close();
            }
        }
        return proList;
    }

    /**
     * 获取产品参数
     * @param id 产品Id
     * @return
     */
    public static List<String> getAttrForGood(String id) {
        List<String> attrList = new ArrayList<>();
        String sql = "SELECT  a.attr_value,c.attr_name  FROM bc_goods AS g left join bc_goods_attr AS a  " +
                "on g.id = a.goods_id  left join bc_attribute c on a.attr_id=c.id   where  g.id='" + id + "'";
        try {
            mDatabase = SQLiteDatabase.openDatabase(UpAppUtils.UP_SAVEPATH + File.separator +"data"+  "/data.sqlite", null, SQLiteDatabase.OPEN_READWRITE);
            Cursor cursor = mDatabase.rawQuery(sql, null);
            if (null != cursor) {
                while (cursor.moveToNext()) {
                    String attr_value = cursor.getString(cursor.getColumnIndex("attr_value"));
                    String attr_name = cursor.getString(cursor.getColumnIndex("attr_name"));
                    if(!AppUtils.isEmpty(attr_value))
                    attrList.add(attr_name + ":" + attr_value);
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != mDatabase) {
                mDatabase.close();
            }
        }
        return attrList;
    }
}
