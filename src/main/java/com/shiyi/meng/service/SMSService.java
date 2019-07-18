package com.shiyi.meng.service;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.shiyi.meng.model.Smscode;
import com.shiyi.meng.util.Constant;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class SMSService {
    //验证验证码是否已经存在
    public boolean deviceHasCode(Integer code) {
        String sql = "select * from smscode where smsCode="+code+" and smsType="+ Constant.smsType_Device;//and smsCreateTime > DATEADD(MINUTE,-10,GETDATE())
        List<Record> list=Db.find(sql);
        if (list==null||list.isEmpty()){
            return false;
        }
        return true;
    }

    //是否十分钟内已经发过
    public boolean deviceCheckPhone(String phone) {
        String sql = "select * from smscode where smsPhone=?  and smsType=?";//and smsCreateTime > DATEADD(MINUTE,-10,GETDATE())
        if (Db.find(sql,phone,Constant.smsType_Device)==null){
            return true;
        }
        return false;
    }
    //保存验证码
    public boolean deviceSaveCode(String phone, Integer code) {
        String sql="insert into smscode set smsPhone="+phone+", smsType="+Constant.smsType_Device+", smsCode="+code;
        /*Smscode smscode=new Smscode();
        smscode.setSmsCode(code.toString());
        smscode.setSmsPhone(phone);
        smscode.setSmsType(Constant.smsType_Device);
        System.out.println("code"+code+" phone"+phone);
        if (smscode.save()){
            return true;
        }*/
        return Db.update(sql)==1;
    }

    //验证验证码是否已经存在
    public boolean storeHasCode(Integer code) {
        String sql = "select * from smscode where smsCode=?  and smsType=?";//and smsCreateTime > DATEADD(MINUTE,-10,GETDATE())
        List<Smscode> smscode=Smscode.dao.find(sql,code, Constant.smsType_Device);
        return !smscode.isEmpty();
    }

    //是否十分钟内已经发过
    public boolean storeCheckPhone(String phone) {
        String sql = "select * from smscode where smsPhone=? and smsType=?";//and smsCreateTime > DATEADD(MINUTE,-10,GETDATE())
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

    public void updateTable() {
        String sql="delete from smscode";
        Db.update(sql);
    }
}
