package bc.otlhd.com.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lib.common.hxp.db.DatabaseManager;
import com.lib.common.hxp.db.SQLHelper;
import com.lib.common.hxp.db.UploadImageSQL;

import java.util.ArrayList;
import java.util.List;

import bc.otlhd.com.bean.UploadImage;
import bc.otlhd.com.cons.Constance;

/**
 * @author: Jun
 * @date : 2017/3/2 16:44
 * @description :
 */
public class UploadImageDao {
    private final String TAG = UploadImageDao.class.getSimpleName();

    public UploadImageDao(Context context) {

        SQLHelper helper = new SQLHelper(context, Constance.DB_NAME, null, Constance.DB_VERSION);
        DatabaseManager.initializeInstance(helper);
    }

    /**
     * 添加(或更新)一条记录
     * @return -1:添加（更新）失败；否则添加成功
     */
    public long replaceOne(UploadImage bean) {
        long result = -1;
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

            ContentValues values = new ContentValues();
            values.put(UploadImageSQL.PID, bean.getId());
            values.put(UploadImageSQL.NAME, bean.getName());

            if (db.isOpen()) {
                result = db.replace(UploadImageSQL.TABLE_NAME, null, values);
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
    public List<UploadImage> getAll() {
        Cursor cursor = null;
        List<UploadImage> beans = new ArrayList<>();
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            if (db.isOpen()) {
                cursor = db.query(UploadImageSQL.TABLE_NAME, null, null, null, null, null, "id desc");
                while (cursor.moveToNext()) {
                    int Id = cursor.getInt(cursor.getColumnIndex(UploadImageSQL.ID));
                    String name = cursor.getString(cursor.getColumnIndex(UploadImageSQL.NAME));
                    UploadImage bean = new UploadImage();
                    bean.setId(Id);
                    bean.setName(name);

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
     * @return 删除成功记录数
     */
    public int deleteOne(int id) {
        int result = 0;
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            if (db.isOpen()) {
                result = db.delete(UploadImageSQL.TABLE_NAME, UploadImageSQL.ID + "=?", new String[]{String.valueOf(id)});
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseManager.getInstance().closeDatabase();
        }
        return result;
    }

    /**
     * 删除所有记录
     *
     * @return 删除成功记录数
     */
    public int deleteAll() {
        int result = 0;
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            if (db.isOpen()) {
                result = db.delete(UploadImageSQL.TABLE_NAME, null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseManager.getInstance().closeDatabase();
        }
        return result;
    }


    /**
     * 按条件查询图片数据
     * @param name
     * @return
     */
    public Boolean getImage(String name){
        Cursor cursor = null;
        UploadImage bean=new UploadImage();
        Boolean isResult=false;
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            if (db.isOpen()) {
                cursor=db.query(UploadImageSQL.TABLE_NAME, null, UploadImageSQL.NAME + "=?", new String[]{name}, null, null, null);
                while (cursor.moveToNext()) {
                    int pId = cursor.getInt(cursor.getColumnIndex(UploadImageSQL.PID));
                    String imageName = cursor.getString(cursor.getColumnIndex(UploadImageSQL.NAME));
                    bean.setId(pId);
                    bean.setName(imageName);
                    isResult=true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseManager.getInstance().closeDatabase();
        }
        return isResult;
    }



}
