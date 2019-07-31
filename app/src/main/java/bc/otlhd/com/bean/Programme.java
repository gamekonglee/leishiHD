package bc.otlhd.com.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author: Jun
 * @date : 2017/3/13 10:10
 * @description :
 */
public class Programme implements Serializable {
    private String attr_name;
    private List<String> attrVal;
    private int id;
    private String goods_id;
    private String title;
    private String shareid;
    private String style;
    private String space;
    private String content;
    private String sceenpath;
    private byte[] pImage;

    public String getSceenpath() {
        return sceenpath;
    }

    public void setSceenpath(String sceenpath) {
        this.sceenpath = sceenpath;
    }

    public byte[] getpImage() {
        return pImage;
    }

    public void setpImage(byte[] pImage) {
        this.pImage = pImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShareid() {
        return shareid;
    }

    public void setShareid(String shareid) {
        this.shareid = shareid;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttr_name() {
        return attr_name;
    }

    public void setAttr_name(String attr_name) {
        this.attr_name = attr_name;
    }

    public List<String> getAttrVal() {
        return attrVal;
    }

    public void setAttrVal(List<String> attrVal) {
        this.attrVal = attrVal;
    }


}
