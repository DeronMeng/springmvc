package com.qb.china.wechat;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.security.MessageDigest;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;

import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.qb.china.domain.JsSDKConfig;
import com.qb.china.util.AppConfig;
import com.qb.china.util.GetWxOrderno;
import com.qb.china.util.JSONUtils;
import com.qb.china.util.Sha1Util;
import com.qb.china.util.Constans.WX;
import com.qb.china.util.MD5Util;
import com.qb.china.util.http.HttpClientConnectionManager;

/**
 * 微信开发工具类
 * Created by Long.Meng on 2015-10-29.
 */
@Component
public class WxDevUtils {
	private static final Logger logger = Logger.getLogger(WxDevUtils.class);

	public static final String GET_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}";
	public static final String GET_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token={0}&type=jsapi";
	public static final String GET_USERINFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token={0}&openid={1}&lang={2}";
	public static final String GET_OAUTH_USER_AGREE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid={0}&redirect_uri={1}&response_type=code&scope={2}&state=1#wechat_redirect";
	public static final String REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid={0}&grant_type=refresh_token&refresh_token={1}";
	public static final String GET_OAUTH_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={0}&secret={1}&code={2}&grant_type=authorization_code";
	public static final String CHECK_OAUTH_TOKEN_IS_VALID = "https://api.weixin.qq.com/sns/auth?access_token={0}&openid={1}";

	private static final String CREATE_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";// 统一下单接口

	private static final String CACHE_KEY_WECHAT_TOKEN = "WECHAT:ACCESS_TOKEN";
	private static final String CACHE_KEY_WECHAT_TICKET = "WECHAT:ACCESS_TICKET";

	// 支付相关参数
	public static final String WECHAT_APP_ID = AppConfig.getProperty("wechat.appid");
	private static final String WECHAT_APP_SECRET = AppConfig.getProperty("wechat.appsecret");
	public static final String WECHAT_MERCHANT = AppConfig.getProperty("wx.merchant");
	private static final String WECHAT_PARTNERKEY = AppConfig.getProperty("wechat.partnerkey");
	
	// 分享相关参数
	public static final String SHARE_WECHAT_TOKEN = AppConfig.getProperty("wechat_token");
	private static final String SHARE_WECHAT_APP_ID = AppConfig.getProperty("wechat_appid");
	private static final String SHARE_WECHAT_APP_SECRET = AppConfig.getProperty("wechat_appsecret");

	public static final String WECHAT_MOBILE_APP_ID = AppConfig.getProperty("wechat.mobile.appid");
	public static final String WECHAT_MOBILE_APP_SECRET = AppConfig.getProperty("wechat.mobile.appsecret");
	public static final String WECHAT_MOBILE_MERCHANT = AppConfig.getProperty("wx.mobile.merchant");

	private static final int BLACK = 0xff000000;
	private static final int WHITE = 0xFFFFFFFF;

	private WxCache cacheService = WxCache.getInstance();

	/**
	 * 根据APPID、APPSECRET 获取accessToken
	 * 因微信的接口有请求频次限制，需全局缓存accessToken
	 */
	public String getAccessToken() throws Exception {
		Date curretDate = new Date();
		String accessToken = null;
		WxAccessToken cacheAccessToken = null;
		if (cacheService.existsKey(CACHE_KEY_WECHAT_TOKEN)) {
			cacheAccessToken = (WxAccessToken) cacheService.getCacheMap(CACHE_KEY_WECHAT_TOKEN);
			accessToken = cacheAccessToken.getAccessToken();
		}

		if (StringUtils.isBlank(accessToken) || (cacheAccessToken != null && curretDate.after(cacheAccessToken.getExpiresDate()))) {
			cacheService.remove(CACHE_KEY_WECHAT_TOKEN);
			String tokenUrl = MessageFormat.format(GET_TOKEN_URL, SHARE_WECHAT_APP_ID, SHARE_WECHAT_APP_SECRET);
			String response = this.sendRequest(tokenUrl);
			WxAccessToken wxAccessToken = JSONUtils.jsonToObj(response, WxAccessToken.class);
			if (wxAccessToken != null && StringUtils.isNotBlank(wxAccessToken.getAccessToken())) {
				accessToken = wxAccessToken.getAccessToken();
				wxAccessToken.setExpiresDate(new Date(System.currentTimeMillis() + 120 * 60 * 1000));
				cacheService.setCacheMap(CACHE_KEY_WECHAT_TOKEN, wxAccessToken);
			} else {
				throw new RuntimeException("Failed get the accessToken.");
			}
		}
		return accessToken;
	}

	/**
	 * 根据accessToken获取 ticket
	 * 因微信的接口有请求频次限制，需全局缓存ticket
	 */
	public String getTicket() throws Exception {
		Date curretDate = new Date();
		String ticket = null;
		WxTicket cacheTicket = null;
		if (cacheService.existsKey(CACHE_KEY_WECHAT_TICKET)) {
			cacheTicket = (WxTicket) cacheService.getCacheMap(CACHE_KEY_WECHAT_TICKET);
			ticket = cacheTicket.getTicket();
		}

		if (StringUtils.isBlank(ticket) || (cacheTicket != null && curretDate.after(cacheTicket.getExpiresDate()))) {
			cacheService.remove(CACHE_KEY_WECHAT_TICKET);
			String token = this.getAccessToken();
			logger.info("=====================token: " + token);
			String ticketUrl = MessageFormat.format(GET_TICKET_URL, token);
			String response = this.sendRequest(ticketUrl);
			WxTicket wxTicket = JSONUtils.jsonToObj(response, WxTicket.class);
			if (wxTicket != null && StringUtils.isNotBlank(wxTicket.getTicket())) {
				ticket = wxTicket.getTicket();
				wxTicket.setExpiresDate(new Date(System.currentTimeMillis() + 120 * 60 * 1000));
				cacheService.setCacheMap(CACHE_KEY_WECHAT_TICKET, wxTicket);
			} else {
				throw new RuntimeException("Failed get the access ticket.");
			}
		}
		return ticket;
	}

	/**
	 * 根据accessToken openid 获取用户基本信息
	 */
	public WxUserinfo getUserInfo(String openid, String language) throws Exception {
		String token = this.getAccessToken();
		String userInfoUrl = MessageFormat.format(GET_USERINFO_URL, token, openid, language);
		String response = this.sendRequest(userInfoUrl);
		WxUserinfo wxUserinfo = JSONUtils.jsonToObj(response, WxUserinfo.class);
		return wxUserinfo;
	}

	/**
	 * 得到授权地址 scope snsapi_userinfo | snsapi_base
	 */
	public String getAuthrUrl(String redirectUri, String scope) throws Exception {
		String getCodeUrl = MessageFormat.format(GET_OAUTH_USER_AGREE_URL, SHARE_WECHAT_APP_ID,
				java.net.URLEncoder.encode(redirectUri, "utf-8"), scope);
		return getCodeUrl;
	}

	/**
	 * 刷新access_token(如果需要) refreshToken
	 */
	public JSONObject refreshToken(String refreshToken) throws Exception {
		String refreshTokenUrl = MessageFormat.format(REFRESH_TOKEN_URL, SHARE_WECHAT_APP_ID, refreshToken);
		String response = this.sendRequest(refreshTokenUrl);
		return JSONObject.parseObject(response);
	}

	/**
	 * 通过code换取网页授权access_token
	 */
	public JSONObject oauth(String code) throws Exception {
		String oauthUrl = MessageFormat.format(GET_OAUTH_URL, SHARE_WECHAT_APP_ID, SHARE_WECHAT_APP_SECRET, code);
		String response = this.sendRequest(oauthUrl);
		return JSONObject.parseObject(response);
	}

	/**
	 * 检查授权凭证是否有效 accessToken
	 */
	public JSONObject checkTokenIsValid(String accessToken, String openId) throws Exception {
		String checkTokenUrl = MessageFormat.format(CHECK_OAUTH_TOKEN_IS_VALID, accessToken, openId);
		String response = this.sendRequest(checkTokenUrl);
		return JSONObject.parseObject(response);
	}

	/**
	 * 通过后台获取openId
	 */
	public String getOpenId(String code) throws Exception {
		String openId = "";
		JSONObject jsonObject = oauth(code);
		if (null != jsonObject) {
			openId = jsonObject.getString("openid");
		}
		return openId;
	}

	/**
	 * 获取Mobile APP统一下单结果
	 * @param notifyUrl TODO
	 */
	public static Map<String, String> buildMobileAppPayParams(WxPay wxPay, String notifyUrl) {
		Map<String, String> result = new HashMap<String, String>();
		String orderId = wxPay.getOrderId();
		String attach = "Memorieslab";
		String totalFee = getMoney(wxPay.getTotalFee());
		String spbill_create_ip = wxPay.getSpbillCreateIp();
		String trade_type = "APP";
		String appId = WECHAT_MOBILE_APP_ID;
		String appSecret = WECHAT_MOBILE_APP_SECRET;
		String mch_id = WECHAT_MOBILE_MERCHANT;
		String nonce_str = getNonceStr();
		String out_trade_no = orderId;
		String body = wxPay.getBody();
		if (body != null) {
			if (StringUtils.isNotBlank(body) && body.length() > 20) {
				body = body.substring(0, 20) + "...";
			} else {
				body = body.substring(0, body.length()) + "...";
			}
		}

		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appId);
		packageParams.put("attach", attach);
		packageParams.put("body", body);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("notify_url", notifyUrl);
		packageParams.put("out_trade_no", out_trade_no);
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("total_fee", totalFee);
		packageParams.put("trade_type", trade_type);

		String prePaySign = buildParamsSign(packageParams, appSecret);

		String xml = "<xml>" + "<appid>" + appId + "</appid>" + "<mch_id>" + mch_id + "</mch_id>" + "<nonce_str>"
				+ nonce_str + "</nonce_str>" + "<sign>" + prePaySign + "</sign>" + "<body><![CDATA[" + body + "]]></body>"
				+ "<out_trade_no>" + out_trade_no + "</out_trade_no>" + "<attach>" + attach + "</attach>"
				+ "<total_fee>" + totalFee + "</total_fee>" + "<spbill_create_ip>" + spbill_create_ip
				+ "</spbill_create_ip>" + "<notify_url>" + notifyUrl + "</notify_url>" + "<trade_type>" + trade_type
				+ "</trade_type>" + "</xml>";

		Map<String, String> prepayResult = GetWxOrderno.getPrepayResult(CREATE_ORDER_URL, xml);
		if(! prepayResult.isEmpty()){
			TreeMap<String, String> payParams = new TreeMap<String, String>();
			payParams.put("appid",prepayResult.get("appid"));
			payParams.put("partnerid",prepayResult.get("mch_id"));
			payParams.put("prepayid",prepayResult.get("prepay_id"));
			payParams.put("package","Sign=WXPay");
			payParams.put("noncestr",prepayResult.get("nonce_str"));
			payParams.put("timestamp",getTimestamp());
			
			String paySign = buildParamsSign(payParams, appSecret);
			payParams.put("sign", paySign);
			result = payParams;
		}
		return result;
	}
	
	public static String getTimestamp(){
		return String.valueOf(System.currentTimeMillis()).substring(0, 10);
	}

	public static String buildParamsSign(SortedMap<String, String> packageParams, String appSecret) {
		StringBuffer sb = new StringBuffer();
		Set es = packageParams.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + appSecret);
		String sign = MD5Util.MD5Encode(sb.toString(), "UTF-8").toUpperCase();
		return sign;
	}

	/**
	 * 获取微信扫码支付二维码链接
	 * @param notifyUrl TODO
	 */
	public static String getCodeurl(WxPay wxPay, String notifyUrl) {
		String orderId = wxPay.getOrderId();
		// 附加数据 原样返回
		String attach = "Memorieslab";
		// 总金额以分为单位，不带小数点
		String totalFee = getMoney(wxPay.getTotalFee());
		String spbill_create_ip = wxPay.getSpbillCreateIp();
		String trade_type = "NATIVE";
		String mch_id = WECHAT_MERCHANT;
		String nonce_str = getNonceStr();
		String out_trade_no = orderId;
		String body = wxPay.getBody();
		if (body != null) {
			if (StringUtils.isNotBlank(body) && body.length() > 20) {
				body = body.substring(0, 20) + "...";
			} else {
				body = body.substring(0, body.length()) + "...";
			}
		}

		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", WECHAT_APP_ID);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("body", body);
		packageParams.put("attach", attach);
		packageParams.put("out_trade_no", out_trade_no);

		packageParams.put("total_fee", totalFee);
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("notify_url", notifyUrl);
		packageParams.put("trade_type", trade_type);

		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(WECHAT_APP_ID, WECHAT_APP_SECRET, WECHAT_PARTNERKEY);

		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>" + "<appid>" + WECHAT_APP_ID + "</appid>" + "<mch_id>" + mch_id + "</mch_id>"
				+ "<nonce_str>" + nonce_str + "</nonce_str>" + "<sign>" + sign + "</sign>" + "<body><![CDATA[" + body
				+ "]]></body>" + "<out_trade_no>" + out_trade_no + "</out_trade_no>" + "<attach>" + attach
				+ "</attach>" + "<total_fee>" + totalFee + "</total_fee>" + "<spbill_create_ip>" + spbill_create_ip
				+ "</spbill_create_ip>" + "<notify_url>" + notifyUrl + "</notify_url>" + "<trade_type>" + trade_type
				+ "</trade_type>" + "</xml>";
		String code_url = "";

		code_url = GetWxOrderno.getCodeUrl(CREATE_ORDER_URL, xml);
		System.out.println("code_url----------------" + code_url);

		return code_url;
	}

	/**
	 * 获取请求预支付id报文
	 * @param notifyUrl TODO
	 */
	public static String getPackage(WxPay tpWxPay, String notifyUrl) {
		String openId = tpWxPay.getOpenId();
		String orderId = tpWxPay.getOrderId();
		String attach = "Memorieslab";
		String totalFee = getMoney(tpWxPay.getTotalFee());
		String spbill_create_ip = tpWxPay.getSpbillCreateIp();
		String trade_type = "JSAPI";
		String mch_id = WECHAT_MERCHANT;
		String nonce_str = getNonceStr();
		String out_trade_no = orderId;
		String body = tpWxPay.getBody();
		if (body != null) {
			if (StringUtils.isNotBlank(body) && body.length() > 20) {
				body = body.substring(0, 20) + "...";
			} else {
				body = body.substring(0, body.length()) + "...";
			}
		}

		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", WECHAT_APP_ID);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("body", body);
		packageParams.put("attach", attach);
		packageParams.put("out_trade_no", out_trade_no);
		packageParams.put("total_fee", totalFee);
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("notify_url", notifyUrl);
		packageParams.put("trade_type", trade_type);
		packageParams.put("openid", openId);

		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(WECHAT_APP_ID, WECHAT_APP_SECRET, WECHAT_PARTNERKEY);

		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>" + "<appid>" + WECHAT_APP_ID + "</appid>" + "<mch_id>" + mch_id + "</mch_id>"
				+ "<nonce_str>" + nonce_str + "</nonce_str>" + "<sign>" + sign + "</sign>" + "<body><![CDATA[" + body
				+ "]]></body>" + "<out_trade_no>" + out_trade_no + "</out_trade_no>" + "<attach>" + attach
				+ "</attach>" + "<total_fee>" + totalFee + "</total_fee>" + "<spbill_create_ip>" + spbill_create_ip
				+ "</spbill_create_ip>" + "<notify_url>" + notifyUrl + "</notify_url>" + "<trade_type>" + trade_type
				+ "</trade_type>" + "<openid>" + openId + "</openid>" + "</xml>";
		String prepay_id = "";

		prepay_id = GetWxOrderno.getPayNo(CREATE_ORDER_URL, xml);
		System.out.println("获取到的预支付ID：" + prepay_id);

		//获取prepay_id后，拼接最后请求支付所需要的package
		SortedMap<String, String> finalpackage = new TreeMap<String, String>();
		String timestamp = Sha1Util.getTimeStamp();
		String packages = "prepay_id=" + prepay_id;
		finalpackage.put("appId", WECHAT_APP_ID);
		finalpackage.put("timeStamp", timestamp);
		finalpackage.put("nonceStr", nonce_str);
		finalpackage.put("package", packages);
		finalpackage.put("signType", "MD5");
		String finalsign = reqHandler.createSign(finalpackage);

		String finaPackage = "\"appId\":\"" + WECHAT_APP_ID + "\",\"timeStamp\":\"" + timestamp + "\",\"nonceStr\":\""
				+ nonce_str + "\",\"package\":\"" + packages + "\",\"signType\" : \"MD5" + "\",\"paySign\":\""
				+ finalsign + "\"";

		System.out.println("V3 jsApi package:" + finaPackage);
		return finaPackage;
	}

	/**
	 * wap支付 获取预支付id 此方式未开通
	 * @param notifyUrl TODO
	 */
	public String getWapPayUrl(WxPay tpWxPay, String notifyUrl) throws UnsupportedEncodingException {
		String orderId = tpWxPay.getOrderId();
		String attach = "Memorieslab";
		String totalFee = getMoney(tpWxPay.getTotalFee());
		String spbill_create_ip = tpWxPay.getSpbillCreateIp();
		String trade_type = "WAP";
		String mch_id = WECHAT_MERCHANT;
		String nonce_str = getNonceStr();
		String out_trade_no = orderId;
		String body = tpWxPay.getBody();
		if (body != null) {
			if (StringUtils.isNotBlank(body) && body.length() > 20) {
				body = body.substring(0, 20) + "...";
			} else {
				body = body.substring(0, body.length()) + "...";
			}
		}

		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", WECHAT_APP_ID);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("body", body);
		packageParams.put("attach", attach);
		packageParams.put("out_trade_no", out_trade_no);
		packageParams.put("total_fee", totalFee);
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("notify_url", notifyUrl);
		packageParams.put("trade_type", trade_type);

		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(WECHAT_APP_ID, WECHAT_APP_SECRET, WECHAT_PARTNERKEY);

		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>" + "<appid>" + WECHAT_APP_ID + "</appid>" + "<mch_id>" + mch_id + "</mch_id>"
				+ "<nonce_str>" + nonce_str + "</nonce_str>" + "<sign>" + sign + "</sign>" + "<body><![CDATA[" + body
				+ "]]></body>" + "<out_trade_no>" + out_trade_no + "</out_trade_no>" + "<attach>" + attach
				+ "</attach>" + "<total_fee>" + totalFee + "</total_fee>" + "<spbill_create_ip>" + spbill_create_ip
				+ "</spbill_create_ip>" + "<notify_url>" + notifyUrl + "</notify_url>" + "<trade_type>" + trade_type
				+ "</trade_type>" + "</xml>";
		String prepay_id = "";

		prepay_id = GetWxOrderno.getPayNo(CREATE_ORDER_URL, xml);

		System.out.println("获取到的预支付ID：" + prepay_id);

		SortedMap<String, String> finalpackage = new TreeMap<String, String>();
		String timestamp = Sha1Util.getTimeStamp();
		finalpackage.put("appId", WECHAT_APP_ID);
		finalpackage.put("timeStamp", timestamp);
		finalpackage.put("nonceStr", nonce_str);
		finalpackage.put("package", "WAP");
		finalpackage.put("prepayid", prepay_id);
		String finalsign = reqHandler.createSign(finalpackage);

		/*String finaPackage = "\"appId\":\"" + WECHAT_APP_ID + "\",\"timeStamp\":\"" + timestamp
				+ "\",\"nonceStr\":\"" + nonce_str + "\",\"package\":\"WAP"+",\"prepayid\" : "+prepay_id + "\",\"paySign\":\""
				+ finalsign + "\"";*/

		//System.out.println("Beta1 Wap package:"+finaPackage);
		String code_url = "";
		code_url = "appid=" + WECHAT_APP_ID + "&noncestr=" + nonce_str
				+ "&package=WAP&prepayid=wx20160106133340da2d00e8b90107681380&timestamp=" + timestamp + "&sign="
				+ finalsign;
		code_url = "weixin://wap/pay?" + java.net.URLEncoder.encode(code_url, "utf-8");
		System.out.println("code_url----------------" + code_url);
		return code_url;
	}

	@SuppressWarnings({ "rawtypes", "static-access" })
	public Map orderQuery(String outTradeNo) {
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		String appId = WECHAT_APP_ID;
		String mchId = WECHAT_MERCHANT;
		// 随机字符串
		String nonce_str = getNonceStr();
		packageParams.put("appid", appId);
		packageParams.put("mch_id", mchId);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("out_trade_no", outTradeNo);

		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(appId, WECHAT_APP_SECRET, WECHAT_PARTNERKEY);

		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>" + "<appid>" + appId + "</appid>" + "<mch_id>" + mchId + "</mch_id>" + "<nonce_str>"
				+ nonce_str + "</nonce_str>" + "<sign>" + sign + "</sign>" + "<out_trade_no>" + outTradeNo
				+ "</out_trade_no>" + "</xml>";
		String orderQueryUrl = "https://api.mch.weixin.qq.com/pay/orderquery";
		Map map = new GetWxOrderno().getTradeState(orderQueryUrl, xml);
		System.out.println("=======================tradeState -> " + (map != null ? map.get("trade_state") : ""));
		return map;
	}

	/**
	 * 获取随机字符串
	 * @return
	 */
	public static String getNonceStr() {
		// 随机数
		String currTime = TenpayUtil.getCurrTime();
		// 8位日期
		String strTime = currTime.substring(8, currTime.length());
		// 四位随机数
		String strRandom = TenpayUtil.buildRandom(4) + "";
		// 10位序列号,可以自行调整。
		return strTime + strRandom;
	}

	/**
	 * 生成二维码图片 不存储 直接以流的形式输出到页面
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void encodeQrcode(String content, HttpServletResponse response) throws IOException {
		if (StringUtils.isBlank(content))
			return;
		//MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		Map hints = new HashMap();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // 设置字符集编码类型
		BitMatrix bitMatrix = null;
		ServletOutputStream stream = null;
		try {
			int width = 200;
			int height = 200;
			stream = response.getOutputStream();
			QRCodeWriter writer = new QRCodeWriter();
			bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height);
			//bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 300, 300, hints);
			BufferedImage image = toBufferedImage(bitMatrix);
			// 输出二维码图片流
			try {
				ImageIO.write(image, "png", stream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (stream != null) {
				stream.flush();
				stream.close();
			}
		}
	}

	/**
	 * 生成二维码内容
	 */
	public static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) == true ? BLACK : WHITE);
			}
		}
		return image;
	}

	/**
	 * 元转换成分
	 */
	public static String getMoney(String amount) {
		if (amount == null) {
			return "";
		}
		// 金额转化为分为单位
		String currency = amount.replaceAll("\\$|\\￥|\\,", ""); //处理包含, ￥ 或者$的金额  
		int index = currency.indexOf(".");
		int length = currency.length();
		Long amLong = 0l;
		if (index == -1) {
			amLong = Long.valueOf(currency + "00");
		} else if (length - index >= 3) {
			amLong = Long.valueOf((currency.substring(0, index + 3)).replace(".", ""));
		} else if (length - index == 2) {
			amLong = Long.valueOf((currency.substring(0, index + 2)).replace(".", "") + 0);
		} else {
			amLong = Long.valueOf((currency.substring(0, index + 1)).replace(".", "") + "00");
		}
		return amLong.toString();
	}

	@SuppressWarnings("rawtypes")
	public static String localIp() {
		String ip = null;
		Enumeration allNetInterfaces;
		try {
			allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
				List<InterfaceAddress> InterfaceAddress = netInterface.getInterfaceAddresses();
				for (InterfaceAddress add : InterfaceAddress) {
					InetAddress Ip = add.getAddress();
					if (Ip != null && Ip instanceof Inet4Address) {
						ip = Ip.getHostAddress();
					}
				}
			}
		} catch (SocketException e) {
			logger.warn("获取本机Ip失败 -> 异常信息:" + e.getMessage());
		}
		return ip;
	}

	// 解析微信通知xml
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map parseXmlToList2(String xml) {
		Map retMap = new HashMap();
		try {
			StringReader read = new StringReader(xml);
			// 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
			InputSource source = new InputSource(read);
			// 创建一个新的SAXBuilder
			SAXBuilder sb = new SAXBuilder();
			// 通过输入源构造一个Document
			Document doc = (Document) sb.build(source);
			Element root = doc.getRootElement();// 指向根节点
			List<Element> es = root.getChildren();
			if (es != null && es.size() != 0) {
				for (Element element : es) {
					retMap.put(element.getName(), element.getValue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retMap;
	}

	public Map<String, String> getWxConfig(String url) throws Exception {
		if (StringUtils.isBlank(url)) {
			throw new RuntimeException("url parameters can not be blank.");
		}
		Map<String, String> map = new HashMap<String, String>();
		String ticket = getTicket();
		String nonce_str = createNonceStr();
		String timestamp = createTimestamp();
		String string1;
		String signature = "";

		// 参数名必须全部小写，且必须有序
		string1 = "jsapi_ticket=" + ticket + "&noncestr=" + nonce_str + "&timestamp=" + timestamp + "&url=" + url;
		logger.info(string1);

		MessageDigest crypt = MessageDigest.getInstance("SHA-1");
		crypt.reset();
		crypt.update(string1.getBytes("UTF-8"));
		signature = byteToHex(crypt.digest());

		map.put("appId", SHARE_WECHAT_APP_ID);
		map.put("timestamp", timestamp);
		map.put("nonceStr", nonce_str);
		map.put("signature", signature);
		map.put("jsApiList", JsSDKConfig.JS_API);
		return map;
	}

	private String sendRequest(String url) throws Exception {
		Objects.requireNonNull(url);
		String response = "";
		InputStream inPutStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL urlGet = new URL(url);
			urlConnection = (HttpURLConnection) urlGet.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
			System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
			logger.info("Request URL: " + url);

			urlConnection.connect();
			inPutStream = urlConnection.getInputStream();

			int size = inPutStream.available();
			byte[] jsonBytes = new byte[size];
			inPutStream.read(jsonBytes);
			response = new String(jsonBytes, "UTF-8");
			logger.info("Response string: " + response);

		} finally {
			IOUtils.closeQuietly(inPutStream);
			if (urlConnection != null)
				urlConnection.disconnect();
		}
		return response;
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	/**
	 * 生成随机字符串
	 */
	private static String createNonceStr() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 时间戳
	 */
	private static String createTimestamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}
}
