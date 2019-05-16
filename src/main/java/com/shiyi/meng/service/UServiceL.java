package com.shiyi.meng.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Const;
import com.shiyi.meng.model.*;
import com.shiyi.meng.util.Constant;
import com.shiyi.meng.util.PaymentApi;
import com.shiyi.meng.util.PaymentKit;
import com.shiyi.meng.util.WXPayUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UServiceL {

    //封装列表中显示的店铺
    private JSONObject packStore(Store store)
    {
        JSONObject packedStore = new JSONObject();
        /*
        * 1“店铺出租”租金/2“生意转让”转让费，租金/3“店铺出售”售价/4“仓库出租”租金
        * */
        packedStore.put("sId",store.getSId());//店铺Id
        packedStore.put("uId",store.getSUId());//店铺所有人
        String storePhoto = store.getSPhoto();
        String[] sPhoto = storePhoto.split("###");
        packedStore.put("sPhoto",sPhoto[0]);//店铺的一张图片
        packedStore.put("sName",store.getSName());//店铺名称
        packedStore.put("sArea",store.getSAera());//店铺面积
        packedStore.put("sColumn",store.getSColumn());//店铺栏目
        packedStore.put("sLoc",store.getSLoc());//位置
        packedStore.put("sStatus",store.getSStatus());//店铺状态
        packedStore.put("sRefuseReason",store.getSRefuseReason());//拒绝原因
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


    //通过openid得到user
    public User selectUserByOpenId(String openId)
    {
        return User.dao.findFirst("select * from user where uCOpenId=?",openId);
    }

    //得到首页的店铺列表
    public JSONArray getStoreListHomePage(String uCity,Integer pageIndex,Integer pageSize)
    {
        JSONArray showStoreList = new JSONArray();
        List<Store> storeList = Store.dao.find("select * from store " +
                "where sStatus=1 AND sCity=? " +
                "order by sModifyTime " +
                "limit "+pageSize+" offset "+(pageIndex-1)*pageSize,uCity);//店铺状态sStatus为1（审核通过）
        //店铺的一张图片，店铺名称，店铺类型，位置，每月租金
        for(Store store:storeList)
        {
            showStoreList.add(packStore(store));
        }
        return showStoreList;
    }
    //得到店铺详情
    public JSONObject getStoreDetail(BigInteger sId,BigInteger uId)
    {
        JSONObject storeDetail =  new JSONObject();
        Store store = Store.dao.findFirst("select * from store where sId=?",sId);
        storeDetail.put("storeDetail",store);
        String uWeiXinIcon = User.dao.findById(store.getSUId()).getUWeiXinIcon();//店铺所有者微信头像
        storeDetail.put("uWeiXinIcon",uWeiXinIcon);
        if(!uId.equals("0"))
        {
            //是否关注该店铺
            Followstore followstore = Followstore.dao.findFirst("select * from followstore " +
                    "where fsStore=? AND fsUser=?",sId,uId);
            if(followstore==null)
            {
                storeDetail.put("isFollow",0);//没有关注该店铺
            }else
            {
                storeDetail.put("isFollow",1);//关注了该店铺
            }

            //是否举报过该店铺
            Abnormalstore abnormalstore = Abnormalstore.dao.findFirst("select * from abnormalstore " +
                    "where asStore=? AND asUser=?",sId,uId);
            if(abnormalstore==null)
            {
                storeDetail.put("isAbnormal",0);//改用户没有举报过该店铺
            }else
            {
                storeDetail.put("isAbnormal",1);//改用户举报过该店铺
            }
        }
        return storeDetail;
    }

    //得到用户的店铺列表
    public JSONArray getUserStoreList(BigInteger uId)
    {
        JSONArray userStoreList =  new JSONArray();
        List<Store> storeList = Store.dao.find("select * from store where sUId=?",uId);
        for(Store store:storeList)
        {
            userStoreList.add(packStore(store));
        }
        return userStoreList;
    }

    //查看用户关注的店铺列表
    public JSONArray getFollowStoreList(BigInteger uId)
    {
        JSONArray followStoreList =  new JSONArray();
        List<Followstore> followstoreList = Followstore.dao.find("select * from followstore " +
                "where fsUser=?",uId);
        for(Followstore followstore:followstoreList)
        {
            Store store = Store.dao.findById(followstore.getFsStore());//得到用户关注的店铺信息
            followStoreList.add(packStore(store));
        }
        return followStoreList;
    }

    //查看用户签约的店铺列表-上交押金
    public JSONArray getSignStoreListByMoney(BigInteger uId)
    {
        JSONArray signStoreList =  new JSONArray();
        List<Signstore> signstoreList = Signstore.dao.find("select * from signstore " +
                "where ssUser=? AND ssIsMoney=1",uId);
        for(Signstore signstore:signstoreList)
        {
            Store store = Store.dao.findById(signstore.getSsStore());//得到用户关注的店铺信息
            packStore(store).put("ssId",signstore.getSsId());//得到签约的id，以便之后状态的改变
            packStore(store).put("ssStatus",signstore.getSsStatus());//签约的状态
            signStoreList.add(packStore(store));
        }
        return signStoreList;
    }

    //查看用户签约的店铺列表-上传合同
    public JSONArray getSignStoreListByContract(BigInteger uId)
    {
        JSONArray signStoreList =  new JSONArray();
        List<Signstore> signstoreList = Signstore.dao.find("select * from signstore " +
                "where ssUser=? AND ssIsContract=1",uId);
        for(Signstore signstore:signstoreList)
        {
            Store store = Store.dao.findById(signstore.getSsStore());//得到用户关注的店铺信息
            packStore(store).put("signStoreId",signstore.getSsId());//得到签约的id，以便之后状态的改变
            signStoreList.add(packStore(store));
        }
        return signStoreList;
    }
    //根据店铺名称，搜索店铺
    public JSONArray searchStoreByName(String sContent)
    {
        JSONArray searchStoreList = new JSONArray();
        List<Store> storeList = Store.dao.find("select * from store " +
                "where sStatus=1 AND sName like '%"+sContent+"%'");
        for(Store store:storeList)
        {
            searchStoreList.add(packStore(store));
        }
        return searchStoreList;
    }
    //显示用户搜索记录
    public List<String> selectSearchRecord(BigInteger uId)
    {
        List<String> searchContent = new ArrayList<>();
        List<Searchrecord> searchrecordList = Searchrecord.dao.find("select * from searchrecord " +
                "where srOwner=? limit 10",uId);//只显示最新10条
        for(Searchrecord searchrecord:searchrecordList)
        {
            searchContent.add(searchrecord.getSrContent());
        }
        return searchContent;
    }
    //店铺的筛选
    public JSONArray selectStoreByCondition(String sql)
    {
        JSONArray selectStoreList = new JSONArray();
        List<Store> storeList = Store.dao.find(sql);
        for(Store store:storeList)
        {
            selectStoreList.add(packStore(store));
        }
        return selectStoreList;
    }
    //根据城市找到对应的商圈景点高校数据
    public List<Cityloc> getCityDetailByCity(String city)
    {
        return Cityloc.dao.find("select * from cityloc where clCity=?",city);
    }
    //根据城市商圈等筛选店铺
    public JSONArray getStoreListByCityDetail(BigInteger clId)
    {
        JSONArray finalStoreList = new JSONArray();
        List<Store> storeList = new ArrayList<>();
        storeList.addAll(Store.dao.find("select * from store where sSpot=?",clId));
        storeList.addAll(Store.dao.find("select * from store where sBusiness=?",clId));
        storeList.addAll(Store.dao.find("select * from store where sSchool=?",clId));
        for(Store store:storeList)
        {
            finalStoreList.add(packStore(store));
        }
        return finalStoreList;
    }
    //得到平台所有提示信息
    public JSONArray findAllHint()
    {
        JSONArray allHint = new JSONArray();
        List<Hint> hintList = Hint.dao.find("select * from hint");
        for(Hint hint:hintList)
        {
            JSONObject showHint = new JSONObject();
            showHint.put("hLoc",hint.getHLoc());
            showHint.put("hContent",hint.getHContent());
            allHint.add(showHint);
        }
        return allHint;
    }
    //显示某个用户交易记录
    public JSONArray findAllBill(BigInteger uId)
    {
        JSONArray allBill = new JSONArray();
        List<Transfermoney> transfermoneyList = Transfermoney.dao.find("select * from transfermoney " +
                "where tmFrom=?",uId);
        transfermoneyList.addAll(Transfermoney.dao.find("select * from transfermoney where tmTo=?",uId));
        for(Transfermoney tm:transfermoneyList)
        {
            JSONObject showBill = new JSONObject();
            if(tm.getTmFrom().equals(new BigInteger("0")))
            {
                showBill.put("tmFrom","餐饮小程序平台");
            }
            if(tm.getTmFrom().equals(new BigInteger("0")))
            {
                showBill.put("tmTo","餐饮小程序平台");
            }
            showBill.put("tmStore",Store.dao.findById(tm.getTmStore()).getSName());
            showBill.put("tmMoney",tm.getTmMoney());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            showBill.put("tmCreateTime",sdf.format(tm.getTmCreateTime()));
            allBill.add(showBill);
        }
        return allBill;
    }

    /**
     * 功能描述:<调用统一下单的接口>
     **/
    public Map<String, String> unifiedOrder(String outTradeNo, BigDecimal money, String openid) throws Exception {
        Map<String, String> reqParams = new HashMap<>();
        //微信分配的小程序ID
        reqParams.put("appid", Constant.APPID);
        //微信支付分配的商户号
        reqParams.put("mch_id", Constant.MCH_ID);
        //随机字符串
        reqParams.put("nonce_str", System.currentTimeMillis() / 1000 + "");
        //签名类型
        reqParams.put("sign_type", "MD5");
        //充值订单 商品描述
        reqParams.put("body", "萌系餐饮人-押金支付订单-微信小程序");
        //商户订单号
        reqParams.put("out_trade_no", outTradeNo);
        //订单总金额，单位为分
        reqParams.put("total_fee", money.multiply(BigDecimal.valueOf(100)).intValue() + "");
        //终端IP
        reqParams.put("spbill_create_ip", "127.0.0.1");
        //通知地址
        reqParams.put("notify_url", Constant.NOTIFY_URL);
        //交易类型
        reqParams.put("trade_type", "JSAPI");
        //用户标识
        reqParams.put("openid", openid);
        //签名
        String sign = WXPayUtil.generateSignature(reqParams, Constant.KEY);
        reqParams.put("sign", sign);
        /*
        调用支付定义下单API,返回预付单信息 prepay_id
         */
        String xmlResult = PaymentApi.pushOrder(reqParams);
        Map<String, String> result = PaymentKit.xmlToMap(xmlResult);
        System.out.println(xmlResult);
        //预付单信息
        String prepay_id = result.get("prepay_id");

        /*
        小程序调起支付数据签名
         */
        Map<String, String> packageParams = new HashMap<String, String>();
        packageParams.put("appId", Constant.APPID);
        packageParams.put("timeStamp", System.currentTimeMillis() / 1000 + "");
        packageParams.put("nonceStr", System.currentTimeMillis() + "");
        packageParams.put("package", "prepay_id=" + prepay_id);
        packageParams.put("signType", "MD5");
        String packageSign = WXPayUtil.generateSignature(packageParams, Constant.KEY);
        packageParams.put("paySign", packageSign);
        return packageParams;


    }
    /**
     * 功能描述: <调用企业支付到零钱的接口>
     **/
    public Map<String, String> mToPOrder(String out_trade_no, BigDecimal money, String openid) throws Exception {
        Map<String, String> reqParams = new HashMap<>();
        //微信分配的小程序ID
        reqParams.put("appid", Constant.APPID);
        //微信支付分配的商户号
        reqParams.put("mch_id", Constant.MCH_ID);
        //随机字符串
        reqParams.put("nonce_str", System.currentTimeMillis() / 1000 + "");
        //签名类型
        reqParams.put("sign_type", "MD5");
        //充值订单 商品描述
        reqParams.put("body", "萌系餐饮人-押金支付订单-微信小程序");
        //商户订单号，需保持唯一性(只能是字母或者数字，不能包含有其他字符)
        reqParams.put("out_trade_no", out_trade_no);
        //用户openid，oxTWIuGaIt6gTKsQRLau2M0yL16E，商户appid下，某用户的openid
        reqParams.put("openid", openid);
        //校验用户姓名选项 	NO_CHECK：不校验真实姓名/FORCE_CHECK：强校验真实姓名
        reqParams.put("check_name", "NO_CHECK");

//        收款用户姓名 	re_user_name 	否 	王小王 	String(64) 	收款用户真实姓名。
//        如果check_name设置为FORCE_CHECK，则必填用户真实姓名

        //订单总金额，单位为分
        reqParams.put("amount", money.multiply(BigDecimal.valueOf(100)).intValue() + "");//都是整数，以分为单位
        //终端IP
        reqParams.put("spbill_create_ip", "127.0.0.1");
        //企业付款备注,企业付款备注，必填。
        reqParams.put("desc", "萌系餐饮小程序交易成功押金返还");
        //签名
        String sign = WXPayUtil.generateSignature(reqParams, Constant.KEY);
        reqParams.put("sign", sign);
        /*
        调用支付定义下单API,返回预付单信息 prepay_id
         */
        String xmlResult = PaymentApi.pushOrder(reqParams);
        Map<String, String> result = PaymentKit.xmlToMap(xmlResult);
        System.out.println(xmlResult);
        //todo 将付款信息存入数据库
        //预付单信息
        String prepay_id = result.get("prepay_id");

        /*
        小程序调起支付数据签名
         */
        Map<String, String> packageParams = new HashMap<String, String>();
        packageParams.put("appId", Constant.APPID);
        packageParams.put("timeStamp", System.currentTimeMillis() / 1000 + "");
        packageParams.put("nonceStr", System.currentTimeMillis() + "");
        packageParams.put("package", "prepay_id=" + prepay_id);
        packageParams.put("signType", "MD5");
        String packageSign = WXPayUtil.generateSignature(packageParams, Constant.KEY);
        packageParams.put("paySign", packageSign);
        return packageParams;
    }





}
