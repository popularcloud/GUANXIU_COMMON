package com.lwc.common.bean;

/**
 * @author younge
 * @date 2019/8/5 0005
 * @email 2276559259@qq.com
 */
public class CodeUrlBean {
    /**
     * url : 2
     * img : 2
     * 	"url": "2",//邀请码链接
     *         "img": "2",//邀请码背景图
     *         "color": "2",//邀请码按钮颜色
     *         "description": "2",//邀请码文字
     *         "qrcode": "2",//二维码
     */

    private String url;
    private String img;
    private String color;
    private String description;
    private String qrcode;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }
}
