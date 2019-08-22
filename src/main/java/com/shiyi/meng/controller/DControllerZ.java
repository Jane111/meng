package com.shiyi.meng.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shiyi.meng.model.*;
import com.shiyi.meng.service.AServiceL;
import com.shiyi.meng.service.DeviceServiceZ;
import com.shiyi.meng.util.BaseResponse;
import com.shiyi.meng.util.Constant;
import com.shiyi.meng.util.CosStsClient;
import com.shiyi.meng.util.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import org.json.JSONException;

import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;

@RestController
@RequestMapping("/device")
@CrossOrigin
public class DControllerZ {


    @Autowired
    DeviceServiceZ deviceService;

    //首页显示设备列表 根据用户经纬度 计算距离排序
    @RequestMapping("/getList")
    @ResponseBody
    public BaseResponse getList(
            @RequestParam("uLat") String uLat, //用户纬度
            @RequestParam("uLng") String uLng, //用户经度
            @RequestParam("start") String start //开始个数 一次传30个
    ){
        BaseResponse response=new BaseResponse();
        JSONArray array=new JSONArray();
        array=deviceService.getDeviceLists(uLat,uLng,start);
        if (array==null||array.isEmpty()){
            response.setData(null);
            System.out.println("null");
            response.setResult(ResultCodeEnum.FIND_FAILURE);
        }
        else {
            System.out.println("getlist array.isEmpty"+array.isEmpty()+" array"+array.get(0));
            response.setData(array);
            response.setResult(ResultCodeEnum.SUCCESS);
        }
        return response;
    }
    //首页显示设备类型
    @RequestMapping("/getTypeList")
    @ResponseBody
    public BaseResponse getTypeList(){
        BaseResponse response=new BaseResponse();
        JSONArray array=deviceService.getDeviceType();
        response.setData(array);
        response.setResult(ResultCodeEnum.SUCCESS);
        return response;
    }

    //根据设备类别筛选二手设备
    @RequestMapping("/selectList")
    @ResponseBody
    public BaseResponse selectList(
            @RequestParam("dType") BigInteger dType
    ){
        BaseResponse response=new BaseResponse();
        JSONArray array=deviceService.selectList(dType);
        if (array==null||array.isEmpty()){
            response.setData(null);
            response.setResult(ResultCodeEnum.FIND_FAILURE);
        }
        else {
            response.setData(array);
            response.setResult(ResultCodeEnum.SUCCESS);
        }
        return response;
    }

    //买新的设备列表
    @RequestMapping("/getNewList")
    @ResponseBody
    public  BaseResponse getNewList(
            @RequestParam("uLat") String uLat, //用户纬度
            @RequestParam("uLng") String uLng, //用户经度
            @RequestParam("start") String start //开始个数 一次传30个
    ){
        BaseResponse response=new BaseResponse();
        JSONArray array=deviceService.getNewList(uLat,uLng,start);
        if (array==null||array.isEmpty()){
            response.setData(null);
            response.setResult(ResultCodeEnum.FIND_FAILURE);
        }
        else {
            response.setData(array);
            response.setResult(ResultCodeEnum.SUCCESS);
        }
        return response;
    }

    //根据设备类别筛选新设备
    @RequestMapping("/selectNewList")
    @ResponseBody
    public BaseResponse selectNewList(
            @RequestParam("dType") BigInteger dType,
            @RequestParam("start") Integer start
    ){
        BaseResponse response=new BaseResponse();
        JSONArray array=deviceService.selectNewList(dType,start);
        if (array==null||array.isEmpty()){
            response.setData(null);
            response.setResult(ResultCodeEnum.FIND_FAILURE);
        }
        else {
            response.setData(array);
            response.setResult(ResultCodeEnum.SUCCESS);
        }
        return response;
    }

    //发布二手设备
    @RequestMapping("/putOldDevice")
    @ResponseBody
    public BaseResponse putOldDevice(
            @RequestParam("dDcpt") String dDcpt,//设备描述
            @RequestParam("dPhoto") String dPhoto,//用 ### 隔开
            @RequestParam("dLat") String dLat,//纬度
            @RequestParam("dLng") String dLng,//经度
            @RequestParam("dName") String dName,//设备名称
            @RequestParam("dType") BigInteger dType,//设备类别
            @RequestParam("dOPrice") Float dOPrice,//原价
            @RequestParam("dSPrice") Float dSPrice,//售价
            @RequestParam("dPhone") String dPhone,//联系电话
            @RequestParam("dPostage") Integer dPostage,//是否包邮
            @RequestParam("dLocation") String dLocation,//地址
            @RequestParam("dOtherConnect") String dOtherConnect,//其他联系方式
            @RequestParam("dOtherType") int dOtherType,//奇台联系方式类型 1-微信 ，2-qq,3-邮箱
            @RequestParam("dOwner") BigInteger dOwner,//发布者
            @RequestParam("dDiscuss") Integer dDiscuss,//是否面议
            @RequestParam("dClean") Integer dClean//是否清洗干净
            //@RequestParam("code")String code
    ){
        BaseResponse response=new BaseResponse();
        /*if (deviceService.checkSMSDeviceCode(dPhone,code)){
            response.setResult(ResultCodeEnum.WRONG_CODE);//20009
            return response;
        }*/

        Device device=new Device();
        device.setDDcpt(dDcpt);
        device.setDDiscuss(dDiscuss);
        device.setDOtherType(dOtherType);
        device.setDClean(dClean);
        device.setDPhone(dPhone);
        device.setDPhoto(dPhoto);
        device.setDLat(dLat);
        device.setDLng(dLng);
        device.setDName(dName);
        device.setDType(dType);
        device.setDOPrice(dOPrice);
        device.setDOwner(dOwner);
        device.setDSPrice(dSPrice);
        device.setDPostage(dPostage);
        device.setDLocation(dLocation);
        device.setDOtherConnect(dOtherConnect);
        device.setDHand(2);//二手
        device.setDStatus(0);//待审核
        response.setResult(device.save()?ResultCodeEnum.SUCCESS:ResultCodeEnum.ADD_FAILURE);
        response.setData(device.getDId());//设备id
        return response;
    }

