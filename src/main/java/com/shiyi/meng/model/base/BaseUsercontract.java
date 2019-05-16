package com.shiyi.meng.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

import java.math.BigInteger;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseUsercontract<M extends BaseUsercontract<M>> extends Model<M> implements IBean {

	public void setUcId(java.math.BigInteger ucId) {
		set("ucId", ucId);
	}

	public java.math.BigInteger getUcId() {
		return get("ucId");
	}

	public void setUcOwner(java.math.BigInteger ucOwner) {
		set("ucOwner", ucOwner);
	}

	public java.math.BigInteger getUcOwner() {
		return get("ucOwner");
	}

	public void setUcContent(String ucContent) {
		set("ucContent", ucContent);
	}

	public String getUcContent() {
		return get("ucContent");
	}

	public void setUcStore(BigInteger ucStore) {
		set("ucStore", ucStore);
	}

	public BigInteger getUcStore() {
		return get("ucStore");
	}

	public void setUcCreateTime(java.util.Date ucCreateTime) {
		set("ucCreateTime", ucCreateTime);
	}

	public java.util.Date getUcCreateTime() {
		return get("ucCreateTime");
	}

	public void setUcModifyTime(java.util.Date ucModifyTime) {
		set("ucModifyTime", ucModifyTime);
	}

	public java.util.Date getUcModifyTime() {
		return get("ucModifyTime");
	}

}
