package com.shiyi.meng.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.shiyi.meng.model.*;
import com.shiyi.meng.util.Constant;
import com.sun.org.apache.bcel.internal.classfile.ConstantNameAndType;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class DeviceServiceZ {

    /**
     * dStatus 2 为已发布 1是下架 0是未审核 3是审核失败
     * dHand 1 新设备
     * @param uLat
     * @param uLng
     * @param start
     * @return
     */


    //获取二手设备列表 根据用户当前位置距离xx公里计算
    public JSONArray getDeviceLists(String uLat,String uLng,String start) {
        JSONArray showDeivceList = new JSONArray();
        List<Device> deviceList = Device.dao.find("select * from device where  dStatus=2 and dHand!=1 " +
                "order by dModifyTime desc and ACOS(SIN(('"+uLat+"' * 3.1415) / 180 ) *SIN((dLat * 3.1415) / 180 ) +COS(('"+uLat+"' * 3.1415) / 180 ) * COS((dLat * 3.1415) / 180 ) *COS(('"+uLng+"' * 3.1415) / 180 - (dLng * 3.1415) / 180 ) ) * 6380  asc  limit "+start+",10");
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
        jsonObject.put("dOwner",device.getDOwner());//设备拥有者
        String dPhotos=device.getDPhoto();
        String[] dPhoto=dPhotos.split("###");//设备照片
        jsonObject.put("dPhoto",dPhoto[0]);
        jsonObject.put("dLocation",device.getDLocation());//设备地址
        if (device.getDHand().toString().equals("2")){
            jsonObject.put("dOPrice",device.getDOPrice());//设备原价
        }
        else {
            jsonObject.put("dOPrice","");//设备原价
        }
        jsonObject.put("dHand",device.getDHand());
        jsonObject.put("dSPrice",device.getDSPrice());//设备售价
        jsonObject.put("dStatus",device.getDStatus());//设备状态 0-待审核 1-下架 2-在架 3-审核失败
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
    public JSONArray getNewList(String uLat,String uLng,String start) {
        String sql="select * from device where dHand=1 and dStatus=2 " +
                "order by dModifyTime desc and ACOS(SIN(('"+uLat+"' * 3.1415) / 180 ) *SIN((dLat * 3.1415) / 180 ) +COS(('"+uLat+"' * 3.1415) / 180 ) * COS((dLat * 3.1415) / 180 ) *COS(('"+uLng+"' * 3.1415) / 180 - (dLng * 3.1415) / 180 ) ) * 6380  asc  limit "+start+",10";
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
        String sql="select * from device where dHand=2 and dType=? order by dModifyTime desc";
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
        String sql="select * from device where dOwner=? and dStatus=2 oder by dModifyTime ";
        List<Device> deviceList=Device.dao.find(sql,uId);
        JSONArray array=new JSONArray();
        for (Device device:deviceList){
            JSONObject object=new JSONObject();
            object=packDevice(device);
            object.put("dCollect",getFollowCount(device.getDId()));
            object.put("dHand",device.getDHand());
            array.add(object);
        }
        return array;
    }

    //根据用户id查询已经发布的设备
    public JSONArray getOnCarriage(String uId) {
        String sql="select * from device where dOwner=? and dStatus =2";
        List<Device> deviceList=Device.dao.find(sql,uId);
        JSONArray array=new JSONArray();
        for (Device device:deviceList){
            JSONObject object=new JSONObject();
            object=packDevice(device);
            object.put("dCollect",getFollowCount(device.getDId()));
            object.put("dHand",device.getDHand());
            array.add(object);
        }
        return array;
    }

    private String getFollowCount(BigInteger dId) {
        String sql="select count(*) as followCount from followdevice where fdDevice=?";
        Integer count= Db.find(sql,dId).get(0).getInt("followCount");
        return count.toString();
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
        String sql="select * from device where dOwner=? and dStatus not in(2,0);";
        List<Device> deviceList=Device.dao.find(sql,uId);
        JSONArray array=new JSONArray();
        for (Device device:deviceList){
            JSONObject object=packDevice(device);
            object.put("dCollect",getFollowCount(device.getDId()));
            object.put("dHand",device.getDHand());
            array.add(object);
        }
        return array;
    }

    //编辑商品至显示新商品信息
    public JSONObject editShowNew(String dId) {
        Device device=Device.dao.findById(dId);
        JSONObject object=packEntireNewDevice(device);
        return object;
    }



    private JSONObject packEntireNewDevice(Device device) {
        JSONObject object=new JSONObject();
        object.put("dId",device.getDId());
        User user=User.dao.findById(device.getDOwner());
        object.put("dOwnerIcon",user.getUWeiXinIcon());//头像
        String ownerName=getDeviceOwnerName(device.getDOwner());
        object.put("dOwnerName",ownerName);
        object.put("dType",Integer.valueOf(device.getDType().toString()));
        object.put("dName",device.getDName());
        String[] dPhoto=device.getDPhoto().split("###");
        object.put("dPhoto",dPhoto);
        object.put("dSPrice",device.getDSPrice());
        object.put("dDiscuss",device.getDDiscuss());
        object.put("dPhone",device.getDPhone());
        object.put("dOtherConnect",device.getDOtherConnet());
        object.put("dLocation",device.getDLocation());
        object.put("dLat",device.getDLat());
        object.put("dLng",device.getDLng());
        object.put("dDcpt",device.getDDcpt());
        object.put("dPostage",device.getDPostage());
        object.put("dHand",device.getDHand());
        object.put("dOtherType",device.getDOtherType());
        Date dCreateTime=device.getDCreateTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time=sdf.format(dCreateTime);
        object.put("dCreateTime",time);
        return object;
    }

    private String getDeviceOwnerName(BigInteger dOwner) {
        String sql="select * from devicebusiness where dbUser=?";
        Devicebusiness devicebusiness=Devicebusiness.dao.findFirst(sql,dOwner);
        if (devicebusiness==null||devicebusiness.equals("")){
            User user=User.dao.findById(dOwner);
            return user.getUWeiXinName();
        }
        return devicebusiness.getDbName();
    }

    //编辑商品之显示商品信息
    public JSONObject editShowOld(String dId) {
        Device device=Device.dao.findById(dId);
        JSONObject object=packEntireOldDevice(device);
        return object;
    }

    private JSONObject packEntireOldDevice(Device device) {
        JSONObject object=new JSONObject();
        User user=User.dao.findById(device.getDOwner());
        object.put("dOwnerIcon",user.getUWeiXinIcon());
        String dOwnerName=getDeviceOwnerName(device.getDOwner());
        object.put("dOwnerName",dOwnerName);
        object.put("dId",device.getDId());
        object.put("dType",Integer.valueOf(device.getDType().toString()));
        object.put("dName",device.getDName());
        String[] dPhoto=device.getDPhoto().split("###");
        object.put("dPhoto",dPhoto);
        object.put("dSPrice",device.getDSPrice());
        object.put("dHand",device.getDHand());
        object.put("dDiscuss",device.getDDiscuss());
        object.put("dPhone",device.getDPhone());
        object.put("dOtherConnect",device.getDOtherConnet());
        object.put("dOtherType",device.getDOtherType());//其他联系方式类型
        object.put("dLocation",device.getDLocation());
        object.put("dLat",device.getDLat());
        object.put("dLng",device.getDLng());
        object.put("dDcpt",device.getDDcpt());
        object.put("dPostage",device.getDPostage());
        if (device.getDHand().toString().equals("1")){//新设备
            object.put("dClean","");
            object.put("dOPrice","");
        }
        else {
            object.put("dClean",device.getDClean());
            object.put("dOPrice",device.getDOPrice());
        }

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
            object.put("bId",bill.getBId());
            object.put("dCollect",getFollowCount(device.getDId()));
            object.put("dHand",device.getDHand());
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
            object.put("dCollect",getFollowCount(device.getDId()));
            object.put("dHand",device.getDHand());
            array.add(object);
        }
        return array;
    }

    //显示商品详情
    public JSONObject getDeviceInfo(String dId,String uId) {
        Device device=Device.dao.findById(dId);
        JSONObject object=packEntireOldDevice(device);

        String sql="select * from followdevice where fdDevice=? and fdUser=?";
        if (Followdevice.dao.find(sql,dId,uId)==null||Followdevice.dao.find(sql,dId,uId).isEmpty())
        {
            object.put("dFollow",0);
        }
        else {
            object.put("dFollow",1);
        }
        if (canReport(dId,uId)){
            object.put("dReport",0);//可以举报
        }
        else {
            object.put("dReport ",1);
        }
        return object;
    }
    //是否可以举报
    private boolean canReport(String dId, String uId) {
        String sql="select * from reportdevice where rdDevice=? and rdReporter=?";
        Reportdevice reportdevice=Reportdevice.dao.findFirst(sql,dId,uId);
        return reportdevice!=null?false:true;
    }

    public boolean followDevice(BigInteger dId, BigInteger uId) {
        Followdevice followdevice=new Followdevice();
        followdevice.setFdDevice(dId);
        followdevice.setFdUser(uId);
        return followdevice.save();
    }

    public BigInteger saveDevicebusiness(Devicebusiness devicebusiness) {
        if (devicebusiness.save()){
            deleteOtherdevicebusiness(devicebusiness.getDbUser(),devicebusiness.getDbId());
            return devicebusiness.getDbId();
        }
        else {
            return null;
        }
    }

    //已经入驻过
    public boolean hasRegister(BigInteger dbUser) {
        String sql="select * from devicebusiness where dbUser=?";//已经申请入驻过
        Devicebusiness devicebusiness=Devicebusiness.dao.findFirst(sql,dbUser);
        return devicebusiness==null?false:true;
    }

    private void deleteOtherdevicebusiness(BigInteger dbUser,BigInteger dbId) {
        String sql="delete from devicebusiness where dbUser=? and dbId !=?";
        Db.update(sql,dbUser,dbId);
    }


    public boolean saveNewDevice(Device device) {
        return device.save();
    }

    /*public JSONObject getNewDeviceInfo(String dId,String uId) {
        Device device=Device.dao.findById(dId);
        JSONObject object=packEntireNewDevice(device);
        String sql="select * from followdevice where fdDevice=? and fdUser=?";
        if (Followdevice.dao.find(sql,dId,uId)==null||Followdevice.dao.find(sql,dId,uId).isEmpty())
        {
            object.put("dFollow ",0);
        }
        else {
            object.put("dFollow ",1);
        }
        if (canReport(dId,uId)){
            object.put("dReport",0);//可以举报
        }
        else {
            object.put("dReport ",1);
        }
        return object;
    }*/

    public boolean unfollowDevice(String dId, String uId) {
        String sql="delete from followdevice where fdDevice="+dId+" and fdUser="+uId;

        return  Db.update(sql)>=1?true:false;
    }

    public boolean reUpDevice(String dId) {
        Device device=Device.dao.findById(dId);
        device.setDStatus(2);//2-在售
        return device.update();
    }

    public boolean deleteBuyRecord(String bId) {
        Bill bill=Bill.dao.findById(bId);
        return bill.delete();
    }

    public Device getDeviceById(String dId) {
        return Device.dao.findById(dId);
    }
    //更新就设备
    public boolean updateOldDevice(Device device) {
        return device.update();
    }

    public boolean updateNewDevice(Device device) {
        return device.update();
    }

    //搜索设备
    public JSONArray searchDeviceByName(String sContent) {
        JSONArray searchDeviceList = new JSONArray();
        List<Device> deviceList = Device.dao.find("select * from device " +
                "where dStatus=2 AND dName like '%"+sContent+"%' order by dModifyTime desc ");
        for(Device device:deviceList)
        {
            searchDeviceList.add(packDevice(device));
        }
        return searchDeviceList;
    }

    //判断是否是新设备商
    public boolean isNewDevicebusiness(String uId) {
        String sql="select * from devicebusiness where dbUser=?";
        Devicebusiness devicebusiness=Devicebusiness.dao.findFirst(sql,new Integer(uId));
        return devicebusiness==null?false:true;
    }

    //等待审核的我发布的设备
    public JSONArray getPassDevice(String uId) {
        String sql="select * from device where dOwner=? and dStatus in(0,3)";
        List<Device> deviceList=Device.dao.find(sql,uId);
        JSONArray array=new JSONArray();
        for (Device device:deviceList){
            JSONObject object=packDevice(device);
            if (device.getDStatus()==3||device.getDStatus().equals(3)){
                object.put("dFailReason",device.getDFailReason());
            }
            else {
                object.put("dFailReason","");
            }
            array.add(object);
        }
        return array;
    }

    //获取附近维修商de
    public JSONArray getRepairList(String uLat, String uLng, String start) {
        String sql="select * from devicebusiness where dbType=3 and dbStatus ="+ Constant.dbStatus_Pass +
                "  order by ACOS(SIN(('"+uLat+"' * 3.1415) / 180 ) *SIN((dbLat * 3.1415) / 180 ) +COS(('"+uLat+"' * 3.1415) / 180 ) * COS((dbLat * 3.1415) / 180 ) *COS(('"+uLng+"' * 3.1415) / 180 - (dbLng * 3.1415) / 180 ) ) * 6380  asc  limit "+start+",10";
        List<Devicebusiness> devicebusinesses=Devicebusiness.dao.find(sql);
        JSONArray array=new JSONArray();
        for (Devicebusiness devicebusiness:devicebusinesses){
            JSONObject object=packDevicebusiness(devicebusiness);
            array.add(object);
        }
        return array;
    }
    //获取附近二手商de
    public JSONArray getSeconddbList(String uLat, String uLng, String start) {
        String sql="select * from devicebusiness where dbType=2 and dbStatus ="+ Constant.dbStatus_Pass +
                "  order by ACOS(SIN(('"+uLat+"' * 3.1415) / 180 ) *SIN((dbLat * 3.1415) / 180 ) +COS(('"+uLat+"' * 3.1415) / 180 ) * COS((dbLat * 3.1415) / 180 ) *COS(('"+uLng+"' * 3.1415) / 180 - (dbLng * 3.1415) / 180 ) ) * 6380  asc  limit "+start+",10";
        List<Devicebusiness> devicebusinesses=Devicebusiness.dao.find(sql);
        JSONArray array=new JSONArray();
        for (Devicebusiness devicebusiness:devicebusinesses){
            JSONObject object=packDevicebusiness(devicebusiness);
            array.add(object);
        }
        return array;
    }
    //获取附近新设备商de
    public JSONArray getNewdbList(String uLat, String uLng, String start) {
        String sql="select * from devicebusiness where dbType=1 and dbStatus ="+ Constant.dbStatus_Pass +
                " order by ACOS(SIN(('"+uLat+"' * 3.1415) / 180 ) *SIN((dbLat * 3.1415) / 180 ) +COS(('"+uLat+"' * 3.1415) / 180 ) * COS((dbLat * 3.1415) / 180 ) *COS(('"+uLng+"' * 3.1415) / 180 - (dbLng * 3.1415) / 180 ) ) * 6380  asc  limit "+start+",10";
        List<Devicebusiness> devicebusinesses=Devicebusiness.dao.find(sql);
        JSONArray array=new JSONArray();
        for (Devicebusiness devicebusiness:devicebusinesses){
            JSONObject object=packDevicebusiness(devicebusiness);
            array.add(object);
        }
        return array;
    }

    //包装设备商
    private JSONObject packDevicebusiness(Devicebusiness devicebusiness) {
        JSONObject object=new JSONObject();
        object.put("dbId",devicebusiness.getDbId());
        object.put("dbType",devicebusiness.getDbType());//3-维修商，1-二手商 2-新设备商
        object.put("dbLoc",devicebusiness.getDbLoc());
        object.put("dbLat",devicebusiness.getDbLat());
        object.put("dbLng",devicebusiness.getDbLng());
        object.put("dbName",devicebusiness.getDbName());
        object.put("dbPhone",devicebusiness.getDbPhone());
        object.put("dbOtherConnect",devicebusiness.getDbOtherConnect());
        object.put("dbOtherType",devicebusiness.getDbOtherType());
        String deviceType=devicebusiness.getDbDeviceType();
        String[] deviceTypes=deviceType.split("###");
        object.put("dbDeviceType",deviceTypes);
        return object;
    }

    //设备商状态
    public JSONObject getDevicebusinessStatus(String uId) {
        String sql="select * from devicebusiness where dbUser=?";
        Devicebusiness devicebusiness=Devicebusiness.dao.findFirst(sql,uId);
        JSONObject object=packDevicebusiness(devicebusiness);
        object.put("dbStatus",devicebusiness.getDbStatus());
        if (devicebusiness.getDbStatus()==1||devicebusiness.getDbStatus().equals(1)){//审核失败
            object.put("dbFailReason",devicebusiness.getDbFailReason());//审核失败原因
        }
        else {
            object.put("dbFailReason","");//审核失败原因
        }
        return object;
    }

    public boolean saveReport(Reportdevice reportdevice) {
        return reportdevice.save();
    }

    public boolean isInValidTime(BigInteger dId){
        Device device=Device.dao.findById(dId);
        Date now = new Date();
        Date beforFive=new Date(now.getTime() - 300000);
        Date modifyTime=device.getDModifyTime();
        System.out.println("befornow 5"+beforFive+"  modify"+modifyTime);
        if (device.getDFlushTime().toString().equals("0")){
            return true;
        }
        if (modifyTime.before(beforFive)){
            return true;
        }
        return false;
    }

    //刷新置顶
    public boolean flushTopDevice(BigInteger dId) {
        Device device=Device.dao.findById(dId);
        BigInteger old=device.getDFlushTime();
        BigInteger newTime=old.add(new BigInteger("1"));
        device.setDFlushTime(newTime);
        return device.update();
    }
}
