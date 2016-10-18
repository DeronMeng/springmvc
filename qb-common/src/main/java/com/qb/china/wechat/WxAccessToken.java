package com.qb.china.wechat;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by Long.Meng on 2015-10-29.
 */
public class WxAccessToken {

	@JSONField(name = "access_token")
    private String accessToken;

    /**有效时间*/
	@JSONField(name = "expires_in")
    private long expiresIn;

    /**返回错误码*/
	@JSONField(name = "errcode")
    private String errCode;

    /**错误消息*/
	@JSONField(name = "errmsg")
    private String errMsg;
    
    /**有效具体日期*/
    private Date expiresDate;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

	public Date getExpiresDate() {
		return expiresDate;
	}

	public void setExpiresDate(Date expiresDate) {
		this.expiresDate = expiresDate;
	}
}