    //发布新设备
    @RequestMapping("/putNewDevice")
    @ResponseBody
    public BaseResponse putNewDevice(
            @RequestParam("dDcpt") String dDcpt,//设备描述
            @RequestParam("dPhoto") String dPhoto,//用 ### 隔开
            @RequestParam("dLat") String dLat,//纬度
            @RequestParam("dLng") String dLng,//经度
            @RequestParam("dName") String dName,//设备名称
            @RequestParam("dType") BigInteger dType,//设备类别
            @RequestParam("dSPrice") Float dSPrice,//售价
            @RequestParam("dPhone") String dPhone,//联系电话
            @RequestParam("dPostage") Integer dPostage,//是否包邮
            @RequestParam("dLocation") String dLocation,//地址
            @RequestParam("dOtherConnect") String dOtherConnect,//其他联系方式
            @RequestParam("dOtherType") int dOtherType,//其他联系方式类型 1-微信 ，2-qq,3-邮箱
            @RequestParam("dOwner") BigInteger dOwner,//发布者
            @RequestParam("dDiscuss") Integer dDiscuss//是否面议
    ){
        BaseResponse response=new BaseResponse();
        Device device=new Device();
        device.setDDcpt(dDcpt);
        device.setDDiscuss(dDiscuss);
        device.setDPhone(dPhone);
        device.setDPhoto(dPhoto);
        device.setDLat(dLat);
        device.setDLng(dLng);
        device.setDName(dName);
        device.setDType(dType);
        device.setDOwner(dOwner);
        device.setDSPrice(dSPrice);
        device.setDPostage(dPostage);
        device.setDLocation(dLocation);
        device.setDOtherConnect(dOtherConnect);
        device.setDOtherType(dOtherType);
        device.setDHand(1);//新的
        device.setDStatus(0);//待审核
        response.setResult(deviceService.saveNewDevice(device)?ResultCodeEnum.SUCCESS:ResultCodeEnum.ADD_FAILURE);
        response.setData(device.getDId());//设备id
        return response;
    }

    //二手商入驻
    @RequestMapping("/registeSencond")
    @ResponseBody
    public BaseResponse registeSencond(
            @RequestParam("dbUser") BigInteger dbUser,
            @RequestParam("dbDcpt") String dbDcpt,
            @RequestParam("dbLat") String dbLat,
            @RequestParam("dbLng") String dbLng,
            @RequestParam("dbLoc") String dbLoc,
            @RequestParam("dbName") String dbName,
            @RequestParam("dbDeviceType") String dbDeviceType,//用###隔开
            @RequestParam("dbPhone") String dbPhone,
            @RequestParam("dbOtherConnect") String dbOtherConnect,
            @RequestParam("dbOtherType") int dbOtherType//其他联系方式类型 1-微信 ，2-qq,3-邮箱
    ){
        BaseResponse response=new BaseResponse();
        Devicebusiness devicebusiness=new Devicebusiness();
        devicebusiness.setDbUser(dbUser);
        devicebusiness.setDbDeviceType(dbDeviceType);
        devicebusiness.setDbLat(dbLat);
        devicebusiness.setDbLng(dbLng);
        devicebusiness.setDbOtherType(dbOtherType);
        devicebusiness.setDbLoc(dbLoc);
        devicebusiness.setDbName(dbName);
        devicebusiness.setDbPhone(dbPhone);
        devicebusiness.setDbOtherConnect(dbOtherConnect);
        devicebusiness.setDbDcpt(dbDcpt);
        devicebusiness.setDbType(2);//2-二手设备商
        devicebusiness.setDbStatus(0);//待审核
        if (deviceService.hasRegister(devicebusiness.getDbUser())){
            response.setResult(ResultCodeEnum.HAS_REGISTER);
        }
        else {
            response.setData(deviceService.saveDevicebusiness(devicebusiness));
            response.setResult(ResultCodeEnum.SUCCESS);
        }
        return response;
    }

