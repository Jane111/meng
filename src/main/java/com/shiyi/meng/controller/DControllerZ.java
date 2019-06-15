package com.shiyi.meng.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shiyi.meng.model.Device;
import com.shiyi.meng.model.Devicebusiness;
import com.shiyi.meng.model.Reportdevice;
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
import java.util.TreeMap;

@RestController
@RequestMapping("/device")
@CrossOrigin
public class DControllerZ {


    @Autowired
    DeviceServiceZ deviceService;
    @Autowired
    BaseResponse response;

    //首页显示设备列表 根据用户经纬度 计算距离排序
    @RequestMapping("/getList")
    @ResponseBody
    public BaseResponse getList(
            @RequestParam("uLat") String uLat, //用户纬度
            @RequestParam("uLng") String uLng, //用户经度
            @RequestParam("start") String start //开始个数 一次传30个
    ){
        JSONArray array=new JSONArray();
        array=deviceService.getDeviceLists(uLat,uLng,start);
        if (array==null){
            response.setData(null);
            response.setResult(ResultCodeEnum.FIND_FAILURE);
        }
        else {
            response.setData(array);
            response.setResult(ResultCodeEnum.SUCCESS);
        }
        return response;
    }
    //首页显示设备类型
    @RequestMapping("/getTypeList")
    @ResponseBody
    public BaseResponse getTypeList(){
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
        JSONArray array=deviceService.selectList(dType);
        if (array==null){
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
        JSONArray array=deviceService.getNewList(uLat,uLng,start);
        if (array==null){
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
            @RequestParam("dType") BigInteger dType
    ){
        JSONArray array=deviceService.selectNewList(dType);
        if (array==null){
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
    ){
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
            @RequestParam("rdPhone") String rdPhone
    ){
        Reportdevice reportdevice=new Reportdevice();
        reportdevice.setRdReporter(rdReporter);
        reportdevice.setRdDevice(rdDevice);
        reportdevice.setRdOption(rdOption);
        reportdevice.setRdDcpt(rdDcpt);
        reportdevice.setRdImg(rdImg);
        reportdevice.setRdName(rdName);
        reportdevice.setRdPhone(rdPhone);
        reportdevice.setRdStatus(0);//待审核
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
        JSONArray array=deviceService.getPassDevice(uId);
        if (array==null){
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
        JSONArray array=deviceService.getRepairList(uLat,uLng,start);
        if (array==null){
            response.setData(array);
            response.setResult(ResultCodeEnum.SUCCESS);
        }else {
            response.setData(array);
            response.setResult(ResultCodeEnum.FIND_FAILURE);
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
        JSONArray array=deviceService.getSeconddbList(uLat,uLng,start);
        if (array==null){
            response.setData(array);
            response.setResult(ResultCodeEnum.SUCCESS);
        }else {
            response.setData(array);
            response.setResult(ResultCodeEnum.FIND_FAILURE);
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
        JSONArray array=deviceService.getNewdbList(uLat,uLng,start);
        if (array==null){
            response.setData(array);
            response.setResult(ResultCodeEnum.SUCCESS);
        }else {
            response.setData(array);
            response.setResult(ResultCodeEnum.FIND_FAILURE);
        }
        return response;
    }

    //设备商入驻状态
    @RequestMapping("/getDevicebusinessStatus")
    @ResponseBody
    public BaseResponse getDevicebusinessStatus(
            @RequestParam("uId") String uId
    ){
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



}
