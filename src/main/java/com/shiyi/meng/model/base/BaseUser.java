package com.shiyi.meng.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseUser<M extends BaseUser<M>> extends Model<M> implements IBean {

	public void setUId(java.math.BigInteger uId) {
		set("uId", uId);
	}

	public java.math.BigInteger getUId() {
		return get("uId");
	}

	public void setUCOpenId(String uCOpenId) {
		set("uCOpenId", uCOpenId);
	}

	public String getUCOpenId() {
		return get("uCOpenId");
	}

	public void setUUnionId(String uUnionId) {
		set("uUnionId", uUnionId);
	}

	public String getUUnionId() {
		return get("uUnionId");
	}

	public void setUGOpenId(String uGOpenId) {
		set("uGOpenId", uGOpenId);
	}

	public String getUGOpenId() {
		return get("uGOpenId");
	}

	public void setUWeiXinIcon(String uWeiXinIcon) {
		set("uWeiXinIcon", uWeiXinIcon);
	}

	public String getUWeiXinIcon() {
		return get("uWeiXinIcon");
	}

	public void setUWeiXinName(String uWeiXinName) {
		set("uWeiXinName", uWeiXinName);
	}

	public String getUWeiXinName() {
		return get("uWeiXinName");
	}

	public void setUCity(String uCity) {
		set("uCity", uCity);
	}

	public String getUCity() {
		return get("uCity");
	}

	public void setUCreateTime(java.util.Date uCreateTime) {
		set("uCreateTime", uCreateTime);
	}

	public java.util.Date getUCreateTime() {
		return get("uCreateTime");
	}

	public void setUModifyTime(java.util.Date uModifyTime) {
		set("uModifyTime", uModifyTime);
	}

	public java.util.Date getUModifyTime() {
		return get("uModifyTime");
	}

}
