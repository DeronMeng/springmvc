package com.qb.china.util;

import com.alibaba.fastjson.JSONObject;

/**
 * Json与JavaBean之间的转化工具类
 * Create by Long.Meng on 2016年9月19日.
 */
public class JSONUtils {
	
	/**
	 * 从一个JSON 对象字符格式中得到一个java对象 
	 */
	public static <T> T jsonToObj(String jsonString, Class<T> beanCalss) {
		JSONObject jsonObject = JSONObject.parseObject(jsonString);
		T bean = (T) JSONObject.toJavaObject(jsonObject, beanCalss);
        
        return bean;
	}
	
	/**将java对象转换成json字符串*/
	public static String beanToJson(Object bean) {
		net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(bean);
        
        return json.toString();
	}
}
