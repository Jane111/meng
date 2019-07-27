package com.shiyi.meng.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shiyi.meng.model.*;
import com.shiyi.meng.util.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AServiceL {

    public JSONObject packStoreForAdmin(Store store)
    {
        JSONObject packedStore = new JSONObject();
        /*
        * 1“店铺出租”租金/2“生意转让”转让费，租金/3“店铺出售”售价/4“仓库出租”租金
        * */
        packedStore.put("sId",store.getSId());//店铺Id
        packedStore.put("uId",store.getSUId());//店铺所有人
        packedStore.put("sPhone",store.getSPhone());//创建店铺时填写的电话号码
        packedStore.put("sConName",store.getSConName());//创建店铺时填写的姓名
        packedStore.put("sName",store.getSName());//店铺名称
        packedStore.put("sArea",store.getSAera());//店铺面积
        packedStore.put("sColumn",store.getSColumn());//店铺栏目
        packedStore.put("sLoc",store.getSLoc());//定位得到位置

        packedStore.put("sStatus",store.getSStatus());//店铺状态

        Integer column = store.getSColumn();//店铺所在栏目
        if(column==1||column==4)
        {
            packedStore.put("sMoney",store.getSRentMoney());//每月租金
        }else if(column==2)
        {
            packedStore.put("sMoney",store.getSTranMoney());//转让费/售价
        }else if(column==3)
        {
            packedStore.put("sMoney",store.getSDeposit());//售价
        }
        return packedStore;
    }

    public JSONArray showStoreList(Integer sColumn,Integer sStatus)
    {
        JSONArray showStoreList = new JSONArray();
        List<Store> storeList = Store.dao.find("select * from store " +
                "where sColumn=? AND sStatus=?",sColumn,sStatus);
        //不同状态的店铺，在user端显示时也不同，不显示没有通过审核，以及举报通过的店铺
        for(Store store:storeList)
        {
            showStoreList.add(packStoreForAdmin(store));
        }
        return showStoreList;
    }
    //异常店铺列表
    public JSONArray selectAbStoreList(Integer asStatus)
    {
        JSONArray abStoreList = new JSONArray();
        List<Abnormalstore> abnormalstoreList = Abnormalstore.dao.find("select * from abnormalstore " +
                "where asStatus=?",asStatus);
        for(Abnormalstore abnormalstore:abnormalstoreList)
        {
            Store store = Store.dao.findById(abnormalstore.getAsStore());
            JSONObject abStore = new JSONObject();
            abStore.put("sId",store.getSId());//店铺Id
            abStore.put("sName",store.getSName()); //被举报的店铺名称
            abStore.put("sLoc",store.getSLoc());//位置
            abStore.put("sColumn",store.getSColumn());//被举报的店铺类型
            abStore.put("asId",abnormalstore.getAsId());//异常店铺Id
            abStore.put("asContact",abnormalstore.getAsContact());//举报联系人姓名
            abStore.put("asPhone",abnormalstore.getAsPhone());//举报者电话号码
            abStore.put("asType",abnormalstore.getAsType());//举报类型
            abStoreList.add(abStore);
        }
        return abStoreList;
    }
    public JSONObject selectAbStore(BigInteger asId)
    {
        Abnormalstore abnormalstore = Abnormalstore.dao.findById(asId);
        JSONObject abStore = new JSONObject();
        abStore.put("asId",abnormalstore.getAsId());//异常店铺Id
        abStore.put("asContact",abnormalstore.getAsContact());//举报联系人姓名
        abStore.put("asPhone",abnormalstore.getAsPhone());//举报者电话号码
        abStore.put("asType",abnormalstore.getAsType());//举报类型
        abStore.put("asReason",abnormalstore.getAsReason());//举报原因
        String photo = abnormalstore.getAsPhoto();
        String[] photoList = photo.split("###");
        abStore.put("asPhoto",photoList);//图片
        //异常店铺详细信息
        Store store = Store.dao.findById(abnormalstore.getAsStore());
        abStore.put("sName",store.getSName());//店铺名称
        abStore.put("sType",store.getSType());//店铺类型
        abStore.put("sLoc",store.getSLoc());//店铺位置
        return abStore;
    }

    //定制化找店信息列表
    public JSONArray findStoreInfo(Integer fdStatus)
    {
        JSONArray fStoreList = new JSONArray();
        List<Findstore> findstoreList = Findstore.dao.find("select * from findstore " +
                "where fdStatus=?",fdStatus);
        for(Findstore findstore:findstoreList)
        {
            JSONObject findStoreDetial = new JSONObject();
            findStoreDetial.put("fdCommand",findstore.getFdCommand());
            findStoreDetial.put("fdId",findstore.getFdId());
            findStoreDetial.put("fdPhone",findstore.getFdPhone());
            findStoreDetial.put("fdName",findstore.getFdName());
            User user = User.dao.findById(findstore.getFdUser());
            findStoreDetial.put("uWeiXinIcon",user.getUWeiXinIcon());
            findStoreDetial.put("uWeiXinName",user.getUWeiXinName());
            fStoreList.add(findStoreDetial);
        }
        return fStoreList;
    }

    //显示提交的返款申请
    public JSONArray showReturnApply()
    {
        JSONArray returnApplyList = new JSONArray();
        List<Signstore> returnmoneyList = Signstore.dao.find("select * from signstore " +
                "where ssStatus=?",0);//用户提交了返款申请状态
        for(Signstore signstore:returnmoneyList)
        {
            JSONObject returnApply = new JSONObject();
            //用户签约店铺的id
            returnApply.put("ssId",signstore.getSsId());
            //用户微信名称
            returnApply.put("uWeiXinName",User.dao.findById(signstore.getSsUser()).getUWeiXinName());
            //店铺名称
            returnApply.put("sName",Store.dao.findById(signstore.getSsStore()).getSName());
            returnApplyList.add(returnApply);
        }
        return returnApplyList;
    }
    public JSONArray showTuiApplyList()
    {
        JSONArray returnApplyList = new JSONArray();
        List<Signstore> returnmoneyList = Signstore.dao.find("select * from signstore " +
                "where ssStatus=?",1 );//用户提交了退款申请状态
        for(Signstore signstore:returnmoneyList)
        {
            JSONObject returnApply = new JSONObject();
            //用户签约店铺的id
            returnApply.put("ssId",signstore.getSsId());
            //店铺名称
            returnApply.put("sName",Store.dao.findById(signstore.getSsStore()).getSName());
            //退款信息Id
            returnApply.put("sdId",signstore.getSsStopDeal());

            //查到对应的退款申请
            Stopdeal stopdeal = Stopdeal.dao.findById(signstore.getSsStopDeal());
            returnApply.put("sdProblem",stopdeal.getSdProblem());
            returnApply.put("sdPhoto",stopdeal.getSdPhoto());
            returnApply.put("sdApplyName",stopdeal.getSdApplyName());
            returnApply.put("sdApplyNum",stopdeal.getSdApplyNum());
            returnApply.put("sdApplyPhone",stopdeal.getSdApplyPhone());

            returnApplyList.add(returnApply);
        }
        return returnApplyList;
    }


    //查看用户上交押金信息
    public JSONArray showHandInDeposit()
    {
        JSONArray showHandInList = new JSONArray();
        List<Transfermoney> transfermoneyList = Transfermoney.dao.find("select * from transfermoney " +
                "where tmTo=0");//tmTo为0，表示转账给平台
        for(Transfermoney transfermoney:transfermoneyList)
        {
            JSONObject showHandIn = new JSONObject();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            showHandIn.put("tmFrom",User.dao.findById(transfermoney.getTmFrom()).getUWeiXinName());
            showHandIn.put("tmStore",Store.dao.findById(transfermoney.getTmStore()).getSName());
            showHandIn.put("tmMoney",transfermoney.getTmMoney());
            showHandIn.put("tmCreateTime",transfermoney.getTmCreateTime());
            showHandInList.add(showHandIn);
        }
        return showHandInList;
    }

    //查看系统返款信息
    public JSONArray showReturnDeposit()
    {
        JSONArray showReturnList = new JSONArray();
        List<Transfermoney> transfermoneyList = Transfermoney.dao.find("select * from transfermoney " +
                "where tmFrom=0");//tmFrom为0，表示转账来源为平台
        for(Transfermoney transfermoney:transfermoneyList)
        {
            JSONObject showReturn = new JSONObject();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            showReturn.put("tmTo",User.dao.findById(transfermoney.getTmTo()).getUWeiXinName());
            showReturn.put("tmStore",Store.dao.findById(transfermoney.getTmStore()).getSName());
            showReturn.put("tmModifyTime",transfermoney.getTmModifyTime());
            showReturnList.add(showReturn);
        }
        return showReturnList;
    }

    /**
     * 功能描述: <调用企业支付到零钱的接口>
     **/
    public void mToPOrder(BigDecimal money, String openid) throws Exception {
        Map<String, String> reqParams = new HashMap<>();
        //微信分配的小程序ID
        reqParams.put("mch_appid", Constant.APPID);
        //微信支付分配的商户号
        reqParams.put("mchid", Constant.MCH_ID);
        //随机字符串
        reqParams.put("nonce_str", System.currentTimeMillis() / 1000 + "");
        //商户订单号，需保持唯一性(只能是字母或者数字，不能包含有其他字符)
        String partner_trade_no= UUID.randomUUID().toString().replaceAll("-", "");
        reqParams.put("partner_trade_no", partner_trade_no);
        //用户openid，oxTWIuGaIt6gTKsQRLau2M0yL16E，商户appid下，某用户的openid
        reqParams.put("openid", openid);
        //校验用户姓名选项 	NO_CHECK：不校验真实姓名/FORCE_CHECK：强校验真实姓名
        reqParams.put("check_name", "NO_CHECK");
        //订单总金额，单位为分
        reqParams.put("amount", money.multiply(BigDecimal.valueOf(100)).intValue() + "");//都是整数，以分为单位
        //终端IP
        reqParams.put("spbill_create_ip", "127.0.0.1");
        //企业付款备注,企业付款备注，必填。
        reqParams.put("desc", "萌系餐饮小程序交易成功押金返还");
        //签名
        String sign = WXPayUtil.generateSignature(reqParams, Constant.KEY);
        reqParams.put("sign", sign);

        String xml = PaymentKit.toXml(reqParams);
        String restxml = HttpUtils.posts("https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers", xml);
        System.out.println(restxml);
    }

    //待审核合同列表
    public JSONArray showUncheckedContractList()
    {
        JSONArray returnData = new JSONArray();
        List<Usercontract> uncheckedContractList = Usercontract.dao.find("select * from usercontract " +
                "where ucstatus=0");
        for(Usercontract usercontract:uncheckedContractList)
        {
            JSONObject contractDetail = new JSONObject();
            contractDetail.put("ucId",usercontract.getUcId());//合同Id
            contractDetail.put("ucIdUrl",usercontract.getUcIdUrl());
            contractDetail.put("ucBusinessUrl",usercontract.getUcBusinessUrl());
            contractDetail.put("ucContractUrl",usercontract.getUcContractUrl());
            Store store = Store.dao.findById(usercontract.getUcStore());
            contractDetail.put("sName",store.getSConName());//店铺负责人姓名
            contractDetail.put("sPhone",store.getSPhone());//店铺负责人联系电话
            returnData.add(contractDetail);
        }
        return returnData;
    }

    //待审核合同列表
    public JSONArray showCheckedContractList()
    {
        JSONArray returnData = new JSONArray();
        List<Usercontract> uncheckedContractList = Usercontract.dao.find("select * from usercontract " +
                "where ucstatus!=0");
        for(Usercontract usercontract:uncheckedContractList)
        {
            JSONObject contractDetail = new JSONObject();
            contractDetail.put("ucId",usercontract.getUcId());//合同Id
            contractDetail.put("ucStatus",usercontract.getUcStatus());//合同status
            contractDetail.put("ucIdUrl",usercontract.getUcIdUrl());
            contractDetail.put("ucBusinessUrl",usercontract.getUcBusinessUrl());
            contractDetail.put("ucContractUrl",usercontract.getUcContractUrl());
            Store store = Store.dao.findById(usercontract.getUcStore());
            contractDetail.put("sName",store.getSConName());//店铺负责人姓名
            contractDetail.put("sPhone",store.getSPhone());//店铺负责人联系电话
            returnData.add(contractDetail);
        }
        return returnData;
    }
}
