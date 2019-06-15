package com.shiyi.meng.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shiyi.meng.model.Admin;
import com.shiyi.meng.service.AdminService;
import com.shiyi.meng.util.BaseResponse;
import com.shiyi.meng.util.Constant;
import com.shiyi.meng.util.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.math.BigInteger;

@RestController
@RequestMapping("/admin")
@CrossOrigin
public class AControllerZ {

    @Autowired
    AdminService adminService=new AdminService();
    @Autowired
    BaseResponse baseResponse=new BaseResponse();

    //待审核二手商列表
    @RequestMapping("/getSecondHandList")
    public BaseResponse getSecondHandList(){
        BaseResponse baseResponse=new BaseResponse();
        JSONArray array=adminService.getSecondHandList();
        if (array.isEmpty()){
            baseResponse.setResult(ResultCodeEnum.FIND_FAILURE);//20004
        }
        else {
            baseResponse.setData(array);
            baseResponse.setResult(ResultCodeEnum.SUCCESS);
        }
        return baseResponse;
    }

    //二手商具体信息
    @RequestMapping("/getSecondHandInfo")
    public BaseResponse getSecondHandInfo(
            @RequestParam("dbId")BigInteger dbId
    ){
        BaseResponse baseResponse=new BaseResponse();
       JSONObject object=adminService.getSecondHandInfo(dbId);
        if (object.isEmpty()){
            baseResponse.setResult(ResultCodeEnum.FIND_FAILURE);
        }
        else
        {
            baseResponse.setResult(ResultCodeEnum.SUCCESS);
            baseResponse.setData(object);
        }
        return baseResponse;
    }

    @RequestMapping("/getNewList")
    public BaseResponse getNewList(){
        BaseResponse baseResponse=new BaseResponse();
        JSONArray array=adminService.getNewList(1);
        if (array.isEmpty()){
            baseResponse.setResult(ResultCodeEnum.FIND_FAILURE);//20004
        }
        else {
            baseResponse.setData(array);
            baseResponse.setResult(ResultCodeEnum.SUCCESS);
        }
        return baseResponse;
    }

    @RequestMapping("/getNewInfo")
    public BaseResponse getNewInfo(
            @RequestParam("dbId")BigInteger dbId
    ){
        BaseResponse baseResponse=new BaseResponse();
        JSONObject array=adminService.getNewAndRepairInfo(dbId);
        if (array.isEmpty()){
            baseResponse.setResult(ResultCodeEnum.FIND_FAILURE);//20004
        }
        else {
            baseResponse.setData(array);
            baseResponse.setResult(ResultCodeEnum.SUCCESS);
        }
        return baseResponse;
    }

    @RequestMapping("/getRepairList")
    public BaseResponse getRepairList(){
        BaseResponse baseResponse=new BaseResponse();
        JSONArray array=adminService.getNewList(Constant.dbType_Repair);
        if (array.isEmpty()){
            baseResponse.setResult(ResultCodeEnum.FIND_FAILURE);//20004
        }
        else {
            baseResponse.setData(array);
            baseResponse.setResult(ResultCodeEnum.SUCCESS);
        }
        return baseResponse;
    }

    @RequestMapping("/getRepairInfo")
    public BaseResponse getRepairInfo(
            @RequestParam("dbId")BigInteger dbId
    ){
        BaseResponse baseResponse=new BaseResponse();
        JSONObject array=adminService.getNewAndRepairInfo(dbId);
        if (array.isEmpty()){
            baseResponse.setResult(ResultCodeEnum.FIND_FAILURE);//20004
        }
        else {
            baseResponse.setData(array);
            baseResponse.setResult(ResultCodeEnum.SUCCESS);
        }
        return baseResponse;
    }

    @RequestMapping("/passOrNotDBusiness")
    public BaseResponse passOrNotDBusiness(
            @RequestParam("dbId") BigInteger dbId,
            @RequestParam("operate") int operate//1-否，2-过
    ){
        BaseResponse baseResponse=new BaseResponse();
        if (adminService.passOrNotDBusiness(dbId,operate))
        {
            baseResponse.setResult(ResultCodeEnum.SUCCESS);
        }
        else {
            baseResponse.setResult(ResultCodeEnum.UPDATE_FAILURE);//20005
        }
        return baseResponse;
    }


