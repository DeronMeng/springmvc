package com.qb.china.wechat;

/**
 * 微信支付实体类
 * Created by Long.Meng on 2015-12-22.
 */
public class WxPay {
	
	private String orderId;
	
	private String totalFee;
	
	/**订单生成的机器 IP*/
	private String spbillCreateIp;
	
	private String notifyUrl;
	
	/**商品描述根据情况修改*/
	private String body;
	
	/**微信用户对一个公众号唯一*/
	private String openId;
	
	public String getOrderId() {
		return orderId;
	}
	
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	public String getTotalFee() {
		return totalFee;
	}
	
	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}
	
	public String getSpbillCreateIp() {
		return spbillCreateIp;
	}
	
	public void setSpbillCreateIp(String spbillCreateIp) {
		this.spbillCreateIp = spbillCreateIp;
	}
	
	public String getNotifyUrl() {
		return notifyUrl;
	}
	
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	
	public String getBody() {
		return body;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	public String getOpenId() {
		return openId;
	}
	
	public void setOpenId(String openId) {
		this.openId = openId;
	}
}
