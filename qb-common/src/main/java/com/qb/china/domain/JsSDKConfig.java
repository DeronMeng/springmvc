package com.qb.china.domain;

/** 
 * @ClassName:     JsSDKConfig.java 
 * @Description:   TODO(微信参数实体)
 * @author         ML  
 * @version        V2.0   
 * @Date           2015年10月28日 下午2:00:15   
 */
public class JsSDKConfig{
	
	public String                appId;
    public String                timestamp;
    public String                nonceStr;
    public String                signature;
    public static final String    JS_API    = "onMenuShareTimeline,onMenuShareAppMessage,onMenuShareQQ,onMenuShareWeibo";
    
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getNonceStr() {
		return nonceStr;
	}
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	public String[] getJsApiList() {
        return JS_API.split(",");
    }
	
	@Override
	public String toString() {
		return "appId：" + this.appId + "\n" + "timestamp：" + this.timestamp + "\n" + "nonceStr：" + this.nonceStr + "\n" + "signature：" + this.signature + "\n" + "JS_API：" + JS_API;
	}
 
}
