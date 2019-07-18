package com.shiyi.meng.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Const;
import com.shiyi.meng.model.*;
import com.shiyi.meng.util.Constant;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class AdminService {



    //获取待审核二手设备
    public JSONArray getSecondHandList() {
        String sql="select * from devicebusiness where dbType=2 and dbStatus="+ Constant.dbStatus_Wait;
        List<Devicebusiness> dbList=Devicebusiness.dao.find(sql);
        JSONArray array=new JSONArray();
        for (Devicebusiness db:dbList){
            JSONObject object=packDBList(db);
            array.add(object);
        }
        return array;
    }

    //获取二手商详情
    public JSONObject getSecondHandInfo(BigInteger dbId) {
        Devicebusiness db=Devicebusiness.dao.findById(dbId);
        if (db==null){
            return null;
        }
        JSONObject object=packSecondDB(db);

        return object;
    }

    private JSONObject packSecondDB(Devicebusiness db) {
        JSONObject object=new JSONObject();
        object.put("dbId",db.getDbId());
        object.put("dbDcpt",db.getDbDcpt());
        object.put("dbName",db.getDbName());
        object.put("dbLoc",db.getDbLoc());
        object.put("dbLat",db.getDbLat());
        object.put("dbLng",db.getDbLng());
        object.put("dbDeviceType",db.getDbDeviceType().replaceAll("###","、"));
        object.put("dbPhone",db.getDbPhone());
        object.put("dbOtherConnect",db.getDbOtherConnect());
        object.put("dbOtherType",db.getDbOtherType());
        object.put("dbStatus",db.getDbStatus());
        return object;
    }

    //获取待审核新设备商
    public JSONArray getNewList(int type) {
        String sql="select * from devicebusiness where dbType="+type+" and dbStatus="+ Constant.dbStatus_Wait;
        List<Devicebusiness> dbList=Devicebusiness.dao.find(sql);
        JSONArray array=new JSONArray();
        for (Devicebusiness db:dbList){
            JSONObject object=packDBList(db);
            array.add(object);
        }
        return array;
    }

    private JSONObject packDBList(Devicebusiness db) {
        JSONObject object=new JSONObject();
        object.put("dbId",db.getDbId());
        object.put("dbName",db.getDbName());
        //object.put("sName",getStoreName(db.getDbUser()));
        object.put("dbLoc",db.getDbLoc());
        object.put("dbDeviceType",db.getDbDeviceType().replaceAll("###","、"));
        object.put("dbPhone",db.getDbPhone());
        object.put("dbStatus",db.getDbStatus());
        return object;
    }
    //获取设备商对应的店铺名称
   /* private Object getStoreName(BigInteger dbUser) {

    }*/

    //获取新设备商、维修商详情
    public JSONObject getNewAndRepairInfo(BigInteger dbId) {
        Devicebusiness db=Devicebusiness.dao.findById(dbId);
        if (db==null){
            return null;
        }
        JSONObject object=packNewAndRepair(db);
        return object;
    }


    private JSONObject packNewAndRepair(Devicebusiness db) {
        JSONObject object=packSecondDB(db);
        String[] dbImg=db.getDbImg().split("###");
        object.put("dbImg",dbImg);
        return object;
    }

    //通过设备商
    public boolean passOrNotDBusiness(BigInteger dbId,int opeate) {
        Devicebusiness devicebusiness=Devicebusiness.dao.findById(dbId);
        if (devicebusiness==null){
            return false;
        }
        if (opeate==1)//否
        {
            devicebusiness.setDbStatus(Constant.dbStatus_NotPass);
        }
        else  if (opeate==0){
            devicebusiness.setDbStatus(Constant.dbStatus_Pass);
        }
        return devicebusiness.update();
    }


    //待审核设备
    public JSONArray getWaitDeviceList(Integer dHand) {
        String sql="select * from device where dHand ="+dHand+" and dStatus ="+Constant.dStatus_Wait;
        List<Device> deviceList=Device.dao.find(sql);
        JSONArray array=new JSONArray();
        for (Device device:deviceList){
            JSONObject object=null;
            if (dHand.equals("1")){//新设备
                object=packNewDevice(device);
            }
            if (dHand.equals("2")){//二手
                object=packSecondDevice(device);
            }
            array.add(object);
        }
        return array;
    }
    //打包二手设备
    private JSONObject packNewDevice(Device device) {
        JSONObject object=new JSONObject();
        String dOwnerName=getDeviceOwnerName(device.getDOwner());
        object.put("dId",device.getDId());
        object.put("dOwnerName",dOwnerName);
        object.put("dPhone",device.getDPhone());
        object.put("dName",device.getDName());
        object.put("dType",getDTypeName(device.getDType()));
        object.put("dHand",device.getDHand());
        object.put("dSPrice",device.getDSPrice());
        object.put("dStatus",device.getDStatus());//设备状态
        return object;
    }

    private String getDTypeName(BigInteger dType ) {
        Devicetype devicetype=Devicetype.dao.findById(dType);
        return devicetype.getDtName();
    }

    //打包二手设备
    private JSONObject packSecondDevice(Device device) {
        JSONObject object=packNewDevice(device);
        object.put("dClean",device.getDClean());
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

    //新旧设备详细信息
    public JSONObject getDeviceInfo(BigInteger dId) {
        Device device=Device.dao.findById(dId);
        JSONObject object=packEntireDevice(device);
        return object;
    }

    private JSONObject packEntireDevice(Device device) {
        JSONObject object=new JSONObject();
        String dOwnerName=getDeviceOwnerName(device.getDOwner());
        object.put("dOwnerName",dOwnerName);
        object.put("dPhone",device.getDPhone());
        object.put("dOtherConnect",device.getDOtherConnet());
        object.put("dOtherType",device.getDOtherType());//其他联系方式类型

        object.put("dId",device.getDId());
        object.put("dDcpt",device.getDDcpt());
        String[] dPhoto=device.getDPhoto().split("###");
        object.put("dPhoto",dPhoto);
        object.put("dLocation",device.getDLocation());
        object.put("dLat",device.getDLat());
        object.put("dLng",device.getDLng());

        object.put("dName",device.getDName());
        object.put("dType",getDTypeName(device.getDType()));
        object.put("dSPrice",device.getDSPrice());
        object.put("dHand",device.getDHand());
        object.put("dDiscuss",device.getDDiscuss());
        object.put("dStatus",device.getDStatus());//设备状态
        if (device.getDHand().toString().equals("1")){//新设备
            object.put("dClean","");
            object.put("dOPrice","");
        }
        else {
            object.put("dClean",device.getDClean());
            object.put("dOPrice",device.getDOPrice());
        }
        object.put("dPostage",device.getDPostage());

        Date dCreateTime=device.getDCreateTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time=sdf.format(dCreateTime);
        object.put("dCreateTime",time);
        return object;
    }

    //通过新旧设备
    public boolean passDevice(BigInteger dId) {
        Device device=Device.dao.findById(dId);
        if (device==null){
            return false;
        }
        device.setDStatus(Constant.dStatus_ON);
        return device.update();
    }

    //不通过新旧设备
    public boolean notPassDevice(BigInteger dId,String dFailReason) {
        Device device=Device.dao.findById(dId);
        if (device==null){
            return false;
        }
        device.setDStatus(Constant.dStatus_Faliure);
        device.setDFailReason(dFailReason);
        return device.update();
    }

    //已经审核过的新旧设备
    public JSONArray checkedDeviceList(Integer dHand,int pageStart,int pageIndex) {
        List<Device> deviceList=Device.dao.find("select * from device " +
                "where dHand=? and (dStatus=? or dStatus=?)  limit "+(pageStart-1)*pageIndex+","+pageIndex+" ",dHand,Constant.dStatus_ON,Constant.dStatus_Faliure);
        JSONArray array=new JSONArray();
        for (Device device:deviceList){
            JSONObject deviceObj=null;
            if (dHand.equals("2")){
                deviceObj=packSecondDevice(device);
            }
            if (dHand.equals("1")){
                deviceObj=packNewDevice(device);
            }
            array.add(deviceObj);
        }
        return array;
    }

    //待审核设备商、维修商列表
    public JSONArray waitDBList(Integer dbType,int pageStart,int pageIndex){
        List<Devicebusiness> devicebusinesses=Devicebusiness.dao.
                find("select * from devicebusiness where dbType="+dbType+" and dbStatus= "+Constant.dbStatus_Wait+" limit "+(pageStart-1)*pageIndex+","+pageIndex+" ");
        JSONArray array=new JSONArray();
        for (Devicebusiness devicebusiness:devicebusinesses){
            JSONObject db=packDBList(devicebusiness);
            array.add(db);
        }
        return array;
    }

    //等待审核的设备商的详细信息
    public JSONObject waitDBInfo(int dbType,BigInteger dbId) {
        Devicebusiness devicebusiness=Devicebusiness.dao.findById(dbId);
        JSONObject object=null;
        if (dbType==2){
            object=packSecondDB(devicebusiness);
        }
        if (dbType==1){//新设备商或是维修商
            packNewAndRepair(devicebusiness);
        }
        return object;
    }

    //是否通过设备商的申请
    public boolean passOrNotDB(BigInteger dbId, int operate) {
        Devicebusiness devicebusiness=Devicebusiness.dao.findById(dbId);
        if (operate==1){
            devicebusiness.setDbStatus(Constant.dbStatus_NotPass);
        }
        else {
            devicebusiness.setDbStatus(Constant.dbStatus_Pass);
        }
        return devicebusiness.update();
    }
    //已经审核通过的设备商
    public JSONArray passedDBList(int dbType, int pageStart, int pageIndex) {
        List<Devicebusiness> devicebusinesses=Devicebusiness.dao.
                find("select * from devicebusiness where dbType="+dbType+" and (dbStatus= "+Constant.dbStatus_NotPass+" or dbStatus= "+Constant.dbStatus_Pass+" ) limit "+(pageStart-1)*pageIndex+","+pageIndex+" ");
        JSONArray array=new JSONArray();
        for (Devicebusiness devicebusiness:devicebusinesses){
            JSONObject db=packDBList(devicebusiness);
            array.add(db);
        }
        return array;
    }
    //异常设备
    public JSONArray waitCheckDeviceList(int dHand, int pageStart, int pageIndex) {
        String sql="select * from reportdevice where rdStatus=? and rdDevice in (select dId from device where dHand= ? ) " +
                "limit "+(pageStart-1)*pageIndex+" , "+pageIndex+" ";
        List<Reportdevice> reportdevices=Reportdevice.dao.find(sql,Constant.reportD_Wait,dHand);
        JSONArray array=new JSONArray();
        for (Reportdevice reportdevice:reportdevices){
            JSONObject object=packReprotDBList(reportdevice);
            array.add(object);
        }
        return array;
    }
    //打包异常商品的列表
    private JSONObject packReprotDBList(Reportdevice reportdevice) {
        JSONObject object=new JSONObject();
        //object.put("rdReporter",getUserName(reportdevice.getRdReporter()));//微信名称
        object.put("rdId",reportdevice.getRdId());//举报编号
        object.put("rdPhone",reportdevice.getRdPhone());
        object.put("rdName",reportdevice.getRdName());
        object.put("dName",getDeviceName(reportdevice.getRdDevice()));//设备名称
        object.put("rdOption",reportdevice.getRdOption());
        object.put("rdStatus",reportdevice.getRdStatus());
        return object;
    }
    //设备名称
    private String getDeviceName(BigInteger rdDevice) {
        Device device=Device.dao.findById(rdDevice);
        return device.getDName();
    }

    //获取用户微信名
    private String getUserName(BigInteger rdReporter) {
        User user=User.dao.findById(rdReporter);
        return user.getUWeiXinName();
    }
    //已经审核过的设备
    public JSONArray checkedReportDeviceList(int dHand, int pageStart, int pageIndex) {
        String sql="select * from reportdevice where (rdStatus=? or rdStatus=?) and rdDevice in (select dId from device where dHand= ? ) " +
                "limit "+(pageStart-1)*pageIndex+" , "+pageIndex+" ";
        List<Reportdevice> reportdevices=Reportdevice.dao.find(sql,Constant.reportD_Pass,Constant.reportD_NotPass,dHand);
        JSONArray array=new JSONArray();
        for (Reportdevice reportdevice:reportdevices){
            JSONObject object=packReprotDBList(reportdevice);
            array.add(object);
        }
        return array;
    }

    //待审核设备详情
    public JSONObject waitCheckDeviceInfo(BigInteger rdId) {
        Reportdevice reportdevice=Reportdevice.dao.findById(rdId);
        JSONObject object=packEntireRd(reportdevice);
        return object;
    }

    private JSONObject packEntireRd(Reportdevice reportdevice) {
        JSONObject object=new JSONObject();
        object.put("rdName",reportdevice.getRdName());
        object.put("rdOption",reportdevice.getRdOption());
        object.put("rdPhone",reportdevice.getRdPhone());
        object.put("rdOtherConnet",reportdevice.getRdOtherConnet());//其他联系方式
        object.put("rdStatus",reportdevice.getRdStatus());
        object.put("rdDcpt",reportdevice.getRdDcpt());
        String []rdImg=reportdevice.getRdImg().split("###");
        object.put("rdImg",rdImg);
        Device device=Device.dao.findById(reportdevice.getRdDevice());
        object.put("dName",device.getDName());
        return object;
    }

    //审核异常设备
    public boolean passOrNotRD(BigInteger rdId, int operate) {
        Reportdevice reportdevice=Reportdevice.dao.findById(rdId);
        if (operate==Constant.reportD_NotPass){
            reportdevice.setRdStatus(Constant.reportD_NotPass);
        }
        else if (operate==Constant.reportD_Pass) {
            reportdevice.setRdStatus(Constant.reportD_Pass);
        }
        return reportdevice.update();
    }

    public JSONArray allSecondDB(int dbType, int pageStart, int pageIndex) {
        String sql="select * from devicebusiness where dbType=? limit "+(pageStart-1)*pageIndex+","+pageIndex+" ";
        List<Devicebusiness> devicebusinessList=Devicebusiness.dao.find(sql,dbType);
        JSONArray array=new JSONArray();
        for (Devicebusiness devicebusiness:devicebusinessList){
            JSONObject object=packDBList(devicebusiness);
            array.add(object);
        }
        return array;
    }

    //将已经上传到云端的模板url存储
    public boolean uploadContractModel(String contractUrl) {
        Contracttemplate contracttemplate =new Contracttemplate();
        contracttemplate.setCtContent(contractUrl);
        return contracttemplate.save();
    }

    public JSONArray getContractModelList(int pageStart, int pageIndex) {
        String sql="select * from contractTemplete limit "+(pageStart-1)*pageIndex+" , "+pageIndex +" Order By ctModifyTime Desc ";
        JSONArray array=new JSONArray();
        List<Contracttemplate> list=Contracttemplate.dao.find(sql);
        int i=(pageStart-1)*pageIndex+1;
        for (Contracttemplate contracttemplate:list){
            JSONObject object=packContractTemplete(contracttemplate,i);
            array.add(object);
            i++;
        }
        return array;
    }
    //打包合同模板列表
    private JSONObject packContractTemplete(Contracttemplate contracttemplate,int i) {
        JSONObject object=new JSONObject();
        object.put("number",i);
        object.put("ctId",contracttemplate.getCtId());
        object.put("ctContent",contracttemplate.getCtContent());
        return object;
    }
    //删除数据库里
    public boolean deleteContractModel(BigInteger ctId) {
        Contracttemplate contracttemplate=Contracttemplate.dao.findById(ctId);
        return contracttemplate.delete();
    }

    //获取用户上传合同列表
    public JSONArray getUserContratcList(int pageIndex, int pageStart) {
        String sql="select * from usercontract where ucStatus=? limit "+(pageStart-1)*pageIndex+" , "+pageIndex +" Order By ctModifyTime Desc ";
        List<Usercontract> list=Usercontract.dao.find(sql, Constant.userContrac_Wait);
        JSONArray array=new JSONArray();
        for (Usercontract uc:list){
            JSONObject object=packUC(uc);
            array.add(object);
        }
        return array;
    }
    //打包列表
    private JSONObject packUC(Usercontract uc) {
        JSONObject object=new JSONObject();
        object.put("ucId",uc.getUcId());
        object.put("ucOwner",uc.getUcOwner());
        object.put("uWeiXinName",User.dao.findById(uc.getUcId()).getUWeiXinName());
//        object.put("ucContent",uc.getUcContent());
        object.put("ucStore",uc.getUcStore());
        object.put("sName",Store.dao.findById(uc.getUcStore()).getSName());
        return object;
    }

    public boolean checkUserContract(BigInteger ucId, int operate) {
        Usercontract usercontract=Usercontract.dao.findById(ucId);
        if (operate==1){
            usercontract.setUcStatus(Constant.userContrac_Not);
        }
        else if (operate==2){
            usercontract.setUcStatus(Constant.userContrac_Pass);
        }
        return usercontract.update();
    }
}
