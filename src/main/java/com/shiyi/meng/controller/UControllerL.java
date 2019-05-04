package com.shiyi.meng.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shiyi.meng.model.Abnormalstore;
import com.shiyi.meng.model.Followstore;
import com.shiyi.meng.model.Store;
import com.shiyi.meng.model.User;
import com.shiyi.meng.service.UServiceL;
import com.shiyi.meng.util.BaseResponse;
import com.shiyi.meng.util.Constant;
import com.shiyi.meng.util.HttpClientUtil;
import com.shiyi.meng.util.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static java.awt.SystemColor.text;

@RestController
@RequestMapping("/user")
public class UControllerL {
    @Autowired
    UServiceL uServiceL;
    @Autowired
    BaseResponse br;
    /*
    *转店的  我的
    *设备的  卖二手  我的
    * */
    //1、小程序用户授权
    @RequestMapping("/authorize")
    public BaseResponse authorize(
            @RequestParam("code") String code,
            @RequestParam("uWeiXinIcon") String uWeiXinIcon,
            @RequestParam("uWeiXinName") String uWeiXinName
    )

    {
        // 配置请求参数
        Map<String, String> param = new HashMap<>();
        param.put("appid", Constant.APPID);
        param.put("secret", Constant.APPSECRET);
        param.put("js_code", code);
        param.put("grant_type", Constant.GRANTTYPE);
        // 发送请求
        String wxResult = HttpClientUtil.doGet(Constant.LOGINURL, param);
        System.out.println(wxResult);
        JSONObject jsonObject = JSONObject.parseObject(wxResult);
        // 获取参数返回的
        String session_key = jsonObject.get("session_key").toString();
        String open_id = jsonObject.get("openid").toString();
        // 封装返回小程序
        Map<String, String> result = new HashMap<>();
        result.put("session_key", session_key);
        result.put("open_id", open_id);

        // 根据返回的user实体类，判断用户是否是新用户，是的话，将用户信息存到数据库
        User user = uServiceL.selectUserByOpenId(open_id);
        if(user == null){

            User insert_user = new User();
            insert_user.setUCOpenId(open_id);//openid
            insert_user.setUWeiXinIcon(uWeiXinIcon);//微信头像
            insert_user.setUWeiXinName(uWeiXinName);//微信名称
            // 添加到数据库
            Boolean flag = insert_user.save();
            if(!flag)
            {
                result.put("uId", insert_user.getUId()+"");
                br.setResult(ResultCodeEnum.ADD_ERROR);
            }
        }
        else{
            result.put("uId", user.getUId()+"");
            br.setResult(ResultCodeEnum.SUCCESS);
        }
        br.setData(result);
        return br;
    }

    //添加店铺
    @RequestMapping("/addStore")
    public BaseResponse addStore(
            @RequestParam("sUId") BigInteger sUId,//用户ID
            @RequestParam("sColumn") Integer sColumn,//店铺所属栏目
            @RequestParam("sPhoto") String sPhoto,//店铺图片
            @RequestParam("sDes") String sDes,//店铺描述
            @RequestParam("sName") String sName,//店铺名称
            @RequestParam("sConName") String sConName,//店铺联系人姓名
            @RequestParam("sPhone") String sPhone,//店铺联系人方式
            @RequestParam("sFloor") Integer sFloor,//楼层
            @RequestParam("sPayMethod") Integer sPayMethod,//付款方式
            @RequestParam("sWareHouse") Integer sWareHouse,//仓库所在层
            @RequestParam("sAera") Float sAera//面积
    )
    {
        Store store = new Store();
        store.setSUId(sUId);
        store.setSColumn(sColumn);
        store.setSPhoto(sPhoto);
        store.setSDes(sDes);
        store.setSName(sName);
        store.setSConName(sConName);
        store.setSPhone(sPhone);
        store.setSFloor(sFloor);
        store.setSPayMethod(sPayMethod);
        store.setSWareHouse(sWareHouse);
        store.setSAera(sAera);
        Boolean flag = store.save();
        if(flag)
        {
            br.setResult(ResultCodeEnum.SUCCESS);//添加成功
        }
        else
        {
            br.setResult(ResultCodeEnum.ADD_ERROR);
        }
        br.setData(null);
        return br;
    }

    //添加店铺
    @RequestMapping("/updateStore")
    public BaseResponse updateStore(
            @RequestParam("sId") BigInteger sId,//店铺ID
            @RequestParam("sColumn") Integer sColumn,//店铺所属栏目
            @RequestParam("sPhoto") String sPhoto,//店铺图片
            @RequestParam("sDes") String sDes,//店铺描述
            @RequestParam("sName") String sName,//店铺名称
            @RequestParam("sConName") String sConName,//店铺联系人姓名
            @RequestParam("sPhone") String sPhone,//店铺联系人方式
            @RequestParam("sFloor") Integer sFloor,//楼层
            @RequestParam("sPayMethod") Integer sPayMethod,//付款方式
            @RequestParam("sWareHouse") Integer sWareHouse,//仓库所在层
            @RequestParam("sAera") Float sAera//面积
    )
    {
        Store store = new Store();
        store.setSId(sId);
        store.setSColumn(sColumn);
        store.setSPhoto(sPhoto);
        store.setSDes(sDes);
        store.setSName(sName);
        store.setSConName(sConName);
        store.setSPhone(sPhone);
        store.setSFloor(sFloor);
        store.setSPayMethod(sPayMethod);
        store.setSWareHouse(sWareHouse);
        store.setSAera(sAera);
        Boolean flag = store.update();
        if(flag)
        {
            br.setResult(ResultCodeEnum.SUCCESS);//添加成功
        }
        else
        {
            br.setResult(ResultCodeEnum.UPDATE_ERROR);
        }
        br.setData(null);
        return br;
    }