    //入驻新设备商
    @RequestMapping("/registeNewDbusiness")
    @ResponseBody
    public BaseResponse registeNewDbusiness(
            @RequestParam("dbUser") BigInteger dbUser,
            @RequestParam("dbDcpt") String dbDcpt,
            @RequestParam("dbLat") String dbLat,
            @RequestParam("dbLng") String dbLng,
            @RequestParam("dbLoc") String dbLoc,
            @RequestParam("dbName") String dbName,
            @RequestParam("dbImg") String dbImg,
            @RequestParam("dbDeviceType") String dbDeviceType,//用###隔开
            @RequestParam("dbPhone") String dbPhone,
            @RequestParam("dbOtherConnect") String dbOtherConnect,
            @RequestParam("dbOtherType") int dbOtherType//其他联系方式类型 1-微信 ，2-qq,3-邮箱
    ){
        BaseResponse response=new BaseResponse();
        Devicebusiness devicebusiness=new Devicebusiness();
        devicebusiness.setDbDeviceType(dbDeviceType);
        devicebusiness.setDbUser(dbUser);
        devicebusiness.setDbLat(dbLat);
        devicebusiness.setDbLng(dbLng);
        devicebusiness.setDbLoc(dbLoc);
        devicebusiness.setDbName(dbName);
        devicebusiness.setDbPhone(dbPhone);
        devicebusiness.setDbImg(dbImg);
        devicebusiness.setDbOtherConnect(dbOtherConnect);
        devicebusiness.setDbOtherType(dbOtherType);
        devicebusiness.setDbDcpt(dbDcpt);
        devicebusiness.setDbType(1);//1-新设备商
        devicebusiness.setDbStatus(0);//待审核
        if (deviceService.hasRegister(devicebusiness.getDbUser())){
            response.setResult(ResultCodeEnum.HAS_REGISTER);
        }
        else {
            response.setData(deviceService.saveDevicebusiness(devicebusiness));
            response.setResult(ResultCodeEnum.SUCCESS);
        }
        return response;
    }

    //入驻维修商
    @RequestMapping("/registeRepairbusiness")
    @ResponseBody
    public BaseResponse registeRepairbusiness(
            @RequestParam("dbUser") BigInteger dbUser,
            @RequestParam("dbDcpt") String dbDcpt,
            @RequestParam("dbLat") String dbLat,
            @RequestParam("dbLng") String dbLng,
            @RequestParam("dbLoc") String dbLoc,
            @RequestParam("dbName") String dbName,
            @RequestParam("dbDeviceType") String dbDeviceType,//用###隔开
            @RequestParam("dbPhone") String dbPhone,
            @RequestParam("dbOtherConnect") String dbOtherConnect,
            @RequestParam("dbOtherType") int dbOtherType//奇台联系方式类型 1-微信 ，2-qq,3-邮箱
    ){
        BaseResponse response=new BaseResponse();
        Devicebusiness devicebusiness=new Devicebusiness();
        devicebusiness.setDbDeviceType(dbDeviceType);
        devicebusiness.setDbUser(dbUser);
        devicebusiness.setDbLat(dbLat);
        devicebusiness.setDbLng(dbLng);
        devicebusiness.setDbLoc(dbLoc);
        devicebusiness.setDbName(dbName);
        devicebusiness.setDbPhone(dbPhone);
        devicebusiness.setDbOtherConnect(dbOtherConnect);
        devicebusiness.setDbOtherType(dbOtherType);
        devicebusiness.setDbDcpt(dbDcpt);
        devicebusiness.setDbType(3);//3-维修商
        devicebusiness.setDbStatus(0);//待审核
        if (deviceService.hasRegister(devicebusiness.getDbUser())){
            response.setResult(ResultCodeEnum.HAS_REGISTER);
        }
        else {
            response.setData(deviceService.saveDevicebusiness(devicebusiness));
            response.setResult(ResultCodeEnum.SUCCESS);
        }
        return response;
    }


    //我发布的设备
    @RequestMapping("/getMyPublished")
    @ResponseBody
    public BaseResponse getMyPublished(
            @RequestParam("uId") String uId
    ){
        BaseResponse response=new BaseResponse();
        JSONArray array=deviceService.getMyPublished(uId);
        response.setData(array);
        response.setResult(ResultCodeEnum.SUCCESS);
        return response;
    }

    //我发布的在架设备
    @RequestMapping("/getOnCarriage")
    @ResponseBody
    public BaseResponse getOnCarriage(
            @RequestParam("uId") String uId
    ){
        BaseResponse response=new BaseResponse();
        JSONArray array=deviceService.getOnCarriage(uId);
        response.setData(array);
        response.setResult(ResultCodeEnum.SUCCESS);
        return response;
    }

    //下架在架设备
    @RequestMapping("/underCarriage")
    @ResponseBody
    public BaseResponse underCarriage(
            @RequestParam("dId") String dId
    ){
        BaseResponse response=new BaseResponse();
        if (deviceService.underCarriage(dId)){
            response.setResult(ResultCodeEnum.SUCCESS);
            return response;
        }
        else {
            response.setResult(ResultCodeEnum.UPDATE_FAILURE);
        }
        return response;
    }

