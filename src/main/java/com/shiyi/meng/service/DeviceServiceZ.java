package com.shiyi.meng.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shiyi.meng.model.Device;
import com.shiyi.meng.model.Devicetype;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class DeviceServiceZ {

    //获取二手设备列表 根据用户当前位置
    public JSONArray getDeviceLists() {
        JSONArray showDeivceList = new JSONArray();
        List<Device> deviceList = Device.dao.find("select * from device dStatus=2 and dHand=2");
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
        String sql="select * from device where dType=?  and dStatus=2 and dHand=2";
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
}
