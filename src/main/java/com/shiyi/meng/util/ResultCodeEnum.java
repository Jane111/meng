package com.shiyi.meng.util;


public enum ResultCodeEnum
{
	SUCCESS("10000", "Success"),
	NET_ERROR("10001","网络错误,请稍后再试"),
	NOT_FIND("10002","访问资源不存在"),
	DATABASE_ERROR("10003","数据库异常，请稍后重试"),
	UPDATE_ERROR("10004","修改失败"),
	ADD_ERROR("10005","添加失败"),
	DELETE_ERROR("10006","删除失败"),
	FIND_ERROR("10007","查询失败"),
    DO_NOT_IN_TIME("20007","不在有效时间内刷新"),
	NOT_ENOUGH_PARA("10009", "请将信息填写完整"),
	DATA_ERROR("10010","参数格式错误"),
	EXIST_USER("10011","该学号已存在"),
	EXIST_USER_EMAIL("10012","邮箱已经被注册"),
	EXIST_USER_PHONE("10013","手机号已经被注册"),
	ERROR_ACCOUNT_OR_PASSWORD("10014","账户或密码错误"),
	NOT_LOGIN("10015", "您还未登录，请先登录！"),
	EXIST_A("10016","该公司已注册"),
	ADD_FAILURE("20001","添加失败"),
	DELETE_FAILURE("20003","删除失败"),
	FIND_FAILURE("20004","查询失败"),
	UPDATE_FAILURE("20005","修改失败"),
	DO_NOT_HAVE_RIGHT("20006","没有权限申请"),
    HAS_REGISTER("20008","已经申请入驻过"),
	WRONG_CODE("20009","验证码错误"),
	WRONG("20010","短信发送失败，请一分钟后再次尝试");

	private String code;
    private String desc;

    ResultCodeEnum(String code, String desc)
    {
        this.code = code;
        this.desc = desc;
    }

    public String getCode()
    {
        return code;
    }

    public String getDesc()
    {
        return desc;
    }


}
