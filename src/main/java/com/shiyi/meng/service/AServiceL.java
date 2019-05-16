package com.shiyi.meng.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shiyi.meng.model.*;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class AServiceL {

    public JSONObject packStoreForAdmin(Store store)
    {
        JSONObject packedStore = new JSONObject();
        packedStore.put("sId",store.getSId());//店铺Id
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
            abStore.put("sType",store.getSType());//类型
            abStore.put("sLoc",store.getSLoc());//位置
            abStoreList.add(abStore);
        }
        return abStoreList;
    }

    //定制化找店信息列表
    public JSONArray findStoreInfo()
    {
        JSONArray fStoreList = new JSONArray();
        List<Findstore> findstoreList = Findstore.dao.find("select * from findstore");
        for(Findstore findstore:findstoreList)
        {
            JSONObject findStoreDetial = new JSONObject();
            findStoreDetial.put("fdCommand",findstore.getFdCommand());
            findStoreDetial.put("fdPhone",findstore.getFdPhone());
            findStoreDetial.put("fdName",findstore.getFdName());
            User user = User.dao.findById(findstore.getFdUser());
            findStoreDetial.put("uWeiXinIcon",user.getUWeiXinIcon());
            findStoreDetial.put("uWeiXinName",user.getUWeiXinName());
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
    public JSONArray showTuiApply()
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

            //查到对应的退款申请
            Stopdeal stopdeal = Stopdeal.dao.findById(signstore.getSsStopDeal());
            returnApply.put("sdProblem",stopdeal.getSdProblem());
            returnApply.put("sdPhoto",stopdeal.getSdPhoto());
            returnApply.put("sdApplyName",stopdeal.getSdApplyName());
            returnApply.put("sdApplyNum",stopdeal.getSdApplyNum());
            returnApply.put("sdApplyPhone",stopdeal.getSdApplyPhone());
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
                "where tmFrom=0 AND tmStatus=1");//tmTo为0，表示转账来源为平台,状态为同意退款的
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






}
