package com.shiyi.meng.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shiyi.meng.model.Bill;
import com.shiyi.meng.model.Device;
import com.shiyi.meng.model.Devicetype;
import com.shiyi.meng.model.Followdevice;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class DeviceServiceZ {

    /**
     * dStatus 2 为已发布 1是下架 0是未审核
     * dHand 1 新设备
     * @param uLat
     * @param uLng
     * @param start
     * @return
     */


    //获取二手设备列表 根据用户当前位置距离xx公里计算
    public JSONArray getDeviceLists(String uLat,String uLng,String start) {
        JSONArray showDeivceList = new JSONArray();
        List<Device> deviceList = Device.dao.find("select * from device dStatus=2 and dHand!=1 " +
                "order by ACOS(SIN(('"+uLat+"' * 3.1415) / 180 ) *SIN((dLat * 3.1415) / 180 ) +COS(('"+uLat+"' * 3.1415) / 180 ) * COS((dLat * 3.1415) / 180 ) *COS(('"+uLng+"' * 3.1415) / 180 - (dLng * 3.1415) / 180 ) ) * 6380  asc  limit "+start+",30");
        //店铺的一张图片，店铺名称，店铺类型，位置，每月租金
        for(Device device:deviceList)
        {
            showDeivceList.add(packDevice(device));
        }
        return showDeivceList;
    }
    //打包json device对象
    private JSONObject packDevice(Device device) {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("dId",device.getDId());
        jsonObject.put("dName",device.getDName());//设备名称
        String dPhotos=device.getDPhoto();
        String[] dPhoto=dPhotos.split("###");//设备照片
        jsonObject.put("dPhoto",dPhoto);
        jsonObject.put("dLocation",device.getDLocation());//设备地址
        jsonObject.put("dPrice",device.getDOPrice());//设备照片
        return jsonObject;
    }

    //根据设备类型筛选二手
    public JSONArray selectList(BigInteger dType) {
        String sql="select * from device where dType=?  and dStatus=2 and dHand!=1";
        List<Device> deviceList=Device.dao.find(sql,dType);
        JSONArray array=new JSONArray();
        for (Device device:deviceList){
            JSONObject object=packDevice(device);
            array.add(object);
        }
        return array;
    }

    //获取设备类型
    public JSONArray getDeviceType() {
        String sql="select * from devicetype where dtFather=0";
        List<Devicetype> devicetypeList=Devicetype.dao.find(sql);
        JSONArray array=new JSONArray();
        for (Devicetype devicetype:devicetypeList){
            JSONObject object=packDeviceType(devicetype);
            array.add(object);
        }
        return array;
    }
    //打包设备类型的对象
    private JSONObject packDeviceType(Devicetype devicetype) {
        JSONObject object=new JSONObject();
        object.put("dtName",devicetype.getDtName());
        object.put("dtId",devicetype.getDtId());
        return object;
    }
    //获取新设备列表
    public JSONArray getNewList() {
        String sql="select * from decvice where dHand=1";
        List<Device> deviceList=Device.dao.find(sql);
        JSONArray array=new JSONArray();
        for (Device device:deviceList){
            JSONObject object=new JSONObject();
            object=packDevice(device);
            array.add(object);
        }
        return array;
    }
    //根据二手类别筛选新设备列表
    public JSONArray selectNewList(BigInteger dType) {
        String sql="select * from device where dHand=2 and dType=?";
        List<Device> deviceList=Device.dao.find(sql,dType);
        JSONArray array=new JSONArray();
        for (Device device:deviceList){
            JSONObject object=new JSONObject();
            object=packDevice(device);
            array.add(object);
        }
        return array;
    }

    //根据用户id查询已经发布的设备
    public JSONArray getMyPublished(String uId) {
        String sql="select * from device where dOwner=? and dStatus=2";
        List<Device> deviceList=Device.dao.find(sql,uId);
        JSONArray array=new JSONArray();
        for (Device device:deviceList){
            JSONObject object=new JSONObject();
            object=packDevice(device);
            array.add(object);
        }
        return array;
    }

    //下架设备
    public boolean underCarriage(String dId) {
        Device device=Device.dao.findById(dId);
        if (device==null){
            return false;
        }
        device.setDStatus(1);
        return device.update();
    }

    //显示下架的设备
    public JSONArray getMyUnderCarraige(String uId) {
        String sql="select * from device where dOwner=? and dStatus=1;";
        List<Device> deviceList=Device.dao.find(sql,uId);
        JSONArray array=new JSONArray();
        for (Device device:deviceList){
            JSONObject object=packDevice(device);
            array.add(object);
        }
        return array;
    }

    //编辑商品之显示商品信息
    public JSONObject editShow(String dId) {
        Device device=Device.dao.findById(dId);
        JSONObject object=packEntireDevice(device);
        return object;
    }

    private JSONObject packEntireDevice(Device device) {
        JSONObject object=new JSONObject();
        Devicetype devicetype=Devicetype.dao.findById(device.getDType());
        String dType="";
        if (devicetype!=null){
            dType=devicetype.getDtName();
        }
        object.put("dType",dType);
        object.put("dName",devicetype.getDtName());
        String[] dPhoto=device.getDPhoto().split("###");
        object.put("dPhoto",dPhoto);
        object.put("dOPrice",device.getDOPrice());
        object.put("dSPrice",device.getDSPrice());
        object.put("dDegree",device.getDDegree());
        object.put("dPhone",device.getDPhoto());
        object.put("dOtherConnect",device.getDOtherConnet());
        object.put("dLocation",device.getDLocation());
        object.put("dLat",device.getDLat());
        object.put("dLng",device.getDLng());
        object.put("dDcpt",device.getDDcpt());
        object.put("dPostage",device.getDPostage());
        object.put("dHand",device.getDHand());
        Date dCreateTime=device.getDCreateTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time=sdf.format(dCreateTime);
        object.put("dCreateTime",time);
        return object;
    }

    //彻底删除
    public boolean deleteUnderCarraige(String dId) {
        Device device=Device.dao.findById(dId);
        List<Followdevice> followdeviceList=Followdevice.dao.find("select * from followdevice where fdDevice=?",dId);
        for (Followdevice followdevice:followdeviceList){
            followdevice.delete();
        }
        return device.delete();
    }

    //获取我买到的设备
    public JSONArray getMyBill(String uId) {
        String sql="select * from bill where bBuyer=?";
        List<Bill> bills=Bill.dao.find(sql,uId);
        JSONArray array=new JSONArray();
        for (Bill bill:bills){
            Device device=Device.dao.findById(bill.getBDevice());
            JSONObject object=packDevice(device);
            array.add(object);
        }
        return array;
    }

    public JSONArray getMyFollow(String uId) {
        String sql="select * from followdevice where fdUser=?";
        List<Followdevice> followdeviceList=Followdevice.dao.find(sql,uId);
        JSONArray array=new JSONArray();
        for (Followdevice followdevice:followdeviceList){
            Device device=Device.dao.findById(followdevice.getFdDevice());
            JSONObject object=packDevice(device);
            array.add(object);
        }
        return array;
    }
}
