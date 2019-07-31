package bc.otlhd.com.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import bc.otlhd.com.bean.Goods;
import bc.otlhd.com.bean.SceneBean;
import bc.otlhd.com.utils.upload.UpAppUtils;
import bocang.utils.AppUtils;

/**
 * Created by gamekonglee on 2019/3/30.
 */

public class SceneDao {
    private static SQLiteDatabase mDatabase = null;
    /**
     *获取产品列表
     * @return
     */
    public static List<SceneBean> getSceneList(String filter_attr_str, int page) {
        String classIdSql = " ";
//
//        if (!AppUtils.isEmpty(class_id) && !class_id.equals("0")) {
//            classIdSql = "and class_id=" + class_id + " ";
//        }
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
                filterAttrStr = "where  g.name like '%"+filter_attr_str+"%'";
//                filterAttrStr = "where a.attr_value in(" + filterAttrStr.substring(0, filterAttrStr.length() - 1) + ") ";
//                havingSql = "having count(*) > " + count + " ";
            }
        }


//        String sortsql = "";
//        if (!AppUtils.isEmpty(sort)) {
//            switch (sort) {
//                case "is_best":
//                    sortsql = "and g.is_best=1 ";
//                    break;
//                case "is_new":
//                    sortsql = "and g.is_new=1 ";
//                    break;
//                case "is_hot":
//                    sortsql = "and g.is_hot=1 ";
//                    break;
//            }
//        }
//        if (AppUtils.isEmpty(keywords)) {
//            keywords = "";
//        }
        String sql = "SELECT  g.*  FROM bc_scene AS g left join bc_goods_attr AS a  on g.id = a.scene_id "+filterAttrStr
                + classIdSql + " group by g.id " + havingSql + "order by g.id  limit 20," + (page ) * 20 + "";
//        String sql = "SELECT   g.*  FROM bc_scene AS g left join bc_goods_attr AS a  on g.id = a.scene_id  "
//                 + "group by g.id " + "order by g.id  limit 20," + (page - 1) * 20 + "";
        Log.v("520it", sql);
        List<SceneBean> proList = new ArrayList<SceneBean>();
        try {
            mDatabase = SQLiteDatabase.openDatabase(UpAppUtils.UP_SAVEPATH + File.separator +"data"+ "/data.sqlite", null, SQLiteDatabase.OPEN_READWRITE);
            Cursor cursor = mDatabase.rawQuery(sql, null);
            if (null != cursor) {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String path = cursor.getString(cursor.getColumnIndex("path"));
                    SceneBean bean = new SceneBean();
                    bean.setId(id+"");
                    bean.setName(name);
                    bean.setPath(path);
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
