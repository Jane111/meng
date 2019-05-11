package com.shiyi.meng.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shiyi.meng.model.*;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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

    //查看用户签约的店铺列表
    public JSONArray getSignStoreList(BigInteger uId)
    {
        JSONArray signStoreList =  new JSONArray();
        List<Signstore> signstoreList = Signstore.dao.find("select * from signstore " +
                "where ssUser=?",uId);
        for(Signstore signstore:signstoreList)
        {
            Store store = Store.dao.findById(signstore.getSsStore());//得到用户关注的店铺信息
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



}
