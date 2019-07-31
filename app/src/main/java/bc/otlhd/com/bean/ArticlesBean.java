package bc.otlhd.com.bean;

/**
 * Created by gamekonglee on 2019/3/14.
 */

public class ArticlesBean {
    int id;
    String name;
    String desc;
    String sort;
    String time;
    String title;
    String url;
    String path;
    private String content;
    public ArticlesBean(){}
    public ArticlesBean(int id, String title, String url){
        this.id=id;
        this.title=title;
        this.url=url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getContent(){
        return this.content;
    }
}
