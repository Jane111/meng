package com.shiyi.meng.util;


import com.shiyi.meng.model.Accesscode;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ScheduledTasks {

    //每两个小时更新access_code
//    @Scheduled(cron="* * */2 * * ?")//秒 分 时 每月第几天 月 星期
    public void getAccessCode() {//每两个小时更新一下access_code
        Map<String, String> reqParams = new HashMap<>();
        //填写 client_credential
        reqParams.put("grant_type", "client_credential");
        //appid
        reqParams.put("appid", Constant.APPID);
        //secret
        reqParams.put("secret", Constant.APPSECRET);
        /*
        获取Access_code的API
         */
        String xmlResult = PaymentApi.getAccessCode(reqParams);
        System.out.println(xmlResult);
//        Map<String, String> result = PaymentKit.xmlToMap(xmlResult);
//
//        //access_token的信息
//        String access_token = result.get("access_token");
//        String expires_in = result.get("expires_in");
//        Accesscode accesscode = new Accesscode();
//        accesscode.setAcCode(access_token);
//        accesscode.setAcTime(expires_in);
//        accesscode.save();
    }
}