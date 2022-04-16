package com.kola.otdr.util;

import java.util.ArrayList;
import java.util.List;


import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * json 工具类
 * @author fusionGroup
 */
public class JsonUtils {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private final static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    private final static Gson dishtmlgson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").disableHtmlEscaping().create();

    public static Gson getInstance(){
        return gson;
    }

    /***
     * List 转为 JSON
     * @param list
     * @return
     */
    public static <T> String toJson(List<T> list) {
        if(null != list && list.size() > 0){
            return gson.toJson(list);
        }
        return "";
    }

    /***
     * JSON 转换为 List
     * @param jsonStr
     *         [{"age":12,"createTime":null,"id":"","name":"wxw","registerTime":null,"sex":1},{...}]
     * @param cls
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> toList(String jsonStr, Class<T> cls){
        List<T> list = new ArrayList<T>();
        if (jsonStr!=null && !jsonStr.equals("")) {
            try {
                Gson gson = new Gson();
                JsonArray arry = new JsonParser().parse(jsonStr).getAsJsonArray();
                for (JsonElement jsonElement : arry) {
                    list.add(gson.fromJson(jsonElement, cls));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return list;
    }


    /***
     * Object 转为  JSON
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        if(null != object){
            return gson.toJson(object);
        }
        return "";
    }
    /***
     * Object 转为  JSON
     * @param object
     * @return
     */
    public static String toJson(Object object,boolean dishtml) {
        if(null != object){
            if(dishtml){
                return dishtmlgson.toJson(object);
            }
            return gson.toJson(object);
        }
        return "";
    }

    /***
     *
     * JSON 转 Object
     *
     * @param jsonStr
     *         [{"age":12,"createTime":null,"id":"","name":"wxw","registerTime":null,"sex":1}]
     * @param objectClass
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T toObject(String jsonStr,  Class<T> objectClass){
        if(null != jsonStr){
            T object = gson.fromJson(jsonStr, objectClass);
            return object;
        }
        return null;
    }

    /**
     * 将json 串转化为list<Map>
     * @param jsonStr
     * @param <T>  Map集合 LinkedTreeMap
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> toListKeyMap(String jsonStr){
        if (jsonStr!=null && !jsonStr.equals("")) {
            List<T> list = gson.fromJson(jsonStr, new TypeToken<List<T>>(){}.getType());
            return list;
        }
        return null;
    }
}
