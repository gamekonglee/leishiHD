package bc.otlhd.com.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lib.common.hxp.db.DatabaseManager;
import com.lib.common.hxp.db.SQLHelper;
import com.lib.common.hxp.db.SetPriceSQL;

import java.util.ArrayList;
import java.util.List;

import bc.otlhd.com.bean.GoodPrices;
import bc.otlhd.com.cons.Constance;

/**
 * @author: Jun
 * @date : 2017/4/24 11:05
 * @description :
 */
public class SetPriceDao {
    private final String TAG = CartDao.class.getSimpleName();

    public SetPriceDao(Context context) {

        SQLHelper helper = new SQLHelper(context, Constance.DB_NAME, null, Constance.DB_VERSION);
        DatabaseManager.initializeInstance(helper);
    }

    /**
     * 添加(或更新)一条记录
     *
     * @param
     * @return -1:添加（更新）失败；否则添加成功
     */
    public long replaceOne(GoodPrices bean) {
        long result = -1;
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

            ContentValues values = new ContentValues();
            values.put(SetPriceSQL.PID, bean.getId());
            values.put(SetPriceSQL.PRICE, bean.getShop_price());

            if (db.isOpen()) {
                result = db.replace(SetPriceSQL.TABLE_NAME, null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseManager.getInstance().closeDatabase();
        }
        return result;
    }

    /**
     * 获取所有记录
     *
     * @return
     */
    public List<GoodPrices> getAll() {
        Cursor cursor = null;
        List<GoodPrices> beans = new ArrayList<>();
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            if (db.isOpen()) {
                cursor = db.query(SetPriceSQL.TABLE_NAME, null, null, null, null, null, null);
                while (cursor.moveToNext()) {
                    int pId = cursor.getInt(cursor.getColumnIndex(SetPriceSQL.PID));
                    float price = cursor.getFloat(cursor.getColumnIndex(SetPriceSQL.PRICE));
                    GoodPrices bean = new GoodPrices();
                    bean.setId(pId);
                    bean.setShop_price(price);
                    beans.add(bean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != cursor)
                cursor.close();
            DatabaseManager.getInstance().closeDatabase();
        }
        return beans;
    }

    /**
     * 删除一个记录
     *
     * @param pId 产品id
     * @return 删除成功记录数
     */
    public int deleteOne(int pId) {
        int result = 0;
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            if (db.isOpen()) {
                result = db.delete(SetPriceSQL.TABLE_NAME, SetPriceSQL.PID + "=?", new String[]{String.valueOf(pId)});
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseManager.getInstance().closeDatabase();
        }
        return result;
    }


    public GoodPrices getProductPrice(String id){
        Cursor cursor = null;
        GoodPrices bean=new GoodPrices();
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            if (db.isOpen()) {
                cursor=db.query(SetPriceSQL.TABLE_NAME,null, SetPriceSQL.PID + "=?", new String[]{id}, null, null, null);
                while (cursor.moveToNext()) {
                    int pId = cursor.getInt(cursor.getColumnIndex(SetPriceSQL.PID));
                    float price = cursor.getFloat(cursor.getColumnIndex(SetPriceSQL.PRICE));
                    bean.setId(pId);
                    bean.setShop_price(price);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseManager.getInstance().closeDatabase();
        }
        return bean;
    }
}