    //查看已下架设备
    @RequestMapping("/getMyUnderCarraige")
    @ResponseBody
    public BaseResponse getMyUnderCarraige(
            @RequestParam("uId") String uId
    ){
        BaseResponse response=new BaseResponse();
        JSONArray array=new JSONArray();
        array=deviceService.getMyUnderCarraige(uId);
        response.setData(array);
        response.setResult(ResultCodeEnum.SUCCESS);
        return response;
    }

    //编辑设备之显示新设备信息
    @RequestMapping("/editShowNew")
    @ResponseBody
    public BaseResponse editShowNew(
            @RequestParam("dId") String dId
    ){
        BaseResponse response=new BaseResponse();
        JSONObject object=deviceService.editShowNew(dId);
        response.setData(object);
        response.setResult(ResultCodeEnum.SUCCESS);
        return response;
    }

    //编辑设备之显示二手设备信息
    @RequestMapping("/editShowOld")
    @ResponseBody
    public BaseResponse editShowOld(
            @RequestParam("dId") String dId
    ){
        BaseResponse response=new BaseResponse();
        JSONObject object=deviceService.editShowOld(dId);
        response.setData(object);
        response.setResult(ResultCodeEnum.SUCCESS);
        return response;
    }

    //修改二手设备
    @RequestMapping("/updateOldDevice")
    @ResponseBody
    public BaseResponse updateOldDevice(
            @RequestParam("dId") String dId,
            @RequestParam("dDcpt") String dDcpt,//设备描述
            @RequestParam("dPhoto") String dPhoto,//用 ### 隔开
            @RequestParam("dLat") String dLat,//纬度
            @RequestParam("dLng") String dLng,//经度
            @RequestParam("dName") String dName,//设备名称
            @RequestParam("dType") BigInteger dType,//设备类别
            @RequestParam("dOPrice") Float dOPrice,//原价
            @RequestParam("dSPrice") Float dSPrice,//售价
            @RequestParam("dPhone") String dPhone,//联系电话
            @RequestParam("dPostage") Integer dPostage,//是否包邮
            @RequestParam("dLocation") String dLocation,//地址
            @RequestParam("dOtherConnect") String dOtherConnect,//其他联系方式
            @RequestParam("dOtherType") int dOtherType,//奇台联系方式类型 1-微信 ，2-qq,3-邮箱
            @RequestParam("dOwner") BigInteger dOwner,//发布者
            @RequestParam("dDiscuss") Integer dDiscuss,//是否面议
            @RequestParam("dClean") Integer dClean//是否清洗干净
    ){
        BaseResponse response=new BaseResponse();
        Device device=deviceService.getDeviceById(dId);
        device.setDDcpt(dDcpt);
        device.setDDiscuss(dDiscuss);
        device.setDOtherType(dOtherType);
        device.setDClean(dClean);
        device.setDPhone(dPhone);
        device.setDPhoto(dPhoto);
        device.setDLat(dLat);
        device.setDLng(dLng);
        device.setDName(dName);
        device.setDType(dType);
        device.setDOPrice(dOPrice);
        device.setDOwner(dOwner);
        device.setDSPrice(dSPrice);
        device.setDPostage(dPostage);
        device.setDLocation(dLocation);
        device.setDOtherConnect(dOtherConnect);
        device.setDHand(2);//二手
        device.setDStatus(0);//待审核
        response.setResult(deviceService.updateOldDevice(device)?ResultCodeEnum.SUCCESS:ResultCodeEnum.UPDATE_FAILURE);
        response.setData(device.getDId());//设备id
        return response;
    }

    @RequestMapping("/updateNewDevice")
    @ResponseBody
    public BaseResponse updateNewDevice(
            @RequestParam("dId")String dId,
            @RequestParam("dDcpt") String dDcpt,//设备描述
            @RequestParam("dPhoto") String dPhoto,//用 ### 隔开
            @RequestParam("dLat") String dLat,//纬度
            @RequestParam("dLng") String dLng,//经度
            @RequestParam("dName") String dName,//设备名称
            @RequestParam("dType") BigInteger dType,//设备类别
            @RequestParam("dSPrice") Float dSPrice,//售价
            @RequestParam("dPhone") String dPhone,//联系电话
            @RequestParam("dPostage") Integer dPostage,//是否包邮
            @RequestParam("dLocation") String dLocation,//地址
            @RequestParam("dOtherConnect") String dOtherConnect,//其他联系方式
            @RequestParam("dOtherType") int dOtherType,//其他联系方式类型 1-微信 ，2-qq,3-邮箱
            @RequestParam("dOwner") BigInteger dOwner,//发布者
            @RequestParam("dDiscuss") Integer dDiscuss//是否面议
    ){
        BaseResponse response=new BaseResponse();
        Device device=deviceService.getDeviceById(dId);
        device.setDDcpt(dDcpt);
        device.setDDiscuss(dDiscuss);
        device.setDPhone(dPhone);
        device.setDPhoto(dPhoto);
        device.setDLat(dLat);
        device.setDLng(dLng);
        device.setDName(dName);
        device.setDType(dType);
        device.setDOwner(dOwner);
        device.setDSPrice(dSPrice);
        device.setDPostage(dPostage);
        device.setDLocation(dLocation);
        device.setDOtherConnect(dOtherConnect);
        device.setDOtherType(dOtherType);
        device.setDHand(1);//新的
        device.setDStatus(0);//待审核
        response.setResult(deviceService.updateNewDevice(device)?ResultCodeEnum.SUCCESS:ResultCodeEnum.UPDATE_FAILURE);
        response.setData(device.getDId());//设备id
        return response;
    }

