package com.shiyi.meng.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;
import sun.security.pkcs11.P11Util;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseDevice<M extends BaseDevice<M>> extends Model<M> implements IBean {

	public void setDId(java.math.BigInteger dId) {
		set("dId", dId);
	}

	public java.math.BigInteger getDId() {
		return get("dId");
	}

	public void setDType(java.math.BigInteger dType) {
		set("dType", dType);
	}

	public java.math.BigInteger getDType() {
		return get("dType");
	}

	public void setDOwner(java.math.BigInteger dOwner) {
		set("dOwner", dOwner);
	}

	public java.math.BigInteger getDOwner() {
		return get("dOwner");
	}

	public void setDName(String dName) {
		set("dName", dName);
	}

	public String getDName() {
		return get("dName");
	}

	public void setDPhoto(String dPhoto) {
		set("dPhoto", dPhoto);
	}

	public String getDPhoto() {
		return get("dPhoto");
	}

	public void setDOPrice(Float dOPrice) {
		set("dOPrice", dOPrice);
	}

	public Float getDOPrice() {
		return get("dOPrice");
	}

	public void setDSPrice(Float dSPrice) {
		set("dSPrice", dSPrice);
	}

	public Float getDSPrice() {
		return get("dSPrice");
	}

	public void setDDegree(Integer dDegree) {
		set("dDegree", dDegree);
	}

	public Integer getDDegree() {
		return get("dDegree");
	}

	public void setDPhone(String dPhone) {
		set("dPhone", dPhone);
	}

	public String getDPhone() {
		return get("dPhone");
	}

	public void setDLocation(String dLocation) {
		set("dLocation", dLocation);
	}

	public String getDLocation() {
		return get("dLocation");
	}

	public void setDDcpt(String dDcpt) {
		set("dDcpt", dDcpt);
	}

	public String getDDcpt() {
		return get("dDcpt");
	}

	public void setDPostage(Integer dPostage) {
		set("dPostage", dPostage);
	}

	public Integer getDPostage() {
		return get("dPostage");
	}

	public void setDStatus(Integer dStatus) {
		set("dStatus", dStatus);
	}

	public Integer getDStatus() {
		return get("dStatus");
	}

	public void setDHand(Integer dHand) {
		set("dHand", dHand);
	}

	public Integer getDHand() {
		return get("dHand");
	}

	public void setDCreateTime(java.util.Date dCreateTime) {
		set("dCreateTime", dCreateTime);
	}

	public java.util.Date getDCreateTime() {
		return get("dCreateTime");
	}

	public void setDModifyTime(java.util.Date dModifyTime) {
		set("dModifyTime", dModifyTime);
	}

	public java.util.Date getDModifyTime() {
		return get("dModifyTime");
	}

	public String getDLng(){return get("dLng");}

	public void setDLng(String dLng){set("dLng", dLng);}

	public String getDLat(){ return("dLat");}

	public void setDLat(String dLat){ set("dLat",dLat);}

	public void setDOtherConnect(String dOtherConnect){set("dOtherConnect",dOtherConnect);}

	public String getDOtherConnet(){return("dOtherConnect");}
}
