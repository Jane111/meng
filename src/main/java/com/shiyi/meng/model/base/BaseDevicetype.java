package com.shiyi.meng.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseDevicetype<M extends BaseDevicetype<M>> extends Model<M> implements IBean {

	public void setDtId(java.math.BigInteger dtId) {
		set("dtId", dtId);
	}

	public java.math.BigInteger getDtId() {
		return get("dtId");
	}

	public void setDtFather(java.math.BigInteger dtFather) {
		set("dtFather", dtFather);
	}

	public java.math.BigInteger getDtFather() {
		return get("dtFather");
	}

	public void setDtName(String dtName) {
		set("dtName", dtName);
	}

	public String getDtName() {
		return get("dtName");
	}

	public void setDtCreateTime(java.util.Date dtCreateTime) {
		set("dtCreateTime", dtCreateTime);
	}

	public java.util.Date getDtCreateTime() {
		return get("dtCreateTime");
	}

	public void setDtModifyTime(java.util.Date dtModifyTime) {
		set("dtModifyTime", dtModifyTime);
	}

	public java.util.Date getDtModifyTime() {
		return get("dtModifyTime");
	}

}
