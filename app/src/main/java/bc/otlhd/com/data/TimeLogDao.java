package bc.otlhd.com.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lib.common.hxp.db.CartSQL;
import com.lib.common.hxp.db.DatabaseManager;
import com.lib.common.hxp.db.SQLHelper;
import com.lib.common.hxp.db.SetPriceSQL;
import com.lib.common.hxp.db.TimeLogSQL;

import java.util.ArrayList;
import java.util.List;

import bc.otlhd.com.bean.GoodPrices;
import bc.otlhd.com.bean.Goods;
import bc.otlhd.com.bean.TimeLogBean;
import bc.otlhd.com.cons.Constance;

/**
 * Created by gamekonglee on 2019/5/27.
 */

public class TimeLogDao {

    public TimeLogDao(Context context) {
        SQLHelper helper = new SQLHelper(context, Constance.DB_NAME, null, Constance.DB_VERSION);
        DatabaseManager.initializeInstance(helper);
    }
    /**
     * 添加(或更新)一条记录
     *
     * @param
     * @return -1:添加（更新）失败；否则添加成功
     */
    public long replaceOne(TimeLogBean bean) {
        long result = -1;
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

            ContentValues values = new ContentValues();
            values.put(TimeLogSQL.SIGNID, bean.getSignId());
            values.put(TimeLogSQL.LOGTIME, bean.getLogTime());
            values.put(TimeLogSQL.LOGTPYE, bean.getLogType());

            if (db.isOpen()) {
                result = db.insert(TimeLogSQL.TABLE_NAME, null, values);
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
    public List<TimeLogBean> getAll() {
        Cursor cursor = null;
        List<TimeLogBean> beans = new ArrayList<>();
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            if (db.isOpen()) {
                cursor = db.query(TimeLogSQL.TABLE_NAME, null, null, null, null, null, null);
                while (cursor.moveToNext()) {
                    String  pId = cursor.getString(cursor.getColumnIndex(TimeLogSQL.SIGNID));
                    String  logTime= cursor.getString(cursor.getColumnIndex(TimeLogSQL.LOGTIME));
                    String  logType= cursor.getString(cursor.getColumnIndex(TimeLogSQL.LOGTPYE));
                    TimeLogBean bean = new TimeLogBean();
                    bean.setSignId(pId);
                    bean.setLogTime(logTime);
                    bean.setLogType(logType);
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
}
