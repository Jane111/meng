package com.shiyi.meng.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseAbnormalstore<M extends BaseAbnormalstore<M>> extends Model<M> implements IBean {

	public void setAsId(java.math.BigInteger asId) {
		set("asId", asId);
	}

	public java.math.BigInteger getAsId() {
		return get("asId");
	}

	public void setAsStore(java.math.BigInteger asStore) {
		set("asStore", asStore);
	}

	public java.math.BigInteger getAsStore() {
		return get("asStore");
	}

	public void setAsUser(java.math.BigInteger asUser) {
		set("asUser", asUser);
	}

	public java.math.BigInteger getAsUser() {
		return get("asUser");
	}

	public void setAsContact(String asContact) {
		set("asContact", asContact);
	}

	public String getAsContact() {
		return get("asContact");
	}

	public void setAsPhone(String asPhone) {
		set("asPhone", asPhone);
	}

	public String getAsPhone() {
		return get("asPhone");
	}

	public void setAsType(Integer asType) {
		set("asType", asType);
	}

	public Integer getAsType() {
		return get("asType");
	}

	public void setAsReason(String asReason) {
		set("asReason", asReason);
	}

	public String getAsReason() {
		return get("asReason");
	}

	public void setAsPhoto(String asPhoto) {
		set("asPhoto", asPhoto);
	}

	public String getAsPhoto() {
		return get("asPhoto");
	}

	public void setAsStatus(Integer asStatus) {
		set("asStatus", asStatus);
	}

	public Integer getAsStatus() {
		return get("asStatus");
	}

	public void setAsFormId(String asFormId) {
		set("asFormId", asFormId);
	}

	public String getAsFormId() {
		return get("asFormId");
	}

	public void setAsCreateTime(java.util.Date asCreateTime) {
		set("asCreateTime", asCreateTime);
	}

	public java.util.Date getAsCreateTime() {
		return get("asCreateTime");
	}

	public void setAsModifyTime(java.util.Date asModifyTime) {
		set("asModifyTime", asModifyTime);
	}

	public java.util.Date getAsModifyTime() {
		return get("asModifyTime");
	}

}