    //待审核的二手设备
    @RequestMapping("/getWaitSecondList")
    public BaseResponse getWaitSecondList(){
        BaseResponse baseResponse=new BaseResponse();
        JSONArray array=adminService.getWaitDeviceList(2);
        if (array.isEmpty()){
            baseResponse.setResult(ResultCodeEnum.FIND_FAILURE);//20004
        }
        else {
            baseResponse.setResult(ResultCodeEnum.SUCCESS);
            baseResponse.setData(array);
        }
        return baseResponse;
    }
    //新旧设备详情
    @RequestMapping("/getDeviceInfo")
    public BaseResponse getDeviceInfo(
            @RequestParam("dId")BigInteger dId
    ){
        BaseResponse baseResponse=new BaseResponse();
        JSONObject object=adminService.getDeviceInfo(dId);
        System.out.println("getDeviceInfo"+object);
        if (object==null||object.isEmpty()){
            baseResponse.setResult(ResultCodeEnum.FIND_FAILURE);//20004
        }
        else {
            baseResponse.setResult(ResultCodeEnum.SUCCESS);
            baseResponse.setData(object);
        }
        return baseResponse;
    }

    //通过新旧设备
    @RequestMapping("/passDevice")
    public BaseResponse passDevice(
            @RequestParam("dId")BigInteger dId
    ){
        BaseResponse baseResponse=new BaseResponse();
        if (adminService.passDevice(dId)){
            baseResponse.setResult(ResultCodeEnum.SUCCESS);
        }else {
            baseResponse.setResult(ResultCodeEnum.UPDATE_FAILURE);//20005
        }
        return baseResponse;
    }
    //不通过新旧设备
    @RequestMapping("/notPassDevice")
    public BaseResponse notPassDevice(
            @RequestParam("dId")BigInteger dId,
            @RequestParam("dFailReason") String dFailReason
    ){
        BaseResponse baseResponse=new BaseResponse();
        if (adminService.notPassDevice(dId,dFailReason)){
            baseResponse.setResult(ResultCodeEnum.SUCCESS);
        }else {
            baseResponse.setResult(ResultCodeEnum.UPDATE_FAILURE);//20005
        }
        return baseResponse;
    }

    //已审核的旧设备列表
    @RequestMapping("/checkedSecondList")
    public BaseResponse checkedSecondList(
    ){
        BaseResponse baseResponse=new BaseResponse();
        JSONArray array=adminService.checkedDeviceList(2);
        if (array.isEmpty()){
            baseResponse.setResult(ResultCodeEnum.FIND_FAILURE);//20004
        }
        else {
            baseResponse.setResult(ResultCodeEnum.SUCCESS);
            baseResponse.setData(array);
        }
        return baseResponse;
    }

    //9-待审核的新设备设备列表
    @RequestMapping("/getWaitNewList")
    public BaseResponse getWaitNewList(){
        BaseResponse baseResponse=new BaseResponse();
        JSONArray array=adminService.getWaitDeviceList(1);
        if (array.isEmpty()){
            baseResponse.setResult(ResultCodeEnum.FIND_FAILURE);//20004
        }
        else {
            baseResponse.setResult(ResultCodeEnum.SUCCESS);
            baseResponse.setData(array);
        }
        return baseResponse;
    }

    //10-已审核的新设备列表
    @RequestMapping("/checkedNewList")
    public BaseResponse checkedNewList(){
        BaseResponse baseResponse=new BaseResponse();
        JSONArray array=adminService.checkedDeviceList(1);
        if (array.isEmpty()){
            baseResponse.setResult(ResultCodeEnum.FIND_FAILURE);//20004
        }
        else {
            baseResponse.setResult(ResultCodeEnum.SUCCESS);
            baseResponse.setData(array);
        }
        return baseResponse;
    }

    //待审核设备商列表
    @RequestMapping("/waitDBList")
    public BaseResponse waitDBList(
            @RequestParam("dbType") int dbType
    ){
        BaseResponse baseResponse=new BaseResponse();
        JSONArray array=adminService.waitDBList(dbType);//二手设备
        if (array.isEmpty()){
            baseResponse.setResult(ResultCodeEnum.FIND_FAILURE);//20004
        }
        else {
            baseResponse.setResult(ResultCodeEnum.SUCCESS);
            baseResponse.setData(array);
        }
        return baseResponse;
    }

