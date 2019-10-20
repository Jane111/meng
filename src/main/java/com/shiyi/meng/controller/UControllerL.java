package com.shiyi.meng.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.shiyi.meng.model.*;
import com.shiyi.meng.service.UServiceL;
import com.shiyi.meng.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;
@CrossOrigin
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
            @RequestParam("sType") String stype,//店铺类型
            @RequestParam("sDes") String sDes,//店铺描述
            @RequestParam("sName") String sName,//店铺名称
            @RequestParam("sConName") String sConName,//店铺联系人姓名
            @RequestParam("sPhone") String sPhone,//店铺联系人方式
            @RequestParam("sTotalFloor") Integer sTotalFloor,//楼层
            @RequestParam("sPayMethod") String sPayMethod,//付款方式
            @RequestParam("sWareHouse") Integer sWareHouse,//仓库所在层
            @RequestParam("sAera") Float sAera,//面积
            @RequestParam("sLoc") String sLoc,//店铺位置
            @RequestParam("sLng") String sLng,//店铺经度
            @RequestParam("sLat") String sLat,//店铺纬度
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
            @RequestParam("sDeposit") Float sDeposit,//店铺售价
            @RequestParam("sFreeTime")String sfreeTime,//免租期
            @RequestParam("sStartTime")String sStartTime,//起租期
            @RequestParam("switchDeposit")Integer switchDeposit,//售价 1-面议，0-有具体数字存在另一个字段
            @RequestParam("switchTransferfee")Integer switchTransferfee,//转让费 1-面议，0-有具体数字存在另一个字段
            @RequestParam("switchRentfee")Integer switchRentfee//月租金 1-面议，0-有具体数字存在另一个字段
    )
    {
        Integer sType=new Integer(stype);
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
            double distance = getDistance(cityloc.getClLat(),cityloc.getClLng(),Float.parseFloat(sLat),Float.parseFloat(sLng));
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
        System.out.println(sLng);
        store.setSCity(sCity);
        store.setSUserWriteLoc(sUserWriteLoc);
        store.setSConnectType(sConnectType);
        store.setSConnectWay(sConnectWay);
        store.setSTranMoney(sTranMoney);
        store.setSwitchTransferfee(switchTransferfee);//转让费是否面议 1-面议，0-否
        store.setSLeftTime(sLeftTime);
        store.setSStatus(sStoreStatus);
        store.setSECharge(sECharge);
        store.setSWCharge(sWCharge);
        store.setSFee(sFee);
        store.setSTag(sTag);
        store.setSRentMoney(sRentMoney);//月租金
        store.setSwitchRentfee(switchRentfee);//月租金是否面议1-面议，0-否
        store.setSDeposit(sDeposit);
        store.setSwitchDeposit(switchDeposit);//售价是否面议 1-面议，0-否
        store.setSStatus(0);//店铺状态为"未审核"
        store.setsFreeTime(sfreeTime);
        store.setsStartTime(sStartTime);
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

    //刷新置顶
    @RequestMapping("/updateStoreRank")
    public BaseResponse updateStoreRank(
            @RequestParam("sId") BigInteger sId
    )
    {
        //得到sId对应的store
        Store pastStore = Store.dao.findById(sId);
        Date now = new Date();
        Date modifyTime=pastStore.getSModifyTime();
        Date beforFive=new Date(now.getTime() - 300000);
        if(modifyTime.before(beforFive)||pastStore.getSFlushTime().equals(new BigInteger("0")))
        {
            pastStore.setSFlushTime(pastStore.getSFlushTime().add(new BigInteger("1")));
            boolean flag = pastStore.update();//更新店铺修改时间
            if(flag)
            {
                br.setResult(ResultCodeEnum.SUCCESS);
            }
            else
            {
                br.setResult(ResultCodeEnum.UPDATE_ERROR);
            }
        }else
        {
            br.setResult(ResultCodeEnum.DO_NOT_IN_TIME);
        }
        br.setData(null);
        return br;
    }
    //首页的刷新置顶
    @RequestMapping("/updateHomeStoreRank")
    public BaseResponse updateHomeStoreRank(
            @RequestParam("sId") BigInteger sId,
            @RequestParam("uCity") String uCity,
            @RequestParam("pageIndex") Integer pageIndex,
            @RequestParam("pageSize") Integer pageSize
    )
    {
        //得到sId对应的store
        Store pastStore = Store.dao.findById(sId);
        Date now = new Date();
        Date modifyTime=pastStore.getSModifyTime();
        Date beforFive=new Date(now.getTime() - 300000);
        if(modifyTime.before(beforFive)||pastStore.getSFlushTime().equals(new BigInteger("0")))
        {
            pastStore.setSFlushTime(pastStore.getSFlushTime().add(new BigInteger("1")));
            boolean flag = pastStore.update();//更新店铺修改时间
            if(flag)
            {
                br.setResult(ResultCodeEnum.SUCCESS);
            }
            else
            {
                br.setResult(ResultCodeEnum.UPDATE_ERROR);
            }
        }else
        {
            br.setResult(ResultCodeEnum.DO_NOT_IN_TIME);
        }
        JSONArray array=uServiceL.getStoreListHomePage(uCity, pageIndex, pageSize);
        br.setData(array);
        return br;
    }

    //查看店铺详情
    @RequestMapping("/showStoreDetail")
    public BaseResponse showStoreDetail(
            @RequestParam("sId") BigInteger sId,
            @RequestParam("uId") BigInteger uId
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
            @RequestParam("asType") Integer asType,
            @RequestParam("formId") String formId
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
        abnormalstore.setAsStatus(0);//状态为未审核
        abnormalstore.setAsFormId(formId);//设置formId
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
        br.setData(followStoreList);
        br.setResult(ResultCodeEnum.SUCCESS);
        return br;
    }

    //查看用户签约的店铺列表-上交押金
    @RequestMapping("/showSignStoreListByMoney")
    public BaseResponse showSignStoreListByMoney(
            @RequestParam("uId") BigInteger uId
    )
    {
        JSONArray signStoreList = uServiceL.getSignStoreListByMoney(uId);
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

    //查看用户签约的店铺列表-上传合同
    @RequestMapping("/showSignStoreListByContract")
    public BaseResponse showSignStoreListByContract(
            @RequestParam("uId") BigInteger uId
    )
    {
        JSONArray signStoreList = uServiceL.getSignStoreListByContract(uId);
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
        String sql = null;
        if(sType==-1)
        {
            sql = "select * from store where sStatus=1 AND sColumn="+sColumn;
        }else
        {
            sql = "select * from store where sStatus=1 AND sColumn="+sColumn+" AND sType="+sType;
        }

        if(minSAera!=null)
        {
            sql+=" AND sAera>"+minSAera;
        }
        if(maxSAera!=null)
        {
            sql+=" AND sAera<"+maxSAera;
        }
        //根据其对应的column判断是哪个价格字段
        if(sPriceType!=-1)//sPriceType==-1表示用户没有对这个选项进行选择
        {
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
    //删除某个用户所有记录
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
            br.setResult(ResultCodeEnum.FIND_ERROR);
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
            br.setResult(ResultCodeEnum.FIND_ERROR);
        }
        return br;
    }

    //发布定制化找店信息
    @RequestMapping("/addFindStoreInfo")
    public BaseResponse addFindStoreInfo(
            @RequestParam("fdUser") BigInteger fdUser,
            @RequestParam("fdCommand") String fdCommand,
            @RequestParam("fdPhone") String fdPhone,
            @RequestParam("fdName") String fdName
    )
    {
        Findstore findstore = new Findstore();
        findstore.setFdUser(fdUser);
        findstore.setFdCommand(fdCommand);
        findstore.setFdPhone(fdPhone);
        findstore.setFdName(fdName);
        findstore.setFdStatus(0);//初始发布，设置为“0-未完成”状态
        boolean flag = findstore.save();
        if(flag)
        {
            br.setResult(ResultCodeEnum.SUCCESS);//添加成功
        }else
        {
            br.setResult(ResultCodeEnum.ADD_ERROR);//添加失败
        }
        br.setData(null);
        return br;
    }

    //上传合同照片
//    @RequestMapping("/addUserContract")
//    public BaseResponse addUserContract(
//            @RequestParam("ucOwner") BigInteger ucOwner,
//            @RequestParam("ucContent") String ucContent,
//            @RequestParam("ucStore") BigInteger ucStore
//    )
//    {
//        Usercontract usercontract = new Usercontract();
//        usercontract.setUcOwner(ucOwner);
//        usercontract.setUcContent(ucContent);
//        usercontract.setUcStore(ucStore);
//        boolean flag = usercontract.save();//将该用户合同加入数据库
//
//        /*修改店铺状态*/
//        Store store = Store.dao.findById(ucStore);
//        store.setSStatus(4);//该店铺在交易过程中
//        store.update();
//
//        /*将该店铺设为用户签约过的店铺*/
//        Signstore signstore = new Signstore();
//        signstore.setSsUser(ucOwner);
//        signstore.setSsStore(ucStore);
//        signstore.setSsIsContract(1);//该签约店铺上传了合同
//        signstore.setSsStatus(6);//设置为正在交易的状态
//        signstore.save();
//
//        if(flag)
//        {
//            br.setResult(ResultCodeEnum.SUCCESS);//添加成功
//        }else
//        {
//            br.setResult(ResultCodeEnum.ADD_ERROR);//添加失败
//        }
//        br.setData(null);
//        return br;
//    }

    //得到用户上交押金总数
    @RequestMapping("/getTotalDeposit")
    public BaseResponse getTotalDeposit(
            @RequestParam("tmFrom") BigInteger tmFrom
    )
    {
        //得到平台转账表
        List<Transfermoney> transfermonies = Transfermoney.dao.find("select * from transfermoney " +
                "where tmfrom=?",tmFrom);
        if(transfermonies.isEmpty())
        {
            br.setData(0);
        }else
        {
            Float totalDesposit = 0f;
            for(Transfermoney tr:transfermonies)
            {
                totalDesposit+=tr.getTmMoney();
            }
            br.setData(totalDesposit);
        }
        br.setResult(ResultCodeEnum.SUCCESS);
        return br;
    }

    //提交终止交易相关信息
    @RequestMapping("/addStopDealInfo")
    public BaseResponse addStopDealInfo(
            @RequestParam("sdUser") BigInteger sdUser,
            @RequestParam("sdStore") BigInteger sdStore,
            @RequestParam("sdProblem") String sdProblem,
            @RequestParam("sdPhoto") String sdPhoto,
            @RequestParam("sdApplyName") String sdApplyName,
            @RequestParam("sdApplyNum") String sdApplyNum,
            @RequestParam("sdApplyPhone") String sdApplyPhone,
            @RequestParam("formId") String formId
    )
    {
        Stopdeal stopdeal = new Stopdeal();
        stopdeal.setSdUser(sdUser);
        stopdeal.setSdStore(sdStore);
        stopdeal.setSdProblem(sdProblem);
        stopdeal.setSdPhoto(sdPhoto);
        stopdeal.setSdApplyName(sdApplyName);
        stopdeal.setSdApplyNum(sdApplyNum);
        stopdeal.setSdApplyPhone(sdApplyPhone);
        stopdeal.setSdFormId(formId);
        boolean flag = stopdeal.save();
        JSONObject returnData = new JSONObject();
        returnData.put("sdId",stopdeal.getSdId());
        if(flag)
        {
            br.setResult(ResultCodeEnum.SUCCESS);
            br.setData(returnData);
        }else
        {
            br.setResult(ResultCodeEnum.ADD_ERROR);
            br.setData(null);
        }
        return br;
    }
    //显示所有提示信息
    @RequestMapping("/showAllHint")
    public BaseResponse showAllHint()
    {
        JSONArray allHint = uServiceL.findAllHint();
        if(!allHint.isEmpty())
        {
            br.setData(allHint);
            br.setResult(ResultCodeEnum.SUCCESS);
        }else
        {
            br.setData(null);
            br.setResult(ResultCodeEnum.FIND_ERROR);
        }
        return br;
    }
    //显示某个用户交易记录
    @RequestMapping("/showUserBill")
    public BaseResponse showUserBill(
            @RequestParam("uId") BigInteger uId
    )
    {
        JSONArray allBill = uServiceL.findAllBill(uId);
        if(!allBill.isEmpty())
        {
            br.setData(allBill);
            br.setResult(ResultCodeEnum.SUCCESS);
        }else
        {
            br.setData(null);
            br.setResult(ResultCodeEnum.FIND_ERROR);
        }
        return br;
    }


    @RequestMapping("/createOrder")
    public BaseResponse createOrder(
            @RequestParam("uId") BigInteger uId,
            @RequestParam("money") BigDecimal money
    ){

        //调用接口获取openId
        String openId = User.dao.findById(uId).getUCOpenId();
        Map<String, String> reqParams = new HashMap<>();
        //订单号  uuid
        //封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
        String outTradeNo= UUID.randomUUID().toString().replaceAll("-", "");
        Map<String, String> result = new HashMap<>();
        try {
            result = uServiceL.unifiedOrder(outTradeNo,money,openId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        br.setData(result);
        br.setResult(ResultCodeEnum.SUCCESS);
        return br;
    }

    //记录用户上交押金的记录
    @RequestMapping("/addPayOrder")
    public BaseResponse addPayOrder(
            @RequestParam("uId") BigInteger uId,
            @RequestParam("sId") BigInteger sId,
            @RequestParam("money") Float money,
            @RequestParam("ssUserPhone") String ssUserPhone,
            @RequestParam("preypay_id") String preypay_id
    ){
        Transfermoney transfermoney = new Transfermoney();
        transfermoney.setTmFrom(uId);//tmfrom 为uId
        transfermoney.setTmTo(new BigInteger("0"));//tmto 为0，表示平台
        transfermoney.setTmStore(sId);
        transfermoney.setTmMoney(money);
        boolean flag = transfermoney.save();

        /*修改店铺状态*/
        Store store = Store.dao.findById(sId);
        store.setSStatus(4);//该店铺在交易过程中
        store.update();

        /*将该店铺设为用户签约过的店铺*/
        Signstore signstore = new Signstore();
        signstore.setSsUser(uId);
        signstore.setSsStore(sId);
        signstore.setSsIsMoney(1);//该签约店铺上交押金
        signstore.setSsStatus(6);//设置为正在交易的状态
        signstore.setSsUserPhone(ssUserPhone);//购买人联系方式
        signstore.setSsPrepayId(preypay_id);//存储对应的prepay_id
        signstore.save();

        //发送对应的消息模板
        uServiceL.payDepositTemplate(uId,money,preypay_id);

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

    //签订合同
    @RequestMapping("/addUserContract")
    public BaseResponse addUserContract(
            @RequestParam("uId") BigInteger uId,
            @RequestParam("sId") BigInteger sId,
            @RequestParam("ucContractUrl") String ucContractUrl,//合同图片
            @RequestParam("ucBusinessUrl") String ucBusinessUrl,//营业执照
            @RequestParam("ucIdUrl") String ucIdUrl//身份证号
    ){
        //存储合同
        Usercontract usercontract = new Usercontract();
        usercontract.setUcStore(sId);
        usercontract.setUcOwner(uId);
        usercontract.setUcContractUrl(ucContractUrl);
        usercontract.setUcBusinessUrl(ucBusinessUrl);
        usercontract.setUcIdUrl(ucIdUrl);
        usercontract.setUcStatus(0);//未审核状态
        boolean flag = usercontract.save();

        /*修改店铺状态*/
        Store store = Store.dao.findById(sId);
        store.setSStatus(4);//该店铺在交易过程中
        store.update();

        /*将该店铺设为用户签约过的店铺*/
        Signstore signstore = new Signstore();
        signstore.setSsUser(uId);
        signstore.setSsStore(sId);
        signstore.setSsIsContract(1);//该签约店铺签订合同
        signstore.setSsStatus(6);//设置为正在交易的状态
        signstore.save();

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
    //提交返款申请
    @RequestMapping("/addReturnApply")
    public BaseResponse addReturnApply(
            @RequestParam("ssId") BigInteger ssId
    )
    {
        Signstore signstore = new Signstore();
        signstore.setSsId(ssId);
        signstore.setSsStatus(0);//交易成功提交返款
        boolean flag = signstore.update();
        if(flag)
        {
            br.setResult(ResultCodeEnum.SUCCESS);//修改状态成功
        }else
        {
            br.setResult(ResultCodeEnum.UPDATE_ERROR);//修改状态失败
        }
        br.setData(null);
        return br;
    }
    //提交退款申请
    @RequestMapping("/addTuiApply")
    public BaseResponse addTuiApply(
            @RequestParam("ssId") BigInteger ssId,
            @RequestParam("sdId") BigInteger sdId
    )
    {
        boolean flag = uServiceL.addTuiApply(ssId,sdId);
        if(flag)
        {
            br.setResult(ResultCodeEnum.SUCCESS);//修改状态成功
        }else
        {
            br.setResult(ResultCodeEnum.UPDATE_ERROR);//修改状态失败
        }
        br.setData(null);
        return br;
    }

    /**
     * 功能描述: <小程序回调>
     * @return:
     * @auther: majker
     * @date: 2019/3/10
     **/
    @RequestMapping("/wxProPayNotify")
    public void wxProPayNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //进入微信小程序支付回调
        String xmlMsg = HttpKit.readData(request);
        System.out.println("微信小程序通知信息" + xmlMsg);
        Map<String, String> resultMap = PaymentKit.xmlToMap(xmlMsg);
        if (resultMap.get("RETURN_CODE").equals("SUCCESS")) {
            String orderNo = resultMap.get("out_trade_no");
            System.out.println("微信小程序支付成功,订单号" + orderNo);
            /**
             *   通过订单号 修改数据库中的记录，此处省略n行代码
             */
        }
        String result = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
        try {
            response.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //查看附近几千米的店铺
    @RequestMapping("/showNearStoreList")
    public BaseResponse showNearStoreList(
            @RequestParam("longitude") double longitude,
            @RequestParam("latitude") double latitude,
            @RequestParam("distance") int distance
    )
    {
        JSONArray storeList = uServiceL.findNearStoreList(latitude,longitude,distance);
        if(storeList.isEmpty())
        {
            br.setResult(ResultCodeEnum.FIND_FAILURE);
        }else
        {
            br.setResult(ResultCodeEnum.SUCCESS);
            br.setData(storeList);
        }
        return br;
    }
    //发送模板消息
    @RequestMapping("/sendTemplageMessage")
    public BaseResponse sendTemplageMessage(String touser,String template_id,String form_id) throws Exception {
        String keyword1="11111";
        String keyword2="22222";
        String keyword3="33333";
        String keyword4="44444";
        String keyword5="55555";
        String access_token = Accesscode.dao.findFirst("select acCode from accesscode ORDER BY acCreateTime DESC").getAcCode();
        String reqParams="{\"access_token\":\""+access_token+"\",\"touser\":\""+touser+"\",\"template_id\":\""+template_id+"\",\"form_id\":\""+form_id+"\"," +
                "\"data\":{\"keyword1\":{\"value\":\""+keyword1+"\"},\"keyword2\":{\"value\":\""+keyword2+"\"}," +
                "\"keyword3\":{\"value\":\""+keyword3+"\"},\"keyword4\":{\"value\":\""+keyword4+"\"}," +
                "\"keyword5\":{\"value\":\""+keyword5+"\"}}}";
        System.out.println(reqParams);
        /*
        调用发送模板消息API
         */
        String xmlResult = PaymentApi.templateMessage(access_token,reqParams);

        System.out.println(xmlResult);
        br.setData(xmlResult);
        return br;
    }

    /*
    *
    * 模板消息
    * */
    //申请入驻
    @RequestMapping("/applyIn")
    public BaseResponse applyIn(
            @RequestParam("keyword1") String keyword1,
            @RequestParam("touser") String touser,
            @RequestParam("form_id") String form_id)
    {
        String keyword2="等待审核";
        String keyword3="1-3日完成审核";
        String template_id="s4fvfyZ2vG5uMuEsfmlri8sYLzXE7ZLs1LaEpacGHt4";//入驻申请通知
        String access_token = Accesscode.dao.findFirst("select acCode from accesscode ORDER BY acCreateTime DESC").getAcCode();
        String reqParams="{\"access_token\":\""+access_token+"\",\"touser\":\""+touser+"\",\"template_id\":\""+template_id+"\",\"form_id\":\""+form_id+"\"," +
                "\"data\":{\"keyword1\":{\"value\":\""+keyword1+"\"},\"keyword2\":{\"value\":\""+keyword2+"\"}," +
                "\"keyword3\":{\"value\":\""+keyword3+"\"}}}";
        System.out.println(reqParams);

        String xmlResult = PaymentApi.templateMessage(access_token,reqParams);
        System.out.println(xmlResult);
        br.setData(xmlResult);
        return br;
    }
    //置顶店铺成功模板消息
    @RequestMapping("/upStoreTemplate")
    public BaseResponse upStoreTemplate(
            @RequestParam("uId") BigInteger uId,
            @RequestParam("sId") BigInteger sId,
            @RequestParam("form_id") String form_id)
    {
        Store store = Store.dao.findById(sId);
        User user = User.dao.findById(uId);
        String keyword1="置顶成功";//置顶状态
        String keyword2=store.getSModifyTime()+"";//置顶时间
        String keyword3=store.getSName();//置顶内容
        String keyword4=user.getUWeiXinName();//置顶人
        String keyword5="您已经成功置顶您的店铺";//置顶详情
        String touser=user.getUCOpenId();
        String template_id="g1v_sueP6tYISez-8U8VtCJ-DdwpFU8DnPfinfC01vw";//置顶成功template
        String access_token = Accesscode.dao.findFirst("select acCode from accesscode ORDER BY acCreateTime DESC").getAcCode();
        String reqParams="{\"access_token\":\""+access_token+"\",\"touser\":\""+touser+"\",\"template_id\":\""+template_id+"\",\"form_id\":\""+form_id+"\"," +
                "\"data\":{\"keyword1\":{\"value\":\""+keyword1+"\"},\"keyword2\":{\"value\":\""+keyword2+"\"}," +
                "\"keyword3\":{\"value\":\""+keyword3+"\"},\"keyword4\":{\"value\":\""+keyword4+"\"}," +
                "\"keyword5\":{\"value\":\""+keyword5+"\"}}}";
        System.out.println(reqParams);
        String xmlResult = PaymentApi.templateMessage(access_token,reqParams);
        System.out.println(xmlResult);
        br.setData(xmlResult);
        return br;
    }
    //新客户访问提醒模板消息
    @RequestMapping("/newCoustmerStoreTemplate")
    public BaseResponse newCoustmerStoreTemplate(
            @RequestParam("uId") BigInteger uId,
            @RequestParam("sId") BigInteger sId,
            @RequestParam("form_id") String form_id)
    {
        Store store = Store.dao.findById(sId);
        User user = User.dao.findById(uId);//访问者
        String keyword1=store.getSName();//访问项目
        String keyword2=user.getUWeiXinName();//昵称

        Date t = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String keyword3=df.format(t);//访问时间

        String keyword4="请保持手机畅通，并时刻关注平台动态。";//温馨提示
        String touser=User.dao.findById(store.getSUId()).getUCOpenId();//通知店铺的主人
        String template_id="9uZ_e4H3rUpf0cDFE84w8kmMKYhbbXtI1l56X0itTJ0";//新客户访问提醒template
        String access_token = Accesscode.dao.findFirst("select acCode from accesscode ORDER BY acCreateTime DESC").getAcCode();
        String reqParams="{\"access_token\":\""+access_token+"\",\"touser\":\""+touser+"\",\"template_id\":\""+template_id+"\",\"form_id\":\""+form_id+"\"," +
                "\"data\":{\"keyword1\":{\"value\":\""+keyword1+"\"},\"keyword2\":{\"value\":\""+keyword2+"\"}," +
                "\"keyword3\":{\"value\":\""+keyword3+"\"},\"keyword4\":{\"value\":\""+keyword4+"\"}}}";
        System.out.println(reqParams);
        String xmlResult = PaymentApi.templateMessage(access_token,reqParams);
        System.out.println(xmlResult);
        br.setData(xmlResult);
        return br;
    }
}
