package com.lwc.common.bean;

import java.util.List;

/**
 * Created by 何栋 on 2017/10/15.
 */
public class Menu {


    /**
     * id : 1
     * devicename : 电脑
     * properties : [{"id":1,"name":"CPU","did":1,"examples":[],"type":"string"},{"id":2,"name":"硬盘","did":1,"examples":[],"type":"string"},{"id":3,"name":"内存","did":1,"examples":[],"type":"string"},{"id":12,"name":"操作系统","did":1,"examples":[],"type":"string"}]
     * types : [{"id":1,"typename":"苹果","did":1},{"id":2,"typename":"联想","did":1},{"id":4,"typename":"惠普","did":1},{"id":5,"typename":"戴尔","did":1},{"id":23,"typename":"组装机","did":1}]
     * repairs : [{"id":2,"did":1,"repairname":"黑屏"},{"id":3,"did":1,"repairname":"蓝屏"},{"id":4,"did":1,"repairname":"开机自动重启"},{"id":5,"did":1,"repairname":"开机无反应"},{"id":6,"did":1,"repairname":"开机界面卡停"},{"id":36,"did":1,"repairname":"系统优化"},{"id":37,"did":1,"repairname":"操作系统更换"},{"id":45,"did":1,"repairname":"电脑反应慢"},{"id":52,"did":1,"repairname":"电脑上不了网"},{"id":53,"did":1,"repairname":"打开网页慢"},{"id":54,"did":1,"repairname":"打印机打印不了"},{"id":55,"did":1,"repairname":"打印机卡纸"},{"id":56,"did":1,"repairname":"打印不清晰"},{"id":57,"did":1,"repairname":"多媒体设备问题"},{"id":58,"did":1,"repairname":"电话有杂音"},{"id":59,"did":1,"repairname":"电话不通"},{"id":60,"did":1,"repairname":"复印机共享不了"},{"id":61,"did":1,"repairname":"复印机打印不了"},{"id":62,"did":1,"repairname":"复印机卡纸"},{"id":63,"did":1,"repairname":"投影机故障"},{"id":64,"did":1,"repairname":"其他"},{"id":65,"did":1,"repairname":"测试"}]

    "deviceTypeName": "电脑",                    //维修的设备类型名称
            "repairs": [
    {
        "reqair_id": "170831102758556103YH",   //维修的技能ID
            "reqair_name": "开机无反应"           //维修的技能名称
    }
    ],
            "deviceTypeId": "170831102758556102YU"       //维修的设备类型ID
     */
    private String deviceTypeId;
    private String deviceTypeName;
    private List<PropertiesBean> properties;
    private List<TypesBean> types;
    private List<RepairsBean> repairs;

    public String getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(String deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public String getDeviceTypeName() {
        return deviceTypeName;
    }

    public void setDeviceTypeName(String deviceTypeName) {
        this.deviceTypeName = deviceTypeName;
    }

    public List<PropertiesBean> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertiesBean> properties) {
        this.properties = properties;
    }

    public List<TypesBean> getTypes() {
        return types;
    }

    public void setTypes(List<TypesBean> types) {
        this.types = types;
    }

    public List<RepairsBean> getRepairs() {
        return repairs;
    }

    public void setRepairs(List<RepairsBean> repairs) {
        this.repairs = repairs;
    }

    public static class PropertiesBean {
        /**
         * id : 1
         * name : CPU
         * did : 1
         * examples : []
         * type : string
         */

        private int id;
        private String name;
        private int did;
        private String type;
        private List<?> examples;

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

        public int getDid() {
            return did;
        }

        public void setDid(int did) {
            this.did = did;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<?> getExamples() {
            return examples;
        }

        public void setExamples(List<?> examples) {
            this.examples = examples;
        }
    }

    public static class TypesBean {
        /**
         * id : 1
         * typename : 苹果
         * did : 1
         */

        private int id;
        private String typename;
        private int did;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTypename() {
            return typename;
        }

        public void setTypename(String typename) {
            this.typename = typename;
        }

        public int getDid() {
            return did;
        }

        public void setDid(int did) {
            this.did = did;
        }
    }

    public static class RepairsBean {
        /**
         * reqair_id : 2
         * did : 1
         * repairname : 黑屏
         */

        private String reqair_id;
        private String did;
        private String reqair_name;

        public String getReqair_id() {
            return reqair_id;
        }

        public void setReqair_id(String reqair_id) {
            this.reqair_id = reqair_id;
        }

        public String getReqair_name() {
            return reqair_name;
        }

        public void setReqair_name(String reqair_name) {
            this.reqair_name = reqair_name;
        }

        public String getDid() {
            return did;
        }

        public void setDid(String did) {
            this.did = did;
        }

    }
}
