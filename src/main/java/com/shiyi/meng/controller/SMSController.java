package com.shiyi.meng.controller;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.jfinal.plugin.activerecord.Db;
import com.shiyi.meng.service.SMSService;
import com.shiyi.meng.util.BaseResponse;
import com.shiyi.meng.util.Constant;
import com.shiyi.meng.util.ResultCodeEnum;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@Component
@RestController
@RequestMapping("/sms")
public class SMSController {

    @Autowired
    SMSService smsService=new SMSService();
    @Autowired
    BaseResponse baseResponse=new BaseResponse();

    //发布设备验证手机
    @RequestMapping("/uploadDevoiceSendText")
    @ResponseBody
    public BaseResponse sendText(
            @RequestParam("phone") String phone
    ){
        if (!smsService.deviceCheckPhone(phone)){
            baseResponse.setResult(ResultCodeEnum.DO_NOT_IN_TIME);//不在规定时间内
        }
        Integer code=(int)(Math.random() * 100000.0f);
        while (smsService.deviceHasCode(code)){
            code=(int)(Math.random() * 100000.0f);
        }
        smsService.deviceSaveCode(phone,code);
        try {
            String[] params = {code.toString()};
            SmsSingleSender ssender = new SmsSingleSender(Constant.MessageAppId, Constant.MessageAppKey);
            SmsSingleSenderResult result = ssender.sendWithParam("86", phone,
                    Constant.SMS_uploadDevice, params, Constant.SMS_mengSign, "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信
            System.out.println(result);
        } catch (HTTPException e) {
            // HTTP 响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // JSON 解析错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络 IO 错误
            e.printStackTrace();
        }
        return baseResponse;
    }

    //入驻验证手机
    @RequestMapping("/applyStoreSendText")
    @ResponseBody
    public void applyStoreSendText(
            @RequestParam("phone") String phone
    ){
        if (!smsService.storeCheckPhone(phone)){
            baseResponse.setResult(ResultCodeEnum.DO_NOT_IN_TIME);//不在规定时间内
        }
        Integer code=(int)(Math.random() * 100000.0f);
        while (smsService.storeHasCode(code)){
            code=(int)(Math.random() * 100000.0f);
        }
        smsService.storeSaveCode(phone,code);

        try {
            String[] params = {code.toString()};
            SmsSingleSender ssender = new SmsSingleSender(Constant.MessageAppId, Constant.MessageAppKey);
            SmsSingleSenderResult result = ssender.sendWithParam("86", phone,
                    Constant.SMS_applyStore, params, Constant.SMS_mengSign, "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信
            System.out.println(result);
        } catch (HTTPException e) {
            // HTTP 响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // JSON 解析错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络 IO 错误
            e.printStackTrace();
        }
    }


    //每10分钟清空一遍
    @Scheduled(cron = "0 0/10 * * * ?")
    public void updateTable(){
        smsService.updateTable();
    }

}