    //彻底删除
    @RequestMapping("/deleteUnderCarraige")
    @ResponseBody
    public BaseResponse deleteUnderCarraige(
            @RequestParam("dId") String dId
    ){
        BaseResponse response=new BaseResponse();
        if (deviceService.deleteUnderCarraige(dId)){
            response.setResult(ResultCodeEnum.SUCCESS);
        }
        else {
            response.setResult(ResultCodeEnum.DELETE_FAILURE);
        }
        return response;
    }

    //我买到的
    @RequestMapping("/getMyBill")
    @ResponseBody
    public BaseResponse getMyBill(
            @RequestParam("uId") String uId
    ){
        BaseResponse response=new BaseResponse();
        JSONArray array=deviceService.getMyBill(uId);
        response.setData(array);
        response.setResult(ResultCodeEnum.SUCCESS);
        return response;
    }
    //我想要的
    @RequestMapping("/getMyFollow")
    @ResponseBody
    public BaseResponse getMyFollow(
            @RequestParam("uId") String uId
    ){
        BaseResponse response=new BaseResponse();
        JSONArray array=deviceService.getMyFollow(uId);
        response.setData(array);
        response.setResult(ResultCodeEnum.SUCCESS);
        return response;
    }

    //二手设备详情页
    @RequestMapping("/getDeviceInfo")
    @ResponseBody
    public BaseResponse getDeviceInfo(
            @RequestParam("dId") String dId,
            @RequestParam("uId") String uId
    ){
        BaseResponse response=new BaseResponse();
        JSONObject device=deviceService.getDeviceInfo(dId,uId);
        response.setData(device);
        response.setResult(ResultCodeEnum.SUCCESS);
        return response;
    }

    /*//新设备详情页
    //设备详情页
    @RequestMapping("/getNewDeviceInfo")
    @ResponseBody
    public BaseResponse getNewDeviceInfo(
            @RequestParam("dId") String dId,
            @RequestParam("uId") String uId
    ){
        JSONObject device=deviceService.getNewDeviceInfo(dId,uId);
        response.setData(device);
        response.setResult(ResultCodeEnum.SUCCESS);
        return response;
    }*/

    //关注设备
    @RequestMapping("/followDevice")
    @ResponseBody
    public BaseResponse followDevice(
            @RequestParam("dId") BigInteger dId,
            @RequestParam("uId") BigInteger uId
    ){
        BaseResponse response=new BaseResponse();
        if (deviceService.followDevice(dId,uId)){
            response.setResult(ResultCodeEnum.SUCCESS);
        }else {
            response.setResult(ResultCodeEnum.ADD_FAILURE);
        }
        return response;
    }


    //举报
    @RequestMapping("/reportDevice")
    @ResponseBody
    public BaseResponse reportDevice(
            @RequestParam("rdReporter") BigInteger rdReporter,
            @RequestParam("rdDevice") BigInteger rdDevice,
            @RequestParam("rdOption") int rdOption,
            @RequestParam("rdDcpt") String rdDcpt,
            @RequestParam("rdImg") String rdImg,
            @RequestParam("rdName") String rdName,
            @RequestParam("rdPhone") String rdPhone,
            @RequestParam("formId") String formId
    ){
        BaseResponse response=new BaseResponse();
        Reportdevice reportdevice=new Reportdevice();
        reportdevice.setRdReporter(rdReporter);
        reportdevice.setRdDevice(rdDevice);
        reportdevice.setRdOption(rdOption);
        reportdevice.setRdDcpt(rdDcpt);
        reportdevice.setRdImg(rdImg);
        reportdevice.setRdName(rdName);
        reportdevice.setRdPhone(rdPhone);
        reportdevice.setRdStatus(0);//待审核
        reportdevice.setRdFormId(formId);
        if (deviceService.saveReport(reportdevice)){
            response.setResult(ResultCodeEnum.SUCCESS);
        }
        else {
            response.setResult(ResultCodeEnum.ADD_FAILURE);
        }
        return response;
    }

    //取消关注
    @RequestMapping("/unfollowDevice")
    public BaseResponse unfollowDevice(
            @RequestParam("dId") String dId,
            @RequestParam("uId") String uId
    ){
        BaseResponse response=new BaseResponse();
        if (deviceService.unfollowDevice(dId,uId)){
            response.setResult(ResultCodeEnum.SUCCESS);
        }else {
            response.setResult(ResultCodeEnum.DELETE_FAILURE);
        }
        return response;
    }

    @RequestMapping("/reUpDevice")
    public BaseResponse reUpDevice(
            @RequestParam("dId") String dId
    ){
        BaseResponse response=new BaseResponse();
        if (deviceService.reUpDevice(dId)){
            response.setResult(ResultCodeEnum.SUCCESS);
        }
        else {
            response.setResult(ResultCodeEnum.UPDATE_FAILURE);
        }
        return response;
    }