    //带审核设备商详情页
    @RequestMapping("/waitSecondDBInfo")
    public BaseResponse waitSecondDBInfo(
            @RequestParam("dbId") BigInteger dbId
    ){
        BaseResponse baseResponse=new BaseResponse();
        JSONObject db=adminService.waitDBInfo(2,dbId);
        if (db==null||db.isEmpty()){
            baseResponse.setResult(ResultCodeEnum.FIND_FAILURE);//20004
        }
        else {
            baseResponse.setResult(ResultCodeEnum.SUCCESS);
            baseResponse.setData(db);
        }
        return baseResponse;
    }
    //带审核设备商详情页
    @RequestMapping("/waitNewAndRDBInfo")
    public BaseResponse waitNewAndRDBInfo(
            @RequestParam("dbId") BigInteger dbId
    ){
        BaseResponse baseResponse=new BaseResponse();
        JSONObject db=adminService.waitDBInfo(1,dbId);
        if (db==null||db.isEmpty()){
            baseResponse.setResult(ResultCodeEnum.FIND_FAILURE);//20004
        }
        else {
            baseResponse.setResult(ResultCodeEnum.SUCCESS);
            baseResponse.setData(db);
        }
        return baseResponse;
    }


    //已经审核设备商详情页
    @RequestMapping("/passedSecondDBList")
    public BaseResponse passedSecondDBList(
            @RequestParam("dbId") BigInteger dbId
    ){
        BaseResponse baseResponse=new BaseResponse();
        JSONObject db=adminService.waitDBInfo(2,dbId);
        if (db==null||db.isEmpty()){
            baseResponse.setResult(ResultCodeEnum.FIND_FAILURE);//20004
        }
        else {
            baseResponse.setResult(ResultCodeEnum.SUCCESS);
            baseResponse.setData(db);
        }
        return baseResponse;
    }

    //已经审核设备商详情页
    @RequestMapping("/passedNewAndRepairDBInfo")
    public BaseResponse passedNewAndRepairDBInfo(
            @RequestParam("dbId") BigInteger dbId
    ){
        BaseResponse baseResponse=new BaseResponse();
        JSONObject db=adminService.waitDBInfo(1,dbId);
        if (db==null||db.isEmpty()){
            baseResponse.setResult(ResultCodeEnum.FIND_FAILURE);//20004
        }
        else {
            baseResponse.setResult(ResultCodeEnum.SUCCESS);
            baseResponse.setData(db);
        }
        return baseResponse;
    }

    //审核设备商
    @RequestMapping("/passOrNotD")
    public BaseResponse passOrNotD(
            @RequestParam("dId") BigInteger dId,
            @RequestParam("operate") int operate//1-不通过,2-通过
    ){
        BaseResponse baseResponse=new BaseResponse();
        if(adminService.passOrNotD(dId,operate)){
            baseResponse.setResult(ResultCodeEnum.SUCCESS);
        }
        else {
            baseResponse.setResult(ResultCodeEnum.UPDATE_FAILURE);//20005
        }
        return baseResponse;
    }

    //待审核举报设备
    @RequestMapping("/waitCheckDeviceList")
    public BaseResponse waitCheckDeviceList(
            @RequestParam("dHand") int dHand
    ){
        BaseResponse baseResponse=new BaseResponse();
        JSONArray array=adminService.waitCheckDeviceList(dHand);
        if (array.isEmpty()){
            baseResponse.setResult(ResultCodeEnum.FIND_FAILURE);//20004
        }
        else {
            baseResponse.setResult(ResultCodeEnum.SUCCESS);
            baseResponse.setData(array);
        }
        return baseResponse;
    }

    //已经审核的举报设备
    @RequestMapping("/checkedReportDeviceList")
    public BaseResponse checkedReportDeviceList(
            @RequestParam("dHand") int dHand
    ){
        BaseResponse baseResponse=new BaseResponse();
        JSONArray array=adminService.checkedReportDeviceList(dHand);
        if (array.isEmpty()){
            baseResponse.setResult(ResultCodeEnum.FIND_FAILURE);//20004
        }
        else {
            baseResponse.setResult(ResultCodeEnum.SUCCESS);
            baseResponse.setData(array);
        }
        return baseResponse;
    }

    //待审核举报设备详情
    @RequestMapping("/waitCheckDeviceInfo")
    public BaseResponse waitCheckDeviceInfo(
            @RequestParam("rdId") BigInteger rdId
    ){
        BaseResponse baseResponse=new BaseResponse();
        JSONObject object=adminService.waitCheckDeviceInfo(rdId);
        if (object==null||object.isEmpty()){
            baseResponse.setResult(ResultCodeEnum.FIND_FAILURE);//20004
        }
        else {
            baseResponse.setResult(ResultCodeEnum.SUCCESS);
            baseResponse.setData(object);
        }
        return baseResponse;
    }

