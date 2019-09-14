package com.shiyi.meng.controller;

import com.alibaba.fastjson.JSONObject;
import com.shiyi.meng.service.UServiceL;
import com.shiyi.meng.util.BaseResponse;
import com.shiyi.meng.util.Constant;
import com.shiyi.meng.util.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UControllerL {
    @Autowired
    UServiceL uServiceL;
    @Autowired
    BaseResponse jr;
    //1、小程序用户授权
    @RequestMapping("/authorize")
    public BaseResponse authorize(
            @RequestParam(value="code",required = false) String code)
    {
        // 配置请求参数
        Map<String, String> param = new HashMap<>();
        param.put("appid", Constant.APPID);
        param.put("secret", Constant.APPSECRET);
        param.put("js_code", code);
        param.put("grant_type", Constant.GRANTTYPE);
        // 发送请求
        System.out.println("code="+code);
        String wxResult = HttpClientUtil.doGet(Constant.LOGINURL, param);
        System.out.println(wxResult);
        JSONObject jsonObject = JSONObject.parseObject(wxResult);
        // 获取参数返回的
        String session_key = jsonObject.get("session_key").toString();
        String open_id = jsonObject.get("openid").toString();
        // 封装返回小程序
        Map<String, String> result = new HashMap<>();
        result.put("session_key", session_key);
        result.put("open_id", open_id);
        // 根据返回的user实体类，判断用户是否是新用户，是的话，将用户信息存到数据库
//        Visitor vs = bs.selectByOpenId(open_id);
//        if(vs == null){
//
//            Visitor insert_visitor = new Visitor();
//            insert_visitor.setVOpenId(open_id);
//
//            // 添加到数据库
//            Boolean flag = bs.insertVisitor(insert_visitor);
//            if(!flag)
//            {
//                jr.setResult(ResultCodeEnum.ADD_ERROR);
//            }
//        }
//        else{
//            Business business = Business.dao.findFirst("select bId from business where bOpenId=?",open_id);
//            if(business==null)//游客身份
//            {
//                result.put("bId","");
//            }
//            else//business身份
//            {
//                result.put("bId", business.getBId()+"");
//            }
//            jr.setResult(ResultCodeEnum.SUCCESS);
//        }
//        jr.setData(result);
        return jr;
    }
}