    //我买到的删除记录
    @RequestMapping("/deleteBuyRecord")
    public BaseResponse deleteBuyRecord(
            @RequestParam("/bId") String bId
    ){
        BaseResponse response=new BaseResponse();
        if (deviceService.deleteBuyRecord(bId)){
            response.setResult(ResultCodeEnum.SUCCESS);
        }
        else {
            response.setResult(ResultCodeEnum.DELETE_FAILURE);
        }
        return response;
    }

    //购买

    //上传图片
    /**
     * 获取cos临时密钥
     */
    @RequestMapping("/getTempKey")
    @ResponseBody
    public String getTempKey()
    {
        TreeMap<String, Object> config = new TreeMap<String, Object>();
        org.json.JSONObject credential;
        try {
            // 固定密钥
            config.put("SecretId", Constant.Secret_id);
            // 固定密钥
            config.put("SecretKey", Constant.Secret_key);

            // 临时密钥有效时长，单位是秒
            config.put("durationSeconds", 1800);

            // 换成您的 bucket
            config.put("bucket", "mengxicanyinren-1258172582");
            // 换成 bucket 所在地区
            config.put("region", "ap-shanghai");

            // 这里改成允许的路径前缀，可以根据自己网站的用户登录态判断允许上传的目录，例子：* 或者 a/* 或者 a.jpg
            config.put("allowPrefix", "*");

            // 密钥的权限列表。简单上传和分片需要以下的权限，其他权限列表请看 https://cloud.tencent.com/document/product/436/31923
            String[] allowActions = new String[] {
                    // 简单上传
                    "name/cos:PutObject",
                    // 分片上传
                    "name/cos:InitiateMultipartUpload",
                    "name/cos:ListMultipartUploads",
                    "name/cos:ListParts",
                    "name/cos:UploadPart",
                    "name/cos:CompleteMultipartUpload"
            };
            config.put("allowActions", allowActions);

            credential = CosStsClient.getCredential(config);
            System.out.println(credential);
        } catch (Exception e) {
            throw new IllegalArgumentException("no valid secret !");
        }
        /*JSONObject object=new JSONObject();
        if (credential!=null&&credential.get("codeDesc").equals("Success")){
            object.put("codeDesc",credential.getString("codeDesc"));
            object.put("code",credential.get("code"));
            JSONObject object1=new JSONObject();
            object1.put("tmpSecretId",credential.get("data").get("").get("tmpSecretId"));
            object.put("data",credential.get("data"));

        }*/
        return credential.toString();
    }

    //搜索设备，根据设备名称
    @RequestMapping("/searchDeviceByName")
    @ResponseBody
    public BaseResponse searchDeviceByName(
            @RequestParam("sContent") String sContent
    )
    {
        BaseResponse response=new BaseResponse();
        JSONArray deviceList = deviceService.searchDeviceByName(sContent);
        if(!deviceList.isEmpty())
        {
            response.setData(deviceList);
            response.setResult(ResultCodeEnum.SUCCESS);
        }else
        {
            response.setData(null);
            response.setResult(ResultCodeEnum.FIND_ERROR);
        }
        return response;
    }

    //判断是否是新设备商
    @RequestMapping("/isNewDevicebusiness")
    @ResponseBody
    public BaseResponse isNewDevicebusiness(
            @RequestParam("uId") String uId
    ){
        BaseResponse response=new BaseResponse();
        if (deviceService.isNewDevicebusiness(uId)){
            response.setResult(ResultCodeEnum.SUCCESS);
        }
        else {
            response.setResult(ResultCodeEnum.FIND_FAILURE);
        }
        return response;
    }

    //待审核、审核不通过的设备
    @RequestMapping("/getPassDevice")
    @ResponseBody
    public BaseResponse getPassDevice(
            @RequestParam("uId") String uId
    ){
        BaseResponse response=new BaseResponse();
        JSONArray array=deviceService.getPassDevice(uId);
        if (array==null||array.isEmpty()){
            response.setData(null);
            response.setResult(ResultCodeEnum.FIND_FAILURE);
        }
        else {
            response.setResult(ResultCodeEnum.SUCCESS);
            response.setData(array);
        }
        return response;
    }

    @RequestMapping("/getRepairList")
    @ResponseBody
    public BaseResponse getRepairList(
            @RequestParam("uLat") String uLat,//经度
            @RequestParam("uLng") String uLng,
            @RequestParam("start") String start //开始个数 一次传30个
    ){
        BaseResponse response=new BaseResponse();
        JSONArray array=deviceService.getRepairList(uLat,uLng,start);
        if (array==null||array.isEmpty()){
            response.setResult(ResultCodeEnum.FIND_FAILURE);
        }else {
            response.setData(array);
            response.setResult(ResultCodeEnum.SUCCESS);
        }
        return response;
    }

