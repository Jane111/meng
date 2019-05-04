package com.shiyi.meng.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shiyi.meng.model.Followstore;
import com.shiyi.meng.model.Signstore;
import com.shiyi.meng.model.Store;
import com.shiyi.meng.model.User;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class UServiceL {

    //封装列表中显示的店铺
    private JSONObject packStore(Store store)
    {
        JSONObject packedStore = new JSONObject();
        packedStore.put("sId",store.getSId());//店铺Id
        String storePhoto = store.getSPhoto();
        String[] sPhoto = storePhoto.split("###");
        packedStore.put("sPhoto",sPhoto[0]);//店铺的一张图片
        packedStore.put("sName",store.getSName());//店铺名称
        packedStore.put("sType",store.getSType());//店铺类型
        packedStore.put("sLoc",store.getSLoc());//位置
        packedStore.put("sRentMoney",store.getSRentMoney());//每月租金
        return packedStore;
    }


    //通过openid得到user
    public User selectUserByOpenId(String openId)
    {
        return User.dao.findFirst("select * from user where uCOpenId=?",openId);
    }

    //得到首页的店铺列表
    public JSONArray getStoreListHomePage()
    {
        JSONArray showStoreList = new JSONArray();
        List<Store> storeList = Store.dao.find("select from store order by sModifyTime");
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


}
