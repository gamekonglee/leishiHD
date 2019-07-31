package bc.otlhd.com.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import bc.otlhd.com.bean.ArticlesBean;
import bc.otlhd.com.bean.Goods;
import bc.otlhd.com.bean.NewsBean;
import bc.otlhd.com.utils.upload.UpAppUtils;

/**
 * Created by gamekonglee on 2019/5/11.
 */

public class NewsDao  {

    private static SQLiteDatabase mDatabase;

    public static   List<ArticlesBean> getNewsList(){
    String sql = "SELECT  *  FROM bc_news";

        Log.v("520it", sql);

    List<ArticlesBean> proList = new ArrayList<ArticlesBean>();
        try {
            mDatabase = SQLiteDatabase.openDatabase(UpAppUtils.UP_SAVEPATH + File.separator +"data"+ "/data.sqlite", null, SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = mDatabase.rawQuery(sql, null);
        if (null != cursor) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String path = cursor.getString(cursor.getColumnIndex("path"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String desc = cursor.getString(cursor.getColumnIndex("desc"));
                String time = cursor.getString(cursor.getColumnIndex("time"));

                ArticlesBean bean = new ArticlesBean();
                bean.setId(id);
                bean.setName(name);
                bean.setPath(path);
                bean.setDesc(desc);
                bean.setTime(time);
                bean.setContent(content+"");
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
}
