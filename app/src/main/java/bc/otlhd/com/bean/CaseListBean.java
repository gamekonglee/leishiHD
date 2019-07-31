package bc.otlhd.com.bean;

import java.util.List;

/**
 * Created by gamekonglee on 2019/3/4.
 */

public class CaseListBean {
    /**
     * Copyright 2019 bejson.com
     */

    /**
     * Auto-generated: 2019-03-04 10:42:34
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */

        private String id;
        private String name;
        private String desc;
        private String path;
        private List<Data> data;
        public void setId(String id) {
            this.id = id;
        }
        public String getId() {
            return id;
        }

        public void setName(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
        public String getDesc() {
            return desc;
        }

        public void setPath(String path) {
            this.path = path;
        }
        public String getPath() {
            return path;
        }

        public void setData(List<Data> data) {
            this.data = data;
        }
        public List<Data> getData() {
            return data;
        }
/**
 * Copyright 2019 bejson.com
 */

    /**
     * Auto-generated: 2019-03-04 10:42:34
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */
    public class Data {

        private String id;
        private String name;
        private String sort;
        private String cat_id;
        private String atta_id;
        private String title;
        private String filepath;
        private String filetype;
        private String filesize;
        private String uploadtime;
        public void setId(String id) {
            this.id = id;
        }
        public String getId() {
            return id;
        }

        public void setName(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }
        public String getSort() {
            return sort;
        }

        public void setCat_id(String cat_id) {
            this.cat_id = cat_id;
        }
        public String getCat_id() {
            return cat_id;
        }

        public void setAtta_id(String atta_id) {
            this.atta_id = atta_id;
        }
        public String getAtta_id() {
            return atta_id;
        }

        public void setTitle(String title) {
            this.title = title;
        }
        public String getTitle() {
            return title;
        }

        public void setFilepath(String filepath) {
            this.filepath = filepath;
        }
        public String getFilepath() {
            return filepath;
        }

        public void setFiletype(String filetype) {
            this.filetype = filetype;
        }
        public String getFiletype() {
            return filetype;
        }

        public void setFilesize(String filesize) {
            this.filesize = filesize;
        }
        public String getFilesize() {
            return filesize;
        }

        public void setUploadtime(String uploadtime) {
            this.uploadtime = uploadtime;
        }
        public String getUploadtime() {
            return uploadtime;
        }

    }
}
