package com.shiyi.meng.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.shiyi.meng.model.*;
import com.shiyi.meng.service.AServiceL;
import com.shiyi.meng.util.BaseResponse;
import com.shiyi.meng.util.Constant;
import com.shiyi.meng.util.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.security.sasl.SaslServer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AControllerL {
    @Autowired
    AServiceL aServiceL;
    @Autowired
    BaseResponse br;

    //管理员登录
    @RequestMapping("/loginin")
    public BaseResponse loginin(
            @RequestParam("aAccount") String aAccount,
            @RequestParam("aPwd") String aPwd
    )
    {
        Admin admin = Admin.dao.findFirst("select * from admin where aAccount=?",aAccount);
        if(admin==null)
        {
            br.setResult(ResultCodeEnum.ERROR_ACCOUNT_OR_PASSWORD);//账号不存在
        }
        else
        {
            if(admin.getAPwd().equals(aPwd))
            {
                br.setResult(ResultCodeEnum.SUCCESS);
            }else
            {
                br.setResult(ResultCodeEnum.ERROR_ACCOUNT_OR_PASSWORD);//密码错误
            }
        }
        br.setData(null);
        return br;
    }
    //审核发布的店铺，根据不同栏目显示待审核店铺列表
    @RequestMapping("/showStoreList")
    public BaseResponse showStoreList(
            @RequestParam("sColumn") Integer sColumn,
            @RequestParam("sStatus") Integer sStatus
    )
    {
        JSONArray storeList = aServiceL.showStoreList(sColumn,sStatus);
        if(storeList.isEmpty())
        {
            br.setData(null);
            br.setResult(ResultCodeEnum.FIND_ERROR);
        }else
        {
            br.setData(storeList);
            br.setResult(ResultCodeEnum.SUCCESS);
        }
        return br;
    }
    //据店铺Id显示店铺详情
    @RequestMapping("/showStoreDetail")
    public BaseResponse showStoreDetail(
            @RequestParam("sId") BigInteger sId
    )
    {
        Store store = Store.dao.findById(sId);
        if(store==null)
        {
            br.setData(null);
            br.setResult(ResultCodeEnum.FIND_ERROR);
        }else
        {
            br.setData(store);
            br.setResult(ResultCodeEnum.SUCCESS);
        }
        return br;
    }
    //审核店铺，修改店铺的状态
    @RequestMapping("/checkStore")
    public BaseResponse checkStore(
            @RequestParam("sId") BigInteger sId,
            @RequestParam("sStatus") Integer sStatus
    )
    {
        Store store = new Store();
        store.setSId(sId);
        store.setSStatus(sStatus);
        boolean flag = store.update();
        if(flag)
        {
            br.setResult(ResultCodeEnum.SUCCESS);
        }else
        {
            br.setResult(ResultCodeEnum.FIND_ERROR);
        }
        br.setData(null);
        return br;
    }
    //审核店铺模块-异常店铺列表
    @RequestMapping("/showAbStoreList")
    public BaseResponse showAbStoreList(
            @RequestParam("asStatus") Integer asStatus
    )
    {
        JSONArray abStoreList = aServiceL.selectAbStoreList(asStatus);
        if(abStoreList.isEmpty())
        {
            br.setData(null);
            br.setResult(ResultCodeEnum.FIND_ERROR);
        }else
        {
            br.setData(abStoreList);
            br.setResult(ResultCodeEnum.SUCCESS);
        }
        return br;
    }
    //显示异常店铺详情
    @RequestMapping("/showAbStore")
    public BaseResponse showAbStore(
            @RequestParam("asId") BigInteger asId//异常店铺编号
    )
    {
        JSONObject abnormalstore = aServiceL.selectAbStore(asId);
        if(abnormalstore==null)
        {
            br.setData(null);
            br.setResult(ResultCodeEnum.FIND_ERROR);
        }else
        {
            br.setData(abnormalstore);
            br.setResult(ResultCodeEnum.SUCCESS);
        }
        return br;
    }
    //审核异常店铺；如果审核成功，则将该店铺状态设置为3异常店铺；否则不对其他表进行操作
    @RequestMapping("/checkAbStore")
    public BaseResponse checkAbStore(
            @RequestParam("sId") BigInteger sId,
            @RequestParam("asStatus") Integer asStatus
    )
    {
        //首先修改异常审核表中的状态
        Abnormalstore abnormalstore = new Abnormalstore();
        abnormalstore.setAsStore(sId);
        abnormalstore.setAsStatus(asStatus);
        boolean flag = abnormalstore.update();
        if(asStatus==1)//审核成功
        {
            Store store = new Store();
            store.setSId(sId);
            store.setSStatus(3);//设置为异常店铺状态
        }
        if(flag)
        {
            br.setResult(ResultCodeEnum.SUCCESS);
        }else
        {
            br.setResult(ResultCodeEnum.UPDATE_ERROR);
        }
        br.setData(null);
        return br;
    }
    //提示词的显示
    @RequestMapping("/showHint")
    public BaseResponse showHint()
    {
        List<Hint> hintList = Hint.dao.find("select * from hint");
        if(hintList.isEmpty())
        {
            br.setData(null);
            br.setResult(ResultCodeEnum.FIND_ERROR);
        }else
        {
            br.setData(hintList);
            br.setResult(ResultCodeEnum.SUCCESS);
        }
        return br;
    }
    //提示词的修改
    @RequestMapping("/updateHint")
    public BaseResponse updateHint(
            @RequestParam("hId") BigInteger hId,
            @RequestParam("hContent") String hContent
    )
    {
        Hint hint = new Hint();
        hint.setHId(hId);
        hint.setHContent(hContent);
        boolean flag = hint.update();
        if(flag)
        {
            br.setResult(ResultCodeEnum.SUCCESS);
        }else
        {
            br.setResult(ResultCodeEnum.UPDATE_ERROR);
        }
        br.setData(null);
        return br;
    }

    //查看定制化找店信息
    @RequestMapping("/showFindStoreInfo")
    public BaseResponse showFindStoreInfo(
            @RequestParam("fdStatus") Integer fdStatus
    )
    {
        JSONArray findStoreInfo = aServiceL.findStoreInfo(fdStatus);
        if(!findStoreInfo.isEmpty())
        {
            br.setData(findStoreInfo);
            br.setResult(ResultCodeEnum.SUCCESS);
        }else
        {
            br.setData(null);
            br.setResult(ResultCodeEnum.FIND_ERROR);
        }
        return br;
    }

    //修改定制化找店信息
    @RequestMapping("/updateFindStoreInfo")
    public BaseResponse updateFindStoreInfo(
            @RequestParam("fdId") BigInteger fdId
    )
    {
        Findstore findstore  = Findstore.dao.findById(fdId);
        findstore.setFdStatus(1);//设置为“1-已完成”状态
        boolean flag = findstore.update();
        if(flag)
        {
            br.setResult(ResultCodeEnum.SUCCESS);
        }else
        {
            br.setResult(ResultCodeEnum.UPDATE_ERROR);
        }
        br.setData(null);
        return br;
    }

    //删除定制化找店信息
    @RequestMapping("/deleteFindStoreInfo")
    public BaseResponse deleteFindStoreInfo(
            @RequestParam("fdId") BigInteger fdId
    )
    {
        Findstore findstore  = Findstore.dao.findById(fdId);
        boolean flag = findstore.delete();
        if(flag)
        {
            br.setResult(ResultCodeEnum.SUCCESS);
        }else
        {
            br.setResult(ResultCodeEnum.DELETE_ERROR);
        }
        br.setData(null);
        return br;
    }

    //显示提交的返款申请
    @RequestMapping("/showReturnApply")
    public BaseResponse checkReturnApply()
    {
        JSONArray returnApply = aServiceL.showReturnApply();
        if(!returnApply.isEmpty())
        {
            br.setData(returnApply);
            br.setResult(ResultCodeEnum.SUCCESS);
        }else
        {
            br.setData(null);
            br.setResult(ResultCodeEnum.FIND_ERROR);
        }
        return br;
    }
    //同意返款申请
    @RequestMapping("/agreeReturnApply")
    public BaseResponse agreeReturnApply(
            @RequestParam("ssId") BigInteger ssId
    )
    {
        Signstore signstore = new Signstore();
        signstore.setSsId(ssId);
        signstore.setSsStatus(2);//同意返款申请
        //实际企业进行返款
        BigInteger storeId = signstore.getSsStore();//交易对应店铺
        BigInteger userId = signstore.getSsUser();//得到交易对应买方
        String openIdBuyer = User.dao.findById(userId).getUCOpenId();//买方的openid
        String openIdSaler = User.dao.findById(Store.dao.findById(storeId).getSUId()).getUCOpenId();
        Transfermoney transfermoney = Transfermoney.dao.findFirst("select tmMoney from transfermoney " +
                "where tmFrom=? AND tmStore=?",userId,storeId);//押金转账记录
        BigDecimal money = new BigDecimal(transfermoney.getTmMoney());//获得押金金额

        try {
            aServiceL.mToPOrder(new BigDecimal(Constant.RETURNMONEYTOBUY),openIdBuyer);//向找店方返98元
            aServiceL.mToPOrder(money.subtract(new BigDecimal(Constant.RETURNMONEYTOBUY)),openIdSaler);//向转店方返（押金-968元）
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean flag = signstore.update();
        if(flag)
        {
            br.setResult(ResultCodeEnum.SUCCESS);
        }else
        {
            br.setResult(ResultCodeEnum.UPDATE_ERROR);
        }
        br.setData(null);
        return br;
    }
    //拒绝返款申请
    @RequestMapping("/refuseReturnApply")
    public BaseResponse refuseReturnApply(
            @RequestParam("ssId") BigInteger ssId
    )
    {
        Signstore signstore = new Signstore();
        signstore.setSsId(ssId);
        signstore.setSsStatus(4);//拒绝返款申请
        boolean flag = signstore.update();
        if(flag)
        {
            br.setResult(ResultCodeEnum.SUCCESS);
        }else
        {
            br.setResult(ResultCodeEnum.UPDATE_ERROR);
        }
        br.setData(null);
        return br;
    }
    //查看用户上交押金信息
    @RequestMapping("/showHandInDeposit")
    public BaseResponse showHandInDeposit()
    {
        JSONArray handInDeposit = aServiceL.showHandInDeposit();
        if(!handInDeposit.isEmpty())
        {
            br.setData(handInDeposit);
            br.setResult(ResultCodeEnum.SUCCESS);
        }else
        {
            br.setData(null);
            br.setResult(ResultCodeEnum.FIND_ERROR);
        }
        return br;
    }
    //查看系统返款信息
    @RequestMapping("/showReturnDeposit")
    public BaseResponse showReturnDeposit()
    {

        JSONArray returnDeposit = aServiceL.showReturnDeposit();
        if(!returnDeposit.isEmpty())
        {
            br.setData(returnDeposit);
            br.setResult(ResultCodeEnum.SUCCESS);
        }else
        {
            br.setData(null);
            br.setResult(ResultCodeEnum.FIND_ERROR);
        }
        return br;
    }

    //显示提交的退款申请列表
    @RequestMapping("/showTuiApplyList")
    public BaseResponse showTuiApplyList()
    {
        JSONArray tuiApply = aServiceL.showTuiApplyList();
        if(!tuiApply.isEmpty())
        {
            br.setData(tuiApply);
            br.setResult(ResultCodeEnum.SUCCESS);
        }else
        {
            br.setData(null);
            br.setResult(ResultCodeEnum.FIND_ERROR);
        }
        return br;
    }

    //显示提交的退款信息详情
    @RequestMapping("/showTuiApplyDetail")
    public BaseResponse showTuiApplyDetail(
            @RequestParam("sdId") BigInteger sdId
    )
    {
        Stopdeal stopdeal = Stopdeal.dao.findById(sdId);
        if(stopdeal!=null)
        {
            br.setData(stopdeal);
            br.setResult(ResultCodeEnum.SUCCESS);
        }else
        {
            br.setData(null);
            br.setResult(ResultCodeEnum.FIND_ERROR);
        }
        return br;
    }

    //同意退款申请
    @RequestMapping("/agreeTuiApply")
    public BaseResponse agreeTuiApply(
            @RequestParam("ssId") BigInteger ssId
    )
    {
        Signstore signstore = Signstore.dao.findById(ssId);
        signstore.setSsStatus(3);//同意退款申请
        //实际企业进行退款
        BigInteger storeId = signstore.getSsStore();//交易对应店铺
        BigInteger userId = signstore.getSsUser();//得到交易对应买方
        String openId = User.dao.findById(userId).getUCOpenId();//买方的openid
        Transfermoney transfermoney = Transfermoney.dao.findFirst("select tmMoney from transfermoney " +
                "where tmFrom=? AND tmStore=?",userId,storeId);//押金转账记录
        BigDecimal money = new BigDecimal(transfermoney.getTmMoney());//获得押金金额

        try {
            aServiceL.mToPOrder(money,openId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean flag = signstore.update();
        if(flag)
        {
            br.setResult(ResultCodeEnum.SUCCESS);
        }else
        {
            br.setResult(ResultCodeEnum.UPDATE_ERROR);
        }
        br.setData(null);
        return br;
    }
    //拒绝退款申请
    @RequestMapping("/refuseTuiApply")
    public BaseResponse refuseTuiApply(
            @RequestParam("ssId") BigInteger ssId
    )
    {
        Signstore signstore = new Signstore();
        signstore.setSsId(ssId);
        signstore.setSsStatus(5);//拒绝退款申请
        boolean flag = signstore.update();
        if(flag)
        {
            br.setResult(ResultCodeEnum.SUCCESS);
        }else
        {
            br.setResult(ResultCodeEnum.UPDATE_ERROR);
        }
        br.setData(null);
        return br;
    }
    //待审核发布店铺|待审核举报店铺
    @RequestMapping("/getNumOfStore")
    public BaseResponse getNumOfStore()
    {
        //待审核 sStatus为0
        Integer unCheckReleaseStore = Db.queryInt("select count(*) from store " +
                "where sStatus=0");
        //待审核举报店铺
        Integer unCheckAbnormalStore = Db.queryInt("select count(*) from abnormalstore " +
                "where asStatus=0");
        //待审核设备
        Integer unCheckDevice = Db.queryInt("select count(*) from device " +
                "where dStatus=0");
        //待审核设备商
        Integer unCheckDeviceBusiness = Db.queryInt("select count(*) from devicebusiness " +
                "where dbStatus=0");
        //待审核举报设备
        Integer unCheckReportDevice = Db.queryInt("select count(*) from reportdevice " +
                "where rdStatus=0");
        JSONObject returnData = new JSONObject();
        returnData.put("unCheckReleaseStore",unCheckReleaseStore);
        returnData.put("unCheckAbnormalStore",unCheckAbnormalStore);
        returnData.put("unCheckDevice",unCheckDevice);
        returnData.put("unCheckDeviceBusiness",unCheckDeviceBusiness);
        returnData.put("unCheckReportDevice",unCheckReportDevice);
        if(returnData!=null)
        {
            br.setData(returnData);
            br.setResult(ResultCodeEnum.SUCCESS);
        }else
        {
            br.setResult(ResultCodeEnum.UPDATE_ERROR);
        }
        br.setData(null);
        return br;
    }

    //企业返款给个人
    @RequestMapping("/returnMoney")
    public BaseResponse createOrder(
            @RequestParam("uId") BigInteger uId,
            @RequestParam("money") BigDecimal money
    ){

        //调用接口获取openId
        String openId = User.dao.findById(uId).getUCOpenId();
//        BigDecimal money = new BigDecimal("0.3");
        Map<String, String> reqParams = new HashMap<>();
        try {
            aServiceL.mToPOrder(money,openId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        br.setData(null);
        br.setResult(ResultCodeEnum.SUCCESS);
        return br;
    }









}
