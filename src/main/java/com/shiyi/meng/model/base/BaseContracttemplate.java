package com.shiyi.meng.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseContracttemplate<M extends BaseContracttemplate<M>> extends Model<M> implements IBean {

	public void setCtId(java.math.BigInteger ctId) {
		set("ctId", ctId);
	}

	public java.math.BigInteger getCtId() {
		return get("ctId");
	}

	public void setCtContent(String ctContent) {
		set("ctContent", ctContent);
	}

	public String getCtContent() {
		return get("ctContent");
	}

	public void setCtCreateTime(java.util.Date ctCreateTime) {
		set("ctCreateTime", ctCreateTime);
	}

	public java.util.Date getCtCreateTime() {
		return get("ctCreateTime");
	}

	public void setCtModifyTime(java.util.Date ctModifyTime) {
		set("ctModifyTime", ctModifyTime);
	}

	public java.util.Date getCtModifyTime() {
		return get("ctModifyTime");
	}

}
