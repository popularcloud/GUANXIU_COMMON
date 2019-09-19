package com.lwc.common.bean;

/**
 * @author younge
 * @date 2019/5/23 0023
 * @email 2276559259@qq.com
 * 签到对象
 */
public class SignBean {

    /**
     * msg  : 您今天已经签到过了
     * score  : 积分
     * status  : false
     */

    private String msg;
    private String score;
    private String scoreMsg;
    private String couponMsg;
    private String type;  //奖励类型  0积分  1优惠券
    private boolean status;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


    public String getScoreMsg() {
        return scoreMsg;
    }

    public void setScoreMsg(String scoreMsg) {
        this.scoreMsg = scoreMsg;
    }

    public String getCouponMsg() {
        return couponMsg;
    }

    public void setCouponMsg(String couponMsg) {
        this.couponMsg = couponMsg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
