package bc.otlhd.com.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xpHuang on 2016/8/18.
 */
public class Goods implements Serializable{
    private int id;//产品的ID
    private String name;//产品的名称
    private String goods_name;//产品的编码
    private String img_url;//产品的URL名称
    private String market_price;// 产品市场售价
    private float shop_price;//产品的app售价
    private String goods_desc;//HTML，产品的详情页
    private String sort;//排序
    private String add_time;//添加时间
    private String is_best;//是否推荐产品（精品），1为是，0为不是
    private String is_new;//是否最新产品（新品），1为是，0为不是
    private String is_hot;//是否热卖产品（热品），1为是，0为不是
    private String is_on_sale;//是否上架销售，1为是，2为不是
    private String goods_number;//产品数量
    private String class_id;//分类ID
    private String goods_type;//商品类型
    private String click;//点击次数
    private String video_url;
    private double agio;//折扣
    private Attr attr;
    List<Gallery> gallery=new ArrayList<>();

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public Attr getAttr() {
        return attr;
    }

    public void setAttr(Attr attr) {
        this.attr = attr;
    }

    public boolean isDelete() {
        return delete;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public List<Gallery> getGallery() {
        return gallery;
    }

    public void setGallery(List<Gallery> gallery) {
        this.gallery = gallery;
    }

    public double getAgio() {
        return agio;
    }

    public void setAgio(double agio) {
        this.agio = agio;
    }


    public boolean delete;//购物车里的产品是否处于选中状态
    public int buyCount;

    public int getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(int buyCount) {
        this.buyCount = buyCount;
    }
/**
 * Copyright 2019 bejson.com
 */

    /**
     * Auto-generated: 2019-02-28 17:29:59
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */
    public class Gallery implements Serializable{

        private String filetype;
        private String img_url;
        private String id;
        private String uploadtime;
        private String goods_id;
        private String filesize;
        private String sort;
        public void setFiletype(String filetype) {
            this.filetype = filetype;
        }
        public String getFiletype() {
            return filetype;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }
        public String getImg_url() {
            return img_url;
        }

        public void setId(String id) {
            this.id = id;
        }
        public String getId() {
            return id;
        }

        public void setUploadtime(String uploadtime) {
            this.uploadtime = uploadtime;
        }
        public String getUploadtime() {
            return uploadtime;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }
        public String getGoods_id() {
            return goods_id;
        }

        public void setFilesize(String filesize) {
            this.filesize = filesize;
        }
        public String getFilesize() {
            return filesize;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }
        public String getSort() {
            return sort;
        }

    }
    @Override
    public String toString() {
        return "Goods{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", img_url='" + img_url + '\'' +
                ", market_price='" + market_price + '\'' +
                ", shop_price=" + shop_price +
                ", goods_desc='" + goods_desc + '\'' +
                ", sort='" + sort + '\'' +
                ", add_time='" + add_time + '\'' +
                ", is_best='" + is_best + '\'' +
                ", is_new='" + is_new + '\'' +
                ", is_hot='" + is_hot + '\'' +
                ", is_on_sale='" + is_on_sale + '\'' +
                ", goods_number='" + goods_number + '\'' +
                ", class_id='" + class_id + '\'' +
                ", goods_type='" + goods_type + '\'' +
                ", click='" + click + '\'' +
                ", delete=" + delete +
                ", buyCount=" + buyCount +
                '}';
    }

    public String getGoods_desc() {
        return goods_desc;
    }

    public void setGoods_desc(String goods_desc) {
        this.goods_desc = goods_desc;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getIs_on_sale() {
        return is_on_sale;
    }

    public void setIs_on_sale(String is_on_sale) {
        this.is_on_sale = is_on_sale;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getGoods_type() {
        return goods_type;
    }

    public void setGoods_type(String goods_type) {
        this.goods_type = goods_type;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public float getShop_price() {
        return shop_price;
    }

    public void setShop_price(float shop_price) {
        this.shop_price = shop_price;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getIs_best() {
        return is_best;
    }

    public void setIs_best(String is_best) {
        this.is_best = is_best;
    }

    public String getIs_new() {
        return is_new;
    }

    public void setIs_new(String is_new) {
        this.is_new = is_new;
    }

    public String getIs_hot() {
        return is_hot;
    }

    public void setIs_hot(String is_hot) {
        this.is_hot = is_hot;
    }

    public String getClick() {
        return click;
    }

    public void setClick(String click) {
        this.click = click;
    }

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    public String getGoods_number() {
        return goods_number;
    }

    public void setGoods_number(String goods_number) {
        this.goods_number = goods_number;
    }


}
