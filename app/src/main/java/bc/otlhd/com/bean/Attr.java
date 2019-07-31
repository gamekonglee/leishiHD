package bc.otlhd.com.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gamekonglee on 2019/2/28.
 */

public class Attr implements Serializable{
    /**
     * Copyright 2019 bejson.com
     */

    /**
     * Auto-generated: 2019-02-28 17:29:59
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */

        private List<Pro> pro;
        private List<Spe> spe;
        public void setPro(List<Pro> pro) {
            this.pro = pro;
        }
        public List<Pro> getPro() {
            return pro;
        }

        public void setSpe(List<Spe> spe) {
            this.spe = spe;
        }
        public List<Spe> getSpe() {
            return spe;
        }

}
