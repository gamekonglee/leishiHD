package bc.otlhd.com.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lib.common.hxp.db.DatabaseManager;
import com.lib.common.hxp.db.ProgrammeSQL;
import com.lib.common.hxp.db.SQLHelper;

import java.util.ArrayList;
import java.util.List;

import bc.otlhd.com.bean.Programme;
import bc.otlhd.com.cons.Constance;
import bocang.utils.AppUtils;

/**
 * Created by xpHuang on 2016/9/29.
 */

public class ProgrammeDao {
    private final String TAG = ProgrammeDao.class.getSimpleName();

    public ProgrammeDao(Context context) {

        SQLHelper helper = new SQLHelper(context, Constance.DB_NAME, null, Constance.DB_VERSION);
        DatabaseManager.initializeInstance(helper);
    }

    /**
     * 添加(或更新)一条记录
     *
     * @param
     * @return -1:添加（更新）失败；否则添加成功
     */
    public long replaceOne(Programme bean) {
        long result = -1;
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

            ContentValues values = new ContentValues();
            values.put(ProgrammeSQL.PID, bean.getId());
            values.put(ProgrammeSQL.GOODSID, bean.getGoods_id());
            values.put(ProgrammeSQL.TITLE, bean.getTitle());
            values.put(ProgrammeSQL.SHAREID, bean.getShareid());
            values.put(ProgrammeSQL.STYLE, bean.getStyle());
            values.put(ProgrammeSQL.SPACE, bean.getSpace());
            values.put(ProgrammeSQL.SCEENPATH, bean.getSceenpath());
            values.put(ProgrammeSQL.CONTENT, bean.getSpace());
            values.put(ProgrammeSQL.PIMAGE, bean.getpImage());

            if (db.isOpen()) {
                result = db.replace(ProgrammeSQL.TABLE_NAME, null, values);
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
    public List<Programme> getAll() {
        Cursor cursor = null;
        List<Programme> beans = new ArrayList<>();
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            if (db.isOpen()) {
                cursor = db.query(ProgrammeSQL.TABLE_NAME, null, null, null, null, null, null);
                while (cursor.moveToNext()) {
                    int pId = cursor.getInt(cursor.getColumnIndex(ProgrammeSQL.PID));
                    String goods_id = cursor.getString(cursor.getColumnIndex(ProgrammeSQL.GOODSID));
                    String title = cursor.getString(cursor.getColumnIndex(ProgrammeSQL.TITLE));
                    String shareid = cursor.getString(cursor.getColumnIndex(ProgrammeSQL.SHAREID));
                    String style = cursor.getString(cursor.getColumnIndex(ProgrammeSQL.STYLE));
                    String space = cursor.getString(cursor.getColumnIndex(ProgrammeSQL.SPACE));
                    String sceenpath = cursor.getString(cursor.getColumnIndex(ProgrammeSQL.SCEENPATH));
                    String content = cursor.getString(cursor.getColumnIndex(ProgrammeSQL.CONTENT));
                    byte[] pimage=cursor.getBlob(cursor.getColumnIndex(ProgrammeSQL.PIMAGE));
                    Programme bean=new Programme();
                    bean.setId(pId);
                    bean.setGoods_id(goods_id);
                    bean.setShareid(shareid);
                    bean.setSpace(space);
                    bean.setTitle(title);
                    bean.setStyle(style);
                    bean.setContent(content);
                    bean.setContent(sceenpath);
                    bean.setpImage(pimage);
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
     * 按条件获取记录
     *
     * @return
     */
    public List<Programme> getData(String mstyle,String mspace,int id) {
        Cursor cursor = null;
        List<Programme> beans = new ArrayList<>();
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            String lastTimeSql="";
            if (db.isOpen()) {
                if(id!=0){
                    lastTimeSql = "select * from "+ProgrammeSQL.TABLE_NAME+" where id='"+id+"' order by id desc";
                }else{
                    if(AppUtils.isEmpty(mstyle) &&AppUtils.isEmpty(mspace)){
                        lastTimeSql = "select * from "+ProgrammeSQL.TABLE_NAME+" order by id desc";
                    }else if(!AppUtils.isEmpty(mstyle) && AppUtils.isEmpty(mspace)){
                        lastTimeSql = "select * from "+ProgrammeSQL.TABLE_NAME+" where style='"+mstyle+"' order by id desc";
                    }else if(AppUtils.isEmpty(mstyle) && !AppUtils.isEmpty(mspace)){
                        lastTimeSql = "select * from "+ProgrammeSQL.TABLE_NAME+" where space='"+mspace+"' order by id desc";
                    }else if(!AppUtils.isEmpty(mstyle) && !AppUtils.isEmpty(mspace)){
                        lastTimeSql = "select * from "+ProgrammeSQL.TABLE_NAME+" where style='"+mstyle+"' and space='"+mspace+"' order by id desc";
                    }
                }

                 cursor = db.rawQuery(lastTimeSql, null);
                while (cursor.moveToNext()) {
                    int pId = cursor.getInt(cursor.getColumnIndex(ProgrammeSQL.PID));
                    String goods_id = cursor.getString(cursor.getColumnIndex(ProgrammeSQL.GOODSID));
                    String title = cursor.getString(cursor.getColumnIndex(ProgrammeSQL.TITLE));
                    String shareid = cursor.getString(cursor.getColumnIndex(ProgrammeSQL.SHAREID));
                    String style = cursor.getString(cursor.getColumnIndex(ProgrammeSQL.STYLE));
                    String space = cursor.getString(cursor.getColumnIndex(ProgrammeSQL.SPACE));
                    String content = cursor.getString(cursor.getColumnIndex(ProgrammeSQL.CONTENT));
                    String sceenpath = cursor.getString(cursor.getColumnIndex(ProgrammeSQL.SCEENPATH));
                    byte[] pimage=cursor.getBlob(cursor.getColumnIndex(ProgrammeSQL.PIMAGE));
                    Programme bean=new Programme();
                    bean.setId(pId);
                    bean.setGoods_id(goods_id);
                    bean.setShareid(shareid);
                    bean.setSpace(space);
                    bean.setTitle(title);
                    bean.setStyle(style);
                    bean.setContent(content);
                    bean.setSceenpath(sceenpath);
                    bean.setpImage(pimage);
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
                result = db.delete(ProgrammeSQL.TABLE_NAME, ProgrammeSQL.PID + "=?", new String[]{String.valueOf(pId)});
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
                result = db.delete(ProgrammeSQL.TABLE_NAME, null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseManager.getInstance().closeDatabase();
        }
        return result;
    }
}
