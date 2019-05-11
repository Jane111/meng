package com.shiyi.meng.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shiyi.meng.model.Abnormalstore;
import com.shiyi.meng.model.Store;
import org.springframework.stereotype.Service;

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



}
