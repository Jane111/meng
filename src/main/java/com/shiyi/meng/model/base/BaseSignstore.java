package com.shiyi.meng.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

import java.math.BigInteger;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseSignstore<M extends BaseSignstore<M>> extends Model<M> implements IBean {

	public void setSsId(java.math.BigInteger ssId) {
		set("ssId", ssId);
	}

	public java.math.BigInteger getSsId() {
		return get("ssId");
	}

	public void setSsStore(java.math.BigInteger ssStore) {
		set("ssStore", ssStore);
	}

	public java.math.BigInteger getSsStore() {
		return get("ssStore");
	}

	public void setSsUser(java.math.BigInteger ssUser) {
		set("ssUser", ssUser);
	}

	public java.math.BigInteger getSsUser() {
		return get("ssUser");
	}

	public void setSsStatus(Integer ssStatus) {
		set("ssStatus", ssStatus);
	}

	public Integer getSsStatus() {
		return get("ssStatus");
	}

    public void setSsIsContract(Integer ssIsContract) {
        set("ssIsContract", ssIsContract);
    }

    public Integer getSsIsContract() {
        return get("ssIsContract");
    }

    public void setSsIsMoney(Integer ssIsMoney) {
        set("ssIsMoney", ssIsMoney);
    }

    public Integer getSsIsMoney() {
        return get("ssIsMoney");
    }

    public void setSsStopDeal(BigInteger ssStopDeal) {
        set("ssStopDeal", ssStopDeal);
    }

    public BigInteger getSsStopDeal() {
        return get("ssStopDeal");
    }

	public void setSsCreateTime(java.util.Date ssCreateTime) {
		set("ssCreateTime", ssCreateTime);
	}

	public java.util.Date getSsCreateTime() {
		return get("ssCreateTime");
	}

	public void setSsModifyTime(java.util.Date ssModifyTime) {
		set("ssModifyTime", ssModifyTime);
	}

	public java.util.Date getSsModifyTime() {
		return get("ssModifyTime");
	}

}