    //审核异常设备
    @RequestMapping("/passOrNotRD")
    public BaseResponse passOrNotRD(
            @RequestParam("rdId")BigInteger rdId,
            @RequestParam("operate") int operate//1-不通过，2-通过
    ){
        BaseResponse baseResponse=new BaseResponse();
        if (adminService.passOrNotRD(rdId,operate)){
            baseResponse.setResult(ResultCodeEnum.SUCCESS);
        }
        else {
            baseResponse.setResult(ResultCodeEnum.UPDATE_FAILURE);//20005
        }
        return baseResponse;
    }

    //已经审核举报设备详情
    @RequestMapping("/checkedDeviceInfo")
    public BaseResponse checkedDeviceInfo(
            @RequestParam("rdId") BigInteger rdId
    ){
        BaseResponse baseResponse=new BaseResponse();
        JSONObject object=adminService.waitCheckDeviceInfo(rdId);
        if (object==null||object.isEmpty()){
            baseResponse.setResult(ResultCodeEnum.FIND_FAILURE);//20004
        }
        else {
            baseResponse.setResult(ResultCodeEnum.SUCCESS);
            baseResponse.setData(object);
        }
        return baseResponse;
    }

    //-----------------------用户管理----------------------------------------
    @RequestMapping("/allDB")
    public BaseResponse allDB(
            @RequestParam("dbType") int dbType
    ){
        BaseResponse baseResponse=new BaseResponse();
        JSONArray array=adminService.allSecondDB(dbType);
        if (array.isEmpty()){
            System.out.println("allDB"+array.isEmpty()+"array==null"+(array==null));
            baseResponse.setResult(ResultCodeEnum.FIND_FAILURE);
        }
        else {
            baseResponse.setResult(ResultCodeEnum.SUCCESS);
            baseResponse.setData(array);
        }
        return baseResponse;
    }


    //-----------------合同管理----------------
    @RequestMapping("/uploadContractModel")
    public BaseResponse uploadContractModel(
            @RequestParam("contractUrl") String contractUrl
    ){//上传文件至云存储，记录文件名
        BaseResponse baseResponse=new BaseResponse();
        if (adminService.uploadContractModel(contractUrl)){
            baseResponse.setResult(ResultCodeEnum.SUCCESS);
        }
        else {
            baseResponse.setResult(ResultCodeEnum.ADD_FAILURE);//20001
        }
        return baseResponse;
    }

    //合同模板列表
    @RequestMapping("/getContractModelList")
    public BaseResponse getContractModelList(
    ){
        BaseResponse baseResponse=new BaseResponse();
        JSONArray array=adminService.getContractModelList();
        if (array.isEmpty()){
            baseResponse.setResult(ResultCodeEnum.FIND_FAILURE);//20004
        }
        else {
            baseResponse.setResult(ResultCodeEnum.SUCCESS);
            baseResponse.setData(array);

        }
        return baseResponse;
    }

    @RequestMapping("/deleteContractModel")
    public BaseResponse deleteContractModel(
            @RequestParam("ctId") BigInteger ctId
    ){//删除合同记录
        BaseResponse baseResponse=new BaseResponse();
        if (adminService.deleteContractModel(ctId)){
            baseResponse.setResult(ResultCodeEnum.SUCCESS);
        }
        else {
            baseResponse.setResult(ResultCodeEnum.DELETE_FAILURE);//20003
        }
        return baseResponse;
    }

    //待审核的用户合同列表
    @RequestMapping("/getUserContratcList")
    public BaseResponse getUserContratcList(
    ){
        BaseResponse baseResponse=new BaseResponse();
        JSONArray array=adminService.getUserContratcList();
        if (array.isEmpty()){
            baseResponse.setResult(ResultCodeEnum.FIND_FAILURE);//20004
        }
        else {
            baseResponse.setResult(ResultCodeEnum.SUCCESS);
            baseResponse.setData(array);
        }
        return baseResponse;
    }

    //审核用户合同
    @RequestMapping("/checkUserContract")
    public BaseResponse checkUserContract(
            @RequestParam("ucId") BigInteger ucId,
            @RequestParam("operate") int operate//1-否 2-过
    ){
        BaseResponse baseResponse=new BaseResponse();
        if (adminService.checkUserContract(ucId,operate)){
            baseResponse.setResult(ResultCodeEnum.SUCCESS);
        }
        else {
            baseResponse.setResult(ResultCodeEnum.UPDATE_FAILURE);//20005
        }
        return baseResponse;
    }
}
