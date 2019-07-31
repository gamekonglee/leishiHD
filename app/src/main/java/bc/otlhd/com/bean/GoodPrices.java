package bc.otlhd.com.bean;

import java.io.Serializable;

/**
 * Created by xpHuang on 2016/8/18.
 */
public class GoodPrices implements Serializable{
    private int id;//产品的ID
    private float shop_price;//产品的app售价


    @Override
    public String toString() {
        return "Goods{" +
                "id=" + id +
                ", shop_price=" + shop_price +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getShop_price() {
        return shop_price;
    }

    public void setShop_price(float shop_price) {
        this.shop_price = shop_price;
    }



}
