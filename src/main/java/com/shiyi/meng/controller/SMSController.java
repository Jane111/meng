package com.shiyi.meng.controller;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.shiyi.meng.util.Constant;
import org.json.JSONException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@RequestMapping("/sms")
public class SMSController {

    //发布设备验证手机
    @RequestMapping("/uploadDevoiceSendText")
    @ResponseBody
    public void sendText(
            @RequestParam("phone") String phone,
            @RequestParam("code") String code
    ){
        try {
            String[] params = {code};
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
    }

    //入驻验证手机
    @RequestMapping("/applyStoreSendText")
    @ResponseBody
    public void applyStoreSendText(
            @RequestParam("phone") String phone,
            @RequestParam("code") String code
    ){
        try {
            String[] params = {code};
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

}
