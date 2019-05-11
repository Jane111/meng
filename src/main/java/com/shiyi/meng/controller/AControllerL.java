package com.shiyi.meng.controller;

import com.alibaba.fastjson.JSONArray;
import com.shiyi.meng.model.Abnormalstore;
import com.shiyi.meng.model.Admin;
import com.shiyi.meng.model.Hint;
import com.shiyi.meng.model.Store;
import com.shiyi.meng.service.AServiceL;
import com.shiyi.meng.util.BaseResponse;
import com.shiyi.meng.util.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.List;

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
        boolean flag = store.save();
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









}
