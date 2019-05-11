package com.shiyi.meng.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shiyi.meng.model.Cityloc;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class GetPOI {
    public static String key = "95e5b1d8ee4776345aef86f5a41c1edf";
    public static String url = "https://restapi.amap.com/v3/place/text?";
    public static String keywords="";
	public static String types="";
    public static String offset="1000";
    public static List<String> cityList = new ArrayList<String>();


    public static void intialCityList()
    {
        cityList.add("郑州");
        cityList.add("乌鲁木齐");
        cityList.add("杭州");
        cityList.add("银川");
        cityList.add("昆明");
        cityList.add("拉萨");
        cityList.add("西宁");
        cityList.add("兰州");
        cityList.add("上海");
        cityList.add("西安");
        cityList.add("重庆");
        cityList.add("贵阳");
        cityList.add("北京");
        cityList.add("海口");
        cityList.add("成都");
        cityList.add("南宁");
        cityList.add("济南");
        cityList.add("长沙");
        cityList.add("合肥");
        cityList.add("广州");
        cityList.add("武汉");
        cityList.add("南昌");
        cityList.add("福州");
        cityList.add("长春");
        cityList.add("南京");
        cityList.add("石家庄");
        cityList.add("哈尔滨");
        cityList.add("哈尔滨");
        cityList.add("沈阳");
        cityList.add("呼和浩特");
        cityList.add("太原");
    }
    public static void getSchool()
    {
        types="高等院校";
        for(String city:cityList)
        {
            String finalUrl = url+"key="+key+"&keywords="+keywords+"&types="+types+"&city="+city+"&offset="+offset;
            System.out.println(finalUrl);
            String get = HttpClientUtil.doGet(finalUrl);
            System.out.println(get);
            JSONObject jsonObject = JSONObject.parseObject(get);
            /*
            * 学校数据的解析
            * */
            JSONArray pois = jsonObject.getJSONArray("pois");
            for(int i=0;i<pois.size();i++)
            {
                JSONObject poi = JSONObject.parseObject(pois.get(i).toString());
//                String pattern = ".*?大学";
//                if(Pattern.matches(pattern, poi.getString("name")))
//                {
//                    System.out.println(poi.getString("name"));
//                }
                if(poi.getString("name").contains("大学")&!poi.getString("name").contains("院")&!poi.getString("name").contains("系")&!poi.getString("name").contains("楼")&!poi.getString("name").contains("站")&!poi.getString("name").contains("·")&!poi.getString("name").contains("处")&!poi.getString("name").contains("中学")&!poi.getString("name").contains("室")&!poi.getString("name").contains("协会")&!poi.getString("name").contains("中心"))
                {
                    System.out.println(poi.getString("name"));
                    String location = poi.getString("location");//112.588142,37.80037
                    String[] loc = location.split(",");
                    Cityloc cityloc = new Cityloc();
                    cityloc.setClType(1);//高校类型
                    cityloc.setClCity(city);
                    cityloc.setClName(poi.getString("name"));
                    cityloc.setClLat(Float.parseFloat(loc[1]));//纬度
                    cityloc.setClLng(Float.parseFloat(loc[0]));//经度
                    cityloc.save();
                }
            }
        }

    }
    public static void getSpot()
    {
        types="国家级景点";
        for(String city:cityList)
        {
            String finalUrl = url+"key="+key+"&keywords="+keywords+"&types="+types+"&city="+city+"&offset="+offset;
            System.out.println(finalUrl);
            String get = HttpClientUtil.doGet(finalUrl);
            System.out.println(get);
            JSONObject jsonObject = JSONObject.parseObject(get);
            JSONArray pois = jsonObject.getJSONArray("pois");

            for(int i=0;i<pois.size();i++)
            {
                JSONObject poi = JSONObject.parseObject(pois.get(i).toString());
                if(!poi.getString("name").contains("(")&!poi.getString("name").contains("滑雪场")&!poi.getString("name").contains("酒庄")&!poi.getString("name").contains("办公楼"))
                {
                    System.out.println(poi.getString("name"));
                    String location = poi.getString("location");//112.588142,37.80037
                    String[] loc = location.split(",");
                    Cityloc cityloc = new Cityloc();
                    cityloc.setClType(0);//景区类型
                    cityloc.setClCity(city);
                    cityloc.setClName(poi.getString("name"));
                    cityloc.setClLat(Float.parseFloat(loc[1]));//纬度
                    cityloc.setClLng(Float.parseFloat(loc[0]));//经度
                    cityloc.save();
                }
            }

        }

    }
    public static void getBusiness()
    {
        types="购物中心";
        for(String city:cityList)
        {
            String finalUrl = url+"key="+key+"&keywords="+keywords+"&types="+types+"&city="+city+"&offset="+offset;
            System.out.println(finalUrl);
            String get = HttpClientUtil.doGet(finalUrl);
            System.out.println(get);
            JSONObject jsonObject = JSONObject.parseObject(get);
            JSONArray pois = jsonObject.getJSONArray("pois");

            for(int i=0;i<pois.size();i++)
            {
                JSONObject poi = JSONObject.parseObject(pois.get(i).toString());
                if(!poi.getString("name").contains("(")&!poi.getString("name").contains("·"))
                {
                    System.out.println(poi.getString("name"));
                    String location = poi.getString("location");//112.588142,37.80037
                    String[] loc = location.split(",");
                    Cityloc cityloc = new Cityloc();
                    cityloc.setClType(2);//商圈类型
                    cityloc.setClCity(city);
                    cityloc.setClName(poi.getString("name"));
                    cityloc.setClLat(Float.parseFloat(loc[1]));//纬度
                    cityloc.setClLng(Float.parseFloat(loc[0]));//经度
                    cityloc.save();
                }
            }

        }

    }
    public static void main()
    {
        intialCityList();
//        getSchool();
//        getSpot();
        getBusiness();
    }



}