    @RequestMapping("/getSeconddbList")
    @ResponseBody
    public BaseResponse getSeconddbList(
            @RequestParam("uLat") String uLat,//经度
            @RequestParam("uLng") String uLng,
            @RequestParam("start") String start //开始个数 一次传30个
    ){
        BaseResponse response=new BaseResponse();
        JSONArray array=deviceService.getSeconddbList(uLat,uLng,start);
        if (array==null||array.isEmpty()){
            response.setResult(ResultCodeEnum.FIND_FAILURE);
        }else {
            response.setData(array);
            response.setResult(ResultCodeEnum.SUCCESS);
        }
        return response;
    }

    @RequestMapping("/getNewdbList")
    @ResponseBody
    public BaseResponse getNewdbList(
            @RequestParam("uLat") String uLat,//经度
            @RequestParam("uLng") String uLng,
            @RequestParam("start") String start //开始个数 一次传30个
    ){
        BaseResponse response=new BaseResponse();
        JSONArray array=deviceService.getNewdbList(uLat,uLng,start);
        if (array==null||array.isEmpty()){
            response.setData(array);
            response.setResult(ResultCodeEnum.FIND_FAILURE);
        }else {
            response.setData(array);
            response.setResult(ResultCodeEnum.SUCCESS);
        }
        return response;
    }

    //设备商入驻状态
    @RequestMapping("/getDevicebusinessStatus")
    @ResponseBody
    public BaseResponse getDevicebusinessStatus(
            @RequestParam("uId") String uId
    ){
        BaseResponse response=new BaseResponse();
        JSONObject devicebusiness=deviceService.getDevicebusinessStatus(uId);
        if (devicebusiness==null){
            response.setResult(ResultCodeEnum.FIND_FAILURE);
            response.setData(null);
        }
        else {
            response.setResult(ResultCodeEnum.SUCCESS);
            response.setData(devicebusiness);
        }
        return response;
    }

    @RequestMapping("/flushTopDevice")
    public BaseResponse flushTopDevice(
            @RequestParam("dId") BigInteger dId,
            @RequestParam("uLat") String uLat, //用户纬度
            @RequestParam("uLng") String uLng, //用户经度
            @RequestParam("start") String start //开始个数 一次传30个
    ){
        BaseResponse response=new BaseResponse();
        System.out.println("flushTop"+dId);
        if (deviceService.isInValidTime(dId)){
            if (deviceService.flushTopDevice(dId)){
                response.setResult(ResultCodeEnum.SUCCESS);
            }
            else {
                response.setResult(ResultCodeEnum.UPDATE_FAILURE);
            }
        }
        else {
            response.setResult(ResultCodeEnum.DO_NOT_IN_TIME);//不在有效刷新时间内
        }
        JSONArray array=deviceService.getDeviceLists(uLat,uLng,start);
        if (array==null||array.isEmpty()){
            response.setData(null);
        }
        else {
            response.setData(array);
        }
        return response;
    }

    @RequestMapping("/mineflushTopDevice")
    public BaseResponse mineflushTopDevice(
            @RequestParam("dId") BigInteger dId
    ){
        BaseResponse response=new BaseResponse();
        System.out.println("flushTop"+dId);
        if (deviceService.isInValidTime(dId)){
            if (deviceService.flushTopDevice(dId)){
                response.setResult(ResultCodeEnum.SUCCESS);
            }
            else {
                response.setResult(ResultCodeEnum.UPDATE_FAILURE);
            }
        }
        else {
            response.setResult(ResultCodeEnum.DO_NOT_IN_TIME);//不在有效刷新时间内
        }
        return response;
    }

    @RequestMapping("updateStoreShowInfo")
    public BaseResponse updateStoreShowInfo(
            @RequestParam("sId")String sId){
        BaseResponse response=new BaseResponse();
        JSONObject store=deviceService.updateStoreShowInfo(sId);
        if (store==null||store.isEmpty()){
            response.setResult(ResultCodeEnum.FIND_FAILURE);
        }
        else {
            response.setData(store);
            response.setResult(ResultCodeEnum.SUCCESS);
        }
        return response;
    }