    //首页显示店铺，按照时间排序
    @RequestMapping("/showStoreHomePage")
    public BaseResponse showStoreHomePage()
    {
        JSONArray storeList = uServiceL.getStoreListHomePage();
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

    //点击按钮使店铺更新
    @RequestMapping("/updateStoreRank")
    public BaseResponse updateStoreRank(
            @RequestParam("sId") BigInteger sId
    )
    {
        Store store = new Store();
        store.setSId(sId);
        boolean flag = store.update();//更新店铺修改时间
        if(flag)
        {
            br.setResult(ResultCodeEnum.SUCCESS);
        }
        else
        {
            br.setResult(ResultCodeEnum.UPDATE_ERROR);
        }
        br.setData(null);
        return br;
    }
    //查看店铺详情
    @RequestMapping("/showStoreDetail")
    public BaseResponse showStoreDetail(
            @RequestParam("sId") BigInteger sId,
            @RequestParam(value = "uId",required = false) BigInteger uId
    )
    {
        if(uId==null)
        {
            uId = new BigInteger("0");//用户没有登陆的情况
        }
        JSONObject storeDetail = uServiceL.getStoreDetail(sId,uId);
        if(storeDetail.isEmpty())
        {
            br.setData(null);
            br.setResult(ResultCodeEnum.FIND_ERROR);
        }else
        {
            br.setData(storeDetail);
            br.setResult(ResultCodeEnum.SUCCESS);
        }
        return br;
    }

    //关注店铺
    @RequestMapping("/followStore")
    public BaseResponse followStore(
            @RequestParam("sId") BigInteger sId,
            @RequestParam("uId") BigInteger uId
    )
    {
        Followstore followstore = new Followstore();
        followstore.setFsUser(uId);
        followstore.setFsStore(sId);
        boolean flag = followstore.save();
        if(flag)
        {
            br.setResult(ResultCodeEnum.SUCCESS);
        }else
        {
            br.setResult(ResultCodeEnum.ADD_ERROR);
        }
        br.setData(null);
        return br;
    }
    //取消关注店铺
    @RequestMapping("/deFollowStore")
    public BaseResponse deFollowStore(
            @RequestParam("sId") BigInteger sId,
            @RequestParam("uId") BigInteger uId
    )
    {
        Followstore followstore = new Followstore();
        followstore.setFsUser(uId);
        followstore.setFsStore(sId);
        boolean flag = followstore.delete();
        if(flag)
        {
            br.setResult(ResultCodeEnum.SUCCESS);
        }else
        {
            br.setResult(ResultCodeEnum.DELETE_FAILURE);
        }
        br.setData(null);
        return br;
    }
    //用户举报店铺
    @RequestMapping("/reportStore")
    public BaseResponse reportStore(
            @RequestParam("sId") BigInteger sId,
            @RequestParam("uId") BigInteger uId
    )
    {
        Abnormalstore abnormalstore = new Abnormalstore();
        abnormalstore.setAsStore(sId);
        abnormalstore.setAsUser(uId);
        boolean flag = abnormalstore.save();
        if(flag)
        {
            br.setResult(ResultCodeEnum.SUCCESS);
        }else
        {
            br.setResult(ResultCodeEnum.ADD_ERROR);
        }
        br.setData(null);
        return br;
    }
    /*
    * 转店的“我的”
    * */
    //查看用户发布的店铺列表
    @RequestMapping("/showMyStoreList")
    public BaseResponse showMyStoreList(
            @RequestParam("uId") BigInteger uId
    )
    {
        JSONArray myStoreList = uServiceL.getUserStoreList(uId);
        if(!myStoreList.isEmpty())
        {
            br.setData(myStoreList);
            br.setResult(ResultCodeEnum.SUCCESS);
        }else
        {
            br.setData(null);
            br.setResult(ResultCodeEnum.FIND_ERROR);
        }
        return br;
    }

    //查看用户关注的店铺列表
    @RequestMapping("/showFollowStoreList")
    public BaseResponse showFollowStoreList(
            @RequestParam("uId") BigInteger uId
    )
    {
        JSONArray followStoreList = uServiceL.getFollowStoreList(uId);
        if(!followStoreList.isEmpty())
        {
            br.setData(followStoreList);
            br.setResult(ResultCodeEnum.SUCCESS);
        }else
        {
            br.setData(null);
            br.setResult(ResultCodeEnum.FIND_ERROR);
        }
        return br;
    }

    //查看用户签约的店铺列表
    @RequestMapping("/showSignStoreList")
    public BaseResponse showSignStoreList(
            @RequestParam("uId") BigInteger uId
    )
    {
        JSONArray signStoreList = uServiceL.getSignStoreList(uId);
        if(!signStoreList.isEmpty())
        {
            br.setData(signStoreList);
            br.setResult(ResultCodeEnum.SUCCESS);
        }else
        {
            br.setData(null);
            br.setResult(ResultCodeEnum.FIND_ERROR);
        }
        return br;
    }





}
