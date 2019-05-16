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

	//图像存储相关信息
//	public static  final String Secret_id="AKID0jJtXvZOlMn7RVnncuQtJn1zgOyIHFWK";
//	public static final String Secret_key="FzUgdXUmobAwSsIWtQG8l8HI3cy3A4jC";
//    public static final String PASSWORD = "123456";
//	public static final String PATH = "E:/IDEAworkspace/cross2u/Cross2U/blockchain/keystore/UTC--2019-03-19T03-20-23.563424700Z--b3bc658eeee4972b29596e3441e9cfbab06fc651";
//
//	public static final String ADDRESS = "http://localhost:9000";
//	public static final BigInteger GAS_PRICE = BigInteger.valueOf(1);
//	public static final BigInteger GAS_LIMIT =BigInteger.valueOf(41000) ;
}