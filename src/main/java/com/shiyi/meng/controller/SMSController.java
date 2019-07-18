package com.shiyi.meng.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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


    //发布设备验证手机
    @RequestMapping("/uploadDevoiceSendText")
    @ResponseBody
    public BaseResponse sendText(
            @RequestParam("phone") String phone
    ){
        BaseResponse baseResponse=new BaseResponse();

        smsService.deleteDeviceSame(phone);//一分钟后重新发送，十分钟之内的删除

        Integer code=(int)((Math.random()*9+1)*100000);
        while (smsService.deviceHasCode(code)){
            code=(int)((Math.random()*9+1)*100000);
        }
        try {
            String[] params = {code.toString()};
            SmsSingleSender ssender = new SmsSingleSender(Constant.MessageAppId, Constant.MessageAppKey);
            SmsSingleSenderResult result = ssender.sendWithParam("86", phone,
                    Constant.SMS_uploadDevice, params, Constant.SMS_mengSign, "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信
            System.out.println(result);
            JSONObject json=JSON.parseObject(result.toString());
            if (json.get("result").equals(0))//发送成功
            {
                smsService.deviceSaveCode(phone,code);
                baseResponse.setResult(ResultCodeEnum.SUCCESS);
            }
            else {
                baseResponse.setResult(ResultCodeEnum.WRONG);
            }
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
    public BaseResponse applyStoreSendText(
            @RequestParam("phone") String phone
    ){
        BaseResponse baseResponse=new BaseResponse();
        smsService.deleteStoreSame(phone);//一分钟后重新发送，十分钟之内的删除

        Integer code=(int)((Math.random()*9+1)*100000);
        while (smsService.storeHasCode(code)){
            code=(int)((Math.random()*9+1)*100000);
        }

        try {
            String[] params = {code.toString()};
            SmsSingleSender ssender = new SmsSingleSender(Constant.MessageAppId, Constant.MessageAppKey);
            SmsSingleSenderResult result = ssender.sendWithParam("86", phone,
                    Constant.SMS_applyStore, params, Constant.SMS_mengSign, "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信
            System.out.println(result);
            JSONObject json=JSON.parseObject(result.toString());
            if (json.get("result").equals(0))//发送成功
            {
                smsService.storeSaveCode(phone,code);
                baseResponse.setResult(ResultCodeEnum.SUCCESS);
            }
            else {
                baseResponse.setResult(ResultCodeEnum.WRONG);
            }
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
    @RequestMapping("/checkCode")
    @ResponseBody
    public BaseResponse checkCode(
            @RequestParam("phone") String phone,
            @RequestParam("code") String code,
            @RequestParam("type") String type //2-店铺，1-设备
    ){
        BaseResponse baseResponse=new BaseResponse();
        if (smsService.checkCode(phone,code,type)){
           baseResponse.setResult(ResultCodeEnum.SUCCESS);
        }
        else {
            baseResponse.setResult(ResultCodeEnum.FIND_FAILURE);
        }
        return baseResponse;
    }

    //每10分钟清空一遍
    @Scheduled(cron = "0 0/10 * * * ?")
    public void updateTable(){
        smsService.updateTable();
    }

}
