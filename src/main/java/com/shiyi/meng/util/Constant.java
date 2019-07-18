package com.shiyi.meng.util;

import java.math.BigInteger;

public class Constant {

	//小程序相关信息
	public static final String LOGINURL="https://api.weixin.qq.com/sns/jscode2session";
	public static final String APPID="wxe3d360d7f680f247";
	public static final String APPSECRET="3fa1771b7860267bbcc74c15e6b2044e";
	public static final String GRANTTYPE="authorization_code";

    /*微信小程序支付相关信息*/
	public static String MCH_ID = "1530460071";//商户号
	public static String NOTIFY_URL = "localhost:8080/user/wxProPayNotify";//回调地址
	public static String KEY = "2cb6b821ae8dbd38fa5e4f80f72b32c9";//API密钥，交易过程中生成签名的密钥

    //返款对应参数
    public static  final String RETURNMONEYTOBUY="98";
    public static  final String RETURNMONEYTOSALEREDUCE="968";

	//图像存储相关信息
	public static  final String Secret_id="AKIDiGFMJ2b3D9r10pop0wHPp5C5j7LiAvCG";
	public static final String Secret_key="rSoukW6RWP3RbVKuEdqoWzD3Ki1yc5My";

	//腾讯短信相关参数
	public static final int MessageAppId=1400208584;
	public static final String MessageAppKey="bada81849a95754d3ed30613335e2081";
	public static final int SMS_uploadDevice=331776;//发布设备 验证手机
	public static final int SMS_applyStore=331777;//入驻申请验证手机
	public static final int SMS_mengSignId=209928;//萌系餐饮人 短信签名id
	public static final String SMS_mengSign="萌系餐饮";//萌系餐饮人 短信签名id

	public static final int dbStatus_Pass=2;//设备商状态 2-审核通过
	public static final int dbStatus_Wait=0;//待审核
	public static final int dbStatus_NotPass=1;//审核不通过

	public static final int dbType_Repair=3;//设备商类别-维修商
	public static final int dbType_New=1;//新设备商
	public static final int dbType_Second=2;//二手设备商

	public static final int dStatus_Wait=0;//0-待审核 1-下架 2-在架 3-审核失败
	public static final int dStatus_Down=1;
	public static final int dStatus_ON=2;
	public static final int dStatus_Faliure=3;

    public static final int dHand_New=1;//新设备
    public static final int dHand_Second=2;//二手设备

    public static final int reportD_Wait=0;
    public static final int reportD_NotPass=1;//否
    public static final int reportD_Pass=2;//过


    public static final int userContrac_Wait=0;
    public static final int userContrac_Not=1;
    public static final int userContrac_Pass=2;
}