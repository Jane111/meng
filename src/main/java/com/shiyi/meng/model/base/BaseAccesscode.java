package com.shiyi.meng.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

@SuppressWarnings("serial")
public abstract class BaseAccesscode<M extends com.shiyi.meng.model.base.BaseAccesscode<M>> extends Model<M> implements IBean {
    public void setAcId(java.math.BigInteger acId) {
        set("acId", acId);
    }

    public java.math.BigInteger getAcId() {
        return get("acId");
    }


    public String getAcCode() {
        return get("acCode");
    }

    public void setAcCode(String acCode) {
        set("acCode", acCode);
    }

    public String getAcTime() {
        return get("acTime");
    }

    public void setAcTime(String acTime) {
        set("acTime", acTime);
    }

    public void setAcCreateTime(java.util.Date acCreateTime) {
        set("acCreateTime", acCreateTime);
    }

    public java.util.Date getAcCreateTime() {
        return get("acCreateTime");
    }

    public void setAcModifyTime(java.util.Date acModifyTime) {
        set("acModifyTime", acModifyTime);
    }

    public java.util.Date getAcModifyTime() {
        return get("acModifyTime");
    }
}
