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
        for (Record record:list){
            System.out.println(record);
        }
        if (list==null||list.isEmpty()){
            System.out.println("false");
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
        List<Record> smscode=Db.find(sql,code, Constant.smsType_Store);
        //List<Smscode> smscode=Smscode.dao.find(sql,code, Constant.smsType_Device);
        return !(smscode==null||smscode.isEmpty());
    }

    //是否十分钟内已经发过
    public boolean storeCheckPhone(String phone) {
        String sql = "select * from smscode where smsPhone=? and smsType=?";//and smsCreateTime > DATEADD(MINUTE,-10,GETDATE())
        List<Record> smscode=Db.find(sql,phone,Constant.smsType_Store);
       // List<Smscode> smscode=Smscode.dao.find(sql,phone,Constant.smsType_Store);
        return smscode==null||smscode.isEmpty();
    }
    //保存验证码
    public void storeSaveCode(String phone, Integer code) {
        /*Smscode smscode=new Smscode();
        smscode.setSmsCode(code.toString());
        smscode.setSmsPhone(phone);
        smscode.setSmsType(Constant.smsType_Store);
        smscode.save();*/
        String sql="insert into smscode set smsPhone="+phone+", smsType="+Constant.smsType_Store+", smsCode="+code;
        Db.update(sql);
    }

    public void updateTable() {
        String sql="delete from smscode where smsCreateTime<=CURRENT_TIMESTAMP - INTERVAL 10 MINUTE;";
        Db.update(sql);
    }

    //验证手机验证码
    public boolean checkCode(String phone, String code, String type) {
        String sql="select * from smscode where smsPhone=? and smsType=? and smsCode=?";
        Record smscode=Db.findFirst(sql,phone,type,code);
        if (smscode==null){
            return false;
        }
        else {
            String sql2="delete from smscode where smsPhone=? and smsType=? and smsCode=?";
            Db.update(sql2,phone,type,code);
            return true;
        }
    }

    public void deleteDeviceSame(String phone) {
        String sql="delete from smscode where smsPhone=? and smsType=1";
        Db.update(sql,phone);
    }

    public void deleteStoreSame(String phone) {
        String sql="delete from smscode where smsPhone=? and smsType=2";
        Db.update(sql,phone);
    }
}
