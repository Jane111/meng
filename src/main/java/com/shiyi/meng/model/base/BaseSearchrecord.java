package com.shiyi.meng.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseSearchrecord<M extends BaseSearchrecord<M>> extends Model<M> implements IBean {

	public void setSrId(java.math.BigInteger srId) {
		set("srId", srId);
	}

	public java.math.BigInteger getSrId() {
		return get("srId");
	}

	public void setSrOwner(java.math.BigInteger srOwner) {
		set("srOwner", srOwner);
	}

	public java.math.BigInteger getSrOwner() {
		return get("srOwner");
	}

	public void setSrContent(String srContent) {
		set("srContent", srContent);
	}

	public String getSrContent() {
		return get("srContent");
	}

	public void setSrCreateTime(java.util.Date srCreateTime) {
		set("srCreateTime", srCreateTime);
	}

	public java.util.Date getSrCreateTime() {
		return get("srCreateTime");
	}

	public void setSrModifyTime(java.util.Date srModifyTime) {
		set("srModifyTime", srModifyTime);
	}

	public java.util.Date getSrModifyTime() {
		return get("srModifyTime");
	}

}