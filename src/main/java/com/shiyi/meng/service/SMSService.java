package com.shiyi.meng.service;

import com.shiyi.meng.model.Smscode;
import com.shiyi.meng.util.Constant;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class SMSService {
    //验证验证码是否已经存在
    public boolean deviceHasCode(Integer code) {
        String sql = "select * from smscode where smsCode=? and smsCreateTime > DATEADD(MINUTE,-10,GETDATE()) and smsType=?";
        List<Smscode> smscode=Smscode.dao.find(sql,code, Constant.smsType_Device);
        return !smscode.isEmpty();
    }

    //是否十分钟内已经发过
    public boolean deviceCheckPhone(String phone) {
        String sql = "select * from smscode where smsPhone=? and smsCreateTime > DATEADD(MINUTE,-10,GETDATE()) and smsType=?";
        List<Smscode> smscode=Smscode.dao.find(sql,phone,Constant.smsType_Device);
        return smscode.isEmpty();
    }
    //保存验证码
    public void deviceSaveCode(String phone, Integer code) {
        Smscode smscode=new Smscode();
        smscode.setSmsCode(code.toString());
        smscode.setSmsPhone(phone);
        smscode.setSmsType(Constant.smsType_Device);
        smscode.save();
    }

    //验证验证码是否已经存在
    public boolean storeHasCode(Integer code) {
        String sql = "select * from smscode where smsCode=? and smsCreateTime > DATEADD(MINUTE,-10,GETDATE()) and smsType=?";
        List<Smscode> smscode=Smscode.dao.find(sql,code, Constant.smsType_Device);
        return !smscode.isEmpty();
    }

    //是否十分钟内已经发过
    public boolean storeCheckPhone(String phone) {
        String sql = "select * from smscode where smsPhone=? and smsCreateTime > DATEADD(MINUTE,-10,GETDATE()) and smsType=?";
        List<Smscode> smscode=Smscode.dao.find(sql,phone,Constant.smsType_Device);
        return smscode.isEmpty();
    }
    //保存验证码
    public void storeSaveCode(String phone, Integer code) {
        Smscode smscode=new Smscode();
        smscode.setSmsCode(code.toString());
        smscode.setSmsPhone(phone);
        smscode.setSmsType(Constant.smsType_Device);
        smscode.save();
    }
}
