package bc.otlhd.com.bean;

import java.io.Serializable;

/**
 * @author: Jun
 * @date : 2017/4/28 9:14
 * @description :
 */
public class UploadImage implements Serializable {
    private int id;//图片的ID
    private String name;//图片的名称

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

    @Override
    public String toString() {
        return "UploadImage{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
