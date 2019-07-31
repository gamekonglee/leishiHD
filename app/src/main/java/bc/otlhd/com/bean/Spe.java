package bc.otlhd.com.bean;

import java.util.List;

/**
 * Created by gamekonglee on 2019/2/28.
 */

public class Spe {
    /**
     * Copyright 2019 bejson.com
     */
    /**
     * Auto-generated: 2019-02-28 17:29:59
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */

        private String id;
        private String name;
        private String attr_type;
        private List<Values> values;
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

        public void setAttr_type(String attr_type) {
            this.attr_type = attr_type;
        }
        public String getAttr_type() {
            return attr_type;
        }

        public void setValues(List<Values> values) {
            this.values = values;
        }
        public List<Values> getValues() {
            return values;
        }

}
