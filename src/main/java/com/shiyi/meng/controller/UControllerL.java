package com.shiyi.meng.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.shiyi.meng.model.*;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            @RequestParam("uWeiXinName") String uWeiXinName,
            @RequestParam("uCity") String uCity
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
            insert_user.setUCity(uCity);//用户所在城市
            // 添加到数据库
            Boolean flag = insert_user.save();
            if(flag)
            {
                result.put("uId", insert_user.getUId()+"");
                br.setResult(ResultCodeEnum.SUCCESS);
            }else
            {
                result.put("uId", "");
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
            @RequestParam("sType") Integer sType,//店铺图片
            @RequestParam("sDes") String sDes,//店铺描述
            @RequestParam("sName") String sName,//店铺名称
            @RequestParam("sConName") String sConName,//店铺联系人姓名
            @RequestParam("sPhone") String sPhone,//店铺联系人方式
            @RequestParam("sTotalFloor") Integer sTotalFloor,//楼层
            @RequestParam("sPayMethod") String sPayMethod,//付款方式
            @RequestParam("sWareHouse") Integer sWareHouse,//仓库所在层
            @RequestParam("sAera") Float sAera,//面积
            @RequestParam("sLoc") String sLoc,//店铺位置
            @RequestParam("sLng") Float sLng,//店铺经度
            @RequestParam("sLat") Float sLat,//店铺纬度
            @RequestParam("sCity") String sCity,//店铺所在城市
            @RequestParam("sUserWriteLoc") String sUserWriteLoc,//用户填写的店铺位置
            @RequestParam("sConnectType") Integer sConnectType,//店铺另一种联系方式类别1微信2QQ3邮箱
            @RequestParam("sConnectWay") String sConnectWay,//店铺另一种联系方式
            @RequestParam("sTranMoney") float sTranMoney,//转让费
            @RequestParam("sLeftTime") String sLeftTime,//剩余租期
            @RequestParam("sStoreStatus") Integer sStoreStatus,//状态
            @RequestParam("sECharge") Float sECharge,//电费
            @RequestParam("sWCharge") Float sWCharge,//水费
            @RequestParam("sFee") Float sFee,//物业费
            @RequestParam("sTag") String sTag,//标签
            @RequestParam("sRentMoney") Float sRentMoney,//月租金
            @RequestParam("sDeposit") Float sDeposit//店铺售价
    )
    {
        //为新添加的店铺分配最近的高校和商圈
        double minSpot=10000d;
        double minSchool=10000d;
        double minBusiness=10000d;
        //1、根据城市找到对应的商圈景点高校数据
        List<Cityloc> citylocList = uServiceL.getCityDetailByCity(sCity);
        Map<Double,BigInteger> distanceList = new HashMap<>();
        for(Cityloc cityloc:citylocList)
        {
            //计算距离
            double distance = getDistance(cityloc.getClLat(),cityloc.getClLng(),sLat,sLng);
            distanceList.put(distance,cityloc.getClId());
            Integer type = cityloc.getClType();//得到该poi的类型
            if(type==0)//景点
            {
                if(distance<minSpot)
                {
                    minSpot=distance;
                }
            }else if(type==1)//高校
            {
                if(distance<minSchool)
                {
                    minSchool=distance;
                }
            }else if(type==2)//商圈
            {
                if(distance<minBusiness)
                {
                    minBusiness=distance;
                }
            }
        }
        Store store = new Store();
        store.setSSpot(distanceList.get(minSpot));
        store.setSBusiness(distanceList.get(minBusiness));
        store.setSSchool(distanceList.get(minSchool));

        store.setSUId(sUId);
        store.setSColumn(sColumn);
        store.setSPhoto(sPhoto);
        store.setSType(sType);
        store.setSDes(sDes);
        store.setSName(sName);
        store.setSConName(sConName);
        store.setSPhone(sPhone);
        store.setSTotalFloor(sTotalFloor);
        store.setSPayMethod(sPayMethod);
        store.setSWareHouse(sWareHouse);
        store.setSAera(sAera);
        store.setSLoc(sLoc);
        store.setSLat(sLat);
        store.setSLng(sLng);
        store.setSCity(sCity);
        store.setSUserWriteLoc(sUserWriteLoc);
        store.setSConnectType(sConnectType);
        store.setSConnectWay(sConnectWay);
        store.setSTranMoney(sTranMoney);
        store.setSLeftTime(sLeftTime);
        store.setSStatus(sStoreStatus);
        store.setSECharge(sECharge);
        store.setSWCharge(sWCharge);
        store.setSFee(sFee);
        store.setSTag(sTag);
        store.setSRentMoney(sRentMoney);
        store.setSDeposit(sDeposit);
        store.setSStatus(0);//店铺状态为"未审核"
        store.setSFlushTime(new BigInteger("0"));//设置刷新置顶的次数为0
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

    //根据经纬度算两点之间的距离
    public double getDistance(float lat1, float lng1, float lat2, float lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000d) / 10000d;
        s = s*1000;
        return s;
    }
    private static double EARTH_RADIUS = 6378.137;
    private double rad(float d) {
        return d * Math.PI / 180.0;
    }


    //修改店铺信息
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
            @RequestParam("sPayMethod") String sPayMethod,//付款方式
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
        store.setSTotalFloor(sFloor);
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
    public BaseResponse showStoreHomePage(
            @RequestParam("uCity") String uCity,
            @RequestParam("pageIndex") Integer pageIndex,
            @RequestParam("pageSize") Integer pageSize
    )
    {
        JSONArray storeList = uServiceL.getStoreListHomePage(uCity,pageIndex,pageSize);
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
        //得到sId对应的store
        Store pastStore = Store.dao.findById(sId);
        Store store = new Store();
        store.setSId(sId);
        store.setSFlushTime(pastStore.getSFlushTime().add(new BigInteger("1")));
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
        Followstore followstore = Followstore.dao.findFirst("select * from followstore " +
                "where fsuser=? and fsstore=?",uId,sId);
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
            @RequestParam("uId") BigInteger uId,
            @RequestParam("asContact") String asContact,
            @RequestParam("asPhone") String asPhone,
            @RequestParam("asReason") String asReason,
            @RequestParam("asPhoto") String asPhoto,
            @RequestParam("asType") Integer asType
    )
    {
        Abnormalstore abnormalstore = new Abnormalstore();
        abnormalstore.setAsStore(sId);
        abnormalstore.setAsUser(uId);
        abnormalstore.setAsContact(asContact);
        abnormalstore.setAsPhone(asPhone);
        abnormalstore.setAsReason(asReason);
        abnormalstore.setAsPhoto(asPhoto);
        abnormalstore.setAsType(asType);
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
    //搜索店铺，根据店铺名称
    @RequestMapping("/searchStoreByName")
    public BaseResponse searchStoreByName(
            @RequestParam("sContent") String sContent
    )
    {
        JSONArray storeList = uServiceL.searchStoreByName(sContent);
        if(!storeList.isEmpty())
        {
            br.setData(storeList);
            br.setResult(ResultCodeEnum.SUCCESS);
        }else
        {
            br.setData(null);
            br.setResult(ResultCodeEnum.FIND_ERROR);
        }
        return br;
    }
    //添加用户的搜索记录
    @RequestMapping("/addSearchRecord")
    public BaseResponse addSearchRecord(
            @RequestParam("sContent") String sContent,
            @RequestParam("uId") BigInteger uId
    )
    {
        List<Searchrecord> recordList = Searchrecord.dao.find("select * from searchrecord " +
                "where srOwner=?",uId);
        boolean flag = false;//该搜索记录在数据库中是没有的
        boolean result = false;
        for(Searchrecord br:recordList)
        {
            if(br.getSrContent().equals(sContent))
            {
                flag = true;
            }
        }
        if(!flag)
        {
            Searchrecord searchrecord = new Searchrecord();
            searchrecord.setSrOwner(uId);
            searchrecord.setSrContent(sContent);
            result = searchrecord.save();
        }
        if(result)
        {
            br.setResult(ResultCodeEnum.SUCCESS);
        }
        else
        {
            br.setResult(ResultCodeEnum.ADD_ERROR);
        }
        br.setData(null);
        return br;
    }
    //显示用户的搜索记录
    @RequestMapping("/showSearchRecord")
    public BaseResponse showSearchRecord(
            @RequestParam("uId") BigInteger uId
    )
    {
        List<String> searchList = uServiceL.selectSearchRecord(uId);
        if(!searchList.isEmpty())
        {
            br.setData(searchList);
            br.setResult(ResultCodeEnum.SUCCESS);
        }else
        {
            br.setData(null);
            br.setResult(ResultCodeEnum.FIND_ERROR);
        }
        return br;
    }
    //店铺的筛选
    @RequestMapping("/selectStoreByCondition")
    public BaseResponse selectStoreByCondition(
            @RequestParam("sColumn") Integer sColumn,
            @RequestParam("sType") Integer sType,
            @RequestParam("minSAera") Integer minSAera,
            @RequestParam("maxSAera") Integer maxSAera,
            @RequestParam("minSRentMoney") Integer minSRentMoney,
            @RequestParam("maxSRentMoney") Integer maxSRentMoney,
            @RequestParam("sPriceType") Integer sPriceType
    )
    {
        String sql = "select * from store where sStatus=1 AND sColumn="+sColumn+" AND sType="+sType;
        if(minSAera!=null)
        {
            sql+=" AND sAera>"+minSAera;
        }
        if(maxSAera!=null)
        {
            sql+=" AND sAera<"+maxSAera;
        }
        //根据其对应的column判断是哪个价格字段
        if(sPriceType==2)//价格区间
        {
            String priceColumn="";
            if(sColumn==1)//“店铺出租”租金
            {
                priceColumn="sRentMoney";
            }else if(sColumn==2)//“生意转让”转让费，租金
            {
                priceColumn="sTranMoney";
            }else if(sColumn==3)//“店铺出售”售价
            {
                priceColumn="sDeposit";
            }else if(sColumn==4)//“仓库出租”租金
            {
                priceColumn="sRentMoney";
            }
            if(minSRentMoney!=null)
            {
                sql+=" AND "+priceColumn+">"+minSRentMoney;
            }
            if(maxSRentMoney!=null)
            {
                sql+=" AND "+priceColumn+"<"+maxSRentMoney;
            }
        }else
        {
            sql+=" AND sPriceType="+sPriceType;
        }
        JSONArray searchStoreList = uServiceL.selectStoreByCondition(sql);
        if(!searchStoreList.isEmpty())
        {
            br.setData(searchStoreList);
            br.setResult(ResultCodeEnum.SUCCESS);
        }else
        {
            br.setData(null);
            br.setResult(ResultCodeEnum.FIND_ERROR);
        }
        return br;
    }

    //得到合同模板列表
    @RequestMapping("/showContractTemplate")
    public BaseResponse showContractTemplate()
    {
        List<String> contractList = new ArrayList<>();
        List<Contracttemplate> contracttemplates = Contracttemplate.dao.find("select * from contracttemplate");
        for(Contracttemplate contracttemplate:contracttemplates)
        {
            contractList.add(contracttemplate.getCtContent());
        }
        if(contractList.isEmpty())
        {
            br.setData(null);
            br.setResult(ResultCodeEnum.FIND_ERROR);
        }else
        {
            br.setData(contractList);
            br.setResult(ResultCodeEnum.SUCCESS);
        }
        return br;
    }
    @RequestMapping("/deleteUserSearchRecord")
    public BaseResponse deleteUserSearchRecord(
            @RequestParam("uId") BigInteger uId
    )
    {
        Integer flag = Db.delete("delete from searchrecord where srowner=?",uId);
        if(flag>=0)
        {
            br.setResult(ResultCodeEnum.SUCCESS);
        }else
        {
            br.setResult(ResultCodeEnum.DELETE_ERROR);
        }
        br.setData(null);
        return br;
    }

    //得到某个城市的商圈
    @RequestMapping("/getCityDetail")
    public BaseResponse getCityDetail(
            @RequestParam("clCity") String clCity
    )
    {
        List<Cityloc> citylocList = uServiceL.getCityDetailByCity(clCity);
        if(!citylocList.isEmpty())
        {
            br.setData(citylocList);
            br.setResult(ResultCodeEnum.SUCCESS);
        }else
        {
            br.setData(null);
            br.setResult(ResultCodeEnum.DELETE_ERROR);
        }
        return br;
    }
    //根据城市商圈等筛选店铺
    @RequestMapping("/getStoreListByCityDetail")
    public BaseResponse getStoreListByCityDetail(
            @RequestParam("clId") BigInteger clId
    )
    {
        JSONArray storeList = uServiceL.getStoreListByCityDetail(clId);
        if(!storeList.isEmpty())
        {
            br.setData(storeList);
            br.setResult(ResultCodeEnum.SUCCESS);
        }else
        {
            br.setData(null);
            br.setResult(ResultCodeEnum.DELETE_ERROR);
        }
        return br;
    }
}
