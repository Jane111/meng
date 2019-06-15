package com.shiyi.meng.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

public abstract class BaseSmscode<M extends BaseSmscode<M>> extends Model<M> implements IBean {


    public void setSmsId(java.math.BigInteger smsId) {
        set("smsId", smsId);
    }

    public java.math.BigInteger getSmsId() {
        return get("smsId");
    }

    public void setSmsPhone(String smsPhone) {
        set("smsPhone", smsPhone);
    }

    public String getSmsPhone() {
        return get("smsPhone");
    }

    public void setSmsCode(String smsCode) {
        set("smsCode", smsCode);
    }

    public int getSmsType() {
        return get("smsType");
    }

    public void setSmsType(int smsType) {
        set("smsType", smsType);
    }

    public String getSmsCode() {
        return get("smsCode");
    }

    public void setSmsCreateTime(java.util.Date smsCreateTime) {
        set("smsCreateTime", smsCreateTime);
    }

    public java.util.Date getSmsCreateTime() {
        return get("smsCreateTime");
    }

    public void setSmsModifyTime(java.util.Date smsModifyTime) {
        set("smsModifyTime", smsModifyTime);
    }

    public java.util.Date getSmsModifyTime() {
        return get("smsModifyTime");
    }
}