    @RequestMapping("/uploadUserContract")
    public BaseResponse uploadUserContract(
            @RequestParam("ucOwner")BigInteger ucOwner,
            @RequestParam("ucStore")BigInteger ucStore,
            @RequestParam("ucIdUrl")String ucIdUrl,
            @RequestParam("ucBusinessUrL")String ucBusinessUrL,
            @RequestParam("ucContractUrl")String ucContractUrl
    ){
        BaseResponse baseResponse=new BaseResponse();
        if (deviceService.uploadUserContract(ucOwner,ucStore,ucIdUrl,ucBusinessUrL,ucContractUrl)){
            baseResponse.setResult(ResultCodeEnum.SUCCESS);
        }
        else
        {
            baseResponse.setResult(ResultCodeEnum.ADD_FAILURE);
        }
        return baseResponse;
    }
    //记录用户上交设备押金的记录
    @RequestMapping("/addDevicePayOrder")
    public BaseResponse addDevicePayOrder(
            @RequestParam("uId") Long uId,
            @RequestParam("dId") BigInteger dId
//            @RequestParam("money") Float money
    ){
        BaseResponse baseResponse=new BaseResponse();
        Bill bill = new Bill();
        bill.setBBuyer(uId);
        bill.setBDevice(dId);
        Device device = Device.dao.findById(dId);//根据设备Id得到对应的设备
        bill.setBSaler(device.getDOwner());//卖出者
        boolean flag=bill.save();
        if(flag)
        {
            baseResponse.setResult(ResultCodeEnum.SUCCESS);
        }else
        {
            baseResponse.setResult(ResultCodeEnum.ADD_ERROR);
        }
        baseResponse.setData(null);
        return baseResponse;
    }
    /*
    * 模板消息
    * */
    //置顶设备成功模板消息
    @RequestMapping("/upDeviceTemplate")
    public void upDeviceTemplate(
            @RequestParam("uId") BigInteger uId,
            @RequestParam("dId") BigInteger dId,
            @RequestParam("form_id") String form_id)
    {
        Device device = Device.dao.findById(dId);
        User user = User.dao.findById(uId);
        String keyword1="置顶成功";//置顶状态
        String keyword2=device.getDModifyTime()+"";//置顶时间
        String keyword3=device.getDName();//置顶内容
        String keyword4=user.getUWeiXinName();//置顶人
        String keyword5="您已经成功置顶您的设备";//置顶详情
        String touser=user.getUCOpenId();
        String template_id="g1v_sueP6tYISez-8U8VtCJ-DdwpFU8DnPfinfC01vw";//置顶成功template
        String access_token = Accesscode.dao.findFirst("select acCode from accesscode ORDER BY acCreateTime DESC").getAcCode();
        String reqParams="{\"access_token\":\""+access_token+"\",\"touser\":\""+touser+"\",\"template_id\":\""+template_id+"\",\"form_id\":\""+form_id+"\"," +
                "\"data\":{\"keyword1\":{\"value\":\""+keyword1+"\"},\"keyword2\":{\"value\":\""+keyword2+"\"}," +
                "\"keyword3\":{\"value\":\""+keyword3+"\"},\"keyword4\":{\"value\":\""+keyword4+"\"}," +
                "\"keyword5\":{\"value\":\""+keyword5+"\"}}}";
        System.out.println(reqParams);
    }
    //新客户访问提醒模板消息
    @RequestMapping("/newCoustmerDeviceTemplate")
    public void newCoustmerDeviceTemplate(
            @RequestParam("uId") BigInteger uId,
            @RequestParam("dId") BigInteger dId,
            @RequestParam("form_id") String form_id)
    {
        Device device = Device.dao.findById(dId);
        User user = User.dao.findById(uId);//访问者
        String keyword1=device.getDName();//访问项目
        String keyword2=user.getUWeiXinName();//昵称

        Date t = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String keyword3=df.format(t);//访问时间

        String keyword4="请保持手机畅通，并时刻关注平台动态。";//温馨提示
        String touser=User.dao.findById(device.getDOwner()).getUCOpenId();//通知设备的主人
        String template_id="9uZ_e4H3rUpf0cDFE84w8kmMKYhbbXtI1l56X0itTJ0";//新客户访问提醒template
        String access_token = Accesscode.dao.findFirst("select acCode from accesscode ORDER BY acCreateTime DESC").getAcCode();
        String reqParams="{\"access_token\":\""+access_token+"\",\"touser\":\""+touser+"\",\"template_id\":\""+template_id+"\",\"form_id\":\""+form_id+"\"," +
                "\"data\":{\"keyword1\":{\"value\":\""+keyword1+"\"},\"keyword2\":{\"value\":\""+keyword2+"\"}," +
                "\"keyword3\":{\"value\":\""+keyword3+"\"},\"keyword4\":{\"value\":\""+keyword4+"\"}}}";
        System.out.println(reqParams);
    }
    //订单状态通知模板消息，在购买设备时使用
    @RequestMapping("/buyDeviceTemplate")
    public void buyDeviceTemplate(
            @RequestParam("uId") BigInteger uId,
            @RequestParam("dId") BigInteger dId,
            @RequestParam("prepay_id") String prepay_id,
            @RequestParam("prepay_money") String prepay_money)
    {
        Device device = Device.dao.findById(dId);
        User user = User.dao.findById(uId);//访问者
        String keyword1=device.getDName();//商品信息
        String keyword2=prepay_money;//交易金额

        String keyword3="正在发货";//订单状态

        String keyword4=device.getDPhone();//设备商的联系电话
        String touser=user.getUCOpenId();//通知访问设备的人
        String template_id="9uZ_e4H3rUpf0cDFE84w8kmMKYhbbXtI1l56X0itTJ0";//新客户访问提醒template
        String access_token = Accesscode.dao.findFirst("select acCode from accesscode ORDER BY acCreateTime DESC").getAcCode();
        String reqParams="{\"access_token\":\""+access_token+"\",\"touser\":\""+touser+"\",\"template_id\":\""+template_id+"\",\"form_id\":\""+prepay_id+"\"," +
                "\"data\":{\"keyword1\":{\"value\":\""+keyword1+"\"},\"keyword2\":{\"value\":\""+keyword2+"\"}," +
                "\"keyword3\":{\"value\":\""+keyword3+"\"},\"keyword4\":{\"value\":\""+keyword4+"\"}}}";
        System.out.println(reqParams);
    }
}
