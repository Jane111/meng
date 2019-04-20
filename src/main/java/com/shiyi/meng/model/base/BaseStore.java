package com.shiyi.meng.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseStore<M extends BaseStore<M>> extends Model<M> implements IBean {

	public void setSId(java.math.BigInteger sId) {
		set("sId", sId);
	}

	public java.math.BigInteger getSId() {
		return get("sId");
	}

	public void setSUId(java.math.BigInteger sUId) {
		set("sUId", sUId);
	}

	public java.math.BigInteger getSUId() {
		return get("sUId");
	}

	public void setSColumn(Integer sColumn) {
		set("sColumn", sColumn);
	}

	public Integer getSColumn() {
		return get("sColumn");
	}

	public void setSPhoto(String sPhoto) {
		set("sPhoto", sPhoto);
	}

	public String getSPhoto() {
		return get("sPhoto");
	}

	public void setSName(String sName) {
		set("sName", sName);
	}

	public String getSName() {
		return get("sName");
	}

	public void setSDes(String sDes) {
		set("sDes", sDes);
	}

	public String getSDes() {
		return get("sDes");
	}

	public void setSLoc(String sLoc) {
		set("sLoc", sLoc);
	}

	public String getSLoc() {
		return get("sLoc");
	}

	public void setSLng(Integer sLng) {
		set("sLng", sLng);
	}

	public Integer getSLng() {
		return get("sLng");
	}

	public void setSLat(Integer sLat) {
		set("sLat", sLat);
	}

	public Integer getSLat() {
		return get("sLat");
	}

	public void setSType(String sType) {
		set("sType", sType);
	}

	public String getSType() {
		return get("sType");
	}

	public void setSAera(Float sAera) {
		set("sAera", sAera);
	}

	public Float getSAera() {
		return get("sAera");
	}

	public void setSRentMoney(String sRentMoney) {
		set("sRentMoney", sRentMoney);
	}

	public String getSRentMoney() {
		return get("sRentMoney");
	}

	public void setSTranMoney(String sTranMoney) {
		set("sTranMoney", sTranMoney);
	}

	public String getSTranMoney() {
		return get("sTranMoney");
	}

	public void setSPayMethod(Integer sPayMethod) {
		set("sPayMethod", sPayMethod);
	}

	public Integer getSPayMethod() {
		return get("sPayMethod");
	}

	public void setSFloor(Integer sFloor) {
		set("sFloor", sFloor);
	}

	public Integer getSFloor() {
		return get("sFloor");
	}

	public void setSStoreStatus(String sStoreStatus) {
		set("sStoreStatus", sStoreStatus);
	}

	public String getSStoreStatus() {
		return get("sStoreStatus");
	}

	public void setSLeftMoney(String sLeftMoney) {
		set("sLeftMoney", sLeftMoney);
	}

	public String getSLeftMoney() {
		return get("sLeftMoney");
	}

	public void setSPhone(String sPhone) {
		set("sPhone", sPhone);
	}

	public String getSPhone() {
		return get("sPhone");
	}

	public void setSConName(String sConName) {
		set("sConName", sConName);
	}

	public String getSConName() {
		return get("sConName");
	}

	public void setSClick(String sClick) {
		set("sClick", sClick);
	}

	public String getSClick() {
		return get("sClick");
	}

	public void setSTag(String sTag) {
		set("sTag", sTag);
	}

	public String getSTag() {
		return get("sTag");
	}

	public void setSLicense(String sLicense) {
		set("sLicense", sLicense);
	}

	public String getSLicense() {
		return get("sLicense");
	}

	public void setSIdCard(String sIdCard) {
		set("sIdCard", sIdCard);
	}

	public String getSIdCard() {
		return get("sIdCard");
	}

	public void setSECharge(Float sECharge) {
		set("sECharge", sECharge);
	}

	public Float getSECharge() {
		return get("sECharge");
	}

	public void setSWCharge(Float sWCharge) {
		set("sWCharge", sWCharge);
	}

	public Float getSWCharge() {
		return get("sWCharge");
	}

	public void setSFee(Float sFee) {
		set("sFee", sFee);
	}

	public Float getSFee() {
		return get("sFee");
	}

	public void setSHasPark(Integer sHasPark) {
		set("sHasPark", sHasPark);
	}

	public Integer getSHasPark() {
		return get("sHasPark");
	}

	public void setSComment(String sComment) {
		set("sComment", sComment);
	}

	public String getSComment() {
		return get("sComment");
	}

	public void setSWareHouse(Integer sWareHouse) {
		set("sWareHouse", sWareHouse);
	}

	public Integer getSWareHouse() {
		return get("sWareHouse");
	}

	public void setSStatus(Integer sStatus) {
		set("sStatus", sStatus);
	}

	public Integer getSStatus() {
		return get("sStatus");
	}

	public void setSDeposit(Float sDeposit) {
		set("sDeposit", sDeposit);
	}

	public Float getSDeposit() {
		return get("sDeposit");
	}

	public void setSRefuseReason(String sRefuseReason) {
		set("sRefuseReason", sRefuseReason);
	}

	public String getSRefuseReason() {
		return get("sRefuseReason");
	}

	public void setSCreateTime(java.util.Date sCreateTime) {
		set("sCreateTime", sCreateTime);
	}

	public java.util.Date getSCreateTime() {
		return get("sCreateTime");
	}

	public void setSModifyTime(java.util.Date sModifyTime) {
		set("sModifyTime", sModifyTime);
	}

	public java.util.Date getSModifyTime() {
		return get("sModifyTime");
	}

}