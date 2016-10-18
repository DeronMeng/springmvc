package com.qb.china.wechat;

import java.util.HashMap;

import org.springframework.stereotype.Component;


/**
 * Create by Long.Meng 2015-10-29.
 */
@Component
public class WxCache extends HashMap<String,Object>{
	private static final long serialVersionUID = 1L;
	
	private static WxCache cacheMap;
	 
	public static WxCache getInstance() {
		if (cacheMap == null) {
			cacheMap = new WxCache();
		}
		return cacheMap;
	}
	
	public void setCacheMap(String key, Object obj){
		cacheMap.put(key, obj);
	}
	
	public Object getCacheMap(String key){
		return cacheMap.get(key);
	}
	
	public boolean existsKey(String key){
		if(cacheMap.getCacheMap(key) == null){
			return false;
		}
		return true;
	}
	
	public void removeCacheMap(String key){
		cacheMap.remove(key);
	}
	
	public void clearCacheMap(){
		cacheMap.clear();
	}

}
